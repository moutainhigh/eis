DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `part_money_lock_3`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `part_money_lock_3`(productId INT, minLockMoney INT, maxLockMoney INT, minWeight INT, fixWeight INT)
BEGIN
  DECLARE citem_id VARCHAR(50);
  DECLARE ctransaction_id VARCHAR(50);
  DECLARE ctransaction_type_id VARCHAR(50);
  DECLARE cname VARCHAR(50);
  DECLARE ccontent VARCHAR(50);
  DECLARE cproduct_id VARCHAR(50);
  DECLARE ccharge_from_account VARCHAR(50);
  DECLARE ccharge_to_account VARCHAR(50);
  DECLARE clabel_money VARCHAR(50);
  DECLARE crate VARCHAR(50);
  DECLARE ccount VARCHAR(50);
  DECLARE crequest_money VARCHAR(50);
  DECLARE center_time VARCHAR(50);
  DECLARE cttl VARCHAR(50);
  DECLARE cclose_time VARCHAR(50);
  DECLARE ccurrent_status VARCHAR(50);
  DECLARE cextra_status VARCHAR(50);
  DECLARE ccart_id VARCHAR(50);
  DECLARE cmoney_source VARCHAR(50);
  DECLARE clock_global_unique_id VARCHAR(50);
  DECLARE csupply_partner_id VARCHAR(50);
  DECLARE cweight INT(11);
  DECLARE minSourceMoney INT(11);
  DECLARE minDestMoney INT(11);
  DECLARE canLockMoney INT(11);
  DECLARE sqlText TEXT;
  DECLARE perfChannel INT; 
  SELECT MIN(min_source_part_use_money) INTO minSourceMoney FROM product_match WHERE source_product_id=productId AND current_status=100001; 
  SELECT item_id,b.min_dest_part_use_money,b.perf_process_channel_id INTO citem_id,minDestMoney,perfChannel FROM item AS a, product_match AS b WHERE a.`product_id`=b.`dest_product_id` AND b.current_status=100001 AND b.source_product_id=productId AND a.`request_money`>=b.`min_dest_part_use_money`  AND a.weight >= minWeight AND IF(fixWeight > 0, a.weight=fixWeight,'1=1') AND a.current_status=710021 AND a.transaction_type_id=12 AND process_count<=max_retry AND (UNIX_TIMESTAMP(NOW()) -UNIX_TIMESTAMP(enter_time) <= ttl) AND a.request_money + a.success_money + a.frozen_money = a.label_money ORDER BY a.weight DESC, (CURRENT_TIMESTAMP - a.enter_time) DIV a.ttl DESC,  a.enter_time, a.request_money DESC LIMIT 0,1;
  IF minSourceMoney < minDestMoney THEN
	SET minSourceMoney = minDestMoney;
  END IF;
  IF minSourceMoney > minLockMoney THEN
	SET minLockMoney = minSourceMoney;
  END IF; 
  IF minLockMoney > maxLockMoney THEN
	SET minLockMoney = maxLockMoney;  
  END IF;
    SELECT a.item_id,a.transaction_id,a.transaction_type_id,a.name,a.content,a.product_id,a.charge_from_account,a.charge_to_account,
    a.label_money,a.rate,a.count,a.request_money,a.enter_time,a.ttl,a.close_time,a.current_status,a.extra_status,a.cart_id,
    a.money_source,a.lock_global_unique_id,a.supply_partner_id, a.weight INTO  citem_id, ctransaction_id, ctransaction_type_id, cname, ccontent, cproduct_id, ccharge_from_account, 
    ccharge_to_account, clabel_money, crate, ccount, crequest_money, center_time, cttl, cclose_time, ccurrent_status, cextra_status, ccart_id, cmoney_source, 
    clock_global_unique_id, csupply_partner_id, cweight FROM item a  WHERE a.item_id=citem_id AND request_money>=minLockMoney  AND request_money>=5 AND a.current_status=710021;
    IF FOUND_ROWS()> 0 THEN  	
		IF crequest_money > maxLockMoney THEN
			SET canLockMoney = maxLockMoney;
		ELSE
			SET canLockMoney = crequest_money;
		END IF;
		UPDATE item SET request_money=request_money-canLockMoney, frozen_money=frozen_money+canLockMoney  WHERE item_id=citem_id AND request_money>=minLockMoney  AND current_status=710021 AND request_money + success_money + frozen_money = label_money AND request_money-canLockMoney>=0;
		IF ROW_COUNT() > 0 THEN
			SET crequest_money=canLockMoney;
			SELECT citem_id item_id, ctransaction_id transaction_id, ctransaction_type_id transaction_type_id, cname NAME, ccontent content, cproduct_id product_id, ccharge_from_account charge_from_account, 
			ccharge_to_account charge_to_account, clabel_money label_money, crate rate, ccount COUNT, crequest_money request_money, crequest_money frozen_money, center_time enter_time, cttl ttl, cclose_time close_time, ccurrent_status current_status, cextra_status extra_status, ccart_id cart_id, cmoney_source money_source, 
			clock_global_unique_id, perfChannel supply_partner_id, cweight weight;
		END IF;
     ELSE
	ROLLBACK;
     END IF;
  
END$$

DELIMITER ;