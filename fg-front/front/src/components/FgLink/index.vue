<template>
  <el-tooltip effect="dark" :content="content" :placement="`bottom-${placement}`" :open-delay="800">
    <el-link :href="disabled ? null : href" :disabled="disabled" :underline="false" @click="handleClick" :type="type">
      <svg-icon v-if="svg" :icon-class="`${icon}`" />
      <i v-else :class="`el-icon-${icon}`" />
      <slot></slot>
    </el-link>
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
      default: 'end'
    },
    icon: {
      type: String,
      default: ''
    },
    disabled: Boolean,
    href: String,
    svg: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'default'
    }
  },
  methods: {
    handleClick(event) {
      if (!this.disabled) {
        if (!this.href) {
          this.$emit('click', event);
        }
      }
    }
  }
}
</script>
