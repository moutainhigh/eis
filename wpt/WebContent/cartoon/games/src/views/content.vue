<template>
  <div class="cartoon-content" >
    <div class="detail-head">
      <table>
      <tbody>
        <tr>
          <td class="icon">
            <router-link :to="'/cartoon/'+id"><img src="./../assets/icon_back2.png" style="width:0.22rem;height:0.4rem;"></router-link>
          </td>		
          <td class="title" >
            <span>{{title}} </span>
          </td>
          <td class="icon" @click="goHome"><img src="./../assets/icon_home.png" style="height:0.54rem;width:0.54rem;"></td>
          <td class="icon" @click="showList"><img src="./../assets/icon_list2.png" style="height:0.36rem;"></td>
          <td class="icon" style="width:15px;">&nbsp;</td>
        </tr>
      </tbody></table>
    </div>
    <div id="episode_list_frame">
      <div class="swiper-container-episode swiper-container-vertical swiper-container-autoheight swiper-container-ios" >
        <div class="swiper-wrapper-episode">
          <div class="swiper-slide-episode" v-for="(item,index) in list" :key="index">
            <img src="./../assets/list-1.jpg" @click="showCont(item,index)"><br>
            <span style="line-height:30px;">{{item.title}} </span>
          </div>
        </div>
      </div>
      <div class="swiper-button-prev2 swiper-button" style="top: 15px;"><img src="./../assets/top_on.png" style="width:30px;"></div>
      <div class="swiper-button-next2 swiper-button" style="bottom: 15px;"><img src="./../assets/bottom_on.png" style="width:30px;"></div>
    </div>
    <img v-for="(item,index) in imageList" :key="index" v-lazy="baseUrl + prefix + item" @click="clickCont($event)"/>
    <div class="nav2-down" id="viewer_footer">

	<table style="" align="center">
		<tbody>
      <tr>
      <td width="13%" class="favorite" @click="favorite()">
			  <input type="hidden" id="contents_alert" value="0">
				<img src="./../assets/icon_alert.png" style="height:0.43rem;" id="alerts_img">
			</td>
			<td v-if="lastDocument" width="26%"  class="list_back change_item" @click="goback()" align="left">
			  <img src="./../assets/icon_prev.png" style="height:0.43rem;margin-right:5px;" id="list_back_img">
        前一话			
      </td>      
			<td v-if="!lastDocument" width="26%"  class="list_back change_item disabled" align="left">
			  <img src="./../assets/icon_prev_disable.png" style="height:0.43rem;margin-right:5px;" id="list_back_img">
        前一话			
      </td>
			<td width="22%" class="go_top" @click="goTop">
			  <img src="./../assets/icon_top2.png" style="height:0.18rem;margin-right:6px;" id="list_img">TOP
			</td>
      <td v-if="!nextDocument" width="26%" class="list_forward change_item disabled" align="right">
			下一话<img src="./../assets/icon_next_disable.png" style="height:0.43rem;margin-left:5px;" id="list_next_img">
			</td>			
      <td v-if="nextDocument" width="26%" class="list_forward change_item" @click="goforward" align="right">
			下一话<img src="./../assets/icon_next.png" style="height:0.43rem;margin-left:5px;" id="list_next_img">
			</td>			
			<td width="13%" class="rating" @click="rating">
			<img src="./../assets/icon_starpoint.png" style="height:0.43rem;" id="rating_img">
			</td>
		</tr>
	</tbody></table>
</div>

  <div class="rate-box">
    <Rate allow-half v-model="rate" /><br/>
    <button @click="submitRate">提交</button>
  </div>
   <login-reg-modal @regSuccess="regSuccess" @logSuccess="loginSuccess"></login-reg-modal>
  </div>
</template>

<script>
import Swiper from 'swiper'; 
import 'swiper/dist/css/swiper.min.css';
import loginRegModal from './../components/loginRegModal.vue'
var timer
export default {
  name: 'cartoon-content',
  components:{
    loginRegModal
  },
  props:['id','code'],
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      imageList:[],
      prefix:'',
      title:'',
      baseUrl:BASE_URL,
      list:[],
      swiper_episode:null,
      rate:5,
      lastDocument:null,
      nextDocument:null
    }
  },
  mounted() {
    this.getData()
    this.getList()
    document.getElementById("app").scrollTop = 0
  },
  methods:{
    getData () {
      this.axios.get('/content/cartoon/'+this.id+'/'+this.code+'.json').then(res => {
        this.imageList = res.data.fileList
        this.title = res.data.document.title
        this.prefix = res.data.downloadPrefix
        this.lastDocument = res.data.lastDocument
        this.nextDocument = res.data.nextDocument
      })
    },
     getList () {
      this.axios.get('/content/cartoon/'+this.id+'/index.json').then(res => {
        this.list = res.data.newsList
      })
    },
    goHome () {
      this.$router.push('/')
    },
    showList () {
      $('#episode_list_frame').show();
      
      this.swiper_episode = new Swiper('.swiper-container-episode', {
                              wrapperClass: "swiper-wrapper-episode",
                              slideClass: "swiper-slide-episode",
                              direction: 'vertical',
                              spaceBetween: 10,
                              autoHeight: 'true',
                              height:'100',
                              navigation: {
                                nextEl: '.swiper-button-next2',
                                prevEl: '.swiper-button-prev2',
                              },  
                          });
      if(this.$route.query.index){
        let index = this.$route.query.index
		    this.swiper_episode.slideTo(index,index);
      }else{
        this.swiper_episode.slideTo(0, 0);
       }
    },
    showCont (item,index) {
      if(item.documentDataMap.price == 0 || item.documentDataMap.price > 0 && item.currentStatus == this.code.FREE_STATUS){
        this.$router.push('/content/'+this.id +'/'+item.documentCode+'?index='+index)
        $('#episode_list_frame').hide();
      }else{
        console.log("should buy,login first")
      }
    },
    clickCont () {
      $('#episode_list_frame').hide();
    },
    favorite () {
      if(sessionStorage.getItem("isLogin") == 'true'){        
        console.log('fav')
      }else{
        $("#loginModal").modal('show')
      }
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
    goback () {
      console.log('go back')
      //是否符合条件
      let index = this.$route.query.index - 1
      this.$router.push('/content/'+this.id+'/'+ this.lastDocument.documentCode +'?index=' + index)
    },
    goforward () {
      console.log('go forward')
      //是否符合条件
      let index = parseInt(this.$route.query.index) + 1
      this.$router.push('/content/'+this.id+'/'+ this.nextDocument.documentCode +'?index=' + index)
    },
    rating () {
      if(sessionStorage.getItem("isLogin") == 'true'){        
        $(".rate-box").show()
      }else{
        $("#loginModal").modal('show')
      }
    },
    submitRate () {
      console.log(this.rate*2)
      $(".rate-box").hide()
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
@import './../style/var.scss';
.cartoon-content{  
  min-height: 600px;
  background: #ffffff;
  .rate-box{
    display: none;
    position: fixed;
    bottom: 1.2rem;
    left: 0;
    background-color: #252525;
    width:100%;
    height:1.8rem;
    text-align: center;
    overflow: hidden;
    padding-top: 0.3rem;
    button{
      background: none;
      padding: 0.1rem 0.2rem;
      color: #fff;
      border:#fff solid 1px;
      border-radius: 10px;
      float: right;
      margin-right: 1rem;
      outline: 0;
      -webkit-appearance: none;
    }
  }
  .detail-head{
    position: fixed;
    left: 0;
    z-index: 666;
    width: 100%;
    height: 50px;
    font-size: 20px;
    text-align: center;
    float: center;
    background-color: #252525;
    color: #fff;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.5);
    opacity: 0.95;
    table{      
      width: 100%;
      height:50px;
      td{
        &.icon{
          text-align:left;padding-left:15px;overflow:hidden;width:35px;cursor:pointer;
        }
        &.title{
          text-align:left;padding-left:0px;overflow:hidden;
          &>span{
            color:#fff;font-size:16px;font-weight:600;text-overflow: ellipsis;word-wrap: normal;overflow: hidden;
          }
        }
      }
    }
  }
  #episode_list_frame{
    display: none;
    position: fixed;
    right: 0px;
    top: 0px;
    left: 100%;
    width: 150px;
    margin-left: -150px;
    background-color: rgb(0, 0, 0);
    height: 100%;
    box-shadow: rgba(0, 0, 0, 0.498039) -4px 0px 4px 0px;
    opacity: 1;
    z-index: 999;
    .swiper-container-episode{
      width:140px;margin-top:10px;margin:60px 5px;height: calc(100% - 120px);overflow: hidden;
      .swiper-wrapper-episode{
        transform: translate3d(0px, 0px, 0px);
        .swiper-slide-episode{
          text-align:center;color: rgb(255, 255, 255); position: relative; height: 100px; margin-bottom: 10px;
          &>img{
            width:100%;cursor:pointer;box-sizing:border-box;
          }
          &.swiper-slide-active>img{
            border:2px solid #e33;
          }
        }
      }
    }
    .swiper-button{
      outline:0;position: absolute;width:150px;text-align:center;left:0px;
    }
  }
  #viewer_footer{
    position: fixed;
    left: 0;
    bottom: 0;
    z-index: 666;
    width: 100%;
    height: 50px;
    font-size: 20px;
    text-align: center;
    float: center;
    background-color: #252525;
    color: #fff;
    -webkit-box-shadow: 0px -4px 4px 0px rgba(0, 0, 0, 0.5);
    box-shadow: 0px -4px 4px 0px rgba(0, 0, 0, 0.5);
    opacity: 0.95;
    table{
      width:100%;height:50px;float:center;margin:auto;
      td{
        &.favorite{
          border-right:1px solid #333;text-align:center;cursor:pointer;line-height:30px;color:#fff;font-size:12px;
        }
        &.change_item{
          border-right:1px solid #333;text-align:center;cursor:pointer;line-height:30px;color:#fff;font-size:12px;
          &.disabled{
            color:#4d4d4d
          }
          &.list_forward{
            border-right:none;
            border-left:1px solid #333;
          }
        }
        &.go_top{
          text-align:center;cursor:pointer;line-height:30px;color:#fff;font-size:12px;
        }
        &.rating{
          border-left:1px solid #333;text-align:center;cursor:pointer;line-height:30px;color:#fff;font-size:12px;
        }
      }
    }
  }
  &>img{
    display: block;
    width: 100%;
    height: auto;
  }
}
</style>
