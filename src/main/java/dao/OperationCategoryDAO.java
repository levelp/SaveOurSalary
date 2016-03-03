package dao;

import model.OperationCategory;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class OperationCategoryDAO extends DAO<OperationCategory> {
    public OperationCategoryDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public OperationCategory find(int id) {
        return em.find(OperationCategory.class, id);
    }

    public List<OperationCategory> listALL() {
        return em.createQuery("select c from OperationCategory c order by c.name",
                OperationCategory.class).getResultList();
    }

    public List<OperationCategory> autocomplete(String name) {
        return em.createNamedQuery(OperationCategory.AUTOCOMPLETE, OperationCategory.class).
                setParameter("filter", name + "%").getResultList();
    }

    /**
     * Добавляем новую категорию и сохраняем её
     *
     * @param name название категории
     */
    public void add(String name) {
        OperationCategory category = new OperationCategory();
        category.setName(name);
        save(category);
    }
}