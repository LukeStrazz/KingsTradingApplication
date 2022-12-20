package com.example.kingstradingapplication;


import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class UserWallet implements Serializable {

    private ArrayList<StockData> stocks;
    private ArrayList<CashData> cash;
    private ArrayList<SellStock> sell;
    public UserWallet(){
        this.cash=new ArrayList<>();
        this.stocks= new ArrayList<>();
        this.sell = new ArrayList<>();

    }

    public ArrayList<SellStock> getSell(){
        return this.sell;
    }

    public ArrayList<StockData> getStocks(){
        return this.stocks;
    }

    public ArrayList<CashData> getCash() {
        return this.cash;
    }

    public void addStock(String stockName, double price, double shares, LocalDate datePurchased, String type){
        double value = shares*price;
        stocks.add(new StockData(stockName, price, shares, datePurchased, value, type));
    }

    public double sellStock(StockData stock, double currentPrice, int shares){
        StockData currentStock = stocks.get(findUserStocks(stock.getId()));
        currentStock.setShares(currentStock.getShares() - shares);
        if ((int)currentStock.getShares() == 0) {
            stocks.remove(findUserStocks(stock.getId()));
        }

        return shares * currentPrice;
    }

    public void newSell(StockData stock, double currentPrice, int shares){
        sell.add(new SellStock(stock.getSymbol(), currentPrice, shares, LocalDate.now(), currentPrice*shares, "Sell"));
    }

    public int findUserStocks(int id){
        for(int i  = 0; i<stocks.size(); i++){
            if(stocks.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }


    public void addDeposit(double amount, LocalDate date) {
        cash.add(new CashData(amount, "Deposit", date));
    }

    public void addWithdrawal(double amount, LocalDate date) {
        cash.add(new CashData(amount, "Withdrawal", date));
    }
}
