import request from "../utils/request";

export function selectRoles(params) {
  return request({
    url: '/admin/roles/?page='+params.page+'&limit='+params.limit + '&roleName='+params.roleName,
    method: 'get'
  })
}

export function saveRole(params) {
  return request({
    url: '/admin/roles/',
    method: 'post',
    data: params
  })
}

export function selectCount(name, id) {
  return request({
    url: '/admin/roles/count/'+name+ '?id='+id,
    method: 'get'
  })
}

export function updateRole(params) {
  return request({
    url: '/admin/roles/'+params.id,
    method: 'put',
    data: params
  })
}

export function deleteById(id) {
  return request({
    url: '/admin/roles/'+id ,
    method: 'delete'
  })
}
