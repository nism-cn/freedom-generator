import Vue from 'vue';
import App from './App.vue';

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
ElementUI.Dialog.props.closeOnClickModal.default = false

import '@/assets/styles/main.css';
import 'normalize.css';

import './assets/icons';
import store from './store';

import FgEditor from "@/components/FgEditor";
import FgLink from "@/components/FgLink";
import FgPage from "@/components/FgPage";

import directive from '@/directive';

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(directive);

Vue.component('fg-page', FgPage)
Vue.component('fg-link', FgLink)
Vue.component('fg-editor', FgEditor)

import browserUtil from './utils/browserUtil';
Vue.prototype.$browserUtil = browserUtil;

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')
