package com.example.kingstradingapplication;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class SellStock implements Serializable {
    // The stock symbol.
    private String symbol;

    // The price of a single share of the stock.
    private double price;

    // The number of shares of the stock that the user owns.
    private double shares;

    // The date when the stock was purchased.
    private LocalDate datePurchased;

    private double value;

    private int id;

    private static int idCount = 0;

    private String type;



    // The constructor initializes the symbol, price, shares, and datePurchased
    // fields.
    public SellStock(String symbol, double price, double shares, LocalDate datePurchased, double value,  String type) {
        this.type=type;
        this.symbol = symbol;
        this.value= value;
        this.shares=shares;
        this.price = price;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // A method to calculate the current value of the stock.
    public double getValue() {
        return this.value;
    }

    public double getCurrentStockPrice(String stockName) throws IOException {

        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(1);
        long startPeriod = getPeriod(start.getYear(), start.getMonthValue(), start.getDayOfMonth());
        long endPeriod = getPeriod(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        URL url = new URL(getURL(stockName, startPeriod, endPeriod));
        Scanner s = new Scanner(url.openStream());
        int count = 0;
        try {
            while (s.hasNext()) {
                count++;
                String[] values = s.nextLine().split(",");
                if (count == 2) {
                    return Double.valueOf(values[5]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Double.MAX_VALUE;
    }
    public long getPeriod(int y, int m, int d){
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1,d);
        Date date = cal.getTime();
        return date.getTime()/1000;
    }
    public String getURL(String stockSymbol, long period1, long period2) {
        String urlTemplate = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=%d&period2=%d&interval=1d&events=history&includeAdjustedClose=true";
        String urlStr = String.format(urlTemplate, stockSymbol, period1, period2);
        return urlStr;
    }


    @Override
    public String toString() {
        try {
            return type +
                    " of Ticker: '" + symbol + "'" +
                    ", Current Price: " + roundToDollarAmount(getCurrentStockPrice(symbol))+
<<<<<<< HEAD
                    ", Share(s) Owned: " + shares +
                    ", P/L: " + 0 +
=======
                    ", Share(s) Sold: " + shares +
                    ", P/L: " + roundToDollarAmount((getCurrentStockPrice(symbol)*shares) - value) +
>>>>>>> b77d56421f8f1c083cfc4b237339ec9d67e8d80f
                    "\nTransaction Date: " + datePurchased +
                    ", Total "+type+": $" +value +
                    ", For: $" + roundToDollarAmount(price) + " Per-Share.";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String roundToDollarAmount(double value) {
        return String.format("%,.2f", value);

    }
}
