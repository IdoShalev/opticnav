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

================================================================================

add some resources

SELECT addResource('type');                         1
SELECT addResource('test');                         2
SELECT addResource('');                             3  we might want to avoid that

SELECT getResourceType (2);

creating maps (p_map_name VARCHAR(64), p_res_id INT(4), p_acc_id INT(4))

SELECT createMap('mapName0', 1, 1);
SELECT createMap('mapName2', 2, 2);
SELECT createMap('mapName3', 1, 2);
SELECT createMap('mapName4', 1, 2);

createMarker

createAnchor

getMapName

getMapResource

getMapMarkers

getMapAnchors

getMapsList

deleteMap
