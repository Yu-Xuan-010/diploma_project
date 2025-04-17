<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程种类" prop="categoryId">
        <el-input
          v-model="queryParams.categoryId"
          placeholder="请输入课程种类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="教师ID" prop="teacherId">
        <el-input
          v-model="queryParams.teacherId"
          placeholder="请输入教师ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择审核状态"
          clearable
          @change="handleQuery"
        >
          <el-option label="待审核" value="pending"></el-option>
          <el-option label="已审核" value="approved"></el-option>
          <el-option label="审核未通过" value="rejected"></el-option>
        </el-select>
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
          v-hasPermi="['system:course:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:course:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:course:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id"/>
      <el-table-column label="课程名称" align="center" prop="name"/>
      <el-table-column label="课程描述" align="center" prop="description"/>
      <el-table-column label="课程种类" align="center" prop="categoryId"/>
      <el-table-column label="评分" align="center" prop="averageRating"/>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'approved' ? 'success' : scope.row.status === 'rejected' ? 'danger' : 'warning'">
            {{ scope.row.status === 'approved' ? '已通过' : scope.row.status === 'rejected' ? '已驳回' : '待审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="教师ID" align="center" prop="teacherId"/>
      <el-table-column label="学习人数" align="center" prop="studentCount"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:course:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:course:remove']"
          >删除
          </el-button>
          <el-button
            v-if="scope.row.status === 'pending'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleReview(scope.row, 'approved')"
            v-hasPermi="['system:course:review']"
          >通过
          </el-button>
          <el-button
            v-if="scope.row.status === 'pending'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReview(scope.row, 'rejected')"
            v-hasPermi="['system:course:review']"
          >驳回
          </el-button>
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

    <!-- 添加或修改课程列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入课程名称"/>
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="课程种类" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入课程种类"/>
        </el-form-item>
        <el-form-item label="封面" prop="image">
          <image-upload v-model="form.image"/>
        </el-form-item>
        <el-form-item label="教师ID" prop="teacherId">
          <el-input v-model="form.teacherId" placeholder="请输入教师ID"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog :title="reviewTitle" :visible.sync="reviewOpen" width="500px" append-to-body>
      <el-form ref="reviewForm" :model="reviewForm" :rules="reviewRules" label-width="80px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="reviewForm.status">
            <el-radio label="approved">通过</el-radio>
            <el-radio label="rejected">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="驳回原因" prop="rejectReason" v-if="reviewForm.status === 'rejected'">
          <el-input
            type="textarea"
            v-model="reviewForm.rejectReason"
            placeholder="请输入驳回原因"
            :rows="3"
          />
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
import {listCourse, getCourse, delCourse, addCourse, updateCourse} from "@/api/system/course";
import request from "@/utils/request";

export default {
  name: "Course",
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
      // 课程列表表格数据
      courseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        description: null,
        categoryId: null,
        teacherId: null,
        status: null  // 添加 status 字段
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "课程名称不能为空", trigger: "blur"}
        ],
        teacherId: [
          {required: true, message: "教师ID不能为空", trigger: "blur"}
        ]
      },
      // 审核对话框
      reviewOpen: false,
      reviewTitle: "",
      reviewForm: {
        id: null,
        status: "approved",
        rejectReason: ""
      },
      reviewRules: {
        status: [
          { required: true, message: "请选择审核结果", trigger: "change" }
        ],
        rejectReason: [
          { required: true, message: "请输入驳回原因", trigger: "blur", validator: (rule, value, callback) => {
            if (this.reviewForm.status === "rejected" && !value) {
              callback(new Error("请输入驳回原因"));
            } else {
              callback();
            }
          }}
        ]
      }
    };
  },
  created() {
    this.getList();  // 创建时获取课程列表
  },
  methods: {
    /** 查询课程列表 */
    getList() {
      this.loading = true;
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        name: null,
        description: null,
        categoryId: null,
        image: null,
        createdAt: null,
        updatedAt: null,
        averageRating: null,
        status: null,  // 重置状态
        teacherId: null,
        studentCount: null
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;  // 重置到第一页
      // 调用 getList 方法获取数据
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加课程列表";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCourse(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改课程列表";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCourse(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCourse(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除课程列表编号为"' + ids + '"的数据项？').then(function () {
        return delCourse(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/course/export', {
        ...this.queryParams
      }, `course_${new Date().getTime()}.xlsx`);
    },

    /** 审核按钮操作 */
    handleReview(row, status) {
      this.reviewForm = {
        id: row.id,
        status: status,
        rejectReason: ""
      };
      this.reviewTitle = status === "approved" ? "审核通过" : "审核驳回";
      this.reviewOpen = true;
    },

    /** 取消审核 */
    cancelReview() {
      this.reviewOpen = false;
      this.resetReviewForm();
    },

    /** 重置审核表单 */
    resetReviewForm() {
      this.reviewForm = {
        id: null,
        status: "approved",
        rejectReason: ""
      };
      this.resetForm("reviewForm");
    },

    /** 提交审核 */
    submitReview() {
      this.$refs["reviewForm"].validate(valid => {
        if (valid) {
          const data = {
            status: this.reviewForm.status,
            rejectReason: this.reviewForm.status === "rejected" ? this.reviewForm.rejectReason : null
          };
          request({
            url: `/api/courses/${this.reviewForm.id}/status`,
            method: 'put',
            data: data
          }).then(response => {
            this.$modal.msgSuccess("审核成功");
            this.reviewOpen = false;
            this.getList();
          });
        }
      });
    }
  }
}
</script>
