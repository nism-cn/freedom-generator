<template>
  <el-collapse-item :name="name">
    <template slot="title">
      <h2><i class="header-icon el-icon-help"></i> Freedom Generator 基本用法</h2>
    </template>

    <!-- 核心概念 -->
    <el-divider content-position="left">
      <p>
        <b>核心概念</b>&nbsp;&nbsp;
        <span class="fg-text-primary">数据库</span> +
        <span class="fg-text-warning">模板</span> +
        <span class="fg-text-danger">配置</span> =
        <span class="fg-text-success">代码</span>
      </p>
    </el-divider>

    <p>
      由来:&nbsp;&nbsp;
      部分开源后台系统都自带代码生成(如:若依,人人开源等), 代码耦合了开发时代码通过模板生成的功能,
      使得线上代码附加的不太必要的功能, 另带来的系统的臃肿(jar体积增大,前端代码增大), 增加系统的运行时间.
      故开发了相对通用的自由代码生成器.
    </p>

    <!-- 用法 -->
    <el-divider content-position="left">
      <b>基本用法</b>
    </el-divider>
    <el-steps simple>
      <el-step status="wait" title="1.配置数据源" icon="el-icon-coin"></el-step>
      <el-step status="process" title="2.开发模板引擎" icon="el-icon-notebook-1"></el-step>
      <el-step status="finish" title="3.配置生成参数" icon="el-icon-setting"></el-step>
      <el-step status="success" title="4.预览/生成代码" icon="el-icon-download"></el-step>
    </el-steps>
    <el-row :gutter="20">
      <el-col :span="6">
        <p style="text-align: center;">请按照JDBC配置方式进行数据源配置</p>
        <el-image :src="imgs[0]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <p style="text-align: center;">请按照Freemarker 模板引擎开发</p>
        <el-image :src="imgs[1]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <p style="text-align: center;">根据您的需求配置生成参数</p>
        <el-image :src="imgs[2]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <p style="text-align: center;">生成您需要的代码</p>
        <el-image :src="imgs[3]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>
    </el-row>

    <!-- 内置字段 -->
    <el-divider content-position="left">
      <b>内置字段</b>
    </el-divider>
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

    <!-- 默认生成路径 -->
    <el-divider content-position="left">
      <b>默认生成路径 (依据文件后部分)</b>
    </el-divider>
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

    <!-- 数据库支持 -->
    <el-divider content-position="left">
      <b>数据库支持</b>
    </el-divider>
    <p>
      <span class="fg-text-primary">兼容大部分数据库:</span>
      主流数据库: Mysql, Oracle, Sqlserver 等; &nbsp;&nbsp;&nbsp;
      国产数据库: DM(达梦), gbase(南大通用) 等; &nbsp;&nbsp;&nbsp;
      大数据: hive, kylin 等; &nbsp;&nbsp;&nbsp;
      小型数据库: sqlite, h2 等; &nbsp;&nbsp;&nbsp;
    </p>
    <p><span class="fg-text-primary">驱动配置:</span> 请将对应驱动包 <b>xxx_jdbc.jar</b> 复制到 fg.home/libs 目录中(项目内置Mysql和h2驱动)</p>

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
          { id: 102, group: 'table', type: 'String', field: 'name', desc: '数据库的表名' },
          { id: 103, group: 'table', type: 'String', def: true, field: 'comment', desc: '数据库的表描述' },
          { id: 104, group: 'table', type: 'String', def: true, field: 'className', desc: '由数据库表名生成的java类名称(大驼峰) ==> FG_HELLO_WROLD -> FgHelloWrold' },
          { id: 105, group: 'table', type: 'String', def: true, field: 'rootPackage', desc: '由配置生成的包名' },
          { id: 106, group: 'table', type: 'String', def: true, field: 'moduleName', desc: '由配置包名生成的模块名(取最后段) ==> org.nism.demo -> demo' },
          { id: 107, group: 'table', type: 'String', def: true, field: 'businessName', desc: '由数据库表名生成的业务名(取最后段) ==> FG_HELLO_WROLD -> wrold' },
          { id: 108, group: 'table', type: 'String', def: true, field: 'author', desc: '由配置生成的作名' },
        ]
      }, {
        id: 201, group: 'columns', field: '', desc: '数据库的表字段 (注: 需配合循环语句操作); 使用,如: ${column.name}', children: [
          { id: 202, group: 'columns', type: 'String', def: true, field: 'name', desc: '数据库的表字段名' },
          { id: 203, group: 'columns', type: 'String', def: true, field: 'comment', desc: '数据库的表字段名' },
          { id: 204, group: 'columns', type: 'String', field: 'type', desc: '数据库的表字段类型' },
          { id: 205, group: 'columns', type: 'boolean', field: 'pk', desc: '数据库的表字段是否为主键' },
          { id: 206, group: 'columns', type: 'boolean', field: 'increment', desc: '数据库的表字段是否为自增' },
          { id: 207, group: 'columns', type: 'boolean', field: 'nullable', desc: '数据库的表字段是否为空' },
          { id: 208, group: 'columns', type: 'boolean', def: true, field: 'required', desc: '数据库的表字段是否为必填 (数据库不为空禁止修改)' },
          { id: 209, group: 'columns', type: 'int', field: 'size', desc: '数据库的表字段大小' },
          { id: 210, group: 'columns', type: 'int', field: 'digit', desc: '数据库的表字段数据长度 (如 decimal(8,2) )' },
          { id: 211, group: 'columns', type: 'boolean', def: true, field: 'canInsert', desc: '设置字段是否需要插入' },
          { id: 212, group: 'columns', type: 'boolean', def: true, field: 'canUpdate', desc: '设置字段是否需要更新' },
          { id: 213, group: 'columns', type: 'boolean', def: true, field: 'canList', desc: '设置字段是否需要列表显示' },
          { id: 214, group: 'columns', type: 'boolean', def: true, field: 'canSelect', desc: '设置字段是否需要查询' },
          { id: 215, group: 'columns', type: 'String', def: true, field: 'selectType', desc: '设置字段的查询方式' },
          { id: 216, group: 'columns', type: 'String', def: true, field: 'javaName', desc: '依据数据库表字段名称,由下划线转小驼峰字段 ==> DEMO_COLUMN -> demoColumn' },
          { id: 217, group: 'columns', type: 'String', def: true, field: 'javaType', desc: '依据数据库表字段类型,转换为对应java类型 ==> varchar -> String ' },
          { id: 218, group: 'columns', type: 'String', def: true, field: 'htmlType', desc: '依据数据库表字段类型,转换为对应html类型 ==> varchar -> input' },
          { id: 219, group: 'columns', type: 'String', def: true, field: 'dictType', desc: '自定义设置字典类型' },
        ]
      },],
      outDef: {
        dir: '写法: <#assign _out_path_ = [输出路径]> (\'_out_path_\' 为关键字)',
        eg: '例子: <#assign _out_path_ = "/src/main/" + table.rootPackage?replace(".","/") + table.name + "Controller.java" >',
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
