<template>
  <div>
    <div v-if="loading" class="loading">Loading...</div>
    <div class="tariff-nav">
      <p>Page: {{page + 1}} of {{totalPages}}</p>
      <md-button class="nav-btn" @click="prevPage()" v-bind:disabled="isFirstPage()">Previous</md-button>
      <md-button class="nav-btn" @click="nextPage()" v-bind:disabled="isLastPage()">Next</md-button>
    </div>
    <span>Country: {{countryCode}}</span>
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
    tariffRepository: Object
  },
  async created() {
    await this.fetchTariffs();
  },
  data() {
    return {
      loading: false,
      error: null,
      tariffs: null,
      page: 0,
      size: 10,
      countryCode: "KR",
      totalPages: null
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
      this.loading = true;
      let tariffsResponse = await this.tariffRepository._getTariffs(
        this.countryCode,
        this.page,
        this.size
      );
      this.totalPages = tariffsResponse.totalPages;
      this.tariffs = tariffsResponse.tariffs;
      this.loading = false;
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
