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
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<#-- 查询参数 -->    
<#list columns as column>
<#assign comment = column.comment>
<#if column.canSelect>
<#if column.htmlType == "input">
      <el-form-item label="${comment}" prop="${column.javaName}">
        <el-input v-model="queryParams.${column.javaName}" placeholder="请输入${comment}" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
<#elseif column.htmlType == "select" || column.htmlType == "radio">
      <el-form-item label="${comment}" prop="${column.javaName}">
        <el-select v-model="queryParams.${column.javaName}" placeholder="请选择${comment}" clearable>
    <#if dictType != "">
          <el-option v-for="dict in dict.type.${dictType}" :key="dict.value" :label="dict.label" :value="dict.value" />
    <#else>
          <el-option label="请选择字典生成" value="" />
    </#if>
        </el-select>
      </el-form-item>
<#elseif column.htmlType == "datetime">
    <#if column.selectType != "BETWEEN">
      <el-form-item label="${comment}" prop="${column.javaName}">
        <el-date-picker clearable v-model="queryParams.${column.javaName}" type="date" value-format="yyyy-MM-dd" placeholder="请选择${comment}" />
      </el-form-item>
    <#else>
      <el-form-item label="${comment}">
        <el-date-picker v-model="daterange${AttrName}" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" />
      </el-form-item>
    </#if>
</#if>
</#if>
</#list>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <#-- toolbar 按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['${moduleName}:${businessName}:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['${moduleName}:${businessName}:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['${moduleName}:${businessName}:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['${moduleName}:${businessName}:export']">导出</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <#-- table 列表 -->
    <el-table v-loading="loading" :data="${businessName}List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<#list columns as column>
<#assign comment = column.comment>
<#assign javaName = column.javaName>
<#if column.pk>
      <el-table-column label="${comment}" align="center" prop="${javaName}" />
</#if>
<#if column.canList>
<#if column.htmlType == "datetime">
      <el-table-column label="${comment}" align="center" prop="${javaName}" width="180">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.${javaName}, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
<#elseif column.htmlType == "imageUpload">
      <el-table-column label="${comment}" align="center" prop="${javaName}" width="100">
        <template slot-scope="scope"><image-preview :src="scope.row.${javaName}" :width="50" :height="50"/></template>
      </el-table-column>
<#elseif column.dictType??>
      <el-table-column label="${comment}" align="center" prop="${javaName}">
        <template slot-scope="scope">
    <#if column.htmlType == "checkbox">
          <dict-tag :options="dict.type.${column.dictType}" :value="scope.row.${javaName} ? scope.row.${javaName}.split(',') : []"/>
    <#else>
          <dict-tag :options="dict.type.${column.dictType}" :value="scope.row.${javaName}"/>
    </#if>
        </template>
      </el-table-column>
<#else>
      <el-table-column label="${comment}" align="center" prop="${javaName}" />
</#if>
</#if>
</#list>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['${moduleName}:${businessName}:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['${moduleName}:${businessName}:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <#-- dialog 对话框 -->
    <!-- 添加或修改${functionName}对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<#list columns as column>
<#assign comment = column.comment>
<#assign field = column.javaName>
<#assign dictType = column.dictType??>
<#if column.canInsert && !column.pk>
<#if column.htmlType == "input">
        <el-form-item label="${comment}" prop="${field}">
          <el-input v-model="form.${field}" placeholder="请输入${comment}" />
        </el-form-item>
<#elseif column.htmlType == "imageUpload">
        <el-form-item label="${comment}" prop="${field}">
          <image-upload v-model="form.${field}"/>
        </el-form-item>
<#elseif column.htmlType == "fileUpload">
        <el-form-item label="${comment}" prop="${field}">
          <file-upload v-model="form.${field}"/>
        </el-form-item>
<#elseif column.htmlType == "editor">
        <el-form-item label="${comment}">
          <editor v-model="form.${field}" :min-height="192"/>
        </el-form-item>
<#elseif column.htmlType == "select" && "" != $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-select v-model="form.${field}" placeholder="请选择${comment}">
            <el-option v-for="dict in dict.type.${dictType}" :key="dict.value" :label="dict.label" :value="dict.value"/>
          </el-select>
        </el-form-item>
<#elseif column.htmlType == "select" && $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-select v-model="form.${field}" placeholder="请选择${comment}">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
<#elseif column.htmlType == "checkbox" && "" != $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-checkbox-group v-model="form.${field}">
            <el-checkbox v-for="dict in dict.type.${dictType}" :key="dict.value" :label="dict.value">{{dict.label}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
<#elseif column.htmlType == "checkbox" && $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-checkbox-group v-model="form.${field}">
            <el-checkbox>请选择字典生成</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
<#elseif column.htmlType == "radio" && "" != $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-radio-group v-model="form.${field}">
            <el-radio v-for="dict in dict.type.${dictType}" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
<#elseif column.htmlType == "radio" && $dictType>
        <el-form-item label="${comment}" prop="${field}">
          <el-radio-group v-model="form.${field}">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
<#elseif column.htmlType == "datetime">
        <el-form-item label="${comment}" prop="${field}">
          <el-date-picker clearable v-model="form.${field}" type="date" value-format="yyyy-MM-dd" placeholder="请选择${comment}"/>
        </el-form-item>
<#elseif column.htmlType == "textarea">
        <el-form-item label="${comment}" prop="${field}">
          <el-input v-model="form.${field}" type="textarea" placeholder="请输入内容" />
        </el-form-item>
</#if>
</#if>
</#list>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list${BusinessName}, get${BusinessName}, del${BusinessName}, add${BusinessName}, update${BusinessName} } from "@/api/${moduleName}/${businessName}";

export default {
  name: "${BusinessName}",
<#--if(${dicts} != '')
  dicts: [${dicts}],
#end-->
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // ${functionName}表格数据
      ${businessName}List: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
<#list columns as column>
<#if column.canSelect>
        ${column.javaName}: null,
</#if>
</#list>
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
<#list columns as column>
<#if column.required>
<#assign comment = column.comment>
<#assign htmlType = column.htmlType>
        ${column.javaName}: [
          { required: true, message: "${comment}不能为空", trigger: ${(htmlType == "select" || htmlType == "radio")?string("'change'" ,"'blur'")} }
        ],
</#if>
</#list>
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询${functionName}列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
<#list columns as column>
<#if column.htmlType == "datetime" && $column.selectType == "BETWEEN">
<#assign AttrName = strUtil.upperFirst(column.javaName)>
      if (null != this.daterange${AttrName} && '' != this.daterange${AttrName}) {
        this.queryParams.params["begin${AttrName}"] = this.daterange${AttrName}[0];
        this.queryParams.params["end${AttrName}"] = this.daterange${AttrName}[1];
      }
</#if>
</#list>
      list${BusinessName}(this.queryParams).then(response => {
        this.${businessName}List = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
<#list columns as column>
        ${column.javaName}: ${(column.htmlType == "checkbox")?string("[]", "null")},
</#list>
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
<#list columns as column>
<#if column.htmlType == "datetime" && $column.selectType == "BETWEEN">
<#assign AttrName = strUtil.upperFirst(column.javaName)>
      this.daterange${AttrName} = [];
</#if>
</#list>
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.${pkColumn.javaName})
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加${functionName}";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ${pkColumn.javaName} = row.${pkColumn.javaName} || this.ids
      get${BusinessName}(${pkColumn.javaName}).then(response => {
        this.form = response.data;
<#list columns as column>
<#if column.htmlType == "checkbox">
        this.form.$column.javaName = this.form.${column.javaName}.split(",");
</#if>
</#list>
        this.open = true;
        this.title = "修改${functionName}";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.${r'$'}refs["form"].validate(valid => {
        if (valid) {
<#list columns as column>
<#if column.htmlType == "checkbox">
          this.form.$column.javaName = this.form.${column.javaName}.join(",");
</#if>
</#list>
          if (this.form.${pkColumn.javaName} != null) {
            update${BusinessName}(this.form).then(response => {
              this.${r'$'}modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            add${BusinessName}(this.form).then(response => {
              this.${r'$'}modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ${pkColumn.javaName}s = row.${pkColumn.javaName} || this.ids;
      this.${r'$'}modal.confirm('是否确认删除${functionName}编号为"' + ${pkColumn.javaName}s + '"的数据项？').then(function() {
        return del${BusinessName}(${pkColumn.javaName}s);
      }).then(() => {
        this.getList();
        this.${r'$'}modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('${moduleName}/${businessName}/export', {
        ...this.queryParams
      }, `${businessName}_${.now?string('yyyy_MM_dd_HH_mm_ss')}.xlsx`)
    }
  }
};
</script>
