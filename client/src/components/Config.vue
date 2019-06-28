<template>
  <div></div>
</template>
<script>
export default {
  name: "TariffsList",
  props: {
    tariffRepository: Object
  },
  async created() {
    this.countryOptions = await this.tariffRepository._getCountries();
  },
  data() {
    return {
      countryOptions: [],
    };
  }
}
</script>