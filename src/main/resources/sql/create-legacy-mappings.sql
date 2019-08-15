TRUNCATE TABLE na_space;
TRUNCATE TABLE na_space_role;
TRUNCATE TABLE na_user_account;
TRUNCATE TABLE na_legacy_mapping;

INSERT INTO na_space (name) VALUES ('Black');
INSERT INTO na_space (name) VALUES ('Blue');
INSERT INTO na_space (name) VALUES ('Green');
INSERT INTO na_space (name) VALUES ('Red');
INSERT INTO na_space (name) VALUES ('Yellow');

INSERT INTO na_space_role (name) VALUES ('Admin');
INSERT INTO na_space_role (name) VALUES ('Author');
INSERT INTO na_space_role (name) VALUES ('Reader');

INSERT INTO na_user_account (username, first_name, last_name) VALUES ('sullrich', 'Sebastian', 'Ullrich');
INSERT INTO na_user_account (username, first_name, last_name) VALUES ('jpretzel', 'Jan', 'Pretzel');

INSERT INTO na_legacy_mapping (comment, space_name, space_role_name, user_account_username) VALUES ('Jan is Admin for Green Space', 'Green', 'Admin', 'jpretzel');
INSERT INTO na_legacy_mapping (comment, space_name, space_role_name, user_account_username) VALUES ('Sebastian is Admin for Red Space', 'Red', 'Admin', 'sullrich');
INSERT INTO na_legacy_mapping (comment, space_name, space_role_name, user_account_username) VALUES ('Sebastian is Author on Green Space', 'Green', 'Author', 'sullrich');
INSERT INTO na_legacy_mapping (comment, space_name, space_role_name, user_account_username) VALUES ('Jan is Reader on Red Space', 'Red', 'Reader', 'jpretzel');