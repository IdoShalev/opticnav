/* *********************************************************************
**	Description:	Gets all rows from the MAP table where the account_id
**                  is the one specified
**  Parameter:      p_user_id - The user ID
**  Returns:        A result set containing map data
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
