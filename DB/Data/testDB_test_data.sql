/* Creating some test data to test procedures */


SELECT registerAccount('user1', 'pass');
SELECT registerAccount('user2', 'pass');
SELECT registerAccount('user3', 'pass');
SELECT registerAccount('user4', 'pass');

SELECT registerAccount('Kay', 'kaypass');
SELECT registerAccount('Ido', 'idopass');
SELECT registerAccount('Danny', 'dannypass');
SELECT registerAccount('Jacky', 'jackypass');

SELECT addResource('res1');
SELECT addResource('res2');
SELECT addResource('res3');
SELECT addResource('res4');
SELECT addResource('res5');

/* creating maps (p_map_name VARCHAR(Web__MaxLength__MAP_NAME), p_res_id INT(4), p_acc_id INT(4)) */

SELECT createMap('mapName1', 1, 1);
SELECT createMap('mapName2', 2, 1);
SELECT createMap('mapName3', 3, 1);
SELECT createMap('mapName4', 4, 2);
SELECT createMap('mapName5', 5, 2);


/* creating some markers (p_name VARCHAR(Web__MaxLength__MARKER_NAME), p_map_id INT(4), p_res_id INT(4), p_lat INT(4), p_long INT(4)) */

CALL createMarker('marker1', 1, 0, 10, 10);
CALL createMarker('marker2', 1, 0, 20, 20);
CALL createMarker('marker3', 1, 0, 30, 30);

CALL createMarker('marker1', 2, 0, 10, 10);
CALL createMarker('marker2', 2, 0, 20, 20);
CALL createMarker('marker3', 2, 0, 30, 30);

CALL createMarker('marker1', 4, 0, 10, 10);
CALL createMarker('marker2', 4, 0, 20, 20);
CALL createMarker('marker3', 4, 0, 30, 30);

/* create some anchors  (p_map_id INT(4), p_local_x INT(4), p_local_y INT(4), p_lat INT(4), p_lng INT(4)) */

CALL createAnchor(1, 10, 10, 20, 20);
CALL createAnchor(1, 10, 10, 20, 20);
CALL createAnchor(1, 10, 10, 20, 20);

CALL createAnchor(2, 10, 10, 20, 20);
CALL createAnchor(2, 10, 10, 20, 20);
CALL createAnchor(2, 10, 10, 20, 20);

CALL createAnchor(4, 10, 10, 20, 20);
CALL createAnchor(4, 10, 10, 20, 20);
CALL createAnchor(4, 10, 10, 20, 20);
