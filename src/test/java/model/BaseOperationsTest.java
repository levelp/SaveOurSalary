package model;

import org.junit.Assert;
import org.junit.Test;

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
        account.spend(10.00);

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
        account.spend(10.00);

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
}
