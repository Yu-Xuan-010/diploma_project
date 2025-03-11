<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>我的学习计划</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddPlan">
              <i class="el-icon-plus"></i> 新建计划
            </el-button>
          </div>

          <el-timeline>
            <el-timeline-item
              v-for="plan in plans"
              :key="plan.id"
              :type="getTimelineItemType(plan.status)"
              :timestamp="plan.deadline"
              placement="top"
            >
              <el-card>
                <div class="plan-header">
                  <h4>{{ plan.title }}</h4>
                  <div class="plan-actions">
                    <el-tag :type="getStatusTag(plan.status)">{{ getStatusText(plan.status) }}</el-tag>
                    <el-dropdown trigger="click" @command="handleCommand($event, plan)">
                      <span class="el-dropdown-link">
                        <i class="el-icon-more"></i>
                      </span>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="edit">编辑</el-dropdown-item>
                        <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </div>
                </div>
                <p class="plan-description">{{ plan.description }}</p>
                <div class="plan-courses">
                  <h5>相关课程：</h5>
                  <el-tag
                    v-for="course in plan.courses"
                    :key="course.id"
                    size="small"
                    @click="viewCourse(course)"
                    style="cursor: pointer; margin-right: 8px;"
                  >
                    {{ course.name }}
                  </el-tag>
                </div>
                <div class="plan-progress">
                  <span>完成进度：</span>
                  <el-progress :percentage="plan.progress"></el-progress>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>学习统计</span>
          </div>
          <div class="statistics">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.totalPlans }}</div>
              <div class="stat-label">总计划数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ statistics.completedPlans }}</div>
              <div class="stat-label">已完成</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ statistics.inProgressPlans }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
          <el-divider></el-divider>
          <h4>近期截止</h4>
          <el-table :data="upcomingDeadlines" style="width: 100%">
            <el-table-column prop="title" label="计划"></el-table-column>
            <el-table-column prop="deadline" label="截止时间" width="100"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新建/编辑计划对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form :model="planForm" :rules="planRules" ref="planForm" label-width="100px">
        <el-form-item label="计划名称" prop="title">
          <el-input v-model="planForm.title" placeholder="请输入计划名称"></el-input>
        </el-form-item>
        <el-form-item label="计划描述" prop="description">
          <el-input type="textarea" v-model="planForm.description" placeholder="请输入计划描述"></el-input>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="planForm.deadline"
            type="datetime"
            placeholder="选择截止时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="相关课程" prop="courses">
          <el-select
            v-model="planForm.courses"
            multiple
            filterable
            placeholder="请选择相关课程"
          >
            <el-option
              v-for="course in courseOptions"
              :key="course.id"
              :label="course.name"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitPlan">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudyPlans, createStudyPlan, updateStudyPlan, deleteStudyPlan } from '@/api/learning'
import { getCourseList } from '@/api/course'

export default {
  name: 'LearningPlan',
  data() {
    return {
      plans: [],
      statistics: {
        totalPlans: 0,
        completedPlans: 0,
        inProgressPlans: 0
      },
      upcomingDeadlines: [],
      courseOptions: [],
      dialogVisible: false,
      dialogTitle: '新建学习计划',
      planForm: {
        id: undefined,
        title: '',
        description: '',
        deadline: '',
        courses: []
      },
      planRules: {
        title: [
          { required: true, message: '请输入计划名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入计划描述', trigger: 'blur' }
        ],
        deadline: [
          { required: true, message: '请选择截止时间', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadPlans()
    this.loadCourses()
  },
  methods: {
    loadPlans() {
      getStudyPlans().then(response => {
        this.plans = response.data.plans
        this.statistics = response.data.statistics
        this.upcomingDeadlines = response.data.upcomingDeadlines
      })
    },
    loadCourses() {
      getCourseList().then(response => {
        this.courseOptions = response.data
      })
    },
    getTimelineItemType(status) {
      switch (status) {
        case 'completed': return 'success'
        case 'in_progress': return 'primary'
        case 'overdue': return 'danger'
        default: return 'info'
      }
    },
    getStatusTag(status) {
      switch (status) {
        case 'completed': return 'success'
        case 'in_progress': return ''
        case 'overdue': return 'danger'
        default: return 'info'
      }
    },
    getStatusText(status) {
      switch (status) {
        case 'completed': return '已完成'
        case 'in_progress': return '进行中'
        case 'overdue': return '已逾期'
        default: return '未开始'
      }
    },
    handleAddPlan() {
      this.dialogTitle = '新建学习计划'
      this.planForm = {
        id: undefined,
        title: '',
        description: '',
        deadline: '',
        courses: []
      }
      this.dialogVisible = true
    },
    handleCommand(command, plan) {
      switch (command) {
        case 'edit':
          this.dialogTitle = '编辑学习计划'
          this.planForm = { ...plan }
          this.dialogVisible = true
          break
        case 'delete':
          this.$confirm('确认删除该学习计划吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteStudyPlan(plan.id).then(() => {
              this.$message.success('删除成功')
              this.loadPlans()
            })
          }).catch(() => {})
          break
      }
    },
    submitPlan() {
      this.$refs.planForm.validate(valid => {
        if (valid) {
          const action = this.planForm.id ? updateStudyPlan : createStudyPlan
          action(this.planForm).then(() => {
            this.$message.success(this.planForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.loadPlans()
          })
        }
      })
    },
    viewCourse(course) {
      this.$router.push(`/course/detail/${course.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;

  h4 {
    margin: 0;
  }

  .plan-actions {
    display: flex;
    align-items: center;
    gap: 10px;
  }
}

.plan-description {
  color: #666;
  margin-bottom: 15px;
}

.plan-courses {
  margin: 15px 0;
  h5 {
    margin: 0 0 10px;
  }
}

.plan-progress {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 15px;
}

.statistics {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;

  .stat-item {
    text-align: center;

    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #409EFF;
    }

    .stat-label {
      color: #666;
      margin-top: 5px;
    }
  }
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
</style>
