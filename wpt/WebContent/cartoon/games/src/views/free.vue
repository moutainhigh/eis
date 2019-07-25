<template>
  <div class="free">
    <div class="title">
      &nbsp;&nbsp;
      <img src="./../assets/new18_icon_freetoon_recommend.png" align="absmiddle" style="width:15px;">&nbsp;&nbsp;
      <b>免费漫画推荐</b> 
			&nbsp;&nbsp;
			<span>PD推荐免费作品</span>
    </div>
    <img src="./../assets/free-read.jpg" class="free-read">
    <div class="title">
      &nbsp;&nbsp;
      <img src="./../assets/new18_icon_freetoon_1coin.png" align="absmiddle" style="width:15px;">&nbsp;&nbsp;
      <b>一点券特价专区</b> 
			&nbsp;&nbsp;
			<span>每周更新的名作优惠区</span>
    </div>
    <inner-list type="1coin"></inner-list>     
    <div class="title">
      &nbsp;&nbsp;
      <img src="./../assets/icon_freetoon_free.png" align="absmiddle" style="width:15px;">&nbsp;&nbsp;
      <b>免费</b> 
			&nbsp;&nbsp;
			<span>免费体验多种漫画</span>
    </div>
    <inner-list type="free"></inner-list>         
  </div>
</template>

<script>
import innerList from './../components/innerList.vue'
export default {
  name: 'free',
  components:{
    innerList
  },
  data () {
    return {
      
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->

<style lang="scss">
@import './../style/var.scss';
  .ivu-tabs-nav-scroll{
    background:#f2f2f2;    
    .ivu-tabs-ink-bar{
      height:0;
    }
  }
  .ivu-tabs-nav .ivu-tabs-tab-active{
    background:#ffffff;
    color:$mainColor;
  }
  .ivu-tabs-nav .ivu-tabs-tab{
    margin-right:0;
    font-size: $fontSize;
  }
  .ivu-tabs-nav .ivu-tabs-tab:hover {
    color: $mainColor;
  }
  .range .ivu-tabs-nav .ivu-tabs-tab{
    padding:10px 33px;
  }
</style>
<style scoped lang="scss">
@import './../style/var.scss';
  .title{
    text-align:left;
    line-height:0.6rem;
    font-size:$fontSize;
    padding:0.2rem 0px 0.1rem 0.2rem;
    color:#000;
    &>span{
      color:#666;
    }
  }
  .free-read{
    width:100%;
    height:auto;
  }
</style>
