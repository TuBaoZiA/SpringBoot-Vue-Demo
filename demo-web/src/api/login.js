import request from "../utils/request";

export function login(username, password) {
  return request({
    url: '/admin/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token){
  return request({
    url: '/admin/' + token ,
    method: 'get'
  })
}

export function logout(token) {
  return request({
    url: '/admin/logout/' + token,
    method: 'post'
  })
}
