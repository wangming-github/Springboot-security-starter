use RBAC;
create table oauth_client_details
(
    client_id               VARCHAR(256) PRIMARY KEY,
    resource_ids            VARCHAR(256),
    client_secret           VARCHAR(256),
    scope                   VARCHAR(256),
    authorized_grant_types  VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities             VARCHAR(256),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(256)
);

INSERT INTO `oauth_client_details`
VALUES ('client1',
        null,
        '$2a$10$/Ci6DDwsvM6/dk9XOkPivuCCX.5WCLDM2H4VchKLyee4NIZdIVapW',
        'all',
        'authorization_code,password,client_credentials,implicit,refresh_token',
        'http://localhost:8888/callback',
        null, '300', '1500', null, 'false');


Client ID
b06abc14ba1cd6d24c0d
Client secret
1a6df8600cd87fe8f4689721db0fe42c112c92ba