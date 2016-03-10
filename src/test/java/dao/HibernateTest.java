package dao;

import model.OperationCategory;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.UserService;

import javax.persistence.PersistenceException;
import java.util.List;

public class HibernateTest extends Assert {
    UserDAO userDAO;
    UserService userService;

    String getPersistenceUnitName() {
        return "Unit-tests-HSQLDB";
    }

    @Before
    public void setUp() {
        userDAO = new UserDAO();
        userService = new UserService();
    }

    /**
     * Добавляем одного пользователя, находим его по id
     */
    @Test
    public void testOneUser() throws Exception {
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
     * Автодополнение категорий операций
     */
    @Test
    public void testOperationCategoryAutocomplete() {
        OperationCategoryDAO categoryDAO = new OperationCategoryDAO();
        categoryDAO.persistenceUnitName = getPersistenceUnitName();
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