package dao;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

/**
 * Пример работы с БД через JDBC
 */
public class JdbcTest extends Assert {

    @Test
    public void testMySQL() throws ClassNotFoundException, SQLException {
        // Загружаем драйвер
        Class.forName("com.mysql.jdbc.Driver");
        // Соединение с базой данных
        String dbUser = "root";
        String dbPassword = "";
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/INFORMATION_SCHEMA", dbUser, dbPassword);
        // Добавляем данные в БД
        Statement statement = con.createStatement();
        // Удаляем всех пользователей
        statement.execute("CREATE DATABASE IF NOT EXISTS saveoursalary_jdbc_test");
        // Удобно для конфигурационных файлов:
        // Соединение с базой данных
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/saveoursalary_jdbc_test", dbUser, dbPassword);
        statement = con.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS `user` (\n" +
                "        id integer not null auto_increment,\n" +
                "        login varchar(255),\n" +
                "        passwordHash varchar(255),\n" +
                "        regDate varchar(255),\n" +
                "        primary key (id)\n" +
                "    )\n" +
                "ENGINE = INNODB\n" +
                "AUTO_INCREMENT = 1\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci\n" +
                "COMMENT = 'Пользователи'");
        // Удаляем всех пользователей
        statement.executeUpdate("DELETE FROM user");
        // Добавляем пользователя
        for (int i = 1; i < 6; i++) {
            assertEquals(1, statement.executeUpdate("INSERT INTO `user`(login, passwordHash)\n" +
                    "VALUES ('admin" + i + "', '234245')"));
        }
        // Получаем список всех пользователей
        // С помощью SELECT
        try (ResultSet list = statement.executeQuery("SELECT * FROM `user`")) {
            while (list.next()) {
                System.out.println("login = " + list.getString("login"));
            }
        }
    }
}
