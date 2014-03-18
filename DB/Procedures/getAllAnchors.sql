/* *********************************************************************
**	Description:	
********************************************************************* */


DROP PROCEDURE IF EXISTS getAllAnchors;

DELIMITER //

CREATE PROCEDURE getAllAnchors
(p_map_id INT(4))
BEGIN
    SELECT * FROM ANCHOR
    WHERE map_id = p_map_id;
END//

DELIMITER ;
