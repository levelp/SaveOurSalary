package dao;

import model.OperationCategory;
import model.User;
import org.junit.*;
import service.UserService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

public class HibernateTest extends Assert {
    static EntityManagerFactory emf;

    UserDAO userDAO;
    UserService userService;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("Unit-tests-HSQLDB");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Close EntityManagerFactory");
        emf.close();
    }

    @Before
    public void setUp() throws IOException {
        userDAO = new UserDAO(emf);
        userService = new UserService(emf);
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

    @Test
    public void testOneUserFindByLogin() throws Exception {
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

    /**
     * Автодополнение категорий операций
     */
    @Test
    public void testOperationCategoryAutocomplete() {
        OperationCategoryDAO categoryDAO = new OperationCategoryDAO(emf);
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
        } catch (UserAlreadyExistsException e) {
            System.out.println("All OK");
        }
    }

    String getPersistenceProperty(String propertyName) {
        return (String) emf.getProperties().get(propertyName);
    }

    @Test
    public void testGetConnectionProperties() {
        String database = getPersistenceProperty("javax.persistence.jdbc.url");
        String dbUser = getPersistenceProperty("javax.persistence.jdbc.user");
        // Невозможно получить пароль в явном виде
        String dbPassword = getPersistenceProperty("javax.persistence.jdbc.password");
        System.out.println("database = " + database);
        System.out.println("dbUser = " + dbUser);
        System.out.println("dbPassword = " + dbPassword);
    }
}