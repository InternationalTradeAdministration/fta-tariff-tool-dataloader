<template>
  <div>
    <tariff-toolbar />
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-5">
        <md-button class="md-icon-button" @click="goToTariffUpload()">
          <md-icon class="fa fa-angle-double-left"></md-icon>
        </md-button>
      </div>
      <md-list>
        <li v-for="country in countries" v-bind:key="country.code" v-bind:value="country.code">
          <div class="layout-item country-code">
            <md-field>
              <label>Code</label>
              <md-input v-model="country.code" disabled></md-input>
            </md-field>
          </div>
          <div class="layout-item">
            <md-field>
              <label>Country</label>
              <md-input v-model="country.name"></md-input>
            </md-field>
          </div>
          <div class="layout-item">
            <md-switch v-model="country.visible"></md-switch>
          </div>
        </li>
      </md-list>
      <div v-if="loading" class="loading">loading...</div>
    </div>
  </div>
</template>
<style>
.layout-item {
  display: inline-block;
  margin-right: 20px;
}
.country-code {
  padding-left: 20px;
  width: 50px;
}
</style>
<script>
import TariffHeader from "./TariffHeader";

export default {
  name: "Config",
  props: {
    tariffRepository: Object
  },
  components: {
    "tariff-toolbar": TariffHeader
  },
  async created() {
    this.loading = true;
    this.countries = await this.tariffRepository._getCountries();
    this.loading = false;
  },
  data() {
    return {
      countries: [],
      loading: true
    };
  },
  methods: {
    async goToTariffUpload() {
      await this.tariffRepository._saveCountries(this.countries);
      this.$router.push({ name: "TariffsUpload" });
    }
  }
};
</script>
