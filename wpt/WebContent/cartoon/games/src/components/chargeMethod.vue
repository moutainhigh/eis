<template>
  <div class="charge-method">
          <div class="title">
            重度使用者专用方案
          </div>        
          <charge-table></charge-table>            
          <div class="title">
            <span class="bold">一般储值方案</span>
            <span>高cp值储值方案</span>
          </div>                  
          <charge-table :with-header="true"></charge-table>
          <div class="charge-methods">
            <div class="head">
              请选择充值方式
            </div>
            <div class="cont">
              <table style="width:100%;">
                <tr style="cursor:pointer;">
                  <td style="text-align:left;padding:4px 0px 0px 0px;width:50%;">
                  <div style="background: #fcfbfb;border: 1px solid #ddd;text-align:center;padding:8px 0px;" >
                    <img src="./../assets/icon_creditcard_re.png" style="height:39px;margin-bottom:3px;"> 信用卡(台湾)	</div>
                  </td>
                  <td style="text-align:left;padding:4px 0px 0px 0px;width:50%;">
                    <div style="background: #fcfbfb;border: 1px solid #ddd;text-align:center;padding:8px 0px;" >
                      <img src="./../assets/icon_phone_re.png?temp=1" style="height:39px;margin-bottom:3px;"> 手机结算 
                    </div>
                  </td>
                </tr>
                <tr style="cursor:pointer;">
                  <td style="text-align:left;padding:0px 0px 0px 0px;width:50%;">
                    
                  <div style="background: #fcfbfb;border: 1px solid #ddd;text-align:center;height:50px;vertical-align: middle;" >
                    <img src="./../assets/_Mastercard-512.png" style="height:20px;margin:15px 0px;"><img src="./../assets/_Visa-512.png" style="height:20px;margin:15px 0px;">
                  </div>
                  </td>
                  <td style="text-align:left;padding:0px 0px 0px 0px;width:50%;">
                  <div style="background: #fcfbfb;border: 1px solid #ddd;text-align:center;height:50px;vertical-align: middle;" >
                    <img src="./../assets/unionpay.png" style="height:30px;margin:10px 0px;">
                  </div>
                  </td>
                </tr>
              </table>
            </div>
						<div class="head">
							付款注意事项
						</div>
						<div style="position:relative;text-align:left;padding:15px;line-height:25px;background:#ffffff" align="center">
							1. 选择线下ATM付费者，请在下单后记下页面上显示的银行代号(BankNo)与汇款账号(VirtualAccount)，前往各地的『ATM柜员机』或『银行柜台』完成转账手续。<br>
							若使用ATM转账或超商代码付款，系统会在付款后的几分钟內充值点券至您的账号。<br>
							2. 超商代码缴费流程:<br>
							在充值页面选择金额后选择"超商代码"-&gt;选择超商-&gt;充值点券,系统会产生一组"缴费代码"<br>
							再前往您选择的超商： 全家FamiPort选择"缴费"-&gt;"代码缴费"-&gt;"输入系统产生之代码"<br>
							<br>
							OK-go选择"缴费"-&gt;"代码缴费"-&gt;"输入系统产生之代码"<br>
							莱尔富Life-ET选择"代收缴款"-&gt;"网路交易"-&gt;"万付通"-&gt;"输入系统产生之代码"<br>
							3. VIP会员使用定期(30天)定额自动扣款，升级VIP会员，最少期数为两期，效期过后不欲续缴的会员，请通过客服终止此服务。<br>
							4. 活动所加送的限时点券，有效期过后系统将自动回收。<br>
							5. 使用海外交易或银行网络ATM进行Web ATM交易者，结帐所产生的『银行转帐手续费』必须自行负担。<br>
							6. 基于线上内容商品的特性，特此告知，会员在购买后，不得因价格明显调降而要求退还/交换。	</div>
          </div>
  </div>
</template>

<script>
import chargeTable from './../components/chargeTable.vue'
export default {
  name: 'change-method',
  components:{
    chargeTable
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
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>  
@import './../style/var.scss';
.charge-method{	
	padding: 0.1rem;
  .title{
    background: #fff9d6;
    padding:0 0.5rem;
    line-height:0.8rem;
    .bold{
      font-weight:bold;
      padding-right:0.2rem;
    }
	}
	.head{
		background: #f6f6f6;
    border-top: 2px solid $mainColor;
    border-bottom: 1px solid #ddd;
    line-height: 0.7rem;
		height: 0.7rem;
		margin-top: 0.2rem;
		font-size: $titleSize;
		padding-left: 0.25rem;
	}
}
</style>
