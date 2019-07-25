DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `fetch_with_lock`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `fetch_with_lock`(transactionId VARCHAR(255), beforeLockStatus INT, afterLockStatus INT, needMinRequestMoney INT, transactionTypeId INT, timeoutPolicy VARCHAR(255), noFrozenMoney BOOLEAN, lockGlobalUniqueId VARCHAR(255), inProductIds TEXT)
BEGIN
  SET @sqlText = CONCAT("SELECT item_id INTO @citem_id  FROM item WHERE current_status=", beforeLockStatus, " AND request_money>=", needMinRequestMoney, " AND transaction_type_id=", transactionTypeId );
  IF transactionId IS NOT NULL THEN
	SET @sqlText = CONCAT(@sqlText," AND transaction_id='", transactionId, "'");
  END IF;
  IF timeoutPolicy = 'TIMEIN_ONLY' THEN
	SET @sqlText = CONCAT(@sqlText, " AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time)) <= ttl AND process_count <= max_retry");
  END IF;
  IF timeoutPolicy = 'TIMEOUT_ONLY' THEN
  	SET @sqlText = CONCAT(@sqlText, " AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time)) > ttl AND process_count <= max_retry + 5");
  END IF;
  IF timeoutPolicy = 'TIMEDEAD_ONLY' THEN
  	SET @sqlText = CONCAT(@sqlText, " AND (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time)) > ttl * 4 AND process_count <= max_retry + 9");
  END IF; 
  IF noFrozenMoney = TRUE THEN
	SET @sqlText = CONCAT(@sqlText, " AND frozen_money <= 0");
  END IF;
  IF inProductIds IS NOT NULL THEN
	SET @sqlText = CONCAT(@sqlText, " AND product_id in (", inProductIds, ")");
  END IF;
  SET @sqlText = CONCAT(@sqlText, "  ORDER BY (UNIX_TIMESTAMP(enter_time)+ttl-UNIX_TIMESTAMP(NOW())) DIV 10, weight DESC, request_money DESC LIMIT 0,1");
  /*select @sqlText;*/
  PREPARE exec FROM @sqlText;
  EXECUTE exec;
  
  
    IF FOUND_ROWS()> 0 THEN  	
		
		UPDATE item SET current_status=afterLockStatus, frozen_money=request_money, request_money=0, lock_global_unique_id=lockGlobalUniqueId, process_count=process_count+1  WHERE item_id=@citem_id AND current_status=beforeLockStatus;
		IF ROW_COUNT() > 0 THEN
			SELECT *  FROM item a WHERE a.item_id=@citem_id AND current_status=afterLockStatus AND lock_global_unique_id=lockGlobalUniqueId ;
		END IF;
     ELSE
	ROLLBACK;
     END IF;
  
END$$

DELIMITER ;