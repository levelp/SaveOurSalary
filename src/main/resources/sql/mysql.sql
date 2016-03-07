--
-- Создать базу данных "saveoursalary"
--
CREATE DATABASE saveoursalary;

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


SELECT COUNT(*) AS TotalNumber
FROM user
WHERE name = 'Иван';

INSERT INTO USER (login, passwordHash, NAME)
VALUES ('admin4', '234245', 'Иван');

UPDATE user
SET name = 'Аркадий'
WHERE login = 'admin4';

-- Удалить таблицу account
DROP TABLE account;