/* *********************************************************************
**	Description:    Inserts a new row into the MARKER table
**  Parameter:      p_name - The name of the marker
**                  p_map_id - The ID of the map the marker belongs to
**                  p_res_id - The marker's resource ID
**                  p_lat - The markers latitude
**                  p_long - The markers longitude
********************************************************************* */

DROP PROCEDURE IF EXISTS createMarker;

DELIMITER //

CREATE PROCEDURE createMarker 
(p_name VARCHAR(Web__MaxLength__MARKER_NAME),
p_map_id INT(4), p_res_id INT(4), p_lat INT(4), p_long INT(4))
BEGIN
    INSERT INTO MARKER
    (marker_name, map_id, resource_id, lat, lng)
    VALUES
    (p_name, p_map_id, p_res_id, p_lat, p_long);
END//

DELIMITER ;
