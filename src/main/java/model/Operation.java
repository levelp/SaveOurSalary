package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Операция со счётом
 */
@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Сумма операции
     */
    @Column(name = "sum")
    private BigDecimal sum;

    /**
     * Дата и время выполнения операции
     */
    private Date dateTime = new Date();

    /**
     * Категории (теги) этой операции
     */
    @ManyToMany(targetEntity = OperationCategory.class, mappedBy = "operations", fetch = FetchType.EAGER)
    private List<OperationCategory> categories = new ArrayList<>();
    /**
     * Ссылка на аккаунт, которому принадлежит операция
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    /**
     *  указание аккаунта для перевода
     */
    private String intoAccount;

    /**
     * указание аккаунта с которого сделан перевод
     */
    private String fromAccount;

    /**
     * Констуктор без параметров нужен чтобы загружать из БД
     */
    protected Operation() {
    }

    public Operation(double sum) {
        this.sum = new BigDecimal(sum);
    }

    public double getSum() {
        return sum.doubleValue();
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getIntoAccount() {
        return intoAccount;
    }

    public void setIntoAccount(String intoName) {
        this.intoAccount = intoName;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;}
}
