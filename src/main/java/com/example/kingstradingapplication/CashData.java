package com.example.kingstradingapplication;

import java.io.Serializable;
import java.time.LocalDate;


public class CashData implements Serializable {
    private double amount;
    private String type;
    private LocalDate date;

    private int id;

    private static int idCount = 0;

    public CashData(double amount, String type, LocalDate date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.id=idCount++;
    }

    public int getId(){
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return type + " For Transaction Amount: $" + roundToDollarAmount(amount) +
                ",\nTransaction Date: " + date;
    }
    private static String roundToDollarAmount(double value) {
        return String.format("%,.2f", value);
    }

}