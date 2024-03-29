

------------------------------------------------------------------------------------------
-- DB name		 : Todo
-- DB Component	 : Tables
-- Release date	 : 31.01.2024
-- Schema Version: 1.0-SNAPSHOT
--
--
-- Modification History
-- Date			Modified by		Task
------------------------------------------------------------------------------------------
-- 31.01.2024	gunnarro 		Initial version 1.0-SNAPSHOT
------------------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS todo;
	-- create user and gran access
	-- CREATE USER 'todo-service'@'localhost' IDENTIFIED BY 'ABcd-2o1o';
	-- GRANT SELECT, INSERT, UPDATE, DELETE ON todo.* TO 'todo-service'@'localhost';
	-- GRANT ALL PRIVILEGES ON * . * TO 'admin'@'localhost'; only example for grant all
	-- FLUSH PRIVILEGES;
	-- SET collation_server=utf8_unicode_ci;
	-- SET character_set_server=utf8;
	-- Turn off fk check
	SET FOREIGN_KEY_CHECKS = 0;

-- Table: user_account
DROP TABLE IF EXISTS user_account;
	CREATE TABLE user_account (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		user_name VARCHAR(100) NOT NULL,
		status VARCHAR(100) NOT NULL,
		UNIQUE (user_name)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- Table: todo
DROP TABLE IF EXISTS todo;
	CREATE TABLE todo (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		created_by_user VARCHAR(100) NOT NULL,
		last_modified_by_user VARCHAR(100) NOT NULL,
		name VARCHAR(100) NOT NULL,
		description VARCHAR(500),
		status VARCHAR(100) NOT NULL,
		FOREIGN KEY (fk_user_account_id) REFERENCES user_account(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_user_account_id BIGINT,
		UNIQUE (name)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- Table: todo_item
DROP TABLE IF EXISTS todo_item;
	CREATE TABLE todo_item (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		created_by_user VARCHAR(100) NOT NULL,
		last_modified_by_user VARCHAR(100) NOT NULL,
		name VARCHAR(100) NOT NULL,
		category VARCHAR(100),
		description VARCHAR(500),
		status VARCHAR(100) NOT NULL,
		action VARCHAR(50) NOT NULL,
		price INTEGER,
		assigned_to VARCHAR(25),
		priority VARCHAR(25) NOT NULL,
		approval_required INTEGER,
		FOREIGN KEY (fk_todo_id) REFERENCES todo(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_id BIGINT,
		UNIQUE (name)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- Table: todo_participant
DROP TABLE IF EXISTS todo_participant;
	CREATE TABLE todo_participant (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		created_by_user VARCHAR(100) NOT NULL,
		last_modified_by_user VARCHAR(100) NOT NULL,
		name VARCHAR(100) NOT NULL,
		email VARCHAR(100),
		enabled INTEGER DEFAULT 0,
		FOREIGN KEY (fk_todo_id) REFERENCES todo(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_id BIGINT,
		UNIQUE (name)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- Table: todo_participant_lnk for many-to-many relations
DROP TABLE IF EXISTS todo_participant_lnk;
	CREATE TABLE todo_participant_lnk (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		FOREIGN KEY (fk_todo_id) REFERENCES todo(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_id BIGINT,
		FOREIGN KEY (fk_todo_participant_id) REFERENCES todo_participant(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_participant_id BIGINT,
		UNIQUE (
			fk_todo_id,
			fk_todo_participant_id
			)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

DROP TABLE IF EXISTS todo_item_approval;
	CREATE TABLE todo_item_approval (
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        created_by_user VARCHAR(100) NOT NULL,
        last_modified_by_user VARCHAR(100) NOT NULL,
		FOREIGN KEY (fk_todo_item_id) REFERENCES todo_item(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_item_id BIGINT,
		FOREIGN KEY (fk_todo_participant_id) REFERENCES todo_participant(id) ON DELETE SET NULL ON UPDATE CASCADE,
		fk_todo_participant_id BIGINT,
		approved INTEGER,
		UNIQUE (
			fk_todo_item_id,
			fk_todo_participant_id
			)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- ----------------------------------------------------------------------------------
-- for auditing
-- needed by hibernate envers
-- ----------------------------------------------------------------------------------
-- create revinfo table
DROP TABLE IF EXISTS revinfo;
	CREATE TABLE revinfo (
		rev INTEGER AUTO_INCREMENT,
		revtstmp BIGINT,
		CONSTRAINT revinfo_pkey PRIMARY KEY (rev)
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

ALTER TABLE revinfo AUTO_INCREMENT = 1;

-- create todo_history table
DROP TABLE IF EXISTS todo_history;
	CREATE TABLE todo_history (
		id BIGINT NOT NULL,
		name CHARACTER VARYING(255),
		name_mod boolean,
		description CHARACTER VARYING(255),
		description_mod boolean,
		status CHARACTER VARYING(255),
		status_mod boolean,
		created_date TIMESTAMP,
		last_modified_date TIMESTAMP,
		created_by_user CHARACTER VARYING(255),
		last_modified_by_user CHARACTER VARYING(255),
		last_modified_by_user_mod boolean,
		-- used by auditing hibernate envers
		revision_id INTEGER NOT NULL,
		revision_end_id INTEGER,
		revision_type INTEGER,
		FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
		CONSTRAINT todo_aud_pkey PRIMARY KEY (
			id,
			revision_id
			),
		CONSTRAINT todo_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo(rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
		-- end hibernate envers
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- create todo_item_history table
DROP TABLE IF EXISTS todo_item_history;
	CREATE TABLE todo_item_history (
		id BIGINT NOT NULL,
		fk_todo_id BIGINT,
		name CHARACTER VARYING(255),
		name_mod boolean,
		category CHARACTER VARYING(255),
		category_mod boolean,
		description CHARACTER VARYING(255),
		description_mod boolean,
		status CHARACTER VARYING(255),
		status_mod boolean,
		action CHARACTER VARYING(255),
		action_mod boolean,
		price CHARACTER VARYING(255),
		price_mod boolean,
		assigned_to CHARACTER VARYING(255),
		priority VARCHAR(25),
		priority_mod boolean,
		approval_required INTEGER,
        approval_required_mod boolean,
		assigned_to_mod boolean,
		created_date TIMESTAMP,
		last_modified_date TIMESTAMP,
		created_by_user CHARACTER VARYING(255),
		last_modified_by_user CHARACTER VARYING(255),
		last_modified_by_user_mod boolean,
		-- used by auditing hibernate envers
		revision_id INTEGER NOT NULL,
		revision_end_id INTEGER,
		revision_type INTEGER,
		FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
		CONSTRAINT todo_item_aud_pkey PRIMARY KEY (
			id,
			revision_id
			),
		CONSTRAINT todo_item_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo(rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
		-- end hibernate envers
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- create todo_participant_history table
DROP TABLE IF EXISTS todo_participant_history;
	CREATE TABLE todo_participant_history (
		id BIGINT NOT NULL,
		fk_todo_id BIGINT,
		name VARCHAR(100),
		name_mod BOOLEAN,
		email VARCHAR(100),
		email_mod BOOLEAN,
		enabled INTEGER DEFAULT 0,
		enabled_mod BOOLEAN,
		created_date TIMESTAMP,
		last_modified_date TIMESTAMP,
		created_by_user CHARACTER VARYING(255),
		last_modified_by_user CHARACTER VARYING(255),
		last_modified_by_user_mod BOOLEAN,
		-- used by auditing hibernate envers
		revision_id INTEGER NOT NULL,
		revision_end_id INTEGER,
		revision_type TINYINT,
		FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
		CONSTRAINT todo_participant_aud_pkey PRIMARY KEY (
			id,
			revision_id
			),
		CONSTRAINT todo_participant_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo(rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
		-- end hibernate envers
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';

-- create todo_item_approval_history table
DROP TABLE IF EXISTS todo_item_approval_history;
	CREATE TABLE todo_item_approval_history (
		id BIGINT NOT NULL,
		fk_todo_item_id BIGINT,
		fk_todo_participant_id BIGINT,
		created_date TIMESTAMP,
		last_modified_date TIMESTAMP,
		created_by_user CHARACTER VARYING(255),
		last_modified_by_user CHARACTER VARYING(255),
		last_modified_by_user_mod BOOLEAN,
		approved INTEGER,
		approved_mod BOOLEAN,
		-- used by auditing hibernate envers
		revision_id INTEGER NOT NULL,
		revision_end_id INTEGER,
		revision_type TINYINT,
		FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
		CONSTRAINT todo_item_approval_aud_pkey PRIMARY KEY (
			id,
			revision_id
			),
		CONSTRAINT todo_item_approval_aud_revinfo FOREIGN KEY (revision_id) REFERENCES revinfo(rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
		-- end hibernate envers
		) CHARACTER

SET 'utf8' COLLATE 'utf8_unicode_ci';
-- Turn on fk check
SET FOREIGN_KEY_CHECKS = 1;

