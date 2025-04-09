<template>
  <el-config-provider :locale="zhCn">
  <div id="app">
    <app-header v-if="showHeader"/> <!-- 仅在非登录/注册页面显示头部 -->
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
  </el-config-provider>
</template>

<script>
// 引入 AppHeader 组件
import AppHeader from '../src/layout/components/AppHeader.vue';
import zhCn from 'element-plus/es/locale/lang/zh-cn';

export default {
  name: 'App',
  components: {
    AppHeader // 注册 AppHeader 组件
  },
  computed: {
    showHeader() {
      // 在登录和注册页面不显示头部
      return !['login', 'register'].includes(this.$route.name);
    }
  },
  data() {
    return {
      zhCn
    };
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
  width: 100%;
}

/* 添加页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
