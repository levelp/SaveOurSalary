package dao;

import org.junit.BeforeClass;

import javax.persistence.Persistence;

public class HibernateMySQLTest extends HibernateTest {
    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("Unit-tests-MySQL");
    }
}