/* *********************************************************************
**	Description:    Gets the resource ID of the specified MAP
**
**  Returns:        The resource ID
********************************************************************* */

DROP FUNCTION IF EXISTS getMapResource;

DELIMITER //

CREATE FUNCTION getMapResource 
(p_map_id INT(4))
RETURNS INT
READS SQL DATA
BEGIN    
    RETURN (SELECT resource_id
            FROM MAP
            WHERE map_id = p_map_id);
END//

DELIMITER ;
