DROP EVENT IF EXISTS all_history_move_event;

DELIMITER $$


CREATE	EVENT `eis_tongbao`.`all_history_move_event`

ON SCHEDULE
	 EVERY 1 DAY STARTS '2016-08-01 07:00:00' ON COMPLETION PRESERVE ENABLE DO BEGIN
  CALL `all_history_move_caller`();
	END$$

DELIMITER ;