<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>大学在线课程管理系统</h2>
        <p>
          本系统旨在为高校学生提供一个高效、便捷的在线学习平台，支持课程分类浏览、个性化课程推荐、学习进度跟踪等功能。用户可以随时随地自主学习，系统根据学习行为分析提供智能化的课程推荐，帮助学生更高效地规划学习路径。同时，管理员可通过后台管理课程信息，优化课程资源配置，提升学习体验。
          本系统致力于打造数据驱动的学习平台，让学习变得更加智能、高效！
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>系统特点</span>
          </div>
          <div class="body">
            <ul>
              <li>课程分类与检索：智能分类，快速定位所需课程</li>
              <li>学习进度跟踪：实时记录，清晰展示学习情况</li>
              <li>个性化推荐系统：基于学习行为，智能推荐课程</li>
              <li>用户反馈与评价：互动交流，持续优化学习体验</li>
              <li>数据可视化分析：直观展示学习数据，助力学习规划</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>用户指南</span>
          </div>
          <div class="body">
            <ul>
              <li>注册/登录：点击右上角"立即注册"或"登录"按钮</li>
              <li>查找课程：使用搜索框或课程分类浏览</li>
              <li>加入课程：点击课程详情页的"加入课程"按钮</li>
              <li>学习进度：在"我的课程"中查看详细进度</li>
              <li>课程推荐：系统根据您的学习记录自动推荐</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>常见问题</span>
          </div>
          <div class="body">
            <ul>
              <li>修改信息：在个人中心进行信息更新</li>
              <li>课程访问：确保网络正常，使用推荐浏览器</li>
              <li>进度保存：系统自动保存，无需手动操作</li>
              <li>技术支持：遇到问题请联系客服</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>最新公告</span>
          </div>
          <div class="body">
            <ul>
              <li>2024-03-20：系统升级维护通知</li>
              <li>2024-03-18：新增10门精品课程</li>
              <li>2024-03-15：优化课程推荐算法</li>
              <li>2024-03-10：新增移动端适配功能</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>热门课程</span>
          </div>
          <div class="body">
            <ul>
              <li>高等数学（上）</li>
              <li>大学物理</li>
              <li>程序设计基础</li>
              <li>英语听说训练</li>
              <li>线性代数</li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>用户反馈</span>
          </div>
          <div class="body">
            <ul>
              <li>张同学：课程内容非常实用，讲解清晰</li>
              <li>李同学：推荐系统很准确，帮助很大</li>
              <li>王同学：界面设计简洁美观，使用方便</li>
              <li>刘同学：学习进度跟踪功能很实用</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    margin-bottom: 20px;
    .body {
      ul {
        padding-left: 20px;
        li {
          margin-bottom: 10px;
          color: #666;
          line-height: 1.6;
        }
      }
    }
  }
}
</style>
