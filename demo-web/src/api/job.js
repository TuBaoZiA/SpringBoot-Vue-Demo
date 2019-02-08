import request from "../utils/request";

export function selectJob() {
  return request({
    url: '/jobs/',
    method: 'get'
  })
}

export function updateJob(param) {
  return request({
    url: '/jobs/'+param.id,
    method: 'put',
    data: param
  })
}

export function saveJob(params) {
  return request({
    url: '/jobs/',
    method: 'post',
    data: params
  })
}

export function deleteJob(jobName, jobGroup) {
  return request({
    url: '/jobs/'+jobName+'/'+jobGroup,
    method: 'delete'
  })
}

export function pauseJob(jobName, jobGroup) {
  return request({
    url: '/jobs/pause/'+jobName+'/'+jobGroup,
    method: 'put'
  })
}

export function resumeJob(jobName, jobGroup) {
  return request({
    url: '/jobs/resume/'+jobName+'/'+jobGroup,
    method: 'put'
  })
}
