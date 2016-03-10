package dao;

import org.junit.BeforeClass;

import javax.persistence.Persistence;

public class HibernatePostgresTest extends HibernateTest {
    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("Unit-tests-Postgres");
    }
}