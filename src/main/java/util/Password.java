package util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Работа с паролем - хеш-суммы вместо хранения пароля в виде текста
 */
public class Password {
    // The higher the number of iterations the more
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 20 * 1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;
    // Символы для генерации пароля
    private static final char[] symbols;
    // Длина пароля
    private static final int PASSWORD_LEN = 8;

    static {
        // Создаём список всех допустимых в пароле символов
        // Спецсимволы
        StringBuilder chars = new StringBuilder("!@#$%&*()_-+=[]{}\\|:/?.,><");
        // Цифры
        for (char c = '0'; c <= '9'; ++c)
            chars.append(c);
        // Буквы
        for (char c = 'a'; c <= 'z'; ++c) {
            chars.append(c);
            chars.append(Character.toUpperCase(c));
        }
        // Сохраняем полученный набор символов как массив
        symbols = chars.toString().toCharArray();
    }

    /**
     * @return генерация пароля
     */
    public static String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < PASSWORD_LEN; ++i)
            buf.append(symbols[random.nextInt(symbols.length)]);
        return buf.toString();
    }

    /**
     * Computes a salted PBKDF2 hash of given plaintext password
     * suitable for storing in a database.
     * Empty passwords are not supported.
     */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
    }

    /**
     * Проверка, соответствует ли пароль захешированному паролю
     */
    public static boolean check(String password, String stored) throws Exception {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                    "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}