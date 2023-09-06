<template>
  <div id="monaco-editor-box">
    <div id="monaco-editor" ref="monacoEditor" />
  </div>
</template>

<script>
import * as monaco from 'monaco-editor';
export default {
  name: "fg-editor",
  props: {
    diffEditor: { type: Boolean, default: false },
    language: { type: String, default: 'text' },
    theme: { type: String, default: 'vs' },
    options: { type: Object, default() { return {}; } },
    value: String,
  },
  data() {
    return {
      editor: null, //文本编辑器
    };
  },
  watch: {
    options: {
      deep: true,
      handler(options) {
        this.editor && this.editor.updateOptions(options);
      }
    },
    value() {
      this.editor && this.value !== this._getValue() && this._setValue(this.value);
    },

    language() {
      if (!this.editor) return;
      if (this.diffEditor) {      //diff模式下更新language
        const { original, modified } = this.editor.getModel();
        monaco.editor.setModelLanguage(original, this.language);
        monaco.editor.setModelLanguage(modified, this.language);
      } else {
        monaco.editor.setModelLanguage(this.editor.getModel(), this.language);
      }
    },
  },
  mounted() {
    this.initEditor();
  },
  methods: {
    initEditor() {
      const self = this;
      // 初始化编辑器，确保dom已经渲染
      this.editor = monaco.editor.create(self.$el.childNodes[0], {
        value: self.value, // 编辑器初始显示内容
        language: self.language, // 支持语言
        theme: self.theme,
        selectOnLineNumbers: true, //显示行号
        automaticLayout: true,
        ...self.options,
      });
      self.editor.onDidChangeModelContent(function (event) {
        self._emitChange(self._getValue(), event)
      });
    },

    _getEditor() {
      if (!this.editor) return null;
      return this.diffEditor ? this.editor.modifiedEditor : this.editor;
    },

    _setValue(value) {
      let editor = this._getEditor();
      if (editor) return editor.setValue(value);
    },

    _getValue() {
      let editor = this._getEditor();
      return (!editor) ? "" : editor.getValue();
    },

    _emitChange(value, event) {
      this.$emit('change', value, event);
      this.$emit('input', value);
    }
  },

};
</script>

<style scoped>
#monaco-editor {
  width: 100%;
  height: calc(100vh - 142px);
}
</style>
