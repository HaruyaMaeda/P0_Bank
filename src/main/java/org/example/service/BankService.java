package org.example.service;

import org.example.dao.DaoFactory;
import org.example.dao.user.UserDao;
import org.example.entity.BankAcc;

import java.util.Scanner;

public class BankService {
    public static void custLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter password:");
        String password = scanner.nextLine();
        UserDao userDao = DaoFactory.getUserDao();
        userDao.loginCus(username,password);
    }

    public static void empLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter password:");
        String password = scanner.nextLine();
        UserDao userDao = DaoFactory.getUserDao();
        userDao.loginEmp(username,password);
    }

    public static void register(){
        UserDao userDao = DaoFactory.getUserDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter first name: ");
        String fName = scanner.nextLine();
        System.out.println("Please enter last name:");
        String lName = scanner.nextLine();
        System.out.println("Please enter desired userName:");
        String userName = scanner.nextLine();
        System.out.println("Please enter password:");
        String password = scanner.nextLine();
        System.out.println("Please enter desired starting balance:");
        double balance = userDao.validateNumber();


        BankAcc bankAcc = new BankAcc(fName,lName,userName,password,balance);
        System.out.println("Bank account entered: " + bankAcc.toString());

        userDao.register(bankAcc);
    }
}
