<template>
  <div>
    <el-card>
      <div style="margin-bottom: 10px;">
        <el-input v-model="queryParams.keyword" placeholder="搜索评论..." clearable style="width: 300px;" />
        <el-button type="primary" @click="fetchComments">搜索</el-button>
      </div>
      <el-table :data="comments" border style="width: 100%">
        <el-table-column prop="id" label="评论 ID" width="80" />
        <el-table-column prop="courseName" label="课程名称" width="150" />
        <el-table-column prop="userName" label="用户昵称" width="120" />
        <el-table-column prop="content" label="评论内容" />
        <el-table-column prop="createdAt" label="评论时间" width="180" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="warning">待审核</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="approveComment(row.id)" v-if="row.status === 0">通过</el-button>
            <el-button size="small" type="danger" @click="deleteComment(row.id)">删除</el-button>
            <el-button size="small" type="primary" @click="viewReplies(row)">查看回复</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="queryParams.page"
        :page-size="queryParams.pageSize"
        layout="prev, pager, next"
        :total="total">
      </el-pagination>

      <!-- 回复列表对话框 -->
      <el-dialog
        :title="'评论回复 - ' + (currentComment.content || '')"
        :visible.sync="replyDialogVisible"
        width="600px"
        @close="handleDialogClose">
        <div v-if="currentComment.content" class="comment-content">
          <p><strong>原评论内容：</strong>{{ currentComment.content }}</p>
          <p><strong>评论人：</strong>{{ currentComment.userName }}</p>
          <el-divider></el-divider>
        </div>
        <el-table :data="replies" border v-loading="replyLoading">
          <el-table-column prop="id" label="回复 ID" width="80" />
          <el-table-column prop="userName" label="回复人" width="120">
            <template #default="{ row }">
              <span>{{ row.userName || '未知用户' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="replyText" label="回复内容" />
          <el-table-column prop="createdAt" label="回复时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" type="danger" @click="deleteReply(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
          <el-button @click="replyDialogVisible = false">关闭</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import * as CCM from "@/api/system/CCM";

export default {
  name: "CourseComment",
  data() {
    return {
      queryParams: {
        keyword: "",
        page: 1,
        pageSize: 10
      },
      comments: [],
      total: 0,
      replyDialogVisible: false,
      replies: [],
      currentComment: {},
      replyLoading: false
    };
  },
  methods: {
    async fetchComments() {
      try {
        const res = await CCM.list(this.queryParams);
        if (res && res.rows) {
          this.comments = res.rows;
          this.total = res.total || 0;
        }
      } catch (error) {
        console.error("获取评论列表失败：", error);
        this.$message.error("获取评论列表失败");
      }
    },
    handleCurrentChange(val) {
      this.queryParams.page = val;
      this.fetchComments();
    },
    async approveComment(id) {
      try {
        await CCM.changeStatus({ id, status: 1 });
        this.$message.success("审核通过成功");
        this.fetchComments();
      } catch (error) {
        this.$message.error("审核失败");
      }
    },
    async deleteComment(id) {
      try {
        await this.$confirm("确认要删除该评论吗？", "提示", {
          type: "warning"
        });
        await CCM.remove(id);
        this.$message.success("删除成功");
        this.fetchComments();
      } catch (error) {
        if (error !== "cancel") {
          this.$message.error("删除失败");
        }
      }
    },
    async viewReplies(comment) {
      this.currentComment = comment;
      this.replyDialogVisible = true;
      this.replyLoading = true;
      try {
        const res = await CCM.replyList(comment.id);
        if (res && res.rows) {
          this.replies = res.rows;
        } else {
          this.replies = [];
        }
      } catch (error) {
        console.error("获取回复列表失败：", error);
        this.$message.error("获取回复列表失败");
      } finally {
        this.replyLoading = false;
      }
    },
    async deleteReply(id) {
      try {
        await this.$confirm("确认要删除该回复吗？", "提示", {
          type: "warning"
        });
        await CCM.removeReply(id);
        this.$message.success("删除成功");
        this.viewReplies(this.currentComment);
      } catch (error) {
        if (error !== "cancel") {
          this.$message.error("删除失败");
        }
      }
    },
    handleDialogClose() {
      this.replies = [];
      this.currentComment = {};
    }
  },
  created() {
    this.fetchComments();
  }
};
</script>

<style scoped>
.comment-content {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.comment-content p {
  margin: 5px 0;
}
</style>
