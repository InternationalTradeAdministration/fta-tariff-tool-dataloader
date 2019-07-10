<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-5">
        <md-button class="md-icon-button top-btn" @click="goToTariffsList()">
          <md-icon class="fa fa-angle-double-left"></md-icon>
        </md-button>
      </div>
      <div class="md-layout-item md-size-15">
        <md-field>
          <label>Country</label>
          <md-select v-model="countryCode">
            <md-option
              v-for="country in countryOptions"
              v-bind:key="country.code"
              v-bind:value="country.code"
            >{{country.name}}</md-option>
          </md-select>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-field>
          <label>Upload .xlsx</label>
          <md-file v-model="fileName" @md-change="onFileUpload($event)"></md-file>
        </md-field>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button class="md-primary top-btn" @click="uploadFile()">Upload</md-button>
      </div>
    </div>
    <div class="error" v-if="errorOccured">
      <ul>
        <li v-for="message in errorMessages" v-bind:key="message">{{message}}</li>
      </ul>
    </div>
    <div v-if="uploading">Uploading...</div>
    <div class="file-contents" v-if="!errorOccured">
      <div class="success">{{successMessage}}</div>
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
  name: "TariffsList",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.countryOptions = await this.tariffRepository._getCountries();
    this.countryCode = this.countryOptions[0].code;
  },
  data() {
    return {
      countryCode: null,
      countryOptions: [],
      fileName: null,
      isXlsxFile: true,
      errorOccured: false,
      errorMessages: [],
      successMessage: null,
      uploading: false,
      fileBlob: null
    };
  },
  methods: {
    onFileUpload(event) {
      this.errorMessages = [];
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
      if (!this.isXlsxFile) {
        this.errorOccured = true;
        this.errorMessages.push("Only .xlsx files may be uploaded.");
        this.uploading = false;
        return;
      }

      if (!this.fileBlob) {
        this.errorOccured = true;
        this.errorMessages.push("Please select a .xlsx file to upload.");
        this.uploading = false;
        return;
      }

      let fileArrayBuffer = await readUploadedFileAsArrayBuffer(this.fileBlob);
      let unit8Array = new Uint8Array(fileArrayBuffer);
      let workbook = read(unit8Array, { type: "array" });
      let workSheetName = workbook.SheetNames[0];
      let workSheet = workbook.Sheets[workSheetName];
      const tariffWorksheetValidator = new TariffWorksheetValidator(workSheet);
      const validationResults = tariffWorksheetValidator._validate();
      if (!validationResults.valid) {
        this.errorOccured = true;
        this.errorMessages = validationResults.errorMessages;
        this.uploading = false;
        return;
      }

      let csv = utils.sheet_to_csv(workSheet);
      const message = await this.tariffRepository._saveTariffs(
        this.countryCode,
        csv
      );
      if (message === "success") {
        this.errorOccured = false;
        this.successMessage = this.fileName + " was uploaded successfully!";
      } else {
        this.errorOccured = true;
        this.errorMessages.push(message);
      }

      this.uploading = false;
    },
    goToTariffsList() {
      this.$router.push({ name: "tariffsList" });
    }
  }
};
</script>