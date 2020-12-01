<template>
  <div id="app">
    <router-view/>
    <tabbar v-show="$route.meta.showTab"></tabbar>
  </div>
</template>

<script>
  import tabbar from './components/tabbar.vue';
  export default {
    name: 'App',
    components: {
      tabbar
    },
    created() {
      console.log(this.$route.query.code)
    }
  }
</script>
<style>
  #app {
    max-width: 960px;
    margin: 0 auto;
  }
</style>
