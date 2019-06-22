
<template>
  <div class="hello">
    <div v-if="loading" class="loading">Loading...</div>
    <h1>Tariff Rates</h1>
    <p>Current Page: {{page + 1}}</p>
    <p>Total Pages: {{totalPages}}</p>
    <button @click="prevPage()">Previous</button>
    <button @click="nextPage()">Next</button>
    <ul>
      <li v-for="tariff in tariffs" v-bind:key="tariff.id">{{tariff.description}}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'TariffRates',
  props: {
    tariffRatesRepository: Object
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
      let tariffsResponse = await this.tariffRatesRepository._getTariffs(this.countryCode, this.page, this.size);
      this.totalPages = tariffsResponse.totalPages;
      this.tariffs = tariffsResponse.tariffs;
      this.loading = false;
    }
  }
};
</script>
