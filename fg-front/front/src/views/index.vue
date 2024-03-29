<template>
  <el-container class="fg-layout">
    <el-header class="fg-layout-header">
      <div class="header-left">
        <el-image src="/favicon.ico" fit="cover" />
        <span>{{ title }}</span>
      </div>
      <div class="header-center">当前: {{ tabId }}</div>
      <div class="header-right">
        <!-- <fg-link content="获取模版样例" icon="notebook-1" @click="tempDemo" /> -->
        <!-- <fg-link content="更换主题" icon="lollipop" /> -->
        <!-- <el-dropdown trigger="click" size="mini" @command="exportCmd">
          <fg-link content="导出数据" icon="download" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-coin" command="db">数据源</el-dropdown-item>
            <el-dropdown-item icon="el-icon-notebook-1" command="temp">模板</el-dropdown-item>
            <el-dropdown-item icon="el-icon-goods" command="all">所有</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <fg-link content="导入数据" icon="upload2" /> -->
        <el-dropdown trigger="click" size="mini" @command="logCmd">
          <fg-link content="操作日志" icon="receiving" class="icon-color-log" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-question" command="debug">debug</el-dropdown-item>
            <el-dropdown-item icon="el-icon-info" command="info">info</el-dropdown-item>
            <el-dropdown-item icon="el-icon-warning" command="error">error</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <fg-link :svg="true" content="全屏操作" icon="fullscreen" @click="screenfull" />
      </div>
    </el-header>

    <el-container>
      <el-aside width="25px">
        <el-tabs id="leftTabs" class="fg-layout-tabs-item" tab-position="left" @tab-click="leftTabClick"
          v-model="leftTabVal">
          <el-tab-pane name="1"><span slot="label"><i class="el-icon-files icon-color-project"></i>项目</span></el-tab-pane>
          <el-tab-pane name="2"><span slot="label"><i
                class="el-icon-notebook-1 icon-color-template"></i>模板</span></el-tab-pane>
          <el-tab-pane name="3"><span slot="label"><i class="el-icon-s-unfold icon-color-maps"></i>设置</span></el-tab-pane>
        </el-tabs>
      </el-aside>

      <splitpanes class="default-theme fg-layout-splitpanes" @resize="resize">
        <pane v-if="leftShow" size="20">
          <fg-proj v-show="leftTabVal == '1'" />
          <fg-temp v-show="leftTabVal == '2'" />
          <fg-base v-show="leftTabVal == '3'" />
        </pane>

        <pane size="70">
          <fg-main />
        </pane>

        <pane v-if="rightShow" size="10">
          <fg-database v-show="rightTabVal == '1'" />
        </pane>
      </splitpanes>

      <el-aside width="25px">
        <el-tabs id="rightTabs" class="fg-layout-tabs-item" tab-position="right" @tab-click="rightTabClick"
          v-model="rightTabVal">
          <el-tab-pane name="1"><span slot="label"><i
                class="el-icon-coin icon-color-database"></i>数据源</span></el-tab-pane>
        </el-tabs>
      </el-aside>
    </el-container>
    <el-footer class="fg-layout-footer">
      <div class="footer-left">Cosmopolita<u><b class="fg-text-logo">nism</b></u> 海纳百川, 四海一家.</div>
      <div class="footer-center">copyright © nism</div>
      <div class="footer-right">
        <fg-link :svg="true" content="Github" icon="github"
          @click="$browserUtil.open('github', 'https://github.com/nism-cn/freedom-generator')" />
        <fg-link :svg="true" content="Gitee" icon="gitee"
          @click="$browserUtil.open('gitee', 'https://gitee.com/nism/freedom-generator')" />
        <fg-link :svg="true" content="帮助文档" icon="help-doc" @click="helpVisible = true" />
      </div>
    </el-footer>

    <fg-help :visible.sync="helpVisible" />

  </el-container>
</template>

<script>
import { mapState } from 'vuex'
import { Pane, Splitpanes } from 'splitpanes';
import 'splitpanes/dist/splitpanes.css';

import screenfull from 'screenfull'
import FgDatabase from '@/views/database';
import FgMain from '@/views/main';
import FgProj from '@/views/proj';
import FgTemp from '@/views/temp';
import FgBase from '@/views/base';
import FgHelp from '@/views/help';
import ioApi from '@/apis/ioApi';
import Intro from '@/utils/intro';

export default {
  components: { Splitpanes, Pane, FgMain, FgProj, FgTemp, FgBase, FgDatabase, FgHelp },
  name: "app-index",
  data() {
    return {
      title: process.env.VUE_APP_TITLE + ' - ' + process.env.VUE_APP_REVERSION,
      leftTabVal: "1",
      rightTabVal: "1",
      leftShow: true,
      rightShow: true,
      helpVisible: false,
      isFullscreen: false
    }
  },
  computed: {
    ...mapState({ tabId: state => state.app.tabId })
  },
  watch: {
  },
  mounted() {
    // this.setGuide();
  },
  methods: {
    tempDemo() {
      ioApi.tempDemo().then(() => this.$message.success("获取模版样例成功！"));
    },
    leftTabClick(tab, event) {
      console.log(tab, event);
    },
    rightTabClick(tab, event) {
      console.log(tab, event);
    },
    resize(e) {
      console.info('调整大小resize', e);
    },
    logCmd(level) {
      let data = {
        id: `log-${level}`,
        name: `日志[${level}]`,
        mainType: 'log',
        level: level
      }
      this.$store.commit("app/addTab", data);
    },
    exportCmd(cmd) {
      console.log(cmd);
    },
    screenfull() {
      if (!screenfull.isEnabled) {
        this.$message({ message: '您的浏览器不支持全屏', type: 'warning' })
        return false
      }
      screenfull.toggle()
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.isEnabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy() {
      if (screenfull.isEnabled) {
        screenfull.off('change', this.change)
      }
    },
    setGuide() {
      let steps = [
        { element: '#step_1', intro: '步骤1：对应Id为#step_1的元素进行选择提示。', position: 'left' },
        { element: '#step_2', intro: '步骤2：对应Id为#step_2的元素进行选择提示。', position: 'right' },
        { element: '#step_3', intro: '步骤3：对应Id为#step_2的元素进行选择提示。', position: 'top' },
        { element: '#step_4', intro: '步骤4：对应Id为#step_2的元素进行选择提示。', position: 'bottom' },
      ]
      Intro.setOptions({ steps: steps })
      this.$nextTick(() => {
        Intro.start()
      })
      // 退出引导回调函数
      Intro.onexit(function () {
        localStorage.setItem('isShowHelp', 1)
      })
    }
  }
};
</script>
<style scoped>
#leftTabs /deep/.el-tabs__item {
  writing-mode: vertical-lr;
  height: auto;
  padding: 8px 0;
  letter-spacing: 3px;
  line-height: 25px;
}

#rightTabs /deep/.el-tabs__item {
  writing-mode: vertical-lr;
  height: auto;
  padding: 8px 0;
  letter-spacing: 3px;
  line-height: 25px;
}
</style>

