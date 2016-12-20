package dao;

import org.junit.BeforeClass;

import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HibernateMySQLTest extends HibernateTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        Class.forName("org.postgresql.Driver");
        // Соединение с базой данных
        String database = "jdbc:mysql://localhost:3306/INFORMATION_SCHEMA";
        String dbUser = "root";
        String dbPassword = "";
        Connection con = DriverManager.getConnection(database, dbUser, dbPassword);
        // Добавляем данные в БД
        Statement statement = con.createStatement();
        // Удаляем всех пользователей
        statement.execute("CREATE DATABASE IF NOT EXISTS saveoursalary_test");

        emf = Persistence.createEntityManagerFactory("Unit-tests-MySQL");
    }
}