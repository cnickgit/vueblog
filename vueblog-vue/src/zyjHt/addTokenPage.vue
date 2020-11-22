<template>
    <div>
        <Header></Header>
        <h1>新增Token</h1>
        <el-radio-group v-model="radio" @change="showText">
            <el-radio :label="1">日</el-radio>
            <el-radio :label="2">月</el-radio>
            <el-radio :label="3">年</el-radio>
        </el-radio-group>
        <div style="margin-top: 20px;">
            <el-input style="width: 400px" maxlength="20" v-model="time" placeholder="请输入内容"></el-input>{{text}}
        </div>
        <div style="margin-top: 20px;">
            <el-input style="width: 400px" maxlength="20" v-model="queryCount" placeholder="请输入内容"></el-input>次
        </div>
        <div style="margin-top: 20px">
            <el-row>
                <el-button type="primary">提交</el-button>
            </el-row>
        </div>
    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "addTokenPage",
        components: {Header},
        data(){
            return {
                time: '',
                queryCount: '',
                radio: 1,
                input: '',
                text: '日',
            }
        },
        methods: {
            showText(){
                console.log("text:",this.text)
                if(this.radio == '1'){
                    this.text = '日'
                }else if(this.radio == '2'){
                    this.text = '月'
                }else if(this.radio == '3'){
                    this.text = '年'
                }
            }
        }
    }
</script>

<style scoped>

</style>