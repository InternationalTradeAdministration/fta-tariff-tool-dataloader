<template>
  <div>
    <div v-if="loading" class="loading">Loading...</div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item">
        <md-field>
          <label for="countries">Country</label>
          <md-select v-model="countryCode" @md-selected="onCountryChange">
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
        <md-field>
          <label>Tariff Line</label>
          <md-input v-model="tariffLine"></md-input>
        </md-field>
      </div>
      <div class="md-layout-item size-input">
        <span>Size:</span>
        <select v-model="size">
          <option value="25">25</option>
          <option value="50">50</option>
          <option value="100">100</option>
        </select>
      </div>
      <div class="md-layout-item page-input">
        <span>Page:</span>
        <input v-model="page">
        of {{totalPages}}
      </div>
      <div class="md-layout-item">
        <md-button class="md-primary request-btn" @click="fetchTariffs()">Request</md-button>
      </div>
    </div>
    <div class="tariff-nav">
      <div class="page-nav"></div>
      <md-button class="nav-btn" @click="prevPage()" v-bind:disabled="isFirstPage()">Previous</md-button>
      <md-button class="nav-btn" @click="nextPage()" v-bind:disabled="isLastPage()">Next</md-button>
    </div>
    <md-table v-if="loading==false" v-model="tariffs" @md-selected="selectTariff">
      <md-table-row
        v-bind:alt="item.description"
        v-bind:title="item.description"
        slot="md-table-row"
        slot-scope="{ item }"
        md-selectable="single"
      >
        <md-table-cell md-label="Tariff Ln">{{item.tariffLine}}</md-table-cell>
        <md-table-cell md-label="HS6">{{item.hs6.code}}</md-table-cell>
        <md-table-cell md-label="Base Rt">{{item.baseRate}}</md-table-cell>
        <md-table-cell md-label="Start Yr">{{item.partnerStartYear}}</md-table-cell>
        <md-table-cell md-label="Final Yr">{{item.finalYear}}</md-table-cell>
        <md-table-cell md-label="Staging Bsk">{{item.stagingBasket.description}}</md-table-cell>
        <md-table-cell md-label="Reporter">{{item.reporterName}}</md-table-cell>
        <md-table-cell md-label="Partener">{{item.partnerName}}</md-table-cell>
        <md-table-cell md-label="Sector">{{item.sectorCode}}</md-table-cell>
        <md-table-cell md-label="TRQ">{{item.tariffRateQuota}}</md-table-cell>
        <md-table-cell md-label="Eliminated">{{item.tariffEliminated}}</md-table-cell>
        <md-table-cell
          v-bind:md-label="year.toString()"
          v-for="year in tariffRateYears"
          v-bind:key="year"
        >{{getRate(item,year)}}</md-table-cell>
      </md-table-row>
    </md-table>
  </div>
</template>
<style>
.nav-btn {
  width: 100px;
}

.tariff-nav {
  display: flex;
  justify-content: flex-end;
}

.page-nav {
  margin-top: 12px;
}

.page-input {
  margin-top: 24px;
}

.page-input input {
  width: 30px;
}

.size-input select {
  margin-top: 24px;
  margin-left: 5px;
}

.request-btn {
  margin-top: 15px;
}
</style>

<script>
export default {
  name: "Tariffs",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.loading = true;
    await this.fetchCountries();
    this.countryCode = this.countries[0].code;
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
      countryCode: null,
      stagingBasketId: -1,
      tariffLine: null,
      totalPages: null,
      countries: [],
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
      this.fetchTariffs();
    },
    prevPage() {
      this.page--;
      this.fetchTariffs();
    },
    async fetchTariffs() {
      let { totalPages, tariffs } = await this.tariffRepository._getTariffs(
        this.countryCode,
        this.stagingBasketId,
        this.tariffLine,
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

      var collator = new Intl.Collator(undefined, {
        numeric: true,
        sensitivity: "base"
      });

      this.tariffRateYears = years.sort(collator.compare);
    },
    async fetchCountries() {
      this.countries = await this.tariffRepository._getCountries();
    },
    async fetchStagingBaskets() {
      let stagingBaskets = await this.tariffRepository._getStagingBaskets(
        this.countryCode
      );
      stagingBaskets.push({ id: -1, description: "(All)" });
      stagingBaskets.sort((a, b) => {
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
    },
    async onCountryChange() {
      this.stagingBasketId = -1;
      await this.fetchStagingBaskets();
    }
  }
};
</script>
