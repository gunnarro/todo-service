
--
-- add todo data
--
insert into todo(ID, UUID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY_USER, LAST_MODIFIED_BY_USER, NAME, DESCRIPTION, STATUS) values(1, '1fa31ddc-a692-41b9-9b6a-1ca172269fa9', CURRENT_DATE, CURRENT_DATE, 'guro', 'guro'. 'b39', 'todo list for bergslia 39', 'open');

--
-- add todo_item data
--
insert into todo_item(ID, UUID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY_USER, LAST_MODIFIED_BY_USER, NAME, DESCRIPTION, STATUS) values(1, '1fa31ddc-a692-41b9-9b6a-1ca172269fa9', CURRENT_DATE, CURRENT_DATE, 'guro', 'guro'. 'b39', 'todo list for bergslia 39', 'open');


-- mysql
-- insert data into TODO
INSERT INTO `todo`.`todo`
(`id`,`uuid`,`created_date`,`last_modified_date`,`created_by_user`,`last_modified_by_user`,`name`,`description`,`status`)
VALUES(10,UUID_TO_BIN(UUID(), 1),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'guro','guro-2','b39','todo list for bergslia 39','Open');

INSERT INTO `todo`.`todo`
(`id`,`uuid`,`created_date`,`last_modified_date`,`created_by_user`,`last_modified_by_user`,`name`,`description`,`status`)
VALUES(11,UUID_TO_BIN(UUID(), 1),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'guro','guro-2','stvgt35','todo list for stavangergata 35','Open');

-- insert data into TODO_ITEM
INSERT INTO `todo`.`todo_item`(`id`,`uuid`,`created_date`,`last_modified_date`,`created_by_user`,`last_modified_by_user`,`name`,`description`,`action`,`status`,`assigned_to`,`fk_todo_id`)
VALUES(100,UUID_TO_BIN(UUID(), 1),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'guro','guro','tv','stue','selges','Open','guro',10);

INSERT INTO `todo`.`todo_item`(`id`,`uuid`,`created_date`,`last_modified_date`,`created_by_user`,`last_modified_by_user`,`name`,`description`,`action`,`status`,`assigned_to`,`fk_todo_id`)
VALUES(101,UUID_TO_BIN(UUID(), 1),CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'guro','guro','sofa','stue','selges','Open','guro',10);
