<template>
    <div>
        <div>
            <van-cell-group>
                <van-field v-model.trim="token" label="" placeholder="请输入激活码" />
            </van-cell-group>
        </div>
        <div>
            <van-row>
                <van-col span="12">
                    <van-button type="info" @click="login">登录</van-button>
                </van-col>
            </van-row>

        </div>
    </div>
</template>

<script>
    import { Field , Button , Row , Cell, CellGroup ,Col} from 'vant'
    export default {
        name: "Login",
        components: {
            [Button.name]: Button,
            [Field.name]: Field,
            [Row.name]: Row,
            [Cell.name]: Cell,
            [CellGroup.name]: CellGroup,
            [Col.name]: Col,
        },
        data(){
            return {
                token: ''
            }
        },
        methods:{
            login(){
                this.enableToken();
            },
            enableToken(){
                this.$axios.get('/enableToken?id='+this.token).then((res) => {
                    console.log("data:",res.data.data)
                    if(res.data.code == 200){
                        this.$router.push({ name: 'HomePage'})
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
