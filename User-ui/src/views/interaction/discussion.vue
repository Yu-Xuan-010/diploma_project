<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <div class="header-left">
              <span>课程讨论</span>
              <el-select
                v-model="queryParams.courseId"
                placeholder="选择课程"
                clearable
                style="margin-left: 20px; width: 200px;"
                @change="handleCourseChange"
              >
                <el-option
                  v-for="course in courseOptions"
                  :key="course.id"
                  :label="course.name"
                  :value="course.id"
                >
                </el-option>
              </el-select>
            </div>
            <el-button type="primary" @click="handleAddDiscussion">发起讨论</el-button>
          </div>

          <div class="discussion-list">
            <div v-for="discussion in discussionList" :key="discussion.id" class="discussion-item">
              <div class="discussion-header">
                <div class="user-info">
                  <el-avatar :size="40" :src="discussion.userAvatar"></el-avatar>
                  <div class="info-detail">
                    <span class="username">{{ discussion.userName }}</span>
                    <span class="time">{{ formatTime(discussion.createTime) }}</span>
                  </div>
                </div>
                <el-tag size="small">{{ discussion.courseName }}</el-tag>
              </div>

              <div class="discussion-content">
                <h3 class="title" @click="viewDiscussion(discussion)">{{ discussion.title }}</h3>
                <p class="content">{{ discussion.content }}</p>
                <div class="content-images" v-if="discussion.images && discussion.images.length">
                  <el-image
                    v-for="(image, index) in discussion.images"
                    :key="index"
                    :src="image"
                    :preview-src-list="discussion.images"
                    fit="cover"
                    class="content-image"
                  ></el-image>
                </div>
              </div>

              <div class="discussion-footer">
                <div class="stats">
                  <span>
                    <i class="el-icon-view"></i>
                    {{ discussion.views }} 浏览
                  </span>
                  <span>
                    <i class="el-icon-chat-dot-round"></i>
                    {{ discussion.replies }} 回复
                  </span>
                  <span>
                    <i class="el-icon-star-off"></i>
                    {{ discussion.likes }} 赞
                  </span>
                </div>
                <div class="actions">
                  <el-button type="text" @click="handleReply(discussion)">
                    <i class="el-icon-chat-dot-round"></i> 回复
                  </el-button>
                  <el-button
                    type="text"
                    :class="{ 'liked': discussion.isLiked }"
                    @click="handleLike(discussion)"
                  >
                    <i :class="discussion.isLiked ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                    {{ discussion.isLiked ? '已赞' : '点赞' }}
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <div class="pagination-container">
            <el-pagination
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryParams.pageNum"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="queryParams.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header">
            <span>热门讨论</span>
          </div>
          <div class="hot-discussions">
            <div v-for="item in hotDiscussions" :key="item.id" class="hot-item" @click="viewDiscussion(item)">
              <h4>{{ item.title }}</h4>
              <div class="hot-stats">
                <span>{{ item.replies }} 回复</span>
                <span>{{ item.views }} 浏览</span>
              </div>
            </div>
          </div>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header">
            <span>我的参与</span>
          </div>
          <div class="my-participations">
            <el-tabs v-model="participationTab">
              <el-tab-pane label="我的发起" name="created">
                <div v-for="item in myCreated" :key="item.id" class="participation-item" @click="viewDiscussion(item)">
                  <p class="title">{{ item.title }}</p>
                  <span class="time">{{ formatTime(item.createTime) }}</span>
                </div>
              </el-tab-pane>
              <el-tab-pane label="我的回复" name="replied">
                <div v-for="item in myReplied" :key="item.id" class="participation-item" @click="viewDiscussion(item)">
                  <p class="title">{{ item.title }}</p>
                  <span class="time">{{ formatTime(item.replyTime) }}</span>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 发起讨论对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form :model="discussionForm" :rules="discussionRules" ref="discussionForm" label-width="80px">
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="discussionForm.courseId" placeholder="请选择课程">
            <el-option
              v-for="course in courseOptions"
              :key="course.id"
              :label="course.name"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="discussionForm.title" placeholder="请输入讨论标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            v-model="discussionForm.content"
            :rows="6"
            placeholder="请输入讨论内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="图片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDiscussion">发 布</el-button>
      </div>
    </el-dialog>

    <!-- 回复对话框 -->
    <el-dialog title="回复讨论" :visible.sync="replyDialogVisible" width="50%">
      <el-form :model="replyForm" :rules="replyRules" ref="replyForm">
        <el-form-item prop="content">
          <el-input
            type="textarea"
            v-model="replyForm.content"
            :rows="4"
            placeholder="请输入回复内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="replyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitReply">回 复</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDiscussionList,
  getHotDiscussions,
  getMyParticipations,
  createDiscussion,
  createReply,
  likeDiscussion
} from '@/api/interaction'
import { getCourseList } from '@/api/course'
import { parseTime } from '@/utils'

export default {
  name: 'CourseDiscussion',
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: undefined
      },
      total: 0,
      discussionList: [],
      hotDiscussions: [],
      courseOptions: [],
      participationTab: 'created',
      myCreated: [],
      myReplied: [],

      // 发起讨论
      dialogVisible: false,
      dialogTitle: '发起讨论',
      discussionForm: {
        courseId: undefined,
        title: '',
        content: '',
        images: []
      },
      discussionRules: {
        courseId: [
          { required: true, message: '请选择课程', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { min: 10, max: 2000, message: '长度在 10 到 2000 个字符', trigger: 'blur' }
        ]
      },
      fileList: [],

      // 回复讨论
      replyDialogVisible: false,
      replyForm: {
        discussionId: undefined,
        content: ''
      },
      replyRules: {
        content: [
          { required: true, message: '请输入回复内容', trigger: 'blur' },
          { min: 2, max: 500, message: '长度在 2 到 500 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getCourses()
    this.getHotList()
    this.getMyParticipationList()
  },
  methods: {
    getList() {
      getDiscussionList(this.queryParams).then(response => {
        this.discussionList = response.rows
        this.total = response.total
      })
    },
    getCourses() {
      getCourseList().then(response => {
        this.courseOptions = response.data
      })
    },
    getHotList() {
      getHotDiscussions().then(response => {
        this.hotDiscussions = response.data
      })
    },
    getMyParticipationList() {
      getMyParticipations().then(response => {
        this.myCreated = response.data.created
        this.myReplied = response.data.replied
      })
    },
    handleCourseChange() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    formatTime(time) {
      return parseTime(time)
    },
    handleAddDiscussion() {
      this.dialogTitle = '发起讨论'
      this.discussionForm = {
        courseId: this.queryParams.courseId,
        title: '',
        content: '',
        images: []
      }
      this.fileList = []
      this.dialogVisible = true
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
    submitDiscussion() {
      this.$refs.discussionForm.validate(valid => {
        if (valid) {
          // 处理图片上传
          const formData = new FormData()
          this.fileList.forEach(file => {
            formData.append('images', file.raw)
          })
          formData.append('courseId', this.discussionForm.courseId)
          formData.append('title', this.discussionForm.title)
          formData.append('content', this.discussionForm.content)

          createDiscussion(formData).then(() => {
            this.$message.success('发布成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleReply(discussion) {
      this.replyForm = {
        discussionId: discussion.id,
        content: ''
      }
      this.replyDialogVisible = true
    },
    submitReply() {
      this.$refs.replyForm.validate(valid => {
        if (valid) {
          createReply(this.replyForm).then(() => {
            this.$message.success('回复成功')
            this.replyDialogVisible = false
            this.getList()
          })
        }
      })
    },
    handleLike(discussion) {
      likeDiscussion(discussion.id).then(() => {
        discussion.isLiked = !discussion.isLiked
        discussion.likes += discussion.isLiked ? 1 : -1
      })
    },
    viewDiscussion(discussion) {
      this.$router.push(`/interaction/discussion/detail/${discussion.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.header-left {
  display: flex;
  align-items: center;
}

.discussion-list {
  .discussion-item {
    padding: 20px;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }

    .discussion-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      .user-info {
        display: flex;
        align-items: center;
        gap: 10px;

        .info-detail {
          display: flex;
          flex-direction: column;

          .username {
            font-weight: bold;
            color: #303133;
          }

          .time {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }

    .discussion-content {
      .title {
        margin: 0 0 10px;
        font-size: 16px;
        color: #303133;
        cursor: pointer;

        &:hover {
          color: #409EFF;
        }
      }

      .content {
        color: #606266;
        margin-bottom: 10px;
        line-height: 1.6;
      }

      .content-images {
        display: flex;
        gap: 10px;
        margin-top: 10px;

        .content-image {
          width: 100px;
          height: 100px;
          border-radius: 4px;
        }
      }
    }

    .discussion-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 15px;

      .stats {
        color: #909399;
        font-size: 13px;

        span {
          margin-right: 20px;

          i {
            margin-right: 4px;
          }
        }
      }

      .actions {
        .el-button {
          padding: 0;
          margin-left: 20px;

          &.liked {
            color: #F56C6C;
          }
        }
      }
    }
  }
}

.hot-discussions {
  .hot-item {
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
    cursor: pointer;

    &:last-child {
      border-bottom: none;
    }

    &:hover h4 {
      color: #409EFF;
    }

    h4 {
      margin: 0 0 8px;
      font-size: 14px;
      color: #303133;
    }

    .hot-stats {
      font-size: 12px;
      color: #909399;

      span {
        margin-right: 15px;
      }
    }
  }
}

.my-participations {
  .participation-item {
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
    cursor: pointer;

    &:last-child {
      border-bottom: none;
    }

    &:hover .title {
      color: #409EFF;
    }

    .title {
      margin: 0 0 5px;
      font-size: 14px;
      color: #303133;
    }

    .time {
      font-size: 12px;
      color: #909399;
    }
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
