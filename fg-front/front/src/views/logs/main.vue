<template>
  <div>
    <div>
      <fg-link content="刷新" placement="start" icon="refresh-right" @click="load" />
      <fg-link content="清除日志" placement="start" icon="delete" @click="clearLog" />
    </div>
    <fg-editor :options="options" :language="'text'" :value="val" />
  </div>
</template>

<script>

import ioApi from '@/apis/ioApi';

export default {
  name: "temp-main",
  props: {
    data: { type: Object }
  },
  data() {
    return {
      val: "",
      options: { readOnly: true, fontSize: 8 },
      interval: undefined
    };
  },
  mounted() {
    this.load();
  },
  methods: {
    load() {
      ioApi.log(this.data.level).then(r => this.val = r.data);
      this.interval = setInterval(() => {
        ioApi.log(this.data.level).then(r => this.val = r.data);
      }, 1000);
    },
    clearLog() {
      ioApi.clearLog(this.data.level).then(r => this.val = r.data);
    }
  },
  beforeDestroy() {
    clearInterval(this.interval);
  }
};
</script>
