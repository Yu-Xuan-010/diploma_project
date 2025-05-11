<template>
  <div class="lesson-container">
    <el-row :gutter="20">
      <!-- 左侧视频播放区域 -->
      <el-col :span="18">
        <div class="video-section">
          <div class="video-header">
            <h2>{{ currentLesson.title }}</h2>
            <div class="lesson-navigation">
            </div>
          </div>

          <div class="video-player">
            <video
                v-if="currentLesson.videoUrl"
                ref="videoRef"
                controls
                :src="currentLesson.videoUrl"
                style="width: 100%;"
            ></video>
            <div v-else class="no-video">
              <el-empty description="暂无视频内容" />
            </div>
          </div>



          <div class="lesson-info">
            <div class="info-header">
              <span class="duration">
                <el-icon><Timer /></el-icon>
                {{ formatDuration(currentLesson.duration) }}
              </span>
            </div>
            <div class="description">
              {{ currentLesson.description }}
            </div>
          </div>
        </div>
      </el-col>

      <!-- 右侧课时列表 -->
      <el-col :span="6">
        <el-card class="lesson-list-card">
          <template #header>
            <div class="card-header">
              <span>课时列表</span>
            </div>
          </template>

          <el-collapse v-model="activeChapters">
            <el-collapse-item
                v-for="chapter in chapters"
                :key="chapter.id"
                :title="chapter.title"
                :name="chapter.id"
            >
              <div class="lesson-list">
                <div
                    v-for="lesson in chapter.lessons"
                    :key="lesson.id"
                    class="lesson-item"
                    :class="{
                    'active': lesson.id === currentLesson.id,
                    'completed': isLessonCompleted(lesson.id)
                  }"
                    @click="navigateLesson(lesson)"
                >
                  <div class="lesson-info">
                    <el-icon><VideoPlay /></el-icon>
                    <span class="lesson-title">{{ lesson.title }}</span>
                    <span class="lesson-duration">{{ formatDuration(lesson.duration) }}</span>
                  </div>
                  <el-icon v-if="isLessonCompleted(lesson.id)" class="completed-icon"><Check /></el-icon>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>
import {ref, computed, watch, nextTick, onUnmounted, onMounted} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import DPlayer from 'dplayer'
import { ElMessage } from 'element-plus'
import { ArrowLeft, ArrowRight, Timer, VideoPlay, Check } from '@element-plus/icons-vue'
import axios from 'axios'


const videoRef = ref()
const route = useRoute()
const router = useRouter()
let lastSentTime = 0


const reportStudyProgress = async (currentTime) => {
    const courseId = Number(route.params.courseId)
    const lessonId = Number(route.params.lessonId)

    await axios.post('/api/study/records', {
      courseId,
      lessonId,
      totalDuration: currentTime,
      status: 0
    })
}



const handleTimeUpdate = () => {
  const currentTime = videoRef.value?.currentTime || 0
  if (currentTime - lastSentTime >= 1) {
    reportStudyProgress(currentTime)
    lastSentTime = currentTime
  }
}

watch(videoRef, (video) => {
  if (video) {
    video.addEventListener('timeupdate', handleTimeUpdate)
  }
})


// 基础数据
const currentLesson = ref({})
const videoPlayer = ref(null)
const chapters = ref([])
const comments = ref([])
const isCompleted = ref(false)
const activeChapters = ref([])
const completedLessons = ref(new Set())

// 评论相关数据
const commentDialogVisible = ref(false)
const commentFormRef = ref(null)
const commentForm = ref({ content: '' })
const commentPage = ref(1)
const commentPageSize = ref(10)
const totalComments = ref(0)





// 获取课程 ID 和课时 ID
const courseId = ref(route.params.courseId)
const lessonId = ref(route.params.lessonId)

// 计算属性
const totalLessons = computed(() => {
  return chapters.value.reduce((total, chapter) => total + chapter.lessons.length, 0)
})

const currentLessonIndex = computed(() => {
  for (let i = 0; i < chapters.value.length; i++) {
    const index = chapters.value[i].lessons.findIndex(lesson => lesson.id === currentLesson.value.id)
    if (index !== -1) {
      return { chapterIndex: i, lessonIndex: index }
    }
  }
  return null
})

const prevLesson = computed(() => {
  if (!currentLessonIndex.value) return null
  const { chapterIndex, lessonIndex } = currentLessonIndex.value
  
  if (lessonIndex > 0) {
    return chapters.value[chapterIndex].lessons[lessonIndex - 1]
  } else if (chapterIndex > 0) {
    const prevChapter = chapters.value[chapterIndex - 1]
    return prevChapter.lessons[prevChapter.lessons.length - 1]
  }
  return null
})

const nextLesson = computed(() => {
  if (!currentLessonIndex.value) return null
  const { chapterIndex, lessonIndex } = currentLessonIndex.value
  
  if (lessonIndex < chapters.value[chapterIndex].lessons.length - 1) {
    return chapters.value[chapterIndex].lessons[lessonIndex + 1]
  } else if (chapterIndex < chapters.value.length - 1) {
    return chapters.value[chapterIndex + 1].lessons[0]
  }
  return null
})

const initVideoPlayer = async () => {
  // 销毁旧播放器（如果存在）
  if (videoPlayer.value) {
    videoPlayer.value.destroy()
    videoPlayer.value = null
  }

  // 等待 DOM 渲染完成
  await nextTick()

  // 检查视频URL是否存在
  if (!currentLesson.value?.videoUrl) {
    console.warn('视频地址无效')
    return
  }

  // 使用MutationObserver监听DOM变化
  const initPlayer = () => {
    const container = document.querySelector('.dplayer-container')
    if (!container) {
      console.warn('容器未渲染')
      return false
    }

    try {
      videoPlayer.value = new DPlayer({
        container,
        video: {
          url: currentLesson.value.videoUrl,
          type: 'auto'
        },
        autoplay: false
      })

      // 错误处理
      videoPlayer.value.on('error', () => {
        ElMessage.error('视频加载失败，请稍后重试')
      })

      return true
    } catch (error) {
      console.error('播放器初始化失败:', error)
      return false
    }
  }

  // 尝试初始化播放器
  let retryCount = 0
  const maxRetries = 3

  const tryInitPlayer = () => {
    if (initPlayer()) {
      return
    }

    if (retryCount < maxRetries) {
      retryCount++
      setTimeout(tryInitPlayer, 200)
    } else {
      console.warn('播放器初始化失败，已达到最大重试次数')
    }
  }

  tryInitPlayer()
}

// 清理课时数据
const clearLessonData = () => {
  if (videoPlayer.value) {
    videoPlayer.value.destroy()
    videoPlayer.value = null
  }
  currentLesson.value = {}
  comments.value = []
  isCompleted.value = false
}

// 加载课时信息
const loadLesson = async () => {
  clearLessonData()
  try {
    // 获取课时信息
    const lessonRes = await axios.get(`/api/lessons/${lessonId.value}`)
    if (lessonRes.data.success) {
      currentLesson.value = lessonRes.data.data
      await checkLessonCompletion()
      await initVideoPlayer()
    } else {
      ElMessage.warning('暂无课时数据')
    }
  } catch (error) {
  }
}

// 获取章节列表
const fetchChapters = async () => {
  try {
    const response = await axios.get(`/api/lessons/course/${courseId.value}`)
    if (response.data.success) {
      chapters.value = response.data.data
      // 找到当前课时所在的章节并展开
      const chapter = chapters.value.find(chapter =>
        chapter.lessons.some(lesson => lesson.id === lessonId.value)
      )
      if (chapter) {
        activeChapters.value = [chapter.id]
      }
      // 获取已完成课时列表
      fetchCompletedLessons()
    }
  } catch (error) {
  }
}

// 获取已完成课时列表
const fetchCompletedLessons = async () => {
  try {
    const response = await axios.get(`/api/lessons/course/${courseId.value}/completed`)
    if (response.data.success) {
      completedLessons.value = new Set(response.data.data)
    }
  } catch (error) {
  }
}



// 检查课时完成状态
const checkLessonCompletion = async () => {
  try {
    const response = await axios.get(`/api/lessons/${lessonId.value}/completion`)
    if (response.data.success) {
      isCompleted.value = response.data.data.completed
      if (isCompleted.value) {
        completedLessons.value.add(lessonId.value)
      }
    }
  } catch (error) {
    console.error('检查课时完成状态失败:', error)
  }
}

// 切换课时完成状态
const toggleComplete = async () => {
  try {
    const method = isCompleted.value ? 'delete' : 'post'
    const response = await axios[method](`/api/lessons/${lessonId.value}/completion`)
    if (response.data.success) {
      isCompleted.value = !isCompleted.value
      if (isCompleted.value) {
        completedLessons.value.add(lessonId.value)
      } else {
        completedLessons.value.delete(lessonId.value)
      }
      ElMessage.success(isCompleted.value ? '已标记为完成' : '已取消完成标记')
    }
  } catch (error) {
    console.error('更新课时完成状态失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  }
}

// 导航到指定课时
const navigateLesson = (lesson) => {
  if (lesson.id === currentLesson.value.id) return
  router.push(`/course/${courseId.value}/lesson/${lesson.id}`)
}

function handlePlay(sectionId) {
  axios.post('/api/study-record/save', {
    courseId: props.courseId,
    sectionId: sectionId,
    duration: 0
  }).then(() => {
    console.log('学习记录已保存')
  }).catch(err => {
    console.error('保存失败', err)
  })
}



// 格式化时长
const formatDuration = (seconds) => {
  if (!seconds) return '00:00'
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const remainingSeconds = seconds % 60
  
  let result = ''
  if (hours > 0) {
    result += `${hours.toString().padStart(2, '0')}:`
  }
  result += `${minutes.toString().padStart(2, '0')}:`
  result += `${remainingSeconds.toString().padStart(2, '0')}`
  
  return result
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '暂无数据'
  try {
    const date = new Date(dateTime)
    if (isNaN(date.getTime())) {
      return '暂无数据'
    }
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化错误:', error)
    return '暂无数据'
  }
}

// 检查课时是否完成
const isLessonCompleted = (lessonId) => {
  return completedLessons.value.has(lessonId)
}


// 监听路由参数变化
watch(
  () => route.params.lessonId,
  async (newId) => {
    if (newId && newId !== lessonId.value) {
      lessonId.value = newId
      await loadLesson()
    }
  },
  { immediate: true }
)

// 监听课程ID变化
watch(
  () => route.params.courseId,
  (newId) => {
    if (newId && newId !== courseId.value) {
      courseId.value = newId
      fetchChapters()
    }
  },
  { immediate: true }
)

// 页面卸载时销毁播放器
onUnmounted(() => {
  if (videoPlayer.value) {
    videoPlayer.value.destroy()
  }
  if (videoRef.value) {
    videoRef.value.removeEventListener('timeupdate', handleTimeUpdate)
  }
})

onMounted(async () =>{
  // 初始化加载
  await fetchChapters()
  await loadLesson()
})
</script>




<style lang="scss" scoped>
.lesson-container {
  padding: 20px;
  max-width: 1400px;
  margin: 60px auto 0;

  .video-section {
    margin-bottom: 20px;

    .video-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h2 {
        margin: 0;
        font-size: 20px;
        color: #333;
      }

      .lesson-navigation {
        display: flex;
        gap: 10px;
      }
    }

    .video-player {
      width: 100%;
      background: #000;
      margin-bottom: 20px;
      border-radius: 8px;
      overflow: hidden;

      .dplayer-container {
        width: 100%;
        aspect-ratio: 16 / 9;
      }

      .no-video {
        width: 100%;
        aspect-ratio: 16 / 9;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f5f7fa;
      }
    }

    .lesson-info {
      .info-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .duration {
          color: #666;
          display: flex;
          align-items: center;
          gap: 5px;
        }
      }

      .description {
        color: #666;
        line-height: 1.6;
      }
    }
  }

  .comment-section {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .comment-list {
      .no-comments {
        text-align: center;
        color: #999;
        padding: 30px 0;
      }

      .comment-item {
        padding: 20px 0;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        .comment-header {
          display: flex;
          align-items: center;
          margin-bottom: 10px;

          .comment-info {
            margin-left: 10px;
            flex: 1;

            .username {
              font-weight: bold;
              color: #333;
            }

            .time {
              margin-left: 10px;
              color: #999;
              font-size: 14px;
            }
          }
        }

        .comment-content {
          color: #666;
          line-height: 1.6;
        }
      }
    }

    .comment-pagination {
      margin-top: 20px;
      display: flex;
      justify-content: center;
    }
  }

  .lesson-list-card {
    position: sticky;
    top: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .total-lessons {
        color: #666;
        font-size: 14px;
      }
    }

    .lesson-list {
      .lesson-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #ecf5ff;
          color: #409eff;
        }

        &.completed {
          color: #67c23a;
        }

        .lesson-info {
          display: flex;
          align-items: center;
          gap: 10px;
          flex: 1;

          .lesson-title {
            flex: 1;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .lesson-duration {
            color: #999;
            font-size: 14px;
          }
        }

        .completed-icon {
          color: #67c23a;
        }
      }
    }
  }
}
</style> 