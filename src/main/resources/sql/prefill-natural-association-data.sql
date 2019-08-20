INSERT INTO na_space (name) VALUES ('Black');
INSERT INTO na_space (name) VALUES ('Blue');
INSERT INTO na_space (name) VALUES ('Green');
INSERT INTO na_space (name) VALUES ('Red');
INSERT INTO na_space (name) VALUES ('Yellow');

INSERT INTO na_space_role (name) VALUES ('Admin');
INSERT INTO na_space_role (name) VALUES ('Author');
INSERT INTO na_space_role (name) VALUES ('Reader');

INSERT INTO na_user (username, first_name, last_name) VALUES ('sull', 'Sebastian', 'Ull');
INSERT INTO na_user (username, first_name, last_name) VALUES ('jpre', 'Jan', 'Pre');

INSERT INTO na_space_role_membership (comment, space_name, space_role_name, user_username) VALUES ('Jan is Admin for Green Space', 'Green', 'Admin', 'jpre');
INSERT INTO na_space_role_membership (comment, space_name, space_role_name, user_username) VALUES ('Sebastian is Admin for Red Space', 'Red', 'Admin', 'sull');
INSERT INTO na_space_role_membership (comment, space_name, space_role_name, user_username) VALUES ('Sebastian is Author on Green Space', 'Green', 'Author', 'sull');
INSERT INTO na_space_role_membership (comment, space_name, space_role_name, user_username) VALUES ('Jan is Reader on Red Space', 'Red', 'Reader', 'jpre');