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
SET FOREIGN_KEY_CHECKS=0;

-- Table: todo
DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account(id 				BIGINT PRIMARY KEY AUTO_INCREMENT,
                   created_date       	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   last_modified_date 	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   user_name    	        VARCHAR(100) NOT NULL,
                   status		           	VARCHAR(100) NOT NULL,
                   UNIQUE(user_name))
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- Table: todo
DROP TABLE IF EXISTS todo;
CREATE TABLE todo(id 						BIGINT PRIMARY KEY AUTO_INCREMENT,
                   created_date       	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   last_modified_date 	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   created_by_user          VARCHAR(100) NOT NULL,
                   last_modified_by_user    VARCHAR(100) NOT NULL,
                   name    	        	    VARCHAR(100) NOT NULL,
                   description    	        VARCHAR(500),
                   status		           	VARCHAR(100) NOT NULL,
                   FOREIGN KEY (fk_user_account_id) REFERENCES user_account(id) ON DELETE SET NULL ON UPDATE CASCADE,
                    fk_user_account_id    	BIGINT,
                   UNIQUE(name))
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- Table: todo_item
DROP TABLE IF EXISTS todo_item;
CREATE TABLE todo_item(id 				    BIGINT PRIMARY KEY AUTO_INCREMENT,
                   created_date            	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   last_modified_date    	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   created_by_user          VARCHAR(100) NOT NULL,
                   last_modified_by_user    VARCHAR(100) NOT NULL,
                   name    	        	    VARCHAR(100) NOT NULL,
                   description    	        VARCHAR(500),
                   status		           	VARCHAR(100) NOT NULL,
                   action                   VARCHAR(50),
                   assigned_to              VARCHAR(25),
                   priority                 INTEGER,
                   FOREIGN KEY (fk_todo_id) REFERENCES todo(id) ON DELETE SET NULL ON UPDATE CASCADE,
                   fk_todo_id    	        BIGINT,
                   UNIQUE(name))
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- for auditing
-- needed by hibernate envers
DROP TABLE IF EXISTS revinfo;
CREATE TABLE revinfo (
    rev             INTEGER PRIMARY KEY AUTO_INCREMENT=1,
    revtstmp        BIGINT,
    CONSTRAINT revinfo_pkey PRIMARY KEY (rev)
)
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

DROP TABLE IF EXISTS todo_history;
CREATE TABLE todo_history (
    id bigint NOT NULL,
    name character varying(255),
    name_mod boolean,
    description character varying(255),
    description_mod boolean,
    status character varying(255),
    status_mod boolean,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by_user character varying(255),
    last_modified_by_user character varying(255),
    last_modified_by_user_mod boolean,
   -- used by auditing hibernate envers
    revision_id integer NOT NULL,
    revision_end_id integer,
    revision_type integer,
    FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT todo_aud_pkey PRIMARY KEY (id, revision_id),
    CONSTRAINT todo_aud_revinfo FOREIGN KEY (revision_id)
    REFERENCES revinfo (rev) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
    -- end hibernate envers
)
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

DROP TABLE IF EXISTS todo_item_history;
CREATE TABLE todo_item_history(
                   id 				        BIGINT NOT NULL,
                   fk_todo_id    	        BIGINT,
                   name    	        	    character varying(255),
                   name_mod                 boolean,
                   description    	        character varying(255),
                   description_mod          boolean,
                   status		           	character varying(255),
                   status_mod               boolean,
                   action                   character varying(255),
                   action_mod               boolean,
                   assigned_to              character varying(255),
                   priority                 integer,
                   priority_mod boolean,
                   assigned_to_mod boolean,
                   created_date TIMESTAMP,
                   last_modified_date TIMESTAMP,
                   created_by_user character varying(255),
                   last_modified_by_user character varying(255),
                   last_modified_by_user_mod boolean,
                    -- used by auditing hibernate envers
                   revision_id integer NOT NULL,
                   revision_end_id integer,
                   revision_type integer,
                   FOREIGN KEY (revision_end_id) REFERENCES revinfo(rev) ON DELETE SET NULL ON UPDATE CASCADE,
                   CONSTRAINT todo_item_aud_pkey PRIMARY KEY (id, revision_id),
                   CONSTRAINT todo_item_aud_revinfo FOREIGN KEY (revision_id)
                   REFERENCES revinfo (rev) MATCH SIMPLE
                   ON UPDATE NO ACTION ON DELETE NO ACTION
                   -- end hibernate envers
)
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- Turn on fk check
SET FOREIGN_KEY_CHECKS=1;
