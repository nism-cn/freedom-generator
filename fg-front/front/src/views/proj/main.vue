<template>
  <div>
    <template>
      <fg-link content="项目设置" placement="start" icon="setting" @click="settingVisible = true" />
      <fg-link content="导入数据" placement="start" icon="upload" @click="openImport" />
      <fg-link :disabled="multiple" content="批量生成" placement="start" icon="download" @click="generator" />
      <fg-link :disabled="multiple" content="批量删除" placement="start" icon="delete" @click="deleteTable" />
      <fg-link content="刷新" placement="start" icon="refresh-right" @click="init" />
      <fg-link :disabled="multiple" content="强制刷新" placement="start" icon="refresh" @click="forceRefresh" />
    </template>
    <el-row>
      <el-col :span="24">
        <el-input v-model="pagePm.search" class="input-search" size="small" placeholder="表名/表描述"
          @keyup.enter.native="init">
          <div slot="append">
            <fg-link content="查询" icon="search" @click="init" />
            <fg-link content="重置" icon="refresh" @click="refreshParam" />
          </div>
        </el-input>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-table height="76vh" size="medium" :data="tables" @row-dblclick="openModify"
          @selection-change="selectChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="表名" />
          <el-table-column prop="comment" label="描述" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot="header">
              <span>操作 </span>
              <el-tooltip effect="dark" content="双击行可以进行编辑" placement="right"><i class="el-icon-info"></i></el-tooltip>
            </template>
            <template slot-scope="scope">
              <!-- <fg-link content="开发预览" placement="start" icon="video-camera" @click="init(scope.row)" /> -->
              <fg-link content="预览" placement="start" icon="view" @click="preview(scope.row)" />
              <fg-link content="编辑" placement="start" icon="edit" @click="openModify(scope.row)" />
              <fg-link content="删除" placement="start" icon="delete" @click="deleteTable(scope.row)" />
              <fg-link content="强制刷新" placement="start" icon="refresh" @click="forceRefresh(scope.row)" />
              <fg-link content="生成代码" placement="start" icon="download" @click="generator(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
        <fg-page :total="pagePm.total" :page.sync="pagePm.pageNum" :limit.sync="pagePm.pageSize"
          @pagination="findByProjectId" />
      </el-col>
    </el-row>

    <proj-setting :data="data" :visible.sync="settingVisible" />
    <proj-modify :id="tableId" :visible.sync="modifyVisible" />

    <el-dialog title="导入表" :visible.sync="importVisible">
      <el-form :model="importForm" @submit.prevent.native>
        <el-form-item label="">
          <el-select v-model="importForm.tables" multiple filterable placeholder="请选择"
            :filter-method="tableSelectFilter">
            <el-option v-for="item in tableNameList" :key="item.tableName" :value="item.tableName">
              <span style="float: left" v-text="item.tableName"></span>
              <span style="float: right; color: #8492a6; font-size: 13px" v-text="item.comment"></span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button plain type="primary" @click="createImport">导 入</el-button>
        <el-button plain @click="importVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="代码预览" :visible.sync="viewVisible" width="90%" top="3vh">
      <div class="el-dialog-div">
        <el-tabs class="gen-main-tabs" v-model="previewActiveName" type="card">
          <el-tab-pane v-for="(item) in previewList" :label="item.showName" :name="item.id" :key="item.id">
            <template>
              <div class="tab-pane-border">&nbsp;
                <fg-link content="复制代码" placement="start" icon="document-copy" v-clipboard:copy="item.code"
                  v-clipboard:success="() => $message.success('复制代码成功')" type="warning"></fg-link>&nbsp;
                <fg-link content="复制全路径" placement="start" icon="copy-document" v-clipboard:copy="item.path"
                  v-clipboard:success="() => $message.success('复制全路径成功')" type="warning"></fg-link>&nbsp;
                <fg-link content="复制文件名" placement="start" icon="files"
                  v-clipboard:copy="item.path.substring(item.path.lastIndexOf('/') + 1)"
                  v-clipboard:success="() => $message.success('复制文件名成功')" type="warning"></fg-link>&nbsp;&nbsp;
                <el-tag size="mini"> 输出路径: {{ item.path }}</el-tag>
              </div>
              <div class="tab-pane-border">
                <!-- <fg-editor :options="{ readOnly: true }" :language="item.showLanguage" :value="item.code" /> -->
                <fg-hljs v-if="viewVisible" :language="item.showLanguage" :code="item.code"></fg-hljs>
              </div>
            </template>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import projectApi from '@/apis/projectApi';
import tableApi from '@/apis/tableApi';
import ProjSetting from './setting';
import ProjModify from './modify';

export default {
  name: "proj-main",
  components: { ProjSetting, ProjModify },
  props: {
    data: { type: Object }
  },
  data() {
    return {
      ids: [],
      tableNames: [],
      single: true,
      multiple: true,
      previewList: [],
      pagePm: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      tables: [],
      tableNameList: [],
      tableNameListOld: [],
      settingVisible: false,
      importVisible: false,
      modifyVisible: false,
      tableId: undefined,
      viewVisible: false,
      previewActiveName: "",
      settingForm: {},
      importForm: {
        tables: []
      },
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    settingVisible: "init",
  },
  methods: {
    init() {
      // 获取当前项目设置
      projectApi.findSetting(this.data.projectId).then(r => this.settingForm = r.data);
      // 获取当前项目表
      this.findByProjectId();
    },
    // 多选框选中数据
    selectChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.tableNames = selection.map((item) => item.name);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    tableSelectFilter(search) {
      this.tableNameList = this.tableNameListOld.filter(e => {
        return e.tableName.includes(search) || e.comment.includes(search)
      })
    },
    preview(data) {
      tableApi.preview(data.id).then(r => {
        this.previewList = r.data;
        this.previewActiveName = r.data[0].id;
        this.viewVisible = true;
      })
    },
    findByProjectId() {
      tableApi.findByProjectId(this.data.projectId, this.pagePm).then(r => {
        this.tables = r.page.rows;
        this.pagePm.total = r.page.total;
      });
    },
    openImport() {
      this.importForm = {};
      this.importVisible = true;
      tableApi.dbTables(this.settingForm.id).then(r => {
        this.tableNameList = r.data;
        this.tableNameListOld = r.data;
      }).catch(() => {
        this.importVisible = false;
        this.settingVisible = true;
      })
    },
    openModify(data) {
      this.modifyVisible = true;
      this.tableId = data.id;
    },
    deleteTable(row) {
      const ids = row.id || this.ids;
      this.$confirm(`确定删除选中吗？`, '提示', { type: 'warning' }).then(() => {
        tableApi.deleteBatch(ids).then(() => this.init());
      }).catch(() => { })
    },
    forceRefresh(row) {
      const ids = row.id || this.ids;
      const tableNames = row.name ? [row.name] : this.tableNames;
      this.$confirm(
        `<b>确定<b style="color:red">强制</b>刷新选中吗？</b> <br> 既为刷新,会删除表结构,数据也会清空!`,
        '提示',
        { type: 'warning', dangerouslyUseHTMLString: true, }
      ).then(() => {
        Promise.all([
          tableApi.deleteBatch(ids),
          projectApi.importTables(this.settingForm.id, tableNames)
        ]).then(() => {
          this.init();
          this.importVisible = false;
          this.$message.success('刷新成功')
        })
      }).catch(() => { })
    },
    generator(row) {
      const ids = row.id || this.ids;
      tableApi.generator(ids);
    },
    createImport() {
      projectApi.importTables(this.settingForm.id, this.importForm.tables).then(r => {
        console.info(r);
        this.init();
        this.importVisible = false;
      })
    },
    refreshParam() {
      this.pagePm = {
        total: 0,
        pageNum: 1,
        pageSize: 10
      };
      this.init();
    },
    del(data) {
      projectApi.delete(data.id).then(() => {
        this.init();
      })
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
