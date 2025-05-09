<template>
  <div class="home-container">
    <!-- 轮播图部分 -->
    <div class="carousel-section">
      <el-carousel height="600px" :interval="4000">
        <el-carousel-item v-for="course in recommendedCourses" :key="course.id">
          <div class="carousel-item" 
            @click="viewCourse(course.id)"
            @mouseenter="hoveredCourse = course.id"
            @mouseleave="hoveredCourse = null"
          >
            <img :src="course.image || '/default-course.jpg'" class="carousel-image">

            <div class="carousel-overlay">
              <h3 class="course-name">{{ course.name }}</h3>
              <transition name="fade-in-up">
                <div class="course-detail" v-if="hoveredCourse === course.id">
                  <p>{{ course.description }}</p>
                  <div class="course-stats">
                    <span><el-icon><View /></el-icon> {{ course.viewCount || 0 }}次观看</span>
                  </div>
                </div>
              </transition>
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
        <!-- 固定展示：全部课程 -->
        <el-menu-item index="0">全部课程</el-menu-item>

        <!-- 展示前 5 个分类 -->
        <el-menu-item
          v-for="category in categories.slice(0, 5)"
          :key="category.id"
          :index="category.id.toString()"
        >
          {{ category.name }}
        </el-menu-item>

        <!-- 更多分类折叠到子菜单中 -->
        <el-sub-menu v-if="categories.length > 5" index="more">
          <template #title>更多</template>
          <el-menu-item
            v-for="category in categories.slice(5)"
            :key="category.id"
            :index="category.id.toString()"
          >
            {{ category.name }}
          </el-menu-item>
        </el-sub-menu>
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
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[9, 18, 27, 36]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ElRow, ElCol, ElCard, ElButton, ElCarousel, ElCarouselItem, ElMenu, ElMenuItem } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

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
      page: 1, // 当前页码
      pageSize: 9, // 每页显示数量
      total: 0, // 总课程数
      loading: false, // 是否正在加载
      hoveredCourse: null, // 当前悬停的课程ID
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
        const res = await axios.get('/api/categories')
        this.categories = res.data?.data || []
      } catch (error) {
        console.error('获取分类失败:', error)
        ElMessage.error('获取分类失败')
      }
    },

    // 获取推荐课程
    async fetchRecommendedCourses() {
      try {
        console.log('开始获取推荐课程')
        const res = await axios.get('/api/courses/recommended')
        console.log('推荐课程响应:', res.data)
        
        if (res.data.success) {
          this.recommendedCourses = res.data.data || []
          console.log('解析后的推荐课程:', this.recommendedCourses)
        } else {
          console.error('获取推荐课程失败:', res.data.message)
          ElMessage.error(res.data.message || '获取推荐课程失败')
        }
      } catch (error) {
        console.error('获取推荐课程失败:', error)
        ElMessage.error('获取推荐课程失败，请稍后重试')
      }
    },

    // 获取所有课程（分页）
    async fetchCourses() {
      if (this.loading) return
      this.loading = true

      try {
        console.log('开始获取课程列表，参数:', {
          categoryId: this.selectedCategory === '0' ? null : this.selectedCategory,
          page: this.page,
          pageSize: this.pageSize,
          status: 'approved'
        })

        const res = await axios.get('/api/courses', {
          params: {
            categoryId: this.selectedCategory === '0' ? null : this.selectedCategory,
            page: this.page,
            pageSize: this.pageSize,
            status: 'approved'
          }
        })

        console.log('课程列表响应:', res.data)
        
        if (res.data.success) {
          const { list = [], total = 0 } = res.data.data || {}
          console.log('解析后的课程列表:', list)
          console.log('课程总数:', total)
          
          this.allCourses = list
          this.total = total
        } else {
          console.error('获取课程列表失败:', res.data.message)
          ElMessage.error(res.data.message || '获取课程列表失败')
        }
      } catch (error) {
        console.error('获取课程列表失败:', error)
        ElMessage.error('获取课程列表失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 处理每页显示数量变化
    handleSizeChange(val) {
      this.pageSize = val
      this.page = 1
      this.fetchCourses()
    },

    // 处理页码变化
    handleCurrentChange(val) {
      this.page = val
      this.fetchCourses()
    },

    // 切换分类
    changeCategory(categoryId) {
      this.selectedCategory = categoryId === '0' ? null : categoryId
      this.page = 1
      this.fetchCourses()
    },

    // 查看课程详情
    async viewCourse(courseId) {
      try {
        await axios.post(`/api/courses/${courseId}/view`)
        await this.fetchCourses()
        await this.fetchRecommendedCourses()
        this.$router.push(`/course/${courseId}`)
      } catch (error) {
        console.error('更新课程浏览量失败:', error)
        ElMessage.warning('浏览量未更新，将继续跳转')
        this.$router.push(`/course/${courseId}`)
      }
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
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 10px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);

    .el-menu {
      border-bottom: none;
      justify-content: center;
    }

    .el-submenu {
      .el-submenu__title {
        font-size: 14px;
        color: #606266;
        
        &:hover {
          color: #409EFF;
        }
      }
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

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
.carousel-item {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
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
  width: 100%;
  padding: 20px;
  color: white;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent 60%);
  transition: all 0.3s ease-in-out;
}

.course-name {
  font-size: 24px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 鼠标移上来高亮区域 */
.carousel-item:hover .carousel-overlay {
  background: rgba(0, 0, 0, 0.7);
}

.course-detail {
  margin-top: 10px;
  font-size: 14px;
}

/* 淡入动画 */
.fade-in-up-enter-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-in-up-enter-from {
  opacity: 0;
  transform: translateY(10px);
}
.fade-in-up-enter-to {
  opacity: 1;
  transform: translateY(0);
}

</style>
