DELIMITER $$

CREATE
    /*[DEFINER = { user | CURRENT_USER }]*/
    PROCEDURE `???`.`remove_timeout_temp_order`()
    /*LANGUAGE SQL
    | [NOT] DETERMINISTIC
    | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
    | SQL SECURITY { DEFINER | INVOKER }
    | COMMENT 'string'*/
    BEGIN
	UPDATE cart SET ttl=86400 WHERE ttl = 0;
	DELETE FROM item_data_history WHERE product_id IN (SELECT item_id FROM item_history WHERE  cart_id IN (SELECT cart_id FROM cart WHERE order_type='TEMP' AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(last_access_time)) >= ttl));
	DELETE FROM item_data WHERE product_id IN (SELECT item_id FROM item_history WHERE  cart_id IN (SELECT cart_id FROM cart WHERE order_type='TEMP' AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(last_access_time)) >= ttl));
	DELETE FROM item_history WHERE  cart_id IN (SELECT cart_id FROM cart WHERE order_type='TEMP' AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(last_access_time)) >= ttl);
	DELETE FROM item WHERE  cart_id IN (SELECT cart_id FROM cart WHERE order_type='TEMP' AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(last_access_time)) >= ttl);
	DELETE FROM cart WHERE order_type='TEMP' AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(last_access_time)) >= ttl;

    END$$

DELIMITER ;