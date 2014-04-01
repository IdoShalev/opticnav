/* *********************************************************************
**	Description:	Deletes a map from the MAP table
********************************************************************* */


DROP FUNCTION IF EXISTS deleteMap;

DELIMITER //

CREATE FUNCTION deleteMap 
(p_map_id INT(4), p_acc_id INT(4))
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE flag BOOLEAN;
    SET flag = false;
    IF (SELECT COUNT(web_account_id)
        FROM MAP
        WHERE p_map_id = map_id
        AND p_acc_id = web_account_id) != 0 THEN
            SELECT deleteAnchor(p_map_id, p_acc_id) INTO flag;
            SELECT deleteMarker(p_map_id, p_acc_id) INTO flag;

            DELETE FROM MAP
            WHERE map_id = p_map_id
            AND p_acc_id = web_account_id;
            
            SET flag = true;
    END IF;
    RETURN flag;
END//

DELIMITER ;


    
