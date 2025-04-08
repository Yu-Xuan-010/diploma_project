<template>
  <div class="login-container">
    <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">用户登录</h3>
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            type="text"
            placeholder="用户名"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" placeholder="密码" @keyup.enter="handleLogin">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click="handleLogin">
          登录
        </el-button>
      </el-form-item>
      <el-form-item class="register-link">
        <span>没有账号？</span>
        <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { User, Lock } from '@element-plus/icons-vue';

export default {
  name: 'Login',
  components: {
    User,
    Lock
  },
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
    async handleLogin() {
      if (!this.$refs.loginFormRef) return;
      
      try {
        await this.$refs.loginFormRef.validate();
        this.loading = true;
        
        console.log('开始登录，表单数据:', this.loginForm);
        
        const result = await this.$store.dispatch('login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        });
        
        console.log('登录结果:', result);
        
        if (result.success) {
          this.$message.success('登录成功');
          const redirect = this.$route.query.redirect || '/home';
          console.log('准备跳转到:', redirect);
          await this.$router.replace(redirect);
        } else {
          this.$message.error(result.message || '登录失败');
        }
      } catch (error) {
        console.error('登录错误:', error);
        this.$message.error(error.message || '登录失败，请重试');
      } finally {
        this.loading = false;
      }
    }
  },
  created() {
    document.getElementById('app').classList.add('login-page');
  },
  destroyed() {
    document.getElementById('app').classList.remove('login-page');
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: url('@/assets/images/bg.jpg') no-repeat center center;
  background-size: cover;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.3);  // 添加半透明遮罩
  }

  .login-form {
    position: relative;  // 确保表单在遮罩层上方
    width: 350px;
    padding: 35px;
    border-radius: 6px;
    background: rgba(255, 255, 255, 0.95);  // 略微透明的白色背景
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);

    .title {
      text-align: center;
      margin: 0 0 30px 0;
      color: #333;
      font-size: 24px;
      font-weight: bold;
    }

    .el-input {
      height: 40px;
      margin-bottom: 10px;

      input {
        height: 40px;
        padding-left: 36px;
      }

      .el-input__icon {
        line-height: 40px;
      }
    }

    .el-button {
      height: 40px;
      font-size: 16px;
    }

    .register-link {
      text-align: center;
      margin-top: 20px;
      
      span {
        color: #666;
      }
    }
  }
}
</style>