<template>
  <el-collapse-item :name="name">
    <template slot="title">
      <h2><i class="header-icon el-icon-help"></i> Freedom Generator 高级用法</h2>
    </template>

    <!-- 参数 -->
    <el-divider content-position="left">
      <b>参数(args): 用于<span class="fg-text-danger">模版</span>文件中, 针对常量不足使用, 例如版权信息 license</b>
    </el-divider>
    <el-table size="small" :data="argsData" style="width: 100%">
      <el-table-column prop="desc" label="说明" />
      <el-table-column prop="in" label="配置" />
      <el-table-column prop="use" label="使用" />
      <el-table-column prop="out" label="结果" />
    </el-table>

    <!-- 参数 -->
    <el-divider content-position="left">
      <b>字典配置: 用于<span class="fg-text-danger">配置</span>中, 针对有字典表的系统的扩充</b>
    </el-divider>
    <p>

    </p>


  </el-collapse-item>
</template>

<script>
export default {
  name: "help-fg-advanced",
  props: ["name"],
  data() {
    return {
      argsData: [
        { id: 1, desc: '基本用法', in: "{ license: 'you license' }", use: '${args.license}', out: 'you license' },
        { id: 2, desc: 'List用法', in: "{ list: ['list1'] }", use: '${args.list[0]}', out: 'list1' },
        { id: 3, desc: 'Map用法', in: "{ map: { key1: 'value1' } }", use: '${args.map.key1}', out: 'value1' },

      ]
    }
  },
}
</script>
