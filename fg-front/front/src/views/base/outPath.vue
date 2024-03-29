<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-row>
          <el-col :span="24">
            <el-input v-model="pagePm.search" placeholder="根据名称模糊搜索" class="input-search" size="small"
              @keyup.enter.native="find()">
              <div slot="prepend">
                <fg-link content="新增" icon="plus" @click="addLine" />
              </div>
              <div slot="append">
                <fg-link content="查询" icon="search" @click="find" />
                <fg-link content="重置" icon="refresh" @click="refreshParam" />
              </div>
            </el-input>
          </el-col>
        </el-row>
        <el-table height="81vh" size="medium" :data="configs" @row-dblclick="edit">
          <el-table-column prop="val" :label="`名称`" width="120">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.name }}</span>
              <el-input size="mini" v-show="scope.row.search" v-model="scope.row.name"
                @keyup.enter.native="saveOrUpdate(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="val" :label="`通配`" width="240">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.key }}</span>
              <el-input size="mini" v-show="scope.row.search" v-model="scope.row.key"
                @keyup.enter.native="saveOrUpdate(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="val" :label="`输出路径`" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.val }}</span>
              <el-input size="mini" v-show="scope.row.search" v-model="scope.row.val"
                @keyup.enter.native="saveOrUpdate(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="val" :label="`说明`" :show-overflow-tooltip="true" width="120">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.remark }}</span>
              <el-input size="mini" v-show="scope.row.search" v-model="scope.row.remark"
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
              <fg-link type="danger" v-show="scope.row.id && !scope.row.dis" content="禁用/删除" placement="start"
                icon="delete" @click="del(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import configApi from '@/apis/configApi';

export default {
  name: "base-main-out-path",
  data() {
    return {
      TYPE: 'OUT_PATH',
      configs: [],
      pagePm: {
        dis: false,
        total: 0,
        pageNum: 1,
        pageSize: 1000
      },
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
    init() {
      this.find();
    },
    find() {
      configApi.findByType(this.TYPE, this.pagePm.search).then(r => {
        this.configs = r.data;
      });
    },
    addLine() {
      this.configs.unshift({
        search: true,
        type: this.TYPE
      });
    },
    edit(row) {
      row.search = true;
    },
    saveOrUpdate(row) {
      configApi.saveOrUpdate(row).then(() => {
        this.$message.success("操作成功!")
      }).finally(() => {
        this.init();
      })
      row.search = null;
    },
    del(row) {
      this.$confirm(`${row.name}`, '确定删除选中吗？',
        { type: 'warning', dangerouslyUseHTMLString: true, }
      ).then(() => {
        configApi.delete(row.id).then(() => {
          this.init();
          this.$message.error(`${row.name}已删除！`);
        });
      }).catch(() => { })
    },
    refreshParam() {
      this.find();
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
