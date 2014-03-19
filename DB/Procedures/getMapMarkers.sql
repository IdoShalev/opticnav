/* *********************************************************************
**	Description:	
**
**  Returns:
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
