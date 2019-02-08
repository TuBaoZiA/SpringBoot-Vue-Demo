import router from './router'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import store from './store'
import {getToken } from "./utils/auth";

import {initWebSocket} from "./utils/webSocket";

const whiteList = ['/login']; // 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start();

  //如果已登录
  if (getToken()) {

    if (to.path === '/login') {
      //访问login页面时，返回主页面
      next({ path: '/' });
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {

      //访问权限信息
      if (store.getters.roles.length === 0) {
        store.dispatch('GetInfo').then(res => { // 拉取用户信息

          initWebSocket();

          router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表

          next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigat

        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            next({ path: '/' })
          })
        })
      } else {
          next()
      }
    }

  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`); // 否则全部重定向到登录页
      NProgress.done()
    }
  }
});

router.afterEach(() => {
  NProgress.done() // 结束Progress
});
