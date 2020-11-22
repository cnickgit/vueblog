<template>
    <div>
      <van-tabs type="card">
        <van-tab title="标签 1">
          <van-search
            v-model="searchName"
            placeholder="请输入搜索关键词"
            input-align="center"
          />
          <div>
            <van-button round type="info" size="large" @click="search()">查询</van-button>
          </div>
          <div v-if="false">
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">买家: 8</van-col>
              <van-col span="12">实名认证: 8</van-col>
            </van-row>
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">买家信誉: 8</van-col>
              <van-col span="12">注册日期: 8</van-col>
            </van-row>
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">商家信誉: 8</van-col>
              <van-col span="12">	淘龄: 8</van-col>
            </van-row>
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">性别: 8</van-col>
              <van-col span="12">	买家总周平均: 8</van-col>
            </van-row>
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">好评率: 8</van-col>
            </van-row>
            <van-row style="margin-top: 20px;" justify="center">
              <van-col span="12">查询时间: 8</van-col>
            </van-row>
          </div>
          <div v-if="true">
            <van-row class="content">
              <van-col span="24">兔子：拿完了商家的返款就恶意退款</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">蜜獾：用各种方式威胁你给钱</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">狐狸：用各种方式骗你钱了</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">鳄鱼：用发票，商标，假货等方式坑你钱</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">野狗：接完了单给了你差评进行要挟</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">老鼠：用了淘客链接来拍你的单</van-col>
            </van-row>
            <van-row class="content">
              <van-col span="24">降权：导致商家单品降权的帐号</van-col>
            </van-row>
          </div>
        </van-tab>
        <van-tab title="标签 2">内容 2</van-tab>
        <van-tab title="标签 3">内容 3</van-tab>
      </van-tabs>
    </div>
</template>

<script>
    import { Loading,Tab,Tabs,Search,Tabbar,TabbarItem,Button,Col, Row  } from 'vant'
    export default {
        name: "homePage",
        components: {
            [Loading.name]: Loading,
            [Tab.name]: Tab,
            [Tabs.name]: Tabs,
            [Search.name]: Search,
            [Tabbar.name]: Tabbar,
            [TabbarItem.name]: TabbarItem,
            [Button.name]: Button,
            [Col.name]: Col,
            [Row.name]: Row
        },
       data(){
          return {
              active: 0,
              searchName: '',
          }
       },
       methods: {
           search(){
             this.$axios.get("/search").then(res => {
               console.log("res:",res)
             })
           },
           login(){
             const _this = this
             _this.$axios.get("/zyjLogin").then(res => {
               console.log(res)
             })
           }
       },
       created() {
          this.login();
       }
    }
</script>

<style scoped>
.content{
  margin-top: 20px;
}
</style>
