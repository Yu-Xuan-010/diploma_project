<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎使用课程管理系统</div>
    
    <!-- 统计数据卡片 -->
    <el-row :gutter="20" class="panel-group">
      <el-col :span="6">
        <div class="card total-courses">
          <div class="card-body">
            <div class="card-title">总课程数</div>
            <div class="card-value">{{ statistics.totalCourses || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card total-students">
          <div class="card-body">
            <div class="card-title">总学员数</div>
            <div class="card-value">{{ statistics.totalStudents || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card total-teachers">
          <div class="card-body">
            <div class="card-title">总讲师数</div>
            <div class="card-value">{{ statistics.totalTeachers || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card total-lessons">
          <div class="card-body">
            <div class="card-title">总课时数</div>
            <div class="card-value">{{ statistics.totalLessons || 0 }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 最近课程列表 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>最近课程</span>
      </div>
      <el-table :data="recentCourses" style="width: 100%">
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="teacherName" label="讲师"></el-table-column>
        <el-table-column prop="studentCount" label="学员数"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { formatDate } from '@/filters'

export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {
        totalCourses: 0,
        totalStudents: 0,
        totalTeachers: 0,
        totalLessons: 0
      },
      recentCourses: []
    }
  },
  created() {
    // 这里可以添加获取统计数据和最近课程的API调用
  },
  methods: {
    formatDate
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    padding: 20px;
  }
  
  &-text {
    font-size: 24px;
    line-height: 46px;
    color: #303133;
    margin-bottom: 20px;
  }
}

.panel-group {
  margin-bottom: 20px;
  
  .card {
    height: 108px;
    cursor: pointer;
    border-radius: 4px;
    position: relative;
    overflow: hidden;
    color: #fff;
    margin-bottom: 20px;
    
    &-body {
      padding: 20px;
    }
    
    &-title {
      font-size: 16px;
      margin-bottom: 10px;
    }
    
    &-value {
      font-size: 28px;
      font-weight: bold;
    }
  }
}

.total-courses {
  background: #40c9c6;
}

.total-students {
  background: #36a3f7;
}

.total-teachers {
  background: #f4516c;
}

.total-lessons {
  background: #34bfa3;
}

.box-card {
  margin-bottom: 20px;
}
</style> 