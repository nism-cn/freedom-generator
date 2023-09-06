<template>
  <el-dialog :visible.sync="innerVisible" title="帮助文档" width="90%" top="3vh">
    <el-collapse v-model="actName" accordion>
      <help-fg-base :name="'fg-base'" />
      <help-fg-advanced :name="'fg-advanced'" />
      <help-ftl :name="'freemarker'" />
    </el-collapse>
  </el-dialog>
</template>

<script>

import HelpFtl from '@/views/help/freemarker'
import HelpFgBase from '@/views/help/fgBase'
import HelpFgAdvanced from '@/views/help/fgAdvanced'

export default {
  components: { HelpFtl, HelpFgBase, HelpFgAdvanced },
  name: "help-index",
  props: ["visible"],
  computed: {
    innerVisible: {
      get() {
        return this.visible;
      },
      set(v) {
        this.$emit("update:visible", v);
      }
    }
  },
  data() {
    return {
      actName: ["fg-base"]
    }
  },
}
</script>
