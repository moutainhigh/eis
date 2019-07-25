DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `item_statistic_gatherx`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `item_statistic_gatherx`(
	stat_enter_time VARCHAR(255), 
	dest INT
)
BEGIN		
        DECLARE itemdata VARCHAR(20);	
	DROP TABLE IF EXISTS `temp_item_stat`;
	CREATE TEMPORARY TABLE temp_item_stat AS SELECT * FROM item_stat WHERE 1=2;	
	/* 统计支付次数、支付成功金额和支付总金额 */
	TRUNCATE TABLE temp_item_stat;	
        SET @stat_enter_time=stat_enter_time; 
        SET @stat_end_time=DATE_FORMAT(SUBDATE(NOW(),INTERVAL 1 HOUR),"%Y%m%d%H"); 
        SET @sqlstring1=CONCAT('insert into temp_item_stat(extra_status,success_count,total_count,success_money,total_money,product_id,region,server,stat_time,inviter) select a.extra_status,b.success_count,a.total_count,b.success_money,a.total_money,a.product_id,0 as region,a.server,a.stat_time,a.inviter from (SELECT a.`extra_status`,DATE_FORMAT(a.enter_time,"%Y%m%d%H") AS stat_time ,COUNT(*) AS total_count,SUM(a.request_money) AS total_money,a.product_id, d.data_value AS SERVER, b.inviter FROM item AS a LEFT JOIN front_user AS b ON a.charge_from_account=b.uuid LEFT JOIN item_data AS c ON a.`item_id`=c.`product_id` AND c.data_define_id=91	LEFT JOIN item_data AS d ON a.`item_id`=d.`product_id` AND d.`data_define_id`=177 WHERE DATE_FORMAT(enter_time,"%Y%m%d%H")>=? AND DATE_FORMAT(enter_time,"%Y%m%d%H")<=? and a.current_status= 710010 and inviter=',dest,' GROUP BY DATE_FORMAT(enter_time,"%Y%m%d%H"),inviter,SERVER,a.`extra_status`) as a left join (
SELECT a.`extra_status`,DATE_FORMAT(a.enter_time,"%Y%m%d%H") AS stat_time ,SUM(a.success_money) AS success_money,count(*) as success_count,a.product_id, d.data_value AS SERVER, b.inviter FROM item AS a LEFT JOIN front_user AS b ON a.charge_from_account=b.uuid LEFT JOIN item_data AS c ON a.`item_id`=c.`product_id` AND c.data_define_id=91	LEFT JOIN item_data AS d ON a.`item_id`=d.`product_id` AND d.`data_define_id`=177 WHERE DATE_FORMAT(enter_time,"%Y%m%d%H")>=? AND DATE_FORMAT(enter_time,"%Y%m%d%H")<=? and success_money>0 and a.current_status= 710010 and inviter=',dest,' GROUP BY DATE_FORMAT(enter_time,"%Y%m%d%H"),inviter,SERVER,a.`extra_status`) as b
on a.stat_time=b.stat_time and a.server=b.server and a.inviter=b.inviter and a.extra_status=b.extra_status');
         -- SELECT @sqlstring1;
        PREPARE sqlstring FROM @sqlstring1;
	EXECUTE sqlstring USING @stat_enter_time,@stat_end_time,@stat_enter_time,@stat_end_time;
	-- xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
SET @sqlstring1=CONCAT('insert into temp_item_stat(extra_status,success_count,total_count,success_money,total_money,product_id,region,server,stat_time,inviter) select a.extra_status,b.success_count,a.total_count,b.success_money,a.total_money,a.product_id,0 as region,a.server,a.stat_time,a.inviter from (SELECT a.`extra_status`,DATE_FORMAT(a.enter_time,"%Y%m%d%H") AS stat_time ,COUNT(*) AS total_count,SUM(a.request_money) AS total_money,a.product_id, d.data_value AS SERVER, b.inviter FROM item_history AS a LEFT JOIN front_user AS b ON a.charge_from_account=b.uuid LEFT JOIN item_data_history AS c ON a.`item_id`=c.`product_id` AND c.data_define_id=91	LEFT JOIN item_data_history AS d ON a.`item_id`=d.`product_id` AND d.`data_define_id`=177 WHERE DATE_FORMAT(enter_time,"%Y%m%d%H")>=? AND DATE_FORMAT(enter_time,"%Y%m%d%H")<=? and a.current_status= 710010 and inviter=',dest,' GROUP BY DATE_FORMAT(enter_time,"%Y%m%d%H"),inviter,SERVER,a.`extra_status`) as a left join (
SELECT a.`extra_status`,DATE_FORMAT(a.enter_time,"%Y%m%d%H") AS stat_time ,SUM(a.success_money) AS success_money,count(*) as success_count,a.product_id, d.data_value AS SERVER, b.inviter FROM item_history AS a LEFT JOIN front_user AS b ON a.charge_from_account=b.uuid LEFT JOIN item_data_history AS c ON a.`item_id`=c.`product_id` AND c.data_define_id=91	LEFT JOIN item_data_history AS d ON a.`item_id`=d.`product_id` AND d.`data_define_id`=177 WHERE DATE_FORMAT(enter_time,"%Y%m%d%H")>=? AND DATE_FORMAT(enter_time,"%Y%m%d%H")<=? and success_money>0 and a.current_status= 710010 and inviter=',dest,' GROUP BY DATE_FORMAT(enter_time,"%Y%m%d%H"),inviter,SERVER,a.`extra_status`) as b
on a.stat_time=b.stat_time and a.server=b.server and a.inviter=b.inviter and a.extra_status=b.extra_status');
        -- SELECT @sqlstring1;
        PREPARE sqlstring FROM @sqlstring1;
	EXECUTE sqlstring USING @stat_enter_time,@stat_end_time,@stat_enter_time,@stat_end_time;		
	/*将临时数据保存到物理表中 */
	DELETE FROM item_stat WHERE stat_time >= stat_enter_time AND stat_time <= @stat_end_time AND inviter=dest;
	INSERT INTO item_stat(success_count,total_count,success_money,total_money,product_id,region,SERVER,stat_time,inviter,flag,extra_status) SELECT success_count,total_count,success_money,total_money,product_id,region,SERVER,stat_time,inviter,flag,extra_status FROM temp_item_stat;
	SELECT COUNT(*) FROM temp_item_stat;
END$$

DELIMITER ;