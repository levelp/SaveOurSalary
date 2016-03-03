--
-- Создать базу данных "saveoursalary"
--
CREATE DATABASE IF NOT EXISTS saveoursalary;

use saveoursalary;

CREATE TABLE user (
  id           INT(11) NOT NULL AUTO_INCREMENT
  COMMENT 'Идентификатор пользователя',
  login        VARCHAR(255)     DEFAULT NULL,
  passwordHash VARCHAR(255)     DEFAULT NULL,
  PRIMARY KEY (id)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  COMMENT = 'Пользователи';



INSERT INTO USER (login, passwordHash)
VALUES ('admin', '234245');

/*
SELECT COUNT(*) AS TotalNumber
FROM user
WHERE name = 'Иван';


UPDATE user
SET name = 'Аркадий'
WHERE login = 'admin4';

-- Удалить таблицу account
DROP TABLE account;
*/