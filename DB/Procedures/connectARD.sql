/* *********************************************************************
**	Description:	Will couple an ARD to a web user account
********************************************************************* */

DROP PROCEDURE IF EXISTS connectARD;

DELIMITER //

CREATE PROCEDURE connectARD 
(p_acc_id INT, p_ard_id INT)
BEGIN
    UPDATE WEB_ACCOUNT
    SET ard_id = p_ard_id
    WHERE web_account_id = p_acc_id;
END//

DELIMITER ;
