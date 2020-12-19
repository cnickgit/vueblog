<template>
    <div>
      <van-tabs type="card">
        <van-tab title="旺旺号">
          <div style="margin-top: 20px">
          <van-search
            v-model="searchName"
            placeholder="请输入搜索关键词"
            input-align="center"
          />
          </div>
          <div>
            <van-button round type="info" size="large" @click="search()">查询</van-button>
          </div>
          <div v-if="showResult">
            <van-row  justify="center">
              <van-col span="12">买家: {{data.aliimSim}}****</van-col>
              <van-col span="12">实名认证: {{data.nameconform_word}}</van-col>
            </van-row>
            <van-row  justify="center">
              <van-col span="12">买家信誉: {{data.buyerCre}}</van-col>
              <van-col span="12">注册日期: {{data.created}}</van-col>
            </van-row>
            <van-row  justify="center">
              <van-col span="12">商家信誉: {{data.sellerCredit}}</van-col>
              <van-col span="12">	淘龄: {{data.registDay}}</van-col>
            </van-row>
            <van-row  justify="center">
              <van-col span="12">性别: {{data.sex}}</van-col>
              <van-col span="12">	买家总周平均: {{data.buyerAvg}}</van-col>
            </van-row>
            <van-row justify="center">
              <van-col span="12">好评率: {{data.received_rate}}</van-col>
            </van-row>
            <van-row justify="center">
              <van-col span="12">查询时间: {{data.queryTime}}</van-col>
            </van-row>

            <h1 style="text-align: center">用户被打标情况</h1>

            <van-row justify="center">
              <van-col span="3">兔子</van-col>
              <van-col span="3">蜜罐</van-col>
              <van-col span="3">狐狸</van-col>
              <van-col span="3">鳄鱼</van-col>
              <van-col span="3">野狗</van-col>
              <van-col span="3">老鼠</van-col>
              <van-col span="3">降权</van-col>
            </van-row>
            <van-row>
              <van-col span="3">{{data.type1}}</van-col>
              <van-col span="3">{{data.type2}}</van-col>
              <van-col span="3">{{data.type3}}</van-col>
              <van-col span="3">{{data.type4}}</van-col>
              <van-col span="3">{{data.type5}}</van-col>
              <van-col span="3">{{data.type6}}</van-col>
              <van-col span="3">{{data.downNum}}</van-col>
            </van-row>

            <van-row justify="center">
              <van-col span="8">好评: {{data.goodNum}}</van-col>
              <van-col span="8">差评: {{data.badNum}}</van-col>
              <van-col span="8">证明: {{data.proveNum}}</van-col>
            </van-row>
            <van-row justify="center">
              <van-col span="12">本周查过商家: {{data.nearWeekShop}}</van-col>
              <van-col span="12">上周查过商家: {{data.lastWeekShop}}</van-col>
            </van-row>
          </div>
          <div v-if="showDefault">
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
        <van-tab title="微信/QQ">
          <div style="margin-top: 20px">
            <van-search
                    v-model="mark"
                    placeholder="请输入搜索关键词"
                    input-align="center"
            />
          </div>
          <div>
            <van-button round type="info" size="large" @click="searchMark()">查询</van-button>
          </div>
          <h2 style="text-align: center">用户被打标情况</h2>
          <div>
            <van-row  justify="center">
              <van-col v-if="showHuli" span="8">骗子狐狸</van-col>
              <van-col v-if="showEy" span="8">打假鳄鱼</van-col>
              <van-col span="8"></van-col>
            </van-row>
            <van-row  justify="center">
              <van-col span="8" v-if="showHuli" ><van-image width="80" height="80" :src="huli" /></van-col>
              <van-col span="8" v-if="showEy"><van-image width="80" height="80" :src="ey" /></van-col>
              <van-col span="8"></van-col>
            </van-row>
            <van-row  justify="center">
              <van-col span="8" v-if="showHuli" >{{markObj.hlwx}}</van-col>
              <van-col span="8" v-if="showEy">{{markObj.eywx}}</van-col>
              <van-col span="8"></van-col>
            </van-row>
          </div>
        </van-tab>
        <van-tab title="淘客">内容 3</van-tab>
      </van-tabs>
    </div>
</template>

<script>
    import { Loading,Tab,Tabs,Search,Tabbar,TabbarItem,Button,Col, Row, Toast, Image } from 'vant'
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
            [Row.name]: Row,
            [Toast.name]: Toast,
            [Image .name]: Image
        },
       data(){
          return {
              ey: require('@/assets/eyu.png'),
              huli: require('@/assets/huli.png'),
              mark: '',
              markObj: {},
              cookie: '',
              token: '',
              showDefault: true,
              showResult: false,
              active: 0,
              searchName: '',
              showEy: false,
              showHuli: false,
              data: {},
          }
       },
       methods: {
           searchMark(){
             this.$axios.get("/searchMarking?searchName="+this.mark+"&code="+this.token).then(res => {
               console.log("res:",res)
               if(res && res.data.code === 200){
                 this.markObj = res.data.data;
                 if(res.data.data.eywx == '有'){
                   this.showEy = true
                 }
                 if(res.data.data.hlwx == '有'){
                   this.showHuli = true
                 }
                 if(res.data.data.result === 'wxnodata'){
                   Toast.fail("当前微信或QQ没有打标记录")
                 }
               }
             })
           },
           search(){
             this.$axios.get("/search?searchName="+this.searchName+"&code="+this.token).then(res => {
               this.showDefault = false;
               this.showResult = true;
               console.log("res:",res)
                 if(res.data.data.result != null && res.data.data.result == '账号不存在'){
                   this.showDefault = true;
                   this.showResult = false;
                   Toast.fail("账号不存在")
                 }else{
                   this.data = res.data.data;
                 }
             })
           }
       },
       created() {
         this.token = this.$route.query.code;
            if(this.token == undefined){
              this.$router.push({ name: 'UserLogin'})
            }
       }
    }
</script>

<style scoped>
.content{
  margin-top: 20px;
  margin-left: 30px;
}
</style>
