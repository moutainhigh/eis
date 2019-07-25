<template>
  <div class="home">
    <Carousel
          dots="inside" autoplay loop :radius-dot="true">
          <CarouselItem>
            <div class="demo-carousel">
              <img src="./../assets/banner-1.jpg"/>
            </div>
          </CarouselItem>
          <CarouselItem>
              <div class="demo-carousel">
                <img src="./../assets/banner-2.jpg"/>
              </div>
          </CarouselItem>
          <CarouselItem>
              <div class="demo-carousel">
                <img src="./../assets/banner-3.jpg"/>
              </div>
          </CarouselItem>
    </Carousel>
    <div class="nav">
      <span>热门排行</span>
      <ul>
        <li :class="{'active':activeIndex===0}" @click="goTab(0)">漫画排行</li>
        <li :class="{'active':activeIndex===1}" @click="goTab(1)">完结排行</li>
        <li :class="{'active':activeIndex===2}" @click="goTab(2)">新作排行</li>
      </ul>
    </div>
    <div class="cont" :style="{'marginLeft':contMarginLeft}">
      <div class="item item-1">
        <ul class="list">
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
        </ul>
        <img src="./../assets/gradation_w.png" height="100%" width="50" class="gradation_w" :class="{'block':(activeIndex+1)%3 == 0}">
      </div>
      <div class="item item-2">
        <ul class="list">
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
        </ul>
        <img src="./../assets/gradation_w.png" height="100%" width="50" class="gradation_w" :class="{'block':(activeIndex+1)%3 == 1}">
      </div>
      <div class="item item-3">
        <ul class="list">
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
          <li>
            <img src="./../assets/list-1.jpg"/>
            <div class="right">
              <div class="title one-line">
                深夜便利店
              </div>
              <div class="desc">
                <font style="color:#e73844">★</font> 
                <font style="color:#111">9.35</font>
                <font style="color:#eee"> &nbsp;|&nbsp; </font>
                <font style="color:#444">后记</font>
              </div>
              <div class="extra">
                <img src="./../assets/recommend.jpg"/>
              </div>
            </div>
          </li>
        </ul>
        <img src="./../assets/gradation_w.png" height="100%" width="50" class="gradation_w" :class="{'block':(activeIndex+1)%3 == 2}">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      activeIndex:0,
      contMarginLeft:0
    }
  },
  methods:{
    goTab(index){
      this.activeIndex = index
      this.contMarginLeft = -1*index * 6.6 + 'rem'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
.home{
  .demo-carousel{
    img{
      width:100%;
      height:auto;
    }
  }
  .nav{
    height:0.8rem;
    line-height:0.8rem;
    font-size:$titleSize;
    color:$fontColor;
    overflow:hidden;
    span{
      font-weight: bold;
      padding-left:0.2rem;
    }
    ul{
      float:right;
      overflow:hidden;
      height:0.7rem;
      li{
        padding:0 0.25rem;
        height:0.56rem;
        line-height:0.56rem;
        margin-top:0.07rem;
        border-top:#f4f4f4 solid 1px;
        border-bottom:#f4f4f4 solid 1px;
        border-right:#f4f4f4 solid 1px;
        float:left;
        :last{
          border-right:0;
        }
        &.active{
          background:#eeeeee;
        }
      }
    }
  }
  .cont{
    overflow: hidden;
    width:19.8rem;
    transition:margin-left 0.3s ease-in-out;
    .item{
      width:6.6rem;
      float: left;
      position:relative;
      padding-left:0.05rem;
      ul{
        li{
          overflow:hidden;
          font-size:$titleSize;
          padding-bottom:0.05rem;
          padding-top:0.05rem;
          border-bottom:1px dashed #ccc;
          &:first-child{
            padding-top:0;
          }
          &:last-child{
            border-bottom:0;
          }
          &>img{
            width:2.7rem;
            height:1.6rem;
            border-radius: 10px;
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
            }
          }
          .extra{
            &>img{
              width:0.56rem;
              height:0.36rem;
            }
          }
        }
      }
      .gradation_w{
        height: 8.5rem;
        position: absolute;
        left: 0;
        top: 0;
        display:none;
        &.block{
          display: block !important;
        }
      }
    }
  }
}
</style>
