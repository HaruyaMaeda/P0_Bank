package org.example;

import org.example.service.BankService;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag){
            System.out.println("Select option:");
            System.out.println("1: Login as Customer");
            System.out.println("2: Login as Employee");
            System.out.println("3: Register new account");
            System.out.println("4: Quit");

            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    BankService.custLogin();
                    break;
                case 2:
                    BankService.empLogin();
                    break;
                case 3:
                    BankService.register();
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }

    }
}
