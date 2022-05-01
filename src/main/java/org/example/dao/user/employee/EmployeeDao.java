package org.example.dao.user.employee;

import org.example.entity.BankAcc;
import org.example.entity.Transaction;

import java.sql.ResultSet;
import java.util.List;

public interface EmployeeDao {
    public void approveAccount();
    public void activateAccount(int id);
    public BankAcc viewAccount(int id);
    public List<Transaction> transactions();
    public Transaction getTransaction(ResultSet resultSet);
}
