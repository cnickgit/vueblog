<template>
    <div>
        <van-tabbar v-model="active">
            <van-tabbar-item
                    v-for="(item,index) in tabbars"
                    :key="index"
                    @click="tab(index,item.name)"
            >
                <span :class="currIndex == index ? active:''">{{item.title}}</span>
                <template slot="icon" slot-scope="props">
                    <img :src="item.normal">
                </template>
            </van-tabbar-item>
        </van-tabbar>
    </div>
</template>

<script>
    import { Loading,Tab,Tabs,Search,Tabbar,TabbarItem,Button  } from 'vant'
    export default {
        components: {
            [Loading.name]: Loading,
            [Tab.name]: Tab,
            [Tabs.name]: Tabs,
            [Search.name]: Search,
            [Tabbar.name]: Tabbar,
            [TabbarItem.name]: TabbarItem,
            [Button.name]: Button
        },
        data(){
            return {
                currIndex: '',
                active: 0,
                tabbars: [
                    {
                        name: "/homePage",
                        title: "首页",
                        normal: require('@/assets/home_ico_active.png'),
                        active: 0
                    },
                    // {
                    //     name: "category",
                    //     title: "打标",
                    //     normal: require('@/assets/report_ico.png'),
                    //     active: 1
                    // },
                    // {
                    //     name: "message",
                    //     title: "客服",
                    //     normal: require('@/assets/teas_ico.png'),
                    //     active: 2
                    // },
                    // {
                    //     name: "cart",
                    //     title: "打标记录",
                    //     normal: require('@/assets/save_ico.png'),
                    //     active: 3
                    // },
                    {
                        name: "My",
                        title: "我的",
                        normal: require('@/assets/my_ico.png'),
                        active: 4
                    }
                ],
            }
        },
        methods: {
            tab(index, val) {
                this.currIndex = index;
                this.$router.push(val);
            }
        }
    };
</script>