use treeshop;
ALTER TABLE category
ADD COLUMN enabled bit(1) AFTER created_day;

DELIMITER $$
create trigger data_update_flag_product_when_delete_category
    AFTER UPDATE
    ON category
    FOR EACH ROW
BEGIN
    IF (New.enabled = false) and (New.enabled <> Old.enabled) THEN
    UPDATE products
    SET enabled = false
    WHERE category_id IN (Select category_id from category where enabled = New.enabled);
END IF;
END$$
v
DELIMITER ;
