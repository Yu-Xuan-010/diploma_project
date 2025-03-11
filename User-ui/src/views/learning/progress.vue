<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="overview-card">
          <div class="overview-header">
            <h2>学习概览</h2>
            <el-select v-model="timeRange" placeholder="时间范围" @change="handleTimeRangeChange">
              <el-option label="本周" value="week"></el-option>
              <el-option label="本月" value="month"></el-option>
              <el-option label="全部" value="all"></el-option>
            </el-select>
          </div>
          <el-row :gutter="20" class="overview-stats">
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon">
                  <i class="el-icon-time"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ overview.totalLearningTime }}h</div>
                  <div class="stat-label">总学习时长</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon">
                  <i class="el-icon-reading"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ overview.completedCourses }}</div>
                  <div class="stat-label">已完成课程</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon">
                  <i class="el-icon-document"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ overview.completedChapters }}</div>
                  <div class="stat-label">已完成章节</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <div class="stat-icon">
                  <i class="el-icon-medal"></i>
                </div>
                <div class="stat-info">
                  <div class="stat-value">{{ overview.earnedPoints }}</div>
                  <div class="stat-label">获得积分</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span>学习进度追踪</span>
          </div>
          <el-table :data="progressList" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称">
              <template slot-scope="scope">
                <el-link type="primary" @click="viewCourse(scope.row)">{{ scope.row.courseName }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="totalChapters" label="总章节" width="100"></el-table-column>
            <el-table-column prop="completedChapters" label="已完成" width="100"></el-table-column>
            <el-table-column label="完成进度" width="200">
              <template slot-scope="scope">
                <el-progress
                  :percentage="scope.row.progress"
                  :status="scope.row.progress === 100 ? 'success' : ''"
                ></el-progress>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button
                  type="primary"
                  size="mini"
                  @click="continueLearning(scope.row)"
                >继续学习</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>学习时间分布</span>
          </div>
          <div class="chart-container">
            <div ref="timeDistributionChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header">
            <span>最近学习记录</span>
          </div>
          <div class="recent-records">
            <el-timeline>
              <el-timeline-item
                v-for="record in recentRecords"
                :key="record.id"
                :timestamp="record.time"
                placement="top"
              >
                <p>学习了 {{ record.courseName }}</p>
                <p class="record-detail">{{ record.chapterName }}</p>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getLearningProgress, getLearningOverview } from '@/api/learning'
import * as echarts from 'echarts'

export default {
  name: 'LearningProgress',
  data() {
    return {
      timeRange: 'week',
      overview: {
        totalLearningTime: 0,
        completedCourses: 0,
        completedChapters: 0,
        earnedPoints: 0
      },
      progressList: [],
      recentRecords: [],
      timeDistributionChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initChart()
  },
  beforeDestroy() {
    if (this.timeDistributionChart) {
      this.timeDistributionChart.dispose()
    }
  },
  methods: {
    loadData() {
      getLearningProgress(this.timeRange).then(response => {
        this.progressList = response.data.progress
        this.recentRecords = response.data.recentRecords
        this.updateChart(response.data.timeDistribution)
      })
      getLearningOverview(this.timeRange).then(response => {
        this.overview = response.data
      })
    },
    handleTimeRangeChange() {
      this.loadData()
    },
    initChart() {
      this.timeDistributionChart = echarts.init(this.$refs.timeDistributionChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value',
          name: '学习时长（小时）'
        },
        series: [{
          data: [],
          type: 'bar',
          name: '学习时长',
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.timeDistributionChart.setOption(option)
    },
    updateChart(data) {
      const option = {
        xAxis: {
          data: data.map(item => item.date)
        },
        series: [{
          data: data.map(item => item.duration)
        }]
      }
      this.timeDistributionChart.setOption(option)
    },
    viewCourse(course) {
      this.$router.push(`/course/detail/${course.id}`)
    },
    continueLearning(course) {
      this.$router.push(`/course/learn/${course.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.overview-card {
  .overview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
    }
  }

  .overview-stats {
    .stat-card {
      background-color: #f5f7fa;
      border-radius: 8px;
      padding: 20px;
      display: flex;
      align-items: center;

      .stat-icon {
        font-size: 40px;
        color: #409EFF;
        margin-right: 15px;
      }

      .stat-info {
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
        }

        .stat-label {
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
}

.chart-container {
  margin: 20px 0;
}

.recent-records {
  .record-detail {
    color: #909399;
    font-size: 13px;
    margin-top: 4px;
  }
}

.el-timeline-item {
  padding-bottom: 20px;
}
</style>
