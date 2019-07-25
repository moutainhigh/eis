<template>
  <div id="app" :style="{'overflow-y':appOverflow}">
    <login-reg-modal @regSuccess = "regSuccess" @loginSuccess="loginSuccess"></login-reg-modal>
    <div class="left-menu" :class="{hidden:$route.path.split('/')[1]=='content',block:$route.path.split('/')[1]!=='content'}">
      <div class="btns">
        <span class="reg" @click="showRegModal()" v-show="isLogin == false">注册会员</span>
        <span class="userName" v-show="isLogin == true">{{userName}}</span>
        <span class="login" @click="showLoginModal()" v-show="isLogin == false">登入</span>
        <span class="logout" @click="logout()" v-show="isLogin == true">登出</span>
        <span class="close" @click="hideLeftMenu()">关闭</span>
      </div>
      <div class="search">
        <input type="text" placeholder="搜寻作者&作品"/>
        <img src="./assets/search.png"/>            
      </div>
      <table width="100%" class="menu-icons" >
        <tbody>
        <tr >
          <td > 
            <img src="./assets/icon_giftbox.png" style="width:50px;margin-bottom:5px;">
            <br>礼物箱 
          </td>
          <td  > 
            <img src="./assets/icon_charge.png" style="width:50px;margin-bottom:5px;">
            <br>充值
          </td>
          <td  > 
            <img src="./assets/icon_mybook.png" style="width:50px;margin-bottom:5px;">
            <br>我的书架 
          </td>
        </tr>
        <tr >
          <td > 
            <img src="./assets/icon_like.png" style="width:50px;margin-bottom:5px;">
            <br>感兴趣的作品 
          </td>
          <td > 
            <img src="./assets/icon_history.png" style="width:50px;margin-bottom:5px;">
            <br>最近观看记录 
          </td>
          <td > 
            <img src="./assets/icon_cs.png" style="width:50px;margin-bottom:5px;">
            <br>客服中心 
          </td>
        </tr>
        <tr >
          <td >
            <img src="./assets/icon_myinfo.png" style="width:50px;margin-bottom:5px;">
            <br>基本资料 
          </td>
          <td > 
            <img src="./assets/icon_setting.png" style="width:50px;margin-bottom:5px;">
            <br>修改个人资料 
          </td>
          <td > 
            <img src="./assets/icon_lang.png" style="width:50px;margin-bottom:5px;">
            <br>语言
          </td>
          <td style="color:#333;text-align:center;width:5%;padding:10px 0px;"> &nbsp; </td>
        </tr>
        <tr >
          <td  > 
            <img src="./assets/icon_event.png" style="width:50px;margin-bottom:5px;">
            <br>优惠活动 
          </td>
          <td  > 
            <img src="./assets/icon_privacy.png" style="width:50px;margin-bottom:5px;">
            <br>个人资料保护方针 
          </td>
          <td  > 
            <img src="./assets/icon_agreement.png" style="width:50px;margin-bottom:5px;">
            <br>使用条款 
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="wrap" :style="{'left':wrapLeft}" >
      <div class="top-menu" :class="{hidden:$route.path.split('/')[1]=='content',block:$route.path.split('/')[1]!=='content'}">
        <div class="left">
          <img src="./assets/headicon_menu.png" class="menu-icon" @click="showLeftMenu()"/>
          <img src="./assets/logo.png" class="logo"/>
        </div>
        <ul class="right">
          <li>
            <img src="./assets/language_icon_mo.png"/>
            <span>简体</span>
          </li>
          <li>
            <router-link to="/charge">
              <img src="./assets/charge_icon.png"/>
              <span>储值</span>
            </router-link>
          </li>
          <li>
            <router-link to="/extra/library">
              <img src="./assets/mylibrary_icon.png"/>
              <span>书柜</span>
            </router-link>
          </li>
          <li>
            <router-link to="/extra">
              <img src="./assets/giftbox_icon_re.png"/>
              <span>礼物</span>
            </router-link>
          </li>
        </ul>
      </div>
      <div class="nav" :class="{hidden:$route.path.split('/')[1]=='content',block:$route.path.split('/')[1]!=='content'}">
        <ul>
          <li><router-link to="/story">连载</router-link></li>
          <li><router-link to="/finished">完结</router-link></li>
          <li><router-link to="/range">排名</router-link></li>
          <li><router-link to="/activity">优惠活动</router-link></li>
          <li><router-link to="/free">免费/特价</router-link></li>
        </ul>
      </div>
      <div :class="{minHeight:$route.path.split('/')[1]=='content'}">
        <router-view :key="$route.path"/>
      </div>
      <footer >
        <div class="footer-title">
          <img src="./assets/footer_logo.png"/>
          <span @click="goTop()">△ TOP  </span>
        </div>
        <div class="links">
          <router-link to="#">使用条款</router-link>&nbsp;|&nbsp;<router-link to="#">个人资源保护方针</router-link>
        </div>
        <div class="cont">
          <p>顶通网站的电子著作，皆受到著作权法保护，若未受到TopCo Co,Ltd. 的承认许可，用户不得擅自复制、引用</p>
          <p>如发现违法事项，请向客服中心举报</p>
          <p>TopCo Co,Ltd.</p>
        </div>
        <div class="contact">
          <p>客服中心 : helpdesk@toptoon.net / 02-2716-0982</p>
          <p>客服咨询时间：周一至周五 09:30~12:30、13:30~18:30</p>
        </div>
        <div class="change-lang">
          选择语言：<span>简体</span>&nbsp;|&nbsp;<span>English</span>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>
let timer = null
import Qs from 'qs'
import loginRegModal from '@/components/loginRegModal'

export default {
  name: 'App',
  components:{
    loginRegModal
  },
  data () {
    return {
      split1:0.5,
      wrapLeft: 0,
      appOverflow:'auto',
      path:'',
      userName:'',
      isLogin:false
    }
  },
  computed:{
    
  },
  mounted() {
    let username =""
    let password = ""
    if(localStorage.getItem("autoLogin")!== undefined && localStorage.getItem("autoLogin")!== ''&&localStorage.getItem("autoLogin")!== null) {
      username = localStorage.getItem('userName')
      if(localStorage.getItem("password")!== undefined && localStorage.getItem("password")!== ''&&localStorage.getItem("password")!== null) {
        password = localStorage.getItem('password')
        let data =  {
          'username':username,
          'userPassword':password
        }
        this.axios({
          method: 'post',
          url: '/user/login.json',
          data: Qs.stringify(data)
        }).then(res => {
          if(res.data.message && res.data.message.operateCode !== this.code.success){
            // this.$Message.info(res.data.message.message)
          }
          if(res.data.message && res.data.message.operateCode == this.code.success){
            // this.$Message.success('登录成功')
            sessionStorage.setItem('isLogin',true)
            this.isLogin = true
            if(this.$route.query.redirect){
              this.$router.push(this.$route.query.redirect)
            }
          }
        }).catch(e => {
          console.log(e)
        })

      }
    }
  },
  updated(){
    this.path = this.$route.path.split("/")[1]
    this.userName = localStorage.getItem('userName')
    console.log(sessionStorage.getItem('isLogin'),123456)
    if(sessionStorage.getItem('isLogin')!==''&&sessionStorage.getItem('isLogin')!==undefined&&sessionStorage.getItem('isLogin')!==null){
      this.isLogin = true
    }
  },
  methods:{
    showLeftMenu(){
      this.wrapLeft = '6rem'
      this.appOverflow = 'hidden'
    },
    hideLeftMenu(){
      this.wrapLeft = '0'
      this.appOverflow = 'auto'
    },
    goTop () {
      timer = setInterval(function () {
        let osTop = document.documentElement.scrollTop || document.getElementById("app").scrollTop
        let ispeed = Math.floor(-osTop / 5)
        document.documentElement.scrollTop = document.getElementById("app").scrollTop = osTop + ispeed
        if (osTop === 0) {
          clearInterval(timer)
        }
      }, 30)
    },
    showLoginModal () {
      $(".modal").modal('hide')
      $("#loginModal").modal('show')
    },
    showRegModal () {
      $(".modal").modal('hide')
      $("#regModal").modal('show')
    },
    regSuccess (params) {
      localStorage.setItem('userName',params.userName)
      this.userName = params.userName
      this.isLogin = true
    },
    loginSuccess (params) {
      this.userName = params.userName
      this.isLogin = true
      console.log(123456789)
    },
    logout () {
      this.axios.get('/user/logout.json').then(res => {
        if(res.data.message && res.data.message.operateCode !== this.code.success){
          this.$Message.info(res.data.message.message)
        }
        if(res.data.message && res.data.message.operateCode == this.code.success){
          this.$Message.success('登出成功')
          sessionStorage.removeItem('isLogin')
          this.userName = ''
          this.isLogin = false
          localStorage.removeItem("autoLogin")
          localStorage.removeItem("userName")
          localStorage.removeItem("password")
        }
      }).catch(e => {
        console.log(e)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import './style/var.scss';
.hidden{
  display: none;
}
.block{
  display: block;
}
.minHeight{
  min-height: 900px;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  max-width:750px;
  width:100%;
  margin:0 auto;
  position:relative;
  height:100%;
  width:100%;
  overflow-x:hidden;
}
.left-menu{
  width:6rem;
  position:absolute;
  top:0;
  bottom:0;
  left:0;
  .btns{
    background:#4c4c4c;
    overflow:hidden;
    height:1rem;
    span{
      font-size:$titleSize;
      color:#fff;
      height:0.6rem;
      margin-top:0.2rem;
      line-height:0.6rem;
      text-align:center;
      border-radius:30px;
      font-weight: bold;
      display: block;
      margin-left:0.2rem; 
      &.reg{
        float:left;
        width:1.8rem;       
        border:#fff solid 1px;
        cursor: pointer;
      }
      &.userName{
        float: left;
      }
      &.login{
        float:left;
        width:1.2rem;        
        cursor: pointer;        
        border:#fff solid 1px;
      }
      &.logout{
        float:left;
        width:1.2rem;        
        cursor: pointer;      
        border:#fff solid 1px;
      }
      &.close{
        float:right;
        width:1.2rem;        
        margin-left:0;        
        margin-right:0.2rem;        
        border:#fff solid 1px;
      }
    }
  }
  .search{
    height:1.2rem;
    background:#eee;
    input{
      height:0.8rem;
      width:4.8rem;
      margin:0.2rem;
      background:#fff;
      padding:0.2rem;
      border-radius: 10px;
      -webkit-tap-highlight-color: rgba(0,0,0,0);
      outline: none;
      border:0px;
    }
    img{
      height:0.5rem;
      width:0.5rem;
      margin-top:0.35rem;
      float:right;
      margin-right:0.2rem;
    }
  }
  .menu-icons{
    border-bottom:1px solid #eee;
    margin:10px 0px 0px 0px;
    tr{
      height:72px;
      td{
        color:#333;
        text-align:center;
        width:32%;
        padding:10px 0px;
        font-size:$fontSize;
      }
    }
  }
}
.wrap{
  max-width:750px;
  width:100%;
  margin:0 auto;
  position:absolute;
  top:0;
  left:0;
  background:#ffffff;
  transition:left 1s ease-in-out;
  .top-menu{
    height:1rem;
    line-height:1rem;
    overflow:hidden;
    border-bottom:2px solid #eeeeee;
    .left{
      float:left;
      overflow:hidden;
      .menu-icon{
        display:block;
        width:0.43rem;
        height:auto;
        margin:0.27rem;
        float:left;
        cursor:pointer;
      }
      .logo{
        display:block;
        height:0.3rem;
        width:auto;
        margin-top:0.32rem;
        float:left;
      }
    }
    .right{
      float:right;
      overflow:hidden;
      li{
        float:left;
        text-align:center;
        height:1rem;
        line-height:1rem;
        padding:0 0.2rem;
        a{
          color:$fontColor;
        }
        img{
          height:0.46rem;
          display: block;
          margin-top:0.1rem;
        }
        span{
          font-size:$fontSize;
          display: block;
          height:0.4rem;
          line-height:0.4rem;
        }
      }
      
    }
  }
  .nav{
    height:0.9rem;
    line-height:0.9rem;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.1);
    position:relative;
    z-index:10;
    ul{
      overflow:hidden;
      li{
        width:15%;
        text-align:center;
        color:$fontColor;
        font-size: $titleSize;
        float:left;
        a{
          color:$fontColor;
          &.router-link-active{
            color:$mainColor;
          }
        }
        &:nth-child(4),&:nth-child(5){
          width:27.5%;
        }
      }
    }
  }
  .demo-carousel{
    img{
      width:100%;
      height:auto;
    }
  }
  footer{
    background:$blackBgColor;
    padding:0.4rem;
    color:$blackBgFontColor;
    .footer-title{
      height:1rem;
      line-height:1rem;
      margin-top:0.2rem;
      overflow:hidden;
      img{
        width:1.4rem;
        height:auto;
        display:block;
        float:left;
      }
      span{
        display:block;
        height:0.6rem;
        width:1.6rem;
        text-align:center;
        line-height:0.6rem;
        background:$blackBgBtnColor;
        border: #ffffff solid 1px;
        border-radius:30px;
        color:#ffffff;
        float:right;
        font-size: $titleSize;
        cursor:pointer;
      }
    }
    .links{
      a{
        color:#fff;
        font-size: $titleSize;
      }
    }
    .cont{
      line-height: 1.8;
      margin-top: 0.2rem;
    }
    .contact{
      line-height:1.8;
      margin-top:2em;
    }
    .change-lang{
      line-height:1.8;
      margin-top:1em;
      span{
        font-size:$titleSize;
        color:#fff;
      }
    }
  }
}

</style>
