<template>
    <div class="profile-container">
      <el-row :gutter="20">
        <!-- 左侧个人信息卡片 -->
        <el-col :span="8">
          <el-card class="profile-card">
            <div class="avatar-container">
              <el-avatar :size="100" :src="userInfo.avatar"></el-avatar>
              <h3 class="username">{{ userInfo.username }}</h3>
              <p class="email">{{ userInfo.email }}</p>
            </div>
            <div class="profile-stats">
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.courseCount }}</div>
                <div class="stat-label">在学课程</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.noteCount }}</div>
                <div class="stat-label">学习笔记</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userInfo.points }}</div>
                <div class="stat-label">学习积分</div>
              </div>
            </div>
            <el-button type="primary" plain @click="showEditProfile">编辑资料</el-button>
          </el-card>
        </el-col>
  
        <!-- 右侧内容区 -->
        <el-col :span="16">
          <el-tabs v-model="activeTab">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <el-form :model="userInfo" label-width="100px">
                <el-form-item label="用户名">
                  <span>{{ userInfo.username }}</span>
                </el-form-item>
                <el-form-item label="邮箱">
                  <span>{{ userInfo.email }}</span>
                </el-form-item>
                <el-form-item label="手机号码">
                  <span>{{ userInfo.phone || '未设置' }}</span>
                </el-form-item>
                <el-form-item label="注册时间">
                  <span>{{ userInfo.registerTime }}</span>
                </el-form-item>
                <el-form-item label="最后登录">
                  <span>{{ userInfo.lastLoginTime }}</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
  
            <!-- 学习记录 -->
            <el-tab-pane label="学习记录" name="learning">
              <el-table :data="learningRecords" style="width: 100%">
                <el-table-column prop="courseName" label="课程名称"></el-table-column>
                <el-table-column prop="progress" label="学习进度">
                  <template slot-scope="scope">
                    <el-progress :percentage="scope.row.progress"></el-progress>
                  </template>
                </el-table-column>
                <el-table-column prop="lastLearnTime" label="最后学习时间"></el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="text" @click="continueLearning(scope.row)">继续学习</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
  
            <!-- 我的笔记 -->
            <el-tab-pane label="我的笔记" name="notes">
              <div v-for="note in notes" :key="note.id" class="note-item">
                <h4>{{ note.courseName }}</h4>
                <p>{{ note.content }}</p>
                <div class="note-meta">
                  <span>{{ note.createTime }}</span>
                  <div>
                    <el-button type="text" @click="editNote(note)">编辑</el-button>
                    <el-button type="text" @click="deleteNote(note)">删除</el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>
  
            <!-- 账号安全 -->
            <el-tab-pane label="账号安全" name="security">
              <el-form label-width="100px">
                <el-form-item label="登录密码">
                  <el-button type="text" @click="showChangePassword">修改密码</el-button>
                </el-form-item>
                <el-form-item label="手机绑定">
                  <el-button type="text" @click="showBindPhone">{{ userInfo.phone ? '修改手机号' : '绑定手机号' }}</el-button>
                </el-form-item>
                <el-form-item label="邮箱绑定">
                  <el-button type="text" @click="showBindEmail">{{ userInfo.email ? '修改邮箱' : '绑定邮箱' }}</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>
  
      <!-- 编辑资料对话框 -->
      <el-dialog title="编辑资料" :visible.sync="editProfileVisible" width="500px">
        <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="100px">
          <el-form-item label="头像">
            <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
              <img v-if="editForm.avatar" :src="editForm.avatar" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="editForm.username"></el-input>
          </el-form-item>
          <el-form-item label="个性签名" prop="bio">
            <el-input type="textarea" v-model="editForm.bio"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editProfileVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitEditProfile">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Profile',
    data() {
      return {
        activeTab: 'basic',
        editProfileVisible: false,
        userInfo: {
          username: '示例用户',
          email: 'example@example.com',
          avatar: 'https://via.placeholder.com/100',
          phone: '13800138000',
          registerTime: '2024-01-01',
          lastLoginTime: '2024-03-14',
          courseCount: 5,
          noteCount: 12,
          points: 280
        },
        editForm: {
          username: '',
          avatar: '',
          bio: ''
        },
        editRules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ]
        },
        learningRecords: [
          {
            courseName: '示例课程1',
            progress: 45,
            lastLearnTime: '2024-03-14 10:00:00'
          },
          {
            courseName: '示例课程2',
            progress: 80,
            lastLearnTime: '2024-03-13 15:30:00'
          }
        ],
        notes: [
          {
            id: 1,
            courseName: '示例课程1',
            content: '这是一条学习笔记的示例内容...',
            createTime: '2024-03-14 09:00:00'
          }
        ]
      }
    },
    methods: {
      showEditProfile() {
        this.editForm.username = this.userInfo.username
        this.editForm.avatar = this.userInfo.avatar
        this.editForm.bio = this.userInfo.bio
        this.editProfileVisible = true
      },
      handleAvatarSuccess(res) {
        this.editForm.avatar = res.url
      },
      submitEditProfile() {
        this.$refs.editForm.validate(valid => {
          if (valid) {
            // 这里调用更新用户信息API
            this.editProfileVisible = false
            this.$message.success('资料更新成功')
          }
        })
      },
      showChangePassword() {
        // 实现修改密码逻辑
      },
      showBindPhone() {
        // 实现绑定手机号逻辑
      },
      showBindEmail() {
        // 实现绑定邮箱逻辑
      },
      continueLearning(course) {
        // 实现继续学习逻辑
      },
      editNote(note) {
        // 实现编辑笔记逻辑
      },
      deleteNote(note) {
        // 实现删除笔记逻辑
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .profile-container {
    padding: 20px;
  
    .profile-card {
      text-align: center;
  
      .avatar-container {
        margin-bottom: 20px;
  
        .username {
          margin: 10px 0 5px;
          font-size: 18px;
        }
  
        .email {
          color: #999;
          font-size: 14px;
          margin: 0;
        }
      }
  
      .profile-stats {
        display: flex;
        justify-content: space-around;
        margin: 20px 0;
  
        .stat-item {
          .stat-value {
            font-size: 20px;
            font-weight: bold;
            color: #409EFF;
          }
  
          .stat-label {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }
  
    .note-item {
      border-bottom: 1px solid #eee;
      padding: 15px 0;
  
      h4 {
        margin: 0 0 10px;
      }
  
      p {
        color: #666;
        margin: 0 0 10px;
      }
  
      .note-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: #999;
      }
    }
  }
  
  .avatar-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
  
      &:hover {
        border-color: #409EFF;
      }
    }
  }
  
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
  }
  
  .avatar {
    width: 100px;
    height: 100px;
    display: block;
  }
  </style>