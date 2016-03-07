package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Счёт пользователя
 * Виды счетов:
 * - Наличные деньги
 * - Кредитная/дебетовая карта
 * - Долг (кому-то)
 * - Кредит
 * - Депозит
 * На счету хранится (учтена) определённая сумма.
 * В определённой (одной) валюте.
 */
@Entity // JPA-аннотация
@Table(name = "account")
public class Account {
    /**
     * Первичный ключ (PrimaryKey)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Сумма на счёте
     */
    private double amount;

    /**
     * Валюта
     */
    private Currency currency;

    /**
     * Процент начисления годовых
     */
    private double percent;

    /**
     * Операции со счётом
     */
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Operation> operations = new ArrayList<>();
    private String type; //тип аккаунта (кридетная карта, дебетовая карта, наличные)

    /**
     * Пользователь тратит деньги со счёта
     *
     * @param sum сумма
     */
    public void spend(double sum) throws NegativeBalanceException {
        operations.add(new Operation(sum));
        if (sum <= amount)
            amount -= sum;
        else
            throw new NegativeBalanceException();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currencyCode Код валюты
     */
    public void setCurrency(String currencyCode) {
        this.currency = Currency.getInstance(currencyCode);
    }

    public String getType() {
        return type;
    }

    public void setType(String typeIn) {
        this.type = typeIn;
    }

    public Operation getOperationById(int num_in) {
        return operations.get(num_in);
    }

    /**
     * перевод средств с одного счёта на другой (в одной валюте)
     *
     * @param account счёт для перевода
     * @param sum     сумма перевода
     * @return true если успешно, false если недостаточно денег
     */
    public boolean send(Account account, double sum) {
        if (account.getCurrency().equals(this.currency)
                && this.getAmount() >= sum) {
            this.setAmount(this.getAmount() - sum);
            account.setAmount(account.getAmount() + sum);
            return true;
        }
        return false;
    }

    /**
     * Пополнение счёта
     *
     * @param sum сумма
     */
    public void income(double sum) {
        Operation operation = new Operation(sum);
        amount += sum;
        operations.add(operation);
    }

    /**
     * @param percent Процент годовых
     */
    public void setPercent(double percent) {
        this.percent = percent;
    }

    /**
     * @param period Период в месяцах
     */
    public double percentCalculate(int period) {
        return amount * period * percent / 1200;
    }

    /**
     * Начисление процентов
     *
     * @param period Период в месяцах
     */
    public void percentApply(int period) {
        double profit = percentCalculate(period);
        amount += profit;
        operations.add(new Operation(profit));

    }

    /**
     * Сумма последней операции
     */
    public double getLastOperationSum() {
        return operations.get(operations.size() - 1).getSum();
    }

    /**
     * метод заполнения данных об операции /создаем новую операцию/заполняем исходящий и входящий аккаунт на обоих счетах
     */
    private void fillOperationData(Account account, double sum) {
        //добавляем операцию для базового класса
        this.operations.add(new Operation(sum));
        //добавляем последней операции идентификатор целевого счета
        this.operations.get(this.operations.size() - 1).setIntoAccount(account.getType());
        this.operations.get(this.operations.size() - 1).setFromAccount(type);

        //добавляем операцию к аккаунту цели
        account.operations.add(new Operation(sum));
        //добавляем ссылки на аккаунты
        account.operations.get(account.operations.size() - 1).setFromAccount(type);
        account.operations.get(account.operations.size() - 1).setIntoAccount(account.getType());
    }


    /**
     * @param account - аккаунт на который производится перевод
     * @param sum     - сумма транзакции
     * @param invoice - размер комиссии
     */
    public void sendWithFixTax(Account account, double sum, double invoice) {
        this.send(account, sum);
        this.setAmount(this.getAmount() - invoice);
        this.fillOperationData(account, sum);


    }

    /**
     * перевод с аккаунта на аккаунт с комиссией, в процентах от перевода
     *
     * @param account - входящий аккаунт, на который производится перевод
     * @param sum     - сумма перевода
     * @param percent - процент перевода
     */
    public void sendWithFlowTax(Account account, double sum, double percent) {
        if (percent >= 1) {
            percent = percent / 100;
        } //пересчет процентов
        if (percent >= 0) {
            this.send(account, sum);
            this.setAmount(this.getAmount() - sum * percent);
            this.fillOperationData(account, sum);
        }
    }

    public void cancelLastOneOperation() {
        amount += operations.get(operations.size() - 1).getSum();
        operations.remove(operations.size() - 1);
    }
}
