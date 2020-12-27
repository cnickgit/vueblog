<template>
    <div class="home-page">
      <van-tabs type="card" @change="changeTab">
        <van-tab title="旺旺号">
          <van-search
            v-model="searchName"
            :placeholder="placeholder"
            input-align="center"
            shape="round"
            left-icon="none"
          />
          <van-button class="search-btn" round type="info" size="large" @click="search()">查询</van-button>
          <div v-if="showResult" class="wwdata">
            <van-row  justify="center">
              <van-col span="12">买家: {{data.aliimSim}}****</van-col>
              <van-col span="12">实名认证: {{data.nameconform_word}} {{hy}} {{tbhy}}</van-col>
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
              <van-col span="24">查询时间: {{data.queryTime}}</van-col>
            </van-row>

            <h1 class="userjb_t">用户被打标情况</h1>

            <van-row class="userjb_c" justify="center">
              <van-col span="3">兔子</van-col>
              <van-col span="3">蜜罐</van-col>
              <van-col span="3">狐狸</van-col>
              <van-col span="3">鳄鱼</van-col>
              <van-col span="3">野狗</van-col>
              <van-col span="3">老鼠</van-col>
              <van-col span="3">降权</van-col>
            </van-row>
            <van-row class="userjb_c">
              <van-col span="3">{{data.type1}}</van-col>
              <van-col span="3">{{data.type2}}</van-col>
              <van-col span="3">{{data.type3}}</van-col>
              <van-col span="3">{{data.type4}}</van-col>
              <van-col span="3">{{data.type5}}</van-col>
              <van-col span="3">{{data.type6}}</van-col>
              <van-col span="3">{{data.downNum}}</van-col>
            </van-row>

            <van-row class="userjb_c" justify="center">
              <van-col span="8">好评: {{data.goodNum}}</van-col>
              <van-col span="8">差评: {{data.badNum}}</van-col>
              <van-col span="8">证明: {{data.proveNum}}</van-col>
            </van-row>
            <van-row class="userjb_c" justify="center">
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
                    :placeholder="placeholder"
                    input-align="center"
            />
          </div>
          <div>
            <van-button round type="info" size="large" @click="searchMark()">查询</van-button>
          </div>
          <h2 class="userjb_t">用户被打标情况</h2>
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
              tbhy: '',
              hy: '',
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
              placeholder: '请输入旺旺号'
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
                  if(res.data.data == null){
                    this.showDefault = true;
                    this.showResult = false;
                    Toast.fail(res.data.msg);
                  }else if(res.data.data.result != null && res.data.data.result == '账号不存在'){
                   this.showDefault = true;
                   this.showResult = false;
                   Toast.fail("账号不存在")
                 }else{
                    this.showDefault = false;
                    this.showResult = true;
                    this.data = res.data.data;
                    if(this.data.vip_level == '0'){
                        this.hy = '普通会员';
                    }else if(this.data.vip_level == '10'){
                        this.hy = '超级会员'
                    }
                    if(this.data.vip_info  == 'c' || this.data.vip_info == 'asso_vip'){
                        this.tbhy = ''
                    }else if(this.data.vip_info == 'vip1'){
                        this.tbhy = '淘宝V1会员'
                    }else if(this.data.vip_info == 'vip2'){
                        this.tbhy = '淘宝V2会员'
                    }else if(this.data.vip_info == 'vip3'){
                        this.tbhy = '淘宝V3会员'
                    }else if(this.data.vip_info == 'vip4'){
                        this.tbhy = '淘宝V4会员'
                    }else if(this.data.vip_info == 'vip5'){
                        this.tbhy = '淘宝V5会员'
                    }else if(this.data.vip_info == 'vip6'){
                        this.tbhy = '淘宝V6会员'
                    }
                 }
             })
           },
         changeTab(name, title) {
             this.$nextTick(() => {
               this.placeholder = `请输入${title}`;
             });
             console.log(name, title)
           console.log(this.placeholder)

         }
       },
       created() {
         this.token = this.$route.query.code;
         sessionStorage.setItem("code",this.token)
            if(this.token == undefined){
              this.$router.push({ name: 'UserLogin'})
            }
       }
    }
</script>

<style lang="less" scoped>
  .home-page {
    /deep/ .van-tabs__nav--card {
      width: 100%;
      height: 100%;
      margin: 0;
    }
    /deep/ .van-tabs--card>.van-tabs__wrap {
      height: 100%;
      cursor: pointer;
    }
    /deep/ .van-tabs__nav--card .van-tab {
      height: 0.57rem;
      line-height: 0.57rem;
      width: 33.33%;
      float: left;
      background: #ccc;
      color: #fff;
      font-size: 0.2rem;
      text-align: center;
      cursor: pointer;
      border-right: 1px solid #fff;
      &.van-tab--active {
        background-image: linear-gradient(to left, #ff315d, #ff7693);
      }
    }
    /deep/ .van-tabs__nav--card {
      border: none;
    }
    /deep/ .van-field__control--center {
      text-align: left;
    }
    /deep/ .van-search {
      padding: 0;
    }
    /deep/ .van-tabs__content {
      padding: 0 0.16rem;
    }
    /deep/ .van-search {
      border: 1px solid #ababab;
      height: 0.5rem;
      border-radius: 2.5rem;
      margin-top: 0.1rem;
    }
    /deep/ .search-btn {
      margin-top: 0.15rem;
      color: #fff;
      text-align: center;
      display: block;
      border-radius: 2.5rem;
      height: 0.5rem;
      line-height: 0.5rem;
      background: #ff5277;
      font-size: 0.17rem;
      cursor: pointer;
      border: none;
    }
    .wwdata {
      background: #fafafa;
      border-radius: 0.05rem;
      margin-top: 0.2rem;
      .van-row {
        margin-top: 4px;
      }
    }
    .userjb_t {
      text-align: center;
      margin-top: 0.4rem;
    }
    .userjb_c {
      display: flex;
      justify-content: space-between;
      /deep/ .van-col {
        text-align: center;
      }
    }
    .red {
      color: #ff5277;
    }
    .content{
      margin-top: 20px;
      margin-left: 30px;
    }
  }
</style>
