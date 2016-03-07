package dao;

import com.mysql.jdbc.Driver;
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
        Driver driver = new com.mysql.jdbc.Driver();
        // Удобно для конфигурационных файлов:
        // Class.forName("com.mysql.jdbc.Driver");
        // Соединение с базой данных
        String database = "jdbc:mysql://localhost:3306/saveoursalary";
        String dbUser = "root";
        String dbPassword = "";
        Connection con = DriverManager.getConnection(
                database, dbUser, dbPassword);
        // Добавляем данные в БД
        Statement statement = con.createStatement();
        // Удаляем всех пользователей
        statement.executeUpdate("DELETE FROM user");
        // Добавляем пользователя
        for (int i = 1; i < 6; i++) {
            assertEquals(1, statement.executeUpdate("INSERT INTO USER (login, passwordHash, NAME)\n" +
                    "VALUES ('admin" + i + "', '234245', 'Иван')"));
        }
        // Получаем список всех пользователей
        // С помощью SELECT
        try (ResultSet list = statement.executeQuery("SELECT * FROM user")) {
            while (list.next()) {
                System.out.println("login = " + list.getString("login"));
            }
            //list.close();
        }

        try {
            statement.execute("DROP TABLE account");
        } catch (Exception ignored) {
        }

        assertFalse(statement.execute("CREATE TABLE account (\n" +
                "  id INT(11) NOT NULL AUTO_INCREMENT,\n" +
                "  amount DECIMAL(10, 2) DEFAULT NULL,\n" +
                "  user_id INT(11) NOT NULL,\n" +
                "  PRIMARY KEY (id),\n" +
                "  CONSTRAINT FK_account_user_id FOREIGN KEY (user_id)\n" +
                "  REFERENCES saveoursalary.user (id) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                ")\n" +
                "ENGINE = INNODB\n" +
                "AUTO_INCREMENT = 1\n" +
                "CHARACTER SET utf8\n" +
                "COLLATE utf8_general_ci\n" +
                "COMMENT = 'Счёт пользователя'"));
    }
}
