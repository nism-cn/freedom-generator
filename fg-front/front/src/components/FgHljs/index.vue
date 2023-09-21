<template>
  <pre><code v-html="hlHtml"></code></pre>
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
    };
  },
  mounted() {
    const data = hljs.highlight(this.language, this.code);
    const { value } = data;
    this.hlHtml = value;
  },
};
</script>
