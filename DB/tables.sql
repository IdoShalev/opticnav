
DROP TABLE WEB_ACCOUNT_OWNED_MAPS;
DROP TABLE INVITE;
DROP TABLE MESSAGE;
DROP TABLE MARKER;
DROP TABLE MAP;
DROP TABLE NOTIFICATION;
DROP TABLE WEB_ACCOUNT;
DROP TABLE ARD;
DROP TABLE RESOURCE;

CREATE TABLE ARD
(
    ARD_id              INT(4)      AUTO_INCREMENT PRIMARY KEY,
    passcode            BINARY(20)  NOT NULL UNIQUE,
    name                VARCHAR(32)
);

CREATE TABLE WEB_ACCOUNT
(
    web_account_id      INT(4)      AUTO_INCREMENT PRIMARY KEY,
    ard_id              INT(4)      NULL,
    user                VARCHAR(25) NOT NULL UNIQUE,
    pass                BINARY(20)  NOT NULL,
    CONSTRAINT          fk_web_account_ard FOREIGN KEY (ard_id) REFERENCES ARD(ard_id)
);

CREATE TABLE NOTIFICATION
(
    notification_id     INT(4)      AUTO_INCREMENT PRIMARY KEY,
    web_account_id      INT(4)      NOT NULL,
    viewed              BOOLEAN     NOT NULL,
    type                VARCHAR(16) NOT NULL,
    payload_id          INT(4)      NOT NULL,
    CONSTRAINT          fk_notification_account FOREIGN KEY (web_account_id) REFERENCES WEB_ACCOUNT(web_account_id)
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
    name                VARCHAR(64) NOT NULL,
    resource_id         INT(4)      NOT NULL,
    CONSTRAINT          fk_map_resource FOREIGN KEY (resource_id) REFERENCES RESOURCE(resource_id)
);

CREATE TABLE WEB_ACCOUNT_OWNED_MAPS
(
    web_account_id      INT(4),
    map_id              INT(4),
    PRIMARY KEY(web_account_id, map_id),
    CONSTRAINT          fk_web_account_owned_maps_web_account FOREIGN KEY (web_account_id) REFERENCES WEB_ACCOUNT(web_account_id),
    CONSTRAINT          fk_web_account_owned_maps_map FOREIGN KEY (map_id) REFERENCES MAP(map_id)
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















