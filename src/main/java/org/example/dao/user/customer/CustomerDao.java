package org.example.dao.user.customer;

import org.example.entity.BankAcc;
import org.example.entity.Transaction;

public interface CustomerDao {
    public void newAccount(BankAcc bankAcc);
    public void viewBalance(int id);
    public void withdrawal(int id, double balance);
    public void deposit(int id, double balance);
    public void postTransfer(Transaction transaction);
    public void acceptTransfer(int id);
}
