<template>
  <div>
    <main-type v-if="data.type == 'type'" />
    <main-maps v-if="data.type == 'typeMap'" />
    <main-out-path v-if="data.type == 'outPath'" />
  </div>
</template>

<script>
import MainType from '@/views/base/type'
import MainMaps from '@/views/base/maps'
import MainOutPath from '@/views/base/outPath'

export default {
  name: "base-main",
  props: {
    data: { type: Object }
  },
  components: { MainType, MainMaps, MainOutPath },
  data() {
    return {
    }
  },
}
</script>
