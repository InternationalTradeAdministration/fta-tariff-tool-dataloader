<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-10">
        <md-button class="md-icon-button" @click="goToTariffUpload()">
          <md-icon class="fa fa-angle-double-left"></md-icon>
        </md-button>
      </div>
      <div class="md-layout-item md-size-90">
        <md-button class="md-primary top-btn" @click="saveCountries()">Save</md-button>
      </div>
    </div>
    <div class="md-layout md-gutter">
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
          <div class="layout-item ">
            <md-switch v-model="country.visible"></md-switch>
          </div>
        </li>
      </md-list>
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
export default {
  name: "Config",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.countries = await this.tariffRepository._getCountries();
  },
  data() {
    return {
      countries: []
    };
  },
  methods: {
    goToTariffUpload() {
      this.$router.push({ name: "TariffsUpload" });
    },
    async saveCountries() {
      await this.tariffRepository._saveCountries(this.countries);
    }
  }
};
</script>
