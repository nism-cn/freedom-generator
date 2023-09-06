import axios from 'axios';
import { Message } from 'element-ui';

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE, // baseURL
  timeout: 10000, // 超时
})

// 创建axios下载实例
const downloadService = axios.create({
  baseURL: process.env.VUE_APP_API_BASE,
  timeout: 10000,
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

downloadService.interceptors.request.use(config => {
  config.responseType = 'blob';
  return config;
})

downloadService.interceptors.response.use(
  r => {
    const disposition = r.headers['content-disposition']
    if (!r || !disposition) {
      Message.error(r.message || '下载失败！')
      return
    }
    if (r && r.status === 200 && r.data) {
      const { data, headers } = r;
      // // 切割出文件名
      let fileName = disposition ? disposition.replace(/\w+;filename=(.*)/, '$1') : data.fileName;
      // 此处当返回json文件时需要先对data进行JSON.stringify处理，其他类型文件不用做处理
      const blob = new Blob([data], { type: headers['content-type'] })
      const dom = document.createElement('a')
      dom.href = window.URL.createObjectURL(blob)
      dom.download = decodeURIComponent(fileName)
      dom.style.display = 'none'
      document.body.appendChild(dom)
      dom.click()
      dom.parentNode.removeChild(dom)
      window.URL.revokeObjectURL(r.config.url)
    } else {
      Message.error(r.message || '下载失败！')
    }
  },
  e => {
    console.error("axios=>", e);
    Message.error("出错拉！！！" + e.message)
    return e;
  }
)

export { service as default, downloadService as download };

