package util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Проверка работы с паролем
 */
public class PasswordTest extends Assert {
    /**
     * Хешируем пароль, а затем проверяем его
     */
    @Test
    public void testHashAndCheck() throws Exception {
        // Генерируем пароль
        String password = Password.generate();
        System.out.println("password = " + password);
        // Хешируем его
        String hashed = Password.getSaltedHash(password);
        System.out.println("hashed = " + hashed);
        // Проверяем пароль
        assertTrue(Password.check(password, hashed));
        // Проверяем что другой пароль не подойдёт
        assertFalse(Password.check(password + "1", hashed));
    }
}
