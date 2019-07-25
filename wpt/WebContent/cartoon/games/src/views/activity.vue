<template>
  <div class="activity">
    <router-link to="/extra"><img src ="./../assets/gift-banner.jpg"></router-link>
    <router-link to="/charge"><img src ="./../assets/charge-banner.jpg"></router-link>
  </div>
</template>

<script>
export default {
  name: 'activity',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.activity{
  &>a{
    display: inline-block;
    width:100%;
    &>img{
      width:100%;
      height: auto;
    }
  }
}
</style>
