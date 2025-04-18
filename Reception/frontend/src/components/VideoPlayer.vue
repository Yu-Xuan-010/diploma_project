<template>
  <div class="video-player-container">
    <div class="dplayer-container"></div>
    <div class="video-info" v-if="totalStudyTime > 0">
      <span>已学习时间: {{ formatDuration(totalStudyTime) }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  updateStudyProgress as updateStudyProgressApi, 
  checkStudyStatus as checkStudyStatusApi, 
  getTotalStudyTime as getTotalStudyTimeApi 
} from '@/api/study'

const props = defineProps({
  videoUrl: {
    type: String,
    required: true
  },
  lessonId: {
    type: [String, Number],
    required: true
  }
})

const player = ref(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const progressInterval = ref(null)
const hasStudied = ref(false)
const totalStudyTime = ref(0)

// 初始化播放器
const initPlayer = () => {
  if (player.value) {
    player.value.pause()
    player.value = null
  }
  
  player.value = new DPlayer({
    container: document.querySelector('.dplayer-container'),
    video: {
      url: props.videoUrl,
      type: 'auto'
    },
    autoplay: false,
    theme: '#F56C6C',
    loop: false,
    screenshot: true,
    hotkey: true,
    preload: 'auto',
    volume: 0.7,
    playbackSpeed: [0.5, 0.75, 1, 1.25, 1.5, 2],
    contextmenu: [
      {
        text: '课程学习平台',
        link: 'https://your-domain.com'
      }
    ]
  })

  setupEventListeners()
  checkStudyStatus()
}

// 设置事件监听器
const setupEventListeners = () => {
  if (!player.value) return

  player.value.on('play', () => {
    isPlaying.value = true
    startProgressTracking()
  })

  player.value.on('pause', () => {
    isPlaying.value = false
    stopProgressTracking()
    updateStudyProgress(props.lessonId, currentTime.value)
  })

  player.value.on('timeupdate', () => {
    currentTime.value = player.value.video.currentTime
    duration.value = player.value.video.duration
  })

  player.value.on('ended', () => {
    isPlaying.value = false
    stopProgressTracking()
    updateStudyProgress(props.lessonId, duration.value)
    ElMessage.success('视频播放完成！')
  })
}

// 开始进度追踪
const startProgressTracking = () => {
  if (progressInterval.value) return
  
  progressInterval.value = setInterval(() => {
    if (isPlaying.value && player.value) {
      updateStudyProgress(props.lessonId, currentTime.value)
    }
  }, 5000) // 每5秒更新一次进度
}

// 停止进度追踪
const stopProgressTracking = () => {
  if (progressInterval.value) {
    clearInterval(progressInterval.value)
    progressInterval.value = null
  }
}

// 更新学习进度
const updateStudyProgress = async (lessonId, currentTime) => {
  try {
    await updateStudyProgressApi({
      lessonId,
      progress: Math.floor((currentTime / duration.value) * 100)
    })
  } catch (error) {
    console.error('更新学习进度失败:', error)
  }
}

// 检查学习状态
const checkStudyStatus = async () => {
  try {
    const response = await checkStudyStatusApi(props.lessonId)
    hasStudied.value = response.data.hasStudied
    totalStudyTime.value = response.data.totalStudyTime || 0
  } catch (error) {
    console.error('获取学习状态失败:', error)
  }
}

// 获取总学习时间
const getTotalStudyTime = async () => {
  try {
    const response = await getTotalStudyTimeApi(props.lessonId)
    totalStudyTime.value = response.data.totalStudyTime || 0
  } catch (error) {
    console.error('获取总学习时间失败:', error)
  }
}

// 监听视频URL变化
watch(() => props.videoUrl, () => {
  initPlayer()
})

// 组件挂载时初始化
onMounted(() => {
  initPlayer()
})

// 组件销毁前清理
onBeforeUnmount(() => {
  stopProgressTracking()
  if (player.value) {
    player.value.destroy()
    player.value = null
  }
})
</script>

<style scoped>
.video-player-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.dplayer-container {
  width: 100%;
  aspect-ratio: 16 / 9;
  background: #000;
}

.video-info {
  margin-top: 10px;
  color: #666;
  font-size: 14px;
}
</style> 