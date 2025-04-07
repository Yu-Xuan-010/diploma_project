<template>
  <div class="search-page">
    <div class="search-header">
      <h2>搜索结果：<span class="keyword">"{{ $route.query.q }}"</span></h2>
      <p v-if="filteredCourses.length > 0">找到 {{ filteredCourses.length }} 个相关课程</p>
      <p v-else>未找到相关课程</p>
    </div>

    <el-row :gutter="20">
      <el-col 
        :xs="24" 
        :sm="12" 
        :md="8" 
        v-for="course in filteredCourses" 
        :key="course.id"
      >
        <el-card class="course-card" shadow="hover" @click="viewCourse(course.id)">
          <img :src="course.image || '/default-course.jpg'" class="course-image">
          <div class="course-info">
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <div class="course-footer">
              <span class="view-count">
                <el-icon><View /></el-icon> {{ course.viewCount || 0 }}
              </span>
              <el-button type="primary" size="small">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="no-results" v-if="!loading && filteredCourses.length === 0">
      <el-empty description="未找到相关课程">
        <el-button type="primary" @click="$router.push('/home')">返回首页</el-button>
      </el-empty>
    </div>

    <div class="loading" v-if="loading">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>
  </div>
</template>

<script>
import { ElRow, ElCol, ElCard, ElButton, ElEmpty, ElSkeleton } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'SearchPage',
  components: {
    ElRow,
    ElCol,
    ElCard,
    ElButton,
    ElEmpty,
    ElSkeleton,
    View
  },
  data() {
    return {
      courses: [],
      loading: false
    }
  },
  computed: {
    filteredCourses() {
      const query = this.$route.query.q?.toLowerCase() || '';
      return this.courses.filter(course => 
        course.name?.toLowerCase().includes(query) || 
        course.description?.toLowerCase().includes(query)
      );
    }
  },
  watch: {
    '$route.query.q': {
      handler() {
        this.fetchCourses();
      },
      immediate: true
    }
  },
  methods: {
    async fetchCourses() {
      this.loading = true;
      try {
        const res = await axios.get('/api/course/search', {
          params: { keyword: this.$route.query.q }
        });
        this.courses = res.data || [];
      } catch (error) {
        console.error('搜索课程失败:', error);
        this.$message.error('搜索失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    viewCourse(id) {
      this.$router.push(`/course/${id}`);
    }
  }
}
</script>

<style lang="scss" scoped>
.search-page {
  padding: 20px;
  max-width: 1200px;
  margin: 60px auto 0;

  .search-header {
    margin-bottom: 30px;
    
    h2 {
      font-size: 24px;
      color: #333;
      margin-bottom: 10px;

      .keyword {
        color: #409EFF;
      }
    }

    p {
      color: #666;
      font-size: 14px;
    }
  }

  .course-card {
    margin-bottom: 20px;
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 16px rgba(0,0,0,0.1);
    }

    .course-image {
      width: 100%;
      height: 200px;
      object-fit: cover;
    }

    .course-info {
      padding: 15px;

      h3 {
        font-size: 18px;
        margin-bottom: 10px;
        color: #333;
      }

      p {
        color: #666;
        margin-bottom: 15px;
        min-height: 40px;
        line-height: 1.5;
      }

      .course-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .view-count {
          color: #999;
          font-size: 14px;
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }

  .no-results {
    margin-top: 40px;
  }

  .loading {
    padding: 20px;
  }
}
</style> 