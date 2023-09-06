import request from '@/utils/request'

const BASE = '/project-setting'

export default {
  // 列表查询
  find: () => {
    return request({
      url: `${BASE}`,
      method: 'GET'
    })
  },
  // 单个查询
  findOne: (id) => {
    return request({
      url: `${BASE}/${id}`,
      method: 'GET'
    })
  },
  // 新增
  create: (data) => {
    return request({
      url: `${BASE}`,
      method: 'POST',
      data: data
    })
  },
  // 更新
  update: (id, data) => {
    return request({
      url: `${BASE}/${id}`,
      method: 'PUT',
      data: data
    })
  },
  // 删除
  delete: (id) => {
    return request({
      url: `${BASE}/${id}`,
      method: 'DELETE'
    })
  },
  // 保存或更新
  saveOrUpdate: (data) => {
    return request({
      url: `${BASE}/saveOrUpdate`,
      method: 'POST',
      data: data
    })
  },
}
