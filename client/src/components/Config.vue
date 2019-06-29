<template>
  <div>
    <md-field>
      <label>Upload</label>
      <md-file v-model="fileName" @md-change="onFileUpload($event)"></md-file>
    </md-field>
    {{fileName}}
    <br />
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
  },
  data() {
    return {
      countryOptions: [],
      fileName: null,
      fileContents: null
    };
  },
  methods: {
    async onFileUpload(event) {
      let fileContents = await readUploadedFileAsText(event[0]);
      this.fileContents = fileContents;
    }
  }
};
</script>