import request, { download } from '@/utils/request'

const BASE = '/table'

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
  deleteBatch: (ids) => {
    return request({
      url: `${BASE}/batch/${ids}`,
      method: 'DELETE'
    })
  },
  findByProjectId: (projectId, params) => {
    return request({
      url: `${BASE}/project/${projectId}`,
      method: 'GET',
      params: params
    })
  },
  // 修改
  modify: (data) => {
    return request({
      url: `${BASE}/modify`,
      method: 'PUT',
      data: data,
    })
  },
  // 预览
  preview: (id) => {
    return request({
      url: `${BASE}/preview/${id}`,
      method: 'GET'
    })
  },
  // 预览
  generator: (ids) => {
    return download({
      url: `${BASE}/generator/${ids}`,
      method: 'PUT'
    })
  },
  // 获取表名
  dbTables: (settingId) => {
    return request({
      url: `${BASE}/db-tables/${settingId}`,
      method: 'GET'
    })
  },
  // 获取字典表数据
  dictList: (id) => {
    return request({
      url: `${BASE}/dictList/${id}`,
      method: 'GET'
    })
  },
}
