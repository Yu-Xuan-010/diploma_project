<template>
  <div class="teacher-applications-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>教师申请管理</span>
          <el-radio-group v-model="filterStatus" @change="handleFilterChange">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="pending">待审核</el-radio-button>
            <el-radio-button label="approved">已通过</el-radio-button>
            <el-radio-button label="rejected">已拒绝</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="applications" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="reason" label="申请理由" show-overflow-tooltip />
        <el-table-column prop="expertise" label="专业领域" show-overflow-tooltip />
        <el-table-column prop="experience" label="教学经验" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="success"
              size="small"
              @click="handleReview(scope.row, true)"
            >
              通过
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="danger"
              size="small"
              @click="handleReview(scope.row, false)"
            >
              拒绝
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="showDetails(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      :title="reviewAction === 'approve' ? '通过申请' : '拒绝申请'"
      width="500px"
    >
      <el-form :model="reviewForm" label-width="100px">
        <el-form-item label="审核意见" required>
          <el-input
            v-model="reviewForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button
            :type="reviewAction === 'approve' ? 'success' : 'danger'"
            @click="submitReview"
          >
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailsDialogVisible"
      title="申请详情"
      width="600px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="申请ID">{{ selectedApplication.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ selectedApplication.userId }}</el-descriptions-item>
        <el-descriptions-item label="申请理由">{{ selectedApplication.reason }}</el-descriptions-item>
        <el-descriptions-item label="专业领域">{{ selectedApplication.expertise }}</el-descriptions-item>
        <el-descriptions-item label="教学经验">{{ selectedApplication.experience }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">
          <el-tag :type="getStatusType(selectedApplication.status)">
            {{ getStatusText(selectedApplication.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ selectedApplication.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ selectedApplication.updatedAt }}</el-descriptions-item>
        <el-descriptions-item label="审核意见">{{ selectedApplication.reviewComment }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'TeacherApplications',
  setup() {
    const applications = ref([])
    const loading = ref(false)
    const filterStatus = ref('all')
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const reviewDialogVisible = ref(false)
    const detailsDialogVisible = ref(false)
    const reviewAction = ref('approve')
    const selectedApplication = ref({})
    const reviewForm = ref({
      comment: ''
    })

    const getStatusType = (status) => {
      const types = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return types[status] || 'info'
    }

    const getStatusText = (status) => {
      const texts = {
        'PENDING': '待审核',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝'
      }
      return texts[status] || '未知'
    }

    const fetchApplications = async () => {
      loading.value = true
      try {
        const response = await axios.get('/api/admin/teacher-applications')
        applications.value = response.data
        total.value = response.data.length
      } catch (error) {
        ElMessage.error('获取申请列表失败')
      } finally {
        loading.value = false
      }
    }

    const handleFilterChange = () => {
      currentPage.value = 1
      fetchApplications()
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      fetchApplications()
    }

    const handleCurrentChange = (val) => {
      currentPage.value = val
      fetchApplications()
    }

    const handleReview = (application, isApprove) => {
      selectedApplication.value = application
      reviewAction.value = isApprove ? 'approve' : 'reject'
      reviewForm.value.comment = ''
      reviewDialogVisible.value = true
    }

    const submitReview = async () => {
      if (!reviewForm.value.comment) {
        ElMessage.warning('请输入审核意见')
        return
      }

      try {
        await axios.post(`/api/admin/teacher-applications/${selectedApplication.value.id}/review`, null, {
          params: {
            approved: reviewAction.value === 'approve',
            comment: reviewForm.value.comment,
            reviewerId: 1 // TODO: 从用户状态获取当前管理员ID
          }
        })
        ElMessage.success('审核完成')
        reviewDialogVisible.value = false
        fetchApplications()
      } catch (error) {
        ElMessage.error('审核失败：' + error.response?.data?.message || '未知错误')
      }
    }

    const showDetails = (application) => {
      selectedApplication.value = application
      detailsDialogVisible.value = true
    }

    onMounted(() => {
      fetchApplications()
    })

    return {
      applications,
      loading,
      filterStatus,
      currentPage,
      pageSize,
      total,
      reviewDialogVisible,
      detailsDialogVisible,
      reviewAction,
      selectedApplication,
      reviewForm,
      getStatusType,
      getStatusText,
      handleFilterChange,
      handleSizeChange,
      handleCurrentChange,
      handleReview,
      submitReview,
      showDetails
    }
  }
}
</script>

<style lang="scss" scoped>
.teacher-applications-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 