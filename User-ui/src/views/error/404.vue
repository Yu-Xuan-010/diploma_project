<template>
  <div class="error-page">
    <div class="error-code">404</div>
    <div class="error-desc">抱歉，你访问的页面不存在</div>
    <div class="error-handle">
      <el-button type="primary" size="large" @click="goBack">返回上一页</el-button>
      <el-button size="large" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Page404',
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    goHome() {
      this.$router.push('/')
    }
  }
}
</script>

<style lang="scss" scoped>
.error-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #f0f2f5;
}

.error-code {
  font-size: 160px;
  color: #409EFF;
  margin-bottom: 20px;
  line-height: 1;
}

.error-desc {
  font-size: 24px;
  color: #666;
  margin-bottom: 30px;
}

.error-handle {
  .el-button {
    margin: 0 10px;
  }
}
</style>
