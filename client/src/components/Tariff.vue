<template>
  <div>Tariff ID: {{ id }}</div>
</template>
<script>
export default {
  name: "Tariff",
  props: {
    id: Number,
    tariffRepository: Object,
    countryRepository: Object,
    productRepository: Object
  },
  async created() {
    console.log(this.id);
  }
};
</script>