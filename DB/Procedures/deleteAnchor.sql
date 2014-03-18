/* *********************************************************************
**	Description:	
********************************************************************* */


DROP PROCEDURE IF EXISTS deleteAnchor;

DELIMITER //

CREATE PROCEDURE deleteAnchor 
(p_map_id INT(4))
BEGIN
    DELETE FROM ANCHOR
    WHERE map_id = p_map_id;
END//

DELIMITER ;
