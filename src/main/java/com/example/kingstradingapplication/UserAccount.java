package com.example.kingstradingapplication;


import java.io.Serializable;

public class UserAccount implements Serializable {
    private static UserAccount instance = null;
    private String username;
    private String firstName;
    private String surName;
    private String password;
    private int id;

    private double cash;

    private UserWallet wallet;

    public UserAccount(String firstName, String surName, String username, String password) {
        this.username = username;
        this.password = password;
        this.firstName=firstName;
        this.surName=surName;
        this.id = id++;
        this.wallet = new UserWallet();
        this.cash = cash;
    }

    public double getTotalStockValue() {
        double total = 0;
        for (StockData stock : wallet.getStocks()) {
            total += stock.getPrice() * stock.getShares();
        }
        return total;
    }

    public double getTotalMoney(UserAccount user) {
        if (user == null) {
            return 0;
        }
        return user.getCashAmount() + user.getTotalStockValue();
    }

    public UserWallet getWallet(){
        return this.wallet;
    }
    public void setWallet(){
        this.wallet=wallet;
    }

    public double getCashAmount(){
        return this.cash;
    }

    public void setCashAmount(double newAmount){
        this.cash = newAmount;
    }

    public int getID(){
        return this.id;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    public String getSurName(){
        return this.surName;
    }
    public void setSurName(String surName){
        this.surName=surName;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String newName){
        this.username=newName;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String newPass){
        this.password=newPass;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof UserAccount))
            return false;

        UserAccount user = (UserAccount)obj;
        return (user.password.equals(this.password) &&
                user.username.equals(this.username));
    }
}
