DELETE FROM ACCOUNT;
source /cs/trunk/DB/tables.sql;


CALL registerAccount('kay', 'password');
CALL registerAccount('ido', 'password');
CALL registerAccount('danny', 'password');

SELECT * FROM WEB_ACCOUNT;

SELECT validateUser('kay', 'password');
SELECT validateUser('kay', 'pass');

SELECT checkAccountName('kay');
SELECT checkAccountName('fail');

SELECT findAccount('kay');
SELECT findAccount('fail');

CALL createARD('tacocat', 'password');
CALL createARD('theard', 'pass');

SELECT * FROM ARD;

SELECT validateARD('password');
SELECT validateARD('pass');
SELECT validateARD('fail');

CALL connectARD(1, 1);
CALL connectARD(2, 2);

SELECT getARD(2);

SELECT getARDNameByARDID(1);
