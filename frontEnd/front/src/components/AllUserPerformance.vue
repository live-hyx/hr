<template>
  <div>
    <!-- 面包屑 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>绩效管理</el-breadcrumb-item>
      <el-breadcrumb-item>全行绩效</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片 -->
    <el-card>
      <el-row>
        <el-col :span="10">
          <div class="grid-content bg-purple-dark">
            <el-input placeholder="请输入用户名称" v-model="userName" @change="searchUserPerformance()">
              <el-button slot="append" icon="el-icon-search" @click="searchUserPerformance()"></el-button>
            </el-input>
          </div>
        </el-col>
      </el-row>
      <el-table :data="userPerformanceList" border style="width: 100%">
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
      userPerformanceList: [],
      page_sizes: [1, 2, 5, 10],
      page_size: 5,
      current_page: 1,
      total: 0
    }
  },
  created () {
    this.searchUserPerformance()
  },
  methods: {
    // 搜索所有用户绩效
    searchUserPerformance () {
      const currentIndex = (this.current_page - 1) * this.page_size
      this.$http.get('/admin/getAllSigns?username=' + this.userName + '&current_index=' + currentIndex + '&page_size=' + this.page_size)
        .then(res => {
          if (res.data.state === 200) {
            this.userPerformanceList = res.data.data
            for (var i = 0; i < this.userPerformanceList.length; i++) {
              if (this.userPerformanceList[i].daily_time > 8) {
                this.userPerformanceList[i].if_overtime = '是'
              } else if (this.userPerformanceList[i].daily_time <= 8) {
                this.userPerformanceList[i].if_overtime = '否'
              }
            }
            this.total = res.data.total
          } else {
            this.$message.error('获取所有人绩效失败')
          }
        })
        .catch(_error => {
          this.$message.error('获取所有人绩效失败')
        })
    },
    // 修改page_size
    handleSizeChange (pageSize) {
      this.page_size = pageSize
      this.searchUserPerformance()
    },
    // 修改current_page
    handleCurrentChange (currentPage) {
      this.current_page = currentPage
      this.searchUserPerformance()
    }
  }
}
</script>
<style scoped>
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
