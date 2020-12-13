<template>
  <div>
    <van-row type="flex" style="margin-top: 20px;">
      <van-col span="24">编号:{{my.id}} </van-col>
    </van-row>
    <van-row type="flex" justify="center" style="margin-top: 20px;">
      <van-col span="6">可查询总数</van-col>
      <van-col span="6">已查询</van-col>
      <van-col span="6">剩余</van-col>
      <van-col span="6">普通</van-col>
    </van-row>
    <van-row type="flex" justify="center" style="margin-top: 20px;">
      <van-col span="6">{{my.prescription}}</van-col>
      <van-col span="6">{{my.prescription - my.remainingTimes}}</van-col>
      <van-col span="6">{{my.remainingTimes}}</van-col>
      <van-col span="6">计费模式</van-col>
    </van-row>
    <h3 style="margin-top: 20px;">有效截止日期: {{my.endTime}}</h3>
  </div>
</template>

<script>
    import { Col, Row, Cell, CellGroup, Icon, Image as VanImage } from 'vant';
    export default {
        name: "my.vue",
        components:{
            [Col.name]: Col,
            [Row.name]: Row,
            [Cell.name]: Cell,
            [CellGroup.name]: CellGroup,
            [Icon.name]: Icon,
            [VanImage.name]: VanImage
        },
        data(){
          return {
            my:{}
          }
        },
        methods: {
          getTokenById(){
            let code = sessionStorage.getItem("code");
            this.$axios.get("/myRecord?id="+code).then(res => {
              if(res.data.code === 200){
                this.my = res.data.data;
              }
              console.log("res:",res)
            })
          }
        },
        created() {
            this.getTokenById();
        }
    }
</script>

<style scoped>
</style>
