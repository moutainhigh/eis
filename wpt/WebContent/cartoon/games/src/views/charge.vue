<template>
  <div class="charge">
      <img src="./../assets/charge.jpg" class="banner"/>
      <Tabs value="rmb">
        <TabPane label="RMB" name="rmb">  
          <charge-method></charge-method>
        </TabPane>
        <TabPane label="USD" name="usd">
          <charge-method></charge-method>
        </TabPane>
    </Tabs>
  </div>
</template>

<script>
import chargeMethod from './../components/chargeMethod.vue'
export default {
  name: 'change',
  components:{
    chargeMethod
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  mounted() {
    
  }
}
</script>
<style lang="scss">
@import './../style/var.scss';
  .charge{
    .ivu-tabs-bar {
      border-bottom: 2px solid $mainColor;
      padding: 0.1rem 0.1rem 0;
      margin-bottom: 0.1rem;
      background: #e8ecf0;
    }
    .ivu-tabs-nav-container {
      line-height: 2;
    }
    .ivu-tabs-ink-bar{
      height: 0;
    }
    .ivu-tabs-nav .ivu-tabs-tab-active {
      color: #fff;
      background: $mainColor;
    }
  }
</style>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>  
@import './../style/var.scss';
.charge{
  background: #e8ecf0;
  .banner{
    width:100%;
    height:auto;
  }
  .title{
    background: #fff9d6;
    padding:0 0.5rem;
    line-height:0.8rem;
    .bold{
      font-weight:bold;
      padding-right:0.2rem;
    }
  }
}
</style>
