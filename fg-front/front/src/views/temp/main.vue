<template>
  <div>
    <div>
      <fg-link content="保存" placement="start" icon="wallet" @click="save" />
      <fg-link content="刷新" placement="start" icon="refresh-right" @click="load" />
    </div>
    <fg-editor ref="editor" @change="change" :options="options" :language="language" :value="val" />
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
      language: "",
      val: "",
      options: { readOnly: true }
    };
  },
  mounted() {
    this.load();
    window.addEventListener('keydown', this.handleEvent)
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleEvent)
  },
  methods: {
    async handleEvent(event) {
      switch (event.keyCode) {
        case 83:
          if (event.ctrlKey && event.code === 'KeyS') {
            console.log('ctrl + s')
            event.preventDefault()
            // 阻止直接保存网页
            event.returnValue = false
            this.save();
          }
          if (event.ctrlKey && event.code === 'KeyS') return false
          break
        case 89:
          console.log('ctrl + y')
          if (event.ctrlKey && event.code === 'KeyY') {
            this.$router.go(+1)
          }
          break
      }
    },
    load() {
      ioApi.read(this.data.relativePath).then(r => {
        let map = new Map();
        map.set('ftl', 'freemarker2');
        map.set('json', 'json');
        this.val = r.data
        this.options.readOnly = false;
        let language = map.get(this.data.suffix);
        this.language = language ? language : 'text';
      });
    },
    save() {
      if (!this.options.readOnly) {
        ioApi.write({ data: this.data.relativePath, sub: this.val }).then(() => {
          this.$message.success("保存成功 - " + this.data.relativePath)
        });
      }
    },
    change(v) {
      this.val = v;
    }
  }
};
</script>
