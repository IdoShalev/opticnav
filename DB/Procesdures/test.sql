DELETE FROM ACCOUNT;

CALL registerAccount('kay', 'password');

SELECT * FROM ACCOUNT;

SELECT validateUser('kay', 'pass');

SELECT checkAccountName('lol');

SELECT findAccount('kai');
