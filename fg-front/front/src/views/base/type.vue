<template>
  <div>
    <template>
      <fg-link content="Tips 新增右侧的数据才有数据" placement="start" icon="plus" @click="addType" />
    </template>

    <el-row>
      <el-col :span="4">
        <el-menu :default-active="actType" @select="menuSelect">
          <el-menu-item v-for="i in groups" :key="i" :index="i">{{ i }}</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col :span="24">
            <el-input v-model="pagePm.search" class="input-search" size="small" placeholder="值"
              @keyup.enter.native="page">
              <div slot="prepend">
                <fg-link content="新增" icon="plus" @click="addLine" />
              </div>
              <div slot="append">
                <el-tooltip effect="dark" content="查询禁用" :placement="`bottom-end`" :open-delay="800">
                  <el-checkbox v-model="pagePm.dis"></el-checkbox>&nbsp;
                </el-tooltip>
                <fg-link content="查询" icon="search" @click="page" />
                <fg-link content="重置" icon="refresh" @click="refreshParam" />
              </div>
            </el-input>
          </el-col>
        </el-row>
        <el-table height="81vh" size="medium" :data="types" @row-dblclick="edit">
          <el-table-column prop="val" label="类型">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.val }}</span>
              <el-input size="mini" v-show="scope.row.search" v-model="scope.row.val"
                @keyup.enter.native="saveOrUpdate(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
            <template slot="header">
              <span>操作 </span>
            </template>
            <template slot-scope="scope">
              <fg-link content="确认" v-show="scope.row.search" placement="start" icon="circle-check"
                @click="saveOrUpdate(scope.row)" />
              <fg-link content="编辑" v-show="scope.row.id && !scope.row.search" placement="start" icon="edit"
                @click="edit(scope.row)" />
              <fg-link type="warning" v-show="scope.row.id && !scope.row.dis" content="禁用/删除" placement="start"
                icon="delete" @click="del(scope.row)" />
              <fg-link type="success" v-show="scope.row.id && scope.row.dis" content="还原" placement="start"
                icon="refresh-left" @click="reduction(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <el-dialog class="fg-create-dialog" title="新增基础类型" :visible.sync="createVisible" width="20%" :show-close="false">
      <el-form ref="typeForm" :rules="rules" :model="typeForm" @submit.prevent.native>
        <el-form-item label="" prop="type">
          <el-input onkeypress='return(!/[\\\\/:*?\"<>|]/.test(String.fromCharCode(event.keyCode)))' ref="type"
            v-model="typeForm.type" autocomplete="off" @keyup.enter.native="doAddType"></el-input>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import typeApi from '@/apis/typeApi';

export default {
  name: "base-main-type",
  data() {
    return {
      type: "",
      types: [],
      groups: [],
      actType: 'db',
      createVisible: false,
      pagePm: {
        dis: false,
        total: 0,
        pageNum: 1,
        pageSize: 1000
      },
      typeForm: {},
      rules: {
        type: { required: true, message: '请基础类型名不能为空', trigger: 'change' },
      }
    }
  },
  mounted() {
    //监听ESC
    let self = this;
    this.$nextTick(function () {
      document.addEventListener('keyup', function (e) {
        if (e.keyCode == 27) {
          self.init();
        }
      })
    })
    this.init();
  },
  methods: {
    menuSelect(key) {
      this.actType = key;
      this.page();
    },
    init() {
      this.page();
      typeApi.groups().then(r => this.groups = r.data);
    },
    page() {
      typeApi.listMold(this.actType, this.pagePm).then(r => {
        this.types = r.data;
      });
    },
    addType() {
      this.createVisible = true;
    },
    doAddType() {
      this.$refs['typeForm'].validate((valid) => {
        if (valid) {
          this.createVisible = false;
          this.actType = this.typeForm.type;
          if (this.groups.find(i => i == this.actType)) {
            this.page();
          } else {
            this.groups.push(this.actType);
            this.types = [];
            this.addLine();
          }
          this.typeForm.type = '';
        }
      });
    },
    addLine() {
      this.types.unshift({
        search: true,
        mold: this.actType,
        name: "custom",
      });
    },
    saveOrUpdate(row) {
      console.log(row);
      typeApi.saveOrUpdate(row).then(() => {
        this.$message.success("操作成功!")
      }).finally(() => this.init())
      row.search = null;
    },
    edit(row) {
      row.search = true
    },
    del(row) {
      console.log(row);
      this.$confirm(
        `如果已经有数据关联，请选择禁用，此操作<b style='color: green'>可还原</b>。<br/>您需要确认数据无误后方可删，此操作<b style='color: red'>不可逆</b>！<br/><small style='color: gray'>tips: 点击空白处/关闭按钮可取消本次操作</small>`, '禁用/删除？',
        {
          type: 'warning',
          distinguishCancelAndClose: true,
          dangerouslyUseHTMLString: true,
          cancelButtonClass: 'el-button el-button--danger',
          cancelButtonText: '删除',
          confirmButtonClass: 'el-button el-button--warning',
          confirmButtonText: '禁用'
        }
      ).then(() => {
        console.log("禁用");
        typeApi.updateDis(row.id, true).then(() => {
          this.$message.warning(`${row.val} 已禁用！`)
          this.init();
        });
      }).catch((e) => {
        console.log("删除", e);
        if (e == 'cancel') {
          typeApi.delete(row.id).then(() => {
            this.$message.error(`${row.val} 已删除！`)
            this.init();
          });
        } else {
          this.$message.info('取消操作')
        }
      })
    },
    reduction(row) {
      typeApi.updateDis(row.id, false).then(() => {
        this.$message.success(`${row.val} 已还原！`)
        this.init();
      });
    },
    refreshParam() {
      this.pagePm = {
        dis: false,
        total: 0,
        pageNum: 1,
        pageSize: 1000
      };
      this.page();
    },
  }
}
</script>
<style scoped>
.el-dialog-div {
  height: 80vh;
  overflow-x: hidden;
}
</style>
