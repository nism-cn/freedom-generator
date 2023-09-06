import introJs from 'intro.js'
import 'intro.js/introjs.css'

const intro = introJs()
// 更多配置参数请到官方文档查看
intro.setOptions({
  prevLabel: "上一步",
  nextLabel: "下一步",
  skipLabel: "跳过",
  doneLabel: "完成",
  exitOnOverlayClick: false,  // 点击叠加层时是否退出介绍
  tooltipPosition: 'bottom',/* 引导说明框相对高亮说明区域的位置 */
  // tooltipClass: 'intro-tooltip', /* 引导说明文本框的样式 */
  // highlightClass: 'intro-highlight', /* 说明高亮区域的样式 */
  exitOnEsc: true,/* 是否使用键盘Esc退出 */
  showStepNumbers: false,/* 是否显示说明的数据步骤*/
  keyboardNavigation: true,/* 是否允许键盘来操作 */
  showButtons: true,/* 是否按键来操作 */
  showBullets: true,/* 是否使用点点点显示进度 */
  showProgress: true,/* 是否显示进度条 */
  scrollToElement: true,/* 是否滑动到高亮的区域 */
  overlayOpacity: 0.5, /* 遮罩层的透明度 */
  positionPrecedence: ["bottom", "top", "right", "left"],/* 当位置选择自动的时候，位置排列的优先级 */
  disableInteraction: true, /* 是否禁止与元素的相互关联 */
  hintPosition: 'top-middle', /* 默认提示位置 */
  hintButtonLabel: 'Got it'/* 默认提示内容 */
})

// }).oncomplete(() => {
//   //点击结束按钮后执行的事件
// }).onexit(() => {
//   //点击跳过按钮后执行的事件
// }).start()

export default intro
