delimiter $$

use `eis-v4-yeele`$$

drop procedure if exists `fetch_front_user_stat_by_hour`$$

create definer=`root`@`localhost` procedure `fetch_front_user_stat_by_hour`(
	start_time varchar(255),
	end_time varchar(255),
	other_condition varchar(255)
    )
begin
	declare max_hour varchar(255);
	declare sql_text varchar(500);
	drop table if exists `front_user_stat_by_hour`;
	/* 从粒度表中取出所有已存在的统计数据 */
	create temporary table `front_user_stat_by_hour` select * from front_user_stat where stat_time>=start_time and stat_time <= end_time;
	/* 检查从粒度表中获取的最后小时 */
	set max_hour = (select max(stat_time) from front_user_stat_by_hour);
	if max_hour is null then set max_hour = start_time;
	end if; 
	/*select max_hour;*/
	/* 如果最后小时小于需要获取的end_time，那么说明还需要获取实时统计数据 */
	set max_hour = date_format(date_add(str_to_date(max_hour,"%Y%m%d%H"), interval 1 hour),"%Y%m%d%H");
	if max_hour < end_time then call front_user_statistic(max_hour, end_time, false);
	insert into front_user_stat_by_hour select * from temp_front_user_stat;
	end if;
	/* 返回结果集 */
	set @sql_text = "select stat_time, sum(register_count) as register_count, sum(active_count) as active_count, sum(login_count) as login_count, sum(use_business_count) as use_business_count, 0 as ref_object_id, 0 as ref_object_type_id, invite_by_uuid  from front_user_stat_by_hour ";
	if other_condition is not null then set @sql_text := concat(@sql_text, " where ");
	set @sql_text := concat(@sql_text, other_condition);
	end if;
	set @sql_text := concat(@sql_text, " group by stat_time order by stat_time DESC");
	prepare stmt from @sql_text;
	execute stmt;
	/*  where other_condition group by stat_time order by stat_time DESC;*/
	
    end$$

delimiter ;