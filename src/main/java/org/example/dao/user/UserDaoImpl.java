package org.example.dao.user;

import org.example.dao.ConnectionFactory;
import org.example.dao.user.customer.CustomerDao;
import org.example.dao.DaoFactory;
import org.example.dao.user.employee.EmployeeDao;
import org.example.entity.BankAcc;
import org.example.entity.Employee;
import org.example.entity.Transaction;

import java.sql.*;
import java.util.Scanner;

public class UserDaoImpl implements UserDao {

    Connection connection;
    public UserDaoImpl(){
        connection = ConnectionFactory.getConnection();
    }


    @Override
    public void loginEmp(String userName, String password) {
        Scanner scanner = new Scanner(System.in);
        String sql = "select * from emp where userName = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Employee employee = getEmpAccount(resultSet);
                if (employee.getPassword().equals(password)) {
                    System.out.println("welcome Back, " + userName + "!");
                    EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
                    boolean flag = true;
                    while (flag) {
                        System.out.println("Menu: ");
                        System.out.println("1: Approve/Reject Accounts");
                        System.out.println("2: Activate/deactivate Accounts");
                        System.out.println("3: View Customer Accounts");
                        System.out.println("4: View Transactions");
                        System.out.println("5: Logout");
                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                employeeDao.approveAccount();
                                break;
                            case 2:
                                System.out.println("Input the account ID you want to activate:");
                                int id = (int) validateNumber();
                                employeeDao.activateAccount(id);
                                break;
                            case 3:
                                System.out.println("Input the account ID you want to view:");
                                id = (int) validateNumber();
                                if (employeeDao.viewAccount(id) == null) {
                                    System.out.println("No bank account with ID!");
                                    break;
                                } else {
                                    System.out.println(employeeDao.viewAccount(id).toString());
                                    break;
                                }
                            case 4:
                                System.out.println("Viewing Transactions...");
                                for (Transaction transaction : employeeDao.transactions()) {
                                    System.out.println(transaction.toString());
                                }
                                break;
                            case 5:
                                flag = false;
                                break;
                            default:
                                System.out.println("Invalid choice! Choose again");
                                break;
                        }
                    }
                }
                else {
                    System.out.println("Failed to login. Username and/or password may be incorrect.");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void loginCus(String userName, String password) {
        Scanner scanner = new Scanner(System.in);
        String sql = "select * from bank where userName = ?;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                BankAcc bankAcc = getAccount(resultSet);
                if(bankAcc.getPassword().equals(password)){
                    if(bankAcc.isApproved()==true) {
                        CustomerDao customerDao = DaoFactory.getCustomerDao();
                        System.out.println("Welcome back, " + bankAcc.getfName() + "!");
                        boolean flag = true;
                        while (flag) {
                            System.out.println("Menu: ");
                            System.out.println("1: Apply for new account");
                            System.out.println("2: View balance");
                            System.out.println("3: Withdraw");
                            System.out.println("4: Deposit");
                            System.out.println("5: Transfer fund to another account");
                            System.out.println("6: Check and Accept transfer of funds");
                            System.out.println("7: Logout");
                            int choice = Integer.parseInt(scanner.nextLine());;

                            switch (choice) {
                                case 1:
                                    System.out.println("Please enter first name:");
                                    String fName = scanner.nextLine();
                                    System.out.println("Please enter last name:");
                                    String lName = scanner.nextLine();
                                    System.out.println("Please enter desired userName:");
                                    String newName = scanner.nextLine();
                                    System.out.println("Please enter password:");
                                    String newPassword = scanner.nextLine();
                                    System.out.println("Please enter desired starting balance:");
                                    double balance = validateNumber();

                                    BankAcc newAcc = new BankAcc(fName, lName, newName, newPassword, balance);
                                    System.out.println("Bank account entered: " + newAcc.toString());
                                    customerDao.newAccount(newAcc);
                                    break;
                                case 2:
                                    System.out.println("Please enter account id:");
                                    int idV = (int)validateNumber();
                                    customerDao.viewBalance(idV);
                                    break;
                                case 3:
                                    System.out.println("Please enter account id:");
                                    int idW = (int)validateNumber();
                                    System.out.println("Please enter amount to withdraw:");
                                    double amountW = validateNumber();
                                    customerDao.withdrawal(idW, amountW);
                                    break;
                                case 4:
                                    System.out.println("Please enter account id:");
                                    int idD = (int)validateNumber();
                                    System.out.println("Please enter amount to deposit:");
                                    double amountD = validateNumber();
                                    customerDao.deposit(idD, amountD);
                                    break;
                                case 5:
                                    System.out.println("Please enter id of the account to transfer to:");
                                    int id2 = (int)validateNumber();
                                    System.out.println("Please enter desired balance:");
                                    double balanceT = validateNumber();
                                    Transaction transaction = new Transaction(bankAcc.getId(), id2, balanceT);
                                    customerDao.postTransfer(transaction);

                                    break;
                                case 6:
                                    System.out.println("Please enter account id:");
                                    int idCT = (int)validateNumber();
                                    customerDao.acceptTransfer(idCT);
                                    break;
                                case 7:
                                    flag = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice! Choose again");
                                    break;
                            }
                        }
                    }
                    else {
                        System.out.println("Account is deactivated.");
                    }
                }
                else {
                    System.out.println("Failed to login. Your password in incorrect.");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void register(BankAcc bankAcc) {
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
                System.out.println("Bank Account registered successfully! Check back to see if your account has been approved!");

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                resultSet.next();

                int id = resultSet.getInt(1);

                System.out.println("Your bank id is " + id);
                System.out.println("Make sure to write it down!");
            }
            else{
                System.out.println("Something went wrong when registering!");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public BankAcc getAccount(ResultSet resultSet){
        try {
            int idData = resultSet.getInt("id");
            String fName = resultSet.getString("fName");
            String lName = resultSet.getString("lName");
            String userName = resultSet.getString("userName");
            String password = resultSet.getString("password");
            double balance = resultSet.getDouble("balance");
            boolean approved = resultSet.getBoolean("approved");
            return new BankAcc(idData,fName,lName,userName,password,balance,approved);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Employee getEmpAccount(ResultSet resultSet){
        try {
            int idData = resultSet.getInt("id");
            String fName = resultSet.getString("fName");
            String lName = resultSet.getString("lName");
            String userName = resultSet.getString("userName");
            String password = resultSet.getString("password");
            return new Employee(idData,fName,lName,userName,password);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //https://kodejava.org/how-do-i-validate-input-when-using-scanner/
    @Override
    public double validateNumber(){
        Scanner scanner = new Scanner(System.in);

        double number = 0;
        do {
            if(number<0) System.out.println("Please Enter a positive number: ");
            while (!scanner.hasNextDouble()) {
                String input = scanner.next();
                System.out.printf("\"%s\" is not a valid number.%nPlease enter a positive number: ", input);
            }
            number = scanner.nextDouble();
        } while (number < 0);

        return number;
    }

}
