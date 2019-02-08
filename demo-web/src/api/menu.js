import request from "@/utils/request";

export function selectMenus() {
  return request({
    url: '/admin/menus/',
    method: 'get'
})
}

export function deleteById(id) {
  return request({
    url: '/admin/menus/'+id ,
    method: 'delete'
  })
}

export function selectCount(name, id) {
  return request({
    url: '/admin/menus/count/'+name+ '?id='+id,
    method: 'get'
  })
}

export function saveMenu(params) {
  return request({
    url: '/admin/menus/',
    method: 'post',
    data: params
  })
}

export function updateMenu(params) {
  return request({
    url: '/admin/menus/'+params.id,
    method: 'put',
    data: params
  })
}
