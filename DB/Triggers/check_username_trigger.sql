DROP TRIGGER IF EXISTS check_username_trigger;

DELIMITER //

CREATE TRIGGER check_username_trigger BEFORE INSERT ON `WEB_ACCOUNT`
     FOR EACH ROW
     BEGIN
     DECLARE msg varchar(255);
     IF (NEW.username = '')
     THEN
        SET msg = ('Username is an empty String');
        SIGNAL sqlstate '45000' SET message_text = msg;
     END IF;
    END//

DELIMITER ;
