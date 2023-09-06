<#-- 自定义变量 -->
<#assign datetime = .now?string('yyyy-MM-dd HH:mm:ss')>
<#assign packageName = table.rootPackage>
<#assign ClassName = table.className, className = strUtil.lowerFirst(ClassName)>
<#assign tableName = table.name, functionName = table.comment, moduleName = table.moduleName, author = table.author>
<#assign businessName = table.businessName, BusinessName = strUtil.upperFirst(businessName)>
<#assign permissionPrefix = moduleName + ":" + businessName>
<#-- 获取 pkColumn -->
<#list columns as column><#if column.pk><#assign pkColumn = column><#break></#if></#list>
<#assign capJavaName = strUtil.upperFirst(pkColumn.javaName)>
<#-- 自定义变量结束 -->
/*
 * @date ${datetime}
 */
import request from '@/utils/request'

// 查询${functionName}列表
export function list${BusinessName}(query) {
  return request({
    url: '/${moduleName}/${businessName}/list',
    method: 'get',
    params: query
  })
}

// 查询${functionName}详细
export function get${BusinessName}(${pkColumn.javaName}) {
  return request({
    url: '/${moduleName}/${businessName}/' + ${pkColumn.javaName},
    method: 'get'
  })
}

// 新增${functionName}
export function add${BusinessName}(data) {
  return request({
    url: '/${moduleName}/${businessName}',
    method: 'post',
    data: data
  })
}

// 修改${functionName}
export function update${BusinessName}(data) {
  return request({
    url: '/${moduleName}/${businessName}',
    method: 'put',
    data: data
  })
}

// 删除${functionName}
export function del${BusinessName}(${pkColumn.javaName}) {
  return request({
    url: '/${moduleName}/${businessName}/' + ${pkColumn.javaName},
    method: 'delete'
  })
}
