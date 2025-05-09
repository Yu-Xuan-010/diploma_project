<template>
  <div class="course-recommend">

    <h2 class="recommend-title">猜你喜欢</h2>
    <!-- 学习记录推荐 -->
    <div v-if="recommendedByStudy.length === 0" class="no-courses">
      <p>没有推荐课程（基于学习记录）</p>
    </div>
    <el-row :gutter="20" v-else>
      <el-col :span="8" v-for="course in recommendedByStudy" :key="course.id">
        <el-card :body-style="{ padding: '20px' }" class="course-card">
          <img :src="course.image" class="course-image" />
          <div class="course-info">
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <el-button type="primary" @click="goToCourseDetail(course.id)">查看课程</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="divider"></div>

    <!-- 高评分推荐 -->
    <h3 class="recommend-title">高评分课程</h3>
    <div v-if="recommendedHighRated.length === 0" class="no-courses">
      <p>没有高评分课程推荐</p>
    </div>
    <el-row :gutter="20" v-else>
      <el-col :span="8" v-for="course in recommendedHighRated" :key="course.id">
        <el-card :body-style="{ padding: '20px' }" class="course-card">
          <img :src="course.image" class="course-image" />
          <div class="course-info">
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <el-button type="primary" @click="goToCourseDetail(course.id)">查看课程</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="divider"></div>

    <!-- 热门课程推荐 -->
    <h3 class="recommend-title">热门课程</h3>
    <div v-if="recommendedHot.length === 0" class="no-courses">
      <p>没有热门课程推荐</p>
    </div>
    <el-row :gutter="20" v-else>
      <el-col :span="8" v-for="course in recommendedHot" :key="course.id">
        <el-card :body-style="{ padding: '20px' }" class="course-card">
          <img :src="course.image" class="course-image" />
          <div class="course-info">
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <el-button type="primary" @click="goToCourseDetail(course.id)">查看课程</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import store from "../store";

export default {
  name: 'CourseRecommend',
  setup() {
    const recommendedByStudy = ref([]) // 存储基于学习记录的推荐课程
    const recommendedHighRated = ref([]) // 存储高评分课程
    const recommendedHot = ref([]) // 存储热门课程

    const router = useRouter()
    const userInfo = store.state.user.userInfo

    // 请求推荐课程
    const fetchRecommendedCourses = async () => {
      try {
        // 请求学习记录推荐
        const responseStudy = await axios.get('/api/recommend/by-study', {params: {userId: userInfo.id}})
        recommendedByStudy.value = responseStudy.data

        // 请求高评分课程
        const responseHighRated = await axios.get('/api/recommend/high-rated')
        recommendedHighRated.value = responseHighRated.data

        // 请求热门课程
        const responseHot = await axios.get('/api/recommend/hot')
        recommendedHot.value = responseHot.data

      } catch (error) {
        ElMessage.error('加载推荐课程失败')
      }
    }

    // 跳转到课程详情页面
    const goToCourseDetail = (courseId) => {
      router.push({path: `/course/${courseId}`})
    }

    onMounted(() => {
      fetchRecommendedCourses()
    })

    return {
      recommendedByStudy,
      recommendedHighRated,
      recommendedHot,
      goToCourseDetail
    }
  }
}
</script>

<style scoped>
.course-recommend {
  padding: 60px 20px 20px; /* 调整上方 padding，避免 header 遮住 */
}

.recommend-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.divider {
  margin: 40px 0;
  border-top: 1px solid #f0f0f0;
}

.no-courses {
  text-align: center;
  font-size: 16px;
  color: #999;
}

.course-card {
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.course-card:hover {
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.course-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.course-info {
  margin-top: 10px;
}

.el-button {
  width: 100%;
}
</style>
