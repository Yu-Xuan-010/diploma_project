<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="card-row">
      <el-col :span="6" v-for="(item, index) in summaryData" :key="index">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">{{item.title}}</div>
          <div class="card-value">{{item.value}}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>课程学习详细数据</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="statisticsList"
        border
        stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="completedStudents" label="完成人数" />
        <el-table-column prop="activeStudents" label="活跃学习者" />
      </el-table>
    </el-card>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>课程学习进度分布</span>
          </div>
          <div id="progressChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>课程参与度分析</span>
          </div>
          <div id="participationChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getCourseStatistics } from '@/api/system/statistical';



export default {
  name: 'CourseStatistics',
  data() {
    return {
      loading: false,
      /** @type {CourseStatistics[]} */
      statisticsList: [],
      summaryData: [],
      charts: {
        progressChart: null,
        participationChart: null
      }
    };
  },
  mounted() {
    this.fetchStatistics();
    // 监听窗口大小变化，调整图表大小
    window.addEventListener('resize', this.resizeCharts);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts);
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      chart && chart.dispose();
    });
  },
  methods: {
    refreshData() {
      this.fetchStatistics();
    },
    formatNumber(value) {
      return value ? Number(value).toFixed(2) : '0.00';
    },
    formatStudyTime(minutes) {
      if (!minutes) return '0小时';
      const hours = (minutes / 60).toFixed(2);
      return `${hours}小时`;
    },
    fetchStatistics() {
      this.loading = true;
      getCourseStatistics()
        .then(response => {
          console.log('API返回数据:', response);
          if (response.code === 200) {
            if (response.data && response.data.length > 0) {
              this.statisticsList = response.data;
              this.calculateSummary();
              this.initCharts();
            } else {
              this.$message.warning('暂无课程统计数据');
            }
          } else {
            this.$message.error(response.msg || '获取数据失败');
          }
        })
        .catch(error => {
          console.error("获取统计数据失败:", error);
          this.$message.error(error.message || '获取统计数据失败，请检查网络连接');
        })
        .finally(() => {
          this.loading = false;
        });
    },
    calculateSummary() {
      try {
        const avgCompletion = this.statisticsList.reduce((sum, item) => sum + (item.completionRate || 0), 0) /
          (this.statisticsList.length || 1);
        const activeStudents = this.statisticsList.reduce((sum, item) => sum + (item.activeStudents || 0), 0);

        this.summaryData = [
          { title: '平均完成率', value: this.formatNumber(avgCompletion) + '%' },
          { title: '活跃学习者', value: activeStudents },
          { title: '课程数量', value: this.statisticsList.length }
        ];
      } catch (error) {
        console.error('计算汇总数据时出错:', error);
        this.$message.error('计算统计数据时出错');
      }
    },
    initCharts() {
      this.$nextTick(() => {
        try {
          this.initProgressChart();
          this.initParticipationChart();
        } catch (error) {
          console.error('初始化图表时出错:', error);
          this.$message.error('初始化图表时出错');
        }
      });
    },
    initProgressChart() {
      if (!this.statisticsList.length) {
        console.warn('没有数据用于绘制进度图表');
        return;
      }

      const chart = echarts.init(document.getElementById('progressChart'));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['平均进度', '完成率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.statisticsList.map(item => item.courseName),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          max: 100,
          name: '百分比'
        },
        series: [
          {
            name: '平均进度',
            type: 'bar',
            data: this.statisticsList.map(item => Number(item.averageProgress || 0).toFixed(2))
          },
          {
            name: '完成率',
            type: 'line',
            data: this.statisticsList.map(item => Number(item.completionRate || 0).toFixed(2))
          }
        ]
      };
      chart.setOption(option);
      this.charts.progressChart = chart;
    },
    initParticipationChart() {
      if (!this.statisticsList.length) {
        console.warn('没有数据用于绘制参与度图表');
        return;
      }

      const chart = echarts.init(document.getElementById('participationChart'));
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['活跃学习者', '完成人数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.statisticsList.map(item => item.courseName),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '人数'
        },
        series: [
          {
            name: '活跃学习者',
            type: 'bar',
            data: this.statisticsList.map(item => item.activeStudents || 0)
          },
          {
            name: '完成人数',
            type: 'bar',
            data: this.statisticsList.map(item => item.completedStudents || 0)
          }
        ]
      };
      chart.setOption(option);
      this.charts.participationChart = chart;
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
