<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 平台基本信息 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平台基本信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleSaveSettings">保存设置</el-button>
          </div>
          <el-form ref="settingsForm" :model="settings" :rules="settingsRules" label-width="100px">
            <el-form-item label="网站名称" prop="siteName">
              <el-input v-model="settings.siteName" placeholder="请输入网站名称"/>
            </el-form-item>
            <el-form-item label="网站Logo" prop="siteLogo">
              <el-input v-model="settings.siteLogo" placeholder="请输入Logo URL"/>
            </el-form-item>
            <el-form-item label="网站图标" prop="siteFavicon">
              <el-input v-model="settings.siteFavicon" placeholder="请输入图标URL"/>
            </el-form-item>
            <el-form-item label="网站描述" prop="siteDescription">
              <el-input type="textarea" v-model="settings.siteDescription" placeholder="请输入网站描述"/>
            </el-form-item>
            <el-form-item label="关键词" prop="siteKeywords">
              <el-input v-model="settings.siteKeywords" placeholder="请输入关键词，用逗号分隔"/>
            </el-form-item>
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="settings.contactEmail" placeholder="请输入联系邮箱"/>
            </el-form-item>
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="settings.contactPhone" placeholder="请输入联系电话"/>
            </el-form-item>
            <el-form-item label="联系地址" prop="contactAddress">
              <el-input type="textarea" v-model="settings.contactAddress" placeholder="请输入联系地址"/>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 通知公告管理 -->
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>通知公告管理</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddAnnouncement">新增公告</el-button>
          </div>
          <el-table v-loading="loading" :data="announcementList" style="width: 100%">
            <el-table-column prop="title" label="标题" show-overflow-tooltip/>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
                  {{ scope.row.status === '1' ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleEditAnnouncement(scope.row)">修改</el-button>
                <el-button size="mini" type="text" @click="handleViewAnnouncement(scope.row)">查看</el-button>
                <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDeleteAnnouncement(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 公告对话框 -->
    <el-dialog :title="announcementDialogTitle" :visible.sync="announcementDialogVisible" width="500px" append-to-body>
      <el-form ref="announcementForm" :model="announcement" :rules="announcementRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="announcement.title" placeholder="请输入公告标题"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="announcement.content" :rows="6" placeholder="请输入公告内容"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="announcement.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="announcementDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAnnouncement">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看公告对话框 -->
    <el-dialog title="查看公告" :visible.sync="viewDialogVisible" width="500px" append-to-body>
      <div class="announcement-view">
        <h3>{{ viewAnnouncement.title }}</h3>
        <div class="announcement-content">{{ viewAnnouncement.content }}</div>
        <div class="announcement-info">
          <span>状态：{{ viewAnnouncement.status === '1' ? '启用' : '禁用' }}</span>
          <span>创建时间：{{ parseTime(viewAnnouncement.createdAt) }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSystemSettings, updateSystemSettings } from "@/api/system/settings";
import { listAnnouncements, getAnnouncement, addAnnouncement, updateAnnouncement, deleteAnnouncement } from "@/api/system/settings";
import { parseTime } from '@/utils/ruoyi';

export default {
  name: "SystemSettings",
  data() {
    return {
      // 加载状态
      loading: false,
      // 系统设置
      settings: {},
      // 系统设置校验规则
      settingsRules: {
        siteName: [{ required: true, message: "网站名称不能为空", trigger: "blur" }],
        contactEmail: [{ type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }]
      },
      // 公告列表
      announcementList: [],
      // 公告对话框标题
      announcementDialogTitle: "",
      // 公告对话框显示状态
      announcementDialogVisible: false,
      // 查看对话框显示状态
      viewDialogVisible: false,
      // 公告表单
      announcement: {
        id: undefined,
        title: "",
        content: "",
        status: "1"
      },
      // 查看的公告
      viewAnnouncement: {},
      // 公告表单校验规则
      announcementRules: {
        title: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
        content: [{ required: true, message: "公告内容不能为空", trigger: "blur" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }]
      }
    };
  },
  created() {
    this.getSettings();
    this.getAnnouncementList();
  },
  methods: {
    parseTime,
    /** 获取系统设置 */
    getSettings() {
      getSystemSettings().then(response => {
        this.settings = response.data;
      });
    },
    /** 保存系统设置 */
    handleSaveSettings() {
      this.$refs["settingsForm"].validate(valid => {
        if (valid) {
          updateSystemSettings(this.settings).then(response => {
            this.$modal.msgSuccess("保存成功");
          });
        }
      });
    },
    /** 获取公告列表 */
    getAnnouncementList() {
      this.loading = true;
      listAnnouncements().then(response => {
        this.announcementList = response.data;
        this.loading = false;
      });
    },
    /** 新增公告按钮操作 */
    handleAddAnnouncement() {
      this.announcement = {
        title: "",
        content: "",
        status: "1"
      };
      this.announcementDialogTitle = "新增公告";
      this.announcementDialogVisible = true;
    },
    /** 修改公告按钮操作 */
    handleEditAnnouncement(row) {
      this.announcement = { ...row };
      this.announcementDialogTitle = "修改公告";
      this.announcementDialogVisible = true;
    },
    /** 查看公告按钮操作 */
    handleViewAnnouncement(row) {
      this.viewAnnouncement = { ...row };
      this.viewDialogVisible = true;
    },
    /** 删除公告按钮操作 */
    handleDeleteAnnouncement(row) {
      this.$modal.confirm('是否确认删除该公告？').then(() => {
        deleteAnnouncement(row.id).then(() => {
          this.getAnnouncementList();
          this.$modal.msgSuccess("删除成功");
        });
      });
    },
    /** 提交公告表单 */
    submitAnnouncement() {
      this.$refs["announcementForm"].validate(valid => {
        if (valid) {
          if (this.announcement.id) {
            updateAnnouncement(this.announcement).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.announcementDialogVisible = false;
              this.getAnnouncementList();
            });
          } else {
            addAnnouncement(this.announcement).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.announcementDialogVisible = false;
              this.getAnnouncementList();
            });
          }
        }
      });
    }
  }
};
</script>

<style scoped>
.announcement-view {
  padding: 20px;
}
.announcement-view h3 {
  margin-bottom: 15px;
  color: #303133;
}
.announcement-content {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #606266;
}
.announcement-info {
  color: #909399;
  font-size: 13px;
}
.announcement-info span {
  margin-right: 15px;
}
</style> 