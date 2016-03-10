package dao;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HibernateMySQLTest extends HibernateTest {
    String database = "jdbc:mysql://localhost:3306";
    String dbUser = "root";
    String dbPassword = "";
    String dbName = "saveoursalary_test";

    String getPersistenceUnitName() {
        return "Unit-tests-MySQL";
    }

    @Before
    public void setUp() {
        super.setUp();
        recreateDatabase();
        userDAO.persistenceUnitName = getPersistenceUnitName();
        userService.userDAO.persistenceUnitName = getPersistenceUnitName();
    }

    @After
    public void tearDown() {
    }

    /**
     * База данных: MySQL
     *
     * @throws Exception
     */
    @Test
    public void testOneUserMySQL() throws Exception {
        try {
            userDAO.startConnection();
            try {
                User user = userDAO.findByLogin("admin");
                if (user != null)
                    userDAO.remove(user);
                userDAO.closeConnection();
                userDAO.startConnection();
            } catch (NoResultException e) {
                System.out.println("Not found");
            }

            userDAO.addUser("admin", "123");

            // Печатаем всех пользователей
            for (User u : userDAO.listALL()) {
                System.out.println(u.getId() + " login = " + u.getLogin());
            }

            User user1 = userDAO.findByLogin("admin");
            assertEquals("admin", user1.getLogin());
            assertTrue(user1.checkPassword("123"));
        } finally {
            userDAO.closeConnection();
        }
    }

    private void recreateDatabase() {
        dropDatabase();
        createDatabase();
    }

    private void createDatabase() {
        try {
            // Создаем тестовую БД
            executeSQL("CREATE DATABASE " + dbName);
        } catch (SQLException sql) {
            if (sql.getMessage().contains("database exists")) {
                System.out.println("Пропускаем создание бд");
                return;
            }
            sql.printStackTrace();
        }
    }

    private void dropDatabase() {
        try {
            // Удаляем старую тестовую БД
            executeSQL("DROP DATABASE " + dbName);
        } catch (SQLException ignored) {
            // Ошибки игнорируем, т.к. операция не критична для тестов
        }
    }

    private void executeSQL(String sql) throws SQLException {
        Connection con = DriverManager.getConnection(database, dbUser, dbPassword);
        Statement statement = con.createStatement();
        System.out.println("Execute SQL: \"" + sql + "\"");
        statement.execute(sql);
    }

    @Test
    public void testCreateDuplicateLogin() throws Exception {
        userService.createUser("test", "123");
        try {
            userService.createUser("test", "888");
            fail("Не должно быть возможности создать пользователя с тем же логином");
        } catch (PersistenceException e) {
            System.out.println("All OK");
        }
    }
}