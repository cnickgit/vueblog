<template>
  <div id="app">
    <router-view/>
    <tabbar v-show="$route.meta.showTab"></tabbar>
  </div>
</template>

<script>
  import tabbar from './components/tabbar.vue';
  import {Toast} from "vant";
  export default {
    name: 'App',
    components: {
      tabbar
    },
    data(){
      return {
        token: '',
      }
    },
    methods: {
      getCookie(){
        this.$axios.get("/zyjLogin").then(res => {
          if(res != null){
            this.cookie = res.data.data;
            this.$router.push({ name: 'HomePage',query: {code: this.token,cookie: this.cookie}})
          }else{
            Toast.fail("激活码失效请联系客服")
          }
        })
      },
      enableToken(){
        this.$axios.get('/enableToken?code='+this.token).then((res) => {
          if(res.data.code == 200){
            this.$router.push({ name: 'HomePage',query: {code: this.token}})
          }else{
            Toast.fail(res.data.msg)
            this.$router.push({ name: 'UserLogin'})
          }
        })
      }
    },
    created() {
      sessionStorage.setItem("code",this.$route.query.code);
      this.token = this.$route.query.code;
      if(this.token == undefined){
        this.$router.push({ name: 'UserLogin'})
      }else{
        this.enableToken();
      }
    }
  }
</script>
<style>
  @import "./assets/style/skin.css";
  #app {
    max-width: 960px;
    margin: 0 auto;
  }
</style>
