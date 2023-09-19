package com.example.kingstradingapplication;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StockApp extends Application implements Serializable {
    private Scene loginScene, regScene, hsScene, asScene, marketDataScene, holdingScene, cashScene, changePWScene;
    private Stage stage;
    public static UserAccount currentUser = new UserAccount("", "", "", "");

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            this.stage = primaryStage;

            Pane loginGui = loginGUI();
            loginScene = new Scene(loginGui, 1900, 1000);
            primaryStage.setTitle("King's Trading");
            loginScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());


            primaryStage.setScene(loginScene);
            primaryStage.show();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Unable to access King-s Trading.");
            alert.setContentText("King's Trading runs on Yahoo Finance. \nOn certain Days or Hours, the app will be inoperable. \n\nPlease Come Back Soon!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DataCenter.readFile();
        launch();
        DataCenter.saveFile();
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


        loginPane.add(lblUsername, 0, 1);
        loginPane.add(lblPassword, 0, 2);
        loginPane.add(tfUsername, 1, 1, 2, 1);
        loginPane.add(tfPassword, 1, 2, 2, 1);

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
        loginPane.add(hbox, 1, 3, 2, 1);
        loginPane.add(hbox2, 1, 4, 2, 1);

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
            UserAccount c = DataCenter.getUser(username, password);
            if (c != null) {
                currentUser = c;
                System.out.println(currentUser.getUsername());
                try {
                    hsScene = new Scene(homeScreenGUI(), 1200, 800);
                } catch (IOException e) {
                   // throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Welcome Back "+username+"!");
                alert.setContentText("User login successful.");
                alert.showAndWait();
                Pane hsSceneGUI = null;
                try {
                    hsSceneGUI = homeScreenGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                hsScene = new Scene(hsSceneGUI,1900, 1000);
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


        btnSignUp.setOnAction(a -> {
            regScene = new Scene(registerGUI(),1900, 1000);
            regScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(regScene);
        });
        btnCancel.setOnAction(c -> {
            DataCenter.saveFile();
            Platform.exit();
        });

        Label titleLabel = new Label("King's Trading");
        titleLabel.setId("title-label");
        loginPane.add(titleLabel, 1, 0);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

// Set the start value for the animation
        KeyValue start = new KeyValue(titleLabel.styleProperty(), ""); // default style

// Set the end value for the animation
        KeyValue end = new KeyValue(titleLabel.styleProperty(), "highlight");

// Create a key frame with the start and end values
        KeyFrame keyFrame = new KeyFrame(Duration.millis(2000), start, end);

// Add the key frame to the timeline
        timeline.getKeyFrames().add(keyFrame);

// Play the timeline
        timeline.play();

        titleLabel.setStyle(".shine");

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

        Label lblRRegUsername = new Label("");
        Label lblRRegPassword = new Label("");
        Label lblRConfRegPassword = new Label("");

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
            lblRRegUsername.setText("Username Must be Longer Than 6 Characters.");
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
            lblRRegPassword.setText("Password Must be Longer Than Six Characters,\nPassword Must Have One Number.");
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
            lblRConfRegPassword.setText("Passwords Must Match.");
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
        pane.add(lblRRegUsername, 4, 2);
        pane.add(lblRRegPassword, 4, 3);
        pane.add(lblRConfRegPassword, 4, 4);

        btnSignUp.setOnAction(d -> {
            String username = tfRegUsername.getText();
            String password = tfRegPassword.getText();
            String regPass = tfRegPassword.getText();
            String firstName = tfFirstName.getText();
            String surName = tfSurName.getText();
            UserAccount user = new UserAccount(firstName, surName, username, password);
            boolean b = !(DataCenter.validateUser(user));
            boolean c = DataCenter.signUpUser(username, password, firstName, surName);
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
                    DataCenter.signUpUser(username, password, firstName, surName);
                    DataCenter.saveFile();
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

    private Pane homeScreenGUI() throws IOException {
        GridPane hsPane = new GridPane();
        hsPane.setAlignment(Pos.CENTER);
        hsPane.setPadding(new Insets(20));
        hsPane.setHgap(10);
        hsPane.setVgap(10);

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
        Label cashAmount = new Label("Total Money: $" + roundToDollarAmount(totalMoney) + "\nAvailable Cash: $" + roundToDollarAmount(currentUser.getCashAmount()));

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

        Button btnCancel = new Button("Logout");
        btnCancel.setPrefSize(10,40);
        btnCancel.setPrefWidth(100);
        btnCancel.setAlignment(Pos.CENTER);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Welcome back "+currentUser.getFirstName() +
                ",\nTotal Investment Evaluation: $ " + roundToDollarAmount(totalMoney - currentUser.getCashAmount()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Seven Day Performance");

        ArrayList<LocalDate> dates = new ArrayList<>();
        for (int i=6; i>=0; i--) {
            dates.add(LocalDate.now().minusDays(i));
        }
        double[] networth = new double[7];

        for (StockData stock : currentUser.getWallet().getStocks()) {
            for (int i=0; i<7; i++) {
                if (!dates.get(i).isBefore(stock.getDatePurchased())) {
                    networth[i] += (stock.getShares()*getCurrentStockPrice(stock.getSymbol()));
                }
            }
        }
        for (int i=0; i<networth.length; i++) {
            if (networth[i] != 0) {
                series.getData().add(new XYChart.Data<>(dates.get(i).toString(), networth[i]));
            }
        }

        lineChart.getData().add(series);

        lineChart.setPrefHeight(500);
        lineChart.setPrefWidth(1000);

        hbox2.getChildren().addAll(btnAccount, btnStockAnalyzer, btnHoldings, btnCancel);
        vbox.getChildren().addAll(lineChart, hbox2);
        hbox.getChildren().addAll(depWith, cashAmount);


        btnAccount.setOnAction(e->{
            asScene = new Scene(accountSceneGUI(),1900, 1000);
            asScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(asScene);
        });

        depWith.setOnAction(e->{
            cashScene = new Scene(cashSceneGUI(),1900, 1000);
            cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(cashScene);
        });

        btnStockAnalyzer.setOnAction(e->{
            marketDataScene = new Scene(marketDataSceneGUI(),1900, 1000);
            marketDataScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(marketDataScene);
        });

        btnHoldings.setOnAction(e->{
            try {
                holdingScene = new Scene(holdingSceneGUI(currentUser),1900, 1000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            holdingScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(holdingScene);
        });

        btnCancel.setOnAction(e->{
            DataCenter.saveFile();
            loginScene = new Scene(loginGUI(),1900, 1000);
            loginScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
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
            asScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
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


        Label lblchangePW = new Label("");
        Label lnlCompChange = new Label("");
        Label lblOrigPw = new Label("");

        Button btnApply = new Button("Apply");
        btnApply.setDisable(true);

        btnApply.setPrefWidth(60);
        btnApply.setAlignment(Pos.CENTER);

        tfOldPassword.setOnKeyTyped(e -> {
            lblOrigPw.setText("Password Must Match User's Current Password.");
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        tfNewPassword.setOnKeyTyped(e -> {
            lblchangePW.setText("Password Must be Longer Than 6 Characters, and Have a Letter and a Number.");
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        tfCNewPassword.setOnKeyTyped(e -> {
            lnlCompChange.setText("New Passwords Must Match.");
            btnApply.setDisable(!(validateChangePW(tfOldPassword.getText(),tfNewPassword.getText(),tfCNewPassword.getText())));
        });

        btnApply.setOnAction(e->{
            String oldPassword = tfOldPassword.getText();
            String newPassword = tfNewPassword.getText();
            boolean c = DataCenter.changePassword(oldPassword, newPassword);
            if(c==true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Password Changed Successfully");
                alert.setContentText("Press 'OK' to Return to the Account Page.");
                currentUser.setPassword(newPassword);
                loginScene = new Scene(loginGUI(),1900, 1000);
                asScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
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


        changePw.add(lblOrigPw, 8, 3);
        changePw.add(lblchangePW, 8, 4);
        changePw.add(lnlCompChange, 8, 5);

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
            try {
                hsScene = new Scene(homeScreenGUI(),1900, 1000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            hsScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(hsScene);
        });


        Button btnChangePassword = new Button("Change Password");
        btnChangePassword.setPrefSize(10,40);
        btnChangePassword.setPrefWidth(200);
        btnChangePassword.setAlignment(Pos.CENTER);
        changePWScene = new Scene(changePWGUI(),1900, 1000);
        btnChangePassword.setOnAction(e->{
            changePWScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(changePWScene);
        });

        asPane.add(btnCancel, 0, 0);
        asPane.add(btnChangePassword, 10, 10);

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
            try {
                hsScene = new Scene(homeScreenGUI(),1900, 1000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            hsScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
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
                        currentUser.getWallet().addWithdrawal(Double.valueOf(tfCashAmount.getText()), LocalDate.now());
                        cashScene = new Scene(cashSceneGUI(),1900, 1000);
                        cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                        stage.setScene(cashScene);
                    }
            });

            btnAddCash.setOnAction(b -> {
                    System.out.println("Added $" + tfCashAmount.getText() + "");
                currentUser.setCashAmount(currentUser.getCashAmount() + Double.valueOf(tfCashAmount.getText()));
                currentUser.getWallet().addDeposit(Double.valueOf(tfCashAmount.getText()), LocalDate.now());
                cashScene = new Scene(cashSceneGUI(),1900, 1000);
                cashScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                stage.setScene(cashScene);
            });
//here
        List<CashData> cashTransactionList = currentUser.getWallet().getCash();
        Collections.reverse(cashTransactionList);
        ObservableList<CashData> oList = FXCollections.observableList(cashTransactionList);


        ListView<CashData> listView = new ListView<>(oList);
        listView.setPrefWidth(600);
        listView.setPrefHeight(1000);
        cashPane.add(listView, 6, 4);

        VBox vbox = new VBox();
        HBox hbox = new HBox();

        hbox.getChildren().addAll(btnAddCash, btnWithCash);
        vbox.getChildren().addAll(cashAmount, tfCashAmount);
        cashPane.add(vbox, 2, 1);
        cashPane.add(hbox, 2, 2);

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

    public String getUR2(String stockSymbol, long period1, long period2) {
        String urlTemplate = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=%d&period2=%d&interval=1d&events=history&includeAdjustedClose=true";
        String urlStr = String.format(urlTemplate, stockSymbol, period1, period2);
        return urlStr;
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
            try {
                hsScene = new Scene(homeScreenGUI(),1900, 1000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            hsScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
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


            shareCount.setDisable(true);

        Label lblCurrentPrice = new Label("");
        tfStock.setOnKeyTyped(e->{
            btnBuy.setDisable(!validateBuyButton(tfStock.getText(), shareCount.getText()));
            shareCount.setDisable(false);

            double currentPrice = 0;
            try {
                currentPrice = getCurrentStockPrice(tfStock.getText());

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            lblCurrentPrice.setText("Stock Value: " + roundToDollarAmount(currentPrice));
            cost.setText("  Available Cash: $" + roundToDollarAmount(currentUser.getCashAmount()));

            if(!shareCount.getText().equals(null)){
                String shareCountString = shareCount.getText();
                try {
                    btnBuy.setDisable(!validateBuyButton(tfStock.getText(), shareCount.getText()));
                    int shareCountInteger = Integer.valueOf(shareCountString);

                    double totalPrice = (shareCountInteger * currentPrice);
                    cost.setText("  Available Cash: $" + roundToDollarAmount(currentUser.getCashAmount()) + "\n  Total price: " + roundToDollarAmount(totalPrice) );
                } catch (NumberFormatException error) {
                    error.printStackTrace();
                }            }
        });

        shareCount.setOnKeyTyped(e->{
                    String shareCountString = shareCount.getText();
                    String tickerSearch = tfStock.getText();
                    try {
                        if (Double.valueOf(shareCountString) >= 1 && !tickerSearch.equals("") && !shareCountString.equals("")) {
                            try {
                                btnBuy.setDisable(!validateBuyButton(tfStock.getText(), shareCount.getText()));
                                int shareCountInteger = Integer.valueOf(shareCountString);
                                double currentPrice = getCurrentStockPrice(tfStock.getText());
                                double totalPrice = (shareCountInteger * currentPrice);
                                cost.setText("  Available Cash: $" + roundToDollarAmount(currentUser.getCashAmount()) + "\n  Total price: " + roundToDollarAmount(totalPrice) );
                            } catch (NumberFormatException | IOException error) {
                            error.printStackTrace();
                            }
                        }
                    } catch (NumberFormatException t) {

                    }
        });



            btnApply.setDisable(false);
            btnApply.setOnAction(e -> {
                if (start.getValue() != null && end.getValue() != null) {
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

                    LineChart<String, Number> lineChart = dayVsPriceLineChart(ret.get(0), ret.get(1), stockSymbol);
                    LineChart<String, Number> lineChartFive = fiveDayVsPriceLineChart(ret.get(0), ret.get(1), stockSymbol);

                    hbox2.getChildren().removeAll();
                    vbox.getChildren().removeAll();
                    GridPane newPane = (GridPane) marketDataSceneGUI();
                    newPane.add(lineChart, 2, 6);
                    newPane.add(lineChartFive, 2, 8);
                    marketDataScene = new Scene(newPane,1900, 1000);
                    marketDataScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                    stage.setScene(marketDataScene);

                }
                });


        btnBuy.setOnAction(e->{
            String shareCountString = shareCount.getText();
            if(!shareCountString.equals("")) {
                try {
                    int shareCountInteger = Integer.valueOf(shareCountString);
                    double currentPrice = getCurrentStockPrice(tfStock.getText());
                    if ((shareCountInteger * currentPrice) > currentUser.getCashAmount()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Purchase Unsuccessful");
                        alert.setContentText("User may have insufficient funds. Please try again.");
                        alert.showAndWait();
                    } else {
                        currentUser.getWallet().addStock(tfStock.getText(), currentPrice, shareCountInteger, today(), "Purchase");
                        currentUser.setCashAmount(currentUser.getCashAmount() - (shareCountInteger * currentPrice));
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Purchase Successful");
                        alert.setContentText("Shares Bought: " + shareCountInteger + ", Amount Purchased: " + roundToDollarAmount(shareCountInteger * currentPrice));
                        GridPane newPane = (GridPane) marketDataSceneGUI();
                        marketDataScene = new Scene(newPane,1900, 1000);
                        marketDataScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
                        stage.setScene(marketDataScene);
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

    public LineChart dayVsPriceLineChart(ArrayList<String> dates, ArrayList<String> prices, String title){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Closing Net");
        LineChart lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(title + " from " + dates.get(1) + ", to " + dates.get(dates.size()-1));

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Days vs Net Worth");

        for (int i=1; i<dates.size(); i++) {
            System.out.println(prices.get(i));
            series1.getData().add(new XYChart.Data<>(dates.get(i), Double.valueOf(prices.get(i))));
        }

        lineChart.getData().add(series1);

        return lineChart;
    }

    LineChart<String, Number> fiveDayVsPriceLineChart(ArrayList<String> dates, ArrayList<String> prices, String stockSymbol) {
        ArrayList<String> movingAverages = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            double sum = 0;
            int count = 0;
            for (int j = i; j > i - 5 && j >= 1; j--) {
                sum += Double.valueOf(prices.get(j));
                count++;
            }
            movingAverages.add(String.valueOf(sum / count));
        }
        // Create the x-axis and y-axis
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Price");

        // Create the line chart
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("5 Day Moving Average for " + stockSymbol);

        // Create a series for the 5 day moving average values and add it to the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("5 Day Moving Average");
        for (int i = 0; i < movingAverages.size(); i++) {
            series.getData().add(new XYChart.Data<>(dates.get(i), Double.valueOf(movingAverages.get(i))));
        }
        lineChart.getData().add(series);

        return lineChart;
    }

    private Pane holdingSceneGUI(UserAccount currentUser) throws IOException {
        GridPane holdingDataPane = new GridPane();
        holdingDataPane.setAlignment(Pos.CENTER);
        holdingDataPane.setPadding(new Insets(20));
        holdingDataPane.setHgap(10);
        holdingDataPane.setVgap(10);

        VBox vbox = new VBox();

        List<StockData> stockList = currentUser.getWallet().getStocks();
        Collections.reverse(stockList);
        ObservableList<StockData> oList = FXCollections.observableList(stockList);


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double totalValue = 0;
        for (StockData stock : currentUser.getWallet().getStocks()) {
            String symbol = stock.getSymbol();
            double value = stock.getShares() * getCurrentStockPrice(symbol);
            totalValue += value;
        }

        for (StockData stock : currentUser.getWallet().getStocks()) {
            String symbol = stock.getSymbol();
            double value = stock.getShares() * getCurrentStockPrice(symbol);
            double percentage = value / totalValue * 100;
            boolean found = false;
            for (PieChart.Data data : pieChartData) {
                if (data.getName().equals(symbol)) {
                    data.setPieValue(percentage);
                    found = true;
                    break;
                }
            }
            if (!found) {
                pieChartData.add(new PieChart.Data(symbol, percentage));
            }
        }

        PieChart pieChart = new PieChart();
        double investmentMoney = currentUser.getTotalMoney(currentUser) - currentUser.getCashAmount();
        pieChart.setTitle(currentUser.getFirstName() + "'s Portfolio and Recent Transactions.\nTotal Investment: $" + roundToDollarAmount(investmentMoney));
        pieChart.setData(pieChartData);

        ListView<StockData> listView = new ListView<>(oList);
        listView.setPrefWidth(620);
        vbox.getChildren().add(listView);

        Button btnCancel = new Button("Back");
        btnCancel.setPrefWidth(60);
        btnCancel.setAlignment(Pos.CENTER);




        btnCancel.setOnAction(e->{
            try {
                hsScene = new Scene(homeScreenGUI(),1900, 1000);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            hsScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(hsScene);
        });

        Label labelInstructions = new Label("Please Select A Transaction to Sell a Stock From.");
        TextField tfShareCount = new TextField();
        tfShareCount.setPromptText("Enter Share(s) Count to Sell");
        tfShareCount.setPrefWidth(60);
        Button btnSell = new Button("Sell");
        btnSell.setPrefWidth(60);

        VBox sellVbox = new VBox();
        sellVbox.getChildren().addAll(tfShareCount, btnSell);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        btnSell.setOnAction(e->{
            StockData sellStock = listView.getSelectionModel().getSelectedItem();
            String sharesString = tfShareCount.getText();
            int sharesInt = 0;
            try {
                sharesInt = Integer.valueOf(sharesString);
            } catch (NumberFormatException error) {
                alert.setHeaderText("Invalid Entry.");
                alert.setContentText("Share Count Must be a Whole Number,\nOr Text-Field is Empty.");
                alert.showAndWait();
                return;
            }
            if (sharesInt > ((int)sellStock.getShares())) {
                alert.setHeaderText("User Does Not Own Enough Shares!");
                alert.showAndWait();

                return;
            }
            try {
                alert.setHeaderText("Sell Successful.");
                alert.setContentText("You Have Sold "+sharesInt+" Shares of " + sellStock.getSymbol());
                alert.showAndWait();
                currentUser.setCashAmount(currentUser.getCashAmount() + currentUser.getWallet().sellStock(sellStock, getCurrentStockPrice(sellStock.getSymbol()), sharesInt));
                currentUser.getWallet().newSell(sellStock, getCurrentStockPrice(sellStock.getSymbol()), sharesInt);
                holdingScene = new Scene(holdingSceneGUI(currentUser),1900, 1000);
            } catch (IOException ex) {
               throw new RuntimeException(ex);
            }
            holdingScene.getStylesheets().add(getClass().getResource("ApplicationStyle.css").toExternalForm());
            stage.setScene(holdingScene);
        });

        vbox.getChildren().add(pieChart);


        List<SellStock> sellTransactionList = (currentUser.getWallet().getSell());
        ObservableList<SellStock> oList2 = FXCollections.observableList(sellTransactionList);
        ListView<SellStock> sellTransactionListView = new ListView<>(oList2);

        sellTransactionListView.setPrefWidth(620);

        holdingDataPane.add(sellTransactionListView, 8, 2);

        holdingDataPane.add(btnCancel, 1, 0);
        holdingDataPane.add(vbox, 1, 2);
        holdingDataPane.add(listView, 4, 2);
        holdingDataPane.add(labelInstructions, 6, 8);
        holdingDataPane.add(tfShareCount, 6, 9);
        holdingDataPane.add(btnSell, 6, 10);

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
