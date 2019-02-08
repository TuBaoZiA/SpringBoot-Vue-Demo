import request from "../utils/request";

export function selectPermission(params) {
  return request({
    url: '/admin/permissions/?page='+params.page+'&limit='+params.limit,
    method: 'get'
  })
}

export function savePermission(params) {
  return request({
    url: '/admin/permissions/',
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

export function updatePermission(params) {
  return request({
    url: '/admin/permissions/'+params.id,
    method: 'put',
    data: params
  })
}

export function deleteById(id) {
  return request({
    url: '/admin/permissions/'+id ,
    method: 'delete'
  })
}
