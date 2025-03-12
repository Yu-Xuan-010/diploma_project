<template>
  <div class="forget-password-container">
    <el-form ref="forgetPasswordForm" :model="forgetPasswordForm" :rules="forgetPasswordRules" class="forget-password-form">
      <h3 class="title">重置密码</h3>
      <el-form-item prop="username">
        <el-input v-model="forgetPasswordForm.username" type="text" placeholder="用户名">
          <i slot="prefix" class="el-input__icon el-icon-user"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input v-model="forgetPasswordForm.code" type="text" placeholder="验证码">
          <i slot="prefix" class="el-input__icon el-icon-key"></i>
        </el-input>
        <el-button type="primary" :disabled="codeButtonDisabled" @click="sendCode">
          {{ codeButtonText }}
        </el-button>
      </el-form-item>
      <el-form-item prop="newPassword">
        <el-input v-model="forgetPasswordForm.newPassword" type="password" placeholder="新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input v-model="forgetPasswordForm.confirmPassword" type="password" placeholder="确认新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleResetPassword">
          重置密码
        </el-button>
      </el-form-item>
      <div class="tips">
        <router-link to="/login">返回登录</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { sendVerifyCode, resetPassword } from '@/api/user'

export default {
  name: 'ForgetPassword',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6个字符'))
      } else {
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.forgetPasswordForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      forgetPasswordForm: {
        username: '',
        code: '',
        newPassword: '',
        confirmPassword: ''
      },
      forgetPasswordRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' }
        ],
        code: [
          { required: true, trigger: 'blur', message: '请输入验证码' }
        ],
        newPassword: [
          { required: true, trigger: 'blur', message: '请输入新密码' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请再次输入新密码' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false,
      countdown: 60,
      codeButtonDisabled: false,
      codeButtonText: '获取验证码'
    }
  },
  methods: {
    sendCode() {
      if (!this.forgetPasswordForm.username) {
        this.$message.error('请先输入用户名')
        return
      }
      this.codeButtonDisabled = true
      this.codeButtonText = `${this.countdown}秒后重试`
      const timer = setInterval(() => {
        this.countdown--
        this.codeButtonText = `${this.countdown}秒后重试`
        if (this.countdown <= 0) {
          clearInterval(timer)
          this.codeButtonDisabled = false
          this.codeButtonText = '获取验证码'
          this.countdown = 60
        }
      }, 1000)
      sendVerifyCode(this.forgetPasswordForm.username)
        .then(() => {
          this.$message.success('验证码已发送')
        })
        .catch(error => {
          console.error('发送验证码失败:', error)
          this.$message.error(error.message || '发送验证码失败')
        })
    },
    handleResetPassword() {
      this.$refs.forgetPasswordForm.validate(valid => {
        if (valid) {
          this.loading = true
          const { username, code, newPassword } = this.forgetPasswordForm
          resetPassword({ username, code, newPassword })
            .then(() => {
              this.$message.success('密码重置成功，请登录')
              this.$router.push('/login')
            })
            .catch(error => {
              console.error('重置密码失败:', error)
              this.$message.error(error.message || '重置密码失败')
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.forget-password-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f3f3f3;

  .forget-password-form {
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
      text-align: center;
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