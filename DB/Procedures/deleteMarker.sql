/* *********************************************************************
**	Description:    Removes the specified marker from the database
********************************************************************* */


DROP PROCEDURE IF EXISTS deleteMarker;

DELIMITER //

CREATE PROCEDURE deleteMarker 
(p_marker_id INT(4))
BEGIN
    DELETE FROM MARKER
    WHERE marker_id = p_marker_id;
END//

DELIMITER ;
