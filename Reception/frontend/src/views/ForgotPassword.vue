<template>
  <div class="forget-password-container">
    <el-form ref="resetForm" :model="resetForm" :rules="resetRules" class="reset-form">
      <h3 class="title">重置密码</h3>
      <el-form-item prop="email">
        <el-input v-model="resetForm.email" placeholder="请输入注册邮箱">
          <i slot="prefix" class="el-input__icon el-icon-message"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input v-model="resetForm.code" placeholder="验证码" style="width: 63%">
          <i slot="prefix" class="el-input__icon el-icon-key"></i>
        </el-input>
        <el-button
          :disabled="isGetCodeDisabled"
          class="getcode"
          @click="handleGetCode"
        >
          {{ codeText }}
        </el-button>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="resetForm.password" type="password" placeholder="新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input v-model="resetForm.confirmPassword" type="password" placeholder="确认新密码">
          <i slot="prefix" class="el-input__icon el-icon-lock"></i>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleReset">
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
export default {
  name: 'ForgetPassword',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.resetForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      resetForm: {
        email: '',
        code: '',
        password: '',
        confirmPassword: ''
      },
      resetRules: {
        email: [
          { required: true, trigger: 'blur', message: '请输入邮箱地址' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        code: [
          { required: true, trigger: 'blur', message: '请输入验证码' },
          { min: 6, max: 6, message: '验证码长度为6位', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入新密码' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请再次输入新密码' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      codeText: '获取验证码',
      isGetCodeDisabled: false,
      countdown: 60,
      loading: false
    }
  },
  methods: {
    handleGetCode() {
      this.$refs.resetForm.validateField('email', (valid) => {
        if (!valid) {
          this.isGetCodeDisabled = true
          this.countdown = 60
          this.codeText = this.countdown + 's'
          const timer = setInterval(() => {
            if (this.countdown > 0) {
              this.countdown--
              this.codeText = this.countdown + 's'
            } else {
              clearInterval(timer)
              this.codeText = '获取验证码'
              this.isGetCodeDisabled = false
            }
          }, 1000)

          // 调用发送验证码接口
          this.$store.dispatch('user/sendResetCode', this.resetForm.email)
        }
      })
    },
    handleReset() {
      this.$refs.resetForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/resetPassword', this.resetForm)
            .then(() => {
              this.$message.success('密码重置成功')
              this.$router.push('/login')
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
.forget-password-container {
  min-height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.reset-form {
  width: 400px;
  padding: 35px;
  border-radius: 6px;
  background: #fff;

  .title {
    text-align: center;
    margin: 0 0 30px 0;
    color: #409EFF;
  }

  .getcode {
    width: 35%;
    float: right;
  }

  .tips {
    text-align: right;
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
