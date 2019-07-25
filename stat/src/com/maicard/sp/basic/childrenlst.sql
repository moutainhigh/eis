DELIMITER $$

USE `eis_v4_yeele`$$

DROP VIEW IF EXISTS `childrenlst`$$

CREATE ALGORITHM=UNDEFINED DEFINER=`yeele`@`%` SQL SECURITY DEFINER VIEW `childrenlst` AS 
SELECT distinct
  `a`.`uuid`  AS `UUID`,
   b.username,
  `getchildlst`(
`a`.`uuid`)  AS `childrenlst`,
  `b`.`level` AS `level`
FROM (`user_data_all` `a`
   LEFT JOIN `partner` `b`
     ON ((`a`.`uuid` = `b`.`uuid`and b.level=2)))
WHERE ((`a`.`data_value` = 'true')
       AND (`a`.`data_define_id` = 97))$$

DELIMITER ;