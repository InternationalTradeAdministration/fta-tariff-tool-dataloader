<template>
  <div>
    <tariff-toolbar />
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-5">
        <md-button class="md-icon-button config-btn" @click="goToConfig()">
          <md-icon class="fa fa-cog"></md-icon>
        </md-button>
      </div>
      <div class="md-layout-item md-size-20">
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
      <div class="md-layout-item md-size-30">
        <md-field>
          <label>Upload .xlsx or .csv</label>
          <md-file v-model="fileName" @md-change="onFileSelection($event)"></md-file>
        </md-field>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button class="md-secondary md-raised top-btn" @click="uploadFile()">Upload</md-button>
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
          v-bind:href="'/api/tariff/download/csv?countryCode='+countryCode"
          v-bind:alt="'/api/tariff/download/csv?countryCode='+countryCode"
          v-bind:title="'/api/tariff/download/csv?countryCode='+countryCode"
          download
        >CSV</md-button>
      </div>
      <div class="md-layout-item md-size-10">
        <md-button
          class="md-secondary top-btn"
          v-bind:href="'/api/tariff/download/json?countryCode='+countryCode"
          v-bind:alt="'/api/tariff/download/json?countryCode='+countryCode"
          v-bind:title="'/api/tariff/download/json?countryCode='+countryCode"
          download
        >JSON</md-button>
      </div>
    </div>
    <div v-if="loading" class="loading">loading...</div>
    <div class="user-feedback">
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
  </div>
</template>
<style>
.user-feedback {
  display: flex;
  justify-content: center;
}
.error {
  color: red;
}
.success {
  color: green;
}
</style>
<script>
import { readUploadedFileAsArrayBuffer } from "./FileHelper";
import { TariffHeadersValidator } from "./TariffHeadersValidator";
import TariffHeader from "./TariffHeader";
import { read, utils } from "xlsx";

export default {
  name: "TariffsUpload",
  props: {
    tariffRepository: Object
  },
  components: {
    "tariff-toolbar": TariffHeader
  },
  async created() {
    this.loading = true;
    this.countryOptions = await this.tariffRepository._getCountries();
    this.countryOptions = this.countryOptions.filter(c => c.visible);
    this.countryCode = this.countryOptions[0].code;
    this.loading = false;
  },
  data() {
    return {
      countryCode: null,
      countryOptions: [],
      fileName: null,
      fileType: null,
      errorOccured: false,
      errorMessages: [],
      uploadSuccessful: false,
      uploading: false,
      fileBlob: null,
      loading: true
    };
  },
  methods: {
    onFileSelection(event) {
      this.errorMessages = [];
      this.uploadSuccessful = false;
      this.fileType = null;
      if (event[0].name.endsWith(".xlsx")) {
        this.fileType = "xlsx";
        this.fileBlob = event[0];
      } else if (event[0].name.endsWith(".csv")) {
        this.fileType = "csv";
        this.fileBlob = event[0];
      } else {
        this.fileType = null;
        this.errorOccured = true;
      }
    },
    async uploadFile() {
      this.uploading = true;
      this.errorMessages = [];
      if (!this.fileType) {
        this.setErrorState([
          "Please select a .xlsx or .csv file to be uploaded."
        ]);
        return;
      }

      let csv = null;
      var headers = [];
      let fileArrayBuffer = await readUploadedFileAsArrayBuffer(this.fileBlob);
      if (this.fileType === "xlsx") {
        let workbook = read(new Uint8Array(fileArrayBuffer), { type: "array" });
        let workSheetName = workbook.SheetNames[0];
        let workSheet = workbook.Sheets[workSheetName];
        var range = utils.decode_range(workSheet["!ref"]);
        for (var colNum = range.s.c; colNum < range.e.c; colNum++) {
          const cell = workSheet[utils.encode_cell({ r: 0, c: colNum })];
          if (!cell) {
            headers.push(null);
            continue;
          }
          headers.push(cell.v);
        }

        const tariffHeadersValidator = new TariffHeadersValidator(headers);
        const validationResults = tariffHeadersValidator._validate();
        if (!validationResults.valid) {
          this.setErrorState(validationResults.errorMessages);
          return;
        }
        csv = utils.sheet_to_csv(workSheet);
      }

      if (this.fileType === "csv") {
        csv = new TextDecoder("utf-8").decode(new Uint8Array(fileArrayBuffer));
        headers = csv
          .substring(0, csv.indexOf("\n"))
          .replace(/"/g, "")
          .split(",");
        const tariffHeadersValidator = new TariffHeadersValidator(headers);
        const validationResults = tariffHeadersValidator._validate();
        if (!validationResults.valid) {
          this.setErrorState(validationResults.errorMessages);
          return;
        }
      }

      const message = await this.tariffRepository._saveTariffs(
        this.countryCode,
        csv
      );

      if (message == "success") {
        this.uploadSuccessful = true;
        this.errorOccured = false;
      } else {
        this.setErrorState([message]);
      }

      this.uploading = false;
    },
    setErrorState(errorMessages) {
      this.errorOccured = true;
      this.errorMessages = errorMessages;
      this.uploadSuccessful = false;
      this.uploading = false;
    },
    goToConfig() {
      this.$router.push({ name: "Config" });
    }
  }
};
</script>
