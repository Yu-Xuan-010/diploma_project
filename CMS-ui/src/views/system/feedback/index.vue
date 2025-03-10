<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程" prop="courseId">
        <el-select v-model="queryParams.courseId" placeholder="请选择课程" clearable size="small" @change="handleQuery">
          <el-option
            v-for="item in courseOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="评分" prop="rating">
        <el-select v-model="queryParams.rating" placeholder="请选择评分" clearable size="small" @change="handleQuery">
          <el-option label="1星" :value="1" />
          <el-option label="2星" :value="2" />
          <el-option label="3星" :value="3" />
          <el-option label="4星" :value="4" />
          <el-option label="5星" :value="5" />
        </el-select>
      </el-form-item>
      <el-form-item label="提交时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:feedback:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:feedback:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="feedbackList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程" align="center" prop="courseName" />
      <el-table-column label="评分" align="center" prop="rating">
        <template slot-scope="scope">
          <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
        </template>
      </el-table-column>
      <el-table-column label="反馈内容" align="center" prop="comment" :show-overflow-tooltip="true" />
      <el-table-column label="提交时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:feedback:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:feedback:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-dot-round"
            @click="handleReply(scope.row)"
            v-hasPermi="['system:feedback:reply']"
          >回复</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改反馈对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程">
            <el-option
              v-for="item in courseOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="form.rating" show-score text-color="#ff9900"></el-rate>
        </el-form-item>
        <el-form-item label="反馈内容" prop="comment">
          <el-input v-model="form.comment" type="textarea" placeholder="请输入反馈内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 回复反馈对话框 -->
    <el-dialog title="回复反馈" :visible.sync="replyOpen" width="500px" append-to-body>
      <el-form ref="replyForm" :model="replyForm" :rules="replyRules" label-width="80px">
        <el-form-item label="回复内容" prop="replyText">
          <el-input v-model="replyForm.replyText" type="textarea" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReply">确 定</el-button>
        <el-button @click="replyOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFeedback, getFeedback, delFeedback, addFeedback, updateFeedback, replyFeedback } from "@/api/system/feedback";
import { listCourse } from "@/api/system/course";

export default {
  name: "Feedback",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 反馈表格数据
      feedbackList: [],
      // 课程选项
      courseOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示回复弹出层
      replyOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        rating: null
      },
      // 表单参数
      form: {},
      // 回复表单参数
      replyForm: {
        feedbackId: null,
        replyText: ""
      },
      // 表单校验
      rules: {
        courseId: [
          { required: true, message: "请选择课程", trigger: "change" }
        ],
        rating: [
          { required: true, message: "请选择评分", trigger: "change" }
        ],
        comment: [
          { required: true, message: "请输入反馈内容", trigger: "blur" }
        ]
      },
      // 回复表单校验
      replyRules: {
        replyText: [
          { required: true, message: "请输入回复内容", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCourseOptions();
  },
  methods: {
    /** 查询反馈列表 */
    getList() {
      this.loading = true;
      listFeedback(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.feedbackList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取课程选项 */
    getCourseOptions() {
      listCourse().then(response => {
        this.courseOptions = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        courseId: null,
        rating: 5,
        comment: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加反馈";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFeedback(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改反馈";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFeedback(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFeedback(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除反馈编号为"' + ids + '"的数据项？').then(function() {
        return delFeedback(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 回复按钮操作 */
    handleReply(row) {
      this.replyForm.feedbackId = row.id;
      this.replyForm.replyText = "";
      this.replyOpen = true;
    },
    /** 提交回复 */
    submitReply() {
      this.$refs["replyForm"].validate(valid => {
        if (valid) {
          replyFeedback(this.replyForm.feedbackId, this.replyForm.replyText).then(response => {
            this.$modal.msgSuccess("回复成功");
            this.replyOpen = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>
