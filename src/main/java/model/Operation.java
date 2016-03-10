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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Сумма операции
     */
    @Column(name = "`sum`")
    private BigDecimal sum;

    /**
     * Дата и время выполнения операции
     */
    private Date dateTime = new Date();

    /**
     * Категории (теги) этой операции
     * FetchType.EAGER - загрузка "сразу"
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
     * Ссылка на аккаунт, с которого сделан перевод
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    /**
     * Ссылка на аккаунт, на который сделан перевод
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "into_account_id")
    private Account intoAccount;

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

    public Account getIntoAccount() {
        return intoAccount;
    }

    public void setIntoAccount(Account intoAccount) {
        this.intoAccount = intoAccount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }
}
