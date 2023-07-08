package com.rehman.womansecuritysystem.Model;

public class UserModel
{
    String name;
    String userName;
    String phoneNumber;
    String email;
    String password;
    String accountCreationKey;
    String accountType;

    public UserModel(String name, String userName, String phoneNumber, String email, String password, String accountCreationKey, String accountType) {
        this.name = name;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.accountCreationKey = accountCreationKey;
        this.accountType = accountType;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountCreationKey() {
        return accountCreationKey;
    }

    public void setAccountCreationKey(String accountCreationKey) {
        this.accountCreationKey = accountCreationKey;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
