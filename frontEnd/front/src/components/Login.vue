<template>
    <div class="login_container">
      <vue-particles
        class="login-bg"
        color="#dedede"
        :particleOpacity="0.7"
        :particlesNumber="60"
        shapeType="circle"
        :particleSize="4"
        linesColor="#dedede"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.5"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
      >
      </vue-particles>
      <div class="login_container1">
            <span class="title_span">人事管理系统</span>
                <el-form ref="loginFormRef" :model="loginForm"  label-width="" class="login_form" :rules="loginFormRules">
                    <el-form-item label="" prop="username">
                        <div class="search1">
                        <el-input v-model="loginForm.username" prefix-icon="el-icon-user" ></el-input>
                        </div>
                    </el-form-item>
                    <el-form-item label="" prop="password">
                      <div class="search2">
                        <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
                      </div>
                    </el-form-item>
                    <el-form-item class="btns">
                      <div class="button">
                        <router-link to="/register">
                          <el-button round >注册</el-button>
                        </router-link>
                        <el-button round @click="login">登录</el-button>
                        <el-button type="info" round @click="reset">重置</el-button>
                      </div>
                    </el-form-item>
                </el-form>
      </div>
    </div>
</template>

<script>
import Qs from 'qs'
import Vue from 'vue'
import VueParticles from 'vue-particles'
Vue.use(VueParticles)

export default {
  data () {
    return {
      loginForm: {
        username: 'admin',
        password: '123'
      },
      loginFormRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名必须在3-20个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '密码必须在3-20个字符之间', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login () {
    // 先进行表单预验证
      this.$refs.loginFormRef.validate(async valid => {
        if (valid === true) {
        // 表单预验证通过，向后台发送请求
          this.$http.post('login', Qs.stringify(this.loginForm))
            .then(res => {
              console.log(res.data)
              const token = res.data.token
              this.$message.success('登录成功')
              // 保存token
              sessionStorage.setItem('token', token)
              this.$router.push('/home')
            })
            .catch(error => {
              const error2 = error + ''
              if (error2.indexOf('401') !== -1) { this.$message.error('用户名或密码错误') } else this.$message.error('服务器出错了！')
            })
        }
      })
    },
    reset () {
      this.$refs.loginFormRef.resetFields()
      this.$message.success('已重置登录框')
    }
  }
}
</script>
<style lang="less" scoped>
.login_container{
    display:flex;
    flex-direction: column;
    justify-content:center;
    align-items:center;
    background:url(../assets/demo-1-bg.jpg)no-repeat fixed top;
    background-color: #555;
    height: 100%;
    background-size: 100% 100%;

}
#particles-js{
  width: 100%;
  height: calc(100%);
  position: absolute;
}
.login_container1{
  display:flex;
  flex-direction: column;
  justify-content:center;
  align-items:center;
  background-color: rgba(255,255,255,0.1);
  height: 500px;
  width: 350px;
  background-size: 100% 100%;
}
.title_span{
    position:relative;
    display:flex;
    flex-direction: column;
    color: floralwhite;
    font-size: 35px;
    font-family: "楷体",sans-serif;
    top: -10%;
}
.search1{
     display: flex;
     flex-direction: column;
     justify-content: center;
     align-items: center;
    .el-input {
      width: 100%;
    }

}
.search2{
     display: flex;
     flex-direction: column;
     justify-content: center;
     align-items: center;
    .el-input {
      width: 100%;
    }
}
.button {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.btns{
    display:flex;
    flex-direction: column;
}
.login_form{
    width: 100%;
    padding: 10px 20px;
    box-sizing: border-box;

}
</style>
