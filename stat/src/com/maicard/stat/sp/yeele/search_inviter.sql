DELIMITER $$

USE `eis_v4_yeele`$$

DROP PROCEDURE IF EXISTS `inviter_search`$$

CREATE DEFINER=`yeele`@`%` PROCEDURE `inviter_search`(customer INT )
BEGIN
  DECLARE uname VARCHAR(20) CHARACTER SET utf8;  
  SELECT username INTO uname FROM partner,(SELECT UUID FROM user_data_all,(SELECT data_value FROM  user_data_all WHERE UUID=customer AND data_define_id = 117) AS a
WHERE data_define_id = 93  AND user_data_all.data_value=a.data_value)AS a  WHERE partner.UUID=a.uuid;
  IF (FOUND_ROWS()=0) THEN
    SELECT '官网' AS inviter;
  ELSE
    SELECT uname;
  END IF;
END$$

DELIMITER ;