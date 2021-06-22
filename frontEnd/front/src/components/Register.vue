<template>
    <div class="register_container">
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
      <div class="register_container1">
            <span class="title_span">人事管理系统</span>
                <el-form ref="registerFormRef" :model="registerForm"  label-width="" class="register_form" :rules="registerFormRules">
                    <el-form-item label="" prop="username">
                        <div class="search1">
                        <el-input v-model="registerForm.username" prefix-icon="el-icon-user" label-width=120px placeholder="请输入用户名"></el-input>
                        </div>
                    </el-form-item>
                    <el-form-item label="" prop="password">
                      <div class="search2">
                        <el-input v-model="registerForm.password" prefix-icon="el-icon-lock" type="password" placeholder="请输入密码"></el-input>
                      </div>
                    </el-form-item>
                  <el-form-item label="" prop="repassword">
                    <div class="search2">
                      <el-input v-model="registerForm.repassword" prefix-icon="el-icon-unlock" type="password" placeholder="请再次输入密码"></el-input>
                    </div>
                  </el-form-item>
                  <el-form-item label="" prop="email">
                    <div class="search2">
                      <el-input v-model="registerForm.email" prefix-icon="el-icon-chat-dot-square" placeholder="请输入邮箱"></el-input>
                    </div>
                  </el-form-item>
                  <el-form-item class="btns">
                    <div class="button1">
                      <el-button type="success" @click="sendVerifyCode" :disabled="isDisabled">发送验证码</el-button>
                    </div>
                  </el-form-item>
                  <el-form-item label="" prop="verifyCode">
                    <div class="search2">
                      <el-input v-model="registerForm.verifyCode" prefix-icon="el-icon-s-promotion" placeholder="请输入验证码"></el-input>
                    </div>
                  </el-form-item>
                    <el-form-item class="btns">
                      <div class="button">
                        <el-button round @click="register">注册</el-button>
                        <router-link to='/login'>
                          <el-button round>返回</el-button>
                        </router-link>
                        <el-button type="info" round @click="reset">重置</el-button>
                      </div>
                    </el-form-item>
                </el-form>
      </div>
    </div>
</template>
<script>
import Vue from 'vue'
import VueParticles from 'vue-particles'
Vue.use(VueParticles)
export default {
  data () {
    return {
      isDisabled: false,
      registerForm: {
        username: '',
        password: '',
        repassword: '',
        email: '',
        verifyCode: ''
      },
      registerFormRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名必须在3-20个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '密码必须在3-20个字符之间', trigger: 'blur' }
        ],
        repassword: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '密码必须在3-20个字符之间', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' }
        ],
        verifyCode: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    register () {
    // 先进行表单预验证
      this.$refs.registerFormRef.validate(async valid => {
        if (valid === true) {
        // 表单预验证通过，向后台发送请求
          this.$http.post('register?username=' + this.registerForm.username + '&pwd=' + this.registerForm.password + '&rpwd=' + this.registerForm.repassword + '&email=' + this.registerForm.email + '&code=' + this.registerForm.verifyCode)
            .then(res => {
              this.$message.success(res.data.msg)
              if (res.data.state === 200) {
                this.$router.push('/login')
              }
            })
            .catch(error => {
              const error2 = error + ''
              this.$message.info('出错了' + ':' + error2)
            })
        }
      })
    },
    reset () {
      this.$refs.registerFormRef.resetFields()
      this.$message.success('已重置注册框')
    },
    sendVerifyCode () {
      this.isDisabled = true
      setTimeout(() => {
        this.isDisabled = false
      }, 5000)
      this.$http.post('email?emailAddress=' + this.registerForm.email)
        .then(res => {
          this.$message.success(res.data.notes)
        })
        .catch(error => {
          const error2 = error + ''
          this.$message.info('出错了' + ':' + error2)
        })
    }
  }
}
</script>
<style lang="less" scoped>
.register_container{
    display:flex;
    flex-direction: column;
    justify-content:center;
    align-items:center;
    background:url(../assets/demo-1-bg.jpg)no-repeat fixed top;
    height: 100%;
    background-size: 100% 100%;
}
#particles-js{
  width: 100%;
  height: calc(100%);
  position: absolute;
}
.register_container1{
  display:flex;
  flex-direction: column;
  justify-content:center;
  align-items:center;
  background-color: rgba(255,255,255,0.1);
  width: 350px;
  height: 600px;
  background-size: 100% 100%;
}
.title_span{
  position:relative;
  display:flex;
  flex-direction: column;
  color: floralwhite;
  font-size: 35px;
  font-family: "楷体",sans-serif;
  top: -4%;
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
.button1 {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}
.btns{
  display:flex;
  flex-direction: column;
}
.register_form{
    width: 100%;
    padding: 10px 20px;
    box-sizing: border-box;
}
</style>
