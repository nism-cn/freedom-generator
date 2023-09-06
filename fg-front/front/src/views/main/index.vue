<template>
  <div>
    <el-tabs class="gen-main-tabs" :value="tabId" type="card" closable @tab-remove="removeTab" @tab-click="tabClick">
      <el-tab-pane v-for="(item) in tabs" :key="item.id" :name="item.id">
        <template>
          <span slot="label" @contextmenu.prevent="openMenu(item, $event)" @click.middle="closeSelectedTag(item)">
            <i v-if="item.mainType == 'project'" class="el-icon-files"></i>
            <i v-else class="el-icon-document"></i>
            {{ item.name }}
          </span>
          <ul v-show="tabMenuShow" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
            <li @click="closeSelectedTag"><i class="el-icon-close"></i> 关闭当前</li>
            <li @click="closeAllTags(item)"><i class="el-icon-circle-close"></i> 全部关闭</li>
          </ul>
          <temp-main v-if="item.mainType == 'template'" :data="item" />
          <proj-main v-if="item.mainType == 'project'" :data="item" />
          <logs-main v-if="item.mainType == 'log'" :data="item" />
        </template>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>

import { mapState } from 'vuex'
import ProjMain from '@/views/proj/main'
import TempMain from '@/views/temp/main'
import LogsMain from '@/views/logs/main'

export default {
  name: "main-index",
  components: { TempMain, ProjMain, LogsMain },
  data() {
    return {
      top: undefined,
      left: undefined,
    }
  },
  computed: {
    ...mapState({
      tabMenuShow: state => state.app.tabMenuShow,
      tabId: state => state.app.tabId,
      tabs: state => state.app.tabs
    })
  },
  methods: {
    closeSelectedTag(data) {
      this.$store.commit("app/tabMenuShow", false);
      this.removeTab(data instanceof Event ? this.selectedTag.id : data.id);
    },
    openMenu(data, e) {
      this.$store.commit("app/tabMenuShow", true);
      const menuMinWidth = 105
      const offsetLeft = this.$el.getBoundingClientRect().left
      const offsetWidth = this.$el.offsetWidth
      const maxLeft = offsetWidth - menuMinWidth
      const left = e.clientX - offsetLeft + 15

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = e.clientY - 68
      this.selectedTag = data
    },
    closeAllTags() {
      this.$store.commit("app/tabMenuShow", false);
      this.$store.commit("app/clearTabs")
    },
    removeTab(tabId) {
      this.$store.commit("app/tabMenuShow", false);
      this.$store.commit("app/delTab", tabId)
    },
    tabClick(tab) {
      this.$store.commit("app/clickTab", tab.name)
    },
  },
}
</script>
