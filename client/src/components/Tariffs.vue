<template>
  <div>
    <div v-if="loading" class="loading">Loading...</div>
    <div class="tariff-nav">
      <p>Page: {{page + 1}} of {{totalPages}}</p>
      <md-button class="nav-btn" @click="prevPage()" v-bind:disabled="isFirstPage()">Previous</md-button>
      <md-button class="nav-btn" @click="nextPage()" v-bind:disabled="isLastPage()">Next</md-button>
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
      </div>
      <div class="md-layout-item">
        <md-field>
          <label for="productType">Product Type</label>
          <md-select v-model="productTypeId" placeholder="(All)">
            <md-option
              v-for="productType in productTypes"
              v-bind:key="productType.id"
              v-bind:value="productType.id"
            >{{productType.description}}</md-option>
          </md-select>
        </md-field>
      </div>
      <div class="md-layout-item">
        <md-button class="md-primary" @click="fetchTariffs()">Filter</md-button>
      </div>
    </div>
    <md-table v-if="loading==false" v-model="tariffs">
      <md-table-row slot="md-table-row" slot-scope="{ item }">
        <md-table-cell md-label="TL">{{item.tariffLine}}</md-table-cell>
        <md-table-cell md-label="HS6">{{item.hs6.code}}</md-table-cell>
        <md-table-cell md-label="Description">{{item.description}}</md-table-cell>
        <md-table-cell md-label="Base Rate">{{item.baseRate}}</md-table-cell>
        <md-table-cell md-label="Start Yr.">{{item.partnerStartYear}}</md-table-cell>
        <md-table-cell md-label="Final Yr.">{{item.finalYear}}</md-table-cell>
        <md-table-cell md-label="Sector">{{item.sectorCode}}</md-table-cell>
        <md-table-cell md-label="Staging Bsk">{{item.stagingBasket.description}}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>

<script>
export default {
  name: "Tariffs",
  props: {
    tariffRepository: Object,
    countryRepository: Object,
    productRepository: Object
  },
  async created() {
    this.loading = true;
    await this.fetchCountries();
    await this.fetchProductTypes();
    this.countryCode = this.countries[0].code;
    await this.fetchTariffs();
    this.loading = false;
  },
  data() {
    return {
      loading: false,
      error: null,
      tariffs: null,
      page: 0,
      size: 25,
      countryCode: null,
      productTypeId: -1,
      totalPages: null,
      countries: null,
      productTypes: null
    };
  },
  methods: {
    isFirstPage() {
      return this.page === 0;
    },
    isLastPage() {
      return this.page === this.totalPages - 1;
    },
    nextPage() {
      this.page++;
      this.fetchTariffs();
    },
    prevPage() {
      this.page--;
      this.fetchTariffs();
    },
    async fetchTariffs() {
      let tariffsResponse = await this.tariffRepository._getTariffs(
        this.countryCode,
        this.productTypeId,
        this.page,
        this.size
      );
      this.totalPages = tariffsResponse.totalPages;
      if (this.totalPages === 0) this.page = 0;
      this.tariffs = tariffsResponse.tariffs;
    },
    async fetchCountries() {
      this.countries = await this.countryRepository._getCountries();
    },
    async fetchProductTypes() {
      let productTypes = await this.productRepository._getTypes();
      productTypes.push({ id: -1, description: "(All)" });
      productTypes.sort(function(a, b) {
        if (a.description < b.description) {
          return -1;
        }
        if (a.description > b.description) {
          return 1;
        }
        return 0;
      });
      this.productTypes = productTypes;
    }
  }
};
</script>

<style>
.nav-btn {
  width: 100px;
}

.tariff-nav {
  display: flex;
  justify-content: flex-end;
}
</style>
