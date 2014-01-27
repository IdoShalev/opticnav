
DROP TABLE INVITE;
DROP TABLE MESSAGE;
DROP TABLE MARKER;
DROP TABLE MAP;
DROP TABLE NOTIFICATION;
DROP TABLE ARD;
DROP TABLE ACCOUNT;
DROP TABLE RESOURCE;


CREATE TABLE ACCOUNT
(
    account_id          INT(4)      AUTO_INCREMENT PRIMARY KEY,
    user                VARCHAR(25) NOT NULL,
    pass                BINARY(20)  NOT NULL
);

CREATE TABLE ARD
(
    ARD_id              INT(4)      AUTO_INCREMENT PRIMARY KEY,
    account_id          INT(4)      NOT NULL,
    passcode            BINARY(20)  NOT NULL,
    CONSTRAINT          fk_ard_account FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id)
);

CREATE TABLE NOTIFICATION
(
    notification_id     INT(4)      AUTO_INCREMENT PRIMARY KEY,
    account_id          INT(4)      NOT NULL,
    viewed              BOOLEAN     NOT NULL,
    type                VARCHAR(16) NOT NULL,
    payload_id          INT(4)      NOT NULL,
    CONSTRAINT          fk_notification_account FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id)
);

CREATE TABLE RESOURCE
(
    resource_id         INT(4)      AUTO_INCREMENT PRIMARY KEY,
    resource_type       VARCHAR(16) NOT NULL,
    resource_uri        VARCHAR(256) NOT NULL
);

CREATE TABLE MAP
(
    map_id              INT(4)      AUTO_INCREMENT PRIMARY KEY,
    account_id          INT(4)      NOT NULL,
    name                VARCHAR(64) NOT NULL,
    resource_id         INT(4)      NOT NULL,
    CONSTRAINT          fk_map_account FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id),
    CONSTRAINT          fk_map_resource FOREIGN KEY (resource_id) REFERENCES RESOURCE(resource_id)
);

CREATE TABLE MARKER
(
    marker_id           INT(4)      AUTO_INCREMENT PRIMARY KEY,
    map_id              INT(4)      NOT NULL,
    resource_id         INT(4)      NOT NULL,
    lat                 INT(4)      NOT NULL,
    longitude           INT(4)      NOT NULL,
    CONSTRAINT          fk_marker_map FOREIGN KEY (map_id) REFERENCES MAP(map_id),
    CONSTRAINT          fk_marker_resource FOREIGN KEY (resource_id) REFERENCES RESOURCE(resource_id)
);

CREATE TABLE MESSAGE
(
    message_id          INT(4)      AUTO_INCREMENT PRIMARY KEY,
    text                VARCHAR(64) NOT NULL
);

CREATE TABLE INVITE
(
    invite_id           INT(4)      AUTO_INCREMENT PRIMARY KEY,
    text                VARCHAR(64) NOT NULL,
    ardDeamonInstance   BINARY(16)  NOT NULL,
    sessionID           INT(4)      NOT NULL
);















