DELETE FROM ACCOUNT;

CALL registerAccount('kay', 'password');
CALL registerAccount('ido', 'password');
CALL registerAccount('danny', 'password');

SELECT * FROM ACCOUNT;

SELECT validateUser('kay', 'pass');

SELECT checkAccountName('lol');

SELECT findAccount('kai');

CALL createARD(1, 'password');
CALL createARD(2, 'pass');

SELECT * FROM ARD;

SELECT getARD(2);

SELECT validateARD('lol');
