/* *********************************************************************
**	Description:	Sets the marker lat and long to the specified values
********************************************************************* */


DROP PROCEDURE IF EXISTS editMarkerCord;

DELIMITER //

CREATE PROCEDURE editMarkerCord 
(p_marker_id INT(4),p_lat INT(4), p_long INT(4))
BEGIN
    UPDATE MARKER
    SET lat = p_lat
    WHERE marker_id = p_marker_id;

    UPDATE MARKER
    SET longitude = p_long
    WHERE marker_id = p_marker_id;
END//

DELIMITER ;
