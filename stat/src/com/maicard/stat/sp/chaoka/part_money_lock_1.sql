DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `part_money_lock_1`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `part_money_lock_1`(wantLockMoney INT, lockStatus INT, transactionType INT, timeoutOnly BOOLEAN)
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
  SET autocommit=0;
  SELECT item_id INTO citem_id FROM item WHERE  request_money=wantLockMoney AND current_status=lockStatus AND transaction_type_id=transactionType AND process_count<=max_retry AND IF (timeoutOnly=TRUE, (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time))> ttl,'1=1') LIMIT 0,1;
  
  BEGIN START TRANSACTION;
    SELECT a.item_id,a.transaction_id,a.transaction_type_id,a.name,a.content,a.product_id,a.charge_from_account,a.charge_to_account,
    a.label_money,a.rate,a.count,a.request_money,a.enter_time,a.ttl,a.close_time,a.current_status,a.extra_status,a.cart_id,
    a.money_source,a.lock_global_unique_id,a.supply_partner_id INTO  citem_id, ctransaction_id, ctransaction_type_id, cname, ccontent, cproduct_id, ccharge_from_account, 
    ccharge_to_account, clabel_money, crate, ccount, crequest_money, center_time, cttl, cclose_time, ccurrent_status, cextra_status, ccart_id, cmoney_source, 
    clock_global_unique_id, csupply_partner_id FROM item a WHERE a.item_id=citem_id AND request_money=wantLockMoney FOR UPDATE;
    UPDATE item SET request_money=request_money-wantLockMoney,frozen_money=frozen_money+wantLockMoney  WHERE item_id=citem_id;
    COMMIT;
  END;
  SET autocommit = 1;
  IF FOUND_ROWS()>0 THEN  
  SELECT citem_id item_id, ctransaction_id transaction_id, ctransaction_type_id transaction_type_id, cname NAME, ccontent content, cproduct_id product_id, ccharge_from_account charge_from_account, 
    ccharge_to_account charge_to_account, clabel_money label_money, crate rate, ccount COUNT, crequest_money request_money, center_time enter_time, cttl ttl, cclose_time close_time, ccurrent_status current_status, cextra_status, ccart_id cart_id, cmoney_source money_source, 
    clock_global_unique_id, csupply_partner_id;
  END IF;  
END$$

DELIMITER ;