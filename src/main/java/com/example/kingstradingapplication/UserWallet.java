package com.example.kingstradingapplication;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserWallet implements Serializable {

    private ArrayList<StockData> stocks;

    public UserWallet(){
        this.stocks= new ArrayList<>();
    }

    public ArrayList<StockData> getStocks(){
        return this.stocks;
    }

    public void addStock(String stockName, double price, double shares, LocalDate datePurchased){
        stocks.add(new StockData(stockName, price, shares, datePurchased));
    }

    public double sellStock(StockData stock, double currentPrice){
        stocks.remove(findUserStocks(stock.getId()));
        return stock.getShares() * currentPrice;
    }

    public int findUserStocks(int id){
        for(int i  = 0; i<stocks.size(); i++){
            if(stocks.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }
}
