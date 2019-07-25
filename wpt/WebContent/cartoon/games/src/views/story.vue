<template>
  <div class="story">
    <Tabs value="name1">
        <TabPane label="周一" name="name1">          
          <inner-list-scroll filter="story" sort="mon"></inner-list-scroll>
        </TabPane>
        <TabPane label="周二" name="name2">
          <inner-list-scroll filter="story" sort="tue"></inner-list-scroll>
        </TabPane>
        <TabPane label="周三" name="name3">
          <inner-list-scroll filter="story" sort="wed"></inner-list-scroll>
        </TabPane>
        <TabPane label="周四" name="name4">
          <inner-list-scroll filter="story" sort="thu"></inner-list-scroll>
        </TabPane>
        <TabPane label="周五" name="name5">
          <inner-list-scroll filter="story" sort="fri"></inner-list-scroll>
        </TabPane>
        <TabPane label="周六" name="name6">
          <inner-list-scroll filter="story" sort="sat"></inner-list-scroll>
        </TabPane>
        <TabPane label="周日" name="name7">
          <inner-list-scroll filter="story" sort="sun"></inner-list-scroll>
        </TabPane>
    </Tabs>
  </div>
</template>

<script>
import innerListScroll from './../components/innerListScroll.vue'
export default {
  name: 'story',
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
  .story .ivu-tabs-nav .ivu-tabs-tab{
    padding:10px 16px;
  }
</style>
<style scoped>

</style>
