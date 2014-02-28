source /cs/trunk/DB/tables.sql;

CALL registerAccount('kay', 'password');
CALL registerAccount('ido', 'password');
CALL registerAccount('danny', 'password');
CALL registerAccount('kaykutxd', 'password');

SELECT * FROM WEB_ACCOUNT;

SELECT validateUser('kay', 'password');
SELECT validateUser('ido', 'password');
SELECT validateUser('danny', 'password');
SELECT validateUser('kay', 'fail');

SELECT checkAccountName('kay');
SELECT checkAccountName('ido');
SELECT checkAccountName('danny');
SELECT checkAccountName('fail');

SELECT findAccount('kay');
SELECT findAccount('ido');
SELECT findAccount('danny');
SELECT findAccount('fail');

CALL createARD('tacocat', 'password');
CALL createARD('theard', 'pass');
CALL createARD('theard', 'lol');
CALL createARD('fail?', 'password');

SELECT * FROM ARD;

SELECT validateARD('password');
SELECT validateARD('pass');
SELECT validateARD('fail');

CALL connectARD(1, 1);
CALL connectARD(2, 2);
CALL connectARD(1, 2);

SELECT getARD(1);
SELECT getARD(2);
SELECT getARD(3);
SELECT getARD(4);

SELECT getARDNameByARDID(1);
SELECT getARDNameByARDID(2);
SELECT getARDNameByARDID(3);
