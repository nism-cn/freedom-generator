<template>
  <div id="app">
    <fg-index @click.native="hideMenus"></fg-index>
  </div>
</template>
<script>
import FgIndex from '@/views/index';
export default {
  components: { FgIndex },
  name: 'app',
  methods: {
    hideMenus() {
      this.$store.commit("app/treeMenuShow", false);
      this.$store.commit("app/tabMenuShow", false);
    }
  }
}
</script>
