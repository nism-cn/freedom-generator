<template>
  <el-collapse-item :name="name">
    <template slot="title">
      <h2><i class="header-icon el-icon-notebook-1"></i> 模板用法</h2>
    </template>

    <el-divider content-position="left">
      <b>变量</b>
    </el-divider>
    <p v-html="baseUsage.$valDef"></p>
    <p v-html="baseUsage.$valUse"></p>

    <el-divider content-position="left">
      <b>分支语句</b>
    </el-divider>
    <p v-html="baseUsage.$if"></p>
    <p v-html="baseUsage.$loop"></p>

    <el-divider content-position="left">
      <b>特殊字符转义</b>
    </el-divider>
    <p v-html="baseUsage.$escape"></p>

    <el-divider content-position="left">
      <b>函数</b>
    </el-divider>
    <b>内置函数</b>
    <p v-html="func.$own.now"></p>
    <b>FG提供函数</b>
    <p v-html="func.$fg.lowerFirst"></p>
    <p v-html="func.$fg.upperFirst"></p>
    <p v-html="func.$fg.toUnderline"></p>
    <p v-html="func.$fg.toCame"></p>
    <p v-html="func.$fg.toPascal"></p>

    <el-divider content-position="left">
      <b>语法糖</b>
    </el-divider>
    <p v-html="lanSugar.$new"></p>
  </el-collapse-item>
</template>

<script>
export default {
  name: "help-freemarker",
  props: ["name"],
  data() {
    return {
      baseUsage: {
        $valDef: `变量定义: <#assign [变量名] = [变量值]>`,
        $valUse: '变量使用: ${变量名}',
        $if: '判断语句: <#if [表达式]>[操作]<&#47#if>',
        $loop: `循环语句: <#list [集合] as [对象]>[操作]<&#47#list>`,
        $escape: "转义: ${r'$'} 输出 '$' / ${r'#'} 输出 '#'",
      },
      func: {
        $own: {
          now: `格式化时间: <#assign datetime = .now?string('yyyy-MM-dd HH:mm:ss')>`
        },
        $fg: {
          lowerFirst: '小写首字母: strUtil.lowerFirst([字符串])',
          upperFirst: '大写首字母: strUtil.upperFirst([字符串])',
          toUnderline: '大驼峰转下划线: strUtil.toUnderline([字符串])',
          toCame: '下划线转小驼峰: strUtil.toCame([字符串])',
          toPascal: '下划线转大驼峰: strUtil.toPascal([字符串])',
        },
      },
      lanSugar: {
        $new: `模板首行标记为 [#ftl] 可用 []代替<> 对于xml类型的模板编辑更为直观`,
      }
    }
  },
}
</script>
