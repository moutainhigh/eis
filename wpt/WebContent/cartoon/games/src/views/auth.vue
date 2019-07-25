<template>
  <div class="auth">
    <div align="center" style="padding:150px 0px;">		<img src="./../assets/error.png" style="width:128px;"><br>该内容需登入才可观看<br><br>
		<span class="btn btn-default" @click="reg">注册会员</span>
    &nbsp;
		<span class="btn btn-danger" @click="login">登入</span>
		</div>
    <login-reg-modal @regSuccess="regSuccess" @logSuccess="loginSuccess"></login-reg-modal>   
  </div>
</template>

<script>
import loginRegModal from './../components/loginRegModal.vue'
export default {
  name: 'auth',
  components:{
    loginRegModal
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  mounted() {
    $("#loginModal").modal("show")
  },
  methods:{
    reg(){
      $("#regModal").modal("show")
    },
    login(){
      $("#loginModal").modal("show")
    },
    regSuccess (params) {
      localStorage.setItem('userName',params.userName)
    },
    loginSuccess (params) {
      localStorage.setItem('userName',params.userName)
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.auth{
  .btn{
    padding:0.2rem 0.3rem;
    outline: 0;
    &.btn-default{
      background: #ccc;
    }
  }
}
</style>
