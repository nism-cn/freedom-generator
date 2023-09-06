[#ftl]
[#-- 自定义变量 --]
[#-- xml文件使用 中括号[] 代码更直观 --]
[#assign datetime = .now?string('yyyy-MM-dd HH:mm:ss')]
[#assign packageName = table.rootPackage]
[#assign ClassName = table.className, className = strUtil.lowerFirst(ClassName)]
[#assign tableName = table.name, functionName = table.comment, moduleName = table.moduleName, author = table.author]
[#assign businessName = table.businessName, BusinessName = strUtil.upperFirst(businessName)]
[#assign permissionPrefix = moduleName + ":" + businessName]
[#-- 获取 pkColumn --]
[#list columns as column][#if column.pk][#assign pkColumn = column][#break][/#if][/#list]
[#assign capJavaName = strUtil.upperFirst(pkColumn.javaName)]
[#-- 自定义变量结束 --]
<!-- @date ${datetime} -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.mapper.${ClassName}Mapper">
    
    <resultMap type="${ClassName}" id="${ClassName}Result">
[#list columns as column]
        <result property="${column.javaName}" column="${column.name}"/>
[/#list]
    </resultMap>

    <sql id="select${ClassName}Vo">
        select [#list columns as column]${column.name}[#if column_has_next], [/#if][/#list]
        from ${tableName}
    </sql>

    <select id="select${ClassName}List" parameterType="${ClassName}" resultMap="${ClassName}Result">
        <include refid="select${ClassName}Vo"/>
        <where>  
[#list columns as column]
[#assign queryType = column.selectType, javaName = column.javaName, javaType = column.javaType]
[#assign selectType = column.selectType]
[#assign columnName = column.name]
[#assign AttrName = strUtil.upperFirst(javaName)]
[#if column.canSelect]
[#if selectType == "eq"]
            <if test="${javaName} != null> and ${columnName} = ${r'#'}{${javaName}}</if>
[#elseif selectType == "ne"]
            <if test="${javaName} != null> and ${columnName} != ${r'#'}{${javaName}}</if>
[#elseif selectType == "gt"]
            <if test="${javaName} != null> and ${columnName} &gt; ${r'#'}{${javaName}}</if>
[#elseif selectType == "ge"]
            <if test="${javaName} != null> and ${columnName} &gt;= ${r'#'}{${javaName}}</if>
[#elseif selectType == "lt"]
            <if test="${javaName} != null> and ${columnName} &lt; ${r'#'}{${javaName}}</if>
[#elseif selectType == "le"]
            <if test="${javaName} != null> and ${columnName} &lt;= ${r'#'}{${javaName}}</if>
[#elseif selectType == "like"]
            <if test="${javaName} != null> and ${columnName} like concat('%', ${r'#'}{${javaName}}, '%')</if>
[#elseif selectType == "between"]
            <if test="params.begin${AttrName} != null and params.begin${AttrName} != '' and params.end${AttrName} != null and params.end${AttrName} != ''"> 
                and ${columnName} between ${r'#'}{params.begin${AttrName}} and ${r'#'}{params.end${AttrName}}
            </if>
[/#if]
[/#if]
[/#list]
        </where>
    </select>
    
    <select id="select${ClassName}By${capJavaName}" parameterType="${pkColumn.javaType}" resultMap="${ClassName}Result">
        <include refid="select${ClassName}Vo"/>
        where ${pkColumn.name} = ${r'#'}{${pkColumn.javaName}}
    </select>
        
    <insert id="insert${ClassName}" parameterType="${ClassName}"[#--if($pkColumn.increment) useGeneratedKeys="true" keyProperty="$pkColumn.javaName"#end--]">
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
[#list columns as column]
[#if column.name != pkColumn.name || !pkColumn.increment]
            <if test="${column.javaName} != null">${column.name},</if>
[/#if]
[/#list]
         </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
[#list columns as column]
[#if column.name != pkColumn.name || !pkColumn.increment]
            <if test="${column.javaName} != null">${r'#'}{${column.javaName}},</if>
[/#if]
[/#list]
         </trim>
    </insert>

    <update id="update${ClassName}" parameterType="${ClassName}">
        update ${tableName}
        <trim prefix="SET" suffixOverrides=",">
[#list columns as column]
[#if column.name != pkColumn.name]
            <if test="${column.javaName} != null">${column.name} = ${r'#'}{${column.javaName}},</if>
[/#if]
[/#list]
        </trim>
        where ${pkColumn.name} = ${r'#'}{${pkColumn.javaName}}
    </update>

    <delete id="delete${ClassName}By${capJavaName}" parameterType="${pkColumn.javaType}">
        delete from ${tableName} where ${pkColumn.name} = ${r'#'}{${pkColumn.javaName}}
    </delete>

    <delete id="delete${ClassName}By${capJavaName}s" parameterType="String">
        delete from ${tableName} where ${pkColumn.name} in 
        <foreach item="${pkColumn.javaName}" collection="array" open="(" separator="," close=")">${r'#'}{${pkColumn.javaName}}</foreach>
    </delete>
</mapper>