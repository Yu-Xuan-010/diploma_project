<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <div class="user-info">
            <div class="avatar-container">
              <el-avatar :size="120" :src="userInfo.avatar"></el-avatar>
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="uploadAvatar"
              >
                <div class="avatar-mask">
                  <i class="el-icon-camera"></i>
                  <span>更换头像</span>
                </div>
              </el-upload>
            </div>
            <h2 class="user-name">{{ userInfo.nickname || userInfo.username }}</h2>
            <p class="user-role">{{ userInfo.roleName }}</p>
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.learningDays }}</div>
                <div class="stat-label">学习天数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.completedCourses }}</div>
                <div class="stat-label">完成课程</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.points }}</div>
                <div class="stat-label">积分</div>
              </div>
            </div>
          </div>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span>学习成就</span>
          </div>
          <div class="achievements">
            <div v-for="achievement in achievements" :key="achievement.id" class="achievement-item">
              <el-tooltip :content="achievement.description" placement="top">
                <el-avatar
                  :size="50"
                  :src="achievement.icon"
                  :class="{ 'unlocked': achievement.unlocked }"
                ></el-avatar>
              </el-tooltip>
              <span class="achievement-name">{{ achievement.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>基本信息</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="handleEdit"
            >编辑</el-button>
          </div>
          <el-form :model="userInfo" label-width="100px">
            <el-form-item label="用户名">
              <span>{{ userInfo.username }}</span>
            </el-form-item>
            <el-form-item label="昵称">
              <span>{{ userInfo.nickname }}</span>
            </el-form-item>
            <el-form-item label="性别">
              <span>{{ genderMap[userInfo.gender] }}</span>
            </el-form-item>
            <el-form-item label="邮箱">
              <span>{{ userInfo.email }}</span>
            </el-form-item>
            <el-form-item label="手机号码">
              <span>{{ userInfo.phone }}</span>
            </el-form-item>
            <el-form-item label="个人简介">
              <span>{{ userInfo.bio || '这个人很懒，什么都没写~' }}</span>
            </el-form-item>
            <el-form-item label="注册时间">
              <span>{{ formatTime(userInfo.createTime) }}</span>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span>学习偏好</span>
          </div>
          <div class="preferences">
            <h4>感兴趣的课程分类</h4>
            <div class="preference-tags">
              <el-tag
                v-for="tag in userInfo.interests"
                :key="tag"
                size="medium"
                style="margin-right: 10px; margin-bottom: 10px;"
              >{{ tag }}</el-tag>
            </div>

            <h4>学习时间分布</h4>
            <div ref="studyTimeChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑个人信息对话框 -->
    <el-dialog title="编辑个人信息" :visible.sync="dialogVisible" width="50%">
      <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="editForm.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item label="个人简介" prop="bio">
          <el-input
            type="textarea"
            v-model="editForm.bio"
            :rows="4"
            placeholder="请输入个人简介"
          ></el-input>
        </el-form-item>
        <el-form-item label="兴趣标签" prop="interests">
          <el-select
            v-model="editForm.interests"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入感兴趣的课程分类"
          >
            <el-option
              v-for="item in interestOptions"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProfile, updateUserProfile, uploadUserAvatar } from '@/api/user'
import { parseTime } from '@/utils'
import * as echarts from 'echarts'

export default {
  name: 'UserProfile',
  data() {
    const validateEmail = (rule, value, callback) => {
      if (value && !/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)) {
        callback(new Error('请输入正确的邮箱地址'))
      } else {
        callback()
      }
    }
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }
    return {
      userInfo: {
        username: '',
        nickname: '',
        avatar: '',
        gender: 0,
        email: '',
        phone: '',
        bio: '',
        roleName: '',
        createTime: '',
        learningDays: 0,
        completedCourses: 0,
        points: 0,
        interests: []
      },
      achievements: [],
      genderMap: {
        0: '保密',
        1: '男',
        2: '女'
      },
      dialogVisible: false,
      editForm: {
        nickname: '',
        gender: 0,
        email: '',
        phone: '',
        bio: '',
        interests: []
      },
      editRules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ],
        bio: [
          { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
        ]
      },
      interestOptions: [
        '计算机科学',
        '数学',
        '物理',
        '英语',
        '文学',
        '历史',
        '艺术'
      ],
      studyTimeChart: null
    }
  },
  mounted() {
    this.getProfile()
    this.initChart()
  },
  beforeDestroy() {
    if (this.studyTimeChart) {
      this.studyTimeChart.dispose()
    }
  },
  methods: {
    getProfile() {
      getUserProfile().then(response => {
        this.userInfo = response.data
        this.achievements = response.data.achievements
        this.updateChart(response.data.studyTimeDistribution)
      })
    },
    formatTime(time) {
      return parseTime(time)
    },
    handleEdit() {
      this.editForm = {
        nickname: this.userInfo.nickname,
        gender: this.userInfo.gender,
        email: this.userInfo.email,
        phone: this.userInfo.phone,
        bio: this.userInfo.bio,
        interests: [...this.userInfo.interests]
      }
      this.dialogVisible = true
    },
    submitEdit() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          updateUserProfile(this.editForm).then(() => {
            this.$message.success('个人信息更新成功')
            this.dialogVisible = false
            this.getProfile()
          })
        }
      })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('头像只能是 JPG 或 PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('头像大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    uploadAvatar(params) {
      const formData = new FormData()
      formData.append('avatar', params.file)
      uploadUserAvatar(formData).then(response => {
        this.userInfo.avatar = response.data.url
        this.$message.success('头像更新成功')
      })
    },
    initChart() {
      this.studyTimeChart = echarts.init(this.$refs.studyTimeChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
          type: 'value',
          name: '学习时长（小时）'
        },
        series: [{
          data: [],
          type: 'line',
          smooth: true,
          name: '学习时长',
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.studyTimeChart.setOption(option)
    },
    updateChart(data) {
      const option = {
        series: [{
          data: data.map(item => item.duration)
        }]
      }
      this.studyTimeChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info {
  text-align: center;
  padding: 20px;

  .avatar-container {
    position: relative;
    display: inline-block;
    margin-bottom: 20px;

    .avatar-mask {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #fff;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 50%;
      opacity: 0;
      transition: opacity 0.3s;
      cursor: pointer;

      i {
        font-size: 24px;
        margin-bottom: 5px;
      }

      span {
        font-size: 12px;
      }
    }

    &:hover .avatar-mask {
      opacity: 1;
    }
  }

  .user-name {
    margin: 10px 0;
    font-size: 20px;
    color: #303133;
  }

  .user-role {
    color: #909399;
    margin-bottom: 20px;
  }

  .user-stats {
    display: flex;
    justify-content: space-around;
    border-top: 1px solid #ebeef5;
    padding-top: 20px;

    .stat-item {
      text-align: center;

      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #409EFF;
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
        margin-top: 5px;
      }
    }
  }
}

.achievements {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  padding: 10px;

  .achievement-item {
    text-align: center;

    .el-avatar {
      filter: grayscale(100%);
      opacity: 0.5;
      transition: all 0.3s;

      &.unlocked {
        filter: none;
        opacity: 1;
      }
    }

    .achievement-name {
      display: block;
      font-size: 12px;
      color: #606266;
      margin-top: 5px;
    }
  }
}

.preferences {
  h4 {
    margin: 20px 0 15px;
    color: #303133;
  }

  .preference-tags {
    margin-bottom: 30px;
  }
}

::v-deep .avatar-uploader {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}
</style>
