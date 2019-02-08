import request from '@/utils/request'

export function getMyISAMList(params) {
  if(params.birthdayData != null && params.birthdayData.length > 0) {
    params.birthdayStart = params.birthdayData[0];
    params.birthdayEnd = params.birthdayData[1]
  }else{
    params.birthdayStart = '';
    params.birthdayEnd = '';
  }

  return request({
    url: '/admin/testMyISAM/select?page='+params.page+'&limit='+params.limit,
    method: 'post',
    data: params
  })
}

export function deleteByIdMyISAM(id) {
  return request({
    url: '/admin/testMyISAM/'+id ,
    method: 'delete'
  })
}

export function insertMyISAM(params) {
  return request({
    url: '/admin/testMyISAM/insert',
    method: 'put',
    data: params
  })
}

export function updateMyISAM(params) {
  return request({
    url: '/admin/testMyISAM/'+params.id,
    method: 'post',
    data: params
  })
}

export function getInnoDBList(params) {
  if(params.birthdayData != null && params.birthdayData.length > 0) {
    params.birthdayStart = params.birthdayData[0];
    params.birthdayEnd = params.birthdayData[1]
  }else{
    params.birthdayStart = '';
    params.birthdayEnd = '';
  }

  return request({
    url: '/admin/testInnoDB/select?page='+params.page+'&limit='+params.limit,
    method: 'post',
    data: params
  })
}

export function deleteByIdInnoDB(id) {
  return request({
    url: '/admin/testInnoDB/'+id ,
    method: 'delete'
  })
}

export function insertInnoDB(params) {
  return request({
    url: '/admin/testInnoDB/insert',
    method: 'put',
    data: params
  })
}

export function updateInnoDB(params) {
  return request({
    url: '/admin/testInnoDB/'+params.id,
    method: 'post',
    data: params
  })
}
