<template>
  <div class="range">
    <Tabs value="name1">
        <TabPane label="人气" name="name1">          
          <inner-list-scroll filter="rank" sort="hot"></inner-list-scroll>
        </TabPane>
        <TabPane label="新作" name="name2">
          <inner-list-scroll filter="rank" sort="recent"></inner-list-scroll>
        </TabPane>
        <TabPane label="周排名" name="name3">
          <inner-list-scroll filter="rank" sort="week"></inner-list-scroll>
        </TabPane>
        <TabPane label="月排名" name="name4">
          <inner-list-scroll filter="rank" sort="month"></inner-list-scroll>
        </TabPane>
    </Tabs>
  </div>
</template>

<script>
import innerListScroll from './../components/innerListScroll.vue'
export default {
  name: 'rank',
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
  .range .ivu-tabs-nav .ivu-tabs-tab{
    padding:10px 33px;
  }
</style>
<style scoped>

</style>
