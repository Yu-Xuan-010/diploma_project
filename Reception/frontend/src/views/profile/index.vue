<template>
    <div class="profile-container">
      <el-row :gutter="20">
        <!-- 左侧：侧边栏 -->
        <el-col :span="6">
          <el-menu
            default-active="basic"
            class="el-menu-vertical"
            @select="handleMenuSelect"
          >
            <el-menu-item index="basic">
              <el-icon><User /></el-icon>
              <span>个人资料</span>
            </el-menu-item>
            <el-menu-item index="learning">
              <el-icon><Reading /></el-icon>
              <span>学习记录</span>
            </el-menu-item>
            <el-menu-item index="favorites">
              <el-icon><Star /></el-icon>
              <span>课程收藏</span>
            </el-menu-item>
            <el-menu-item index="notes">
              <el-icon><Document /></el-icon>
              <span>我的笔记</span>
            </el-menu-item>
            <el-menu-item index="security">
              <el-icon><Lock /></el-icon>
              <span>账号安全</span>
            </el-menu-item>
            <el-menu-item v-if="isTeacher" index="upload">
              <el-icon><Upload /></el-icon>
              <span>上传课程</span>
            </el-menu-item>
          </el-menu>
        </el-col>
  
        <!-- 右侧：对应的内容 -->
        <el-col :span="18">
          <!-- 个人资料 -->
          <div v-if="activeTab === 'basic'" class="content-section">
            <h2>个人资料</h2>
            <el-card>
              <div class="profile-header">
                <h3>基本信息</h3>
                <el-button 
                  v-if="!isEditing" 
                  type="primary" 
                  @click="startEditing"
                >
                  修改资料
                </el-button>
                <div v-else class="edit-actions">
                  <el-button @click="cancelEditing">取消</el-button>
                  <el-button type="primary" @click="saveProfile">保存</el-button>
                </div>
              </div>
              <el-form :model="userInfo" :rules="rules" ref="userForm" label-width="100px">
                <el-form-item label="用户名" prop="username">
                  <span v-if="!isEditing">{{ userInfo.username }}</span>
                  <el-input v-else v-model="userInfo.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="真实姓名" prop="realName">
                  <span v-if="!isEditing">{{ userInfo.realName || '未设置' }}</span>
                  <el-input v-else v-model="userInfo.realName" placeholder="请输入真实姓名"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                  <span v-if="!isEditing">{{ userInfo.nickname || '未设置' }}</span>
                  <el-input v-else v-model="userInfo.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>
                <el-form-item label="头像">
                  <el-upload
                    class="avatar-uploader"
                    action="/api/file/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :on-error="handleAvatarError"
                    :before-upload="beforeAvatarUpload"
                    :disabled="!isEditing"
                    name="file"
                  >
                    <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar">
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="userInfo.gender" :disabled="!isEditing">
                    <el-radio label="male">男</el-radio>
                    <el-radio label="female">女</el-radio>
                    <el-radio label="other">保密</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="生日" prop="birthday">
                  <span v-if="!isEditing">{{ userInfo.birthday ? formatDate(userInfo.birthday) : '未设置' }}</span>
                  <el-date-picker
                      v-else
                      v-model="userInfo.birthday"
                      type="date"
                      placeholder="选择生日"
                      format="YYYY年MM月DD日"
                      value-format="YYYY-MM-DD"
                      :shortcuts="false"
                      :disabled-date="(time) => time > new Date()"
                      :locale="{
                        name: 'zh-cn',
                        el: {
                          datepicker: {
                            now: '此刻',
                            today: '今天',
                            cancel: '取消',
                            clear: '清除',
                            confirm: '确定',
                            selectDate: '选择日期',
                            selectTime: '选择时间',
                            startDate: '开始日期',
                            startTime: '开始时间',
                            endDate: '结束日期',
                            endTime: '结束时间',
                            prevYear: '前一年',
                            nextYear: '后一年',
                            prevMonth: '上个月',
                            nextMonth: '下个月',
                            year: '年',
                            month1: '1 月',
                            month2: '2 月',
                            month3: '3 月',
                            month4: '4 月',
                            month5: '5 月',
                            month6: '6 月',
                            month7: '7 月',
                            month8: '8 月',
                            month9: '9 月',
                            month10: '10 月',
                            month11: '11 月',
                            month12: '12 月',
                            weeks: ['日', '一', '二', '三', '四', '五', '六'],
                            placeholder: '请选择日期'
                          }
                        }
                      }"
                  ></el-date-picker>
                </el-form-item>
                <el-form-item label="邮箱">
                  <span v-if="!isEditing">{{ userInfo.email || '未设置' }}</span>
                  <el-input v-else v-model="userInfo.email" placeholder="请输入邮箱"></el-input>
                </el-form-item>
                <el-form-item label="手机号码" prop="phoneNumber">
                  <span v-if="!isEditing">{{ userInfo.phoneNumber || '未设置' }}</span>
                  <el-input v-else v-model="userInfo.phoneNumber" placeholder="请输入手机号码"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                  <span v-if="!isEditing">{{ userInfo.address || '未设置' }}</span>
                  <el-input v-else v-model="userInfo.address" placeholder="请输入地址"></el-input>
                </el-form-item>
                <el-form-item label="所属学院">
                  <span v-if="!isEditing">{{ userInfo.college || '未设置' }}</span>
                  <el-select v-else v-model="selectedCollegeId" placeholder="请选择学院" @change="handleCollegeChange">
                    <el-option
                      v-for="college in colleges"
                      :key="college.id"
                      :label="college.name"
                      :value="college.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="所属专业">
                  <span v-if="!isEditing">{{ userInfo.major || '未设置' }}</span>
                  <el-select v-else v-model="userInfo.majorId" placeholder="请选择专业" :disabled="!selectedCollegeId">
                    <el-option
                      v-for="major in filteredMajors"
                      :key="major.id"
                      :label="major.name"
                      :value="major.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="注册时间">
                  <span>{{ formatDateTime(userInfo.createTime) }}</span>
                </el-form-item>
                <el-form-item label="用户角色">
                  <span>{{ roleText }}</span>
                  <el-button 
                    v-if="userInfo.role === 'student'" 
                    type="primary" 
                    link 
                    class="apply-teacher-btn"
                    @click="showApplyTeacherDialog"
                  >
                    申请成为教师
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
  
            <!-- 学习记录 -->
          <div v-if="activeTab === 'learning'" class="content-section">
            <h2>学习记录</h2>
              <el-table :data="learningRecords" style="width: 100%">
                <el-table-column prop="courseName" label="课程名称"></el-table-column>
                <el-table-column prop="progress" label="学习进度">
                <template #default="scope">
                    <el-progress :percentage="scope.row.progress"></el-progress>
                  </template>
                </el-table-column>
                <el-table-column prop="lastLearnTime" label="最后学习时间"></el-table-column>
                <el-table-column label="操作">
                <template #default="scope">
                  <el-button type="primary" link @click="continueLearning(scope.row)">
                    继续学习
                  </el-button>
                  </template>
                </el-table-column>
              </el-table>
          </div>
  
          <!-- 课程收藏 -->
          <div v-if="activeTab === 'favorites'" class="content-section">
            <h2>课程收藏</h2>
            <el-row :gutter="20">
              <el-col :span="8" v-for="course in favoriteCourses" :key="course.id">
                <el-card class="course-card" shadow="hover">
                  <img :src="course.coverImage || '/default-course.jpg'" class="course-image">
                  <div class="course-info">
                    <h3>{{ course.title }}</h3>
                    <p>{{ course.description }}</p>
                    <div class="course-footer">
                      <el-button type="primary" link @click="viewCourse(course.id)">
                        查看详情
                      </el-button>
                      <el-button type="danger" link @click="removeFavorite(course.id)">
                        取消收藏
                      </el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
  
            <!-- 我的笔记 -->
          <div v-if="activeTab === 'notes'" class="content-section">
            <h2>我的笔记</h2>
              <div v-for="note in notes" :key="note.id" class="note-item">
                <h4>{{ note.courseName }}</h4>
                <p>{{ note.content }}</p>
              <div class="note-footer">
                <span class="note-time">{{ note.createTime }}</span>
                <el-button type="primary" link @click="editNote(note)">编辑</el-button>
                <el-button type="danger" link @click="deleteNote(note.id)">删除</el-button>
                  </div>
                </div>
              </div>
  
            <!-- 账号安全 -->
          <div v-if="activeTab === 'security'" class="content-section">
            <h2>账号安全</h2>
            <el-card>
              <el-form label-width="100px">
                <el-form-item label="登录密码">
                  <el-button type="primary" link @click="showChangePassword">修改密码</el-button>
                </el-form-item>
                <el-form-item label="手机绑定">
                  <el-button type="primary" link @click="showBindPhone">
                    {{ userInfo.phoneNumber ? '修改手机号' : '绑定手机号' }}
                  </el-button>
                </el-form-item>
                <el-form-item label="邮箱绑定">
                  <el-button type="primary" link @click="showBindEmail">
                    {{ userInfo.email ? '修改邮箱' : '绑定邮箱' }}
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
  
          <!-- 上传课程（仅教师可见） -->
          <div v-if="activeTab === 'upload' && isTeacher" class="content-section">
            <h2>上传课程</h2>
            <el-card>
              <el-form :model="courseForm" label-width="100px">
                <el-form-item label="课程标题">
                  <el-input v-model="courseForm.title" placeholder="请输入课程标题"></el-input>
                </el-form-item>
                <el-form-item label="课程简介">
                  <el-input type="textarea" v-model="courseForm.description" rows="4" placeholder="请输入课程简介"></el-input>
                </el-form-item>
                <el-form-item label="课程封面">
                  <el-upload
                    class="cover-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleCoverSuccess"
                  >
                    <img v-if="courseForm.coverImage" :src="courseForm.coverImage" class="cover">
                    <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
                <el-form-item label="课程分类">
                  <el-select v-model="courseForm.category" placeholder="请选择课程分类">
                    <el-option
                      v-for="item in categories"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitCourse">提交课程</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </el-col>
      </el-row>
  
      <!-- 修改密码对话框 -->
      <el-dialog v-model="passwordDialogVisible" title="修改密码" width="400px">
        <el-form :model="passwordForm" label-width="100px">
          <el-form-item label="原密码">
            <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="passwordForm.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="passwordForm.confirmPassword" type="password"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="passwordDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="changePassword">确认</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 申请成为教师对话框 -->
      <el-dialog v-model="applyTeacherDialogVisible" title="申请成为教师" width="500px">
        <el-form :model="applyForm" label-width="100px">
          <el-form-item label="申请理由" required>
            <el-input
              type="textarea"
              v-model="applyForm.reason"
              :rows="4"
              placeholder="请详细说明您申请成为教师的理由"
            ></el-input>
          </el-form-item>
          <el-form-item label="专业领域" required>
            <el-select v-model="applyForm.expertise" multiple placeholder="请选择您的专业领域">
              <el-option
                v-for="item in expertiseOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="教学经验">
            <el-input
              type="textarea"
              v-model="applyForm.experience"
              :rows="3"
              placeholder="请描述您的教学经验（如有）"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="applyTeacherDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitTeacherApplication">提交申请</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { ref, computed, watch, onMounted } from 'vue'
  import { useStore } from 'vuex'
  import { useRouter } from 'vue-router'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {
    User,
    Reading,
    Star,
    Document,
    Lock,
    Upload,
    Plus
  } from '@element-plus/icons-vue'
  import axios from 'axios'
  import { getProfile, updateProfile } from '@/api/user'
  import { submitTeacherApplication as submitTeacherApplicationApi } from '@/api/teacher'
  import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
  
  export default {
    name: 'Profile',
    components: {
      User,
      Reading,
      Star,
      Document,
      Lock,
      Upload,
      Plus
    },
    setup() {
      const store = useStore()
      const router = useRouter()
      const activeTab = ref('basic')
      const passwordDialogVisible = ref(false)
      const applyTeacherDialogVisible = ref(false)
      const isTeacher = computed(() => store.state.userInfo?.role === 'teacher')
      const roleText = computed(() => {
        const roleMap = {
          'student': '学生',
          'teacher': '教师',
          'admin': '管理员'
        }
        return roleMap[userInfo.value.role] || '未知'
      })
  
      const userInfo = ref({
          username: '',
          email: '',
          phoneNumber: '',
          createTime: '',
          avatar: '',
          gender: 'other',
          role: 'student',
          college: '',
          major: '',
          collegeId: null,
          majorId: null,
          realName: '',
          nickname: '',
          birthday: '',
          address: ''
      })
  
      const learningRecords = ref([
        { courseName: 'Vue 课程', progress: 60, lastLearnTime: '2024-03-14' },
        { courseName: 'Spring Boot', progress: 80, lastLearnTime: '2024-03-13' }
      ])
  
      const notes = ref([
        { id: 1, courseName: 'Vue 课程', content: 'Vue 是一个渐进式框架', createTime: '2024-03-14' }
      ])
  
      const favoriteCourses = ref([
        {
          id: 1,
          title: 'Vue.js 高级教程',
          description: '深入学习 Vue.js 的高级特性',
          coverImage: '/course-1.jpg'
        },
        {
          id: 2,
          title: 'Spring Boot 实战',
          description: '从零开始学习 Spring Boot',
          coverImage: '/course-2.jpg'
        }
      ])
  
      const courseForm = ref({
        title: '',
        description: '',
        coverImage: '',
        category: ''
      })
  
      const categories = [
        { value: 'frontend', label: '前端开发' },
        { value: 'backend', label: '后端开发' },
        { value: 'mobile', label: '移动开发' },
        { value: 'database', label: '数据库' }
      ]
  
      const passwordForm = ref({
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      })
  
      const applyForm = ref({
        reason: '',
        expertise: [],
        experience: ''
      })
  
      const expertiseOptions = computed(() => {
        return majors.value.map(major => ({
          value: major.id,
          label: major.name
        }))
      })
  
      const isEditing = ref(false)
      const originalUserInfo = ref(null)
  
      // 获取所有专业列表
      const majors = ref([])
      const colleges = ref([])
      const selectedCollegeId = ref(null)
      const filteredMajors = computed(() => {
        if (!selectedCollegeId.value) return []
        return majors.value.filter(major => major.collegeId === selectedCollegeId.value)
      })
  
      // 获取所有学院列表
      const fetchColleges = async () => {
        try {
          const response = await axios.get('/api/colleges')
          if (response.data.success) {
            colleges.value = response.data.data
          }
        } catch (error) {
          console.error('获取学院列表失败:', error)
          ElMessage.error('获取学院列表失败')
        }
      }
  
      // 获取所有专业列表
      const fetchMajors = async () => {
        try {
          const response = await axios.get('/api/majors')
          if (response.data.success) {
            majors.value = response.data.data
          }
        } catch (error) {
          console.error('获取专业列表失败:', error)
          ElMessage.error('获取专业列表失败')
        }
      }
  
      // 获取用户信息
      const fetchUserInfo = async () => {
        try {
          const response = await getProfile()
          // 获取用户数据
          const userData = response.data
          console.log('获取到的用户数据:', userData)
          
          // 处理性别值转换
          let genderValue = userData.gender || 'other'
          
          // 更新用户信息
          userInfo.value = {
            ...userInfo.value,
            ...userData,
            // 确保这些字段有默认值
            username: userData.username || '',
            email: userData.email || '',
            phoneNumber: userData.phoneNumber || '',
            avatar: userData.avatar || '',
            gender: genderValue,
            college: userData.college?.name || '',
            major: userData.major?.name || '',
            collegeId: userData.collegeId || null,
            majorId: userData.majorId || null,
            realName: userData.realName || '',
            nickname: userData.nickname || '',
            birthday: userData.birthday || '',
            address: userData.address || ''
          }
          
          // 设置选中的学院ID
          if (userData.collegeId) {
            selectedCollegeId.value = userData.collegeId
          }
          
          console.log('处理后的用户信息:', userInfo.value)
        } catch (error) {
          console.error('获取用户信息失败:', error)
          ElMessage.error('获取用户信息失败')
        }
      }
  
      // 组件挂载时获取用户信息、学院列表和专业列表
      onMounted(async () => {
        try {
          await Promise.all([
            fetchUserInfo(),
            fetchColleges(),
            fetchMajors()
          ])
        } catch (error) {
          console.error('初始化数据失败:', error)
          ElMessage.error('初始化数据失败，请刷新页面重试')
        }
      })
  
      // 监听路由变化，重新获取用户信息
      watch(() => router.currentRoute.value.path, (newPath) => {
        if (newPath === '/profile') {
          fetchUserInfo()
        }
      })
  
      // 监听 store 中的用户信息变化
      watch(() => store.state.userInfo?.id, (newId, oldId) => {
        if (newId !== oldId) {
          fetchUserInfo()
        }
      })
  
      // 监听登录状态变化
      watch(() => store.state.isLoggedIn, (newValue) => {
        if (newValue) {
          fetchUserInfo()
        }
      })
  
      // 监听用户角色变化
      watch(() => store.state.userInfo?.role, (newRole) => {
        if (newRole === 'teacher') {
          ElMessage.success('恭喜！您的教师身份已通过审核')
          userInfo.value.role = newRole
        }
      })
  
      const handleMenuSelect = (index) => {
        activeTab.value = index
      }
  
      const showChangePassword = () => {
        passwordDialogVisible.value = true
      }
  
      const changePassword = async () => {
        if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
          ElMessage.error('两次输入的密码不一致')
          return
        }
  
        try {
          await axios.post('/api/user/change-password', {
            oldPassword: passwordForm.value.oldPassword,
            newPassword: passwordForm.value.newPassword
          })
          ElMessage.success('密码修改成功')
          passwordDialogVisible.value = false
        } catch (error) {
          ElMessage.error('密码修改失败：' + error.response?.data?.message || '未知错误')
        }
      }
  
      const showBindPhone = () => {
        // 实现手机绑定逻辑
      }
  
      const showBindEmail = () => {
        // 实现邮箱绑定逻辑
      }
  
      const continueLearning = (course) => {
        router.push(`/course/${course.id}`)
      }
  
      const viewCourse = (courseId) => {
        router.push(`/course/${courseId}`)
      }
  
      const removeFavorite = async (courseId) => {
        try {
          await axios.delete(`/api/user/favorites/${courseId}`)
          favoriteCourses.value = favoriteCourses.value.filter(course => course.id !== courseId)
          ElMessage.success('已取消收藏')
        } catch (error) {
          ElMessage.error('取消收藏失败')
        }
      }
  
      const editNote = (note) => {
        // 实现笔记编辑逻辑
      }
  
      const deleteNote = async (noteId) => {
        try {
          await axios.delete(`/api/user/notes/${noteId}`)
          notes.value = notes.value.filter(note => note.id !== noteId)
          ElMessage.success('笔记已删除')
        } catch (error) {
          ElMessage.error('删除笔记失败')
        }
      }
  
      const handleCoverSuccess = (response) => {
        courseForm.value.coverImage = response.url
      }
  
      const submitCourse = async () => {
        try {
          await axios.post('/api/course', courseForm.value)
          ElMessage.success('课程提交成功')
          courseForm.value = {
            title: '',
            description: '',
            coverImage: '',
            category: ''
          }
        } catch (error) {
          ElMessage.error('课程提交失败：' + error.response?.data?.message || '未知错误')
        }
      }
  
      const showApplyTeacherDialog = () => {
        ElMessageBox.confirm(
          '申请成为教师后，需要管理员审核，是否继续？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          applyTeacherDialogVisible.value = true
        }).catch(() => {})
      }
  
      const submitTeacherApplication = async () => {
        if (!applyForm.value.reason || !applyForm.value.expertise.length) {
          ElMessage.warning('请填写申请理由和专业领域')
          return
        }
  
        try {
          const response = await submitTeacherApplicationApi({
            userId: store.state.userInfo.id,
            reason: applyForm.value.reason,
            expertise: applyForm.value.expertise.join(','),
            experience: applyForm.value.experience
          })
          
          if (response.success) {
            ElMessage.success('申请已提交，请等待审核')
            applyTeacherDialogVisible.value = false
            applyForm.value = {
              reason: '',
              expertise: [],
              experience: ''
            }
          } else {
            ElMessage.error(response.message || '申请提交失败')
          }
        } catch (error) {
          console.error('申请提交失败:', error)
          ElMessage.error('申请提交失败：' + (error.response?.data?.message || '未知错误'))
        }
      }
  
      const formatDateTime = (dateTime) => {
        if (!dateTime) return '暂无数据'
        try {
          // 尝试解析ISO格式的日期字符串
          const date = new Date(dateTime)
          if (isNaN(date.getTime())) {
            return '暂无数据'
          }
          return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
          })
        } catch (error) {
          console.error('日期格式化错误:', error)
          return '暂无数据'
        }
      }
      
      const formatDate = (date) => {
        if (!date) return '暂无数据'
        try {
          // 尝试解析ISO格式的日期字符串
          const dateObj = new Date(date)
          if (isNaN(dateObj.getTime())) {
            return '暂无数据'
          }
          return dateObj.toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
          })
        } catch (error) {
          console.error('日期格式化错误:', error)
          return '暂无数据'
        }
      }
  
      const handleAvatarSuccess = (response) => {
        if (response.success) {
          userInfo.value.avatar = response.data;
          ElMessage.success('头像上传成功');
          // 更新用户信息到后端
          updateUserProfile();
        } else {
          ElMessage.error(response.message || '头像上传失败');
        }
      }

      const handleAvatarError = (error) => {
        ElMessage.error('头像上传失败：' + (error.message || '未知错误'));
      }

      const beforeAvatarUpload = (file) => {
        const isImage = file.type.startsWith('image/');
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isImage) {
          ElMessage.error('上传头像图片只能是图片格式!');
          return false;
        }
        if (!isLt2M) {
          ElMessage.error('上传头像图片大小不能超过 2MB!');
          return false;
        }
        return true;
      }
  
      const updateUserProfile = async () => {
        try {
          await axios.put('/api/user/profile', {
            username: userInfo.value.username,
            email: userInfo.value.email,
            phoneNumber: userInfo.value.phoneNumber,
            avatar: userInfo.value.avatar,
            gender: userInfo.value.gender === 'male' ? 0 : userInfo.value.gender === 'female' ? 1 : 2,
            majorId: userInfo.value.majorId,
            collegeId: userInfo.value.collegeId,
            realName: userInfo.value.realName || '',
            nickname: userInfo.value.nickname || '',
            birthday: userInfo.value.birthday || null,
            address: userInfo.value.address || ''
          })
          ElMessage.success('个人信息更新成功')
        } catch (error) {
          ElMessage.error('个人信息更新失败：' + error.response?.data?.message || '未知错误')
        }
      }
  
      const startEditing = () => {
        // 深拷贝当前用户信息作为原始数据
        originalUserInfo.value = JSON.parse(JSON.stringify(userInfo.value))
        // 设置编辑模式
        isEditing.value = true
        // 确保表单中显示原始数据
        userInfo.value = JSON.parse(JSON.stringify(originalUserInfo.value))
        
        // 设置选中的学院ID
        selectedCollegeId.value = userInfo.value.collegeId
      }
  
      const cancelEditing = () => {
        userInfo.value = JSON.parse(JSON.stringify(originalUserInfo.value))
        isEditing.value = false
      }
  
      const rules = {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phoneNumber: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        birthday: [
          { 
            validator: (rule, value, callback) => {
              if (!value) {
                callback()
              } else {
                const selectedDate = new Date(value)
                const today = new Date()
                today.setHours(0, 0, 0, 0)
                if (selectedDate > today) {
                  callback(new Error('生日不能晚于今天'))
                } else {
                  callback()
                }
              }
            },
            trigger: 'change'
          }
        ]
      }

      const userForm = ref(null)

      const saveProfile = async () => {
        try {
          // 表单验证
          await userForm.value.validate()
          
          // 校验生日
          if (userInfo.value.birthday) {
            const selectedDate = new Date(userInfo.value.birthday)
            const today = new Date()
            today.setHours(0, 0, 0, 0)
            if (selectedDate > today) {
              ElMessage.error('生日不能晚于今天')
              return
            }
          }
          
          // 准备要发送的数据
          const profileData = {
            username: userInfo.value.username,
            email: userInfo.value.email,
            phoneNumber: userInfo.value.phoneNumber,
            avatar: userInfo.value.avatar,
            gender: userInfo.value.gender === 'male' ? 0 : userInfo.value.gender === 'female' ? 1 : 2,
            majorId: userInfo.value.majorId,
            collegeId: userInfo.value.collegeId,
            realName: userInfo.value.realName || '',
            nickname: userInfo.value.nickname || '',
            birthday: userInfo.value.birthday || null,
            address: userInfo.value.address || ''
          }
          
          console.log('发送到后端的数据:', profileData)
          
          const response = await axios.put('/api/user/profile', profileData)
          if (response.data.success) {
            ElMessage.success('个人信息更新成功')
            isEditing.value = false
            // 更新成功后重新获取用户信息
            await fetchUserInfo()
          } else {
            ElMessage.error(response.data.message || '更新失败')
          }
        } catch (error) {
          console.error('更新用户信息失败:', error)
          if (error.response?.data?.message) {
            ElMessage.error(error.response.data.message)
          } else if (error.message) {
            ElMessage.error(error.message)
          } else {
            ElMessage.error('个人信息更新失败')
          }
        }
      }
  
      // 处理学院选择变化
      const handleCollegeChange = (collegeId) => {
        // 当学院变化时，清空专业选择
        userInfo.value.majorId = null
      }
  
      const dateShortcuts = []  // 移除快捷选项
  
      return {
        activeTab,
        userInfo,
        learningRecords,
        notes,
        favoriteCourses,
        courseForm,
        categories,
        passwordDialogVisible,
        passwordForm,
        applyTeacherDialogVisible,
        applyForm,
        expertiseOptions,
        isTeacher,
        roleText,
        handleMenuSelect,
        showChangePassword,
        changePassword,
        showBindPhone,
        showBindEmail,
        continueLearning,
        viewCourse,
        removeFavorite,
        editNote,
        deleteNote,
        handleCoverSuccess,
        submitCourse,
        showApplyTeacherDialog,
        submitTeacherApplication,
        formatDateTime,
        formatDate,
        handleAvatarSuccess,
        handleAvatarError,
        beforeAvatarUpload,
        updateUserProfile,
        isEditing,
        startEditing,
        cancelEditing,
        saveProfile,
        selectedCollegeId,
        colleges,
        filteredMajors,
        handleCollegeChange,
        userForm,
        rules,
        dateShortcuts,
        zhCn
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .profile-container {
    padding: 20px;
    max-width: 1200px;
    margin: 60px auto 0;
  }
  
  .el-menu-vertical {
    width: 100%;
    min-height: 300px;
    border-radius: 8px;
    border-right: 1px solid #ebeef5;
  }
  
  .content-section {
    h2 {
      margin-bottom: 20px;
      font-size: 20px;
      color: #333;
    }
  }
  
  .course-card {
    margin-bottom: 20px;
    transition: all 0.3s ease;
  
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 16px rgba(0,0,0,0.1);
    }
  
    .course-image {
      width: 100%;
      height: 160px;
      object-fit: cover;
    }
  
    .course-info {
      padding: 15px;
  
      h3 {
        margin: 0 0 10px;
        font-size: 16px;
      }
  
      p {
        color: #666;
        margin-bottom: 15px;
        font-size: 14px;
        height: 40px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
  
      .course-footer {
        display: flex;
        justify-content: space-between;
      }
    }
  }
  
  .note-item {
    padding: 15px;
    margin-bottom: 15px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
  
    h4 {
      margin: 0 0 10px;
      color: #333;
    }
  
    p {
      color: #666;
      margin-bottom: 10px;
    }
  
    .note-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: #999;
      font-size: 14px;
    }
  }
  
  .cover-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;
  
      &:hover {
        border-color: #409EFF;
      }
    }
  }
  
  .cover-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
    line-height: 178px;
  }
  
  .cover {
    width: 178px;
    height: 178px;
    display: block;
    object-fit: cover;
  }
  
  .apply-teacher-btn {
    margin-left: 20px;
  }
  
  .avatar-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;
  
      &:hover {
        border-color: #409EFF;
      }
    }
  }
  
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
  }
  
  .avatar {
    width: 100px;
    height: 100px;
    display: block;
    object-fit: cover;
    border-radius: 50%;
  }
  
  .profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  
    h3 {
      margin: 0;
      font-size: 18px;
      color: #333;
    }
  }
  
  .edit-actions {
    display: flex;
    gap: 10px;
  }
  </style>