delimiter $$

use `eis-v4-yeele`$$

drop procedure if exists `transaction_statistic`$$

create definer=`root`@localhost procedure `transaction_statistic`(
	stat_start_time varchar(255), 
	stat_end_time varchar(255),
	flag int(11)
)
begin
		
	/* 创建临时表 */
	drop table if exists `temp_transaction_stat`;
	create temporary table temp_transaction_stat as select * from transaction_stat where 1=2;
	
	truncate table temp_transaction_stat;
	/* ============== 开始统计交易次数、支付成功金额和支付总金额 ============== */
	INSERT INTO temp_transaction_stat(stat_time, transaction_count, total_transaction_money, total_paied_money, total_used_money,ref_object_type_id, ref_object_id, pay_method_id,invite_by_uuid) 
	SELECT 
		DATE_FORMAT(a.start_time,"%Y%m%d%H") AS stat_time ,
		COUNT(*) AS transaction_count, 
		SUM(a.total_money) AS total_transaction_money, 
		SUM(a.paied_money) AS total_paied_money, 
		SUM(a.used_money) AS total_used_money, 
		a.ref_object_type_id, 
		a.ref_object_id, 
		c.pay_method_id, 
		b.invite_by_uuid  
	FROM `transaction` AS a
		LEFT JOIN front_user AS b ON a.to_account=b.uuid 
		LEFT JOIN pay AS c ON a.`transaction_id`=c.`transaction_id` 
	WHERE DATE_FORMAT(a.start_time,"%Y%m%d%H")>=stat_start_time  AND 
		DATE_FORMAT(a.start_time,"%Y%m%d%H")<=stat_end_time 
	GROUP BY DATE_FORMAT(a.start_time,"%Y%m%d%H"),a.ref_object_type_id, a.ref_object_id, b.invite_by_uuid;
	/* ============== 结束统计交易次数、支付成功金额和支付总金额 ============== */
	
	
	
	/*============ 开始更新成功完成支付的总次数 ===========*/
	UPDATE temp_transaction_stat AS a 
	INNER JOIN (
		/* 按时间统计出成功支付的交易次数 as b */
		SELECT DATE_FORMAT(c.start_time,"%Y%m%d%H") AS stat_time ,
			COUNT(*) AS paied_transaction_count, 
			c.ref_object_type_id, 
			c.ref_object_id, 
			e.pay_method_id, 
			d.invite_by_uuid FROM `transaction` AS c 
		LEFT JOIN front_user AS d ON c.to_account=d.uuid 
		LEFT JOIN pay AS e ON c.`transaction_id`=e.`transaction_id` 
		WHERE c.paied_money>0 GROUP BY DATE_FORMAT(c.start_time,"%Y%m%d%H"),
		c.ref_object_type_id, 
		c.ref_object_id)AS b
	 ON a.stat_time=b.stat_time 
	 AND a.ref_object_type_id=b.ref_object_type_id 
	 AND a.ref_object_id=b.ref_object_id 
	 AND a.invite_by_uuid=b.invite_by_uuid 
	 
	 SET a.paied_transaction_count = b.paied_transaction_count AND a.pay_method_id=b.pay_method_id 
	 WHERE 
		a.stat_time=b.stat_time 
		AND a.ref_object_type_id=b.ref_object_type_id AND a.ref_object_id=b.ref_object_id AND a.pay_method_id=b.pay_method_id AND a.invite_by_uuid=b.invite_by_uuid;
	/*============ 结束更新成功完成支付的总次数 ===========*/
		
	/*============ 开始更新成功完成支付和充值（即交易状态为710012或710015）的总次数 ===========*/
	UPDATE temp_transaction_stat AS a 
	INNER JOIN (
		/* 按时间统计出成功支付并完成充值的交易次数 as b */
		SELECT DATE_FORMAT(c.start_time,"%Y%m%d%H") AS stat_time ,
			COUNT(*) AS success_transaction_count, 
			c.ref_object_type_id, 
			c.ref_object_id, 
			e.pay_method_id, 
			d.invite_by_uuid FROM `transaction` AS c 
		LEFT JOIN front_user AS d ON c.to_account=d.uuid 
		LEFT JOIN pay AS e ON c.`transaction_id`=e.`transaction_id` 
		WHERE c.paied_money>0 AND c.transaction_status in (710012,710015) GROUP BY DATE_FORMAT(c.start_time,"%Y%m%d%H"),
		c.ref_object_type_id, 
		c.ref_object_id)AS b
	 ON a.stat_time=b.stat_time 
	 AND a.ref_object_type_id=b.ref_object_type_id 
	 AND a.ref_object_id=b.ref_object_id 
	 AND a.invite_by_uuid=b.invite_by_uuid 
	 
	 SET a.success_transaction_count = b.success_transaction_count AND a.pay_method_id=b.pay_method_id 
	 WHERE 
		a.stat_time=b.stat_time 
		AND a.ref_object_type_id=b.ref_object_type_id AND a.ref_object_id=b.ref_object_id AND a.pay_method_id=b.pay_method_id AND a.invite_by_uuid=b.invite_by_uuid;
	/*============ 结束更新成功完成支付和充值（即交易状态为710012或710015）的总次数 ===========*/
		
	
	/*将临时数据保存到物理表中 */
	if flag = 0 then  delete from transaction_stat where stat_time >= stat_start_time and stat_time <= stat_end_time;
		insert into transaction_stat select * from temp_transaction_stat;
		select row_count();
	end if;
	if flag =1 then	select 
			substr(stat_time,1,8) as stat_time,
			sum(transaction_count) as transaction_count, 
			sum(paied_transaction_count) as paied_transaction_count, 
			sum(success_transaction_count) as success_transaction_count, 
			sum(total_transaction_money) as total_transaction_money, 
			sum(total_paied_money) as total_paied_money, 
			sum(total_used_money) as total_used_money, 
			0 as ref_object_id, 
			0 as ref_object_type_id, 
			pay_method_id,
			invite_by_uuid
	from temp_transaction_stat group by stat_time order by stat_time DESC;
	END IF;
    end$$

delimiter ;