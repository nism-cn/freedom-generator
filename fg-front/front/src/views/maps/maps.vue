<template>
  <div>
    <template>
      <fg-link content="项目设置" placement="start" icon="setting" />
      <fg-link content="导入数据" placement="start" icon="upload" />
      <fg-link content="刷新" placement="start" icon="refresh-right" @click="page" />
    </template>

    <el-row>
      <el-col :span="4">
        <el-menu :default-active="actType" @select="menuSelect">
          <el-menu-item v-for="g in groups" :index="`${g.typeMold}-${g.mapMold}`" :key="`${g.typeMold}-${g.mapMold}`">
            <span slot="title">{{ g.typeMold }} - {{ g.mapMold }}</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col :span="24">
            <el-input disabled v-model="pagePm.search" class="input-search" size="small">
              <div slot="prepend">
                <fg-link content="新增" icon="plus" @click="addLine" />
              </div>
              <div slot="append">
                <fg-link content="查询" icon="search" @click="page" />
                <fg-link content="重置" icon="refresh" @click="refreshParam" />
              </div>
            </el-input>
          </el-col>
        </el-row>
        <el-table height="81vh" size="medium" :data="maps" @row-dblclick="edit">
          <el-table-column prop="val" :label="`类型-${actTypeMold}`">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.typeVal }}</span>
              <el-select size="mini" v-show="scope.row.search" v-model="scope.row.typeId" filterable>
                <el-option v-for="i in typeOptions" :label="i.val" :value="i.id" :key="i.id" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="val" :label="`映射-${actMapMold}`">
            <template slot-scope="scope">
              <span v-show="!scope.row.search">{{ scope.row.mapVal }}</span>
              <el-select size="mini" v-show="scope.row.search" v-model="scope.row.mapId" filterable>
                <el-option v-for="i in mapOptions" :label="i.val" :value="i.id" :key="i.id" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
import typeApi from '@/apis/typeApi';
import typeMapApi from '@/apis/typeMapApi';

export default {
  name: "maps-main-maps",
  data() {
    return {
      types: [],
      typeMap: {},
      maps: [],
      groups: [],
      typeOptions: [],
      mapOptions: [],
      actType: 'DB-JAVA',
      actTypeMold: 'DB',
      actMapMold: 'JAVA',
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
    menuSelect(key) {
      this.actType = key;
      const list = this.actType.split('-');
      this.actTypeMold = list[0];
      this.actMapMold = list[1];
      this.page();
    },
    init() {
      typeApi.find().then(r => {
        this.types = r.data;
        this.typeMap = this.types.reduce((group, o) => {
          const { mold } = o;
          group[mold] = group[mold] ?? [];
          group[mold].push(o);
          return group;
        }, {});
      });
      typeMapApi.groups().then(r => this.groups = r.data);
      this.page();
    },
    page() {
      typeMapApi.listMold(this.actTypeMold, this.actMapMold, this.pagePm).then(r => {
        this.maps = r.data;
      });
    },
    addLine() {
      this.maps.unshift({
        search: true,
        typeId: undefined,
        typeMold: this.actTypeMold,
        mapId: undefined,
        mapMold: this.actMapMold,
      });
      this.typeOptions = this.typeMap[this.actTypeMold];
      this.mapOptions = this.typeMap[this.actMapMold];
    },
    edit(row) {
      row.search = true;
      this.typeOptions = this.typeMap[row.typeMold];
      this.mapOptions = this.typeMap[row.mapMold];
    },
    saveOrUpdate(row) {
      console.log(row);
      typeMapApi.saveOrUpdate(row).then(() => {
        this.$message.success("操作成功!")
      }).finally(() => {
        this.init();
      })
      row.search = null;
    },
    del(row) {
      this.$confirm(`${row.typeVal} -> ${row.mapVal} <br/>删除后映射不存在的关系会显示未 unknown`, '确定删除选中吗？',
        { type: 'warning', dangerouslyUseHTMLString: true, }
      ).then(() => {
        typeMapApi.delete(row.id).then(() => {
          this.init();
          this.$message.error(`${row.typeVal} -> ${row.mapVal} 映射关系已删除！`);
        });
      }).catch(() => { })
    },
    reduction(row) {
      typeMapApi.update(row.id, { dis: false }).then(() => {
        this.$message.success(`${row.val}已还原！`)
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
