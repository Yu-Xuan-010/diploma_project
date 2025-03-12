<template>
  <div class="course-list-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索课程"
        class="search-input"
        clearable
        @keyup.enter.native="handleSearch"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
      </el-input>
      <el-select v-model="selectedCategory" placeholder="课程分类" clearable @change="handleCategoryChange">
        <el-option
          v-for="item in categories"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </div>

    <!-- 课程列表 -->
    <el-row :gutter="20" class="course-list">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="course in courses" :key="course.id">
        <el-card class="course-card" :body-style="{ padding: '0px' }">
          <div class="course-image">
            <img :src="course.cover" :alt="course.title">
          </div>
          <div class="course-info">
            <h3 class="course-title">{{ course.title }}</h3>
            <p class="course-description">{{ course.description }}</p>
            <div class="course-meta">
              <span>
                <i class="el-icon-user"></i>
                {{ course.studentCount }}人学习
              </span>
              <span>
                <i class="el-icon-star-off"></i>
                {{ course.rating }}分
              </span>
            </div>
            <div class="course-footer">
              <span class="course-price">￥{{ course.price }}</span>
              <el-button type="primary" size="small" @click="viewCourseDetail(course.id)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[12, 24, 36, 48]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CourseList',
  data() {
    return {
      searchQuery: '',
      selectedCategory: '',
      currentPage: 1,
      pageSize: 12,
      total: 0,
      courses: [],
      categories: [
        { value: '1', label: '前端开发' },
        { value: '2', label: '后端开发' },
        { value: '3', label: '移动开发' },
        { value: '4', label: '数据库' },
        { value: '5', label: '运维' }
      ]
    }
  },
  created() {
    this.fetchCourses()
  },
  methods: {
    fetchCourses() {
      // 这里调用获取课程列表API
      // 模拟数据
      this.courses = Array(8).fill().map((_, index) => ({
        id: index + 1,
        title: `示例课程 ${index + 1}`,
        description: '这是一个示例课程描述，介绍课程的主要内容和特点。',
        cover: 'https://via.placeholder.com/300x200',
        studentCount: Math.floor(Math.random() * 1000),
        rating: (Math.random() * 2 + 3).toFixed(1),
        price: Math.floor(Math.random() * 200 + 100)
      }))
      this.total = 100
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchCourses()
    },
    handleCategoryChange() {
      this.currentPage = 1
      this.fetchCourses()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchCourses()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchCourses()
    },
    viewCourseDetail(courseId) {
      this.$router.push(`/course/detail/${courseId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.course-list-container {
  padding: 20px;

  .search-bar {
    display: flex;
    margin-bottom: 20px;
    gap: 20px;

    .search-input {
      width: 300px;
    }
  }

  .course-list {
    margin-bottom: 20px;
  }

  .course-card {
    margin-bottom: 20px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }

    .course-image {
      height: 200px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .course-info {
      padding: 15px;

      .course-title {
        margin: 0 0 10px;
        font-size: 16px;
        font-weight: bold;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .course-description {
        height: 40px;
        margin: 0 0 10px;
        font-size: 14px;
        color: #666;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .course-meta {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
        font-size: 13px;
        color: #999;

        i {
          margin-right: 5px;
        }
      }

      .course-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .course-price {
          color: #f56c6c;
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
  }

  .pagination {
    text-align: center;
    margin-top: 20px;
  }
}
</style> 