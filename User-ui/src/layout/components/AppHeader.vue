<template>
  <div class="app-header">
    <div class="logo">
      <router-link to="/">
        <h1>在线学习平台</h1>
      </router-link>
    </div>
    <el-menu mode="horizontal" :router="true" class="nav-menu">
      <el-menu-item index="/course">课程中心</el-menu-item>
      <el-menu-item index="/learning">学习进度</el-menu-item>
      <el-menu-item index="/discussion">讨论区</el-menu-item>
    </el-menu>
    <div class="right-menu">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="el-dropdown-link">
          {{ userInfo.userName || '未登录' }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="profile">个人中心</el-dropdown-item>
          <el-dropdown-item command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'AppHeader',
  computed: {
    ...mapGetters(['userInfo'])
  },
  methods: {
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/profile')
      } else if (command === 'logout') {
        this.$store.dispatch('user/logout').then(() => {
          this.$router.push('/login')
        })
      }
    }
  }
}
</script>

<style scoped>
.app-header {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.logo {
  width: 200px;
}

.logo h1 {
  margin: 0;
  font-size: 20px;
  color: #409EFF;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
}

.right-menu {
  padding-right: 20px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
</style>
