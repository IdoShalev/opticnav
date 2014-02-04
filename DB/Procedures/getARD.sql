/* *********************************************************************
**	Description:	Will return the ARD_id of the ARD belonging to the
**                  provided account_id.
**  Returns:        0 if no ARD is registered to that user
**                  The ARD_id if an ARD is registered.
********************************************************************* */

DROP FUNCTION IF EXISTS getARD;

DELIMITER //

CREATE FUNCTION getARD 
(p_accountID INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE id INT;
    SET id = 0;
    
    SELECT ARD_id INTO id
        FROM ARD
        WHERE p_accountID = account_id;

    RETURN id;
END//

DELIMITER ;
