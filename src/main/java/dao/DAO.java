package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Data Access Object
 */
public abstract class DAO<T> {
    EntityManagerFactory emf;
    EntityManager em;

    public void startConnection() {
        emf = Persistence.createEntityManagerFactory("Unit-tests-HSQLDB");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    public void closeConnection() {
        em.getTransaction().commit();
        emf.close();
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
