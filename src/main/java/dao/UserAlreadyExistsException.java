package dao;

public class UserAlreadyExistsException extends Exception {
    @Override
    public String toString() {
        return "A user with the same login is already exists";
    }
}
