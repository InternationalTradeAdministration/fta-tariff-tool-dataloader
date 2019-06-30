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
    {{fileContents}}
  </div>
</template>

<script>
import { readUploadedFileAsText } from "./FileHelper";

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
      fileContents: null,
      fileBlob: null
    };
  },
  methods: {
    onFileUpload(event) {
      this.fileBlob = event[0];
    },
    async uploadFile() {
      let fileContents = await readUploadedFileAsText(this.fileBlob);
      this.fileContents = fileContents;
    },
    goToTariffsList() {
      this.$router.push({ name: "tariffsList" });
    }
  }
};
</script>