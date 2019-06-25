<template>
  <div>
    <div v-if="loading" class="loading">Loading...</div>
    <div class="tariff-nav">
      <div class="page-nav">
        <div class="size-input">
          <span>Size:</span>
          <select v-model="size">
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>
        </div>
        <div class="page-input">
          <span>Page:</span>
          <input v-model="page">
          of {{totalPages}}
        </div>
      </div>
      <md-button class="nav-btn" @click="prevPage()" v-bind:disabled="isFirstPage()">Previous</md-button>
      <md-button class="nav-btn" @click="nextPage()" v-bind:disabled="isLastPage()">Next</md-button>
    </div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <md-field>
          <label for="countries">Country</label>
          <md-select v-model="countryCode" @md-selected="fetchProductTypes">
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
          <label for="productTypeId">Product Type</label>
          <md-select v-model="productTypeId">
            <md-option
              v-for="productType in productTypes"
              v-bind:key="productType.id"
              v-bind:value="productType.id"
            >{{productType.description}}</md-option>
          </md-select>
        </md-field>
      </div>

      <div class="md-layout-item">
        <md-field>
          <label for="stagingBasketId">Staging Bsk</label>
          <md-select v-model="stagingBasketId">
            <md-option
              v-for="stagingBasket in stagingBaskets"
              v-bind:key="stagingBasket.id"
              v-bind:value="stagingBasket.id"
            >{{stagingBasket.description}}</md-option>
          </md-select>
        </md-field>
      </div>

      <div class="md-layout-item">
        <md-button class="md-primary" @click="fetchTariffs()">Filter</md-button>
      </div>
    </div>
    <md-table v-if="loading==false" v-model="tariffs" @md-selected="selectTariff">
      <md-table-row slot="md-table-row" slot-scope="{ item }" md-selectable="single">
        <md-table-cell md-label="TL">{{item.tariffLine}}</md-table-cell>
        <md-table-cell md-label="HS6">{{item.hs6.code}}</md-table-cell>
        <md-table-cell md-label="Description">{{item.description}}</md-table-cell>
        <md-table-cell md-label="Base Rate">{{item.baseRate}}</md-table-cell>
        <md-table-cell md-label="Start Yr">{{item.partnerStartYear}}</md-table-cell>
        <md-table-cell md-label="Final Yr">{{item.finalYear}}</md-table-cell>
        <md-table-cell md-label="Staging Bsk">{{item.stagingBasket.description}}</md-table-cell>
        <md-table-cell md-label="Sector">{{item.sectorCode}}</md-table-cell>
        <md-table-cell
          v-bind:md-label="year.toString()"
          v-for="year in tariffRateYears"
          v-bind:key="year"
        >{{getRate(item,year)}}</md-table-cell>
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
    this.countryCode = this.countries[0].code;

    await this.fetchProductTypes();
    await this.fetchStagingBaskets();
    await this.fetchTariffs();

    this.loading = false;
  },
  data() {
    return {
      loading: false,
      error: null,
      tariffs: null,
      page: 1,
      size: 25,
      selectedCountry: null,
      countryCode: null,
      productTypeId: -1,
      stagingBasketId: -1,
      totalPages: null,
      countries: [],
      productTypes: [],
      stagingBaskets: [],
      tariffRateYears: []
    };
  },
  methods: {
    isFirstPage() {
      return this.page === 1;
    },
    isLastPage() {
      return this.currentPage(this.page, this.totalPages) === this.totalPages;
    },
    nextPage() {
      this.page++;
      this.fetchProductTypes();
      this.fetchTariffs();
    },
    prevPage() {
      this.page--;
      this.fetchProductTypes();
      this.fetchTariffs();
    },
    async fetchTariffs() {
      let { totalPages, tariffs } = await this.tariffRepository._getTariffs(
        this.countryCode,
        this.productTypeId,
        this.stagingBasketId,
        this.page,
        this.size
      );
      this.totalPages = totalPages;
      this.tariffs = tariffs;

      let years = [];
      this.tariffs.map(t => {
        t.rates.map(r => {
          if (!years.includes(r.year)) years.push(r.year);
        });
      });

      this.tariffRateYears = years.sort();
    },
    async fetchCountries() {
      this.countries = await this.tariffRepository._getCountries();
    },
    async fetchProductTypes() {
      let productTypes = await this.tariffRepository._getProductTypes(
        this.countryCode
      );
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
    },
    async fetchStagingBaskets() {
      let stagingBaskets = await this.tariffRepository._getStagingBaskets();
      stagingBaskets.push({ id: -1, description: "(All)" });
      stagingBaskets.sort(function(a, b) {
        if (a.description < b.description) {
          return -1;
        }
        if (a.description > b.description) {
          return 1;
        }
        return 0;
      });
      this.stagingBaskets = stagingBaskets;
    },
    getRate(tariffLine, year) {
      if (tariffLine.rates) {
        let rateForYear = tariffLine.rates.find(r => r.year === year);
        if (rateForYear && rateForYear.value) {
          return rateForYear.value;
        } else {
          return "-";
        }
      }
    },
    currentPage() {
      if (this.totalPages > 0) {
        return this.page;
      }
      return this.page;
    },
    selectTariff(tariff) {
      this.$router.push({ name: "tariff", query: { id: tariff.id } });
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

.page-nav {
  display: flex;
  margin-top: 12px;
}

.page-input input {
  width: 30px;
}

.size-input {
  display: inline-table;
  margin-right: 10px;
}
</style>
