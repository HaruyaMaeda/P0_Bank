package org.example.dao;

import org.example.dao.user.UserDao;
import org.example.dao.user.UserDaoImpl;
import org.example.dao.user.customer.CustomerDao;
import org.example.dao.user.customer.CustomerDaoImpl;
import org.example.dao.user.employee.EmployeeDao;
import org.example.dao.user.employee.EmployeeDaoImpl;

public class DaoFactory {
    private static CustomerDao customerDao;
    private static EmployeeDao employeeDao;
    private static UserDao userDao;

    private DaoFactory(){

    }

    public static CustomerDao getCustomerDao(){
        if (customerDao == null){
            customerDao = new CustomerDaoImpl();
        }
        return customerDao;
    }

    public static EmployeeDao getEmployeeDao(){
        if (employeeDao == null){
            employeeDao = new EmployeeDaoImpl();
        }
        return employeeDao;
    }

    public static UserDao getUserDao(){
        if (userDao == null){
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

}
