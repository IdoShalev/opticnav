/* *********************************************************************
**	Description:	This trigger checks if the username is an empty
**                  string before inserting it into the WEB_ACCOUNT table
**  Throws:         error 45000 with an appropriate message
********************************************************************* */

DROP TRIGGER IF EXISTS check_username_trigger;

DELIMITER //

CREATE TRIGGER check_username_trigger BEFORE INSERT ON `WEB_ACCOUNT`
     FOR EACH ROW
     BEGIN
     DECLARE msg VARCHAR(255);
     IF (NEW.username = '')
     THEN
        SET msg = ('Username is an empty String');
        SIGNAL sqlstate '45000' SET message_text = msg;
     END IF;
    END//

DELIMITER ;
