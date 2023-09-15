<template>
  <el-dialog title="修改生成配置" :visible.sync="innerVisible" width="90%" top="3vh">
    <el-collapse v-model="collapseActive">
      <el-collapse-item title="基本信息" name="1">
        <el-form :model="tableForm" label-width="120px" @submit.prevent.native>
          <el-row>
            <el-col :span="12">
              <el-form-item>
                <template slot="label"> 表名
                  <el-tooltip content="数据库表名,不可修改" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input disabled="disabled" v-model="tableForm.name" />
              </el-form-item>
              <el-form-item>
                <template slot="label"> 功能名
                  <el-tooltip content="类,方法等描述" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input v-model="tableForm.comment" />
              </el-form-item>
              <el-form-item>
                <template slot="label"> Java包
                  <el-tooltip content="生成的包路径,例:org.nism.gen" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input v-model="tableForm.rootPackage" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item>
                <template slot="label"> Java类名
                  <el-tooltip content="实体类名称" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input v-model="tableForm.className" />
              </el-form-item>
              <el-form-item>
                <template slot="label"> 模块名
                  <el-tooltip content="所属模块,例:system (默认取包最后段)" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input v-model="tableForm.moduleName" />
              </el-form-item>
              <el-form-item>
                <template slot="label"> 业务名
                  <el-tooltip content="模块的子集,具体功能,例:user (默认取表名最后段)" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </template>
                <el-input v-model="tableForm.businessName" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-collapse-item>
      <el-collapse-item title="字段信息" name="2">
        <el-table ref="dragTable" :data="columnList" row-key="id" :size="tableSize" v-loading="loading">
          <el-table-column label="字段描述" min-width="10%">
            <template slot-scope="scope">
              <el-input :size="tableSize" v-model="scope.row.comment"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="字段列名" prop="name" min-width="10%" :show-overflow-tooltip="true">
            <template slot="header">字段列名
              <el-tooltip content="有'钥匙'表示为主键" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              {{ scope.row.name }} <i v-if="scope.row.pk" class="el-icon-key" style="color: red;"></i>
            </template>
          </el-table-column>
          <el-table-column label="物理类型" prop="type" width="140" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span v-if="scope.row.digit <= 0" v-text="`${scope.row.type}(${scope.row.size})`.toLowerCase()"></span>
              <span v-else v-text="`${scope.row.type}(${scope.row.size}, ${scope.row.digit})`.toLowerCase()"></span>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="10%">
            <template slot-scope="scope">
              <el-input :size="tableSize" v-model="scope.row.javaName"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="Java类型" width="140">
            <template slot="header">Java类型
              <el-tooltip content="刷新映射" placement="top">
                <el-link type="danger" icon="el-icon-s-unfold"></el-link>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-select :size="tableSize" v-model="scope.row.javaType">
                <el-option v-for="item in types.javaType" :label="item" :value="item" :key="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" width="60">
            <template slot="header">必填
              <el-tooltip content="数据库字段不为空,禁止修改!" placement="top">
                <i class="el-icon-question"></i>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-checkbox :disabled="!scope.row.nullable" v-model="scope.row.required"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="插入" width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.canInsert"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="编辑" width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.canUpdate"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="列表" width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.canList"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询" width="50">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.canSelect"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" width="140">
            <template slot="header">查询方式
              <el-tooltip content="刷新映射" placement="top">
                <el-link type="danger" icon="el-icon-s-unfold"></el-link>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-select :size="tableSize" v-model="scope.row.selectType">
                <el-option v-for="item in types.selectType" :label="item" :value="item" :key="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" width="140">
            <template slot="header">显示类型
              <el-tooltip content="刷新映射" placement="top">
                <el-link type="danger" icon="el-icon-s-unfold"></el-link>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-select :size="tableSize" v-model="scope.row.htmlType">
                <el-option v-for="item in types.htmlType" :label="item" :value="item" :key="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="dictList.length > 0" label="字典类型" min-width="12%">
            <template slot="header">字典类型
              <el-tooltip content="刷新映射" placement="top">
                <el-link type="danger" icon="el-icon-s-unfold"></el-link>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-select :size="tableSize" v-model="scope.row.dictType" clearable filterable placeholder="请选择">
                <el-option v-for="dict in dictList" :key="dict.val" :label="dict.name" :value="dict.val">
                  <span style="float: left">{{ dict.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 10px">{{ dict.val }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-collapse-item>
    </el-collapse>
    <div slot="footer" class="dialog-footer">
      <el-button plain type="primary" @click="modify">保 存</el-button>
      <el-button plain @click="innerVisible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import projectApi from '@/apis/projectApi';
import tableApi from '@/apis/tableApi';

export default {
  name: 'proj-edit',
  props: {
    id: { type: String },
    visible: { type: Boolean, default: false },
  },
  data() {
    return {
      tableSize: "mini",
      collapseActive: ["1", "2"],
      tableForm: {},
      columnList: [],
      types: [],
      loading: false,
      dictList: [],
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
        this.loading = true;
        this.tableForm = {};
        this.columnList = [];
        this.dictList = [];
        projectApi.types().then(r => this.types = r.data);
        tableApi.findOne(this.id).then(r => {
          this.tableForm = r.data;
          this.columnList = r.data.columns;
        }).finally(() => this.loading = false)

        tableApi.dictList(this.id).then(r => {
          this.dictList = r.data;
        })
      }
    },
    modify() {
      let data = { data: this.tableForm, sub: this.columnList };
      tableApi.modify(data).then(() => {
        this.$message.success('修改成功')
      }).finally(() => this.innerVisible = false)
    },
  },
}
</script>

