DELIMITER $$

USE `eis_v4_chaoka`$$

DROP FUNCTION IF EXISTS `getchildlst`$$

CREATE DEFINER=`chaoka`@`%` FUNCTION `getchildlst`(rootId BIGINT) RETURNS VARCHAR(5000) CHARSET utf8
BEGIN 
       DECLARE sTemp VARCHAR(10000); 
       DECLARE sTempChd VARCHAR(10000);      
       SET sTemp = '$'; 
       SET sTempChd =CAST(rootId AS CHAR); 
       WHILE sTempChd IS NOT NULL DO 
         SET sTemp = CONCAT(sTemp,',',sTempChd); 
         SELECT GROUP_CONCAT(UUID) INTO sTempChd FROM partner WHERE FIND_IN_SET(parent_uuid,sTempChd)>0; 
       END WHILE; 
       RETURN RIGHT(sTemp,LENGTH(sTemp)-2); 
     END$$

DELIMITER ;