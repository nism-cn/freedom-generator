import axios from 'axios';
import { Message } from 'element-ui';

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE, // baseURL
  timeout: 10000, // 超时
})

service.interceptors.response.use(
  r => {
    let data = r.data;
    console.info("axios=>", data);
    if (!data.ok) {
      let msg = data.msg;
      msg = msg ? msg.replaceAll('\n', '<br>') : "空指针";
      Message.error({ message: msg, dangerouslyUseHTMLString: true });
      return Promise.reject(r)
    }
    return data;
  },
  e => {
    console.error("axios=>", e);
    Message.error("出错拉！！！" + e.message)
    return e;
  }
)

function download(params) {
  var form = document.createElement("form");
  form.style.display = "none";
  form.action = process.env.VUE_APP_API_BASE + params.url;
  form.method = params.method;
  document.body.appendChild(form);
  if (params.params) {
    for (var key in params.params) {
      var input = document.createElement("input");
      input.type = "hidden";
      input.name = key;
      input.value = params[key];
      form.appendChild(input);
    }
  }
  form.submit();
  form.remove();
}

export { service as default, download };

