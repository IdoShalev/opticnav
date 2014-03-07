/* *********************************************************************
**	Description:
********************************************************************* */

DROP FUNCTION IF EXISTS getARD;

DELIMITER //

CREATE FUNCTION getARD 
(p_acc_id INT(4))
RETURNS INT
READS SQL DATA
BEGIN
    RETURN (SELECT ard_id
            FROM WEB_ACCOUNT
            WHERE p_acc_id = web_account_id);
END//

DELIMITER ;
