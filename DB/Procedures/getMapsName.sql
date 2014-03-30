/* *********************************************************************
**	Description:    Returns the name of a map
**
**  Returns:        The name
********************************************************************* */

DROP FUNCTION IF EXISTS getMapName;

DELIMITER //

CREATE FUNCTION getMapName 
(p_map_id INT(4))
RETURNS VARCHAR(Web__MaxLength__MAP_NAME)
READS SQL DATA
BEGIN    
    RETURN (SELECT name
            FROM MAP
            WHERE map_id = p_map_id);
END//

DELIMITER ;
