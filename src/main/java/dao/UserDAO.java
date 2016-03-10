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
        if (findByLogin(login) == null){
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        save(user);
    }

    /**
     * Получение пользователя по логину из БД
     *
     * @param login логин
     * @return Пользователь или null если пользователь не найден
     */
    public User findByLogin(String login) {
        return (User) em.createQuery("SELECT u FROM User u WHERE u.login = :login").
                setParameter("login", login).getSingleResult();
    }
}