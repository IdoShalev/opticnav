/* *********************************************************************
**	Description:	Sets the marker resource to the specified value
********************************************************************* */


DROP PROCEDURE IF EXISTS editMarkerResource;

DELIMITER //

CREATE PROCEDURE editMarkerResource 
(p_marker_id INT(4),p_res_id INT(4))
BEGIN
    UPDATE MARKER
    SET resource_id = p_res_id
    WHERE marker_id = p_marker_id;
END//

DELIMITER ;
