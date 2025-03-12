<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  name: 'App',
  created() {
    // 检查是否有 token，如果有则获取用户信息
    if (getToken()) {
      this.$store.dispatch('user/getUserInfo')
    }
  }
}
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}

html {
  height: 100%;
  box-sizing: border-box;
}

body {
  height: 100%;
  margin: 0;
  padding: 0;
}

*,
*:before,
*:after {
  box-sizing: inherit;
}
</style>
