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
package ${packageName}.service;

import java.util.List;
import ${packageName}.domain.${ClassName};

/**
 * ${functionName}Service接口
 * 
 * @author ${author}
 * @date ${datetime}
 */
public interface I${ClassName}Service {
    /**
     * 查询${functionName}
     * 
     * @param ${pkColumn.javaName} ${functionName}主键
     * @return ${functionName}
     */
    ${ClassName} select${ClassName}By${capJavaName}(${pkColumn.javaType} ${pkColumn.javaName});

    /**
     * 查询${functionName}列表
     * 
     * @param ${className} ${functionName}
     * @return ${functionName}集合
     */
    List<${ClassName}> select${ClassName}List(${ClassName} ${className});

    /**
     * 新增${functionName}
     * 
     * @param ${className} ${functionName}
     * @return 结果
     */
    int insert${ClassName}(${ClassName} ${className});

    /**
     * 修改${functionName}
     * 
     * @param ${className} ${functionName}
     * @return 结果
     */
    int update${ClassName}(${ClassName} ${className});

    /**
     * 批量删除${functionName}
     * 
     * @param ${pkColumn.javaName}s 需要删除的${functionName}主键集合
     * @return 结果
     */
    int delete${ClassName}By${capJavaName}s(${pkColumn.javaType}[] ${pkColumn.javaName}s);

    /**
     * 删除${functionName}信息
     * 
     * @param ${pkColumn.javaName} ${functionName}主键
     * @return 结果
     */
    int delete${ClassName}By${capJavaName}(${pkColumn.javaType} ${pkColumn.javaName});
}
