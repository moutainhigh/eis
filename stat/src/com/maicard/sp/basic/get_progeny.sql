DELIMITER $$

USE `eis_yixian`$$

DROP PROCEDURE IF EXISTS `get_child_partner`$$
DROP PROCEDURE IF EXISTS `get_progeny`$$

CREATE DEFINER=`yixian`@`%` PROCEDURE `get_progeny`(uid BIGINT)
BEGIN
    DECLARE child TEXT;
    DECLARE sqlText TEXT;
    SELECT COUNT(*) INTO @child_count FROM partner WHERE parent_uuid=uid;
    SET @sub_child = uid;
    SET child = "";
    WHILE @child_count > 0 DO
	SET @sqlText = CONCAT('SELECT GROUP_CONCAT(`uuid`) INTO @sub_child FROM partner WHERE parent_uuid in (', @sub_child, ')');
	PREPARE exec FROM @sqlText;
	EXECUTE exec;
	
	SET child = CONCAT(child,',',@sub_child);
	
	SET @sqlText = CONCAT('SELECT COUNT(*) INTO @child_count FROM partner where parent_uuid IN(', @sub_child, ')');
	PREPARE exec FROM @sqlText;
	EXECUTE exec;
	
	-- select @child_count;		
    END WHILE;
    
    SELECT child;
	
    END$$

DELIMITER ;