CREATE TABLE WEB_ACCOUNT
(
    web_account_id      INT(4)      AUTO_INCREMENT PRIMARY KEY,
    user                VARCHAR(25) NOT NULL UNIQUE,
    pass                BINARY(20)  NOT NULL
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
