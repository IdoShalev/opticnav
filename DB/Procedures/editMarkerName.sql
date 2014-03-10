/* *********************************************************************
**	Description:	Sets the marker name to the specified value
********************************************************************* */


DROP PROCEDURE IF EXISTS editMarkerName;

DELIMITER //

CREATE PROCEDURE editMarkerName 
(p_marker_id INT(4),p_name VARCHAR(64))
BEGIN
    UPDATE MARKER
    SET marker_name = p_name
    WHERE marker_id = p_marker_id;
END//

DELIMITER ;
