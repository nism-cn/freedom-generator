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
package ${packageName}.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${packageName}.mapper.${ClassName}Mapper;
import ${packageName}.domain.${ClassName};
import ${packageName}.service.I${ClassName}Service;

/**
 * ${functionName}Service业务层处理
 * 
 * @author ${author}
 * @date ${datetime}
 */
@Service
public class ${ClassName}ServiceImpl implements I${ClassName}Service {
    @Autowired
    private ${ClassName}Mapper ${className}Mapper;

    /**
     * 查询${functionName}
     * 
     * @param ${pkColumn.javaName} ${functionName}主键
     * @return ${functionName}
     */
    @Override
    public ${ClassName} select${ClassName}By${capJavaName}(${pkColumn.javaType} ${pkColumn.javaName}) {
        return ${className}Mapper.select${ClassName}By${capJavaName}(${pkColumn.javaName});
    }

    /**
     * 查询${functionName}列表
     * 
     * @param ${className} ${functionName}
     * @return ${functionName}
     */
    @Override
    public List<${ClassName}> select${ClassName}List(${ClassName} ${className}) {
        return ${className}Mapper.select${ClassName}List(${className});
    }

    /**
     * 新增${functionName}
     * 
     * @param ${className} ${functionName}
     * @return 结果
     */
    @Transactional
    @Override
    public int insert${ClassName}(${ClassName} ${className}) {
        return ${className}Mapper.insert${ClassName}(${className});
    }

    /**
     * 修改${functionName}
     * 
     * @param ${className} ${functionName}
     * @return 结果
     */
    @Transactional
    @Override
    public int update${ClassName}(${ClassName} ${className}) {
        return ${className}Mapper.update${ClassName}(${className});
    }

    /**
     * 批量删除${functionName}
     * 
     * @param ${pkColumn.javaName}s 需要删除的${functionName}主键
     * @return 结果
     */
    @Transactional
    @Override
    public int delete${ClassName}By${capJavaName}s(${pkColumn.javaType}[] ${pkColumn.javaName}s) {
        return ${className}Mapper.delete${ClassName}By${capJavaName}s(${pkColumn.javaName}s);
    }

    /**
     * 删除${functionName}信息
     * 
     * @param ${pkColumn.javaName} ${functionName}主键
     * @return 结果
     */
    @Transactional
    @Override
    public int delete${ClassName}By${capJavaName}(${pkColumn.javaType} ${pkColumn.javaName}) {
        return ${className}Mapper.delete${ClassName}By${capJavaName}(${pkColumn.javaName});
    }

}
