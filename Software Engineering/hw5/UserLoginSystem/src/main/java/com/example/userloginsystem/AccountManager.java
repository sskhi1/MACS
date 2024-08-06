package com.example.userloginsystem;

import java.util.HashMap;

public class AccountManager {
    public static final String ROLE = "Account Manager";
    private HashMap<String, String> data;

    public AccountManager(){
        data = new HashMap<>();
        createNewAccount("Patrick", "1234");
        createNewAccount("Molly", "FloPup");
    }

    public boolean accountExists(String username){
        return data.containsKey(username);
    }

    public boolean isCorrectPassword(String username, String password){
        return accountExists(username)
                &&
                data.get(username).equals(password);
    }

    public void createNewAccount(String username, String password){
        data.put(username, password);
    }
}
