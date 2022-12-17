package com.example.kingstradingapplication;

import java.io.*;
import java.util.ArrayList;

public class DataCenter implements Serializable {
    private static ArrayList<UserAccount> userList = new ArrayList<>();
    private UserAccount user;

    private DataCenter(){
    }

    public static String fileName = "ApplicationData.dat";

    public static ArrayList<UserAccount> readFile() {

        try(ObjectInputStream ois = new ObjectInputStream( new DataInputStream(new BufferedInputStream(
                new FileInputStream(fileName))))){
            userList = (ArrayList<UserAccount>) ois.readObject();
            return userList;
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
            oos.writeObject(userList);

        }
        catch(IOException ioe){

        }
        catch(Exception e) {

        }
    }

    public static UserAccount getUser(String username, String password){
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

    public static boolean changePassword(String password, String newPassword){
        for(int i = 0; i<userList.size(); i++) {
            if(userList.get(i).getPassword().equals(password) && !password.equals(newPassword)){
                userList.get(i).setPassword(newPassword);
                //save here on file
                return true;
            }
        }
        return false;
    }

    public static boolean signUpUser(String username, String password, String firstName, String lastName){
        UserAccount user = new UserAccount(firstName, lastName, username, password);
        if (validateUser(user)) {
            return userList.add(user);
            //user object, send to save file here, send user as a parameter not strings, method arg: is user
        }
        return false;
    }

    public static boolean validateUser(UserAccount user){
        for(int i = 0; i<userList.size(); i++) {
            if(userList.get(i).getUsername().equals(user.getUsername())){
                return false;
            }
        }
        return true;
    }
}
