DELIMITER $$

USE `eis_v4_chaoka`$$

DROP PROCEDURE IF EXISTS `saveToCsv`$$

CREATE DEFINER=`chaoka`@`%` PROCEDURE `saveToCsv`(
  begintime DATETIME,
  endtime DATETIME
)
BEGIN
SET @@GROUP_CONCAT_MAX_LEN=10485760;
SELECT GROUP_CONCAT(csv SEPARATOR '\r\n') FROM (
SELECT 'order_id,transaction_id,product_code,product_name,partner,card_serialnumber,card_password,request_money,success_money,enter_time,close_time,current_status' csv
UNION ALL   
SELECT
  CONCAT(order_id,',',transaction_id,',',product_code,',', product_name,',',partner,',', card_serialnumber,',',card_password,',',request_money,',',success_money,',',enter_time,',',close_time,',',`current_status`) AS csv
FROM
  (
  SELECT
    in_order_id order_id,
    transaction_id,
    product.product_code,
    product_name,
    charge_from_account partner,
    a.data_value card_serialnumber,
    b.`data_value` card_password,
    request_money,
    success_money,
    enter_time,
    close_time,
    item.`current_status`
  FROM
    item,
    product,
    item_data a,
    item_data b
  WHERE enter_time BETWEEN begintime
    AND endtime
    AND charge_from_account = 300206
    AND item.`product_id` = product.`product_id`
    AND a.`product_id` = item.`item_id`
    AND a.`data_define_id` = 179
    AND b.`product_id` = item.`item_id`
    AND b.`data_define_id` = 181
  UNION
  SELECT
    in_order_id order_id,
    transaction_id,
    product.product_code,
    product_name,
    charge_from_account partner,
    a.data_value card_serialnumber,
    b.`data_value` card_password,
    request_money,
    success_money,
    enter_time,
    close_time,
    item_history.`current_status`
  FROM
    item_history,
    product,
    item_data_history a,
    item_data_history b
  WHERE enter_time BETWEEN begintime
    AND endtime
    AND charge_from_account = 300206
    AND item_history.`product_id` = product.`product_id`
    AND a.`product_id` = item_history.`item_id`
    AND a.`data_define_id` = 179
    AND b.`product_id` = item_history.`item_id`
    AND b.`data_define_id` = 181) AS a
) AS a ;
END$$

DELIMITER ;