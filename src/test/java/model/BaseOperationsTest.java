package model;


import org.junit.Assert;
import org.junit.Test;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Базовые операции по работе со счётом, для одного пользователя
 */
public class BaseOperationsTest extends Assert {

    public static final double DELTA = 1e-3;

    private Account createUserWithOneAccount() {
        // Один пользователь
        User user = new User();

        // У него есть счёт
        Account account = new Account();
        user.addAccount(account);
        return account;
    }

    private ArrayList<Account> createUserWithSeveralAccounts(int accounts) {
        // Один пользователь
        User user = new User();

        for (int i = 0; i < accounts; i++) {
            user.addAccount(new Account());
        }

        return (ArrayList<Account>) user.getAccounts();
    }

    /**
     * Один пользователь, у которого есть
     * только наличные деньги.
     */
    @Test
    public void testOneUserSpendCash() {
        Account account = createUserWithOneAccount();

        account.setAmount(50.23);
        account.setCurrency("RUR");

        // Пользователь тратит
        try {
            account.spend(10.00);
        } catch (NegativeBalanceException ignored) {
            fail("У пользователя достаточно средств, не должно быть исключения");
        }

        // Сумма уменьшается
        assertEquals(50.23 - 10.00, account.getAmount(), DELTA);
        // Валюта сохраняется
        assertEquals("RUR", account.getCurrency().toString());
    }

    /**
     * Все операции сохраняются в истории операци и их можно получить
     */
    @Test
    public void testAllOperationsStoredInHistory() {
        Account account = createUserWithOneAccount();

        account.setAmount(50.23);
        account.setCurrency("RUR");

        // Дата и время до выполнения операции
        Date beforeOperation = new Date();

        // Пользователь тратит
        try {
            account.spend(10.00);
        } catch (NegativeBalanceException ignored) {
            fail("У пользователя достаточно средств, не должно быть исключения");
        }

        // Дата и время после выполнения операции
        Date afterOperation = new Date();

        List<Operation> operations = account.getOperations();
        assertEquals("Была одна операция", 1, operations.size());
        Operation op1 = operations.get(0);
        assertEquals(10.00, op1.getSum(), DELTA);

        assertTrue("Дата и время записанные до операции " +
                        "должны быть до операции или с тем же временем: " + beforeOperation +
                        " " + op1.getDateTime(),
                beforeOperation.before(op1.getDateTime()) ||
                        beforeOperation.equals(op1.getDateTime()));
        assertTrue("Дата и время записанные после операции " +
                        "должны быть после операции: " + afterOperation +
                        " " + op1.getDateTime(),
                afterOperation.after(op1.getDateTime()) ||
                        afterOperation.equals(op1.getDateTime()));
    }

    /**
     * Перевод денег с одного счёта на другой (в одной валюте)
     */
    @Test
    public void testTransactionBetweenAccounts() {
        ArrayList<Account> accounts = createUserWithSeveralAccounts(2);
        for (Account acc : accounts) {
            acc.setCurrency("RUR");
            acc.setAmount(1500.00);
        }
        accounts.get(0).send(accounts.get(1), 55.0);
        assertEquals(1445.0, accounts.get(0).getAmount(), DELTA);
        assertEquals(1555.0, accounts.get(1).getAmount(), DELTA);
    }

    /**
     * Категории операций:
     * https://github.com/levelp/SaveOurSalary/issues/8
     */
    @Test
    public void testCategories() {
        // Пользователь может создать произвольное количество категорий
        OperationCategory category = new OperationCategory();
        category.setName("Транспорт");

        OperationCategory category2 = new OperationCategory();
        category2.setName("Оплата по карте");

        // Каждую трату / покупку / перевод отнести к одной из категорий
        Operation operation = new Operation(10);
        operation.addCategory(category);
        operation.addCategory(category2);

        // У каждой операции можно получить список категорий
        List<OperationCategory> list = operation.getCategories();
        assertEquals(2, list.size());
        assertEquals("Транспорт", list.get(0).getName());
    }

    /**
     * Увеличение суммы на счёте
     */
    @Test
    public void testIncome() {
        Account account = createUserWithOneAccount();

        account.setAmount(100.67);
        account.setCurrency("RUR");

        account.income(30.56);
        double sum = 100.67 + 30.56;
        assertEquals("Суммы не совпадают", 100.67 + 30.56, account.getAmount(), DELTA);

        double rand = Math.random();
        account.income(rand);
        assertEquals("Суммы не совпадают", sum + rand, account.getAmount(), DELTA);
    }

    @Test
    public void testIncomeOperations() {
        Account account = createUserWithOneAccount();

        account.setAmount(109.8);
        account.setCurrency("RUR");

        // Дата и время до выполнения операции
        Date beforeOperation = new Date();

        // Пользователь получает
        account.income(20.00);

        // Дата и время после выполнения операции
        Date afterOperation = new Date();

        List<Operation> operations = account.getOperations();
        assertEquals("Была одна операция", 1, operations.size());
        Operation op1 = operations.get(0);
        assertEquals(20.00, op1.getSum(), DELTA);

        assertTrue("Дата и время записанные до операции " +
                        "должны быть до операции или с тем же временем: " + beforeOperation +
                        " " + op1.getDateTime(),
                beforeOperation.before(op1.getDateTime()) ||
                        beforeOperation.equals(op1.getDateTime()));
        assertTrue("Дата и время записанные после операции " +
                        "должны быть после операции: " + afterOperation +
                        " " + op1.getDateTime(),
                afterOperation.after(op1.getDateTime()) ||
                        afterOperation.equals(op1.getDateTime()));
    }

    /**
     * Начисляются проценты на остаток по счету
     */
    @Test
    public void testPercentIncome() {

        Account debetAccount = new Account();
        debetAccount.setAmount(100);
        debetAccount.setCurrency("RUR");
        debetAccount.setPercent(12);

        User userD = new User();
        userD.addAccount(debetAccount);

        assertEquals("Проценты за вклад", 2, debetAccount.percentCalculate(2), DELTA);

        debetAccount.percentApply(2);
        assertEquals("Пополнение счета за вклад", 102, debetAccount.getAmount(), DELTA);
        assertEquals("Сумма последней операции", 2, debetAccount.getLastOperationSum(), DELTA);

    }

    @Test
    public void testOperationSpendMoreThanUserHas() {
        Account account = createUserWithOneAccount();

        account.setAmount(50.00);
        account.setCurrency("RUR");

        try {
            account.spend(60.00);
        } catch (NegativeBalanceException e) {
            assertEquals(50.00, account.getAmount(), DELTA);
        }
    }

    /**
     *
     * обмен валюты без счета меняемой валюты у пользователя
     * @throws NegativeBalanceException
     */

    @Test
    public void testExchangeOperationWithOutAccount() throws NegativeBalanceException {

        // инициализация пользователя

        User user = new User();
        Account fromAcc = new Account();
        fromAcc.setAmount(100.00);
        fromAcc.setCurrency("RUR");
        Account toAcc = new Account();
        toAcc.setAmount(10.00);
        toAcc.setCurrency("USD");
        user.addAccount(fromAcc);

        assertEquals(1, user.getAccounts().size());
        fromAcc.exchange(user, user.getAccounts().get(0), toAcc ,2.00);

        assertEquals(80.00, user.getAccounts().get(0).getAmount(), DELTA);
        assertEquals(10.00, user.getAccounts().get(1).getAmount(), DELTA);
        assertEquals(1, fromAcc.getOperations().size());
        assertEquals(2, user.getAccounts().size());
        assertEquals("Операция на счете списания", "EXCHANGE", user.getAccounts().get(0).getOperations().get(0).getCategories().get(0).getName());
        assertEquals("Операция на счете зачисления", "EXCHANGE", user.getAccounts().get(1).getOperations().get(0).getCategories().get(0).getName());
    }

    /**
     *
     * обмен валюты со счетом меняемой валюты у пользователя
     * @throws NegativeBalanceException
     */
    @Test
    public void testExchangeOperationWithExchangeAccount() throws NegativeBalanceException {

        // инициализация пользователя

        User user = new User();
        Account fromAcc = new Account();
        fromAcc.setAmount(100.00);
        fromAcc.setCurrency("RUR");
        Account toAcc = new Account();
        toAcc.setAmount(10.00);
        toAcc.setCurrency("USD");
        user.addAccount(fromAcc);

        //Добавляем счет до выполнения операции обмена, т.е. имитируем наличие у пользователя счета с валютой обмена

        user.addAccount(toAcc);
        assertEquals(2, user.getAccounts().size());

        fromAcc.exchange(user, user.getAccounts().get(0), toAcc ,2.00);

        assertEquals(80.00, user.getAccounts().get(0).getAmount(), DELTA);
        assertEquals(10.00, user.getAccounts().get(1).getAmount(), DELTA);
        assertEquals(1, fromAcc.getOperations().size());
        assertEquals(2, user.getAccounts().size());
        assertEquals("Операция на счете списания", "EXCHANGE", user.getAccounts().get(0).getOperations().get(0).getCategories().get(0).getName());
        assertEquals("Операция на счете зачисления", "EXCHANGE", user.getAccounts().get(1).getOperations().get(0).getCategories().get(0).getName());
    }

    @Test
    /*
    *Тест перевод со счета на счет с удержанием комиссии
     */
    public void testFromDebitToCredit() {
        //инициализация аккаунтов с начальными данными
        Account debitCard = new Account();
        Account creditCard = new Account();

        debitCard.setAmount(150.00);
        debitCard.setCurrency("RUR");
        debitCard.setType("DebitCard");
        creditCard.setAmount(200.00);
        creditCard.setCurrency("RUR");
        creditCard.setType("CreditCard");

        //перевод с дебетовой карты на кредитную с фиксированной комиссией
        debitCard.sendWithFixTax(creditCard, 15.00, 1.00);
        //тест
        assertEquals(150.00 - 15.00 - 1.00, debitCard.getAmount(), DELTA);

        //перевод с дебетовой карты на кредитную с комиссией в процентах
        debitCard.sendWithFlowTax(creditCard, 15.00, 5);
        //тест
        assertEquals(134.00 - 15.00 - 15.00 * 5 / 100, debitCard.getAmount(), DELTA);

        //тест на количество операций в каждой аккаунте
        assertEquals(2, debitCard.getOperations().size());
        assertEquals(2, creditCard.getOperations().size());

        //тест что на дебетовом аккаунте from - debib, into - credit
        assertEquals("DebitCard", debitCard.getOperationById(0).getFromAccount());
        assertEquals("CreditCard", debitCard.getOperationById(0).getIntoAccount());

        //тест что во второй операции на аккаунте кредитки from - debit, into - credit
        assertEquals("DebitCard", creditCard.getOperationById(1).getFromAccount());
        assertEquals("CreditCard", creditCard.getOperationById(1).getIntoAccount());



    }

    @Test
    /*
    *Тест перевод со счета на счет с удержанием комиссии
     */
    public void testFromDebitToCredit(){
        //инициализация аккаунтов с начальными данными
        Account debitCard = new Account();
        Account creditCard = new Account();

        debitCard.setAmount(150.00);
        debitCard.setCurrency("RUR");
        debitCard.setType("DebitCard");
        creditCard.setAmount(200.00);
        creditCard.setCurrency("RUR");
        creditCard.setType("CreditCard");

        //перевод с дебетовой карты на кредитную с фиксированной комиссией
        debitCard.sendWithFixTax(creditCard, 15.00, 1.00);
        //тест
        assertEquals(150.00-15.00-1.00, debitCard.getAmount(),DELTA);

        //перевод с дебетовой карты на кредитную с комиссией в процентах
        debitCard.sendWithFlowTax(creditCard, 15.00, 5);
        //тест
        assertEquals(134.00-15.00-15.00*5/100, debitCard.getAmount(), DELTA);

        //тест на количество операций в каждой аккаунте
        assertEquals(2,debitCard.getOperations().size());
        assertEquals(2,creditCard.getOperations().size());

        //тест что на дебетовом аккаунте from - debib, into - credit
        assertEquals("DebitCard",debitCard.getOperationById(0).getFromAccount());
        assertEquals("CreditCard",debitCard.getOperationById(0).getIntoAccount());

        //тест что во второй операции на аккаунте кредитки from - debit, into - credit
        assertEquals("DebitCard",creditCard.getOperationById(1).getFromAccount());
        assertEquals("CreditCard",creditCard.getOperationById(1).getIntoAccount());



    }
}
