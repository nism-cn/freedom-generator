<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-input class="input-search" size="small" placeholder="请输入内容" suffix-icon="el-icon-search">
          <div slot="prepend">
            <fg-link id="step_1" content="新增" placement="start" icon="plus" @click="openCreate" />
            <fg-link content="刷新" placement="start" icon="refresh-right" @click="find" />
          </div>
        </el-input>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <div class="link-list" v-for="item in list" :key="item.id">
          <el-link :underline="false" @click="openUpdate(item.id)">
            <i class="el-icon-coin" style="color: #089910"></i>{{ item.name }}
          </el-link>
        </div>
      </el-col>
    </el-row>

    <!-- dialog -->
    <el-dialog title="数据源" :visible.sync="dialogVisible">
      <el-form :model="form" label-width="120px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="URL">
          <el-input v-model="form.jdbcUrl" />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" />
        </el-form-item>
        <el-form-item label="说明">
          <el-input type="textarea" :rows="5" v-model="form.remark" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="form.id" plain type="primary" @click="update">修 改</el-button>
        <el-button v-else plain type="primary" @click="create">创 建</el-button>
        <el-button plain type="info" @click="testConnection">测试连接</el-button>
        <el-button plain @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import databaseApi from '@/apis/databaseApi';

export default {
  name: "database-index",
  data() {
    return {
      dialogVisible: false,
      list: [],
      form: {}
    }
  },
  mounted() {
    this.find();
  },
  methods: {
    find() {
      databaseApi.find().then(r => this.list = r.data);
    },
    testConnection() {
      databaseApi.testConnection(this.form)
        .then(() => this.$message.success("连接成功"))
        .catch((e) => console.error(e));
    },
    openCreate() {
      this.form = {};
      this.dialogVisible = true;
    },
    openUpdate(id) {
      databaseApi.findOne(id).then(r => {
        this.form = r.data;
        this.dialogVisible = true;
      })
    },
    create() {
      databaseApi.create(this.form)
        .then(() => this.$message.success("新增成功"))
        .finally(() => this.closeDialog())
    },
    update() {
      databaseApi.update(this.form.id, this.form)
        .then(() => this.$message.success("修改成功"))
        .finally(() => this.closeDialog())
    },
    closeDialog() {
      this.find();
      this.dialogVisible = false;
    }
  }
}
</script>
