<template>

  <div class="fg-hljs">
    <div class="fg-hljs-numbers-pre">
      <pre><code v-html="hlHtml"></code></pre>
      <pre>
          <ul class="fg-hljs-line-numbers">
            <li v-for="i in lines" :key="i" v-html="i"></li>
          </ul>
      </pre>
    </div>
  </div>
</template>

<script>
import hljs from "highlight.js";
import "highlight.js/styles/vs.css";

export default {
  name: "fg-hljs",
  props: {
    language: String,
    code: String,
  },
  data() {
    return {
      hlHtml: null,
      lines: 0
    };
  },
  mounted() {
    const hlHtml = hljs.highlight(this.language, this.code).value;
    this.lines = this.code.split("\n").length;
    this.hlHtml = hlHtml;
  },
};
</script>

<style scoped>
.fg-hljs {
  position: relative;
}

.fg-hljs-numbers-pre {
  padding-left: 40px;
  margin-top: -10px;
}

.fg-hljs-line-numbers {
  position: absolute;
  padding-left: 10px;
  margin-top: -30px;
  top: 0;
  left: 0;
  width: 0;
  text-align: right;
  list-style-type: none;
  color: #ccc;
}
</style>
