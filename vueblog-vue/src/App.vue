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
    methods: {
      enableToken(){
        this.$axios.get('/enableToken?id='+this.token).then((res) => {
          if(res.data.code == 200){
            console.log("res:",res)
          }
        })
      }
    },
    created() {
      console.log(this.$route.query.code)
      this.token = this.$route.query.code;
      this.enableToken();
      //启用token
    }
  }
</script>
<style>
  #app {
    max-width: 960px;
    margin: 0 auto;
  }
</style>
