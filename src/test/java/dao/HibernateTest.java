package dao;

import model.OperationCategory;
import model.User;
import org.junit.Assert;
import org.junit.Test;

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