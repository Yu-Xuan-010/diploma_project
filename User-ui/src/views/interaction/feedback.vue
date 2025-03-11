<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>提交反馈</span>
          </div>
          
          <el-form :model="feedbackForm" :rules="feedbackRules" ref="feedbackForm" label-width="100px">
            <el-form-item label="反馈类型" prop="type">
              <el-select v-model="feedbackForm.type" placeholder="请选择反馈类型">
                <el-option label="功能建议" value="feature"></el-option>
                <el-option label="问题反馈" value="bug"></el-option>
                <el-option label="内容建议" value="content"></el-option>
                <el-option label="使用咨询" value="usage"></el-option>
                <el-option label="其他" value="other"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="反馈标题" prop="title">
              <el-input v-model="feedbackForm.title" placeholder="请输入反馈标题"></el-input>
            </el-form-item>

            <el-form-item label="反馈内容" prop="content">
              <el-input
                type="textarea"
                v-model="feedbackForm.content"
                :rows="6"
                placeholder="请详细描述您的反馈内容"
              ></el-input>
            </el-form-item>

            <el-form-item label="相关课程" prop="courseId">
              <el-select 
                v-model="feedbackForm.courseId" 
                placeholder="请选择相关课程（选填）"
                clearable
                filterable
              >
                <el-option
                  v-for="course in courseOptions"
                  :key="course.id"
                  :label="course.name"
                  :value="course.id"
                ></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="附件">
              <el-upload
                action="#"
                list-type="picture-card"
                :auto-upload="false"
                :file-list="fileList"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
              >
                <i class="el-icon-plus"></i>
                <div slot="tip" class="el-upload__tip">可上传截图或相关文件（选填）</div>
              </el-upload>
            </el-form-item>

            <el-form-item label="联系方式" prop="contact">
              <el-input 
                v-model="feedbackForm.contact" 
                placeholder="请留下您的联系方式，方便我们及时回复（选填）"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>我的反馈记录</span>
          </div>
          
          <div class="feedback-list">
            <div v-for="item in feedbackList" :key="item.id" class="feedback-item">
              <div class="feedback-header">
                <el-tag :type="getFeedbackTypeTag(item.type)" size="small">
                  {{ getFeedbackTypeText(item.type) }}
                </el-tag>
                <span class="time">{{ formatTime(item.createTime) }}</span>
              </div>
              <h4 class="title">{{ item.title }}</h4>
              <p class="content">{{ item.content }}</p>
              <div class="feedback-footer">
                <el-tag 
                  :type="getStatusTag(item.status)" 
                  size="small"
                >{{ getStatusText(item.status) }}</el-tag>
                <el-button 
                  v-if="item.status === 'replied'" 
                  type="text"
                  @click="viewReply(item)"
                >查看回复</el-button>
              </div>
            </div>
          </div>

          <div class="empty-block" v-if="!feedbackList.length">
            <el-empty description="暂无反馈记录"></el-empty>
          </div>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix">
            <span>反馈须知</span>
          </div>
          <div class="notice-list">
            <p><i class="el-icon-info"></i> 请详细描述您遇到的问题或建议</p>
            <p><i class="el-icon-info"></i> 可以附上截图以便我们更好地理解</p>
            <p><i class="el-icon-info"></i> 我们会在2个工作日内回复您的反馈</p>
            <p><i class="el-icon-info"></i> 如有紧急问题，请联系在线客服</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查看回复对话框 -->
    <el-dialog title="反馈回复" :visible.sync="replyDialogVisible" width="50%">
      <div class="reply-content">
        <div class="original-feedback">
          <h4>原始反馈</h4>
          <p>{{ selectedFeedback.title }}</p>
          <p class="feedback-detail">{{ selectedFeedback.content }}</p>
        </div>
        <el-divider></el-divider>
        <div class="reply-info">
          <h4>管理员回复</h4>
          <p class="reply-time">回复时间：{{ formatTime(selectedFeedback.replyTime) }}</p>
          <p class="reply-detail">{{ selectedFeedback.reply }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createFeedback, getMyFeedbacks } from '@/api/interaction'
import { getCourseList } from '@/api/course'
import { parseTime } from '@/utils'

export default {
  name: 'SubmitFeedback',
  data() {
    return {
      feedbackForm: {
        type: '',
        title: '',
        content: '',
        courseId: undefined,
        contact: '',
        attachments: []
      },
      feedbackRules: {
        type: [
          { required: true, message: '请选择反馈类型', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入反馈标题', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入反馈内容', trigger: 'blur' },
          { min: 10, max: 2000, message: '长度在 10 到 2000 个字符', trigger: 'blur' }
        ]
      },
      fileList: [],
      courseOptions: [],
      feedbackList: [],
      replyDialogVisible: false,
      selectedFeedback: {}
    }
  },
  created() {
    this.getCourses()
    this.getFeedbackList()
  },
  methods: {
    getCourses() {
      getCourseList().then(response => {
        this.courseOptions = response.data
      })
    },
    getFeedbackList() {
      getMyFeedbacks().then(response => {
        this.feedbackList = response.data
      })
    },
    handleFileChange(file) {
      this.fileList.push(file)
    },
    handleFileRemove(file) {
      const index = this.fileList.indexOf(file)
      if (index !== -1) {
        this.fileList.splice(index, 1)
      }
    },
    submitFeedback() {
      this.$refs.feedbackForm.validate(valid => {
        if (valid) {
          const formData = new FormData()
          this.fileList.forEach(file => {
            formData.append('attachments', file.raw)
          })
          Object.keys(this.feedbackForm).forEach(key => {
            if (this.feedbackForm[key] !== undefined) {
              formData.append(key, this.feedbackForm[key])
            }
          })

          createFeedback(formData).then(() => {
            this.$message.success('反馈提交成功')
            this.resetForm()
            this.getFeedbackList()
          })
        }
      })
    },
    resetForm() {
      this.$refs.feedbackForm.resetFields()
      this.fileList = []
    },
    formatTime(time) {
      return parseTime(time)
    },
    getFeedbackTypeTag(type) {
      const tags = {
        feature: 'success',
        bug: 'danger',
        content: 'warning',
        usage: 'info',
        other: ''
      }
      return tags[type] || ''
    },
    getFeedbackTypeText(type) {
      const texts = {
        feature: '功能建议',
        bug: '问题反馈',
        content: '内容建议',
        usage: '使用咨询',
        other: '其他'
      }
      return texts[type] || '其他'
    },
    getStatusTag(status) {
      const tags = {
        pending: 'info',
        processing: 'warning',
        replied: 'success',
        closed: ''
      }
      return tags[status] || ''
    },
    getStatusText(status) {
      const texts = {
        pending: '待处理',
        processing: '处理中',
        replied: '已回复',
        closed: '已关闭'
      }
      return texts[status] || '未知'
    },
    viewReply(feedback) {
      this.selectedFeedback = feedback
      this.replyDialogVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.feedback-list {
  .feedback-item {
    padding: 15px 0;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }

    .feedback-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;

      .time {
        font-size: 12px;
        color: #909399;
      }
    }

    .title {
      margin: 0 0 8px;
      font-size: 14px;
      color: #303133;
    }

    .content {
      color: #606266;
      font-size: 13px;
      margin-bottom: 10px;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }

    .feedback-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}

.notice-list {
  p {
    margin: 10px 0;
    color: #606266;
    font-size: 13px;
    line-height: 1.6;

    i {
      color: #409EFF;
      margin-right: 5px;
    }
  }
}

.reply-content {
  .original-feedback {
    h4 {
      margin: 0 0 10px;
      color: #303133;
    }

    .feedback-detail {
      color: #606266;
      margin: 10px 0;
      padding: 10px;
      background-color: #f5f7fa;
      border-radius: 4px;
    }
  }

  .reply-info {
    h4 {
      margin: 0 0 10px;
      color: #303133;
    }

    .reply-time {
      font-size: 12px;
      color: #909399;
      margin-bottom: 10px;
    }

    .reply-detail {
      color: #606266;
      line-height: 1.6;
    }
  }
}

.el-upload__tip {
  line-height: 1.2;
}
</style> 