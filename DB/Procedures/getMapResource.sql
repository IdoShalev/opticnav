/* *********************************************************************
**	Description:    Gets the resource_id of the specified MAP
**  Parameter:      p_map_id - The map_id
**  Returns:        The resource_id
********************************************************************* */

DROP FUNCTION IF EXISTS getMapResource;

DELIMITER //

CREATE FUNCTION getMapResource 
(p_map_id INT(4), p_acc_id INT(4))
RETURNS INT
READS SQL DATA
BEGIN    
    RETURN (SELECT resource_id
            FROM MAP
            WHERE p_map_id = map_id
            AND p_acc_id = web_account_id);
END//

DELIMITER ;
