/* *********************************************************************
**	Description:    Gets the name of the specified map
**  Parameter:      p_map_id - The map_id
**                  p_acc_id - The account_id
**  Returns:        The name
********************************************************************* */

DROP FUNCTION IF EXISTS getMapName;

DELIMITER //

CREATE FUNCTION getMapName 
(p_map_id INT(4), p_acc_id INT(4))
RETURNS VARCHAR(255)
READS SQL DATA
BEGIN    
    RETURN (SELECT name
            FROM MAP
            WHERE p_map_id = map_id
            AND p_acc_id = web_account_id);
END//

DELIMITER ;
