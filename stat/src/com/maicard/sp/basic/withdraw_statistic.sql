DELIMITER $$

DROP PROCEDURE IF EXISTS `withdraw_statistic`$$

CREATE DEFINER=`yht`@`%` PROCEDURE `withdraw_statistic`(
	stat_start_time VARCHAR(255), 
	stat_end_time VARCHAR(255)
    )
BEGIN
	/* init version */
    /* 统计付款次数、付款成功金额和发起付款的总金额 */
	DELETE FROM withdraw_stat WHERE stat_time>=stat_start_time AND stat_time <= stat_end_time;
	INSERT INTO withdraw_stat(stat_time, total_count, total_money, success_money,withdraw_method_id,inviter) 
	SELECT DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H") AS stat_time ,COUNT(*) AS total_count, SUM(face_money) AS total_money, SUM(real_money) AS success_money, withdraw_method_id, inviter FROM withdraw 
	WHERE DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H")<=stat_end_time
	GROUP BY DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H"), withdraw_method_id, inviter;	
	/* 更新成功次数 */
	UPDATE withdraw_stat AS c INNER JOIN (
	SELECT DATE_FORMAT(end_time,"%Y%m%d%H") AS stat_time ,COUNT(*) AS success_count, withdraw_method_id, inviter FROM withdraw 
	WHERE end_time IS NOT NULL AND current_status=710010 AND `real_money`>0 AND DATE_FORMAT(end_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(end_time,"%Y%m%d%H")<=stat_end_time
	GROUP BY DATE_FORMAT(end_time,"%Y%m%d%H"), withdraw_method_id, inviter) AS d SET c.`success_count`=d.success_count 
	WHERE c.`stat_time`=d.stat_time AND c.`inviter`=d.inviter AND c.`withdraw_method_id`=d.withdraw_method_id;
	
	/* 对于使用cartId并且是1对1交易的情况，即一个购物车里面肯定只有一个商品，根据item的inMoney更新支付统计的毛利 */		
	/*UPDATE withdraw_stat AS c INNER JOIN (
		SELECT DATE_FORMAT(a.close_time,"%Y%m%d%H") AS stat_time ,SUM(a.in_money) profit, b.withdraw_method_id, b.inviter FROM item AS a, withdraw  AS b
		WHERE a.`cart_id`=b.`ref_buy_transaction_id` AND end_time IS NOT NULL AND b.current_status=710010 AND `real_money`>0 AND DATE_FORMAT(end_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(end_time,"%Y%m%d%H")<=stat_end_time
		GROUP BY DATE_FORMAT(end_time,"%Y%m%d%H"), withdraw_method_id, inviter) AS d 
	SET c.`profit`=d.profit 
	WHERE c.`stat_time`=d.stat_time AND c.`inviter`=d.inviter AND c.`withdraw_method_id`=d.withdraw_method_id;*/
    END$$

DELIMITER ;