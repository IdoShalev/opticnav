/* *********************************************************************
**	Description:	Sets an ARD ID to the specified WEB_ACCOUNT
**  Parameter:      p_acc_id - The account id
**                  p_ard_id - The ard ID
********************************************************************* */


DROP PROCEDURE IF EXISTS setARD;

DELIMITER //

CREATE PROCEDURE setARD 
(p_acc_id INT(4), p_ard_id INT(4))
BEGIN
    UPDATE WEB_ACCOUNT
    SET ard_id = p_ard_id
    WHERE web_account_id = p_acc_id;
END//

DELIMITER ;
