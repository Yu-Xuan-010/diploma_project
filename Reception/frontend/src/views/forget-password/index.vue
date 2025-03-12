<template>
  <div class="forget-password-container">
    <el-form ref="forgetForm" :model="forgetForm" :rules="forgetRules" class="forget-form">
      <h3 class="title">重置密码</h3>
      <el-form-item prop="email">
        <el-input v-model="forgetForm.email" type="email" placeholder="请输入注册邮箱">
          <i slot="prefix" class="el-input__icon el-icon-message"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="showVerifyCode">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-input v-model="forgetForm.code" placeholder="请输入验证码">
              <i slot="prefix" class="el-input__icon el-icon-key"></i>
            </el-input>
          </el-col>
          <el-col :span="8">
            <el-button style="width: 100%" :disabled="isCodeSending" @click="sendCode">
              {{ codeButtonText }}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item prop="newPassword" v-if="showNewPassword">
        <el-input v-model="forgetForm.newPassword" type="password" placeholder="请输入新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword" v-if="showNewPassword">
        <el-input v-model="forgetForm.confirmPassword" type="password" placeholder="请确认新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleSubmit">
          {{ submitButtonText }}
        </el-button>
      </el-form-item>
      <div class="tips">
        <router-link to="/login">返回登录</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
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
      if (value !== this.forgetForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      step: 1,
      forgetForm: {
        email: '',
        code: '',
        newPassword: '',
        confirmPassword: ''
      },
      forgetRules: {
        email: [
          { required: true, trigger: 'blur', message: '请输入邮箱地址' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        code: [
          { required: true, trigger: 'blur', message: '请输入验证码' }
        ],
        newPassword: [
          { required: true, trigger: 'blur', message: '请输入新密码' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请确认新密码' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false,
      isCodeSending: false,
      countdown: 60
    }
  },
  computed: {
    showVerifyCode() {
      return this.step >= 2
    },
    showNewPassword() {
      return this.step >= 3
    },
    submitButtonText() {
      return this.step === 1 ? '下一步' : this.step === 2 ? '验证' : '重置密码'
    },
    codeButtonText() {
      return this.isCodeSending ? `${this.countdown}秒后重试` : '获取验证码'
    }
  },
  methods: {
    sendCode() {
      this.isCodeSending = true
      this.countdown = 60
      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--
        } else {
          this.isCodeSending = false
          clearInterval(timer)
        }
      }, 1000)
      // 这里调用发送验证码API
    },
    handleSubmit() {
      this.$refs.forgetForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.step === 1) {
            // 验证邮箱
            this.step = 2
            this.loading = false
          } else if (this.step === 2) {
            // 验证验证码
            this.step = 3
            this.loading = false
          } else {
            // 重置密码
            this.$message.success('密码重置成功')
            this.$router.push('/login')
          }
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

  .forget-form {
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