<template>
  <el-collapse-item :name="name">
    <template slot="title">
      <h2><i class="header-icon el-icon-edit-outline"></i> 基本用法</h2>
    </template>

    <h3>内置变量</h3>
    <el-table size="small" :data="builtInData" style="width: 100%" row-key="id">
      <el-table-column prop="group" label="大类" width="200" />
      <el-table-column prop="field" label="字段" width="200">
        <template slot="header">字段
          <el-tooltip content="绿色属性表示可自定义修改" placement="top">
            <i class="el-icon-question"></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <span :class="scope.row.def ? 'fg-text-success' : 'fg-text-main'">{{ scope.row.field }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="输出类型" width="120" />
      <el-table-column prop="desc" label="说明" />
    </el-table>

    <h3>输出路径</h3>
    <el-table size="small" :data="outPathData" style="width: 100%" row-key="id">
      <el-table-column prop="suffix" label="文件后部分" width="160" />
      <el-table-column prop="outPath" label="生成路径" />
      <el-table-column prop="desc" label="说明" />
    </el-table>
    <p>
      <b>不使用默认生成路径,可在模板文件中自定义</b>
      <b class="fg-text-primary">输出路径: </b> <br>
      <span class="fg-text-danger" v-html="outDef.dir"></span> <br>
      <span class="fg-text-warning" v-html="outDef.eg"></span> <br>
      <span class="fg-text-success" v-html="outDef.result"></span>
    </p>

  </el-collapse-item>
</template>

<script>
export default {
  name: "help-fg-base",
  props: ["name"],
  data() {
    return {
      imgs: [
        require('@/assets/images/dbInfo.png'),
        require('@/assets/images/temp.png'),
        require('@/assets/images/setting.png'),
        require('@/assets/images/code.png'),
      ],
      imgStyle: { height: '250px' },
      builtInData: [{
        id: 101, group: 'table', field: '', desc: '数据库的表; 使用,如: ${table.name}', children: [
          { id: 102, group: 'table', type: 'String', field: 'name', desc: '表名' },
          { id: 103, group: 'table', type: 'String', def: true, field: 'comment', desc: '表描述' },
          { id: 104, group: 'table', type: 'String', def: true, field: 'camel', desc: '小驼峰 tableName' },
          { id: 105, group: 'table', type: 'String', def: true, field: 'upCamel', desc: '大驼峰 TableName' },
          { id: 106, group: 'table', type: 'String', def: true, field: 'underline', desc: '小下划线 table_name' },
          { id: 107, group: 'table', type: 'String', def: true, field: 'upUnderline', desc: '大下划线 TABLE_NAME' },
          { id: 108, group: 'table', type: 'String', def: true, field: 'module', desc: '模块名 取pkg最后段' },
        ]
      }, {
        id: 201, group: 'columns', field: '', desc: '数据库的表字段 (注: 需配合循环语句操作); 使用,如: ${column.name}', children: [
          { id: 202, group: 'columns', type: 'String', field: 'name', desc: '字段名' },
          { id: 208, group: 'columns', type: 'String', def: true, field: 'comment', desc: '字段描述' },
          { id: 204, group: 'columns', type: 'String', def: true, field: 'camel', desc: '小驼峰 columnName' },
          { id: 205, group: 'columns', type: 'String', def: true, field: 'upCamel', desc: '大驼峰 ColumnName' },
          { id: 206, group: 'columns', type: 'String', def: true, field: 'underline', desc: '小下划线 column_name' },
          { id: 207, group: 'columns', type: 'String', def: true, field: 'upUnderline', desc: '大下划线 COLUMN_NAME' },
          { id: 209, group: 'columns', type: 'String', field: 'types', desc: '根据类型映射生成' },
          { id: 210, group: 'columns', type: 'boolean', field: 'pk', desc: '是否为主键' },
          { id: 211, group: 'columns', type: 'boolean', field: 'increment', desc: '是否为自增' },
          { id: 212, group: 'columns', type: 'boolean', field: 'nullable', desc: '是否为空' },
          { id: 213, group: 'columns', type: 'boolean', def: true, field: 'required', desc: '是否为必填 (是否为空的非)' },
          { id: 214, group: 'columns', type: 'int', field: 'size', desc: '字段大小' },
          { id: 215, group: 'columns', type: 'int', field: 'digit', desc: '字段数据长度 (如 decimal(8,2) )' },
          { id: 216, group: 'columns', type: 'String', def: true, field: 'dictType', desc: '自定义设置字典类型' },
        ]
      },],
      outDef: {
        dir: '写法: <#assign _out_path_ = [输出路径]> (\'_out_path_\' 为关键字)',
        eg: '例子: <#assign _out_path_ = "/src/main/" + sets.pkg?replace(".","/") + table.upCamel + "Controller.java" >',
        result: '结果: 文件将输出至 "/src/main/org/nism/demo/HelloWroldController.java" 中'
      },
      outPathData: [
        { id: 101, suffix: '*.sql', outPath: '/sql/${table.className}.sql', desc: '' },
        { id: 102, suffix: '*.java', outPath: '/main/java/${packageLine}/${classLine}.java', desc: 'packageLine: 指在 package 关键字所在行的包路径;  classLine 指在 class 所在类名' },
        { id: 103, suffix: '*Mapper.xml', outPath: '/main/resources/mapper/${table.className}Mapper.xml', desc: '' },
        { id: 104, suffix: '*.js', outPath: '/ui/api/${table.moduleName}/${table.businessName}.js', desc: '' },
        { id: 105, suffix: '*.vue', outPath: '/ui/views/${table.moduleName}/${table.businessName}/index.vue', desc: '' },
        { id: 106, suffix: '*.html', outPath: '/ui/html/${table.moduleName}/${table.businessName}.html', desc: '' },
      ],
    }
  },
}
</script>
