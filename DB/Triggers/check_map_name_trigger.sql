/* *********************************************************************
**	Description:	This trigger checks the map name and the map resource
**                  before inserting the data into the MAP table
**                  It checks if the map name is an empty string and
**                  if the map resource id exists
**  Throws:         error 45000 with an appropriate message
********************************************************************* */

DROP TRIGGER IF EXISTS check_map_name_trigger;

DELIMITER //

CREATE TRIGGER check_map_name_trigger BEFORE INSERT ON `MAP`
     FOR EACH ROW
     BEGIN
     DECLARE msg VARCHAR(255);
     IF (NEW.name = '')
     THEN
        SET msg = ('Map name is an empty String');
        SIGNAL sqlstate '45000' SET message_text = msg;
     END IF;

     IF (SELECT EXISTS(SELECT 1 FROM RESOURCE WHERE resource_id = NEW.resource_id) = 0)
     THEN
        SET msg = ('Map resource does not exist');
        SIGNAL sqlstate '45000' SET message_text = msg;
     END IF;
    END//

DELIMITER ;
