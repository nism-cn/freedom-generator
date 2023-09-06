<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-input class="input-search" size="small" placeholder="请输入内容" suffix-icon="el-icon-search">
          <div slot="append">
            <fg-link content="刷新" placement="end" icon="refresh-right" @click="init" />
            <fg-link content="全部展开" placement="end" icon="sort" @click="expandAll(true)" />
            <fg-link content="全部折叠" placement="end" icon="sort-up" @click="expandAll(false)" />
            <el-dropdown trigger="click" @command="openCreate">
              <fg-link id="step_2" content="新建" placement="end" icon="plus" />
              <el-dropdown-menu size="mini" slot="dropdown">
                <el-dropdown-item :command="cmd.group" icon="el-icon-plus">模板组</el-dropdown-item>
                <el-dropdown-item :disabled="!treeClickData.directory" :command="cmd.mkdir"
                  icon="el-icon-folder-add">目录</el-dropdown-item>
                <el-dropdown-item :disabled="!treeClickData.directory" :command="cmd.touch"
                  icon="el-icon-document-add">文件</el-dropdown-item>
                <el-dropdown-item :disabled="!treeClickData.group" :command="cmd.args"
                  icon="el-icon-odometer">参数</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <fg-link content="隐藏" placement="end" icon="minus" />
          </div>
        </el-input>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-tree ref="tree" :data="treeList" :default-expand-all="true" node-key="absolutePath" :props="{ label: 'name' }"
          :expand-on-click-node="false" @node-click="handleNodeClick" @node-contextmenu="contextmenu">
          <span class="gen-tree-node" slot-scope="{ node, data }">
            <span>
              <i v-if="data.name == 'args'" class="el-icon-odometer"></i>
              <i v-else-if="data.directory" :class="node.expanded ? 'el-icon-folder-opened' : 'el-icon-folder'"></i>
              <i v-else class="el-icon-document"></i>
              {{ node.label }}
            </span>
          </span>
        </el-tree>
      </el-col>
    </el-row>

    <el-dialog class="fg-create-dialog" :title="title" :visible.sync="createVisible" width="20%" :show-close="false">
      <span slot="title">
        <span v-if="type == 'group' || type == 'rename'">{{ title }}</span>
        <span v-else>在 {{ treeClickData.name }} 下{{ title }} </span>
      </span>
      <el-form :model="form" @submit.prevent.native>
        <el-form-item label="">
          <el-input onkeypress='return(!/[\\\\/:*?\"<>|]/.test(String.fromCharCode(event.keyCode)))' ref="urlRef"
            v-model="form.url" autocomplete="off" @keyup.enter.native="create">
            <template v-if="suffixShow || (type != 'group' && treeClickData.suffix == 'ftl')"
              slot="append">.ftl</template>
          </el-input>
        </el-form-item>
      </el-form>
    </el-dialog>

    <div v-show="treeMenuShow" class="fg-tree-menu-div" :style="menuStyle">
      <el-button size="small" class="fg-tree-menu" :disabled="!isDir" @click="openCreate(cmd.mkdir)">新建目录</el-button>
      <el-button size="small" class="fg-tree-menu" :disabled="!isDir" @click="openCreate(cmd.touch)">新建文件</el-button>
      <el-button size="small" class="fg-tree-menu" :disabled="isArgs" @click="openCreate(cmd.rename)">重命名</el-button>
      <el-button size="small" class="fg-tree-menu" @click="remove">删除</el-button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import ioApi from '@/apis/ioApi';

export default {
  name: "temp-index",
  data() {
    return {
      count: 0,
      treeList: [],
      treeClickData: {},
      createVisible: false,
      type: "",
      title: "",
      form: {},
      cmd: {
        group: { type: 'group', title: '新建模板组' },
        mkdir: { type: 'mkdir', title: '新建目录' },
        touch: { type: 'touch', title: '新建文件' },
        args: { type: 'args', title: '新建参数' },
        rename: { type: 'rename', title: '重命名' },
      },
      suffixShow: false,
      menuStyle: {},
      isDir: false,
      isArgs: false,
    }
  },
  computed: {
    ...mapState({ treeMenuShow: state => state.app.treeMenuShow })
  },
  mounted() {
    this.init();
  },
  methods: {
    remove() {
      this.$store.commit("app/treeMenuShow", false);
      this.$confirm(`此操作将永久删除 [${this.treeClickData.name}], 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.del(this.treeClickData);
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    contextmenu(e, data) {
      this.$store.commit("app/treeMenuShow", true);
      this.isDir = this.treeClickData.directory
      this.isArgs = this.treeClickData.name == 'args'
      this.treeClickData = data
      this.changeMenuStyle(e);
    },
    init() {
      ioApi.fileTree().then(r => {
        this.treeList = r.data
      })
    },
    handleNodeClick(data) {
      this.$store.commit("app/treeMenuShow", false);
      this.count++;
      if (this.count > 2) {
        return;
      }
      this.treeClickData = data;
      setTimeout(() => {
        // 双击
        if (this.count == 2) {
          this.$nextTick(() => {
            this.$refs.tree.store.nodesMap[data.absolutePath].expanded = !this.$refs.tree.store.nodesMap[data.absolutePath].expanded;
            if (data.file) {
              this.$store.commit("app/addTab", data);
            }
          })
        }
        this.count = 0;
      }, 300)
    },
    /**
     * 展开/收起
     */
    expandAll(expanded) {
      this.expandNodes(this.$refs.tree.store.root, expanded);
    },
    expandNodes(node, expanded) {
      node.expanded = expanded;
      for (let i = 0; i < node.childNodes.length; i++) {
        node.childNodes[i].expanded = expanded;
        if (node.childNodes[i].childNodes.length > 0) {
          this.expandNodes(node.childNodes[i], expanded);
        }
      }
    },
    openCreate(data) {
      this.$store.commit("app/treeMenuShow", false);
      if (data.type == 'args') {
        this.createArgs();
      } else {
        this.form = { url: undefined };
        this.title = data.title;
        this.type = data.type;
        this.createVisible = true;
        this.suffixShow = data.type == 'touch';
        this.form.url = data.type == 'rename' ? this.treeClickData.name : undefined;
        this.$nextTick(() => this.$refs.urlRef.focus());
      }
    },
    createArgs() {
      let path = this.treeClickData.relativePath + '/args.json';
      ioApi.touch(path).then(() => this.createDone())
    },
    create() {
      let basePath = this.treeClickData ? this.treeClickData.relativePath + "/" : "";
      if (this.type == 'group') {
        ioApi.mkdir(this.form.url).then(() => this.createDone())
      } else if (this.type == 'mkdir') {
        ioApi.mkdir(basePath + this.form.url).then(() => this.createDone())
      } else if (this.type == 'touch') {
        ioApi.touch(basePath + this.form.url + ".ftl").then(() => this.createDone())
      } else if (this.type == 'rename') {
        let name = this.treeClickData.suffix == 'ftl' ? this.form.url + '.ftl' : this.form.url;
        this.renameDo(this.treeClickData.relativePath, name);
      } else {
        console.error(111);
      }
    },
    createDone() {
      this.createVisible = false;
      this.$message.success('创建成功!');
      this.init();
    },
    del(data) {
      ioApi.del(data.id).then(() => {
        this.$message.success('删除成功!');
        this.init();
      })
    },
    renameDo(oldVal, newVal) {
      ioApi.rename(oldVal, newVal).then(() => {
        this.createVisible = false;
        this.$message.success('重命名成功!');
        this.init();
      });
    },
    changeMenuStyle(e) {
      this.menuStyle = {
        left: e.x + 'px',
        top: e.y + 'px',
      };
    },
  },
}
</script>
<style scoped></style>
