/* *********************************************************************
**	Description:    Stores a marker in the database
**
**  Returns:        The marker ID
********************************************************************* */

DROP FUNCTION IF EXISTS createMarker;

DELIMITER //

CREATE FUNCTION createMarker 
(p_name VARCHAR(64), p_map_id INT(4), p_res_id INT(4), p_lat INT(4), p_long INT(4))
RETURNS INT
DETERMINISTIC
BEGIN
    INSERT INTO MARKER
    (marker_name, map_id, resource_id, lat, longitude)
    VALUES
    (p_name, p_map_id, p_res_id, p_lat, p_long);

    RETURN (SELECT `AUTO_INCREMENT`
            FROM  INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = 'OpticNavDB'
            AND   TABLE_NAME   = 'MAP') - 1;
END//

DELIMITER ;
