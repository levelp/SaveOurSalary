package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Категория траты/расхода (на транспорт, оплата жилья, учёба...)
 */
@Entity
@Table(name = "operation_category")
@NamedQuery(name = OperationCategory.AUTOCOMPLETE, query = "select c from OperationCategory c " +
        "where c.name like :filter order by c.name")
public class OperationCategory {
    public static final String AUTOCOMPLETE = "OperationCategory.autocomplete";

    @Id
    private String name;
    /**
     * Операции с этой категорией
     */
    @ManyToMany(targetEntity = Operation.class, fetch = FetchType.LAZY)
    private List<Operation> operations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
