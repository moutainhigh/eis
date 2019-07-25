DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `i_uda`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `i_uda`(b VARCHAR(20),e VARCHAR(20))
BEGIN
SET @b=b;
SET @e=e;
SET @x=0;
REPEAT 
   SET @sql=CONCAT("INSERT into user_data_",@x,"(UUID, data_define_id, data_value,current_status,create_time) SELECT UUID,97,'true',100001,create_time FROM partner WHERE create_time   BETWEEN ? AND ? AND RIGHT(UUID,1)="
   ,@x," and level=2 AND UUID NOT IN (SELECT UUID FROM user_data_all WHERE data_define_id=97 AND data_value='true')");
    PREPARE dsql FROM @sql ;
   EXECUTE dsql USING @b,@e; 
   -- select @sql;   
   SET @x=@x+1;
   -- SELECT ROW_COUNT(); 
UNTIL @x>=10
END REPEAT;   
SELECT ROW_COUNT(); 
END$$

DELIMITER ;