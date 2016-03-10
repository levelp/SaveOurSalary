package service;

import dao.UserDAO;

/**
 * Работа с пользователями
 */
public class UserService {
    public UserDAO userDAO = new UserDAO();

    public void createUser(String login, String password) throws Exception {
        userDAO.startConnection();
        try {
            userDAO.addUser(login, password);
        } finally {
            userDAO.closeConnection();
        }
    }
}
