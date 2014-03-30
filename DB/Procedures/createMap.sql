/* *********************************************************************
**	Description:    Creates a Map
**
**  Returns:        The map ID
********************************************************************* */

DROP FUNCTION IF EXISTS createMap;

DELIMITER //

CREATE FUNCTION createMap 
(p_map_name VARCHAR(Web__MaxLength__MAP_NAME), p_res_id INT(4), p_acc_id INT(4))
RETURNS INT
DETERMINISTIC
BEGIN    
    INSERT INTO MAP
    (name, resource_id, web_account_id)
    VALUES
    (p_map_name, p_res_id, p_acc_id);    
    
    RETURN (SELECT `AUTO_INCREMENT`
            FROM  INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = (SELECT DATABASE() FROM DUAL)
            AND   TABLE_NAME   = 'MAP') - 1;
END//

DELIMITER ;
