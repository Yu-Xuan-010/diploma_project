<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="card-header">
        <span>教师申请列表</span>
      </div>
      
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="申请状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table v-loading="loading" :data="applicationList">
        <el-table-column label="申请编号" prop="id" width="80" />
        <el-table-column label="申请人" prop="userName" width="120" />
        <el-table-column label="专业领域" prop="expertise" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.expertise ? scope.row.expertise.split(',').join('、') : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="申请状态" prop="status" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" prop="createdAt" width="160">
          <template slot-scope="scope">
            {{ parseTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="审核时间" prop="reviewedAt" width="160">
          <template slot-scope="scope">
            {{ scope.row.status === 'PENDING' ? '未审核' : parseTime(scope.row.reviewedAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === 'PENDING'" type="text" icon="el-icon-check" @click="handleApprove(scope.row)">批准</el-button>
            <el-button v-if="scope.row.status === 'PENDING'" type="text" icon="el-icon-close" @click="handleReject(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请人">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">
          <el-tag :type="getStatusType(form.status)">{{ getStatusText(form.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="专业领域">{{ form.expertise ? form.expertise.split(',').join('、') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ parseTime(form.createdAt) }}</el-descriptions-item>
        <el-descriptions-item label="申请理由" :span="2">{{ form.reason }}</el-descriptions-item>
        <el-descriptions-item label="教学经验" :span="2">{{ form.experience }}</el-descriptions-item>
        <template v-if="form.status !== 'PENDING'">
          <el-descriptions-item label="审核人">{{ form.reviewerName }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ parseTime(form.reviewedAt) }}</el-descriptions-item>
          <el-descriptions-item label="审核意见" :span="2">{{ form.reviewComment }}</el-descriptions-item>
        </template>
      </el-descriptions>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog :title="reviewTitle" :visible.sync="reviewOpen" width="500px" append-to-body>
      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules" label-width="80px">
        <el-form-item label="审核意见" prop="reviewComment">
          <el-input v-model="reviewForm.reviewComment" type="textarea" :rows="4" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReview">确 定</el-button>
        <el-button @click="cancelReview">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTeacherApplications, getTeacherApplication, reviewTeacherApplication } from '@/api/system/teacherApplication'
import { parseTime } from '@/utils'

export default {
  name: 'TeacherApplication',
  data() {
    return {
      // 查询参数
      queryParams: {
        status: undefined
      },
      // 数据列表
      applicationList: [],
      loading: false,
      // 对话框控制
      open: false,
      reviewOpen: false,
      title: '',
      reviewTitle: '',
      // 表单数据
      form: {},
      reviewForm: {
        id: undefined,
        status: undefined,
        reviewComment: ''
      },
      // 表单校验规则
      reviewRules: {
        reviewComment: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待审核',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝'
      }
      return statusMap[status] || status
    },
    // 查询列表
    async getList() {
      this.loading = true
      try {
        const response = await listTeacherApplications(this.queryParams)
        this.applicationList = response.data
      } catch (error) {
        console.error('获取教师申请列表失败:', error)
        this.$message.error('获取教师申请列表失败')
      }
      this.loading = false
    },
    // 搜索按钮操作
    handleQuery() {
      this.getList()
    },
    // 重置按钮操作
    resetQuery() {
      this.queryParams.status = undefined
      this.handleQuery()
    },
    // 查看详情
    async handleView(row) {
      try {
        const response = await getTeacherApplication(row.id)
        this.form = response.data
        this.title = '查看教师申请详情'
        this.open = true
      } catch (error) {
        console.error('获取申请详情失败:', error)
        this.$message.error('获取申请详情失败')
      }
    },
    // 批准申请
    handleApprove(row) {
      this.reviewForm = {
        id: row.id,
        status: 'APPROVED',
        reviewComment: ''
      }
      this.reviewTitle = '批准教师申请'
      this.reviewOpen = true
    },
    // 拒绝申请
    handleReject(row) {
      this.reviewForm = {
        id: row.id,
        status: 'REJECTED',
        reviewComment: ''
      }
      this.reviewTitle = '拒绝教师申请'
      this.reviewOpen = true
    },
    // 提交审核
    async submitReview() {
      this.$refs.reviewFormRef.validate(async valid => {
        if (valid) {
          try {
            await reviewTeacherApplication(this.reviewForm)
            this.$message.success('审核成功')
            this.reviewOpen = false
            this.getList()
          } catch (error) {
            console.error('审核失败:', error)
            this.$message.error('审核失败')
          }
        }
      })
    },
    // 取消审核
    cancelReview() {
      this.reviewOpen = false
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
