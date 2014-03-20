/* *********************************************************************
**	Description:    Removes the specified marker from the database
********************************************************************* */


DROP PROCEDURE IF EXISTS deleteMarker;

DELIMITER //

CREATE PROCEDURE deleteMarker 
(p_map_id INT(4))
BEGIN
    DELETE FROM MARKER
    WHERE map_id = p_map_id;
END//

DELIMITER ;
