<template>
  <div class="app-header">
    <div class="logo">
      <router-link to="/home">
        <h1>在线学习平台</h1>
      </router-link>
    </div>
    <el-menu mode="horizontal" :router="true" class="nav-menu" background-color="#fff">
      <el-menu-item index="/course">课程中心</el-menu-item>
      <el-menu-item index="/learning">学习进度</el-menu-item>
      <el-menu-item index="/discussion">讨论区</el-menu-item>
    </el-menu>
    <div class="right-menu">
      <template v-if="isLoggedIn">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            {{ userInfo.username || '用户' }}
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" link @click="goToLogin">登录</el-button>
        <el-button type="primary" @click="goToRegister">注册</el-button>
      </template>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'AppHeader',
  components: {
    ArrowDown
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    const userInfo = computed(() => store.state.userInfo)
    const isLoggedIn = computed(() => store.getters.isAuthenticated)

    const handleCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'logout':
          store.dispatch('logout')
          ElMessage.success('已退出登录')
          router.push('/login')
          break
      }
    }

    const goToLogin = () => {
      router.push('/login')
    }

    const goToRegister = () => {
      router.push('/register')
    }

    return {
      userInfo,
      isLoggedIn,
      handleCommand,
      goToLogin,
      goToRegister
    }
  }
}
</script>

<style lang="scss" scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .logo {
    width: 200px;
    
    a {
      text-decoration: none;
    }

    h1 {
      margin: 0;
      font-size: 20px;
      color: #409EFF;
      font-weight: 600;
    }
  }

  .nav-menu {
    flex: 1;
    border-bottom: none;
    background-color: transparent;

    :deep(.el-menu-item) {
      height: 60px;
      line-height: 60px;
      font-size: 14px;
      
      &:hover, &.is-active {
        color: #409EFF;
      }
    }
  }

  .right-menu {
    padding-right: 20px;
    display: flex;
    align-items: center;
    gap: 12px;

    .el-dropdown-link {
      cursor: pointer;
      color: #409EFF;
      font-size: 14px;
      display: flex;
      align-items: center;
      gap: 4px;

      .el-icon--right {
        margin-top: 1px;
      }

      &:hover {
        color: #66b1ff;
      }
    }

    .el-button {
      padding: 8px 16px;
      font-size: 14px;
    }
  }
}
</style>
