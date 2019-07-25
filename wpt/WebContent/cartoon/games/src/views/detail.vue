<template>
  <div class="detail">
    <img :src="picUrl + node.pic" v-if="node.pic" class="detail-pic"/>
    <div class="btns">
      <span class="btn-free">第一话免费</span>
      <span class="btn-share">分享</span>
    </div>
    <div class="title">
      {{node.name}}<img src="./../assets/recommend.jpg"/>
    </div>
    <div class="desc">
      <font style="color:#555;">作者:{{node.data.author}}</font>
      <font style="color:#ddd;">|</font>
      <font style="color:#3ab4c0">观看次数:{{node.data.readCount}}</font>
    </div>
    <div class="extra">
      <font style="color:#ddd;">|</font>&nbsp;
      <font style="color:#e73844">★</font>
      <font style="color:#111">{{node.data.point}}</font>
    </div><br/>
    <div class="tips">{{node.brief}},{{node.name}}，歡迎你的光臨～</div>
    <img class="charge" src="./../assets/charge.jpg"/>
    <div class="head" v-if="list.length > 0">章节列表</div>
    <ul class="list">
      <li v-for="(item,index) in list" :key="index">
          <img src="./../assets/list-1.jpg" class="pic"/>
          <div class="right">
            <p class="title">{{item.title}}</p>
            <p class="gray">{{item.content}}</p>
            <p class="gray">{{item.publishTime}}</p>
          </div>          
          <router-link :to="'/content/'+id +'/'+item.documentCode+'?index='+index"><div class="btn-free" v-if="item.documentDataMap.price == 0">免费</div></router-link>
          <router-link :to="'/content/'+id +'/'+item.documentCode+'?index='+index"><div class="btn-pay" v-if="item.documentDataMap.price > 0 && item.currentStatus == FREE_STATUS">观看</div></router-link>
          <div class="btn-pay" v-if="item.documentDataMap.price > 0 && item.currentStatus == BUY_STATUS">购买</div>
        
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'detail',
  components:{
    
  },
  props:['id'],
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      node:{
        data:{
          author:'',
          point:'',
          readCount:''
        }
      },
      picUrl:PIC_URL,
      list:[],
      FREE_STATUS:FREE_STATUS,
      BUY_STATUS:BUY_STATUS
    }
  },
  mounted() {
    this.getData()
  },
  methods:{
    getData () {
      this.axios.get('/content/cartoon/'+this.id+'/index.json').then(res => {
        this.node = res.data.node
        this.$set(this.node,'data',res.data.node.data)
        this.node.data.readCount = parseInt(res.data.node.data.readCount).toLocaleString()
        this.list = res.data.newsList
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
.detail{
  .btn{
    padding:0.2rem 0.3rem;
    outline: 0;
    &.btn-default{
      background: #ccc;
    }
  }
  .detail-pic{
    width:100%;
    height:2.8rem;
  }
  .btns{
    display:flex;
    flex-direction: row;
    justify-content: space-around;
    font-size: $titleSize;
    .btn-free{
      display:inline-block;
      height:0.8rem;
      width: 65%;
      border: $mainColor solid 1px;
      border-radius:5px;
      line-height:0.8rem;
      text-align: center;
      margin:0.2rem 0;
      color:$mainColor;
    }
    .btn-share{
      display:inline-block;
      height:.8rem;
      width:27%;
      border:1px solid #2556a0;
      color:#2556a0;
      line-height:0.8rem;
      text-align:center;
      margin: 0.2rem;
      border-radius: 5px;
    }
  }
  .title{
    font-weight: bold;
    font-size: $titleSize;
    padding-left: 0.2rem;
    img{
      vertical-align: text-top;
      height:0.35rem;
      width:auto;
      margin-left:0.15rem;
    }
  }
  .desc{
    font-size: $titleSize;
    padding-left: 0.2rem;
  }
  .extra{
    font-size: $titleSize;
    padding-left: 0.2rem;
  }
  .tips{
    font-size:12px;
    padding-left:0.2rem;
    color:#888;
    margin-top:0.2rem;
  }
  .charge{
    display: block;
    width: 100%;
    height: auto;
    margin: 0.2rem 0;
  }
  .head{
    height: 0.8rem;
    line-height: 0.8rem;
    border-bottom: $blackBgColor solid 1px;
    padding-left: 0.2rem;
    font-weight: bold;
    font-size: $titleSize;
  }
  .list{
      li{
        padding:0.2rem;
        overflow: hidden;
        position: relative;
        border-bottom: #ddd solid 1px;
        .pic{
          display: block;
          float: left;
          width: 3rem;
          height:1.5rem;
        }
        .right{
          float: left;
          .title{
            font-size: $titleSize;
            color: #333;
          }
          .gray{
            padding-left: 0.2rem;
            color: $fontColor;
            font-size: $fontSize;
          }
        }
        .btn-free{
          display: inline-block;
          position: absolute;
          top: 0.3rem;
          right: 0.4rem;
          border-radius: 5px;
          padding: 0.1rem;
          font-size: $fontSize;
          color: $mainColor;
          border: $mainColor 1px solid;
        }
        .btn-pay{
          display: inline-block;
          position: absolute;
          top: 0.3rem;
          right: 0.4rem;
          border-radius: 5px;
          padding: 0.1rem;
          font-size: $fontSize;
          color: $blackBgColor;
          border: $fontColor 1px solid;
        }
      }
  }
}
</style>
