Database tests

create some users first

SELECT registerAccount('user1', 'password');        returns a number > 0
SELECT registerAccount('user2', 'password');        returns a number > 0
SELECT registerAccount('user3', 'password');        returns a number > 0
SELECT registerAccount('user4', 'password');        returns a number > 0
SELECT registerAccount('', '');                     returns 0
SELECT registerAccount('nope', '');                 returns 0
SELECT registerAccount('', 'nope');                 returns 0

SELECT * FROM WEB_ACCOUNT;                          
You should see the users with the coresponding id

validating users should return their ID or 0 if they dont exsist

SELECT validateUser('user1', 'password');           returns a number > 0
SELECT validateUser('user2', 'password');           returns a number > 0
SELECT validateUser('user3', 'password');           returns a number > 0
SELECT validateUser('user4', 'password');           returns a number > 0
SELECT validateUser('java', 'password');            returns 0
SELECT validateUser('', '');                        returns 0
SELECT validateUser('java', '');                    returns 0
SELECT validateUser('', 'password');                returns 0

finding an account should return the user ID or 0 if there is an error

SELECT findAccount('user1');                        returns a number > 0
SELECT findAccount('user2');                        returns a number > 0
SELECT findAccount('user3');                        returns a number > 0
SELECT findAccount('Fail');                         returns 0
SELECT findAccount('');                             returns 0

setting the ard row in the WEB_ACCOUNT table
this returns nothing

CALL setARD(1,101);                                 -
CALL setARD(2,202);                                 -
CALL setARD(,);                                     ERROR 1064 (42000)
CALL setARD(3,);                                    ERROR 1064 (42000)
CALL setARD(,404);                                  ERROR 1064 (42000)

SELECT * FROM WEB_ACCOUNT;
check if the values are set as they should

trying to get the ard_id from the database

SELECT getARD(1);                                   returns 101
SELECT getARD(2);                                   returns 202
SELECT getARD(3);                                   returns NULL
SELECT getARD(4);                                   returns NULL
SELECT getARD(999);                                 returns NULL
SELECT getARD();                                    ERROR 1318 (42000)

removing the ard_id from the database

CALL deleteARD(1);                                  -
CALL deleteARD(2);                                  -
CALL deleteARD(2);                                  -
CALL deleteARD(999);                                -
CALL deleteARD();                                   ERROR 1318 (42000)

SELECT * FROM WEB_ACCOUNT;
make sure the ards are removed

add some resources

SELECT addResource('type');                         1
SELECT addResource('test');                         2
SELECT addResource('');                             3

trying to get the resource type

SELECT getResourceType (1);                         type
SELECT getResourceType (2);                         test
SELECT getResourceType (3);                         ''
SELECT getResourceType (4);                         NULL
SELECT getResourceType ();                          ERROR 1318 (42000)

creating maps (p_map_name VARCHAR(64), p_res_id INT(4), p_acc_id INT(4))

SELECT createMap('mapName0', 1, 1);                 returns number > 0
SELECT createMap('mapName2', 2, 2);                 returns number > 0
SELECT createMap('mapName3', 1, 2);                 returns number > 0
SELECT createMap('mapName4', 1, 2);                 returns number > 0
SELECT createMap('', 1, 2);                         ERROR 1644 (45000)
SELECT createMap('mapName4', , 2);                  ERROR 1064 (42000)
SELECT createMap('mapName4', 1, );                  ERROR 1064 (42000)
SELECT createMap('mapName4', 99, 2);                ERROR 1452 (23000)
SELECT createMap('mapName4', 1, 99);                ERROR 1452 (23000)

trying to get the map name

SELECT getMapName(1);                               mapName0
SELECT getMapName(2);                               mapName2
SELECT getMapName(3);                               mapName3
SELECT getMapName(4);                               mapName4
SELECT getMapName(9);                               NULL
SELECT getMapName();                                ERROR 1318 (42000)

trying to get the map resource

SELECT getMapResource(1);                           1
SELECT getMapResource(2);                           2
SELECT getMapResource(9);                           NULL
SELECT getMapResource();                            ERROR 1318 (42000)

creating some markers (p_name VARCHAR(64), p_map_id INT(4), p_res_id INT(4), p_lat INT(4), p_long INT(4))

CALL createMarker('marker1', 1, 0, 10, 10);         -
CALL createMarker('marker2', 1, 0, 10, 10);         -
CALL createMarker('marker1', 1, , 10, 10);          ERROR 1064 (42000)
CALL createMarker('marker1', , 0, 10, 10);          ERROR 1064 (42000)
CALL createMarker('', 1, 0, 10, 10);                -
CALL createMarker('marker1', 1, 0, , 10);           ERROR 1064 (42000)
CALL createMarker('marker1', 1, 0, 10, );           ERROR 1064 (42000)

SELECT * FROM MARKER;

getting the map markers

CALL getMapMarkers(1);                              a table of 3 makers
CALL getMapMarkers(2);                              empty set
CALL getMapMarkers(99);                             empty set
CALL getMapMarkers();                               ERROR 1318 (42000)

create some anchors  (p_map_id INT(4), p_local_x INT(4), p_local_y INT(4), p_lat INT(4), p_lng INT(4))

CALL createAnchor(1, 10, 10, 20, 20);               -
CALL createAnchor(1, 20, 20, 30, 30);               -
CALL createAnchor(1, 30, 30, 40, 40);               -
CALL createAnchor(9, 30, 30, 40, 40);               ERROR 1452 (23000)
CALL createAnchor(1, 30, 30, 40, );                 ERROR 1064 (42000)

getting the anchors of a map

CALL getMapAnchors(1);                              a table of 2 anchors
CALL getMapAnchors(9);                              empty set
CALL getMapAnchors();                               ERROR 1318 (42000)

getting the maps of a user

CALL getMapsList(1);                                a table of x lists
CALL getMapsList(9);                                empty set
CALL getMapsList();                                 ERROR 1318 (42000)

deleteing a map

CALL deleteMap(1);







