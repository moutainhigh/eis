<template>
<table class="charge-table">
    <tr height="45" v-show="withHeader">
      <th scope="col" colspan="2" style="background-color:#f6f6f6;text-align:center;font-weight:800;border-bottom:1px solid #ddd;border-right:1px solid #ccc;">金額(TWD)</th>
      <th scope="col" style="background-color:#f6f6f6;text-align:center;font-weight:800;border-bottom:1px solid #ddd;border-right:1px solid #ddd;">基本點券+加贈點券</th>
      <th scope="col" style="background-color:#f6f6f6;text-align:center;font-weight:800;border-bottom:1px solid #ddd;">優惠</th>
    </tr>
    <tr style="height:60px;cursor:pointer;" >		
			<td style="background-color:#fff;text-align:center;border-bottom:1px solid #f2dea5;">
				<div class="checks" style="margin-bottom:7px;">
					<input type="radio" name="charge_idx" id="charge_81">
					<label for="ex_rd" style="display: unset;">&nbsp;</label>
				</div>
			</td>
			<td style="background-color:#fff;padding-left:10px;border-bottom:1px solid #f2dea5;border-right:1px solid #e0e0e0;">
			  <span style="font-size:12px;font-weight:400;color:#e63740;text-decoration: line-through;">1,398</span><br>				<span class="item_price" style="font-size:12px;font-weight:bold;">699</span>
			</td>
			<td style="background-color:#fff;padding-left:10px;border-bottom:1px solid #f2dea5;border-right:1px solid #e0e0e0;">
			  <img src="./../assets/payment_coinimg36x36.png" style="height:18px;" align="absmiddle">
			  130			<span style="font-size:12px; font-weight:bold; color:#e63740;"> + 156</span>
        <span style="padding: 0.1rem 0.2rem;background:#e63740;color: #fff;border-radius: 50px;height: 20px;line-height: 20px;margin-left: 5px;">赠送</span>			
      </td>
			<td style="background-color:#fff;padding-left:15px;border-bottom:1px solid #f2dea5;">
			  <span style="font-size:12px;color:#e33;font-weight:bold;">加赠 120% </span>
				<span style="padding: 0.1rem 0.2rem;background:#666;margin-left:0.5rem;color: #fff;border-radius: 50px;height: 20px;line-height: 20px;margin-right: 5px;" class="charge_btn_mini" id="charge_btn_mini_81">
				  储值				
        </span>

			</td>
		</tr>
</table>
</template>

<script>
export default {
  name: 'chargeTable',
  props:['withHeader'],
  data () {
    return {
      
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
@import './../style/var.scss';
.charge-table{
  width:100%;
}
</style>
