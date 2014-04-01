/* *********************************************************************
**	Description:    Removes the specified marker from the database
********************************************************************* */


DROP FUNCTION IF EXISTS deleteMarker;

DELIMITER //

CREATE FUNCTION deleteMarker 
(p_map_id INT(4), p_acc_id INT(4))
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE flag BOOLEAN;
    SET flag = false;

    IF (SELECT COUNT(web_account_id) 
        FROM MAP
        WHERE p_map_id = map_id
        ANd p_acc_id = web_account_id) != 0 THEN

                DELETE FROM MARKER
                    WHERE map_id = p_map_id
                    AND p_acc_id = (SELECT web_account_id
                                        FROM MAP
                                        WHERE p_map_id = map_id);

            SET flag = true;
    END IF;

    RETURN flag;
END//

DELIMITER ;
