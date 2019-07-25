<template>
  <div class="gift">
    <div class="title">
      <img src="./../assets/icon_freetoon_gift.png">
      <span class="title-text">礼物箱</span>
      <span class="info">您可在此页面中确认所收到的礼物</span>
    </div>
    <div class="subtitle">
      <img src="./../assets/icon_todaygift.png">
      <span class="title-text">今日的礼物</span>
    </div>
    <ul class="list">
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
								■ 他的高跟鞋
              </div>
              <div class="desc">
                19小时后结束
              </div>
              <div class="extra">
                剩余两张租借券
              </div>
            </div>
            <span class="btn">马上使用</span>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
								■ 他的高跟鞋
              </div>
              <div class="desc">
                19小时后结束
              </div>
              <div class="extra">
                剩余两张租借券
              </div>
            </div>
            <span class="btn">马上使用</span>
          </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'gift',
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
.gift{
  &>.title{
    padding:0.4rem 0.2rem;    
    margin:0 0.2rem;
    img{
      width:0.4rem;
      height:auto;
      vertical-align: text-bottom;
    }
    .title-text{
      font-size:$titleSize;
      font-weight: bold;
    }
    .info{
      font-size:$fontSize;
      color:#666;
      padding-left:0.5rem;
    }
  }
  &>.subtitle{
    padding:0.3rem 0.2rem;
    margin:0 0.2rem;
    border-radius: 10px;
    background: #faeece;
    img{
      width:0.4rem;
      height:auto;
      vertical-align: text-bottom;
    }
    .title-text{
      font-size:$titleSize;
      font-weight: bold;
    }    
  }
  .list{
      li{
          overflow:hidden;
          font-size:$titleSize;
          padding:0.2rem;
          border-bottom:1px dashed #ccc;
          position: relative;
          &:last-child{
            border-bottom:0;
          }
          &>img{
            width:2.7rem;
            height:1.6rem;
            border-radius: 10px;
            border-top-right-radius: 0;
            display:block;
            float:left;
          }
          .right{
            float:left;
            padding-left:0.2rem;
            .one-line{
              width: 3rem;
              padding-right: 0.4rem;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              font-weight: bold;
            }
          }
          .extra{
            color: $mainColor;
            font-weight:bold;
          }
          .btn{
            padding:0.2rem;
            background:$blackBgBtnColor;
            color:#ffffff;
            position:absolute;
            right:0.2rem;
            bottom:0.2rem;
            border-radius:10px;
          }
        }
    }
}
</style>
