DELIMITER $$
create trigger data_update_price_product
    AFTER UPDATE
    ON products
    FOR EACH ROW
BEGIN
    IF (New.discount_percent = 0)
        THEN
    UPDATE cart as m_tb
        INNER JOIN (select product_id from cart where cart.product_id = New.product_id) as p_id_tb
    SET m_tb.price = New.price
    WHERE m_tb.product_id IN (p_id_tb.product_id);
    ELSEIF (New.discount_percent <> 0)
        THEN
    UPDATE cart as m_tb
        INNER JOIN (select product_id from cart where cart.product_id = New.product_id) as p_id_tb
    SET m_tb.price = New.price * (1 - (select New.discount_percent) / 100)
    WHERE m_tb.product_id IN (p_id_tb.product_id);
    ELSEIF (New.price <> Old.price)
        THEN
            IF (New.discount_percent = 0)
            THEN
    UPDATE cart as m_tb
        INNER JOIN (select product_id from cart where cart.product_id = New.product_id) as p_id_tb
    SET m_tb.price = New.price
    WHERE m_tb.product_id IN (p_id_tb.product_id);
    ELSEIF (New.discount_percent <> 0)
            THEN
    UPDATE cart as m_tb
        INNER JOIN (select product_id from cart where cart.product_id = New.product_id) as p_id_tb
    SET m_tb.price = New.price * (1 - (select New.discount_percent) / 100)
    WHERE m_tb.product_id IN (p_id_tb.product_id);
END IF;
END IF;
END$$

DELIMITER ;