<template>
  <div class="recent">
    <extra-list>
      <span slot="name">最近观看的50部作品已依顺序排列显示，此记录将保存120日。</span>
    </extra-list>
  </div>
</template>

<script>
import extraList from './../components/extraList.vue';
export default {
  name: 'recent',
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

  