<template>
  <div class="register-container">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">用户注册</h3>

      <el-form-item prop="username">
        <el-input v-model="registerForm.username" type="text" placeholder="用户名">
          <i slot="prefix" class="el-input__icon el-icon-user"></i>
        </el-input>
      </el-form-item>

      <el-form-item prop="email">
        <el-input v-model="registerForm.email" type="email" placeholder="邮箱">
          <i slot="prefix" class="el-input__icon el-icon-message"></i>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input v-model="registerForm.password" type="password" placeholder="密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleRegister">
          注册
        </el-button>
      </el-form-item>

      <div class="tips">
        已有账号？<router-link to="/">立即登录</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      registerRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' },
          { min: 2, max: 20, message: '用户名长度必须在2到20个字符之间', trigger: 'blur' }
        ],
        email: [
          { required: true, trigger: 'blur', message: '请输入邮箱' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        password: [
          {required: true, trigger: 'blur', message: '请输入密码'},
          {validator: this.validatePassword, trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, trigger: 'blur', message: '请再次输入密码'},
          {validator: this.validateConfirmPassword, trigger: 'blur'}
        ]
      },
      loading: false
    };
  },
  created() {
    document.getElementById('app').classList.add('register-page');
  },
  destroyed() {
    document.getElementById('app').classList.remove('register-page');
  },
  methods: {
    validatePassword(rule, value, callback) {
      if (value.length < 6) {
        callback(new Error('密码不能少于6个字符'));
      } else {
        callback();
      }
    },
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    },
    async handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            await axios.post('/api/user/register', this.registerForm, {
              headers: {'Content-Type': 'application/json'}
            });
            this.$message.success('注册成功，请登录');
            this.$router.push('/');
          } catch (error) {
            this.$message.error(error.response?.data || '注册失败');
          } finally {
            this.loading = false;
          }
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  display: flex;
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

  .register-form {
    position: relative;  // 确保表单在遮罩层上方
    width: 400px;
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

    .tips {
      text-align: center;
      margin-top: 20px;

      a {
        color: #409EFF;
        text-decoration: none;
        font-weight: bold;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
}
</style>