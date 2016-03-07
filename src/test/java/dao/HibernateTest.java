package dao;

import model.OperationCategory;
import model.User;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.NoResultException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class HibernateTest extends Assert {

    /**
     * Добавляем одного пользователя, находим его по id
     */
    @Test
    public void testOneUser() throws Exception {
        UserDAO userDAO = new UserDAO();
        userDAO.startConnection();
        try {
            userDAO.addUser("admin", "123");

            // Печатаем всех пользователей
            for (User u : userDAO.listALL()) {
                System.out.println(u.getId() + " login = " + u.getLogin());
            }

            // Это первый сохранённый пользователь, его id 1
            User user1 = userDAO.find(1);
            assertEquals("admin", user1.getLogin());
        } finally {
            userDAO.closeConnection();
        }
    }

    /**
     * База данных: MySQL
     *
     * @throws Exception
     */
    @Test
    public void testOneUserMySQL() throws Exception {
        createDatabase("saveoursalary_test");
        UserDAO userDAO = new UserDAO();
        userDAO.persistenceUnitName = "Unit-tests-MySQL";
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

    private void createDatabase(String databaseName) {
        try {
            String database = "jdbc:mysql://localhost:3306";
            String dbUser = "root";
            String dbPassword = "";
            Connection con = DriverManager.getConnection(
                    database, dbUser, dbPassword);
            // Добавляем данные в БД
            Statement statement = con.createStatement();
            // Удаляем всех пользователей
            System.out.println("Drop database: \"" + databaseName + "\"");
            statement.execute("DROP DATABASE " + databaseName);
            System.out.println("Create database: \"" + databaseName + "\"");
            statement.execute("CREATE DATABASE " + databaseName);
        } catch (SQLException sql) {
            if (sql.getMessage().contains("database exists")) {
                System.out.println("Пропускаем создание бд");
                return;
            }
            sql.printStackTrace();
        }
    }

    /**
     * Автодополнение категорий операций
     */
    @Test
    public void testOperationCategoryAutocomplete() {
        OperationCategoryDAO categoryDAO = new OperationCategoryDAO();
        categoryDAO.startConnection();

        categoryDAO.add("A1");
        categoryDAO.add("A3");
        categoryDAO.add("B1");
        categoryDAO.add("A2");

        List<OperationCategory> list = categoryDAO.autocomplete("A");
        assertEquals(3, list.size());
        assertEquals("A1", list.get(0).getName());
        assertEquals("A2", list.get(1).getName());
        assertEquals("A3", list.get(2).getName());

        categoryDAO.closeConnection();
    }
}