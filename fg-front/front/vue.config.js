const { defineConfig } = require('@vue/cli-service');
const CompressionPlugin = require('compression-webpack-plugin');
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');
process.env.VUE_APP_REVERSION = require('./package.json').version;
const path = require('path');
function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = defineConfig({
  assetsDir: 'static',
  productionSourceMap: false,
  transpileDependencies: true,
  configureWebpack: {
    plugins: [new MonacoWebpackPlugin()]
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },
    plugins: [
      new CompressionPlugin({
        test: /\.(js|css|html|svg)?$/i, // 压缩文件格式
        filename: '[path][base].gz',    // 压缩后的文件名
        algorithm: 'gzip',              // 使用gzip压缩
        minRatio: 0.8,                  // 压缩率小于1才会压缩
        threshold: 10240,               // 压缩率小于1才会压缩
      })
    ],
  },
  chainWebpack: config => {
    // fix monaco bug: Uncaught (in promise) Error: Unexpected usage
    config.plugin('monaco').use(new MonacoWebpackPlugin())
    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
    // chunk libs
    config.when(true,
      config => {
        config
          .plugin('ScriptExtHtmlWebpackPlugin')
          .after('html')
          .use('script-ext-html-webpack-plugin', [{
            inline: /runtime\..*\.js$/
          }])
          .end()
        config
          .optimization.splitChunks({
            chunks: 'all',
            cacheGroups: {
              libs: {
                name: 'chunk-libs',
                test: /[\\/]node_modules[\\/]/,
                priority: 10,
                chunks: 'initial'
              },
              elementUI: {
                name: 'chunk-element-ui',
                priority: 20,
                test: /[\\/]node_modules[\\/]_?element-ui(.*)/
              },
              monacoEditor: {
                name: 'chunk-monaco-editor',
                priority: 20,
                test: /[\\/]node_modules[\\/]_?monaco-editor(.*)/
              },
              commons: {
                name: 'chunk-commons',
                test: resolve('src/components'),
                minChunks: 3,
                priority: 5,
                reuseExistingChunk: true
              }
            }
          })
      },
    )
  },
})

