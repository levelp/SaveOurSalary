package model;

public class NegativeBalanceException extends Throwable {
    @Override
    public String toString() {
        return "Negative balance";
    }
}
