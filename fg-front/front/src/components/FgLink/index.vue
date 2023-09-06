<template>
  <el-tooltip effect="dark" :content="content" :placement="`bottom-${placement}`" :open-delay="800">
    <el-link :disabled="disabled" :underline="false" :icon="`el-icon-${icon}`" @click="handleClick"></el-link>
  </el-tooltip>
</template>
<script>
export default {
  name: 'fg-link',
  props: {
    content: {
      type: String,
      default: ''
    },
    placement: {
      type: String,
      default: ''
    },
    icon: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleClick(evt) {
      this.$emit('click', evt);
    }
  }
}
</script>
