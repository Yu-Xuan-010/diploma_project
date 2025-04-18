<template>
  <div class="video-player-container">
    <video
      ref="videoPlayer"
      class="video-js"
      :src="videoUrl"
      @play="handlePlay"
      @pause="handlePause"
      @ended="handleEnded"
      @timeupdate="handleTimeUpdate"
      controls
    ></video>
    <div class="study-progress" v-if="totalStudyTime > 0">
      <span>已学习时长：{{ formatDuration(totalStudyTime) }}</span>
      <el-tag :type="hasStudied ? 'success' : 'info'">
        {{ hasStudied ? '已完成学习' : '学习中' }}
      </el-tag>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { updateStudyProgress, checkStudyStatus, getTotalStudyTime } from '@/api/study'

export default {
  name: 'VideoPlayer',
  props: {
    videoUrl: {
      type: String,
      required: true
    },
    lessonId: {
      type: [String, Number],
      required: true
    }
  },
  setup(props) {
    const store = useStore()
    const videoPlayer = ref(null)
    const startTime = ref(null)
    const lastRecordedTime = ref(0)
    const studyInterval = ref(null)
    const hasStudied = ref(false)
    const totalStudyTime = ref(0)

    // 记录学习时间
    const recordStudyTime = async (duration) => {
      try {
        await updateStudyProgress(props.lessonId, duration)
        totalStudyTime.value += duration
        if (totalStudyTime.value >= 300) { // 5分钟 = 300秒
          hasStudied.value = true
        }
      } catch (error) {
        console.error('保存学习记录失败:', error)
        ElMessage.error('保存学习记录失败')
      }
    }

    // 开始播放时记录开始时间
    const handlePlay = () => {
      startTime.value = Date.now()
      // 每分钟记录一次学习时间
      studyInterval.value = setInterval(() => {
        const currentTime = Date.now()
        const duration = Math.floor((currentTime - startTime.value) / 1000) // 转换为秒
        if (duration > lastRecordedTime.value) {
          recordStudyTime(duration - lastRecordedTime.value)
          lastRecordedTime.value = duration
        }
      }, 60000) // 每分钟检查一次
    }

    // 暂停时记录学习时间
    const handlePause = () => {
      if (startTime.value) {
        const duration = Math.floor((Date.now() - startTime.value) / 1000)
        recordStudyTime(duration - lastRecordedTime.value)
        lastRecordedTime.value = duration
        clearInterval(studyInterval.value)
      }
    }

    // 视频结束时记录学习时间
    const handleEnded = () => {
      if (startTime.value) {
        const duration = Math.floor((Date.now() - startTime.value) / 1000)
        recordStudyTime(duration - lastRecordedTime.value)
        lastRecordedTime.value = duration
        clearInterval(studyInterval.value)
      }
    }

    // 定期更新播放进度
    const handleTimeUpdate = () => {
      // 可以在这里添加进度更新逻辑
    }

    // 格式化时长
    const formatDuration = (seconds) => {
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      if (hours > 0) {
        return `${hours}小时${minutes}分钟`
      }
      return `${minutes}分钟`
    }

    // 组件挂载时检查学习状态
    onMounted(async () => {
      try {
        const [statusResponse, timeResponse] = await Promise.all([
          checkStudyStatus(props.lessonId),
          getTotalStudyTime(props.lessonId)
        ])
        hasStudied.value = statusResponse.data
        totalStudyTime.value = timeResponse.data
      } catch (error) {
        console.error('获取学习状态失败:', error)
      }
    })

    // 组件卸载前清理
    onBeforeUnmount(() => {
      if (startTime.value) {
        const duration = Math.floor((Date.now() - startTime.value) / 1000)
        recordStudyTime(duration - lastRecordedTime.value)
      }
      if (studyInterval.value) {
        clearInterval(studyInterval.value)
      }
    })

    return {
      videoPlayer,
      hasStudied,
      totalStudyTime,
      handlePlay,
      handlePause,
      handleEnded,
      handleTimeUpdate,
      formatDuration
    }
  }
}
</script>

<style scoped>
.video-player-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.video-js {
  width: 100%;
  height: auto;
  aspect-ratio: 16/9;
}

.study-progress {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style> 