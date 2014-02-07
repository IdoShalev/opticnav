/* *********************************************************************
**	Description:	Will add a new row to the ARD table belonging to
**                  the provided account name, the provided password 
**                  in a hashed binary form, and an auto incementing
**                  ARD id.
********************************************************************* */

DROP PROCEDURE IF EXISTS createARD;

DELIMITER //

CREATE PROCEDURE createARD 
(p_ard_name VARCHAR(32), p_password VARCHAR(20))
BEGIN
    INSERT INTO ARD
    (name, passcode)
    VALUES (p_ard_name, UNHEX(SHA1(p_password)));
END//

DELIMITER ;
