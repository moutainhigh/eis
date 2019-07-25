DELIMITER $$

CREATE
    
    PROCEDURE `remove_relate_product`(productId int)
    
    
    BEGIN
	SET @product_id = productId;
	SET @udid = (SELECT udid FROM document WHERE document_code in (SELECT product_code FROM product where product_id = @product_id));
	DELETE FROM tag_object_relation WHERE udid = @udid;
	DELETE FROM document_data WHERE udid = @udid;
	DELETE FROM doucument d,product p WHERE d.document_code = p.product_code AND p.product_id = @product_id;
    END$$

DELIMITER ;