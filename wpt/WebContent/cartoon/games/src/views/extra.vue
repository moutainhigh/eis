<template>
  <div class="hello">
    <div class="nav">
      <ul>
        <li><router-link to="/extra">礼物</router-link></li>
        <li><router-link to="/extra/library">书柜</router-link></li>
        <li><router-link to="/extra/favorite">有兴趣</router-link></li>
        <li><router-link to="/extra/recent">浏览记录</router-link></li>
      </ul>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
.nav{
    height:0.9rem;
    line-height:0.9rem;
    position:relative;
    z-index:10;
    ul{
      overflow:hidden;
      background:#f2f2f2;
      li{
        width:20%;
        text-align:center;
        color:$fontColor;
        font-size: $titleSize;
        float:left;
        a{
          color:$fontColor;
          display: block;
          font-size:$fontSize;
          &.router-link-exact-active{
            color:$mainColor;
            background:#ffffff;
          }
        }        
        &:nth-child(3),&:nth-child(4){
          width:30%;
        }
      }
    }
  }
</style>
