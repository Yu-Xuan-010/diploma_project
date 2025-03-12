<template>
    <div class="login-container">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <h3 class="title">在线学习平台</h3>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名">
            <i slot="prefix" class="el-input__icon el-icon-user"></i>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码">
            <i slot="prefix" class="el-input__icon el-icon-lock"></i>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
            登录
          </el-button>
        </el-form-item>
        <div class="tips">
          <router-link to="/register">注册账号</router-link>
          <router-link to="/forget-password">忘记密码</router-link>
        </div>
      </el-form>
    </div>
  </template>
<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' })
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

  <style lang="scss" scoped>
  .login-container {
    min-height: 100%;
    width: 100%;
    background-color: #2d3a4b;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .login-form {
    width: 400px;
    padding: 35px;
    border-radius: 6px;
    background: #fff;

    .title {
      text-align: center;
      margin: 0 0 30px 0;
      color: #409EFF;
    }

    .tips {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
      font-size: 14px;

      a {
        color: #409EFF;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
  </style>
