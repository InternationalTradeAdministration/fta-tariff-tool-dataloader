<template>
  <div>
    <div class="md-layout md-gutter">
      <div class="md-layout-item md-size-15">
        <md-field>
          <label>Country</label>
          <md-select v-model="countryCode" @md-selected="onCountryChange">
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
          <label>Staging Bsk</label>
          <md-select v-model="stagingBasketId">
            <md-option
              v-for="stagingBasket in stagingBasketOptions"
              v-bind:key="stagingBasket.id"
              v-bind:value="stagingBasket.id"
            >{{stagingBasket.description}}</md-option>
          </md-select>
        </md-field>
      </div>
      <div class="md-layout-item md-size-15">
        <md-field md-clearable>
          <label>Tariff Line</label>
          <md-input v-model="tariffLine"></md-input>
        </md-field>
      </div>
      <div class="md-layout-item page-nav md-size-30">
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
      <div class="md-layout-item">
        <md-button class="md-primary request-btn" @click="fetchTariffs()">Request</md-button>
      </div>
      <div class="md-layout-item">
        <md-button class="md-icon-button config-btn" @click="goToConfig()">
          <md-icon class="fa fa-cog"></md-icon>
        </md-button>
      </div>
    </div>
    <div class="tariff-nav">
      <md-button class="nav-btn" @click="prevPage()" v-bind:disabled="isFirstPage()">Previous</md-button>
      <md-button class="nav-btn" @click="nextPage()" v-bind:disabled="isLastPage()">Next</md-button>
    </div>
    <div v-if="loading" class="loading">Loading...</div>
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
  margin-top: 24px;
}

.page-input {
  float: left;
}

.page-input input {
  width: 30px;
}

.size-input {
  float: left;
  margin-right: 10px;
}

.request-btn {
  margin-top: 15px;
}

.config-btn {
  margin-top: 12px;
  float: right;
}
</style>

<script>
export default {
  name: "TariffsList",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.loading = true;
    this.countryOptions = await this.tariffRepository._getCountries();
    this.countryCode = this.countryOptions[0].code;
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
      countryOptions: [],
      stagingBasketOptions: [],
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
    async onCountryChange() {
      this.stagingBasketId = -1;
      await this.fetchStagingBaskets();
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
      this.stagingBasketOptions = stagingBaskets;
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
    goToConfig() {
      this.$router.push({ name: "config" });
    }
  }
};
</script>
