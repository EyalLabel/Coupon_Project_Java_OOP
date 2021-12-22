package Facade;

import Beans.Company;
import Beans.Customer;
import DBDAO.CompaniesDBDao;
import DBDAO.CouponsDBDao;
import DBDAO.CustomersDBDao;
import Exceptions.CustomerException;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminFacade extends ClientFacade {


    public AdminFacade(CustomersDBDao customersDBDao, CouponsDBDao couponsDBDao, CompaniesDBDao companiesDBDao) throws SQLException {
        super(customersDBDao, couponsDBDao, companiesDBDao);
    }

    /***
     * Constructor- creates the Facade
     * @throws SQLException
     */
    public AdminFacade() throws SQLException {
        System.out.println("Welcome Admin");
    }

    /***
     * Logs in to the system
     * @param email
     * @param password
     * @return true or false if the details are correct
     */
    @Override
    public boolean login(String email, String password) {
        return (email.equals("admin@admin.com") && password.equals("admin"));
    }

    /***
     * Adds company to database using CompaniesDBDao
     * @param company
     */
    public void addCompany(Company company) {
        try {
            companiesDBDao.addCompany(company);
        } catch (SQLException throwables) {
            System.out.println("SQL Error while adding Company:\n------------------\n"+throwables.getSQLState());
        }

    }

    /***
     * Updates an existing company using CompaniesDBDao
     * @param company
     */
    public void updateCompany(Company company) {
        try {
            companiesDBDao.updateCompany(company);
        } catch (SQLException throwables) {
            System.out.println("SQL Error while updating Company:\n------------------\n"+throwables.getSQLState());
        }

    }

    /***
     * deletes a company from database using CompaniesDBDao
     * @param companyID
     */
    public void deleteCompany(int companyID) {
        try {
            companiesDBDao.deleteCompany(companyID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * retuns all companies using CompaniesDBDao
     * @return ArrayList of all Company
     */
    public ArrayList<Company> getAllCompanies() {
        try {
            return companiesDBDao.getAllCompanies();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * returns one Company using CompaniesDBDao
     * @param companyID
     * @return matching Company
     */
    public Company getOneCompany(int companyID) {
        Company resultCompany;
        try {
             resultCompany= companiesDBDao.getOneCompany(companyID);
             resultCompany.setCoupons(couponsDBDao.getAllCouponsByCompany(companyID));
             return resultCompany;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * adds a Customer to database using CustomerDBDao
     * @param customer
     */
    public void addCustomer(Customer customer){
        try {
            customersDBDao.addCustomer(customer);
        } catch (SQLException throwables) {
            System.out.println("Error adding Customer");;
        }
    }

    /***
     * updates Customer using CustomerDBDao
     * @param customer
     */
    public void updateCustomer(Customer customer) {
        try {
            customersDBDao.updateCustomer(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * deletes Customer using CustomerDBDao
     * @param customerID
     */
    public void deleteCustomer(int customerID) {
        try {
            customersDBDao.deleteCustomer(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * returns all Customer using CustomerDBDao
     * @return ArrayList of all Customers
     */
    public ArrayList<Customer> getAllCustomers() {
        try {
            return customersDBDao.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * returns one Customer from database using CustomerDBDao
     * @param customerID
     * @return
     */
    public Customer getOneCustomer(int customerID) {
        try {
            return customersDBDao.getOneCustomer(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
