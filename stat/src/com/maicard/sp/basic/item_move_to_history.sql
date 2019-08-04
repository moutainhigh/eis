DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `item_move_to_history`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `item_move_to_history`(begintime VARCHAR(20),endtime VARCHAR(20),table_suffix VARCHAR (10))
BEGIN
  DECLARE table1 VARCHAR(50); 
  DECLARE rowcount,rowcount1 INT; 
  IF table_suffix<>'' AND begintime<>'' AND endtime<>'' THEN
    BEGIN
       SET table1=CONCAT('item_data_',table_suffix);
       SET @createsql=CONCAT('create table if not exists ',table1,' like item'); 
       PREPARE exec FROM @createsql;
       EXECUTE exec;
       SET @insert_sql=CONCAT('insert into ',table1,' SELECT item_data.* FROM item_data,(SELECT item_id FROM item where DATE_FORMAT(enter_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(enter_time,"%Y%m%d%H")<="',endtime,'") a where item_data.product_id=a.item_id');                       
       PREPARE exec FROM @insert_sql;
       BEGIN START TRANSACTION;
         EXECUTE exec;
         SELECT ROW_COUNT() INTO rowcount;
         SET @insert_sql=CONCAT('delete item_data from item_data,(SELECT item_id FROM item where DATE_FORMAT(enter_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(enter_time,"%Y%m%d%H")<="',endtime,'") a where item_data.product_id=a.item_id ');               
         PREPARE exec FROM @insert_sql;
         EXECUTE exec;
         SELECT ROW_COUNT() INTO rowcount1;
         SELECT rowcount,rowcount1;         
         IF rowcount<>rowcount1 THEN
           BEGIN
             ROLLBACK ;           
           END;
         ELSE
           BEGIN
             SET table1=CONCAT('item_',table_suffix);
             SET @createsql=CONCAT('create table if not exists ',table1,' like item'); 
             PREPARE exec FROM @createsql;
             EXECUTE exec;
             SET @insert_sql=CONCAT('insert into ',table1,' SELECT * FROM item where DATE_FORMAT(enter_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(enter_time,"%Y%m%d%H")<="',endtime,'"');           
             PREPARE exec FROM @insert_sql;            
             EXECUTE exec;                         
             SELECT ROW_COUNT() INTO rowcount;
             SET @insert_sql=CONCAT('DELETE FROM item WHERE DATE_FORMAT(enter_time,"%Y%m%d%H") >="',begintime,'" and DATE_FORMAT(enter_time,"%Y%m%d%H")<="',endtime,'"');
             PREPARE exec FROM @insert_sql;
             EXECUTE exec;           
             SELECT ROW_COUNT() INTO rowcount1;
             SELECT rowcount,rowcount1;
             IF rowcount<>rowcount1 THEN
               BEGIN
                 ROLLBACK ;           
               END;
             ELSE
               COMMIT;  
             END IF;
           END;
         END IF;      
       END;
    END;
  ELSE
    SELECT "One of parameters is NUll!!" AS warning;
  END IF;  
END$$

DELIMITER ;