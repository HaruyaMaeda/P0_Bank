package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.example.dao.DaoFactory;
import org.example.dao.user.customer.CustomerDao;
import org.example.dao.user.employee.EmployeeDao;
import org.example.entity.Transaction;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void activateAccountTest()
    {
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        employeeDao.activateAccount(4);

        //assertEquals("No Account with that ID!", );
    }
    @Test
    public void viewAccountTest()
    {
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        assertEquals("bob", employeeDao.viewAccount(1).getfName());
        assertEquals("billy", employeeDao.viewAccount(2).getfName());
        assertEquals(null, employeeDao.viewAccount(100));
    }

    @Test
    public void withdrawTest()
    {
        CustomerDao customerDao = DaoFactory.getCustomerDao();
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        customerDao.withdrawal(1,10);
        customerDao.viewBalance(1);
        employeeDao.transactions();

    }

    @Test
    public void depositTest()
    {
        CustomerDao customerDao = DaoFactory.getCustomerDao();
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        customerDao.deposit(1,10);
        customerDao.viewBalance(1);
        employeeDao.transactions();

    }

    @Test
    public void transferTest()
    {
        CustomerDao customerDao = DaoFactory.getCustomerDao();
        EmployeeDao employeeDao = DaoFactory.getEmployeeDao();
        Transaction transaction = new Transaction(1,2,10);

        customerDao.postTransfer(transaction);
        customerDao.acceptTransfer(2);
        customerDao.viewBalance(1);
        customerDao.viewBalance(2);
        employeeDao.transactions();

    }
}
