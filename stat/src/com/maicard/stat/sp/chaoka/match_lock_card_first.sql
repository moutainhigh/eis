DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `match_lock_card_first`$$

CREATE DEFINER=`root`@`%` PROCEDURE `match_lock_card_first`(minLockMoney INT, maxLockMoney INT, timeoutPolicy VARCHAR(50), noFrozenMoney BOOLEAN, beforeLockStatus INT)
BEGIN
      DECLARE sourceItemId VARCHAR(50);
      DECLARE destItemId VARCHAR(50);
      DECLARE sourceProductId INT(11);
      DECLARE sourceLockMoney INT(11);
	/* 查找卡密 */
	SELECT item_id,product_id,request_money INTO sourceItemId,sourceProductId,sourceLockMoney FROM item 
		WHERE transaction_type_id=13 AND current_status=beforeLockStatus AND request_money>=minLockMoney AND request_money<=maxLockMoney
		AND request_money + success_money + frozen_money = label_money 
		AND IF(timeoutPolicy='TIMEIN_ONLY', (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time)) <= ttl , (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(enter_time)) > ttl)
		AND IF(noFrozenMoney = TRUE, frozen_money<=0, '1=1')
	ORDER BY weight DESC, enter_time, request_money DESC LIMIT 0,1;
  
	IF FOUND_ROWS()> 0 THEN  	
		/* 查找符合规则的可充值帐号 */
		SELECT item_id INTO destItemId FROM item 
			WHERE transaction_type_id=12 AND current_status=beforeLockStatus AND request_money>=minLockMoney
			AND product_id IN (SELECT dest_product_id FROM product_match WHERE source_product_id=sourceProductId AND current_status=100001)
		ORDER BY weight DESC, enter_time, request_money DESC LIMIT 0,1;
		IF FOUND_ROWS() > 0 THEN
			/* 成功找到匹配卡密和帐号 */
			UPDATE item SET request_money=request_money-sourceLockMoney, frozen_money=frozen_money+sourceLockMoney, process_count=process_count+1, ttl=ttl+5,lock_global_unique_id=lockGlobalUniqueId
				WHERE item_id=sourceItemId AND request_money>=sourceLockMoney  AND current_status=beforeLockStatus
				AND request_money + success_money + frozen_money = label_money AND request_money-sourceLockMoney>=0;
			IF ROW_COUNT() > 0 THEN
				UPDATE item SET request_money=request_money-sourceLockMoney, frozen_money=frozen_money+sourceLockMoney, process_count=process_count+1, lock_global_unique_id=lockGlobalUniqueId
					WHERE item_id=destItemId AND request_money>=sourceLockMoney  AND current_status=beforeLockStatus
					AND request_money + success_money + frozen_money = label_money AND request_money-sourceLockMoney>=0;
				IF ROW_COUNT() > 0 THEN
					/* 返回被锁定的两条匹配数据 */
					SELECT item_id,transaction_type_id,transaction_id,in_order_id,out_order_id,`name`,content,product_id,charge_from_account,charge_to_account,label_money,rate,`count`,request_money,success_money,sourceLockMoney AS frozen_money, in_money, out_money, enter_time,ttl,max_retry,close_time,formatted_custom_data,current_status,extra_status,billing_status,out_status,cart_id,lock_global_unique_id,money_source,process_count,supply_partner_id,fail_policy,weight,share_config_id FROM item WHERE item_id IN (sourceItemId, destItemId) ORDER BY transaction_type_id DESC;
				ELSE
				/* 回滚锁定的卡密 */
					UPDATE item SET request_money=request_money+sourceLockMoney, frozen_money=frozen_money-sourceLockMoney, process_count=process_count-1,ttl=ttl-5,lock_global_unique_id=NULL
						WHERE item_id=sourceItemId;
				END IF;
			END IF;
		END IF;
	END IF;
	
    END$$

DELIMITER ;