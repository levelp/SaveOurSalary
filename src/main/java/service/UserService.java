package service;

import dao.UserDAO;

import javax.persistence.EntityManagerFactory;

/**
 * Работа с пользователями
 */
public class UserService {
    public UserDAO userDAO;

    public UserService(EntityManagerFactory emf) {
        userDAO = new UserDAO(emf);
    }

    public void createUser(String login, String password) throws Exception {
        try {
            userDAO.startConnection();
            userDAO.addUser(login, password);
        } finally {
            userDAO.closeConnection();
        }
    }
}
