package org.example.dao.user.employee;

import org.example.dao.ConnectionFactory;
import org.example.dao.DaoFactory;
import org.example.dao.user.UserDao;
import org.example.entity.BankAcc;
import org.example.entity.Transaction;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeDaoImpl implements EmployeeDao{

    Connection connection;
    public EmployeeDaoImpl(){
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public void approveAccount() {
        UserDao userDao = DaoFactory.getUserDao();
        Scanner scanner = new Scanner(System.in);
        String sql = "Select * from bank where approved = '0';";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                BankAcc bankAcc = userDao.getAccount(resultSet);
                System.out.println(bankAcc.toString());
                System.out.println("Approve Account? y/n");
                String choice = scanner.nextLine();
                if(choice.equals("y")){
                    sql = "Update bank set approved = true where id = ?;";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1,bankAcc.getId());
                    int count = preparedStatement.executeUpdate();
                    if(count == 1) System.out.println("Update successful!");
                    else System.out.println("Something went wrong with the update!");;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("No more accounts to approve!");
    }

    @Override
    public void activateAccount(int id) {
        UserDao userDao = DaoFactory.getUserDao();
        Scanner scanner = new Scanner(System.in);
        String sql = "Select * from bank where id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                BankAcc bankAcc = userDao.getAccount(resultSet);
                if(bankAcc.getId() != id){
                    System.out.println("No account with ID!");
                }
                bankAcc.toString();
                boolean flag = true;
                while (flag) {
                    System.out.println("Activate Account? y/n");
                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "y":
                            sql = "Update bank set approved = true where id = ?;";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setInt(1, id);
                            int count = preparedStatement.executeUpdate();
                            if (count == 1) System.out.println("Update successful!");
                            else System.out.println("Something went wrong with the update!");
                            flag = false;
                            break;
                        case "n":
                            sql = "Update bank set approved = false where id = ?;";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setInt(1, id);
                            count = preparedStatement.executeUpdate();
                            if (count == 1) System.out.println("Update successful!");
                            else System.out.println("Something went wrong with the update!");
                            flag = false;
                            break;
                        default:
                            System.out.println("Invalid option!");
                            break;
                    }

                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public BankAcc viewAccount(int id) {
        UserDao userDao = DaoFactory.getUserDao();
        String sql = "select * from bank where id = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                BankAcc bankAcc = userDao.getAccount(resultSet);
                if(bankAcc.getId() != id){
                    System.out.println("Something went wrong!");
                    return null;
                }
                return bankAcc;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> transactions() {
        List<Transaction> tr = new ArrayList<Transaction>();
        String sql = "select * from tran;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Transaction transaction = getTransaction(resultSet);
                tr.add(transaction);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tr;
    }
    @Override
    public Transaction getTransaction(ResultSet resultSet){
        try {
            int idData = resultSet.getInt("id");
            int user1 = resultSet.getInt("user1");
            int user2 = resultSet.getInt("user2");
            String tranType = resultSet.getString("tranType");
            int balance = resultSet.getInt("balance");
            return new Transaction(idData,user1,user2,tranType,balance);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


}
