import Vue from 'vue'
import VueRouter from 'vue-router'
// import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogEdit from '../views/BlogEdit.vue'
import BlogDetail from '../views/BlogDetail.vue'
import HomePage from "@/home/homePage";
import My from "@/page/my.vue";
import UserLogin from "@/components/Login.vue";

Vue.use(VueRouter)


const routes = [
  {
    path: '/HomePage',
    name: 'HomePage',
    component: HomePage,
    meta: {
      showTab:true
    }
  },
  {
    path: '/UserLogin',
    name: 'UserLogin',
    component: UserLogin,
    meta: {
          showTab:false
    }
  },
  // {
  //   path: '/AddTokenPage',
  //   name: 'AddTokenPage',
  //   component: AddTokenPage
  // },
  // {
  //   path: '/',
  //   name: 'HomePage',
  //   component: HomePage
  // },
  {
    path: '/My',
    name: 'My',
    component: My,
    meta: {
          showTab:true
    }
  },
  {
    path: '/blogs',
    name: 'Blogs',
    component: Blogs
  },
  // {
  //   path: '/login',
  //   name: 'Login',
  //   component: Login
  // },
  {
    path: '/blog/add',
    name: 'BlogAdd',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/:blogId/edit',
    name: 'BlogEdit',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
