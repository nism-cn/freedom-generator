import request from '@/utils/request'

const BASE = '/type'

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
  saveOrUpdate(data) {
    return request({
      url: `${BASE}/saveOrUpdate`,
      method: 'POST',
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
  // 分页
  listMold: (mold, params) => {
    return request({
      url: `${BASE}/list/${mold}`,
      method: 'GET',
      params: params
    })
  },
}
