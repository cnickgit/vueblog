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
        this.$axios.get('/enableToken?code='+this.token).then((res) => {
          if(res.data.code == 200){
            console.log("res:",res)
          }else{
            this.$router.push({ name: 'Login'})
          }
        })
      }
    },
    created() {
      console.log(this.$route.query.code)
      this.token = this.$route.query.code;
      sessionStorage.setItem("token",this.token);
      if(this.token == undefined){
        this.$router.push({ name: 'Login'})
      }else{
        this.enableToken();
      }
    }
  }
</script>
<style>
  #app {
    max-width: 960px;
    margin: 0 auto;
  }
</style>
