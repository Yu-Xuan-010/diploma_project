<template>
  <div class="course-detail" v-loading="loading">
    <el-card class="course-info">
      <div class="course-header">
        <h1 class="course-title">{{ course.name }}</h1>
        <div class="course-meta">
          <span><i class="el-icon-user"></i> {{ course.teacherName }}</span>
          <span><i class="el-icon-view"></i> {{ course.viewCount }}</span>
          <span><i class="el-icon-date"></i> {{ course.createTime }}</span>
        </div>
      </div>
      <div class="course-content">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="课程介绍" name="intro">
            <div class="course-intro" v-html="course.description"></div>
          </el-tab-pane>
          <el-tab-pane label="课程目录" name="catalog">
            <div class="course-catalog">
              <el-tree
                :data="course.chapters"
                :props="defaultProps"
                @node-click="handleNodeClick">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                  <span>{{ node.label }}</span>
                  <span v-if="data.type === 'lesson'">
                    <el-tag size="mini" :type="data.status === 1 ? 'success' : ''">
                      {{ data.status === 1 ? '已完成' : '未学习' }}
                    </el-tag>
                  </span>
                </span>
              </el-tree>
            </div>
          </el-tab-pane>
          <el-tab-pane label="课程评价" name="comments">
            <div class="course-comments">
              <div class="comment-form">
                <el-rate v-model="commentForm.rating" show-text></el-rate>
                <el-input
                  type="textarea"
                  :rows="3"
                  placeholder="请输入您的评价"
                  v-model="commentForm.content">
                </el-input>
                <div class="form-footer">
                  <el-button type="primary" @click="submitComment">提交评价</el-button>
                </div>
              </div>
              <div class="comment-list">
                <div class="comment-item" v-for="comment in comments" :key="comment.id">
                  <div class="comment-user">
                    <span class="username">{{ comment.userName }}</span>
                    <el-rate v-model="comment.rating" disabled show-score></el-rate>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                  <div class="comment-time">{{ comment.createTime }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CourseDetail',
  data() {
    return {
      loading: false,
      course: {
        name: '',
        teacherName: '',
        viewCount: 0,
        createTime: '',
        description: '',
        chapters: []
      },
      activeTab: 'intro',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      commentForm: {
        rating: 0,
        content: ''
      },
      comments: []
    }
  },
  created() {
    this.getCourseDetail()
    this.getComments()
  },
  methods: {
    getCourseDetail() {
      this.loading = true
      const courseId = this.$route.params.id
      this.$store.dispatch('course/getDetail', courseId).then(response => {
        this.course = response
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getComments() {
      const courseId = this.$route.params.id
      this.$store.dispatch('course/getComments', courseId).then(response => {
        this.comments = response
      })
    },
    handleNodeClick(data) {
      if (data.type === 'lesson') {
        // 处理课时点击事件
        this.$router.push(`/learning/lesson/${data.id}`)
      }
    },
    submitComment() {
      if (this.commentForm.rating === 0) {
        this.$message.warning('请选择评分')
        return
      }
      if (!this.commentForm.content) {
        this.$message.warning('请输入评价内容')
        return
      }
      const courseId = this.$route.params.id
      const params = {
        courseId,
        ...this.commentForm
      }
      this.$store.dispatch('course/submitComment', params).then(() => {
        this.$message.success('评价提交成功')
        this.commentForm.rating = 0
        this.commentForm.content = ''
        this.getComments()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.course-detail {
  padding: 20px;

  .course-info {
    .course-header {
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 20px;
      margin-bottom: 20px;

      .course-title {
        margin: 0 0 15px;
        font-size: 24px;
        color: #303133;
      }

      .course-meta {
        color: #909399;
        font-size: 14px;

        span {
          margin-right: 20px;

          i {
            margin-right: 5px;
          }
        }
      }
    }

    .course-content {
      .course-intro {
        line-height: 1.8;
        color: #606266;
      }

      .course-catalog {
        .custom-tree-node {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: space-between;
          font-size: 14px;
          padding-right: 8px;
        }
      }

      .course-comments {
        .comment-form {
          margin-bottom: 30px;

          .el-rate {
            margin-bottom: 15px;
          }

          .form-footer {
            margin-top: 15px;
            text-align: right;
          }
        }

        .comment-list {
          .comment-item {
            padding: 15px 0;
            border-bottom: 1px solid #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .comment-user {
              display: flex;
              align-items: center;
              margin-bottom: 10px;

              .username {
                margin-right: 15px;
                font-weight: bold;
              }
            }

            .comment-content {
              color: #606266;
              line-height: 1.6;
            }

            .comment-time {
              margin-top: 10px;
              color: #909399;
              font-size: 13px;
            }
          }
        }
      }
    }
  }
}
</style>
