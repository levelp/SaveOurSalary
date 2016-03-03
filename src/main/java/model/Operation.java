package model;

import java.util.Date;

/**
 * Операция со счётом
 */
public class Operation {
    private double sum;

    /**
     * Дата и время выполнения операции
     */
    private Date dateTime = new Date();

    public Operation(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
