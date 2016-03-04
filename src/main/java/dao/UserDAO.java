package dao;

import model.User;

import java.util.List;

public class UserDAO extends DAO<User> {
    public User find(int id) {
        return em.find(User.class, id);
    }

    public List<User> listALL() {
        return em.createNamedQuery(User.ALL_USERS, User.class).getResultList();
    }

    public void addUser(String login, String password) throws Exception {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        save(user);
    }
}