DROP TRIGGER IF EXISTS set_marker_resource_trigger;

DELIMITER //

CREATE TRIGGER set_marker_resource_trigger BEFORE INSERT ON `MARKER`
     FOR EACH ROW
     BEGIN
     IF (NEW.resource_id = 0)
     THEN
        SET NEW.resource_id = NULL;
     END IF;
    END//

DELIMITER ;
