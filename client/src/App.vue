<template>
  <div id="container">
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
    />
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App"
};
</script>

<style lang="scss">
@import "~vue-material/dist/theme/engine"; // Import the theme engine
@include md-register-theme("default", (
  primary: #3e4853, // The primary color of your brand
));
@import "~vue-material/dist/theme/all"; // Apply the theme

.md-button.top-btn {
  margin-top: 14px;
}

</style>
