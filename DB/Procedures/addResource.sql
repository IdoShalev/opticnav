/* *********************************************************************
**	Description:    Stores the mine type and the id of a Resource
**
**  Returns:        The ID of the Resource
**********************************************************************/

DROP FUNCTION IF EXISTS addResource;

DELIMITER //

CREATE FUNCTION addResource 
(p_type VARCHAR(127))
RETURNS INT
DETERMINISTIC
BEGIN
    INSERT INTO RESOURCE
    (resource_type)
    VALUES
    (p_type);

    RETURN (SELECT `AUTO_INCREMENT`
            FROM  INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = 'OpticNavDB'
            AND   TABLE_NAME   = 'RESOURCE') - 1;
END//

DELIMITER ;
