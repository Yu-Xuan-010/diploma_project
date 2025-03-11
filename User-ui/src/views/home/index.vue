<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header">
            <span>推荐课程</span>
            <el-button style="float: right; padding: 3px 0" type="text">查看更多</el-button>
          </div>
          <div class="course-list">
            <el-row :gutter="20">
              <el-col :span="8" v-for="i in 3" :key="i">
                <el-card :body-style="{ padding: '0px' }" shadow="hover">
                  <img src="https://via.placeholder.com/300x200" class="course-image">
                  <div style="padding: 14px;">
                    <span>示例课程 {{ i }}</span>
                    <div class="bottom">
                      <el-button type="text" class="button" @click="viewCourse(i)">查看详情</el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header">
            <span>学习进度</span>
          </div>
          <div class="progress-list">
            <div v-for="i in 3" :key="i" class="progress-item">
              <span>课程 {{ i }}</span>
              <el-progress :percentage="i * 30"></el-progress>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Home',
  methods: {
    viewCourse(id) {
      this.$router.push(`/course/detail/${id}`)
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.course-image {
  width: 100%;
  display: block;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.progress-list .progress-item {
  margin-bottom: 20px;
}
</style>
