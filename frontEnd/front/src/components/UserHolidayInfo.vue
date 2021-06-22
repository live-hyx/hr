<<<<<<< HEAD
<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>请休假管理</el-breadcrumb-item>
      <el-breadcrumb-item>个人假期查询</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-col :span="10">
          <div class="grid-content bg-purple-dark">
            <el-col :span="10" class="addBtn">
              <el-button type="primary" @click="showAddHolidayDialog()">请假申请</el-button>
            </el-col>
            <el-col :span="5" class="addBtn">
              <el-button type="info" @click="searchHoliday()">刷新</el-button>
            </el-col>
          </div>
        </el-col>
      </el-row>
      <el-table :data="holidayList" border style="width: 100%">
        <el-table-column
          prop="bdate"
          label="请假开始日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="edate"
          label="请假结束日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="apply_date"
          label="申请日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="notes"
          label="请假理由"
          width="200">
        </el-table-column>
        <el-table-column
          prop="date_num"
          label="请假天数"
          width="100">
        </el-table-column>
        <el-table-column
          prop=""
          label="操作" >
          <template slot-scope="scope">
            <el-button type="success" @click="showModifyDialog(scope.row)" size="mini">发起修改流程</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 新增请假 -->
    <el-dialog
      title="新增请假" :visible.sync="addHolidayDialogVisable" width="35%" @close="resetDialog">
      <el-form ref="addFormRef" :model="addHolidayForm" label-width="110px" :rules="addHolidayFormRules">
        <el-form-item label="请假起止日期" prop="dateRange">
          <el-date-picker
            v-model="addHolidayForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="notes">
          <el-input v-model="addHolidayForm.notes"></el-input>
        </el-form-item>
        <el-form-item >
              <span class="button_span">
                <el-button type="primary" @click="addHoliday()" size="small">添加</el-button>
                <el-button type="info" @click="resetDialog ()" size="small">重置</el-button>
              </span>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 修改请假 -->
    <el-dialog
      title="修改请假" :visible.sync="modifyHolidayDialogVisable" width="35%" @close="resetModifyDialog">
      <el-form ref="modifyFormRef" :model="modifyHolidayForm" label-width="110px" :rules="modifyHolidayFormRules">
       <el-form-item label="请假起止日期" prop="dateRange">
          <el-date-picker
            v-model="modifyHolidayForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="notes">
          <el-input v-model="modifyHolidayForm.notes"></el-input>
        </el-form-item>
        <el-form-item >
              <span class="button_span">
                <el-button type="primary" @click="modifyHoliday()" size="small">修改</el-button>
                <el-button type="info" @click="resetModifyDialog ()" size="small">重置</el-button>
              </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data () {
    return {
      holidayList: [],
      addHolidayDialogVisable: false,
      datePickerOptions: {
        disabledDate: (time) => {
          const nowDate = new Date()
          const oneDay = 1000 * 60 * 60 * 24
          const oneYearLater = new Date(nowDate.getTime() + (oneDay * 365))
          return time.getTime() < nowDate || time.getTime() > oneYearLater
        }
      },
      addHolidayForm: {
        dateRange: '',
        apply_date: '',
        date_num: '',
        notes: '',
        bdate: '',
        edate: ''
      },
      addHolidayFormRules: {
        dateRange: [{ required: true, message: '日期不能为空', trigger: 'blur' }],
        notes: [{ required: true, message: '请假理由不能为空', trigger: 'blur' }]
      },
      modifyHolidayDialogVisable: false,
      modifyHolidayForm: {
        pre_id: '',
        apply_date: '',
        date_num: '',
        notes: '',
        bdate: '',
        edate: '',
        dateRange: ''
      },
      modifyHolidayFormRules: {
        dateRange: [{ required: true, message: '日期不能为空', trigger: 'blur' }],
        notes: [{ required: true, message: '请假理由不能为空', trigger: 'blur' }]
      }
    }
  },
  created () {
    this.searchHoliday()
  },
  methods: {
    // 查询个人假期
    searchHoliday () {
      this.$http.get('/user/get_user_holiday')
        .then(res => {
          if (res.data.state === 200) {
            console.log(res.data.data)
            this.holidayList = res.data.data
          } else {
            this.$message.error('请获取本人假期失败')
          }
        })
        .catch(_error => {
          this.$message.error('获取假期失败')
        })
    },
    // 显示发起请假流程对话框
    showAddHolidayDialog () {
      this.addHolidayDialogVisable = true
    },
    // 重置发起请假流程对话框
    resetDialog () {
      this.$refs.addFormRef.resetFields()
    },
    // 发起请假流程
    addHoliday () {
      // 先进行表单验证
      this.$refs.addFormRef.validate(valid => {
        console.log(this.addHolidayForm)
        if (valid === true) {
          this.addHolidayForm.bdate = this.addHolidayForm.dateRange[0]
          this.addHolidayForm.edate = this.addHolidayForm.dateRange[1]
          console.log(this.addHolidayForm)
          // 访问后端接口，发起请假流程
          this.$http.post('/user/add_holiday', this.addHolidayForm)
            .then(res => {
              if (res.data.state === 200) {
                this.addHolidayDialogVisable = false
                this.$message.success('发起新增请假流程成功')
              } else {
                this.$message.error('发起新增请假流程失败')
              }
            })
            .catch(_error => {
              this.$message.error('发起新增请假流程失败' + _error)
            })
        }
      })
    },
    // 显示修改请假对话框
    showModifyDialog (row) {
      console.log(row)
      this.modifyHolidayDialogVisable = true
      this.modifyHolidayForm.pre_id = row.id
      this.modifyHolidayForm.dateRange = row.dateRange
      this.modifyHolidayForm.bdate = row.bdate
      this.modifyHolidayForm.edate = row.edate
      this.modifyHolidayForm.notes = row.notes
      // console.log(row)
    },
    // 重置修改请假对话框
    resetModifyDialog () {
      this.$refs.modifyFormRef.resetFields()
      this.searchHoliday()
    },
    // 修改请假
    modifyHoliday () {
      // 先进行表单验证
      this.$refs.modifyFormRef.validate(valid => {
        if (valid === true) {
          this.modifyHolidayForm.bdate = this.modifyHolidayForm.dateRange[0]
          this.modifyHolidayForm.edate = this.modifyHolidayForm.dateRange[1]
          this.$http.post('/user/modify_user_holiday', this.modifyHolidayForm)
            .then(res => {
              if (res.data.state === 200) {
                this.modifyHolidayDialogVisable = false
                this.$message.success('发起修改请假流程成功')
                console.log('123' + this.modifyHolidayForm.bdate)
                console.log(this.modifyHolidayForm.edate)
              } else {
                this.$message.error('发起修改请假流程失败')
              }
            })
            .catch(_error => {
              this.$message.error('发起修改请假流程失败')
            })
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.el-table{
  margin-top: 10px;
}
.el-pagination{
  margin-top: 10px;
}
</style>
=======
<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>请休假管理</el-breadcrumb-item>
      <el-breadcrumb-item>个人假期查询</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-col :span="10">
          <div class="grid-content bg-purple-dark">
            <el-col :span="10" class="addBtn">
              <el-button type="primary" @click="showAddHolidayDialog()">请假申请</el-button>
            </el-col>
            <el-col :span="5" class="addBtn">
              <el-button type="info" @click="searchHoliday()">刷新</el-button>
            </el-col>
          </div>
        </el-col>
      </el-row>
      <el-table :data="holidayList" border style="width: 100%">
        <el-table-column
          prop="bdate"
          label="请假开始日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="edate"
          label="请假结束日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="apply_date"
          label="申请日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="notes"
          label="请假理由"
          width="200">
        </el-table-column>
        <el-table-column
          prop="date_num"
          label="请假天数"
          width="100">
        </el-table-column>
        <el-table-column
          prop=""
          label="操作" >
          <template slot-scope="scope">
            <el-button type="success" @click="showModifyDialog(scope.row)" size="mini">发起修改流程</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 新增请假 -->
    <el-dialog
      title="新增请假" :visible.sync="addHolidayDialogVisable" width="35%" @close="resetDialog">
      <el-form ref="addFormRef" :model="addHolidayForm" label-width="110px" :rules="addHolidayFormRules">
        <el-form-item label="请假起止日期" prop="dateRange">
          <el-date-picker
            v-model="addHolidayForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="notes">
          <el-input v-model="addHolidayForm.notes"></el-input>
        </el-form-item>
        <el-form-item >
              <span class="button_span">
                <el-button type="primary" @click="addHoliday()" size="small">添加</el-button>
                <el-button type="info" @click="resetDialog ()" size="small">重置</el-button>
              </span>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 修改请假 -->
    <el-dialog
      title="修改请假" :visible.sync="modifyHolidayDialogVisable" width="35%" @close="resetModifyDialog">
      <el-form ref="modifyFormRef" :model="modifyHolidayForm" label-width="110px" :rules="modifyHolidayFormRules">
       <el-form-item label="请假起止日期" prop="dateRange">
          <el-date-picker
            v-model="addHolidayForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="notes">
          <el-input v-model="modifyHolidayForm.notes"></el-input>
        </el-form-item>
        <el-form-item >
              <span class="button_span">
                <el-button type="primary" @click="modifyHoliday()" size="small">修改</el-button>
                <el-button type="info" @click="resetModifyDialog ()" size="small">重置</el-button>
              </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data () {
    return {
      holidayList: [],
      addHolidayDialogVisable: false,
      datePickerOptions: {
        disabledDate: (time) => {
          const nowDate = new Date()
          const oneDay = 1000 * 60 * 60 * 24
          const oneYearLater = new Date(nowDate.getTime() + (oneDay * 365))
          return time.getTime() < nowDate || time.getTime() > oneYearLater
        }
      },
      addHolidayForm: {
        dateRange: '',
        apply_date: '',
        date_num: '',
        notes: '',
        bdate: '',
        edate: ''
      },
      addHolidayFormRules: {
        dateRange: [{ required: true, message: '日期不能为空', trigger: 'blur' }],
        notes: [{ required: true, message: '请假理由不能为空', trigger: 'blur' }]
      },
      modifyHolidayDialogVisable: false,
      modifyHolidayForm: {
        pre_id: '',
        apply_date: '',
        date_num: '',
        notes: '',
        bdate: '',
        edate: ''
      },
      modifyHolidayFormRules: {
        dateRange: [{ required: true, message: '开始日期不能为空', trigger: 'blur' }],
        notes: [{ required: true, message: '请假理由不能为空', trigger: 'blur' }]
      }
    }
  },
  created () {
    this.searchHoliday()
  },
  methods: {
    // 查询个人假期
    searchHoliday () {
      this.$http.get('/user/get_user_holiday')
        .then(res => {
          if (res.data.state === 200) {
            console.log(res.data.data)
            this.holidayList = res.data.data
          } else {
            this.$message.error('请获取本人假期失败')
          }
        })
        .catch(_error => {
          this.$message.error('获取假期失败')
        })
    },
    // 显示发起请假流程对话框
    showAddHolidayDialog () {
      this.addHolidayDialogVisable = true
    },
    // 重置发起请假流程对话框
    resetDialog () {
      this.$refs.addFormRef.resetFields()
    },
    // 发起请假流程
    addHoliday () {
      // 先进行表单验证
      this.$refs.addFormRef.validate(valid => {
        console.log(this.addHolidayForm)
        if (valid === true) {
          this.addHolidayForm.bdate = this.addHolidayForm.dateRange[0]
          this.addHolidayForm.edate = this.addHolidayForm.dateRange[1]
          console.log(this.addHolidayForm)
          // 访问后端接口，发起请假流程
          this.$http.post('/user/add_holiday', this.addHolidayForm)
            .then(res => {
              if (res.data.state === 200) {
                this.addHolidayDialogVisable = false
                this.$message.success('发起新增请假流程成功')
              } else {
                this.$message.error('发起新增请假流程失败')
              }
            })
            .catch(_error => {
              this.$message.error('发起新增请假流程失败' + _error)
            })
        }
      })
    },
    // 显示修改请假对话框
    showModifyDialog (row) {
      console.log(row)
      this.modifyHolidayDialogVisable = true
      this.modifyHolidayForm.pre_id = row.id
      this.modifyHolidayForm.bdate = row.bdate
      this.modifyHolidayForm.edate = row.edate
      this.modifyHolidayForm.notes = row.notes
      // console.log(row)
    },
    // 重置修改请假对话框
    resetModifyDialog () {
      this.$refs.modifyFormRef.resetFields()
      this.searchHoliday()
    },
    // 修改请假
    modifyHoliday () {
      // 先进行表单验证
      this.$refs.modifyFormRef.validate(valid => {
        if (valid === true) {
          this.modifyHolidayForm.bdate = this.modifyHolidayForm.dateRange[0]
          this.modifyHolidayForm.edate = this.modifyHolidayForm.dateRange[1]
          this.$http.post('/user/modify_user_holiday', this.modifyHolidayForm)
            .then(res => {
              if (res.data.state === 200) {
                this.modifyHolidayDialogVisable = false
                this.$message.success('发起修改请假流程成功')
              } else {
                this.$message.error('发起修改请假流程失败')
              }
            })
            .catch(_error => {
              this.$message.error('发起修改请假流程失败')
            })
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.el-table{
  margin-top: 10px;
}
.el-pagination{
  margin-top: 10px;
}
</style>
>>>>>>> 1c629f66aa92a0a3f2c27e4dddd04bbab20bd734
