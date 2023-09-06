<template>
  <div :class="{ 'hidden': hidden }" class="pagination-container">
    <el-pagination :background="background" :current-page.sync="currentPage" :page-size.sync="pageSize" :layout="layout"
      :page-sizes="pageSizes" :pager-count="pagerCount" :total="total" v-bind="$attrs" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { scrollTo } from './scroll-to'

export default {
  name: 'fg-page',
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 50, 100, 500, 1000]
      }
    },
    // 移动端页码按钮的数量端默认值5
    pagerCount: {
      type: Number,
      default: document.body.clientWidth < 992 ? 5 : 7
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    }
  }
}
</script>

<style scoped>
.pagination-container {
  position: relative;
  height: 30px;
  background: #fff;
  padding: 10px;
}

.pagination-container.hidden {
  display: none;
}

.pagination-container .el-pagination {
  right: 20px;
  position: absolute;
}
</style>
