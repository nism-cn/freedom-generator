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
    cache: {
      type: 'filesystem',
      allowCollectingMemory: true
    },
    name: process.env.VUE_APP_TITLE,
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
      }),
      new MonacoWebpackPlugin({
        languages: ['freemarker2']
      }),
    ],
  },
  chainWebpack: config => {
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
    // config.optimization.minimizer('terser').tap(options => {
    //   const compress = {
    //     warnings: false,
    //     drop_console: true,
    //     drop_debugger: true,
    //     pure_funcs: ['console.log'],
    //   };
    //   const initCompress = options[0].terserOptions.compress;
    //   options[0].terserOptions.compress = { ...initCompress, ...compress };
    //   return options;
    // })
    // chunk libs
    config.when(process.env.NODE_ENV !== 'development',
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
              vue: {
                name: 'chunk-vue-bucket',
                priority: 12,
                test: /[\\/]node_modules[\\/]_?vue(.*)|[\\/]node_modules[\\/]_?vuex(.*)|[\\/]node_modules[\\/]_?axios(.*)/
              },
              elementUI: {
                name: 'chunk-element-ui',
                priority: 20,
                test: /[\\/]node_modules[\\/]_?element-ui(.*)/
              },
              highlightJS: {
                name: 'chunk-highlight-js',
                priority: 21,
                test: /[\\/]node_modules[\\/]_?highlight.js(.*)/
              },
              monacoEditor: {
                name: 'chunk-monaco-editor',
                priority: 22,
                test: /[\\/]node_modules[\\/]_?monaco-editor(.*)/,
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
    config.plugins.delete('prefetch')
    config.plugins.delete('preload')
  },
})

