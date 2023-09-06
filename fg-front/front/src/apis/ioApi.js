import request from '@/utils/request'

const BASE = '/io'

export default {
  // 查询列表
  fileTree: () => {
    return request({
      url: `${BASE}/file-tree`,
      method: 'GET'
    })
  },
  // 查询列表
  read: (url) => {
    return request({
      url: `${BASE}/read`,
      method: 'POST',
      data: { data: url }
    })
  },
  // 保存文件
  write: (data) => {
    return request({
      url: `${BASE}/write`,
      method: 'POST',
      data: data
    })
  },
  // 创建目录
  mkdir: (url) => {
    return request({
      url: `${BASE}/mkdir`,
      method: 'POST',
      data: { data: url }
    })
  },
  // 创建文件
  touch: (url) => {
    return request({
      url: `${BASE}/touch`,
      method: 'POST',
      data: { data: url }
    })
  },
  // 删除
  del: (url) => {
    return request({
      url: `${BASE}/del`,
      method: 'DELETE',
      data: { data: url }
    })
  },
  temp: () => {
    return request({
      url: `${BASE}/temp`,
      method: 'GET'
    })
  },
  rename: (oldVal, newVal) => {
    return request({
      url: `${BASE}/rename`,
      method: 'POST',
      data: { data: oldVal, sub: newVal }
    })
  },
  tempDemo: () => {
    return request({
      url: `${BASE}/temp-demo`,
      method: 'GET',
    })
  },
  log: (level) => {
    return request({
      url: `${BASE}/log/${level}`,
      method: 'GET',
    })
  },
  clearLog: (level) => {
    return request({
      url: `${BASE}/log/${level}`,
      method: 'DELETE',
    })
  }
}
