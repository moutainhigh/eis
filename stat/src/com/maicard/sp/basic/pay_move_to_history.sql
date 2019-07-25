DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `pay_move_to_history`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `pay_move_to_history`(begintime VARCHAR(20),endtime VARCHAR(20),table_suffix VARCHAR (10))
BEGIN
  DECLARE table1 VARCHAR(50); 
  DECLARE rowcount,rowcount1 INT; 
  IF table_suffix<>'' AND begintime<>'' AND endtime<>'' THEN
    BEGIN
       SET table1=CONCAT('pay_',table_suffix);
       SET @createsql=CONCAT('create table if not exists ',table1,' like pay'); 
       PREPARE exec FROM @createsql;
       EXECUTE exec;
       SET @insert_sql=CONCAT('insert into ',table1,' SELECT * FROM pay where DATE_FORMAT(start_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(start_time,"%Y%m%d%H")<="',endtime,'"');           
       PREPARE exec FROM @insert_sql;
       EXECUTE exec;
       SELECT ROW_COUNT() INTO rowcount;
       SELECT rowcount;
       BEGIN START TRANSACTION;
         DELETE FROM pay WHERE DATE_FORMAT(start_time,"%Y%m%d%H")>=begintime AND DATE_FORMAT(start_time,"%Y%m%d%H")<=endtime;
         SELECT ROW_COUNT() INTO rowcount1;
         IF rowcount<>rowcount1 THEN
           BEGIN
             ROLLBACK ;           
             SET @delete_sql=CONCAT('delete from ',table1,'DATE_FORMAT(start_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(start_time,"%Y%m%d%H")<="',endtime,'"');
             PREPARE exec FROM @delete_sql;
             EXECUTE exec;
           END;
         END IF;
         COMMIT;
       END;
    END;
  ELSE
    SELECT "One of parameters is NUll!!" AS warning;
  END IF;
END$$

DELIMITER ;