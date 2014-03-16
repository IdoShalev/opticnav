CREATE TABLE WEB_ACCOUNT
(
    web_account_id      INT(4)      AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(25) NOT NULL UNIQUE,
    ard_id              INT(4),
    pass                BINARY(20)  NOT NULL
);

CREATE TABLE RESOURCE
(
    resource_id         INT(4)      AUTO_INCREMENT PRIMARY KEY,
    resource_type       VARCHAR(127) NOT NULL
);

CREATE TABLE MAP
(
    map_id              INT(4)      AUTO_INCREMENT PRIMARY KEY,
    web_account_id      INT(4)      NOT NULL,
    name                VARCHAR(64) NOT NULL,
    resource_id         INT(4)      NOT NULL,
    CONSTRAINT          fk_map_resource FOREIGN KEY (resource_id) REFERENCES RESOURCE(resource_id),
    CONSTRAINT          fk_map_web_account FOREIGN KEY (web_account_id) REFERENCES WEB_ACCOUNT(web_account_id)
);

CREATE TABLE MARKER
(
    marker_name         VARCHAR(64) ,
    map_id              INT(4)      NOT NULL,
    resource_id         INT(4)      ,
    lat                 INT(4)      NOT NULL,
    longitude           INT(4)      NOT NULL,
    CONSTRAINT          fk_marker_map FOREIGN KEY (map_id) REFERENCES MAP(map_id),
    CONSTRAINT          fk_marker_resource FOREIGN KEY (resource_id) REFERENCES RESOURCE(resource_id)
);

CREATE TABLE ANCHOR
(
    map_id              INT(4)      NOT NULL,
    image_x             INT(4)      NOT NULL,
    image_y             INT(4)      NOT NULL,
    lat                 INT(4)      NOT NULL,
    longitude           INT(4)      NOT NULL,
    CONSTRAINT          fk_anchor_map FOREIGN KEY (map_id) REFERENCES MAP(map_id)
);
