/* *********************************************************************
**	Description:	
********************************************************************* */


DROP PROCEDURE IF EXISTS getMapAnchors;

DELIMITER //

CREATE PROCEDURE getMapAnchors
(p_map_id INT(4))
BEGIN
    SELECT * FROM ANCHOR
    WHERE map_id = p_map_id;
END//

DELIMITER ;
