<template>
  <div id="app">
    <router-view/>
    <tabbar></tabbar>
  </div>
</template>

<script>
  import tabbar from './components/tabbar.vue';
  export default {
    name: 'App',
    components: {
      tabbar
    }
  }
</script>
<style>
  #app {
    max-width: 960px;
    margin: 0 auto;
  }
</style>
