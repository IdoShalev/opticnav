DROP TRIGGER IF EXISTS check_map_name_trigger;

DELIMITER //

CREATE TRIGGER check_map_name_trigger BEFORE INSERT ON `MAP`
     FOR EACH ROW
     BEGIN
     DECLARE msg varchar(255);
     IF (NEW.name = '')
     THEN
        SET msg = ('Map name is an empty String');
        SIGNAL sqlstate '45000' SET message_text = msg;
     END IF;
    END//

DELIMITER ;
