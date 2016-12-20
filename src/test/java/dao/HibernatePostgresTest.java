package dao;

import org.junit.BeforeClass;

import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HibernatePostgresTest extends HibernateTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
        Class.forName("org.postgresql.Driver");
        // Соединение с базой данных
        String database = "jdbc:postgresql://localhost:5432/postgres";
        String dbUser = "postgres";
        String dbPassword = "123";
        Connection con = DriverManager.getConnection(database, dbUser, dbPassword);
        // Добавляем данные в БД
        Statement statement = con.createStatement();
        // Удаляем всех пользователей
        try {
            statement.execute("CREATE DATABASE saveoursalary_test");
        } catch (Exception ignored) {
        }

        emf = Persistence.createEntityManagerFactory("Unit-tests-Postgres");
    }
}