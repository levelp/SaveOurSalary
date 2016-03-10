package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Data Access Object - слой работы с базой данных
 */
public abstract class DAO<T> {
    // Определяет подключение к БД - какую БД сейчас использовать?
    EntityManagerFactory emf;
    EntityManager em;

    public DAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void startConnection() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    public void closeConnection() {
        em.getTransaction().commit();
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public void edit(T entity) {
        em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
    }

    abstract public T find(int id);
}
