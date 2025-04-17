<template>
  <div class="course-detail" v-loading="loading">
    <el-card class="course-info">
      <div class="course-header">
        <div class="course-cover">
          <el-image 
            :src="course.image" 
            fit="cover"
            :preview-src-list="[course.image]">
            <template #error>
              <div class="image-placeholder">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>
        <div class="course-info-content">
          <h1 class="course-title">{{ course.name }}</h1>
          <div class="course-meta">
            <span><a>作者：</a><i class="el-icon-user"></i> {{ course.teacher?.name }}</span>
            <span><a>学习人数：</a><i class="el-icon-user"></i> {{ course.studentCount }}</span>
            <span><a>评分：</a><i class="el-icon-star-on"></i> {{ course.averageRating }}</span>
            <el-button 
              :type="isFavorited ? 'danger' : 'primary'" 
              :icon="isFavorited ? 'Star' : 'StarFilled'"
              @click="toggleFavorite"
              class="favorite-btn">
              {{ isFavorited ? '取消收藏' : '收藏课程' }}
            </el-button>
          </div>
        </div>
      </div>
      <div class="course-content">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="课程介绍" name="intro">
            <div class="course-intro" v-html="course.description"></div>
          </el-tab-pane>
          <el-tab-pane label="课程目录" name="catalog">
            <div class="course-catalog">
              <el-tree
                :data="course.lessons"
                :props="defaultProps"
                @node-click="handleNodeClick">
                <template #default="{ node, data }">
                  <span class="custom-tree-node">
                    <span class="lesson-title">
                      <span class="lesson-number">第{{ data.sortOrder }}课时：</span>
                      {{ node.label }}
                    </span>
                    <span v-if="data.type === 'lesson'" class="lesson-status">
                      <el-tag size="small" :type="data.status === 1 ? 'success' : 'info'">
                        {{ data.status === 1 ? '已完成' : '未学习' }}
                      </el-tag>
                      <span class="lesson-duration" v-if="data.duration">
                        <el-icon><Timer /></el-icon>
                        {{ formatDuration(data.duration) }}
                      </span>
                    </span>
                  </span>
                </template>
              </el-tree>
            </div>
          </el-tab-pane>
          <el-tab-pane label="课程评论" name="comments">
            <div class="course-comments">
              <div class="comment-form" v-if="!hasCommented">
                <el-rate v-model="commentForm.rating" show-text></el-rate>
                <el-input
                  type="textarea"
                  :rows="3"
                  placeholder="请输入您的评论"
                  v-model="commentForm.content">
                </el-input>
                <div class="form-footer">
                  <el-button type="primary" @click="submitComment">提交评论</el-button>
                </div>
              </div>
              <div class="comment-list" v-if="comments && comments.length > 0">
                <div class="comment-item" v-for="comment in comments" :key="comment.id">
                  <div class="comment-user">
                    <el-avatar 
                      :size="40" 
                      :src="comment.userAvatar"
                      :fallback="getDefaultAvatar(comment.userName)">
                      {{ getInitial(comment.userName) }}
                    </el-avatar>
                    <div class="user-info">
                      <span class="username">{{ comment.userName }}</span>
                      <div class="rating-wrapper">
                        <el-rate 
                          v-model="comment.rating"
                          disabled
                          :max="5"
                          :colors="['#99A9BF', '#F7BA2A', '#FF9900']">
                        </el-rate>
                        <span class="rating-score">{{ comment.rating }}分</span>
                      </div>
                    </div>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                  <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
                </div>
              </div>
              <div class="no-comments" v-else>
                <el-empty description="暂无评论"></el-empty>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { Picture, Timer, Star, StarFilled } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'CourseDetail',
  components: {
    Picture,
    Timer,
    Star,
    StarFilled
  },
  setup() {
    const router = useRouter()
    const route = useRoute()
    const store = useStore()
    const loading = ref(false)
    const course = ref({
      id: null,
      name: '',
      description: '',
      image: '',
      teacher: {},
      lessons: [],
      averageRating: 0,
      studentCount: 0
    })
    const activeTab = ref('intro')
    const defaultProps = {
      children: 'children',
      label: 'title'
    }
    const commentForm = ref({
      rating: 0,
      content: ''
    })
    const comments = ref([])
    const hasCommented = ref(false)
    const isFavorited = ref(false)

    const getDefaultAvatar = (name) => {
      return `https://api.dicebear.com/7.x/initials/svg?seed=${encodeURIComponent(name || '')}`
    }

    const getInitial = (name) => {
      return name?.charAt(0)?.toUpperCase() || '?'
    }

    const getCourseDetail = async () => {
      try {
        loading.value = true
        const courseId = route.params.courseId
        console.log('获取课程详情，课程ID:', courseId)
        
        // 获取课程基本信息
        const courseResponse = await axios.get(`/api/courses/${courseId}`)
        if (courseResponse.data.success) {
          const courseData = courseResponse.data.data
          course.value = {
            id: courseData.id,
            name: courseData.name,
            description: courseData.description,
            image: courseData.image,
            teacher: {
              id: courseData.teacherId,
              name: courseData.teacherName
            },
            averageRating: courseData.averageRating,
            studentCount: courseData.studentCount,
            viewCount: courseData.viewCount,
            createdAt: courseData.createdAt,
            updatedAt: courseData.updatedAt,
            status: courseData.status,
            categoryId: courseData.categoryId,
            categoryName: courseData.categoryName
          }
          console.log('课程基本信息:', course.value)
        }
        
        // 获取课程课时列表
        const lessonsResponse = await axios.get(`/api/lessons/course/${courseId}`)
        if (lessonsResponse.data.success) {
          // 将课时列表转换为树形结构
          course.value.lessons = lessonsResponse.data.data.map(lesson => ({
            id: lesson.id,
            title: lesson.title,
            type: 'lesson',
            status: lesson.status || 0,
            videoUrl: lesson.videoUrl,
            duration: lesson.duration,
            sortOrder: lesson.sortOrder
          })).sort((a, b) => a.sortOrder - b.sortOrder)
          
          console.log('课程课时列表:', course.value.lessons)
        }
      } catch (error) {
        console.error('获取课程详情失败:', error)
        ElMessage.error('获取课程详情失败')
      } finally {
        loading.value = false
      }
    }

    const handleNodeClick = (data) => {
      if (data.type === 'lesson') {
        router.push(`/course/${course.value.id}/lesson/${data.id}`)
      }
    }

    const handleTabClick = (tab) => {
      console.log('切换到标签页:', tab.props.name)
      if (tab.props.name === 'comments') {
        getComments()
      }
    }

    const submitComment = async () => {
      if (commentForm.value.rating === 0) {
        ElMessage.warning('请选择评分')
        return
      }
      if (!commentForm.value.content) {
        ElMessage.warning('请输入评论内容')
        return
      }
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        const params = {
          courseId,
          userId: userInfo.id,
          content: commentForm.value.content,
          rating: commentForm.value.rating
        }
        console.log('提交评论参数:', params)
        const response = await axios.post(`/api/courses/${courseId}/comments`, params)
        if (response.data.code === 200) {
          ElMessage.success('评论提交成功')
          commentForm.value.rating = 0
          commentForm.value.content = ''
          await getComments()
          hasCommented.value = true
        } else {
          ElMessage.error(response.data.message || '评论提交失败')
        }
      } catch (error) {
        console.error('提交评论失败:', error)
        ElMessage.error('提交评论失败')
      }
    }

    const formatTime = (time) => {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString()
    }

    const checkUserComment = async () => {
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) return
        
        const response = await axios.get(`/api/courses/${courseId}/comments/check/${userInfo.id}`)
        hasCommented.value = response.data.code === 200 && response.data.data
      } catch (error) {
        console.error('检查用户评论状态失败:', error)
      }
    }

    const getComments = async () => {
      try {
        const courseId = route.params.courseId
        console.log('获取课程评论，课程ID:', courseId)
        const response = await axios.get(`/api/courses/${courseId}/comments`)
        console.log('评论数据:', response.data)
        if (response.data.code === 200) {
          // 确保数据是数组并且每个评论对象都有必要的字段
          comments.value = (response.data.data || []).map(comment => ({
            id: comment.id,
            courseId: comment.courseId,
            userId: comment.userId,
            content: comment.content,
            rating: comment.rating,
            createTime: comment.createTime,
            updateTime: comment.updateTime,
            userName: comment.userName || '未知用户',
            userAvatar: comment.userAvatar || ''
          }))
          console.log('处理后的评论列表:', comments.value)
        } else {
          ElMessage.error(response.data.message || '获取评论失败')
        }
      } catch (error) {
        console.error('获取课程评论失败:', error)
        ElMessage.error('获取课程评论失败')
      }
    }

    const formatDuration = (seconds) => {
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      if (hours > 0) {
        return `${hours}小时${minutes}分钟`
      }
      return `${minutes}分钟`
    }

    const checkFavoriteStatus = async () => {
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) return
        
        const response = await axios.get(`/api/courses/${courseId}/favorite/check`)
        isFavorited.value = response.data.code === 200 && response.data.data
        console.log('收藏状态:', isFavorited.value)
      } catch (error) {
        console.error('检查收藏状态失败:', error)
      }
    }

    const toggleFavorite = async () => {
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) {
          ElMessage.warning('请先登录后再收藏课程')
          return
        }
        
        const url = isFavorited.value 
          ? `/api/courses/${courseId}/favorite/cancel`
          : `/api/courses/${courseId}/favorite`
        
        const method = isFavorited.value ? 'delete' : 'post'
        
        const response = await axios[method](url)
        
        if (response.data.code === 200) {
          isFavorited.value = !isFavorited.value
          ElMessage.success(isFavorited.value ? '收藏成功' : '已取消收藏')
        } else {
          ElMessage.error(response.data.message || (isFavorited.value ? '取消收藏失败' : '收藏失败'))
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        ElMessage.error('操作失败：' + (error.response?.data?.message || '未知错误'))
      }
    }

    onMounted(() => {
      getCourseDetail()
      checkUserComment()
      checkFavoriteStatus()
      // 如果当前是评论标签页，也加载评论
      if (activeTab.value === 'comments') {
        getComments()
      }
    })

    // 监听标签页变化
    watch(activeTab, (newVal) => {
      console.log('标签页变化:', newVal)
      if (newVal === 'comments') {
        getComments()
      }
    })

    return {
      loading,
      course,
      activeTab,
      defaultProps,
      commentForm,
      comments,
      hasCommented,
      isFavorited,
      handleNodeClick,
      handleTabClick,
      submitComment,
      getComments,
      formatDuration,
      formatTime,
      getDefaultAvatar,
      getInitial,
      toggleFavorite
    }
  }
}
</script>

<style lang="scss" scoped>
.course-detail {
  padding: 30px;
  background-color: #f5f7fa;

  .course-info {
    background: #fff;
    border-radius: 8px;
    padding: 25px;
    margin-top: 60px;

    .course-header {
      display: flex;
      gap: 30px;
      border-bottom: 1px solid #ebeef5;
      padding-bottom: 20px;
      margin-bottom: 25px;

      .course-cover {
        width: 300px;
        height: 169px;
        border-radius: 8px;
        overflow: hidden;
        flex-shrink: 0;

        .el-image {
          width: 100%;
          height: 100%;
        }

        .image-placeholder {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #f5f7fa;
          color: #909399;
          font-size: 30px;
        }
      }

      .course-info-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .course-title {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 20px;
        }

        .course-meta {
          color: #909399;
          font-size: 14px;
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          gap: 15px;

          span {
            display: inline-flex;
            align-items: center;

            i {
              margin-right: 6px;
            }
          }
          
          .favorite-btn {
            margin-left: auto;
          }
        }
      }
    }

    .course-content {
      padding-top: 10px;

      .el-tabs__nav {
        font-size: 16px;
      }

      .course-intro {
        padding: 15px 5px;
        line-height: 1.8;
        color: #606266;
        font-size: 15px;
      }

      .course-catalog {
        padding: 15px 5px;

        .custom-tree-node {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 14px;
          padding-right: 10px;
          width: 100%;

          .lesson-title {
            display: flex;
            align-items: center;
            gap: 8px;

            .lesson-number {
              color: #409EFF;
              font-weight: 500;
            }
          }

          .lesson-status {
            display: flex;
            align-items: center;
            gap: 12px;

            .lesson-duration {
              color: #909399;
              font-size: 12px;
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }
        }
      }

      .course-comments {
        padding: 15px 5px;

        .comment-form {
          background: #f9f9f9;
          padding: 20px;
          border-radius: 8px;
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
            padding: 20px 0;
            border-bottom: 1px solid #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .comment-user {
              display: flex;
              align-items: flex-start;
              margin-bottom: 12px;

              .user-info {
                margin-left: 12px;
                flex: 1;

                .username {
                  font-weight: 600;
                  color: #303133;
                  margin-bottom: 4px;
                  display: block;
                }

                .rating-wrapper {
                  display: flex;
                  align-items: center;
                  gap: 8px;

                  .rating-score {
                    color: #F7BA2A;
                    font-size: 14px;
                    font-weight: 500;
                  }
                }
              }
            }

            .comment-content {
              color: #606266;
              font-size: 14px;
              line-height: 1.6;
              margin: 8px 0;
            }

            .comment-time {
              color: #909399;
              font-size: 12px;
            }
          }
        }

        .no-comments {
          padding: 40px 0;
          text-align: center;
        }
      }
    }
  }
}
</style>

