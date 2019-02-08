import {Notification, MessageBox, Message} from 'element-ui'
import store from "@/store";
import {resetUserInfo} from "@/api/user";

export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value ] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function formatTime(time, option) {
  time = + time * 1000
  const d = new Date(time)
  const now = Date.now()
  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 将‘2018-12-22T14:40:33.000+0000’ 转换为 ‘2018-12-22 22:40:33’
 * @param date
 * @returns {string}
 */
export function parseDateTime(date) {
  let datetime = new Date(date).toJSON();
  return new Date(+new Date(datetime)+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'')
}

export function successNotify(message){
  Notification({
    title: '成功',
    message: message,
    type: 'success',
    duration: 2000
  })
}

export function deleteConfirm(then) {
  MessageBox.confirm('确认删除吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(then).catch(() => {
    Message({
      type: 'info',
      message: '已取消删除'
    });
  });
}

export function resetUser() {
  resetUserInfo(store.getters.name).then(response => {
    location.reload()
  });
}
