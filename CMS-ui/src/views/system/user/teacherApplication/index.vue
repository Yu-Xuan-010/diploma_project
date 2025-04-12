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
      <el-table v-loading="loading" :data="applicationList" style="width: 100%">
        <!-- 申请编号（减少宽度） -->
        <el-table-column label="申请编号" prop="id" min-width="60" />

        <!-- 申请人（减少宽度） -->
        <el-table-column label="申请人" prop="userName" min-width="100" />

        <!-- 专业领域（自适应填充空间，避免空白） -->
        <el-table-column label="专业领域" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ getExpertiseNames(row.expertise) }}</span>
          </template>
        </el-table-column>

        <!-- 申请状态（减少宽度） -->
        <el-table-column label="申请状态" prop="status" min-width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 申请时间（稍微加宽，提高可读性） -->
        <el-table-column label="申请时间" prop="createdAt" min-width="180">
          <template #default="{ row }">
            {{ row.createdAt ? parseTime(row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') : '-' }}
          </template>
        </el-table-column>

        <!-- 操作列（固定在右侧，稍微加宽） -->
        <el-table-column label="操作" align="center" min-width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" icon="el-icon-view" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 'PENDING'" type="text" icon="el-icon-check" @click="handleApprove(row)">批准</el-button>
            <el-button v-if="row.status === 'PENDING'" type="text" icon="el-icon-close" @click="handleReject(row)">拒绝</el-button>
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
        <el-descriptions-item label="专业领域">{{ getExpertiseNames(form.expertise) }}</el-descriptions-item>
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
import request from '@/utils/request'
import { getToken } from '@/utils/auth'

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
      },
      // 专业列表
      majorList: [],
      // 当前用户信息
      currentUser: null
    }
  },
  created() {
    this.getList()
    this.getMajorList()
    this.getCurrentUser()
  },
  methods: {
    parseTime(time, format = '{y}-{m}-{d} {h}:{i}:{s}') {
      if (!time) return '-';
      const date = typeof time === 'string' ? new Date(time.replace(/-/g, '/')) : new Date(time);
      if (isNaN(date.getTime())) return '-'; // 处理无效时间
      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
      };
      return format.replace(/{(y|m|d|h|i|s)}/g, (match, key) => {
        return formatObj[key].toString().padStart(2, '0'); // 补零
      });
    },
    // 查询列表
    async getList() {
      this.loading = true;
      try {
        // 根据状态筛选
        let url = '/system/teacherApplication/list';
        if (this.queryParams.status) {
          url = `/system/teacherApplication/list/${this.queryParams.status}`;
        }
        
        const response = await request({
          url: url,
          method: 'get'
        });
        
        console.log('Teacher application response:', response); // 添加日志
        if (response.code === 200) {
          this.applicationList = response.data;
        } else {
          this.$message.error(response.msg || '获取教师申请列表失败');
        }
      } catch (error) {
        console.error('获取教师申请列表失败:', error);
        this.$message.error('获取教师申请列表失败');
      }
      this.loading = false;
    },
    // 获取专业领域名称
    getExpertiseNames(expertise) {
      if (!expertise || !this.majorList.length) return '-';
      try {
        // 确保 expertise 是字符串，防止传入数组
        const expertiseStr = Array.isArray(expertise) ? expertise.join(',') : String(expertise);
        
        return expertiseStr.split(',')
          .map(id => {
            const major = this.majorList.find(m => m.id === parseInt(id.trim()));
            return major ? major.name : '-';
          })
          .filter(name => name !== '-') // 过滤掉 '-'
          .join('、') || '-';
      } catch (error) {
        console.error('处理专业领域出错:', error, expertise);
        return expertise || '-';
      }
    },
    // 获取专业列表
    async getMajorList() {
      try {
        const response = await request({
          url: '/system/major/list',
          method: 'get'
        });
        console.log('Major list response:', response); // 添加日志
        if (response.code === 200) {
          this.majorList = response.data;
        } else {
          this.$message.error(response.msg || '获取专业列表失败');
        }
      } catch (error) {
        console.error('获取专业列表失败:', error);
        this.$message.error('获取专业列表失败');
      }
    },
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
    // 获取当前用户信息
    async getCurrentUser() {
      try {
        const response = await request({
          url: '/system/user/profile',
          method: 'get'
        })
        if (response.code === 200) {
          this.currentUser = response.data
        } else {
          this.$message.error('获取当前用户信息失败')
        }
      } catch (error) {
        console.error('获取当前用户信息失败:', error)
        this.$message.error('获取当前用户信息失败')
      }
    },
    // 提交审核
    async submitReview() {
      this.$refs.reviewFormRef.validate(async valid => {
        if (valid) {
          try {
            if (!this.currentUser || !this.currentUser.userId) {
              this.$message.error('无法获取当前用户信息')
              return
            }
            
            const reviewData = {
              ...this.reviewForm,
              reviewerId: this.currentUser.userId
            }
            
            await reviewTeacherApplication(reviewData)
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
