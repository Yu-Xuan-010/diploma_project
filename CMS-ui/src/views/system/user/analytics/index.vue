<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 用户活跃趋势图 -->
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用户活跃趋势</span>
          </div>
          <div id="activityChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 课程学习情况 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>课程学习情况</span>
          </div>
          <div id="courseChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <!-- 学习偏好分布 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>学习偏好分布</span>
          </div>
          <div id="preferenceChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 用户学习详情表格 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>用户学习详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>
      <el-table v-loading="loading" :data="userDetails" border stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="totalStudyTime" label="总学习时长">
          <template slot-scope="scope">
            {{ formatStudyTime(scope.row.totalStudyTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="completedCourses" label="已完成课程数" />
        <el-table-column prop="lastStudyTime" label="最近学习时间">
          <template slot-scope="scope">
            {{ parseTime(scope.row.lastStudyTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getAnalyticsData } from '@/api/system/userAnalytics';
import { parseTime } from '@/utils/ruoyi';

export default {
  name: "UserAnalytics",
  data() {
    return {
      loading: false,
      userDetails: [],
      charts: {
        activityChart: null,
        courseChart: null,
        preferenceChart: null
      }
    };
  },
  mounted() {
    this.fetchData();
    window.addEventListener('resize', this.resizeCharts);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts);
    Object.values(this.charts).forEach(chart => {
      chart && chart.dispose();
    });
  },
  methods: {
    parseTime,
    formatStudyTime(minutes) {
      if (!minutes) return '0小时';
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return `${hours}小时${mins}分钟`;
    },
    refreshData() {
      this.fetchData();
    },
    fetchData() {
      this.loading = true;
      getAnalyticsData()
        .then(response => {
          console.log('获取到的数据:', response);
          if (response.code === 200 && response.data) {
            const { activityTrend, courseStats, preferences, userDetails } = response.data;
            
            // 检查数据是否为空
            if (!activityTrend || activityTrend.length === 0) {
              console.warn('活跃度数据为空');
            }
            if (!courseStats || courseStats.length === 0) {
              console.warn('课程统计数据为空');
            }
            if (!preferences || preferences.length === 0) {
              console.warn('学习偏好数据为空');
            }
            if (!userDetails || userDetails.length === 0) {
              console.warn('用户详情数据为空');
            }

            this.userDetails = userDetails || [];
            this.initCharts(activityTrend || [], courseStats || [], preferences || []);
          } else {
            this.$message.error(response.msg || '获取数据失败');
          }
        })
        .catch(error => {
          console.error("获取数据失败:", error);
          this.$message.error(error.message || '获取数据失败，请检查网络连接');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    initCharts(activityTrend, courseStats, preferences) {
      this.$nextTick(() => {
        try {
          console.log('初始化图表数据:', {
            activityTrend,
            courseStats,
            preferences
          });
          
          if (activityTrend.length > 0) {
            this.initActivityChart(activityTrend);
          }
          if (courseStats.length > 0) {
            this.initCourseChart(courseStats);
          }
          if (preferences.length > 0) {
            this.initPreferenceChart(preferences);
          }
        } catch (error) {
          console.error('初始化图表时出错:', error);
          this.$message.error('初始化图表失败：' + error.message);
        }
      });
    },
    initActivityChart(data) {
      const chart = echarts.init(document.getElementById('activityChart'));
      const dates = data.map(item => parseTime(item.date, '{y}-{m}-{d}'));
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['日活跃用户', '月活跃用户', '新增用户']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '日活跃用户',
            type: 'line',
            data: data.map(item => item.dau)
          },
          {
            name: '月活跃用户',
            type: 'line',
            data: data.map(item => item.mau)
          },
          {
            name: '新增用户',
            type: 'line',
            data: data.map(item => item.newUsers)
          }
        ]
      };
      chart.setOption(option);
      this.charts.activityChart = chart;
    },
    initCourseChart(data) {
      const chart = echarts.init(document.getElementById('courseChart'));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['学习人数', '完成率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.courseName),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '人数'
          },
          {
            type: 'value',
            name: '完成率',
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          }
        ],
        series: [
          {
            name: '学习人数',
            type: 'bar',
            data: data.map(item => item.studentCount)
          },
          {
            name: '完成率',
            type: 'line',
            yAxisIndex: 1,
            data: data.map(item => item.completionRate)
          }
        ]
      };
      chart.setOption(option);
      this.charts.courseChart = chart;
    },
    initPreferenceChart(data) {
      const chart = echarts.init(document.getElementById('preferenceChart'));
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: data.map(item => item.preferenceCategory)
        },
        series: [
          {
            name: '学习偏好',
            type: 'pie',
            radius: '50%',
            data: data.map(item => ({
              name: item.preferenceCategory,
              value: item.userCount
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      chart.setOption(option);
      this.charts.preferenceChart = chart;
    },
    resizeCharts() {
      Object.values(this.charts).forEach(chart => {
        chart && chart.resize();
      });
    }
  }
};
</script>

<style scoped>
.chart-row {
  margin-top: 20px;
  margin-bottom: 20px;
}
.box-card {
  margin-bottom: 20px;
}
</style> 