<template>
  <div>
    <main-type v-if="data.type == 'type'" />
    <main-maps v-if="data.type == 'typeMap'" />
  </div>
</template>

<script>
import MainType from '@/views/maps/type'
import MainMaps from '@/views/maps/maps'

export default {
  name: "maps-main",
  props: {
    data: { type: Object }
  },
  components: { MainType, MainMaps },
  data() {
    return {
    }
  },
}
</script>
