<template>
<div class="modal fade" :id="name" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <img src="./../assets/modal-img.jpg" class="modal-img" v-show="hasImg"/>
            <div class="modal-header">
                <slot name="header"></slot> 
            </div>
            <div class="modal-body">
              <slot name="body">
              </slot>
            </div>
            <div class="modal-footer">
              <slot name="btns"></slot>
            </div>
            <slot name="extra"></slot>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</template>

<script>
export default {
  name: 'bsmodal',
  props:["name","hasImg"],
  data () {
    return {
      
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
.modal{
  .modal-content{
    overflow:hidden;
    .modal-img{
      width:100%;
    }
    .modal-header{
      padding:0.3rem;
      display: block;
      h4{
        font-size:$titleSize;
        float:left;
        &:before{
          height: 0.5rem;
          width: 0.1rem;
          background: #ccc;
          content: '';
          display: block;
          float: left;
          margin-right: 0.2rem;
        }
      }
      .title-btn{
        float: right;
        margin-right: 0.1rem;
        border: 1px solid #999;
        color: #555;
        font-size: $fontSize;
        padding: 3px 2px;
        border-radius: 10px;
        font-weight: 500;
        cursor: pointer;
        width: 1.8rem;
        text-align: center;
      }
      .close{
        font-size:0.5rem;
        padding: 0.05rem;
        margin: 0;
      }
    }
    .modal-body{
      padding:0;
      .input-box{
        width:100%;
        position:relative;
        background-color:#f8f8f8;
        padding:5px 15px;
        .input-style{
          color: #492540;
          border: 0px;
          height: 40px;
          line-height: 40px;
          font-size: 18px;
          border-bottom: 1px solid #a2909e;
          background-color: #f8f8f8;
          padding: 0rem 0.3rem;
          width: 100%;
          outline:0;
        }
      }   
      .checkboxes{
        width:100%;
        position:relative;
        height:0.6rem;
        padding:0.2rem;
        text-align: right;
        input[type="checkbox"]{
          vertical-align: text-bottom;
        }
      } 
    }
    .modal-footer{
      padding:0.3rem;
      .btn-modal{
        background-color: #592076;
        color: #fff;
        font-size: 0.36rem;
        padding: 0.2rem 2rem;
        border-radius: 0.4rem;
        font-weight: 500;
        margin: 0.1rem;
        cursor: pointer;
        width: 100%;
      }
    }
    .extra-text{
      width:100%;
      position:relative;
      margin:0.3rem 0rem;
      height:1rem;
      font-size:$fontSize; 
    }
    .forget-password{
      width:100%;
      position:relative;
      font-size:12px;
      background-color:#f9f9f9;
      padding:0.3rem 0rem;
      margin-top:0.5rem;
      text-align: center;
    }
  }
}
</style>
