<template>
  <div>
    <template>
      <fg-link content="Tips 新增右侧的数据才有数据" placement="start" icon="plus" @click="addMap" />
    </template>

    <el-row>
      <el-col :span="4">
        <el-menu :default-active="actType" @select="menuSelect">
          <el-menu-item v-for="g in groups" :index="`${g.typeMold}-${g.mapMold}`" :key="`${g.typeMold}-${g.mapMold}`">
            <span slot="title">{{ g.typeMold }} -> {{ g.mapMold }}</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col :span="24">
            <el-input v-model="pagePm.search" disabled class="input-search" size="small">
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
              <span v-show="scope.row.id">{{ scope.row.typeVal }}</span>
              <el-select size="mini" v-show="!scope.row.id" v-model="scope.row.typeId" filterable
                @visible-change="filterDbType">
                <el-option v-for="i in filterTypeOptions" :label="i.val" :value="i.id" :key="i.id" />
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

    <el-dialog class="fg-create-dialog" title="新增类型映射" :visible.sync="createVisible" width="20%" :show-close="false">
      <el-form @submit.prevent.native>
        <el-form-item label="">
          <el-select v-model="map" placeholder="" @change="doAddMap" filterable @visible-change="filterGroups">
            <el-option v-for="i in typeGroups" :key="i" :label="i" :value="i"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import typeApi from '@/apis/typeApi';
import typeMapApi from '@/apis/typeMapApi';
import _ from 'lodash';

export default {
  name: "base-main-maps",
  data() {
    return {
      types: [],
      typeMap: {},
      maps: [],
      groups: [],
      typeGroups: [],
      typeOptions: [],
      filterTypeOptions: [],
      mapOptions: [],
      actType: 'db-java',
      actTypeMold: 'db',
      actMapMold: 'java',
      createVisible: false,
      map: '',
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
      typeApi.groups().then(r => this.typeGroups = r.data);
      typeMapApi.groups().then(r => this.groups = r.data);
      this.page();
    },
    page() {
      typeMapApi.listMold(this.actTypeMold, this.actMapMold, this.pagePm).then(r => {
        this.maps = r.data;
      });
    },
    addMap() {
      this.init();
      this.createVisible = true;
    },
    doAddMap() {
      this.createVisible = false;
      this.groups.push({ typeMold: 'db', mapMold: this.map });
      this.actType = 'db-' + this.map;
      this.actMapMold = this.map;
      this.maps = [];
      this.addLine();
    },
    // filters
    filterGroups(flag) {
      if (flag) {
        let seleced = this.groups.map(i => i.mapMold);
        seleced.push('db');
        this.typeGroups = _.difference(this.typeGroups, seleced);
      }
    },
    filterDbType(flag) {
      console.info(flag)
      this.filterTypeOptions = [];
      if (flag) {
        for (const type of this.typeOptions) {
          console.info(type)
          let exist = false;
          for (const map of this.maps) {
            if (map.typeVal == type.val) {
              exist = true;
              break;
            }
          }
          if (!exist) {
            this.filterTypeOptions.push(type);
          }
        }
      }
      console.info(this.maps, this.typeOptions)
      console.info(this.typeOptions)
      console.info(this.filterTypeOptions)
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
