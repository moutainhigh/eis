<template>
  <div class="favorite">
    <extra-list>
      <span slot="name">加入兴趣清单后，即可收到该作品的更新或相关活动通知</span>
    </extra-list>
  </div>
</template>

<script>
import extraList from './../components/extraList.vue';
export default {
  name: 'favorite',
  components:{
    extraList
  },
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>

</style>
