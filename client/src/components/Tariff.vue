<template>
  <div>
    <div class="tariff-btns">
      <md-button class="md-accent">Delete</md-button>
      <md-button class="md-primary">Save</md-button>
    </div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <md-field>
          <label for="countries">Country</label>
          <md-select v-model="countryCode">
            <md-option
              v-for="country in countries"
              v-bind:key="country.code"
              v-bind:value="country.code"
            >{{country.name}}</md-option>
          </md-select>
        </md-field>
        <md-field>
          <label>Tariff Line</label>
          <md-input v-model="tariffLine"></md-input>
        </md-field>
        <md-field>
          <label>Base Rate</label>
          <md-input v-model="baseRate"></md-input>
        </md-field>
        <md-field>
          <label>Sector Code</label>
          <md-input v-model="sectorCode"></md-input>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-autocomplete
          v-model="stagingBasket"
          :md-options="stagingBaskets"
          md-layout="box"
          md-dense
        >
          <label>Staging Basket</label>
        </md-autocomplete>
        <md-field>
          <label>Partner Start Year</label>
          <md-input v-model="partnerStartYear" type="number"></md-input>
        </md-field>
        <md-field>
          <label>Partner</label>
          <md-input v-model="partnerName"></md-input>
        </md-field>
        <md-field>
          <label>Final Year</label>
          <md-input v-model="finalYear" type="number"></md-input>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-autocomplete v-model="productType" :md-options="productTypes" md-layout="box" md-dense>
          <label>Product Types</label>
        </md-autocomplete>
        <md-field>
          <label>Reporter Start Year</label>
          <md-input v-model="reporterStartYear" type="number"></md-input>
        </md-field>
        <md-field>
          <label>Reporter</label>
          <md-input v-model="reporterName"></md-input>
        </md-field>
        <md-field>
          <label>Quota Name</label>
          <md-input v-model="quotaName"></md-input>
        </md-field>
        <md-field>
          <label>Tariff Rate Quota</label>
          <md-input v-model="tariffRateQuota"></md-input>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-field>
          <label>Description</label>
          <md-textarea v-model="description"></md-textarea>
        </md-field>
        <md-field>
          <label>Rule Text</label>
          <md-textarea v-model="ruleText"></md-textarea>
        </md-field>
        <md-field>
          <label>Tariff Rate Quota Notes</label>
          <md-textarea v-model="tariffRateQuotaNotes"></md-textarea>
        </md-field>
      </div>
    </div>
  </div>
</template>
<style>
.tariff-btns {
  display: flex;
  justify-content: flex-end;
}
</style>
<script>
export default {
  name: "Tariff",
  props: {
    id: String,
    tariffRepository: Object
  },
  async created() {
    this.countries = await this.tariffRepository._getCountries();

    let stagingBaskets = await this.tariffRepository._getAllStagingBaskets();
    this.stagingBaskets = stagingBaskets.map(sb => sb.description);

    let productTypes = await this.tariffRepository._getAllProductTypes();
    this.productTypes = productTypes.map(pt => pt.description);

    let tariff = await this.tariffRepository._getTariff(this.id);
    this.tariffLine = tariff.tariffLine;
    this.description = tariff.description;
    this.finalYear = tariff.finalYear;
    this.partnerName = tariff.partnerName;
    this.reporterName = tariff.reporterName;
    this.partnerStartYear = tariff.partnerStartYear;
    this.reporterStartYear = tariff.reporterStartYear;
    this.baseRate = tariff.baseRate;
    this.sectorCode = tariff.sectorCode;
    this.ruleText = tariff.ruleText;
    this.countryCode = tariff.country.code;
    this.stagingBasket = tariff.stagingBasket.description;
    this.productType = tariff.productType.description;
    this.quotaName = tariff.quotaName;
    this.tariffRateQuota = tariff.tariffRateQuota;
    this.tariffRateQuotaNotes = tariff.tariffRateQuotaNotes;
  },
  data() {
    return {
      countries: [],
      stagingBaskets: [],
      productTypes: [],
      tariffLine: null,
      description: null,
      finalYear: null,
      partnerStartYear: null,
      reporterStartYear: null,
      partnerName: null,
      reporterName: null,
      baseRate: null,
      sectorCode: null,
      ruleText: null,
      countryCode: null,
      stagingBasket: null,
      productType: null,
      tariffRateQuota: null,
      tariffRateQuotaNotes: null,
      quotaName: null,
    };
  }
};
</script>