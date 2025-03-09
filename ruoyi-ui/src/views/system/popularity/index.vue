<template>
  <div class="app-container">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="card-row">
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">总播放量</div>
          <div class="card-value">{{ totalViewCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">总学习人数</div>
          <div class="card-value">{{ totalStudentCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">课程数量</div>
          <div class="card-value">{{ courseCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>课程热度对比</span>
          </div>
          <div id="popularityChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>热度趋势分析</span>
          </div>
          <div id="trendChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>课程热度详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>
      <el-table 
        v-loading="loading"
        :data="tableData" 
        border 
        stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="viewCount" label="播放量" sortable>
          <template slot-scope="scope">
            {{ scope.row.viewCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="studentCount" label="学习人数" sortable>
          <template slot-scope="scope">
            {{ scope.row.studentCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="统计时间" width="180">
          <template slot-scope="scope">
            {{ parseTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getCoursePopularity } from "@/api/system/popularity";
import { parseTime } from '@/utils/ruoyi';

export default {
  name: 'CoursePopularity',
  data() {
    return {
      loading: false,
      tableData: [],
      totalViewCount: 0,
      totalStudentCount: 0,
      courseCount: 0,
      charts: {
        popularityChart: null,
        trendChart: null
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
    refreshData() {
      this.fetchData();
    },
    fetchData() {
      this.loading = true;
      getCoursePopularity()
        .then(response => {
          console.log('API返回数据:', response);
          if (response.code === 200 && response.data) {
            const { trendList, totalList } = response.data;
            this.tableData = trendList;
            this.calculateSummary(totalList);
            this.initCharts(trendList, totalList);
          } else {
            this.$message.error(response.msg || '获取数据失败');
          }
        })
        .catch(error => {
          console.error("获取课程热度数据失败:", error);
          this.$message.error(error.message || '获取数据失败，请检查网络连接');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    calculateSummary(totalList) {
      this.totalViewCount = totalList.reduce((sum, item) => sum + (item.viewCount || 0), 0);
      this.totalStudentCount = totalList.reduce((sum, item) => sum + (item.studentCount || 0), 0);
      this.courseCount = totalList.length;
    },
    initCharts(trendList, totalList) {
      this.$nextTick(() => {
        this.initPopularityChart(totalList);
        this.initTrendChart(trendList);
      });
    },
    initPopularityChart(data) {
      const chart = echarts.init(document.getElementById('popularityChart'));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['播放量', '学习人数']
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
            name: '播放量'
          },
          {
            type: 'value',
            name: '人数'
          }
        ],
        series: [
          {
            name: '播放量',
            type: 'bar',
            data: data.map(item => item.viewCount || 0)
          },
          {
            name: '学习人数',
            type: 'line',
            yAxisIndex: 1,
            data: data.map(item => item.studentCount || 0)
          }
        ]
      };
      chart.setOption(option);
      this.charts.popularityChart = chart;
    },
    initTrendChart(data) {
      const chart = echarts.init(document.getElementById('trendChart'));
      const dates = [...new Set(data.map(item => parseTime(item.createdAt, '{y}-{m}-{d}')))];
      const courseNames = [...new Set(data.map(item => item.courseName))];
      
      // 按课程名称分组数据
      const seriesData = courseNames.map(name => {
        const courseData = data.filter(item => item.courseName === name);
        return {
          name: name,
          type: 'line',
          data: dates.map(date => {
            const item = courseData.find(d => parseTime(d.createdAt, '{y}-{m}-{d}') === date);
            return item ? item.viewCount : 0;
          })
        };
      });

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: courseNames,
          type: 'scroll'
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
          data: dates,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '播放量'
        },
        series: seriesData
      };
      chart.setOption(option);
      this.charts.trendChart = chart;
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
.app-container {
  padding: 20px;
}
.card-row {
  margin-bottom: 20px;
}
.data-card {
  text-align: center;
  padding: 20px;
}
.card-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}
.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}
.chart-row {
  margin-top: 20px;
}
.box-card {
  margin-bottom: 20px;
}
</style> 