<template>
  <div class="finished">
    <Tabs value="name1">
        <TabPane label="最高销量排列" name="name1">          
          <inner-list-scroll filter="finished" sort="top"></inner-list-scroll>
        </TabPane>
        <TabPane label="最新完结作" name="name2">
          <inner-list-scroll  filter="finished" sort="recent"></inner-list-scroll>
        </TabPane>
        <TabPane label="一点券专区" name="name3"> 
          <inner-list-scroll filter="finished" sort="1coin"></inner-list-scroll>
        </TabPane>
        <TabPane label="观看全部" name="name4">
          <inner-list-scroll filter="finished" sort="all"></inner-list-scroll>
        </TabPane>
        <TabPane label="单行本" name="name5">
          <inner-list-scroll filter="finished" sort="single"></inner-list-scroll>
        </TabPane>
    </Tabs>
  </div>
</template>

<script>
import innerListScroll from './../components/innerListScroll.vue'
export default {
  name: 'finished',
  components:{
    innerListScroll
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
  .finished .ivu-tabs-nav .ivu-tabs-tab{
    padding:10px 12px;
  }
</style>
<style scoped>

</style>
