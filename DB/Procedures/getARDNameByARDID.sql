/* *********************************************************************
**	Description:	Will return the name of the ARD belonging to the
**                  provided ard_id.
**  Returns:        0 if no ARD is registered to that user
**                  The name of the ARD if an ARD is registered.
********************************************************************* */

DROP FUNCTION IF EXISTS getARDNameByARDID;

DELIMITER //

CREATE FUNCTION getARDNameByARDID 
(p_ard_id INT)
RETURNS VARCHAR(32)
READS SQL DATA
BEGIN
    DECLARE v_name VARCHAR(32);
    SET v_name = '0';
    
    SELECT name INTO v_name
        FROM ARD
        WHERE ARD_id = p_ard_id;

    RETURN v_name;
END//

DELIMITER ;
