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
DROP TABLE IF EXISTS todo;
CREATE TABLE todo(id 						INTEGER PRIMARY KEY AUTO_INCREMENT,
                   global_unique_id         BIGINT,
                   uuid BINARY(16)          DEFAULT (UUID_TO_BIN(UUID(), 1)),
                   created_date       	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   last_modified_date 	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   created_by_user          VARCHAR(100) NOT NULL,
                   last_modified_by_user    VARCHAR(100) NOT NULL,
                   name    	        	    VARCHAR(100) NOT NULL,
                   description    	        VARCHAR(500) NOT NULL,
                   status		           	VARCHAR(100) NOT NULL,
                   UNIQUE(name))
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- Table: todo_item
DROP TABLE IF EXISTS todo_item;
CREATE TABLE todo_item(id 				    INTEGER PRIMARY KEY AUTO_INCREMENT,
                   uuid BINARY(16)          DEFAULT (UUID_TO_BIN(UUID(), 1)),
                   created_date            	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   last_modified_date    	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                   created_by_user          VARCHAR(100) NOT NULL,
                   last_modified_by_user    VARCHAR(100) NOT NULL,
                   name    	        	    VARCHAR(100),
                   status		           	VARCHAR(100),
                   FOREIGN KEY (fk_todo_id) REFERENCES todo(id) ON DELETE SET NULL ON UPDATE CASCADE,
                   fk_todo_id    	        INTEGER,
                   UNIQUE(name))
CHARACTER SET 'utf8'
COLLATE 'utf8_unicode_ci';

-- Turn on fk check
SET FOREIGN_KEY_CHECKS=1;
