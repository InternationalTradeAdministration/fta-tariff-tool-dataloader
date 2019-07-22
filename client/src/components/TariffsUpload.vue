<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-15">
        <md-field>
          <label>Country</label>
          <md-select v-model="countryCode">
            <md-option
              v-for="country in countryOptions"
              v-bind:key="country.countryCode"
              v-bind:value="country.countryCode"
            >{{country.countryName}}</md-option>
          </md-select>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-field>
          <label>Upload .xlsx</label>
          <md-file v-model="fileName" @md-change="onFileSelection($event)"></md-file>
        </md-field>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button class="md-primary top-btn" @click="uploadFile()">Upload</md-button>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button
          class="md-secondary top-btn"
          v-bind:href="'/api/tariff/log?countryCode='+countryCode"
          target="_blank"
          v-bind:alt="'/api/tariff/log?countryCode='+countryCode"
          v-bind:title="'/api/tariff/log?countryCode='+countryCode"
        >Log</md-button>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button
          class="md-secondary top-btn"
          v-bind:href="'/api/tariff/download?countryCode='+countryCode"
          v-bind:alt="'/api/tariff/download?countryCode='+countryCode"
          v-bind:title="'/api/tariff/download?countryCode='+countryCode"
          download
        >Download</md-button>
      </div>
      <div class="md-layout-item md-size-5">
        <a
          href="https://github.com/InternationalTradeAdministration/ita-tariff-tool"
          alt="Git Hub Link"
          target="_blank"
        >
          <svg
            height="32"
            class="git-icon"
            viewBox="0 0 16 16"
            version="1.1"
            width="32"
            aria-hidden="true"
          >
            <path
              fill-rule="evenodd"
              d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"
            />
          </svg>
        </a>
      </div>
    </div>
    <div class="error" v-if="errorOccured">
      <ul>
        <li v-for="message in errorMessages" v-bind:key="message">{{message}}</li>
      </ul>
    </div>
    <div v-if="uploading">Uploading...</div>
    <div v-if="uploadSuccessful" class="success">
      <p>{{this.fileName}} was uploaded successfully!</p>
    </div>
  </div>
</template>
<style>
.error {
  color: red;
}
.success {
  color: green;
}
.git-icon {
  margin-top: 12px;
}
</style>

<script>
import { readUploadedFileAsArrayBuffer } from "./FileHelper";
import { TariffWorksheetValidator } from "./TariffWorksheetValidator";
import { read, utils } from "xlsx";

export default {
  name: "TariffsUpload",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.countryOptions = await this.tariffRepository._getCountries();
    this.countryCode = this.countryOptions[0].countryCode;
  },
  data() {
    return {
      countryCode: null,
      countryOptions: [],
      fileName: null,
      isXlsxFile: true,
      errorOccured: false,
      errorMessages: [],
      uploadSuccessful: false,
      uploading: false,
      fileBlob: null
    };
  },
  methods: {
    onFileSelection(event) {
      this.errorMessages = [];
      this.uploadSuccessful = false;
      if (event[0].name.endsWith(".xlsx")) {
        this.isXlsxFile = true;
        this.fileBlob = event[0];
      } else {
        this.errorOccured = true;
        this.isXlsxFile = false;
      }
    },
    async uploadFile() {
      this.uploading = true;
      this.errorMessages = [];
      if (!this.isXlsxFile) {
        this.errorOccured = true;
        this.errorMessages.push("Only .xlsx files may be uploaded.");
        this.uploadSuccessful = false;
        this.uploading = false;
        return;
      }

      if (!this.fileBlob) {
        this.errorOccured = true;
        this.errorMessages.push("Please select a .xlsx file to upload.");
        this.uploadSuccessful = false;
        this.uploading = false;
        return;
      }

      let fileArrayBuffer = await readUploadedFileAsArrayBuffer(this.fileBlob);
      let workbook = read(new Uint8Array(fileArrayBuffer), { type: "array" });
      let workSheetName = workbook.SheetNames[0];
      let workSheet = workbook.Sheets[workSheetName];
      const tariffWorksheetValidator = new TariffWorksheetValidator(workSheet);
      const validationResults = tariffWorksheetValidator._validate();
      if (!validationResults.valid) {
        this.errorOccured = true;
        this.errorMessages = validationResults.errorMessages;
        this.uploadSuccessful = false;
        this.uploading = false;
        return;
      }

      let csv = utils.sheet_to_csv(workSheet);
      const message = await this.tariffRepository._saveTariffs(
        this.countryCode,
        csv
      );

      if (message == "success") {
        this.uploadSuccessful = true;
        this.errorOccured = false;
      } else {
        this.errorOccured = true;
        this.uploadSuccessful = false;
        this.errorMessages.push(message);
      }

      this.uploading = false;
    },
    viewLog() {
      //"/api/tariff/log?countryCode=" + this.countryCode;
    },
    downloadFile() {
      // console.log("there");
      // this.$router.push("/api/tariff/download?countryCode=" + this.countryCode);
    }
  }
};
</script>