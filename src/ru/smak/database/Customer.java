package ru.smak.database;

import com.lambdaworks.crypto.SCryptUtil;

public class Customer {
    private String phone;
    private String login;
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = SCryptUtil.scrypt(password, 16, 16, 16);
    }

    public void setHashedPassword(String passwordHash){
        password = passwordHash;
    }

    public boolean verifyPassword(String originalPassword, String storedPassword) {
        return SCryptUtil.check(originalPassword, storedPassword);
    }

    public Customer(String phone, String login, String password){
        setPhone(phone);
        setLogin(login);
        try {
            setPassword(password);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Customer(){}

    @Override
    public String toString(){
        return phone + ": " + login + " (*****)";
    }
}
