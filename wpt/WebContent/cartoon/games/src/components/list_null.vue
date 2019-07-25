<template>
  <div class="list-null">
    <div class="name">
      <slot name="name"></slot>
    </div>
    <img src="./../assets/box.png">
    <div class="desc">
      <slot name="desc"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'list-null',
  data () {
    return {
      
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
  .list-null{
    height:100%;
    width:100%;
    .name{
      width:90%;
      font-size: $titleSize;
      color: $fontColor;
      border:#ccc solid 1px;
      border-radius: 5px;
      margin:0.5rem auto;
      line-height:1.8;
      padding:0.25rem;
    }
    img{
      display:block;
      margin:1.5rem auto;
      margin-bottom:0.3rem;
    }
    .desc{
      text-align:center;
      margin-bottom:0.5rem;
    }
  }
</style>
