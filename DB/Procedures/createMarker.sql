/* *********************************************************************
**	Description:    Stores a marker in the database
********************************************************************* */

DROP PROCEDURE IF EXISTS createMarker;

DELIMITER //

CREATE PROCEDURE createMarker 
(p_name VARCHAR(64), p_map_id INT(4), p_res_id INT(4), p_lat INT(4), p_long INT(4))
BEGIN
    INSERT INTO MARKER
    (marker_name, map_id, resource_id, lat, lng)
    VALUES
    (p_name, p_map_id, p_res_id, p_lat, p_long);
END//

DELIMITER ;
