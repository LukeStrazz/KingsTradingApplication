package com.example.kingstradingapplication;


import java.io.*;
import java.util.ArrayList;

public class DataCenter implements Serializable {
    private static DataCenter instance = null;
    private ArrayList<UserAccount> userList = new ArrayList<>();
    private UserAccount user;

    private DataCenter(){
    }

    public static String fileName = "ApplicationData.dat";

    public static DataCenter readFile() {

        try(ObjectInputStream ois = new ObjectInputStream( new DataInputStream(new BufferedInputStream(
                new FileInputStream(fileName))))){
            return((DataCenter) ois.readObject());

        }
        catch(IOException ioe){

        }
        catch(Exception e) {

        }
        return null;
    }

    public static void saveFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(fileName))))){
            oos.writeObject(instance);

        }
        catch(IOException ioe){

        }
        catch(Exception e) {

        }
    }

    public static DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }

    public UserAccount getUser(String username, String password){
        for(int i = 0; i<userList.size(); i++) {
            if(userList.get(i).getUsername().equals(username) && userList.get(i).getPassword().equals(password)){
                return userList.get(i);
            }
        }
        return null;
    }

    public void setUser(String username, String password){

        UserAccount thisUser = getUser(username, password);

    }

    public boolean changePassword(String password, String newPassword){
        for(int i = 0; i<userList.size(); i++) {
            if(userList.get(i).getPassword().equals(password) && !password.equals(newPassword)){
                userList.get(i).setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    public boolean signUpUser(String username, String password, String firstName, String lastName){
        UserAccount user = new UserAccount(firstName, lastName, username, password);
        if (validateUser(user)) {
            return userList.add(user);
        }
        return false;
    }



    public boolean validateUser(UserAccount user){
        for(int i = 0; i<userList.size(); i++) {
            if(userList.get(i).getUsername().equals(user.getUsername())){
                return false;
            }
        }
        return true;
    }
}
