// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import './style/reset.css'
import axios from 'axios'
import VueLazyload from 'vue-lazyload'

Vue.use(VueLazyload)
Vue.use(iView)
Vue.config.productionTip = false
Vue.prototype.axios = axios
axios.defaults.baseURL=BASE_URL;
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.withCredentials=true;

Vue.prototype.code = {
  success:102008,
  FREE_STATUS: 100001,
  BUY_STATUS: 100010
}

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {  // 判断该路由是否需要登录权限
      if (sessionStorage.getItem("isLogin")) {  // 通过vuex state获取当前的token是否存在
          next();
      }
      else {
          next({
              path: '/auth',
              query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
          })
      }
  }
  else {
      next();
  }
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
