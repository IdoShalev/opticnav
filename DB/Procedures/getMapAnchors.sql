/* *********************************************************************
**	Description:	Get all rows from the ANCHOR table ehere the map id 
**                  is the one specified
**  Parameter:      p_map_id - The map ID
**  Returns:        A resultset containing anchor data
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
