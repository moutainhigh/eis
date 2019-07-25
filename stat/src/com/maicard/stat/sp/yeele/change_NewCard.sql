DELIMITER $$

USE `eis_v4_yeele`$$

DROP TRIGGER /*!50032 IF EXISTS */ `change_NewCard`$$

CREATE
    /*!50017 DEFINER = 'yeele'@'%' */
    TRIGGER `change_NewCard` BEFORE UPDATE ON `product_server` 
    FOR EACH ROW BEGIN
    DECLARE cardcount INT;
  IF EXISTS(SELECT * FROM newcard_total WHERE product_id=new.product_id) AND (new.extra_status=122002) THEN
    SELECT COUNT(*)-20 INTO cardcount FROM gift_card WHERE current_status=100001 AND object_id IN (SELECT product_server_id FROM product_server WHERE product_id=new.product_id);
    UPDATE  gift_card SET object_id=new.product_server_id WHERE current_status=100001 AND object_id IN (SELECT product_server_id FROM product_server WHERE product_id=new.product_id) LIMIT cardcount;
    SET new.weight=new.product_server_id;
  END IF;
END;
$$

DELIMITER ;