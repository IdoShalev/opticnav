/* *********************************************************************
**	Description:    Inserts an new row into the RESOURCE table
**  Parameter:      p_type - the mime type of the resource
**  Returns:        The ID of the Resource
**********************************************************************/

DROP FUNCTION IF EXISTS addResource;

DELIMITER //

CREATE FUNCTION addResource 
(p_type VARCHAR(255))
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
