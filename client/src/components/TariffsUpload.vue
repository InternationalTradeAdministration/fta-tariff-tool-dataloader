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
        <md-button class="md-secondary top-btn" v-bind:href="'/api/tariff/log?countryCode='+countryCode" target="_blank">Log</md-button>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button class="md-secondary top-btn" v-bind:href="'/api/tariff/download?countryCode='+countryCode" download>Download</md-button>
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