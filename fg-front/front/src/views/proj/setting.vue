<template>
  <el-dialog title="项目设置" :visible.sync="innerVisible">
    <el-form size="small" :model="settingForm" label-width="120px" @submit.prevent.native>
      <el-form-item label="数据源">
        <el-select v-model="settingForm.dbInfoId">
          <el-option v-for="item in dbInfoList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="模版">
        <el-select v-model="settingForm.tempPath">
          <el-option v-for="item in tempList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="Java包路径">
        <el-input v-model="settingForm.rootPackage" />
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="settingForm.author" />
      </el-form-item>
      <el-form-item label="忽略表前缀">
        <el-input v-model="settingForm.ignoreTablePrefix" />
      </el-form-item>
      <el-form-item label="忽略表字段">
        <el-input type="textarea" :rows="3" v-model="settingForm.ignoreColumn" />
      </el-form-item>
      <el-form-item label="字典表">
        <el-switch v-model="settingForm.dictUse" />
      </el-form-item>
      <el-form-item v-if="settingForm.dictUse" label="字典表查询sql">
        <el-input type="textarea" :rows="5" v-model="settingForm.dictSql" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button plain type="primary" @click="saveOrUpdate">保 存</el-button>
      <el-button plain @click="innerVisible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import ioApi from '@/apis/ioApi';
import databaseApi from '@/apis/databaseApi';
import projectApi from '@/apis/projectApi';
import projectSettingApi from '@/apis/projectSettingApi';

export default {
  name: "proj-setting",
  props: {
    data: { type: Object },
    visible: { type: Boolean, default: false },
  },
  data() {
    return {
      dbInfoList: [],
      tempList: [],
      settingForm: {},
    }
  },
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
  watch: {
    visible: "init"
  },
  methods: {
    init() {
      if (this.visible) {
        // 获取当前项目设置
        projectApi.findSetting(this.data.projectId).then(r => this.settingForm = r.data);
        // 获取所有数据源
        databaseApi.find().then(r => this.dbInfoList = r.data);
        // 获取所有模版组
        ioApi.temp().then(r => this.tempList = r.data);
      }
    },
    saveOrUpdate() {
      let data = { ...this.settingForm, ...{ projectId: this.data.projectId } };
      projectSettingApi.saveOrUpdate(data).then(r => {
        this.innerVisible = false;
        this.settingForm = r.data;
      })
    },
  }
}
</script>

<style></style>
