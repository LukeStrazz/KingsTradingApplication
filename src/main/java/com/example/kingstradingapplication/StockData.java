package com.example.kingstradingapplication;

import java.io.Serializable;
import java.time.LocalDate;

public class StockData implements Serializable{
    // The stock symbol.
    private String symbol;

    // The price of a single share of the stock.
    private double price;

    // The number of shares of the stock that the user owns.
    private double shares;

    // The date when the stock was purchased.
    private LocalDate datePurchased;

    private int id;

    private static int idCount = 0;



    // The constructor initializes the symbol, price, shares, and datePurchased
    // fields.
    public StockData(String symbol, double price, double shares, LocalDate datePurchased) {
        this.symbol = symbol;
        this.price = price;
        this.shares=shares;
        this.datePurchased = datePurchased;
        this.id = idCount++;
    }

    // Getters and setters for the symbol, price, shares, and datePurchased fields.
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getShares() { return shares; }
    public LocalDate getDatePurchased() { return datePurchased; }
    public int getId(){return id;}
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setPrice(double price) { this.price = price; }
    public void setShares(double shares) { this.shares = shares; }
    public void setDatePurchased(LocalDate datePurchased) { this.datePurchased = datePurchased; }
    public void setId(int newId){this.id=newId;}

    // A method to calculate the current value of the stock.
    public double getValue() {
        return price * shares;
    }

    @Override
    public String toString() {
        return "symbol='" + symbol + "'" +
                ", price=" + price +
                ", shares=" + shares +
                ", datePurchased=" + datePurchased;
    }
}

