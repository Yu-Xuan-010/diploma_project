const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  lintOnSave: false,
  transpileDependencies: true,
  devServer: {
    port:5000,
    proxy: {
      '/api': {
        target: 'http://192.168.151.141:5000',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src') // 确保 @ 指向 src 目录
      }
    }
  }
}) // 确保结尾没有多余的分号
