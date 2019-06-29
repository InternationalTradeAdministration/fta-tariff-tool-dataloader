<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-5">
        <md-button class="md-icon-button config-btn" @click="goToTariffsList()">
          <md-icon class="fa fa-angle-double-left"></md-icon>
        </md-button>
      </div>
      <div class="md-layout-item md-size-95 tariff-btns">
        <md-button class="md-accent">Delete</md-button>
        <md-button class="md-primary">Save</md-button>
      </div>
    </div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <md-field>
          <label>Tariff Line</label>
          <md-input v-model="tariffLine"></md-input>
        </md-field>
        <md-field>
          <label>HS6</label>
          <md-input v-model="hs6" v-bind:alt="hsdescription" v-bind:title="hsdescription"></md-input>
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
        <md-autocomplete v-model="stagingBasket" :md-options="stagingBasketOptions">
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
        <div>
          <label class="radio-lbl">Eliminated</label>
          <md-radio v-model="tariffEliminated" :value="true">True</md-radio>
          <md-radio v-model="tariffEliminated" :value="false">False</md-radio>
        </div>
      </div>
      <div class="md-layout-item">
        <md-autocomplete v-model="productType" :md-options="productTypeOptions">
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
.radio-lbl {
  margin-right: 16px;
}
</style>
<script>
export default {
  name: "Tariff",
  props: {
    id: Number,
    tariffRepository: Object
  },
  async created() {
    this.countryOptions = await this.tariffRepository._getCountries();

    let stagingBaskets = await this.tariffRepository._getAllStagingBaskets();
    this.stagingBasketOptions = stagingBaskets.map(sb => sb.description);

    let productTypes = await this.tariffRepository._getAllProductTypes();
    this.productTypeOptions = productTypes.map(pt => pt.description);

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
    this.tariffEliminated = tariff.tariffEliminated;
    this.hs6 = tariff.hs6.code;
    this.hsdescription = tariff.hs6.description;
  },
  data() {
    return {
      countryOptions: [],
      stagingBasketOptions: [],
      productTypeOptions: [],
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
      tariffEliminated: null,
      hs6: null,
      hsdescription: null
    };
  },
  methods: {
    goToTariffsList() {
      this.$router.push({ name: "tariffsList" });
    }
  }
};
</script>