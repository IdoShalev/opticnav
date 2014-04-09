/* *********************************************************************
**	Description:	Inserts a new row into the ANCHOR table
**  Parameter:      p_map_id  - the ID of the map this anchor belongs to
**                  p_local_x - the anchor's x coordinate
**                  p_local_y - the anchor's y coordinate
**                  p_lat - the anchor's latitude
**                  p_lng - the anchor's longitude
********************************************************************* */


DROP PROCEDURE IF EXISTS createAnchor;

DELIMITER //

CREATE PROCEDURE createAnchor
(p_map_id INT(4), p_local_x INT(4), p_local_y INT(4), p_lat INT(4), p_lng INT(4))
BEGIN
    INSERT INTO ANCHOR
    (map_id, image_x, image_y, lat, lng)
    VALUES
    (p_map_id, p_local_x, p_local_y, p_lat, p_lng);
END//

DELIMITER ;
