/* *********************************************************************
**	Description:	Deletes a map from the MAP table
********************************************************************* */


DROP PROCEDURE IF EXISTS deleteMap;

DELIMITER //

CREATE PROCEDURE DeleteMap 
(p_map_id INT(4))
BEGIN
    DELETE FROM MAP
    WHERE map_id = p_map_id;
END//

DELIMITER ;
