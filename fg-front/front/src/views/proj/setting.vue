<template>
  <el-dialog title="项目设置" :visible.sync="innerVisible">
    <el-form ref="setsForm" size="small" :model="setsForm" :rules="rules" label-width="120px" @submit.prevent.native>
      <el-form-item label="数据源" prop="dbInfoId">
        <el-select v-model="setsForm.dbInfoId">
          <el-option v-for="item in dbInfoList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="模版" prop="tempPath">
        <el-select v-model="setsForm.tempPath">
          <el-option v-for="item in tempList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="路径/包" prop="pkg">
        <el-input v-model="setsForm.pkg" />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="setsForm.author" />
      </el-form-item>
      <!-- TODO -->
      <!-- <el-form-item label="字典表">
        <el-switch v-model="setsForm.dictUse" />
      </el-form-item>
      <el-form-item v-if="setsForm.dictUse" label="字典表查询sql">
        <el-input type="textarea" :rows="5" v-model="setsForm.dictSql" />
      </el-form-item> -->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button plain type="primary" @click="saveOrUpdate">保 存</el-button>
      <el-button plain @click="innerVisible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import ioApi from '@/apis/ioApi';
import dbInfoApi from '@/apis/dbInfoApi';
import projApi from '@/apis/projApi';
import setsApi from '@/apis/setsApi';

export default {
  name: "proj-sets",
  props: {
    data: { type: Object },
    visible: { type: Boolean, default: false },
  },
  data() {
    return {
      rules: {
        dbInfoId: { required: true, message: '请选择数据源', trigger: 'change' },
        tempPath: { required: true, message: '请选择模板', trigger: 'change' },
        pkg: { required: true, message: '请填写路径或包名', trigger: 'change' },
        author: { required: true, message: '请填写作者', trigger: 'change' },
      },
      dbInfoList: [],
      tempList: [],
      setsForm: {},
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
        projApi.findSetting(this.data.projectId).then(r => this.setsForm = r.data);
        // 获取所有数据源
        dbInfoApi.find().then(r => this.dbInfoList = r.data);
        // 获取所有模版组
        ioApi.temp().then(r => this.tempList = r.data);
      }
    },
    saveOrUpdate() {
      this.$refs['setsForm'].validate((valid) => {
        if (valid) {
          let data = { ...this.setsForm, ...{ projectId: this.data.projectId } };
          setsApi.saveOrUpdate(data).then(r => {
            this.innerVisible = false;
            this.setsForm = r.data;
          })
        }
      });

    },
  }
}
</script>

<style></style>
