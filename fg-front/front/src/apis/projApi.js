import request from '@/utils/request'

const BASE = '/proj'

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
  // 单个查询
  findSetting: (id) => {
    return request({
      url: `${BASE}/setting/${id}`,
      method: 'GET'
    })
  },
  // 导入表
  importTables: (dbId, tables) => {
    return request({
      url: `${BASE}/importTables/${dbId}`,
      method: 'POST',
      data: { data: tables }
    })
  },
  // 数据映射类型
  types: () => {
    return request({
      url: `${BASE}/types`,
      method: 'GET',
    })
  }
}
