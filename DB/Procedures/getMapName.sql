/* *********************************************************************
**	Description:    Returns the name of a map
**
**  Returns:        The name
********************************************************************* */

DROP FUNCTION IF EXISTS getMapName;

DELIMITER //

CREATE FUNCTION getMapName 
(p_map_id INT(4), p_acc_id INT(4))
RETURNS VARCHAR(Web__MaxLength__MAP_NAME)
READS SQL DATA
BEGIN    
    RETURN (SELECT name
            FROM MAP
            WHERE p_map_id = map_id
            AND p_acc_id = web_account_id);
END//

DELIMITER ;
