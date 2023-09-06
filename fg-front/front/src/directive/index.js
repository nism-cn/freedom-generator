import clipboard from './module/clipboard'

const install = function (Vue) {
  Vue.directive('clipboard', clipboard)
}

export default install
