package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Операция со счётом
 */
public class Operation {
    private double sum;

    /**
     * Дата и время выполнения операции
     */
    private Date dateTime = new Date();

    /**
     * Категории (теги) этой операции
     */
    private List<OperationCategory> categories = new ArrayList<>();

    /**
     * Констуктор без параметров нужен чтобы загружать из БД
     */
    protected Operation() {
    }

    public Operation(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void addCategory(OperationCategory category) {
        categories.add(category);
    }

    public List<OperationCategory> getCategories() {
        return categories;
    }
}
