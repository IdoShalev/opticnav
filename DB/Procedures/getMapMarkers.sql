/* *********************************************************************
**	Description:	Gets all rows from the MARKER table where the map_id
**                  is the one specified
**  Parameter:      p_map_id - The map ID
**  Returns:        A result set with all marker data
********************************************************************* */


DROP PROCEDURE IF EXISTS getMapMarkers;

DELIMITER //

CREATE PROCEDURE getMapMarkers
(p_map_id INT(4))
BEGIN
    SELECT * FROM MARKER
    WHERE map_id = p_map_id;
END//

DELIMITER ;
