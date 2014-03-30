/* *********************************************************************
**	Description:    Stores the mime type and the id of a Resource
**
**  Returns:        The ID of the Resource
**********************************************************************/

DROP FUNCTION IF EXISTS addResource;

DELIMITER //

CREATE FUNCTION addResource 
(p_type VARCHAR(Web__MaxLength__RESOURCE_TYPE))
RETURNS INT
DETERMINISTIC
BEGIN
    INSERT INTO RESOURCE
    (resource_type)
    VALUES
    (p_type);

    RETURN (SELECT `AUTO_INCREMENT`
            FROM  INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = (SELECT DATABASE() FROM DUAL)
            AND   TABLE_NAME   = 'RESOURCE') - 1;
END//

DELIMITER ;
