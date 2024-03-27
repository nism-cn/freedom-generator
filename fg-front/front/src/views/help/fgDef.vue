<template>
  <el-collapse-item :name="name">
    <template slot="title">
      <h2><i class="header-icon el-icon-coordinate"></i>
        核心概念:
        <span class="fg-text-primary">数据源</span> +
        <span class="fg-text-warning">模板</span> +
        <span class="fg-text-danger">配置</span> =
        <span class="fg-text-success">代码</span>
      </h2>
    </template>

    <p>
      &nbsp;&nbsp;
      部分开源后台系统都自带代码生成, 使系统耦合代码生成功能,
      导致生成环境附加不必要的功能, 另带来的系统的臃肿(和项目无关的jar,前端引入无关js),
      增加系统的运行负担.故有感开发了本工具.
    </p>

    <el-steps align-center>
      <el-step status="process" title="1.配置数据源" description="按照JDBC配置数据源" icon="el-icon-coin"></el-step>
      <el-step status="process" title="2.开发模板" description="按照Freemarker模板开发" icon="el-icon-notebook-1"></el-step>
      <el-step status="process" title="3.配置参数" description="根据需求配置参数" icon="el-icon-setting"></el-step>
      <el-step status="process" title="4.生成代码" description="生成或预览代码" icon="el-icon-circle-check"></el-step>
    </el-steps>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-image :src="imgs[0]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <el-image :src="imgs[1]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <el-image :src="imgs[2]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>

      <el-col :span="6">
        <el-image :src="imgs[3]" :preview-src-list="imgs" :style="imgStyle" />
      </el-col>
    </el-row>

    <h3>数据库支持</h3>
    <p>
      <span class="fg-text-primary">兼容情况:</span>
      主流数据库: Mysql, Oracle, Sqlserver 等; &nbsp;
      国产数据库: DM(达梦), gbase(南大通用) 等; &nbsp;
      大数据: hive, kylin 等; &nbsp;
      小型数据库: sqlite, h2 等; &nbsp;
      以及常见数据库等.
    </p>
    <p><span class="fg-text-primary">驱动配置:</span> 请将对应驱动包 <b>xxx_jdbc.jar</b> 复制到 fg.home/libs 目录中(项目内置Mysql和h2驱动)</p>

  </el-collapse-item>
</template>

<script>
export default {
  name: "help-fg-def",
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
    }
  },
}
</script>
