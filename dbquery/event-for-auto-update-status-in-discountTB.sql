# Auto update status of discount-code when end_date < current_date
CREATE EVENT reset_status_in_discountTB
    ON SCHEDULE
        EVERY 1 HOUR
    DO
update discountcode
set status='Hết hạng'
where discountcode.end_date < current_date();