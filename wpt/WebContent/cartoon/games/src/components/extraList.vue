<template>
  <div class="extra-list">
    <div class="name">
      <slot name="name"></slot>
    </div>
    <div class="desc">
      <ul class="list">
          <li  v-for="(item,index) in list" :key="index">
            <div class="delete-icon" @click="deleteHistory(item.nodeId)"><img src="./../assets/recycle.png" ></div>
            <img :src="baseUrl + item.pic"/>
            <div class="right">
              <div class="title one-line">
				■ {{item.name}}
              </div>
              <div class="desc">
                {{item.desc}}
              </div>
              <div class="extra">                
                <font style="color:#e73844">★</font>
                <font style="color:#111">{{item.data.point}}</font>
                <font style="color:#ddd;">|</font> {{item.data.author}}
              </div>
              <div class="img">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>

    </ul>
    </div>
  </div>
</template>

<script>
import Qs from 'qs'

export default {
  name: 'extra-list',
  data () {
    return {
		list:[],
	    baseUrl: PIC_URL
    }
  },
    mounted () {
    this.getList()
  },
  methods:{
    getList () {
      this.isListLoading = true
      this.axios.get('/misc/bookHistory.json').then(res => {
        this.isListLoading = false
        console.log(res.data)
        this.list = res.data.subNodeList
      })
    },
	deleteHistory (nodeId) {
	
		let data =  {
        'nodeId':nodeId
		}
      this.axios({
        method: 'post',
        url: '/misc/deleteHistory.json',
        data: Qs.stringify(data)
      }).then(res => {
        if(res.data.message && res.data.message.operateCode !== this.code.success){
          this.$Message.info(res.data.message.message)
        }
        if(res.data.message && res.data.message.operateCode == this.code.success){
          this.$Message.success('删除成功')
         
        }
      }).catch(e => {

      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
  .extra-list{
    height:100%;
    width:100%;
    .name{
      width:90%;
      font-size: $titleSize;
      color: $fontColor;
      border:#ccc solid 1px;
      border-radius: 5px;
      margin:0.5rem auto;
      line-height:1.8;
      padding:0.25rem;
    }
    .desc{
      .list{
        li{
          overflow:hidden;
          font-size:$titleSize;
          padding:0.2rem;
          border-bottom:1px dashed #ccc;
          position: relative;
          &:last-child{
            border-bottom:0;
          }
          .delete-icon{
             position:absolute;
             text-align:center;
             width:0.8rem;
             height:0.8rem;
             background-color:#fff;
             left:0px;
             top:0px;
             line-height:0.3rem;
             color:#111;
             font-weight:800;
             border-radius:0px 0px 10px 0px;
             font-size:$fontSize;
             padding-top:4px;
             z-index:1;
          }
          &>img{
            width:2.7rem;
            height:2rem;
            border-radius: 10px;
            border-top-right-radius: 0;
            display:block;
            float:left;
          }
          .right{
            float:left;
            padding-left:0.2rem;
            .one-line{
              width: 3rem;
              padding-right: 0.4rem;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              font-weight: bold;
            }
            .img{
              &>img{
                width:0.6rem;
                height:auto;
                margin-top:0.25rem;
              }
            }
          }
          .btn{
            padding:0.2rem;
            background:$blackBgBtnColor;
            color:#ffffff;
            position:absolute;
            right:0.2rem;
            bottom:0.2rem;
            border-radius:10px;
          }
        }
      }
    }
  }
</style>
