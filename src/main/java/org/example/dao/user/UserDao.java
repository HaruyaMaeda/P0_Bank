package org.example.dao.user;

import org.example.entity.BankAcc;

import java.sql.ResultSet;
import java.util.List;

public interface UserDao {
    public void loginEmp(String userName, String password);
    public void loginCus(String userName, String password);
    public void register(BankAcc bankAcc);
    public BankAcc getAccount(ResultSet resultSet);
    public double validateNumber();
}
