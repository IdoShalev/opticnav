/* *********************************************************************
**	Description:	Deletes all rows from the ANCHOR table that have
**                  the same map_id as the one specified
**  Parameter:      p_map_id - The map ID of the anchors to be deleted
**                  p_acc_id - The account ID of the map of the anchor to be deleted
**  Returns:        True if the anchors where deleted, false otherwise
********************************************************************* */


DROP FUNCTION IF EXISTS deleteAnchor;

DELIMITER //

CREATE FUNCTION deleteAnchor 
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

                DELETE FROM ANCHOR
                    WHERE map_id = p_map_id
                    AND p_acc_id = (SELECT web_account_id
                                        FROM MAP
                                        WHERE p_map_id = map_id);

            SET flag = true;
    END IF;

    RETURN flag;
END//

DELIMITER ;
