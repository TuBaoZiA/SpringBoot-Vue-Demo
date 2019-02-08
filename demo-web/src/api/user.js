import request from "../utils/request";

export function updatePwd(id, password, sourcePass) {
    return request({
      url: '/admin/users/rePass/'+id,
      method: 'put',
      data:{
        sourcePass,
        password
      }
    })
}

export function resetUserInfo(username) {
  return request({
    url: '/admin/reset/'+username,
    method: 'post'
  })
}

export function updateUserInfo(params) {
  return request({
    url: '/admin/users/'+params.id,
    method: 'put',
    data: params

  })
}

export function selectUsers(params) {
  return request({
    url: '/admin/users/select?page='+params.page+'&limit='+params.limit,
    method: 'post',
    data: params
  })
}

export function selectCount(name, id) {
  return request({
    url: '/admin/users/count/'+name+'?id='+id,
    method: 'get'
  })
}

export function saveUser(params) {
  return request({
    url: '/admin/users/',
    method: 'post',
    data: params
  })
}

export function updateUserRole(params) {
  return request({
    url: '/admin/users/role',
    method: 'put',
    data: params
  })
}

export function deleteById(id) {
  return request({
    url: '/admin/users/'+id ,
    method: 'delete'
  })
}
