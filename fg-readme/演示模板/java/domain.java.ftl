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
package ${packageName}.domain;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * ${functionName}对象 ${tableName}
 * 
 * @author ${author}
 * @date ${datetime}
 */
public class ${ClassName} extends BaseEntity {
    private static final long serialVersionUID = 1L;

<#-- entity attribute -->
<#list columns as column>
    /*** ${column.comment} */
<#if column.javaType == 'Date'>
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "${column.comment}", width = 30, dateFormat = "yyyy-MM-dd")
<#else>
    @Excel(name = "${column.comment}")
</#if>
    private ${column.javaType} ${column.javaName};
</#list>

<#-- getter and setter -->
<#list columns as column>
<#assign AttrName = strUtil.upperFirst(column.javaName)>
    public void set${AttrName}($column.javaType $column.javaName) {
        this.${column.javaName} = ${column.javaName};
    }

    public ${column.javaType} get${AttrName}() {
        return ${column.javaName};
    }
</#list>

<#-- toString -->
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
<#list columns as column>
<#assign AttrName = strUtil.upperFirst(column.javaName)>
            .append("${column.javaName}", get${AttrName}())
</#list>
            .toString();
    }
}
