DELIMITER $$

USE `dima`$$

DROP PROCEDURE IF EXISTS `pay_statistic`$$

CREATE DEFINER=`dima`@`%` PROCEDURE `pay_statistic`(
	stat_start_time VARCHAR(255), 
	stat_end_time VARCHAR(255)
    )
BEGIN
    /* 对payCardType为空的情况处理为默认的UN,2019-01-13*/
    /* 2018-1-9 增加了payCardType的粒度 */
    /* 更新于2017-3-25,NetSnake 1. 增加了毛利统计; 2.使用COALESCE处理end_time为空的情况 */
    /* 统计支付次数、支付成功金额和支付总金额 */
DELETE FROM pay_stat WHERE stat_time>=stat_start_time AND stat_time <= stat_end_time;
	INSERT INTO pay_stat(stat_time, total_count, total_money, success_money,pay_method_id,pay_card_type, inviter) 
	SELECT DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H") AS stat_time ,COUNT(*) AS total_count, SUM(face_money) AS total_money, SUM(real_money) AS success_money, pay_method_id, IFNULL(pay_card_type,'UN') pay_card_type, inviter FROM pay 
	WHERE DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H")<=stat_end_time
	GROUP BY DATE_FORMAT(COALESCE(end_time,start_time),"%Y%m%d%H"), pay_method_id, pay_card_type, inviter;	
	/* 更新成功次数 */
	UPDATE pay_stat AS c INNER JOIN (
	SELECT DATE_FORMAT(end_time,"%Y%m%d%H") AS stat_time ,COUNT(*) AS success_count, pay_method_id, IFNULL(pay_card_type,'UN') pay_card_type_1, inviter FROM pay 
	WHERE end_time IS NOT NULL AND current_status=710010 AND `real_money`>0 AND DATE_FORMAT(end_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(end_time,"%Y%m%d%H")<=stat_end_time
	GROUP BY DATE_FORMAT(end_time,"%Y%m%d%H"), pay_method_id, pay_card_type, inviter) AS d SET c.`success_count`=d.success_count 
	WHERE c.`stat_time`=d.stat_time AND c.`inviter`=d.inviter AND c.`pay_method_id`=d.pay_method_id AND c.`pay_card_type`=d.pay_card_type_1;
	
	/* 对于使用cartId并且是1对1交易的情况，即一个购物车里面肯定只有一个商品，根据item的inMoney更新支付统计的毛利 */		
	/*UPDATE pay_stat AS c INNER JOIN (
		SELECT DATE_FORMAT(a.close_time,"%Y%m%d%H") AS stat_time ,SUM(a.in_money) profit, b.pay_method_id, b.inviter FROM item AS a, pay  AS b
		WHERE a.`cart_id`=b.`ref_buy_transaction_id` AND end_time IS NOT NULL AND b.current_status=710010 AND `real_money`>0 AND DATE_FORMAT(end_time,"%Y%m%d%H")>=stat_start_time AND DATE_FORMAT(end_time,"%Y%m%d%H")<=stat_end_time
		GROUP BY DATE_FORMAT(end_time,"%Y%m%d%H"), pay_method_id, inviter) AS d 
	SET c.`profit`=d.profit 
	WHERE c.`stat_time`=d.stat_time AND c.`inviter`=d.inviter AND c.`pay_method_id`=d.pay_method_id;*/
    END$$

DELIMITER ;