/* *********************************************************************
**	Description:    Gets the resource type of the specified RESOURCE
**
**  Returns:        The resource type
********************************************************************* */

DROP FUNCTION IF EXISTS getResourceType;

DELIMITER //

CREATE FUNCTION getResourceType
(p_res_id INT(4))
RETURNS VARCHAR(Web__MaxLength__RESOURCE_TYPE)
READS SQL DATA
BEGIN    
    RETURN (SELECT resource_type
            FROM RESOURCE
            WHERE resource_id = p_res_id);
END//

DELIMITER ;
