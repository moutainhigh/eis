<template>
  <div class="library">
    <div v-show="list.length === 0">
      <list-null>
        <span slot="name">
          您所收藏的作品目录
        </span>
        <span slot="desc">
          无收藏内容
        </span>
      </list-null>
    </div>
  </div>
</template>

<script>
import listNull from './../components/list_null.vue'
export default {
  name: 'library',
  components:{
    listNull
  },
  data () {
    return {
      list: []
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
