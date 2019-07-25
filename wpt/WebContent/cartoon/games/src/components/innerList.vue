<template>
  <div class="inner-list">
    <ul class="list">
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
  </div>
</template>

<script>
import listNull from './list_null.vue'
export default {
  name: 'inner-list',
  components:{
    listNull
  },
  props:['type'],
  data () {
    return {
      list:[],
      isListLoading:false,
	    baseUrl: PIC_URL
    }
  },
  mounted () {
    this.getList()
  },
  methods:{
    getList () {
      this.isListLoading = true
      this.axios.get('/content/cartoon/index.json').then(res => {
        this.isListLoading = false
        console.log(res.data)
        this.list = res.data.subNodeList
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
  .inner-list{
    min-height:400px;
    ul{
      padding:0;
      margin:0;
      width:100%;
      overflow: hidden;
      li{
        width:33.3%;
        float:left;
        padding:1%;
        a{
          display: block;
          &>img{
            width:98%;
            height:auto;
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
  }
</style>
