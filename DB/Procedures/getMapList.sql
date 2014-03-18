/* *********************************************************************
**	Description:	
********************************************************************* */


DROP PROCEDURE IF EXISTS getMapsList;

DELIMITER //

CREATE PROCEDURE getMapsList
(p_user_id INT(4))
BEGIN
    SELECT * FROM MAP
    WHERE web_account_id = p_user_id;
END//

DELIMITER ;
