package org.example.dao.user.customer;

import org.example.dao.ConnectionFactory;
import org.example.dao.DaoFactory;
import org.example.dao.user.UserDao;
import org.example.dao.user.employee.EmployeeDao;
import org.example.entity.BankAcc;
import org.example.entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerDaoImpl implements CustomerDao{

    Connection connection;
    public CustomerDaoImpl(){
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public void newAccount(BankAcc bankAcc) {
        String sql = "insert into bank (id, fName, lName, userName, password, balance, approved) values (default, ?, ?, ?, ?, ?, false);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bankAcc.getfName());
            preparedStatement.setString(2, bankAcc.getlName());
            preparedStatement.setString(3,bankAcc.getUserName());
            preparedStatement.setString(4,bankAcc.getPassword());
            preparedStatement.setDouble(5,bankAcc.getBalance());

            int count = preparedStatement.executeUpdate();
            if(count == 1){
                System.out.println("Bank Account request sent successfully!");

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                resultSet.next();

                int id = resultSet.getInt(1);
                System.out.println("Your new account id is "+id);
                System.out.println("Don't forget to write it down!");

            }
            else{
                System.out.println("Account requested, but something went wrong when registering!");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void viewBalance(int id) {
        UserDao userDao = DaoFactory.getUserDao();
        String sql = "select * from bank where id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                BankAcc bankAcc = userDao.getAccount(resultSet);
                if (bankAcc.getId() != id) {
                    System.out.println("Something went wrong! Account does not match");
                }
                else {
                    System.out.println("Account ID: "+ id + "\nBalance: $" + bankAcc.getBalance());
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void withdrawal(int id, double balance) {
        UserDao userDao = DaoFactory.getUserDao();
        String sql = "select * from bank where id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                BankAcc bankAcc = userDao.getAccount(resultSet);
                if (bankAcc.getId() != id || !bankAcc.isApproved()) {
                    System.out.println("Something went wrong! Id does not exist or is deactivated");
                }
                else {
                    if (bankAcc.getBalance() >= balance) {
                        String sql1 = "update bank set balance = balance - ? where id = ?;";
                        try {
                            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                            preparedStatement1.setDouble(1, balance);
                            preparedStatement1.setInt(2, id);
                            int count = preparedStatement1.executeUpdate();
                            if (count == 1) {
                                resultSet = preparedStatement.executeQuery();
                                resultSet.next();

                                bankAcc = userDao.getAccount(resultSet);
                                postTransaction(id, "withdraw", balance);
                                System.out.println("You have withdrawn: $" + balance);
                                System.out.println("Current balance: $" + bankAcc.getBalance());
                            } else System.out.println("Error:Multiple Withdrawal");

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else
                        System.out.println("Bank account id:" + bankAcc.getId() + " does not have enough balance for transaction.");
                }
            }
            else System.out.println("No account found?");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deposit(int id, double balance) {
        UserDao userDao = DaoFactory.getUserDao();
            String sql = "select * from bank where id = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    BankAcc bankAcc = userDao.getAccount(resultSet);
                    if (bankAcc.getId() != id || !bankAcc.isApproved()) {
                        System.out.println("Something went wrong! Id does not exist or is deactivated");
                    }
                    else {
                        sql = "update bank set balance = balance + ? where id = ?;";
                        try {
                            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
                            preparedStatement1.setDouble(1, balance);
                            preparedStatement1.setInt(2, id);
                            int count = preparedStatement1.executeUpdate();
                            if (count == 1) {
                                resultSet = preparedStatement.executeQuery();
                                resultSet.next();
                                bankAcc = userDao.getAccount(resultSet);
                                postTransaction(id, "deposit", balance);
                                System.out.println("You have withdrew: $" + balance);
                                System.out.println("Current balance: $" + bankAcc.getBalance());
                            } else System.out.println("Update failed due to count != 1");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void postTransfer(Transaction transaction) {
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        UserDao userDao = DaoFactory.getUserDao();
        String s = "select * from bank where id = ?;";
        String sql = "insert into tran (id, user1, user2, tranType, balance) values (default, ?, ?, 'transferRequest', ?);";
        try {
            PreparedStatement p = connection.prepareStatement(s);
            p.setInt(1, transaction.getUser1());
            ResultSet r = p.executeQuery();
            r.next();
            BankAcc bankAcc = userDao.getAccount(r);
            p.setInt(1, transaction.getUser2());
            ResultSet r2 = p.executeQuery();
            r2.next();
            BankAcc receiving = userDao.getAccount(r2);
            if(employeeDao.viewAccount(transaction.getUser2())!=null && receiving.isApproved()) {
                if (bankAcc.getBalance() > transaction.getAmount()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setInt(1, transaction.getUser1());
                    preparedStatement.setInt(2, transaction.getUser2());
                    preparedStatement.setDouble(3, transaction.getAmount());

                    int count = preparedStatement.executeUpdate();
                    if (count == 1) {
                        System.out.println("Transfer posted!");
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
                        resultSet.next();
                        System.out.println(getTransaction(resultSet));
//                        int id = resultSet.getInt(1);
//                        System.out.println("Transaction ID: " + id);
                    } else {
                        System.out.println("Something was going wrong when posting the transfer!");
                    }
                } else {
                    System.out.println("Not enough funds to transfer! \nCurrent Balance: $" + bankAcc.getBalance());
                    System.out.println("Desired transfer fund: $" + transaction.getAmount());
                }
            }
            else System.out.println("The transfer account destination is deactivate or does not exist");
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void acceptTransfer(int id) {
        Scanner scanner = new Scanner(System.in);
        String sql = "select * from tran;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                UserDao userDao = DaoFactory.getUserDao();
                Transaction transaction = getTransaction(resultSet);
                if(transaction.getUser2()==id&&transaction.getTranType().equals("transferRequest")){
                    transaction.toString();
                    System.out.println("Accept transfer? y/n");
                    String choice = scanner.nextLine();
                    Boolean flag = true;
                    while(flag) {
                        switch (choice) {
                            case "y":
                                String sql0 = "update bank set balance = balance + ? where id = ?;";
                                sql0 += "update bank set balance = balance - ? where id = ?;";
                                sql0 += "insert into tran (id, user1, user2, tranType, balance) values (default, ?, ?, 'transfer', ?);";
                                sql0 +="update tran set tranType = 'transferAccepted' where id = ?;";
                                try {
                                    preparedStatement = connection.prepareStatement(sql0);
                                    preparedStatement.setDouble(1, transaction.getAmount());
                                    preparedStatement.setInt(2, id);

                                    preparedStatement.setDouble(3, transaction.getAmount());
                                    preparedStatement.setInt(4, transaction.getUser1());

                                    preparedStatement.setInt(5,transaction.getUser1());
                                    preparedStatement.setInt(6,transaction.getUser2());
                                    preparedStatement.setDouble(7,transaction.getAmount());

                                    preparedStatement.setInt(8,transaction.getId());
                                    int count = preparedStatement.executeUpdate();
                                    if(count == 4) {
                                        String sql1 = "select * from bank where id = ?";
                                        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                                        preparedStatement1.setInt(1, id);
                                        ResultSet resultSet1 = preparedStatement1.executeQuery();
                                        if(resultSet.next()) {
                                            BankAcc bankAcc = userDao.getAccount(resultSet1);
                                            System.out.println("You have transferred: $" + transaction.getAmount());
                                            System.out.println("Current balance: $" + bankAcc.getBalance());
                                        }
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
//                                deposit(transaction.getUser2(), transaction.getAmount());
//                                withdrawal(transaction.getUser1(), transaction.getAmount());
                                System.out.println("Transfer complete!");
                                flag=false;
                                break;
                            case "n":
                                System.out.println("Transfer declined!");
                                flag=false;
                                break;
                            default:
                                System.out.println("Invalid answer!");
                                break;
                        }
                    }

                }


            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void postTransaction(int id, String type,double balance) {
        String sql = "insert into tran (id, user1, user2, tranType, balance) values (default, ?, 0, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3,balance);

            int count = preparedStatement.executeUpdate();
            if(count == 1){
                System.out.println("Transaction Successful!");
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int idT = resultSet.getInt(1);
                System.out.println("Transaction ID: " + idT);
            }
            else{
                System.out.println("Something was going wrong during the transaction!");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Transaction getTransaction(ResultSet resultSet){
        try {
            int idData = resultSet.getInt("id");
            int user1 = resultSet.getInt("user1");
            int user2 = resultSet.getInt("user2");
            String type = resultSet.getString("tranType");
            double balance = resultSet.getDouble("balance");
            return new Transaction(idData,user1,user2,type,balance);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
