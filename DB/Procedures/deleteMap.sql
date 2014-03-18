/* *********************************************************************
**	Description:	Deletes a map from the MAP table
********************************************************************* */


DROP PROCEDURE IF EXISTS deleteMap;

DELIMITER //

CREATE PROCEDURE deleteMap 
(p_map_id INT(4))
BEGIN
    CALL deleteAnchor(p_map_id);
    CALL deleteMarker(p_map_id);

    DELETE FROM MAP
    WHERE map_id = p_map_id;
END//

DELIMITER ;
