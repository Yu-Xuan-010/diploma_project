<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">用户登录</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" placeholder="用户名">
          <i slot="prefix" class="el-input__icon el-icon-user"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" placeholder="密码" @keyup.enter.native="handleLogin">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
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
    };
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;

          // 用户点击登录按钮时触发的事件
          const data = {
            username: this.loginForm.username,
            password: this.loginForm.password
          };

          axios.post('/api/user/login', data, {
            headers: {
              'Content-Type': 'application/json'
            }
          })
          .then(response => {
            console.log('登录成功:', response.data);
            // 处理登录成功逻辑
            localStorage.setItem('token', response.data.token); // 保存 token
            this.$message.success('登录成功');
            this.$router.push('/'); // 跳转到首页
          })
          .catch(error => {
            console.error('登录失败:', error.response || error);
            // 处理登录失败逻辑
            this.$message.error('登录失败: ' + (error.response?.data || '未知错误'));
          })
          .finally(() => {
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f3f3f3;

  .el-input {
    height: 30px !important;
    font-size: 18px;
  }

  .el-button {
    height: 40px !important;
    font-size: 18px;
  }

  .login-form {
    width: 400px;
    padding: 35px;
    border-radius: 6px;
    background: #ffffff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .title {
      text-align: center;
      margin: 0 0 30px 0;
      color: #333;
    }

    .tips {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;

      a {
        color: #409EFF;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
}
</style>