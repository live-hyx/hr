<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>绩效管理</el-breadcrumb-item>
      <el-breadcrumb-item>个人绩效</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <el-row>
        <el-col :span="10">
          <div class="grid-content bg-purple-dark">
          </div>
        </el-col>
      </el-row>
      <el-table :data="performanceList" border style="width: 100%">
        <el-table-column
          prop="username"
          label="用户名"
          width="250">
        </el-table-column>
        <el-table-column
          prop="btime"
          label="签到时间"
          width="250">
        </el-table-column>
        <el-table-column
          prop="etime"
          width="250"
          label="签退时间">
        </el-table-column>
        <el-table-column
          prop="daily_time"
          width="200"
          label="工作时长">
        </el-table-column>
        <el-table-column
          prop="if_overtime"
          label="是否加班">
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="page_sizes"
        :page-size="page_size"
        layout="total, sizes, prev, pager, next"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>
<script>
export default {
  data () {
    return {
      userName: '',
      performanceList: [],
      page_sizes: [1, 2, 5, 10],
      page_size: 5,
      current_page: 1,
      total: 0
    }
  },
  created () {
    this.searchHoliday()
  },
  methods: {
    searchHoliday () {
      const currentIndex = (this.current_page - 1) * this.page_size
      this.$http.get('/user/getAllSigns?user_name=' + this.userName + '&current_index=' + currentIndex + '&page_size=' + this.page_size)
        .then(res => {
          if (res.data.state === 200) {
            console.log(res.data)
            this.performanceList = res.data.data
            for (var i = 0; i < this.performanceList.length; i++) {
              if (this.performanceList[i].daily_time > 8) {
                this.performanceList[i].if_overtime = '是'
              } else if (this.performanceList[i].daily_time <= 8) {
                this.performanceList[i].if_overtime = '否'
              }
            }
            this.total = res.data.total
          } else {
            this.$message.error('获取已发的流程失败')
          }
        })
        .catch(_error => {
          this.$message.error('获取已发的流程失败')
        })
    },
    // 修改page_size
    handleSizeChange (pageSize) {
      this.page_size = pageSize
      this.searchHoliday()
    },
    // 修改current_page
    handleCurrentChange (currentPage) {
      this.current_page = currentPage
      this.searchHoliday()
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
.addBtn{
  margin-left: 10px;
}
</style>
