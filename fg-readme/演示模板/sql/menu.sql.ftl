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
-- @date ${datetime}
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}', '1', '1', '${businessName}', '${moduleName}/${businessName}/index', 1, 0, 'C', '0', '0', '${permissionPrefix}:list', '#', 'admin', sysdate(), '', null, '${functionName}菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', '${permissionPrefix}:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', '${permissionPrefix}:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', '${permissionPrefix}:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', '${permissionPrefix}:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${functionName}导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', '${permissionPrefix}:export',       '#', 'admin', sysdate(), '', null, '');