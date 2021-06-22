<template>

    <el-container class="home-container">
        <!-- 头部区域 -->
        <el-header>
            <div>
                <span style="text-align:center;line-height:18px;" @click="backHome">人事管理系统</span>
            </div>
          <div>
          <el-button :type="buttonType(index)" ref="sign" @click="sign" :disabled="isDisabled">{{signParam}}</el-button>
          <el-button type="danger" @click="logout">退出</el-button>
          </div>

        </el-header>
        <el-container>
            <!-- 侧边栏 -->
            <el-aside :width="isCollapse? '5%':'15%'">
                <div class="toggle-button" @click="toggleCollapse">|||</div>
                <el-menu
                    :unique-opened="true" :router="true" :collapse="isCollapse" :collapse-transition="false"
                    class="el-menu-vertical-demo" :default-active="activePath"
                    background-color="#272C37"
                    text-color="#fff"
                    active-text-color="#409eff">
                    <!-- 一级菜单 -->
                    <el-submenu v-for="item in menuList" :key="item.id" :index="item.id" >
                        <template slot="title">
                        <i :class="item.icon"></i>
                        <span>{{item.name}}</span>
                        </template>
                        <el-menu-item v-for="subitem in item.children" :key="subitem.id" :index="subitem.path"
                        @click="saveNavState(subitem.path)">
                            <template slot="title">
                            <i :class="subitem.icon"></i>
                            <span>{{subitem.name}}</span>
                            </template>
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <!-- 主体区域 -->
            <el-main>
                <router-view>
                  <div class="im">

                  </div>
                </router-view>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
export default {
  inject: ['reload'],
  data () {
    return {
      menuList: [],
      isCollapse: false,
      activePath: '',
      signParam: '',
      isDisabled: false,
      signFlag: '',
      index: ''
    }
  },
  created () {
    this.getMenu()
    this.activePath = window.sessionStorage.getItem('activePath')
  },
  methods: {
    buttonType (index) {
      console.log(this.index)
      // eslint-disable-next-line eqeqeq
      if (index == 1) {
        return 'success'
        // eslint-disable-next-line eqeqeq
      } else if (index == 2) {
        return 'warning'
        // eslint-disable-next-line eqeqeq
      } else if (index == 3) {
        return 'info'
      }
    },
    getCurrentTime () {
      var yy = new Date().getFullYear()
      var mm = new Date().getMonth() + 1
      var dd = new Date().getDate()
      var hh = new Date().getHours()
      var mf = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes()
      var ss = new Date().getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds()
      this.gettime = yy + '-' + mm + '-' + dd + ' ' + hh + ':' + mf + ':' + ss
      console.log(this.gettime)
    },
    sign () {
      if (this.signFlag === 202) {
        this.getCurrentTime()
        this.$http.post('/user/signIn')
          .then(res => {
            this.$message.success(' 签到成功 ' + this.gettime)
            console.log(' 签到成功 ' + this.gettime)
            this.signParam = '签退'
            this.index = 2
            this.signFlag = 200
            this.reload()
            console.log(res)
          })
          .catch(error => {
            this.$message.error('签到出错啦')
            console.log(error)
          })
      }
      if (this.signFlag === 200) {
        this.getCurrentTime()
        this.$http.post('/user/signUp')
          .then(res => {
            this.$message.success(' 签退成功 ' + this.gettime)
            console.log(' 签退成功 ' + this.gettime)
            this.signParam = '已签退'
            this.signFlag = 201
            this.index = 3
            this.isDisabled = true
            this.reload()
            console.log(res)
          })
          .catch(error => {
            this.$message.error('签退出错啦')
            console.log(error)
          })
      }
    },
    // 获取菜单
    getMenu () {
      this.$http.get('/user/getmenu')
        .then(res => {
          const data = res.data.data
          this.menuList = data
          this.signFlag = res.data.sign
          this.signParam = res.data.signNotes
          if (res.data.sign === 202) {
            this.index = 1
          } else if (res.data.sign === 201) {
            this.index = 3
            this.isDisabled = true
          } else if (res.data.sign === 200) {
            this.index = 2
          }
        })
        .catch(error => {
          console.log(error)
        })
    },
    // 退出
    logout () {
      sessionStorage.clear()
      this.$router.push('/login')
    },
    // 点击按钮切换菜单的折叠与展开
    toggleCollapse () {
      this.isCollapse = !this.isCollapse
    },
    // 返回主页而
    backHome () {
      this.$router.push('/home')
    },
    // 保存链接的激活状态
    saveNavState (activePath) {
      window.sessionStorage.setItem('activePath', activePath)
    }
  }
}
</script>
<style lang="less" scoped>
.home-container{
    height: 100%;
}
.el-header{
    background-color: #272C37;
    display: flex;
    justify-content: space-between;
    padding-left: 0;
    align-items: center;
    color:#fff;
    font-size: 25px;
    >div{
        display: flex;
        align-items: center;
        span{
            margin-left: 10px;
        }
    }
}
.el-aside{
    background-color: #272C37;
    .el-menu{
        border-right: none;
    }
}
.el-main{
    background-color:#fff;
}
.toggle-button{
    background-color: #272C37;
    color: #fff;
    font-size: 10px;
    line-height: 24px;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer;
}
</style>
