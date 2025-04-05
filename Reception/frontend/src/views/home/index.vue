<template>
  <div class="home-container">
    <!-- 轮播图部分 -->
    <div class="carousel-section">
      <el-carousel height="400px" :interval="4000">
        <el-carousel-item v-for="course in recommendedCourses" :key="course.id">
          <div class="carousel-item" @click="viewCourse(course.id)">
            <img :src="course.image" class="carousel-image">
            <div class="carousel-overlay">
              <h3>{{ course.title }}</h3>
              <p>{{ course.description }}</p>
              <div class="course-stats">
                <span><el-icon><View /></el-icon> {{ course.viewCount }}次观看</span>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <el-menu 
        mode="horizontal" 
        :default-active="selectedCategory || '0'"
        @select="changeCategory"
      >
        <el-menu-item index="0">全部课程</el-menu-item>
        <el-menu-item 
          v-for="category in categories" 
          :key="category.id" 
          :index="category.id.toString()"
        >
          {{ category.name }}
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 课程列表 -->
    <div class="course-section">
      <div class="course-list">
        <el-row :gutter="20">
          <el-col 
            :xs="24" 
            :sm="12" 
            :md="8" 
            v-for="course in allCourses" 
            :key="course.id"
          >
            <el-card class="course-card" shadow="hover" @click="viewCourse(course.id)">
              <img :src="course.image" class="course-image">
              <div class="course-info">
                <h3>{{ course.title }}</h3>
                <p>{{ course.description }}</p>
                <div class="course-footer">
                  <span class="view-count">
                    <el-icon><View /></el-icon> {{ course.viewCount }}
                  </span>
                  <el-button type="primary" size="small">查看详情</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 加载更多 -->
      <div class="load-more" v-if="!finished">
        <el-button 
          :loading="loading" 
          type="primary" 
          @click="fetchCourses"
        >
          {{ loading ? '加载中...' : '加载更多' }}
        </el-button>
      </div>
      <div v-else class="no-more">没有更多课程了</div>
    </div>
  </div>
</template>

<script>
import { ElRow, ElCol, ElCard, ElButton, ElCarousel, ElCarouselItem, ElMenu, ElMenuItem } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'Home',
  components: {
    ElRow,
    ElCol,
    ElCard,
    ElButton,
    ElCarousel,
    ElCarouselItem,
    ElMenu,
    ElMenuItem,
    View
  },
  data() {
    return {
      categories: [], // 课程分类
      selectedCategory: null, // 当前选中的分类
      recommendedCourses: [], // 推荐课程（轮播图）
      allCourses: [], // 所有课程
      page: 1, // 分页
      pageSize: 9,
      loading: false, // 是否正在加载
      finished: false // 是否加载完所有数据
    }
  },
  mounted() {
    this.fetchCategories()
    this.fetchRecommendedCourses()
    this.fetchCourses()
    window.addEventListener('scroll', this.handleScroll)
  },
  unmounted() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    // 获取分类
    async fetchCategories() {
      try {
        const res = await axios.get('/api/category/list')
        this.categories = res.data || []
      } catch (error) {
        console.error('获取分类失败:', error)
      }
    },

    // 获取推荐课程
    async fetchRecommendedCourses() {
      try {
        const res = await axios.get('/api/course/recommended')
        this.recommendedCourses = res.data || []
      } catch (error) {
        console.error('获取推荐课程失败:', error)
      }
    },

    // 获取所有课程（分页）
    async fetchCourses() {
      if (this.loading || this.finished) return
      this.loading = true

      try {
        const res = await axios.get('/api/course/list', {
          params: {
            category: this.selectedCategory,
            page: this.page,
            pageSize: this.pageSize
          }
        })

        const { courses, total } = res.data
        this.allCourses = [...this.allCourses, ...courses]
        
        if (this.allCourses.length >= total) {
          this.finished = true
        }
        this.page++
      } catch (error) {
        console.error('获取课程列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 处理滚动加载
    handleScroll() {
      const scrollHeight = document.documentElement.scrollHeight
      const scrollTop = document.documentElement.scrollTop
      const clientHeight = document.documentElement.clientHeight
      
      if (scrollHeight - scrollTop - clientHeight <= 100) {
        this.fetchCourses()
      }
    },

    // 切换分类
    changeCategory(categoryId) {
      this.selectedCategory = categoryId === '0' ? null : categoryId
      this.allCourses = []
      this.page = 1
      this.finished = false
      this.fetchCourses()
    },

    // 查看课程详情
    viewCourse(courseId) {
      this.$router.push(`/course/${courseId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 60px auto 0;

  .carousel-section {
    margin-bottom: 30px;

    .carousel-item {
      height: 100%;
      cursor: pointer;
      position: relative;
      overflow: hidden;

      &:hover .carousel-overlay {
        transform: translateY(0);
      }

      .carousel-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .carousel-overlay {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        background: linear-gradient(to top, rgba(0,0,0,0.8), transparent);
        padding: 20px;
        color: white;
        transform: translateY(100px);
        transition: transform 0.3s ease;

        h3 {
          font-size: 24px;
          margin-bottom: 10px;
        }

        p {
          font-size: 16px;
          opacity: 0.9;
          margin-bottom: 10px;
        }

        .course-stats {
          font-size: 14px;
          opacity: 0.8;

          .el-icon {
            margin-right: 5px;
          }
        }
      }
    }
  }

  .category-nav {
    margin-bottom: 30px;

    .el-menu {
      border-bottom: none;
    }
  }

  .course-section {
    .course-list {
      margin-bottom: 30px;
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
  }

  .load-more {
    text-align: center;
    margin: 20px 0;
  }

  .no-more {
    text-align: center;
    color: #999;
    padding: 20px 0;
  }
}
</style>
