import request from "@/utils/request";

export function rolesDrop() {
  return request({
    url: '/admin/common/roles',
    method: 'get'
  })
}

export function permissionsDrop(){
  return request({
    url: '/admin/common/permissions',
    method: 'get'
  })
}

export function menusDrop(id, keepParent){
  return request({
    url: '/admin/common/menus?id='+id+'&keepParent='+keepParent,
    method: 'get'
  })
}
