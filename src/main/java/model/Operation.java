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
     *  указание аккаунта для перевода
     */
    private Account intoAccount;

    /**
    * указание аккаунта с которого сделан перевод
     */
    private Account fromAccount;

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

    public void setIntoAccount(Account intoAccount){this.intoAccount = intoAccount;}

    public Account getIntoAccount(){return intoAccount;}

    public void setFromAccount(Account fromAccount){ this.fromAccount = fromAccount;}

    public Account getFromAccount(){return fromAccount;}
}
