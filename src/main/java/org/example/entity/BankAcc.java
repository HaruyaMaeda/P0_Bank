package org.example.entity;

import java.lang.ref.SoftReference;

public class BankAcc {
    private int id;
    private String fName;
    private String lName;
    private String userName;
    private String password;
    private double balance;
    private boolean approved;

    public BankAcc(int id, String fName, String lName, String userName, String password, double balance, boolean approved) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
        this.approved = approved;
    }

    public BankAcc(int id, String fName, String lName, String userName, String password, double balance) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }

    public BankAcc(String fName, String lName, String userName, String password, double balance) {
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return  "User ID:" + id +
                " | First Name:" + fName +
                " | Last Name:" + lName +
                " | UserName:" + userName +
                " | Balance:" + balance +
                " | Active:" + approved +
                '}';
    }
}
