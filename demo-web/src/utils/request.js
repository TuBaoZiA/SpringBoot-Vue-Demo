import axios from 'axios'
import { Message , MessageBox} from 'element-ui'
import store from '@/store'
import {getToken} from "@/utils/auth";

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 5000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    // Do something before request is sent
    if (store.getters.token) {
      // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {

    console.log(error) // for debug
    Promise.reject(error)
  })


// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;

    if (res.code !== 20000) {

      //106
      if (res.code === 106 ) {
        MessageBox.confirm('你已被登出，请重新登录',  {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
      }else {
        Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
      }
      return Promise.reject('error')
    }
    return res;
  },

  error => {

    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
);

export default service
