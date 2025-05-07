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
              <!-- 评分区域 -->
              <div class="rating-section" v-if="!hasRated">
                <h3>课程评分</h3>
                <div class="rating-form">
                  <el-rate 
                    v-model="ratingForm.rating" 
                    show-text 
                    :texts="['很差', '较差', '一般', '较好', '很好']"
                    @change="handleRatingChange">
                  </el-rate>
                  <div class="rating-tip" v-if="ratingForm.rating">
                    您给出的评分：{{ ratingForm.rating }}分
                  </div>
                </div>
              </div>
              <div class="rating-info" v-else>
                <h3>课程评分</h3>
                <div class="current-rating">
                  <el-rate 
                    v-model="userRating" 
                    disabled 
                    show-score>
                  </el-rate>
                  <span class="rating-text">您的评分：{{ userRating }}分</span>
                </div>
              </div>

              <!-- 评论区域 -->
              <div class="comment-section">
                <h3>课程评论</h3>
                <div class="comment-form">
                  <el-input
                    type="textarea"
                    :rows="3"
                    placeholder="请输入您的评论"
                    v-model="commentForm.content">
                  </el-input>
                  <div class="form-footer">
                    <el-button type="primary" @click="submitComment">发表评论</el-button>
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
                      </div>
                    </div>
                    <div class="comment-content">{{ comment.content }}</div>
                    <div class="comment-actions">
                      <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                      <div class="action-buttons">
                        <el-button 
                          type="text" 
                          size="small" 
                          @click="showReplyDialog(comment)"
                          v-if="isLoggedIn">
                          回复
                        </el-button>
                        <el-button 
                          type="text" 
                          size="small" 
                          @click="deleteComment(comment)"
                          v-if="isLoggedIn && currentUserId && comment.userId === currentUserId">
                          删除
                        </el-button>
                        <el-button 
                          type="text" 
                          size="small" 
                          @click="toggleReplies(comment)"
                          v-if="comment.replies && comment.replies.length > 0">
                          {{ comment.showReplies ? '收起回复' : `展开${comment.replies.length}条回复` }}
                        </el-button>
                      </div>
                    </div>
                    
                    <!-- 回复列表 -->
                    <div class="reply-list" v-if="comment.showReplies && comment.replies?.length > 0">
                      <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                        <el-avatar :src="reply.userAvatar" size="small" />
                        <div class="reply-content">{{ reply.content }}</div>
                        <el-button
                            v-if="isLoggedIn && reply.userId === currentUserId"
                            @click="deleteReply(reply)">
                          删除
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="no-comments" v-else>
                  <el-empty description="暂无评论"></el-empty>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
    <el-card class="study-records" v-if="studyRecords.length > 0">
      <template #header>
        <div class="card-header">
          <span>学习记录</span>
        </div>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="record in recentStudyRecords"
          :key="record.id"
          :timestamp="formatTime(record.studyDate)"
          :type="record.status === 1 ? 'success' : 'primary'"
        >
          <h4>{{ record.lessonTitle || '未知课程' }}</h4>
          <p>学习时长：{{ formatDuration(record.duration) }}</p>
          <p v-if="record.courseName">课程：{{ record.courseName }}</p>
          <p v-if="record.teacherName">教师：{{ record.teacherName }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 回复对话框 -->
    <el-dialog
      v-model="replyDialogVisible"
      title="回复评论"
      width="500px">
      <el-form :model="replyForm" :rules="replyRules" ref="replyFormRef">
        <el-form-item prop="content">
          <el-input
            type="textarea"
            v-model="replyForm.content"
            :rows="3"
            placeholder="请输入回复内容">
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="replyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReply">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Timer, Star, StarFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import VideoPlayer from '@/components/VideoPlayer.vue'
import { getStudyRecords, getRecentStudyRecords } from '@/api/study'

export default {
  name: 'CourseDetail',
  components: {
    Picture,
    Timer,
    Star,
    StarFilled,
    VideoPlayer
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
    const ratingForm = ref({
      rating: 0
    })
    const userRating = ref(0)
    const commentForm = ref({
      content: ''
    })
    const comments = ref([])
    const hasRated = ref(false)
    const isFavorited = ref(false)
    const courseId = ref(route.params.courseId)
    const studyRecords = ref([])
    const recentStudyRecords = ref([])
    const isLoggedIn = ref(false)
    const currentUserId = ref(null)
    const replyDialogVisible = ref(false)
    const replyForm = ref({
      content: '',
      commentId: null
    })
    const replyFormRef = ref(null)
    const replyRules = {
      content: [
        { required: true, message: '请输入回复内容', trigger: 'blur' },
        { min: 1, max: 500, message: '回复内容长度在1-500个字符之间', trigger: 'blur' }
      ]
    }

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

    const handleNodeClick = async (nodeData) => {
      if (nodeData.type === 'lesson') {
        try {
          // 提交学习记录
          await axios.post('/api/study/records', {
            lessonId: route.params.lessonId,
            totalDuration: 0
          })

          // 跳转到课时页面（需要拿到 courseId）
          await router.push(`/course/${course.value.id}/lesson/${nodeData.id}`)
        } catch (error) {
          console.error('跳转失败', error)
          ElMessage.error('无法进入课时学习页面')
        }
      }
    }


    const handleTabClick = (tab) => {
      console.log('切换到标签页:', tab.props.name)
      if (tab.props.name === 'comments') {
        getComments()
      }
    }

    const checkUserRating = async () => {
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) return
        
        const response = await axios.get(`/api/courses/${courseId}/rating/user/${userInfo.id}`, {
          headers: {
            'Authorization': `Bearer ${store.state.user.token}`
          }
        })
        if (response.data.code === 200 && response.data.data) {
          hasRated.value = true
          userRating.value = response.data.data.rating
        } else {
          hasRated.value = false
          userRating.value = 0
        }
      } catch (error) {
        console.error('获取用户评分失败:', error)
        hasRated.value = false
        userRating.value = 0
      }
    }

    const handleRatingChange = async (value) => {
      if (!value) return
      
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) {
          ElMessage.warning('请先登录后再评分')
          return
        }
        
        const response = await axios.post(`/api/courses/${courseId}/rating`, {
          rating: value,
          courseId: courseId,
          userId: userInfo.id,
          userName: userInfo.username,
          userAvatar: userInfo.avatar
        }, {
          headers: {
            'Authorization': `Bearer ${store.state.user.token}`
          }
        })
        
        if (response.data.code === 200) {
          ElMessage.success('评分成功')
          hasRated.value = true
          userRating.value = value
          ratingForm.value.rating = 0
          // 更新课程平均评分
          if (response.data.data && response.data.data.averageRating) {
            course.value.averageRating = response.data.data.averageRating
          }
        }
      } catch (error) {
        console.error('提交评分失败:', error)
        ElMessage.error('评分失败，请稍后重试')
        ratingForm.value.rating = 0
      }
    }

    const getComments = async () => {
      try {
        const courseId = route.params.courseId
        console.log('获取课程评论，课程ID:', courseId)
        const response = await axios.get(`/api/comments/course/${courseId}`)
        console.log('评论数据:', response.data)
        if (response.data.code === 200) {
          // 确保每个评论对象都包含必要的字段
          comments.value = response.data.data.map(comment => ({
            ...comment,
            replies: comment.replies || [],
            showReplies: false,  // 添加展开/收起状态
            userId: comment.userId,
            userName: comment.userName,
            userAvatar: comment.userAvatar,
            createTime: comment.createTime
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

    const submitComment = async () => {
      if (!commentForm.value.content) {
        ElMessage.warning('请输入评论内容')
        return
      }
      
      try {
        const courseId = route.params.courseId
        const userInfo = store.state.user.userInfo
        if (!userInfo) {
          ElMessage.warning('请先登录后再评论')
          return
        }
        
        const response = await axios.post(`/api/comments`, {
          courseId: courseId,
          userId: userInfo.id,
          content: commentForm.value.content,
          userName: userInfo.username,
          userAvatar: userInfo.avatar
        }, {
          headers: {
            'Authorization': `Bearer ${store.state.user.token}`
          }
        })
        
        if (response.data.code === 200) {
          ElMessage.success('评论发表成功')
          commentForm.value.content = ''
          await getComments()
        }
      } catch (error) {
        console.error('发表评论失败:', error)
        ElMessage.error('发表评论失败')
      }
    }

    const formatTime = (time) => {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString()
    }

    const formatDuration = (seconds) => {
      if (!seconds) return '0分钟'
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
          
          // 更新 Vuex store 中的收藏状态
          if (store.state.user) {
            store.dispatch('user/updateFavoriteCourses')
          }
        } else {
          ElMessage.error(response.data.message || (isFavorited.value ? '取消收藏失败' : '收藏失败'))
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        ElMessage.error('操作失败：' + (error.response?.data?.message || '未知错误'))
      }
    }

    const fetchCourseDetails = async () => {
      try {
        await getCourseDetail()
        await checkUserRating()
        await checkFavoriteStatus()
        if (activeTab.value === 'comments') {
          await getComments()
        }
      } catch (error) {
        console.error('获取课程详情失败:', error)
        ElMessage.error('获取课程详情失败')
      }
    }

    const fetchStudyRecords = async () => {
      try {
        const [recordsResponse, recentResponse] = await Promise.all([
          getStudyRecords(),
          getRecentStudyRecords(5)
        ])
        
        // 确保响应数据存在
        if (recordsResponse?.data?.code === 200) {
          studyRecords.value = recordsResponse.data.data || []
        } else {
          studyRecords.value = []
          console.warn('获取学习记录失败:', recordsResponse?.data?.message || '未知错误')
        }
        
        if (recentResponse?.data?.code === 200) {
          recentStudyRecords.value = recentResponse.data.data || []
        } else {
          recentStudyRecords.value = []
          console.warn('获取最近学习记录失败:', recentResponse?.data?.message || '未知错误')
        }
      } catch (error) {
        console.error('获取学习记录失败:', error)
        ElMessage.error('获取学习记录失败，请稍后重试')
        studyRecords.value = []
        recentStudyRecords.value = []
      }
    }

    const deleteComment = async (comment) => {
      try {
        await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' });
        const token = localStorage.getItem('token');
        const res = await axios.delete(`/api/comments/${comment.id}`, {
          headers: { 'Authorization': `Bearer ${token}` },
          data: { userId: currentUserId.value }
        });
        if (res.data.success) {
          comments.value = comments.value.filter(c => c.id !== comment.id);
        }
      } catch (error) {
        if (error !== 'cancel') ElMessage.error('删除失败');
      }
    };

    const showReplyDialog = (comment) => {
      replyForm.value = {
        content: '',
        commentId: comment.id
      }
      replyDialogVisible.value = true
    }

    const submitReply = async () => {
      if (!replyFormRef.value) return
      
      try {
        await replyFormRef.value.validate()
        const userInfo = JSON.parse(localStorage.getItem("user"))
        if (!userInfo) {
          ElMessage.warning('请先登录后再回复')
          return
        }
        
        const token = localStorage.getItem('token')
        if (!token) {
          ElMessage.error('未登录或登录已过期')
          return
        }
        
        const response = await axios.post(`/api/comments/${replyForm.value.commentId}/reply`, {
          userId: userInfo.id,
          content: replyForm.value.content,
          userName: userInfo.nickname || userInfo.username,
          userAvatar: userInfo.avatar || ''
        }, {
          headers: {
            'Authorization': token.startsWith('Bearer ') ? token : `Bearer ${token}`
          }
        })
        
        if (response.data.code === 200) {
          ElMessage.success('回复成功')
          replyDialogVisible.value = false
          replyForm.value.content = ''
          await getComments()
        } else {
          ElMessage.error(response.data.message || '回复失败')
        }
      } catch (error) {
        console.error('提交回复失败:', error)
        ElMessage.error('提交回复失败')
      }
    }

    const deleteReply = async (reply) => {
      try {
        await ElMessageBox.confirm('确定要删除这条回复吗？', '提示', {
          type: 'warning'
        })
        
        const token = localStorage.getItem('token')
        if (!token) {
          ElMessage.error('未登录或登录已过期')
          return
        }
        
        const response = await axios.delete(`/api/comments/reply/${reply.id}`, {
          headers: {
            'Authorization': token.startsWith('Bearer ') ? token : `Bearer ${token}`
          }
        })
        
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          await getComments()
        } else {
          ElMessage.error(response.data.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除回复失败:', error)
          ElMessage.error('删除回复失败')
        }
      }
    }

    const toggleReplies = (comment) => {
      comment.showReplies = !comment.showReplies
    }

    // 检查登录状态
    const checkLoginStatus = () => {
      try {
        // 尝试不同的可能的key
        const userStr = localStorage.getItem("user") || 
                       localStorage.getItem("userInfo") || 
                       localStorage.getItem("currentUser")
        console.log('localStorage中的user字符串:', userStr)
        
        if (userStr) {
          const user = JSON.parse(userStr)
          console.log('解析后的用户信息:', user)
          
          if (user && user.id) {
            isLoggedIn.value = true
            currentUserId.value = user.id
            console.log('设置登录状态为true, 用户ID:', user.id)
          } else {
            console.log('用户信息不完整，重置登录状态')
            isLoggedIn.value = false
            currentUserId.value = null
          }
        } else {
          // 如果localStorage中没有，尝试从store中获取
          const storeUser = store.state.user?.userInfo
          console.log('从store中获取的用户信息:', storeUser)
          
          if (storeUser && storeUser.id) {
            isLoggedIn.value = true
            currentUserId.value = storeUser.id
            console.log('从store设置登录状态为true, 用户ID:', storeUser.id)
          } else {
            console.log('未找到用户信息，重置登录状态')
            isLoggedIn.value = false
            currentUserId.value = null
          }
        }
      } catch (error) {
        console.error('检查登录状态时出错:', error)
        isLoggedIn.value = false
        currentUserId.value = null
      }
    }

    onMounted(() => {
      console.log('组件挂载，开始检查登录状态')
      checkLoginStatus()
      if (!courseId.value) {
        console.warn('courseId 为空，取消加载课程详情')
        return
      }
      fetchCourseDetails()
      fetchStudyRecords()
      checkUserRating()
    })

    // 监听标签页变化
    watch(activeTab, (newVal) => {
      console.log('标签页变化:', newVal)
      if (newVal === 'comments') {
        getComments()
      }
    })

    // 监听路由变化，重新检查收藏状态
    watch(() => route.params.courseId, (newId, oldId) => {
      if (newId && newId !== oldId) {
        checkFavoriteStatus()
        fetchCourseDetails() // 重新获取课程详情
      }
    })

    // 监听登录状态变化
    watch(() => store.state.user.isLoggedIn, (newValue) => {
      checkLoginStatus()
    })

    // 临时打印调试信息
    console.log('登录状态:', isLoggedIn.value)
    console.log('当前用户ID:', currentUserId.value)
    console.log("评论数据", comments.value)


    return {
      loading,
      course,
      activeTab,
      defaultProps,
      ratingForm,
      userRating,
      commentForm,
      comments,
      hasRated,
      isFavorited,
      handleNodeClick,
      handleTabClick,
      getComments,
      formatDuration,
      formatTime,
      getDefaultAvatar,
      getInitial,
      toggleFavorite,
      courseId,
      studyRecords,
      recentStudyRecords,
      isLoggedIn,
      currentUserId,
      replyDialogVisible,
      replyForm,
      replyFormRef,
      replyRules,
      deleteComment,
      showReplyDialog,
      submitReply,
      deleteReply,
      handleRatingChange,
      submitComment,
      toggleReplies,
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

        .rating-section {
          margin-bottom: 30px;
          padding: 20px;
          background: #f9f9f9;
          border-radius: 8px;

          h3 {
            margin-bottom: 15px;
            color: #303133;
          }

          .rating-form {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
          }

          .rating-tip {
            color: #409EFF;
            font-size: 14px;
          }
        }

        .rating-info {
          margin-bottom: 30px;
          padding: 20px;
          background: #f9f9f9;
          border-radius: 8px;

          h3 {
            margin-bottom: 15px;
            color: #303133;
          }

          .current-rating {
            display: flex;
            align-items: center;
            gap: 10px;

            .rating-text {
              color: #409EFF;
              font-size: 14px;
            }
          }
        }

        .comment-section {
          h3 {
            margin-bottom: 15px;
            color: #303133;
          }

          .comment-form {
            margin-bottom: 20px;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 8px;

            .form-footer {
              margin-top: 15px;
              text-align: right;
            }
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
              }
            }

            .comment-content {
              color: #606266;
              font-size: 14px;
              line-height: 1.6;
              margin: 8px 0;
            }

            .comment-actions {
              display: flex;
              justify-content: space-between;
              align-items: center;
              margin-top: 8px;
              
              .action-buttons {
                display: flex;
                gap: 10px;
              }
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

  .study-records {
    margin-top: 20px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .comment-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;
    
    .action-buttons {
      display: flex;
      gap: 10px;
    }
  }

  .reply-list {
    margin-left: 50px;
    margin-top: 10px;
    padding: 10px;
    background: #f9f9f9;
    border-radius: 4px;

    .reply-item {
      padding: 10px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .reply-user {
        display: flex;
        gap: 10px;

        .reply-info {
          flex: 1;

          .username {
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
            display: block;
          }

          .reply-content {
            color: #606266;
            font-size: 14px;
            line-height: 1.6;
            margin: 4px 0;
          }

          .reply-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 4px;

            .reply-time {
              color: #909399;
              font-size: 12px;
            }
          }
        }
      }
    }
  }
}
</style>

