source /cs/trunk/DB/tables.sql;

SELECT registerAccount('kay', 'password');
SELECT registerAccount('ido', 'password');
SELECT registerAccount('danny', 'password');
SELECT registerAccount('kaykutxd', 'password');
SELECT registerAccount('', '');
SELECT registerAccount('nope', '');
SELECT registerAccount('', 'nope');

SELECT * FROM WEB_ACCOUNT;

SELECT validateUser('Kay', 'kaypass');
SELECT validateUser('Ido', 'idopass');
SELECT validateUser('danny', 'password');
SELECT validateUser('kay', 'fail');
SELECT validateUser('java', 'password');

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
