<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-input class="input-search" size="small" placeholder="请输入内容" suffix-icon="el-icon-search">
          <div slot="append">
            <fg-link content="新增" icon="plus" @click="openCreate(cmd.mkProj)" />
            <fg-link content="隐藏" icon="minus" />
          </div>
        </el-input>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-tree ref="projectTree" :data="list" :default-expand-all="true" node-key="absolutePath"
          :props="{ label: 'name' }" :expand-on-click-node="false" @node-click="handleNodeClick"
          @node-contextmenu="contextmenu">
          <span class="gen-tree-node" slot-scope="{ node }">
            <span><i class="el-icon-s-unfold"></i> {{ node.label }}</span>
          </span>
        </el-tree>
      </el-col>
    </el-row>

    <el-dialog class="fg-create-dialog" :title="op.title" :visible.sync="createVisible" width="20%" :show-close="false">
      <el-form :model="form" @submit.prevent.native>
        <el-form-item label="">
          <el-input onkeypress='return(!/[\\\\/:*?\"<>|]/.test(String.fromCharCode(event.keyCode)))' ref="projName"
            v-model="form.name" autocomplete="off" @keyup.enter.native="operation"></el-input>
        </el-form-item>
      </el-form>
    </el-dialog>

    <div v-show="treeMenuShow" class="fg-tree-menu-div" :style="menuStyle">
      <el-button size="small" class="fg-tree-menu" @click="openCreate(cmd.mkProj)">新建项目</el-button>
      <el-button size="small" class="fg-tree-menu" @click="openCreate(cmd.rename)">重命名</el-button>
      <el-button size="small" class="fg-tree-menu" @click="remove">删除</el-button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import projectApi from '@/apis/projectApi';

export default {
  name: "maps-index",
  data() {
    return {
      count: 0,
      list: [{
        "id": "基础数据",
        "name": "基础数据",
        "mainType": "maps",
        "type": "type",
      }, {
        "id": "映射数据",
        "name": "映射数据",
        "mainType": "maps",
        "type": "typeMap",
      }],
      treeClickData: {},
      createVisible: false,
      form: {},
      menuStyle: {},
      op: {},
      cmd: {
        mkProj: { type: 'mkProj', title: '新建项目' },
        rename: { type: 'rename', title: '重命名' },
        del: { type: 'del', title: '删除' },
      },
    }
  },
  computed: {
    ...mapState({ treeMenuShow: state => state.app.treeMenuShow })
  },
  mounted() {
    // this.find();
  },
  methods: {
    handleNodeClick(data) {
      this.count++;
      console.info(data);
      if (this.count > 2) {
        return;
      }
      this.treeClickData = data;
      setTimeout(() => {
        // 双击
        if (this.count == 2) {
          this.$store.commit("app/addTab", data);
        }
        this.count = 0;
      }, 300)
    },
    openCreate(data) {
      this.$store.commit("app/treeMenuShow", false);
      this.form = { name: undefined };
      this.op = data;
      this.createVisible = true;
      this.form.name = data.type == 'rename' ? this.treeClickData.name : undefined;
      this.$nextTick(() => this.$refs.projName.focus());
    },
    operation() {
      let type = this.op.type;
      if (type == 'mkProj') {
        projectApi.create(this.form).then((r) => {
          this.$store.commit("app/addTab", r.data);
          this.closeDialog();
        });
      } else if (type == 'rename') {
        projectApi.update(this.treeClickData.id, this.form).then(() => {
          this.closeDialog();
        });
      }
    },
    closeDialog() {
      this.createVisible = false;
      // this.find();
    },
    del(data) {
      projectApi.delete(data.id).then(() => {
        // this.find();
        this.$store.commit("app/delTab", data.id);
      })
    },
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
      this.treeClickData = data
      this.changeMenuStyle(e);
    },
    changeMenuStyle(e) {
      this.menuStyle = {
        left: e.x + 'px',
        top: e.y + 'px',
      };
    },
  }
}
</script>
