package org.example.entity;

public class Transaction {
    private int id;
    private int user1;
    private int user2;
    private String tranType;
    private double amount;


    public Transaction(int id, int user1, int user2, String type, double amount) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.tranType = type;
        this.amount = amount;
    }

    public Transaction(int user1, String type, double amount) {
        this.user1 = user1;
        this.tranType = type;
        this.amount = amount;
    }

    public Transaction(int user1, int user2, double amount) {
        this.user1 = user1;
        this.user2 = user2;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public String getTranType() {
        return tranType;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        if(user2 == 0){
            return "Transaction ID:" + id +
                    " | User ID:" + user1 +
                    " | Transaction Type:" + tranType +
                    " | amount=" + amount;
        }
        return  "Transaction ID:" + id +
                " | User ID:" + user1 +
                " | User ID:" + user2 +
                " | Transaction Type:" + tranType +
                " | amount:" + amount;
    }
}
