CREATE SCHEMA IF NOT EXISTS todo;
SET SCHEMA todo;

--
-- create todo table
--
create table TODO (
     INTEGER PRIMARY KEY AUTO_INCREMENT,
     uuid BINARY(16)          DEFAULT (UUID_TO_BIN(UUID(), 1)),
     created_date       	  DATE,
     last_modified_date 	  DATE,
     created_by_user          VARCHAR(100),
     last_modified_by_user    VARCHAR(100),
     name    	        	  VARCHAR(100),
     description    	      VARCHAR(500),
     status		           	  VARCHAR(100)
     )
);

--
-- create todo_item table
--
create table TODO_ITEM (

);



