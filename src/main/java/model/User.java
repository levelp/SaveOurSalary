package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь системы
 */
public class User {
    List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
