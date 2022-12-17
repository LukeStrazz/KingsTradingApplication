package com.example.kingstradingapplication;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class StockApp extends Application implements Serializable {
    private Scene loginScene, regScene, hsScene, asScene, marketDataScene, holdingScene, cashScene, changePWScene;
    private Stage stage;
    public static UserAccount currentUser = new UserAccount("", "", "", "");

    private static DataCenter instance = null;

    public UserAccount getCurrentUser() {
        return currentUser;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            this.stage = primaryStage;

            Pane loginGui = loginGUI();
            loginScene = new Scene(loginGui, 1200, 800);
            primaryStage.setTitle("King's Trading");
            loginScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            regScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());

            primaryStage.setScene(loginScene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        instance = DataCenter.readFile();
        launch();
        DataCenter.getInstance().saveFile();
    }

    public double getTotalMoney(UserAccount user) {
        if (user == null) {
            return 0;
        }

        return user.getCashAmount() + user.getTotalStockValue();
    }

    private Pane loginGUI() {
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(20));
        loginPane.setHgap(10);
        loginPane.setVgap(10);

        Label lblUsername = new Label("Username");
        Label lblPassword = new Label("Password");
        TextField tfUsername = new TextField();
        PasswordField tfPassword = new PasswordField();
        tfUsername.setPromptText("Enter Username");
        tfPassword.setPromptText("Enter Password");


        loginPane.add(lblUsername, 0, 0);
        loginPane.add(lblPassword, 0, 1);
        loginPane.add(tfUsername, 1, 0, 2, 1);
        loginPane.add(tfPassword, 1, 1, 2, 1);

        HBox hbox = new HBox();
        HBox hbox2 = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.BOTTOM_RIGHT);
        Button btnLogin = new Button("Login");
        Button btnSignUp = new Button("Sign Up");
        Button btnCancel = new Button("Exit");
        btnCancel.setAlignment(Pos.CENTER);
        btnLogin.setAlignment(Pos.CENTER);
        btnSignUp.setAlignment(Pos.CENTER);
        btnLogin.setDisable(true);
        btnLogin.setPrefWidth(60);
        btnCancel.setPrefWidth(60);
        hbox.getChildren().addAll(btnLogin, btnSignUp);
        hbox2.getChildren().addAll(btnCancel);
        loginPane.add(hbox, 1, 2, 2, 1);
        loginPane.add(hbox2, 1, 3, 2, 1);

        class userPassBlocker implements EventHandler<KeyEvent> {
            public void handle(KeyEvent e) {
                String username = tfUsername.getText();
                String password = tfPassword.getText();
                btnLogin.setDisable(!validateLogin(username, password));
            }
        }
        tfUsername.setOnKeyTyped(new userPassBlocker());
        tfPassword.setOnKeyTyped(new userPassBlocker());

        btnLogin.setOnAction(a -> {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            UserAccount c = DataCenter.getInstance().getUser(username, password);
            hsScene = new Scene(homeScreenGUI(), 1200, 800);
            if (c != null) {
                currentUser = c;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Welcome Back "+username+"!");
                alert.setContentText("User login successful.");
                alert.showAndWait();
                hsScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                stage.setScene(hsScene);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("User Login Unsuccessful.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        });

        regScene = new Scene(registerGUI(), 1200, 800);
        btnSignUp.setOnAction(a -> {
            stage.setScene(regScene);
        });
        btnCancel.setOnAction(c -> {
            Platform.exit();
            DataCenter.getInstance().saveFile();

        });

        return loginPane;
    }

    private Pane registerGUI() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);

        Button btnSignUp = new Button("Sign Up");
        btnSignUp.setDisable(true);

        Button btnCancel = new Button("Cancel");
        btnSignUp.setPrefWidth(80);
        btnCancel.setPrefWidth(80);

        hbox.getChildren().addAll(btnSignUp, btnCancel);
        pane.add(hbox, 1, 5, 2, 1);

        Label lblFirstName = new Label("First Name");
        Label lblSurName = new Label("Last Name");
        Label lblRegUsername = new Label("Username");
        Label lblRegPassword = new Label("Password");
        Label lblConfRegPassword = new Label("Confirm Password");
        TextField tfRegUsername = new TextField();
        PasswordField tfRegPassword = new PasswordField();
        PasswordField tfRegConfPassword = new PasswordField();
        tfRegUsername.setPromptText("Enter Username");
        tfRegPassword.setPromptText("Enter Password");
        tfRegConfPassword.setPromptText("Confirm Password");
        TextField tfFirstName = new TextField();
        TextField tfSurName = new TextField();
        tfFirstName.setPromptText("Enter First Name");
        tfSurName.setPromptText("Enter Last Name");

        tfFirstName.setOnKeyTyped(e->{
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String confPass = tfRegConfPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            if(password.length() >= 1 && username.length() >= 1) {
                if(firstName.length()>=1 && surName.length()>=1) {
                    btnSignUp.setDisable(!(validateRegister(username, password, confPass, firstName, surName)));
                }
            }
        });
        tfSurName.setOnKeyTyped(e->{
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String confPass = tfRegConfPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            if(password.length() >= 1 && username.length() >= 1) {
                if(firstName.length()>=1 && surName.length()>=1) {
                    btnSignUp.setDisable(!(validateRegister(username, password, confPass, firstName, surName)));
                }
            }
        });
        tfRegUsername.setOnKeyTyped(e->{
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String confPass = tfRegConfPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            if(password.length() >= 1 && username.length() >= 1) {
                if(firstName.length()>=1 && surName.length()>=1) {
                    btnSignUp.setDisable(!(validateRegister(username, password, confPass, firstName, surName)));
                }
            }
        });
        tfRegPassword.setOnKeyTyped(e->{
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String confPass = tfRegConfPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            if(password.length() >= 1 && username.length() >= 1) {
                if(firstName.length()>=1 && surName.length()>=1) {
                    btnSignUp.setDisable(!(validateRegister(username, password, confPass, firstName, surName)));
                }
            }
        });
        tfRegConfPassword.setOnKeyTyped(e->{
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String confPass = tfRegConfPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            if(password.length() >= 1 && username.length() >= 1) {
                if(firstName.length()>=1 && surName.length()>=1) {
                    btnSignUp.setDisable(!(validateRegister(username, password, confPass, firstName, surName)));
                }
            }
        });

        pane.add(lblFirstName, 0, 0);
        pane.add(lblSurName, 0, 1);
        pane.add(lblRegUsername, 0, 2);
        pane.add(lblRegPassword, 0, 3);
        pane.add(lblConfRegPassword, 0, 4);
        pane.add(tfFirstName, 1, 0, 2, 1);
        pane.add(tfSurName, 1, 1, 2, 1);
        pane.add(tfRegUsername, 1, 2, 2, 1);
        pane.add(tfRegPassword, 1, 3, 2, 1);
        pane.add(tfRegConfPassword, 1, 4, 2, 1);

        btnSignUp.setOnAction(d -> {
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String regPass = tfRegPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            UserAccount user = new UserAccount(firstName, surName, username, password);
            boolean b = !(DataCenter.getInstance().validateUser(user));
            boolean c = DataCenter.getInstance().signUpUser(username, password, firstName, surName);
            if (b) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("User creation unsuccessful.");
                alert.setContentText("Username Already Exists");
                alert.showAndWait();
            }
            else if (c=true) {
                if(password.equals(regPass)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(username + ", Welcome to King's Trading!");
                    alert.setContentText("New User Creation Successful.");
                    DataCenter.getInstance().signUpUser(username, password, firstName, surName);
                    DataCenter.getInstance().saveFile();
                    stage.setScene(loginScene);
                    alert.showAndWait();
                }
            } else if (password.length()<6){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid user credentials.");
                alert.setContentText("Failed to validate user, please try again.");
                alert.showAndWait();
            }
        });
        btnCancel.setOnAction(a -> {
            stage.setScene(loginScene);
        });

        return pane;
    }
    public LineChart dayVsPriceLineChart(ArrayList<String> dates, ArrayList<String> prices, String title){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Closing Net");
        LineChart lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(title+" from "+dates.get(1)+", to "+dates.get(dates.size()-1));

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Days vs Net Worth");

        for (int i=1; i<dates.size(); i++) {
            System.out.println(prices.get(i));
            series1.getData().add(new XYChart.Data<>(dates.get(i), Double.valueOf(prices.get(i))));
        }

        lineChart.getData().add(series1);

        return lineChart;
    }
    private Pane homeScreenGUI(){
        GridPane hsPane = new GridPane();
        hsPane.setAlignment(Pos.CENTER);
        hsPane.setPadding(new Insets(20));
        hsPane.setHgap(10);
        hsPane.setVgap(10);


      //  LineChart userNetWorth = dayVsPriceLineChart(null, null);
        Button btnStockAnalyzer = new Button("Stock Market");
        btnStockAnalyzer.setPrefSize(10,40);
        btnStockAnalyzer.setAlignment(Pos.CENTER);
        Button btnHoldings = new Button("Holdings");
        btnHoldings.setPrefSize(10,40);
        btnHoldings.setAlignment(Pos.CENTER);
        Button btnAccount = new Button("Manage Account");
        btnAccount.setPrefSize(10,40);
        btnAccount.setAlignment(Pos.CENTER);

        double totalMoney = currentUser.getTotalMoney(currentUser);
        Label cashAmount = new Label("Total Money: " + roundToDollarAmount(totalMoney));

        cashAmount.setPadding(new Insets(5));
        Button depWith = new Button("Manage Cash");
        depWith.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER_LEFT);
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        HBox hbox2 = new HBox();;
        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.BASELINE_CENTER);
        depWith.setPrefWidth(150);
        btnAccount.setPrefWidth(150);
        btnStockAnalyzer.setPrefWidth(125);
        btnHoldings.setPrefWidth(125);
        cashAmount.setPrefWidth(250);
        cashAmount.setPadding(new Insets(5));
        depWith.setPadding(new Insets(5));
        //userNetWorth.setPrefWidth(450);
        //userNetWorth.setPrefHeight(450);

        Button btnCancel = new Button("Logout");
        btnCancel.setPrefSize(10,40);
        btnCancel.setPrefWidth(100);
        btnCancel.setAlignment(Pos.CENTER);

        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

// Add data to the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stock Prices");
        for (StockData stock : currentUser.getWallet().getStocks()) {
            series.getData().add(new XYChart.Data<>(stock.getSymbol(), stock.getPrice()));
        }
        lineChart.getData().add(series);

        hbox2.getChildren().addAll(btnAccount, btnStockAnalyzer, btnHoldings, btnCancel);
        vbox.getChildren().addAll(lineChart, hbox2);
        hbox.getChildren().addAll(depWith, cashAmount);



        btnAccount.setOnAction(e->{
            asScene = new Scene(accountSceneGUI(), 1200, 800);
            asScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(asScene);
        });

        depWith.setOnAction(e->{
            cashScene = new Scene(cashSceneGUI(), 1200, 800);
            cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(cashScene);
        });

        btnStockAnalyzer.setOnAction(e->{
            marketDataScene = new Scene(marketDataSceneGUI(), 1200, 800);
            marketDataScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(marketDataScene);
        });

        btnHoldings.setOnAction(e->{
            holdingScene = new Scene(holdingSceneGUI(currentUser), 1200, 800);
            holdingScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(holdingScene);
        });

        btnCancel.setOnAction(e->{
            stage.setScene(loginScene);
        });

        hsPane.add(hbox, 0, 1, 1, 1);
        hsPane.add(vbox, 0, 4, 1, 1);

        return hsPane;
    }

    private Pane changePWGUI(){
        GridPane changePw = new GridPane();
        changePw.setAlignment(Pos.CENTER);
        changePw.setPadding(new Insets(20));
        changePw.setHgap(10);
        changePw.setVgap(10);

        Button btnCancel = new Button("Back");
        btnCancel.setPrefWidth(60);
        btnCancel.setAlignment(Pos.CENTER);
        btnCancel.setOnAction(e->{
            stage.setScene(asScene);
        });

        Label lblOldPassword = new Label("Old Password");
        Label lblNewPassword = new Label("New Password");
        Label lblCNewPassword = new Label("Confirm New Password");
        TextField tfOldPassword = new TextField();
        PasswordField tfNewPassword = new PasswordField();
        PasswordField tfCNewPassword = new PasswordField();
        tfOldPassword.setPromptText("Enter Old Password");
        tfNewPassword.setPromptText("Enter New Password");
        tfCNewPassword.setPromptText("Confirm New Password");

        Button btnApply = new Button("Apply");
        btnApply.setDisable(true);

        btnApply.setPrefWidth(60);
        btnApply.setAlignment(Pos.CENTER);

        tfOldPassword.setOnKeyTyped(e -> {
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        tfNewPassword.setOnKeyTyped(e -> {
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        tfCNewPassword.setOnKeyTyped(e -> {
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        btnApply.setOnAction(e->{
            String oldPassword = tfOldPassword.getText();
            String newPassword = tfNewPassword.getText();
            boolean c = DataCenter.getInstance().changePassword(oldPassword, newPassword);
            if(c==true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Password Changed Successfully");
                alert.setContentText("Press 'OK' to Return to the Account Page.");
                currentUser.setPassword(newPassword);
                stage.setScene(asScene);
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Password Unsuccessfully Changed.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        });

        changePw.add(lblOldPassword, 0, 3);
        changePw.add(lblNewPassword, 0, 4);
        changePw.add(lblCNewPassword, 0, 5);

        changePw.add(tfOldPassword, 1, 3, 2, 1);
        changePw.add(tfNewPassword, 1, 4, 2, 1);
        changePw.add(tfCNewPassword, 1, 5, 2, 1);

        changePw.add(btnApply,3,6);
        changePw.add(btnCancel,2,6);

        return  changePw;
    }
    private Pane accountSceneGUI(){
        GridPane asPane = new GridPane();
        asPane.setAlignment(Pos.CENTER);
        asPane.setPadding(new Insets(20));
        asPane.setHgap(10);
        asPane.setVgap(10);

        Button btnCancel = new Button("Back");
        btnCancel.setPrefWidth(60);
        btnCancel.setAlignment(Pos.CENTER);
        btnCancel.setOnAction(e->{
            stage.setScene(hsScene);
        });


        Button btnChangePassword = new Button("Change Password");
        btnChangePassword.setPrefSize(10,40);
        btnChangePassword.setPrefWidth(125);
        btnChangePassword.setAlignment(Pos.CENTER);
        changePWScene = new Scene(changePWGUI(), 1200, 800);
        btnChangePassword.setOnAction(e->{
            changePWScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(changePWScene);
        });

        asPane.add(btnCancel, 1, 0,1,1);
        asPane.add(btnChangePassword, 4, 4,1,1);

        return asPane;
    }
    private Pane cashSceneGUI(){
        GridPane cashPane = new GridPane();
        cashPane.setAlignment(Pos.CENTER);
        cashPane.setPadding(new Insets(20));
        cashPane.setHgap(10);
        cashPane.setVgap(10);

        Button btnCancel = new Button("Back");
        btnCancel.setPrefWidth(60);
        btnCancel.setAlignment(Pos.CENTER);

        btnCancel.setOnAction(e->{
            stage.setScene(hsScene);
        });

        Button btnAddCash = new Button("Deposit Cash");
        Button btnWithCash = new Button("Withdrawal Cash");
        btnAddCash.setDisable(true);
        btnWithCash.setDisable(true);
        TextField tfCashAmount = new TextField();
        tfCashAmount.setPrefWidth(175);
        tfCashAmount.setPromptText("Cash Amount (Up to $10,000)");

        double currentUserHoldings = 0;
        if (currentUser != null) {
            currentUserHoldings = currentUser.getCashAmount();
        }


        Label cashAmount = new Label("Available cash: $" + roundToDollarAmount(currentUserHoldings));
        cashPane.add(btnCancel, 1, 0);


            tfCashAmount.setOnKeyTyped(e -> {
                btnAddCash.setDisable(!validateDeposit(tfCashAmount.getText()));
                btnWithCash.setDisable(!validateWithdrawal(tfCashAmount.getText()));
            });

            btnWithCash.setOnAction(a -> {
                    if (Double.valueOf(tfCashAmount.getText())<=currentUser.getCashAmount()) {
                        System.out.println("Withdrew $" + Double.valueOf(tfCashAmount.getText()) + "");
                        currentUser.setCashAmount(currentUser.getCashAmount() - Double.valueOf(tfCashAmount.getText()));
                        cashScene = new Scene(cashSceneGUI(), 1200, 800);
                        cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                        stage.setScene(cashScene);
                    }
            });

            btnAddCash.setOnAction(b -> {
                    System.out.println("Added $" + tfCashAmount.getText() + "");
                currentUser.setCashAmount(currentUser.getCashAmount() + Double.valueOf(tfCashAmount.getText()));
                cashScene = new Scene(cashSceneGUI(), 1200, 800);
                cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                stage.setScene(cashScene);
            });

        VBox vbox = new VBox();
        HBox hbox = new HBox();
        hbox.getChildren().addAll(btnAddCash, btnWithCash);
        vbox.getChildren().addAll(cashAmount, tfCashAmount);
        cashPane.add(vbox, 4, 8);
        cashPane.add(hbox, 4, 9);

        return cashPane;
    }

    private TextField tfStock;
    private DatePicker start;
    private DatePicker end;
    private Button btnApply;

    public ArrayList<ArrayList<String>> getStockInfo(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        Scanner s = new Scanner(url.openStream());
        ArrayList<String> day = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> volume = new ArrayList<>();
        ArrayList<String> hold = new ArrayList<>();
        String line = "";

            try {
                while (s.hasNext()) {
                    String values[] = s.nextLine().split(",");
                    day.add(values[0]);
                    price.add(values[5]);
                    System.out.println("Date: " + values[0] + ",price " + values[5] +", volume " + values[6]);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ArrayList<ArrayList<String>> ret = new ArrayList<>();
            ret.add(day);
            ret.add(price);
            return ret;



        // 1.open connection to remote file via "urlStr" // Scanner class
        // 2. read from remote (line by line, parse, create a record (arraylist<record>), add this record into ...
        // ... DataCenter -> StockList -> Stock -> list)
        // 3.update TableView/"LineChart"
    }
    public double getCurrentStockPrice(String stockName) throws IOException {
        LocalDate now = LocalDate.now();
        long period = getPeriod(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        URL url = new URL(getURL(stockName, period, period));
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
    private Pane marketDataSceneGUI(){
        GridPane marketDataPane = new GridPane();
        marketDataPane.setAlignment(Pos.CENTER);
        marketDataPane.setPadding(new Insets(20));
        marketDataPane.setHgap(10);
        marketDataPane.setVgap(10);

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        tfStock = new TextField();
        tfStock.setPadding(new Insets(5));
        tfStock.setPrefSize(200, 50);
        tfStock.setPadding(new Insets(20));

        start = new DatePicker();
        start.setPadding(new Insets(20));
        start.setPrefSize(250, 50);
        end = new DatePicker();
        end.setPadding(new Insets(20));
        end.setPrefSize(250, 50);

        btnApply = new Button("Apply");
        btnApply.setPrefSize(70, 50);
        tfStock.setPromptText("Search for Stock by Ticker");
        start.setPromptText("Enter Start Date (MM/DD/YYYY)");
        end.setPromptText("Enter End Date (MM/DD/YYYY)");
        Button btnCancel = new Button("Back");
        btnCancel.setPrefSize(70, 50);
        btnCancel.setAlignment(Pos.CENTER);

        btnCancel.setOnAction(e->{
            stage.setScene(hsScene);
        });


        HBox hbox2 = new HBox();
        hbox.getChildren().addAll(btnCancel, tfStock, start, end);
        hbox.getChildren().add(btnApply);
        vbox.getChildren().addAll(hbox);

        Button btnBuy = new Button("Buy");
        btnBuy.setDisable(true);
        TextField shareCount = new TextField();
        shareCount.setPromptText("Enter share count.");

        Label cost = new Label("");
        cost.setPadding(new Insets(10));

        HBox buyShares = new HBox();
        buyShares.getChildren().addAll(shareCount, cost);

        VBox shareBox = new VBox();
        shareBox.getChildren().addAll(buyShares, btnBuy);

        tfStock.setOnKeyTyped(e->{
            btnBuy.setDisable(!validateBuyButton(tfStock.getText(), shareCount.getText()));
        });

            shareCount.setDisable(true);

        Label lblCurrentPrice = new Label("");
        tfStock.setOnKeyTyped(e->{
            shareCount.setDisable(false);

            double currentPrice = 0;
            try {
                currentPrice = getCurrentStockPrice(tfStock.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            lblCurrentPrice.setText("Stock Value: " + roundToDollarAmount(currentPrice));
        });

        shareCount.setOnKeyTyped(e->{
                    String shareCountString = shareCount.getText();
                    String tickerSearch = tfStock.getText();

                    if(Double.valueOf(shareCountString)>=1 && !tickerSearch.equals("") && !shareCountString.equals("")) {
                        try {
                            btnBuy.setDisable(!validateBuyButton(tfStock.getText(), shareCount.getText()));
                            int shareCountInteger = Integer.valueOf(shareCountString);
                            double currentPrice = getCurrentStockPrice(tfStock.getText());
                            double totalPrice = (shareCountInteger * currentPrice);
                            cost.setText("  Total price: " + roundToDollarAmount(totalPrice));
                        } catch (NumberFormatException | IOException error) {
                            error.printStackTrace();
                        }
                    }
        });



        if (start.getValue() != null && end.getValue() !=null) {
            btnApply.setDisable(false);
            btnApply.setOnAction(e -> {
                LocalDate period1 = start.getValue();
                LocalDate period2 = end.getValue();
                int year1 = period1.getYear();
                int month1 = period1.getMonthValue();
                int day1 = period1.getDayOfMonth();
                int year2 = period2.getYear();
                int month2 = period2.getMonthValue();
                int day2 = period2.getDayOfMonth();

                long startPeriod = getPeriod(year1, month1, day1);
                long endPeriod = getPeriod(year2, month2, day2);

                String stockSymbol = tfStock.getText();
                String strUrl = getURL(stockSymbol, startPeriod, endPeriod);

                ArrayList<ArrayList<String>> ret = new ArrayList<>();
                try {
                    ret = getStockInfo(strUrl);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                hbox2.getChildren().removeAll();
                vbox.getChildren().removeAll();

                LineChart lineChart = dayVsPriceLineChart(ret.get(0), ret.get(1), stockSymbol);

                marketDataPane.add(lineChart, 2, 6);

            });
        }

        btnBuy.setOnAction(e->{

            String shareCountString = shareCount.getText();

            if(!shareCountString.equals("")) {
                try {

                    int shareCountInteger = Integer.valueOf(shareCountString);
                    double currentPrice = getCurrentStockPrice(tfStock.getText());
                    if ((shareCountInteger * currentPrice) > currentUser.getCashAmount()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Purchase Unsuccessful");
                        alert.setContentText("Please try again.");
                        alert.showAndWait();
                    } else {
                        currentUser.getWallet().addStock(tfStock.getText(), currentPrice, shareCountInteger, today());
                        currentUser.setCashAmount(currentUser.getCashAmount() - (shareCountInteger * currentPrice));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Purchase Successful");
                        alert.setContentText("Shares Bought: " + shareCountInteger + ", Amount Purchased: " + roundToDollarAmount(shareCountInteger * currentPrice));
                        alert.showAndWait();
                    }
                } catch (NumberFormatException | IOException error) {
                    error.printStackTrace();
                }
            }
        });

        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().add(vbox);


        marketDataPane.add(hbox2, 2, 0,1,1);
        marketDataPane.add(lblCurrentPrice, 2, 2);
        marketDataPane.add(shareBox, 2, 5,1,1);

        return marketDataPane;
    }

    public LocalDate today(){
        return LocalDate.now();
    }

    public PieChart userPieChart(){
        PieChart pieChart = new PieChart();
        pieChart.setTitle("user"+" Pie Chart");

        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();

        list.add(new PieChart.Data("TSLA", 200));
        list.add(new PieChart.Data("GOGL", 350));
        list.add(new PieChart.Data("AMRS", 560));
        list.add(new PieChart.Data("RBLX", 226));
        list.add(new PieChart.Data("T", 10));

        pieChart.setData(list);

        return pieChart;
    }

    private Pane holdingSceneGUI(UserAccount currentUser){
        GridPane holdingDataPane = new GridPane();
        holdingDataPane.setAlignment(Pos.CENTER);
        holdingDataPane.setPadding(new Insets(20));
        holdingDataPane.setHgap(10);
        holdingDataPane.setVgap(10);

        VBox vbox = new VBox();

        ObservableList<StockData> oList = FXCollections.observableList(currentUser.getWallet().getStocks());
        System.out.println("Listview: " + currentUser.getWallet().getStocks().size());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (StockData stock : currentUser.getWallet().getStocks()) {
            pieChartData.add(new PieChart.Data(stock.getSymbol(), stock.getShares()));
        }

        PieChart pieChart = new PieChart();

        pieChart.setData(pieChartData);

        ListView<StockData> listView = new ListView<>(oList);
        listView.setPrefWidth(500);
        vbox.getChildren().add(listView);


        Button btnCancel = new Button("Back");
        btnCancel.setPrefWidth(60);
        btnCancel.setAlignment(Pos.CENTER);

        btnCancel.setOnAction(e->{
            stage.setScene(hsScene);
        });

        Button btnSell = new Button("Sell");

        btnSell.setOnAction(e->{
            StockData sellStock = listView.getSelectionModel().getSelectedItem();
            try {
                currentUser.setCashAmount(currentUser.getCashAmount() + currentUser.getWallet().sellStock(sellStock, getCurrentStockPrice(sellStock.getSymbol())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            holdingScene = new Scene(holdingSceneGUI(currentUser), 1200, 800);
            holdingScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(holdingScene);
        });

        vbox.getChildren().add(pieChart);

        holdingDataPane.add(btnCancel, 1, 0);
        holdingDataPane.add(vbox, 1, 4);
        holdingDataPane.add(listView, 6, 4);
        holdingDataPane.add(btnSell, 7, 7);

        return holdingDataPane;
    }
    private boolean validateLogin(String username, String password) {
        return (username.length() >= 6 && password.length() >= 6);
    }
    private boolean validateRegister(String username, String password, String confirmPassword, String firstName, String lastName) {
        if (Character.isAlphabetic(username.charAt(0)) && password.equals(confirmPassword)) {
            if (password.length() >= 6 && username.length() >= 6) {
                for (int i = 0; i < password.length(); i++) {
                    if (Character.isDigit(password.charAt(i))) {
                        return (firstName.length() >= 1 && lastName.length() >= 1);
                    }
                }
            }
        }
        return false;
    }

    private boolean validateChangePW(String password, String newPassword, String cNewPassword){
        if(currentUser.getPassword().equals(password) && !password.equals(newPassword)) {
            if(newPassword.equals(cNewPassword) && !cNewPassword.equals(password)) {
                if (cNewPassword.length() >= 6) {
                    for (int i = 0; i < cNewPassword.length(); i++) {
                        if (Character.isDigit(cNewPassword.charAt(i))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean validateBuyButton(String stockTicker, String shareCount){
        if(!shareCount.isEmpty() && !stockTicker.isEmpty()){
            return true;
        }
        return false;
    }

    private boolean validateDeposit(String cashAmount){
        if(Double.valueOf(cashAmount)<=10000 && Double.valueOf(cashAmount)>=1){
            return true;
        }
        return false;
    }

    private boolean validateWithdrawal(String cashAmount){
        if(Double.valueOf(cashAmount)<=10000 && Double.valueOf(cashAmount)<=currentUser.getCashAmount()){
            return true;
        }
        return false;
    }
    private static String roundToDollarAmount(double value) {
        return String.format("%,.2f", value);
    }
}
