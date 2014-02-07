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
        WHERE ARD_id = (SELECT ARD_id
                        FROM WEB_ACCOUNT
                        WHERE web_account_id = p_accountID);

    RETURN id;
END//

DELIMITER ;
