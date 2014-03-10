/* *********************************************************************
**	Description:	Deletes the resource with the specified ID
********************************************************************* */


DROP PROCEDURE IF EXISTS removeResource;

DELIMITER //

CREATE PROCEDURE removeResource 
(p_res_id INT(4))
BEGIN
    DELETE FROM RESOURCE
    WHERE resource_id = p_res_id;
END//

DELIMITER ;
