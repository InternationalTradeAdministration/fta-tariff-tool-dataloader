<template>
  <md-toolbar class="md-primary" md-elevation="1">
    <h3 class="md-title" style="flex: 1">FTA Tariff Tool Dataloader</h3>
    <md-button
      class="md-secondary"
      href="/api/open-data-catalog"
      target="_blank"
      alt="/api/open-data-catalog"
      title="/api/open-data-catalog"
    >Open Data Catalog</md-button>
    <git-hub />
  </md-toolbar>
</template>
<script>
import GitHub from "./GitHub";
export default {
  name: "TariffHeader",
  components: {
    "git-hub": GitHub
  }
};
</script>