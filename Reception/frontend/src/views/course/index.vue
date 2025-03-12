<template>
  <div class="course-container">
    <el-card class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="课程名称">
          <el-input v-model="queryParams.name" placeholder="课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程分类">
          <el-select v-model="queryParams.categoryId" placeholder="课程分类" clearable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="course-list">
      <el-col :span="6" v-for="course in courseList" :key="course.id">
        <el-card :body-style="{ padding: '0px' }" shadow="hover" class="course-item">
          <img :src="course.coverImg" class="course-image">
          <div class="course-info">
            <h3 class="course-title">{{ course.name }}</h3>
            <p class="course-desc">{{ course.description }}</p>
            <div class="course-meta">
              <span><i class="el-icon-user"></i> {{ course.teacherName }}</span>
              <span><i class="el-icon-view"></i> {{ course.viewCount }}</span>
            </div>
            <div class="course-action">
              <el-button type="primary" size="small" @click="viewCourse(course.id)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[8, 16, 32, 64]"
        :page-size="queryParams.pageSize"
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
      queryParams: {
        pageNum: 1,
        pageSize: 8,
        name: '',
        categoryId: undefined
      },
      categoryOptions: [],
      courseList: [],
      total: 0,
      loading: false
    }
  },
  created() {
    this.getCategoryList()
    this.getList()
  },
  methods: {
    getCategoryList() {
      // 获取课程分类列表
      this.$store.dispatch('course/getCategoryList').then(response => {
        this.categoryOptions = response.map(item => {
          return { value: item.id, label: item.name }
        })
      })
    },
    getList() {
      this.loading = true
      this.$store.dispatch('course/getList', this.queryParams).then(response => {
        this.courseList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 8,
        name: '',
        categoryId: undefined
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    viewCourse(id) {
      this.$router.push(`/course/detail/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.course-container {
  padding: 20px;

  .filter-container {
    margin-bottom: 20px;
  }

  .course-list {
    margin-bottom: 20px;
  }

  .course-item {
    margin-bottom: 20px;
    transition: all .3s;

    &:hover {
      transform: translateY(-5px);
    }
  }

  .course-image {
    width: 100%;
    height: 160px;
    object-fit: cover;
  }

  .course-info {
    padding: 14px;

    .course-title {
      margin: 0;
      font-size: 16px;
      color: #303133;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .course-desc {
      font-size: 14px;
      color: #606266;
      margin: 10px 0;
      height: 40px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .course-meta {
      color: #909399;
      font-size: 13px;
      margin-bottom: 10px;

      span {
        margin-right: 15px;

        i {
          margin-right: 5px;
        }
      }
    }

    .course-action {
      text-align: right;
    }
  }

  .pagination-container {
    text-align: center;
  }
}
</style>
