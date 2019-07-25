delimiter $$

use `eis-v4-yeele`$$

drop procedure if exists `fetch_transaction_stat_by_hour`$$

create definer=`root`@`localhost` procedure `fetch_transaction_stat_by_hour`(
	start_time varchar(255),
	end_time varchar(255),
	other_condition varchar(255)
    )
begin
	declare max_hour varchar(255);
	declare sql_text varchar(500);
	drop table if exists `transaction_stat_by_hour`;
	/* 从粒度表中取出所有已存在的统计数据 */
	create temporary table `transaction_stat_by_hour` select * from transaction_stat where stat_time>=start_time and stat_time <= end_time;
	/* 检查从粒度表中获取的最后小时 */
	set max_hour = (select max(stat_time) from transaction_stat_by_hour);
	if max_hour is null then set max_hour = start_time;
	end if; 
	/*select max_hour;*/
	/* 如果最后小时小于需要获取的end_time，那么说明还需要获取实时统计数据 */
	set max_hour = date_format(date_add(str_to_date(max_hour,"%Y%m%d%H"), interval 1 hour),"%Y%m%d%H");
	if max_hour < end_time then call transaction_statistic(max_hour, end_time, 2);
	insert into transaction_stat_by_hour select * from temp_transaction_stat;
	end if;
	/* 返回结果集 */
	set @sql_text = "select stat_time, sum(transaction_count) as transaction_count, sum(paied_transaction_count) as paied_transaction_count, sum(success_transaction_count) as success_transaction_count, sum(total_transaction_money) as total_transaction_money,	sum(total_paied_money) as total_paied_money, sum(total_used_money) as total_used_money,	0 as ref_object_id, 0 as ref_object_type_id, pay_method_id, invite_by_uuid  from transaction_stat_by_hour ";
	if other_condition is not null then set @sql_text := concat(@sql_text, " where ");
	set @sql_text := concat(@sql_text, other_condition);
	end if;
	set @sql_text := concat(@sql_text, " group by stat_time order by stat_time DESC");
	prepare stmt from @sql_text;
	execute stmt;
	/*  where other_condition group by stat_time order by stat_time DESC;*/
	
    end$$

delimiter ;