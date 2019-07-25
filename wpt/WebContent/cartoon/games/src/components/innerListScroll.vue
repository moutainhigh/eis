<template>
  <div class="inner-list"
      :class="{'down':(state===0),'up':(state==1),refresh:(state===2),touch:touching}"
      @touchstart.passive="touchStart($event)"
      @touchmove.passive="touchMove($event)"
      @touchend.passive="touchEnd($event)"
      >
      <div class="pull-refresh" v-show="refreshShow">
        <Spin>
          <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
          <div class="up-tip tips">松开更新</div>
          <div class="down-tip tips">下拉更新</div>
          <div class="refresh-tip tips">更新中...</div>
        </Spin>
      </div>
      <ul id="list" class="list" ref="list" v-show="list.length !== 0"
      @scroll.passive="listScroll" :style="{ transform: 'translate3d(0, ' + top + 'px, 0)' ,paddingBottom:paddingBottom+'px'}">
          <li v-for="(item,index) in list" :key="index">
            <router-link :to="item.path">
              <img :src="baseUrl + item.pic"/>
              <div class="bottom">
                <div class="title one-line" style="color:#111">
                  {{item.name}}
                </div>
                <div class="desc">
                  <font style="color:#e73844">★</font> 
                  <font style="color:#111">9.35</font>
                  <font style="color:#eee"> &nbsp;|&nbsp; </font>
                  <font style="color:#444">后记</font>
                </div>
                <div class="free" v-show="type==='free'"> 
                  <span style="color: #e11;font-size:13px;">4回免费</span>
                </div>
                <div class="1coin" v-show="type==='1coin'"> 
                  <span style="color: #999;text-decoration: line-through;font-size: 12px;">2点券</span>
                  <span style="color: #e11;font-size:14px;">1点券</span>
                </div>
                <div class="extra">
                  <img src="./../assets/recommend.jpg"/>
                </div>
              </div>
            </router-link>
          </li>          
          <div class="no-more" v-show="nomore">~~~没有更多了~~~</div>
      </ul>      
    <div v-show="list.length === 0">
      <list-null>
        <span slot="name">
          作品目录
        </span>
        <span slot="desc">
          无内容
        </span>
      </list-null>
    </div>
    <Spin fix v-show="isListLoading">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin> 
    <Spin v-show="isLoadingMore">
      <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
      <div>Loading</div>
    </Spin> 
  </div>
</template>

<script>
import listNull from './list_null.vue'
export default {
  name: 'inner-list',
  components:{
    listNull
  },
  props:{
    offset: {
      type: Number,
      default: 30
    },
    enableInfinite: {
      type: Boolean,
      default: true
    },
    enableRefresh: {
      type: Boolean,
      default: true
    },
    type:{
      type:String,
      default:undefined
    },
    filter:{
      type:String,
      default:undefined
    },
    sort:{
      type:String,
      default:undefined
    }
  },
  data () {
    return {
      list:[],
      isListLoading:false,
      baseUrl: PIC_URL,
      isLoadingMore:false,
      scrollIndex:1,
      nomore:false,
      top: 0,
      state: 0,
      startY: 0,  //保存开始滑动时，y轴位置
      touching: false,
      refreshShow: false
    }
  },
  watch:{
    isLoadingMore:{
      handler(val,oldVal){
        if(val === true && oldVal === false){          
          if(this.nomore){
            this.isLoadingMore = false
            return false
          }
          this.debounce(2000,() => {   
            this.scrollIndex++
            this.getList(false)
          })()
        }
      },
      immediate:true,
      deep:true
    },    
    state:{
      handler(val,oldVal){
        if((val === 2 && oldVal === 1)||(val === 2 && oldVal === 0)){          
          this.debounce(500,() => {   
            this.scrollIndex = 1
            this.getList(false)
          })()
        }
      },
      immediate:true,
      deep:true
    }
  },
  computed:{
    paddingBottom(){
      return this.top + 10
    }
  },
  mounted () {
    this.getList(true)
  },
  methods:{
    getList (flag) {
      if(flag){
        this.isListLoading = true
      }
      let options = {}
      options.page = this.scrollIndex
      options.rows = 9
      if(this.filter !== undefined) {
        options.filter = this.filter
      }
      if(this.sort !== undefined) {
        options.sort = this.sort
      }
      this.axios.get('/content/cartoon/index.json',{
        params:options
      }).then(res => {
        if(flag){
         this.isListLoading = false
        }
        if(res.data.subNodeList.length > 0 && this.state == 2){          
          this.list = res.data.subNodeList
        }else{
          this.list = this.list.concat(res.data.subNodeList)
        }
        if(res.data.totalPages <= this.scrollIndex && this.isLoadingMore == true){
          this.nomore = true
        }else{
          this.nomore = false
        }
        this.isLoadingMore = false
        this.refreshShow = false
        this.state = 0
        this.top = 0
      }).catch(e => {

      })
    },
    listScroll () {
      if(!this.enableInfinite){
        return false
      }
      var scrollHeight =this.$refs.list.scrollHeight;
      var scrollTop = this.$refs.list.scrollTop;
      var clientHeight = this.$refs.list.clientHeight;
      if(clientHeight + scrollTop + 1 >= scrollHeight){
        this.isLoadingMore = true          
      }
      if(scrollTop <= 1){
        this.isFresh = true
      }
    },
    debounce (idle, func) {
      var last
      return function () {
        clearTimeout(last)
        last = setTimeout(() => {
          func()
        },idle)
      }
    },
    touchStart(e) {
      if(!this.enableRefresh) return
      this.startY = e.targetTouches[0].pageY
      this.startScroll = this.$el.scrollTop || 0
      this.touching = true
    },
    touchMove(e) {
      if (!this.enableRefresh || this.$el.scrollTop > 0 || !this.touching) {
        return
      }
      let diff = e.targetTouches[0].pageY - this.startY - this.startScroll
      if (diff > 0 ) {     
        this.refreshShow = true
      }
      this.top = Math.pow(diff, 0.8) + (this.state === 2 ? this.offset : 0)
      if (this.state === 2) { 
        return
      }
      if (this.top >= this.offset) {
        this.state = 1
      } else {
        this.state = 0
      }
    },
    touchEnd(e) {
      if (!this.enableRefresh) return
      this.touching = false
      if (this.state === 2) { // in refreshing
        this.state = 2
        this.top = this.offset
        return
      }
      if (this.top >= this.offset) { // do refresh        
        this.state = 2
        this.top = this.offset
        this.refreshShow = true
      } else { // cancel refresh
        this.state = 0
        this.top = 0
        this.refreshShow = false
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
  .inner-list{
    min-height:400px;
    .pull-refresh{
      .tips{
        display: none;
      }
    }
    &.up .up-tip{
      display:block;
    }
    &.down .down-tip{
      display: block;
    } 
    &.refresh .refresh-tip{
      display: block;
    } 
    ul{
      padding:0;
      margin:0;
      width:100%;
      overflow: hidden;
      height:600px;
      overflow-y: auto;
      -webkit-overflow-scrolling: touch;
      li{
        width:33.3%;
        float:left;
        padding:1%;
        a{
          display: block;
          &>img{
            width:98%;
            height:3.2rem;
            border-bottom-left-radius:8px;
            border-bottom-right-radius:8px;
          }
          .bottom{
              .one-line{
                width: 3rem;
                padding-right: 0.4rem;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
          }
          .extra{
              &>img{
                width:0.56rem;
                height:0.36rem;
              }
          }
        }
        &:nth-child(3n + 0)::after{
          content:'';
          clear:both;
        }
      }
    }
    .no-more{
      color: $fontColor;
      font-size: $fontSize;
      text-align: center;
      line-height: 0.7rem;
    }
  }
</style>
