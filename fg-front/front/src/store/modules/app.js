export default {
  namespaced: true,
  state: () => ({
    tabs: [],
    tabId: undefined,
    treeMenuShow: false,
    tabMenuShow: false,
  }),
  mutations: {
    addTab(state, tab) {
      state.tabId = tab.id;
      let hasTab = state.tabs.filter(e => e.id === tab.id);
      if (hasTab.length == 0) {
        state.tabs.push(tab);
      }
    },
    clickTab(state, tabId) {
      state.tabId = tabId;
    },
    delTab(state, tabId) {
      let tabs = state.tabs;
      let activeName = state.tabId;
      if (activeName === tabId) {
        tabs.forEach((tab, i) => {
          if (tab.id === tabId) {
            let nextTab = tabs[i + 1] || tabs[i - 1];
            if (nextTab) {
              activeName = nextTab.id;
            }
          }
        });
      }
      state.tabId = activeName;
      state.tabs = tabs.filter(e => e.id !== tabId);
    },
    clearTabs(state) {
      state.tabId = undefined;
      state.tabs = [];
    },
    treeMenuShow(state, flag) {
      state.treeMenuShow = flag;
    },
    tabMenuShow(state, flag) {
      state.tabMenuShow = flag;
    },
  },
}
