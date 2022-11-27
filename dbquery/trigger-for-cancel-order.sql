DELIMITER $$
create trigger data_update_quantity_product_by_status_order
    AFTER UPDATE
    ON orders
    FOR EACH ROW
BEGIN
    IF (New.status = 'Đã hủy') and (New.status <> Old.status) THEN
    UPDATE products
    SET unit_stock = unit_stock + (select quantity from lineitem where lineitem.product_id = products.product_id and lineitem.order_id = New.order_id)
    WHERE product_id IN (select product_id from lineitem where order_id = New.order_id);
END IF;
END$$

DELIMITER ;