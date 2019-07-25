<template>
  <div class="loginRegModal">
    <bs-modal name="loginModal" :hasImg="true" >
      <div slot="header">
        <h4 class="modal-title" id="myModalLabel">登录</h4>   
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>           
        <span class="title-btn" @click="showReg">注册</span> 
      </div>
      <div slot="body">
        <div class="input-box">
          <input type="email" class="input-style" v-model="loginForm.username" placeholder="请输入账号" id="user_id">
        </div>
        <div class="input-box">
          <input type="password" class="input-style" v-model="loginForm.password" placeholder="请输入密码" >
        </div>
        <div class="checkboxes">          
          <input id="remember_password_input" type="checkbox" v-model="loginCheck[0]">
          <label id="remember_password_label" for="remember_password_input">记住密码</label>
          <input id="auto_login_input" type="checkbox" value="check" v-model="loginCheck[1]" >
          <label id="auto_login_label" for="auto_login_input">自动登入</label>
        </div>
      </div>
      <button type="button" class="btn btn-modal" slot="btns" @click="login">登录</button>
      <div slot="extra">
        <div class="extra-text" align="center">
          ● 使用下方社交平台帐号登入 ●
        </div>
        <div class="forget-password">
			    若您忘记帐号/密码，请按[这里]		
        </div>
      </div>
    </bs-modal> 
    <bs-modal name="regModal" :hasImg="true" >
      <div slot="header">
        <h4 class="modal-title" id="myModalLabel">注册</h4>   
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>           
        <span class="title-btn" @click="showLogin">登录</span> 
      </div>
      <div slot="body">
        <div class="input-box">
          <input type="email" class="input-style" v-model="regForm.username" placeholder="请输入账号" id="user_id">
        </div>
        <div class="input-box">
          <input type="password" class="input-style" v-model="regForm.password" placeholder="请输入密码" >
        </div>
        <div class="checkboxes">          
          <input id="remember_password_input" type="checkbox" v-model="regCheck[0]" >
          <label id="remember_password_label" for="remember_password_input">同意使用条款</label>
          <input id="auto_login_input" type="checkbox" v-model="regCheck[1]">
          <label id="auto_login_label" for="auto_login_input">同意个人资料保护方针</label>
        </div>
      </div>
      <button type="button" class="btn btn-modal" slot="btns" @click="reg">注册账号</button>
      <div slot="extra">
        <div class="extra-text" align="center">
          ● 使用下方社交平台帐号登入 ●
        </div>
        <div class="forget-password">
			    若您忘记帐号/密码，请按[这里]		
        </div>
      </div>
    </bs-modal>   
  </div>
</template>

<script>
import Qs from 'qs'
import bsModal from './../components/bsmodal.vue'
export default {
  name: 'loginRegModal',
  components:{
    bsModal
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      regForm:{},
	    loginForm:{},
      regCheck:[true,true],
      loginCheck:[true,true]
    }
  },
  mounted() {
    this.$Message.config({
      
    });
    this.$set(this.loginForm,'username',localStorage.getItem('userName'))
    if(localStorage.getItem("password")!== undefined && localStorage.getItem("password")!== ''&&localStorage.getItem("password")!== null) {
      this.$set(this.loginForm,'password',localStorage.getItem('password'))
      this.loginCheck[0] = true
    }else{
      this.loginCheck[0] = false
    }
    if(localStorage.getItem("autoLogin")!== undefined && localStorage.getItem("autoLogin")!== ''&&localStorage.getItem("autoLogin")!== null) {
      this.loginCheck[1] = true
    }else{
      this.loginCheck[1] = false
    }
  },
  updated(){
  },
  methods:{
    showReg(){
      $(".modal").modal('hide')
      $("#regModal").modal('show')
    },
    showLogin(){
      $(".modal").modal('hide')
      $("#loginModal").modal('show')
    },
    reg(){
      if(this.regForm.username == undefined || this.regForm.username == ''||this.regForm.username.indexOf('@') == -1){
        this.$Message.warning('请输入正确的邮箱号');
        return 
      }
      if(this.regForm.password == undefined || this.regForm.password == ''){
        this.$Message.warning('请输入密码');
        return 
      }
      if(this.regCheck[0] == false || this.regCheck[1] == false){
        this.$Message.warning('请同意条款及保护方针');
        return 
      }
      let data =  {
        'userBindMailBox':this.regForm.username,
        'userPassword':this.regForm.password
      }
      this.axios({
        method: 'post',
        url: '/user/registerByMailBox.json',
        data: Qs.stringify(data)
      }).then(res => {
        if(res.data.message && res.data.message.operateCode !== this.code.success){
          this.$Message.info(res.data.message.message)
        }
        if(res.data.message && res.data.message.operateCode == this.code.success){
          this.$Message.success('注册成功')
          $('#regModal').modal('hide')
          this.$emit('regSuccess',{
            'userName':this.regForm.username,
            'password':this.regForm.password
          })
          sessionStorage.setItem('isLogin',true)
        }
      }).catch(e => {
        console.log(e)
      })
    },
	login(){
      if(this.loginForm.username == undefined || this.loginForm.username == ''||this.loginForm.username.indexOf('@') == -1){
        this.$Message.warning('请输入正确的邮箱号');
        return 
      }
      if(this.loginForm.password == undefined || this.loginForm.password == ''){
        this.$Message.warning('请输入密码');
        return 
      }
      let data =  {
        'username':this.loginForm.username,
        'userPassword':this.loginForm.password
      }
      this.axios({
        method: 'post',
        url: '/user/login.json',
        data: Qs.stringify(data)
      }).then(res => {
        if(res.data.message && res.data.message.operateCode !== this.code.success){
          this.$Message.info(res.data.message.message)
        }
        if(res.data.message && res.data.message.operateCode == this.code.success){
          this.$Message.success('登录成功')
          $('.modal').modal('hide')
          localStorage.setItem("userName",this.loginForm.username)
          sessionStorage.setItem('isLogin',true)
          console.log("login",sessionStorage.getItem('isLogin'))
          this.$emit('loginSuccess',{
            'userName':this.loginForm.username,
            'password':this.loginForm.password
          })
          if(this.loginCheck[0] == true){               
            localStorage.setItem("password",this.loginForm.password)
          }else{
            localStorage.removeItem("password")
          }
          if(this.loginCheck[1] == true){
            localStorage.setItem("autoLogin",true)
          }else{
            localStorage.removeItem("autoLogin")
          }
          if(this.$route.query.redirect){
            this.$router.push(this.$route.query.redirect)
          }
        }
      }).catch(e => {
        console.log(e)
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.btn{
  outline: 0;
  &.focus, &:focus {
    outline: 0;
    -webkit-box-shadow: none;
    box-shadow: none;
  }
}
</style>
