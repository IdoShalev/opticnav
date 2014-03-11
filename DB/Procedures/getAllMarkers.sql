/* *********************************************************************
**	Description:	
**
**  Returns:
********************************************************************* */


DROP PROCEDURE IF EXISTS getAllMarkers;

DELIMITER //

CREATE PROCEDURE getAllMarkers
p_map_id INT(4))
BEGIN
    SELECT * FROM MARKER
    WHERE map_id = p_map_id;
END//

DELIMITER ;
