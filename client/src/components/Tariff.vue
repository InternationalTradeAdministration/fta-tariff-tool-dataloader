<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-5">
        <md-button class="md-icon-button" @click="goToTariffsList()">
          <md-icon class="fa fa-angle-double-left"></md-icon>
        </md-button>
      </div>
      <div class="md-layout-item md-size-95 tariff-btns">
        <md-button class="md-accent top-btn" disabled>Delete</md-button>
        <md-button class="md-primary top-btn" disabled>Save</md-button>
      </div>
    </div>
    <div class="tariff-container">
      <div class="section-a">
        <div class="md-layout md-gutter">
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>Tariff Line</label>
              <md-input v-model="tariffLine" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>HS6</label>
              <md-input
                v-model="hs6"
                v-bind:alt="hsdescription"
                v-bind:title="hsdescription"
                readonly
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-30">
            <md-autocomplete v-model="stagingBasket" :md-options="stagingBasketOptions" readonly>
              <label>Staging Basket</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-size-20">
            <md-autocomplete v-model="productType" :md-options="productTypeOptions" readonly>
              <label>Product Types</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>Final Year</label>
              <md-input v-model="finalYear" type="number" readonly></md-input>
            </md-field>
          </div>
        </div>
        <div class="md-layout md-gutter">
          <div class="md-layout-item md-size-25">
            <md-field>
              <label>Partner</label>
              <md-input v-model="partnerName" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>Start Year</label>
              <md-input v-model="partnerStartYear" type="number" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-25">
            <md-field>
              <label>Reporter</label>
              <md-input v-model="reporterName" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>Start Year</label>
              <md-input v-model="reporterStartYear" type="number" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-15">
            <md-field>
              <label>Sector</label>
              <md-input v-model="sectorCode" readonly></md-input>
            </md-field>
          </div>
        </div>
        <div class="md-layout md-gutter">
          <div class="md-layout-item md-size-25">
            <md-field>
              <label>Base Rate</label>
              <md-input v-model="baseRate" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-20">
            <md-field>
              <label>Quota Name</label>
              <md-input v-model="quotaName" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-20">
            <md-field>
              <label>Tariff Rate Quota</label>
              <md-input v-model="tariffRateQuota" readonly></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-size-35">
            <label class="radio-lbl">Eliminated</label>
            <md-radio v-model="tariffEliminated" :value="true">True</md-radio>
            <md-radio v-model="tariffEliminated" :value="false">False</md-radio>
          </div>
        </div>
        <div class="md-layout md-gutter">
          <div class="md-layout-item">
            <md-field>
              <label>Description</label>
              <md-textarea v-model="description" readonly></md-textarea>
            </md-field>
          </div>
        </div>
        <div class="md-layout md-gutter">
          <div class="md-layout-item">
            <md-field>
              <label>Rule Text</label>
              <md-textarea v-model="ruleText" readonly></md-textarea>
            </md-field>
          </div>
        </div>
        <div class="md-layout md-gutter">
          <div class="md-layout-item">
            <md-field>
              <label>Tariff Rate Quota Notes</label>
              <md-textarea v-model="tariffRateQuotaNotes" readonly></md-textarea>
            </md-field>
          </div>
        </div>
      </div>
      <div class="section-b">
        <div>
          <div class="rates-header">
            <label>Annual Rates</label>
          </div>
          <md-list v-for="rate in rates" v-bind:key="rate.id">
            <li>
              <input class="rate-year-input" v-bind:value="rate.year" readonly />
              <input class="rate-value-input" v-bind:value="rate.value" readonly />
            </li>
          </md-list>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
.rates-header {
  display: flex;
  justify-content: space-between;
}
input[type="number"] {
  width: 0px;
}
.tariff-container {
  display: flex;
}
.section-a {
  width: 900px;
}
.section-b {
  margin-left: 25px;
  width: 310px;
}
.rate-year-input {
  width: 40px;
  margin-right: 8px;
}
.rate-value-input {
  width: 210px;
  margin-right: 8px;
}
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
    this.rates = tariff.rates.sort((a, b) => {
      return a.year - b.year;
    });
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
      hsdescription: null,
      rates: []
    };
  },
  methods: {
    goToTariffsList() {
      this.$router.push({ name: "tariffsList" });
    }
  }
};
</script>