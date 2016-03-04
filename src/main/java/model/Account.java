package model;

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
public class Account {
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
    private List<Operation> operations = new ArrayList<>();

    /**
     * Пользователь тратит деньги со счёта
     *
     * @param sum сумма
     */
    public void spend(double sum) {
        operations.add(new Operation(sum));
        amount -= sum;
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

    /**
     * @param percent Процент годовых
     */
    public void setPercent(double percent) { this.percent = percent;  }

    /**
     *
     * @param period Период в месяцах
     */
    public double percentCalculate(int period) {   return  amount*period*percent/1200;  }

    /**
     * Начисление процентов
     *  @param period Период в месяцах
     */
    public void percentApply(int period) {
        double profit=percentCalculate(period);
        amount+=profit;
        operations.add(new Operation(profit));

    }

    /**
     * Сумма последней операции
     */
    public double getLastOperationSum() {  return operations.get(operations.size()- 1).getSum();  }
}
