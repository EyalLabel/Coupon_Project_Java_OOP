package DBDAO;

import Beans.Company;
import Beans.Customer;
import DAO.CustomersDAO;
import Exceptions.CustomerException;
import SQL.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomersDBDao implements CustomersDAO {
    private static final String ADD_CUSTOMER = "INSERT INTO `CouponSystem`.`CUSTOMERS`(`FIRST_NAME`,`LAST_NAME`,`EMAIL`,`PASSWORD`) VALUES(?,?,?,?)";
    private static final String IS_CUSTOMER_EXISTS = "SELECT count(id) FROM couponsystem.customers where email=? and password=? LIMIT 0, 1000";
    private static final String UPDATE_CUSTOMER = "UPDATE `CouponSystem`.`CUSTOMERS` set FIRST_NAME=?,LAST_NAME=?,EMAIL=?,PASSWORD=? WHERE EMAIL=? AND PASSWORD=?";
    private static final String DELETE_CUSTOMER = "DELETE FROM `CouponSystem`.`CUSTOMERS` where id=?";
    private static final String GET_ALL_CUSTOMER = "SELECT * FROM `CouponSystem`.`CUSTOMERS`";
    private static final String GET_ONE_CUSTOMER = "SELECT * FROM `CouponSystem`.`CUSTOMERS` WHERE id=?";
    private static final String GET_ONE_CUSTOMER_BY_LOGIN = "SELECT * FROM `CouponSystem`.`CUSTOMERS` WHERE email=? and password=? LIMIT 0, 1000";
    Connection connection;

    public CustomersDBDao() {
        try {
            ConnectionPool.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * checks if customer exists according to email and password
     * @param email
     * @param password
     * @return true/false
     * @throws SQLException
     */
    @Override
    public boolean isCustomerExists(String email, String password) throws SQLException {
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_CUSTOMER_EXISTS);
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            resultSet.next();
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            System.out.println("failed to check customer existence");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            return (resultSet.getInt(1) > 0);
        }
    }

    /***
     * returns one Customer according to his Email and password
     * @param email
     * @param password
     * @return matching Customer
     * @throws SQLException
     */
    public Customer getCustomerByLogin(String email, String password) throws SQLException {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_CUSTOMER_BY_LOGIN);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getString("EMAIL"), resultSet.getString("PASSWORD"));
            }
            if (isCustomerExists(email, password) == false) {
                throw new CustomerException("Customer does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CustomerException e) {
            System.out.println("Customer does not exist");
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return customer;
    }

    /***
     * adds a customer to the database
     * @param customer
     * @throws SQLException
     */
    @Override
    public void addCustomer(Customer customer) throws SQLException {
        try {
            //get connection to the database
            connection = ConnectionPool.getInstance().getConnection();

            //create a prepared sql statement
            PreparedStatement statement = connection.prepareStatement(ADD_CUSTOMER);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());
            statement.execute();
            if (isCustomerExists(customer.getEmail(), customer.getPassword())) {
                throw new CustomerException("Customer already exists");
            }
            System.out.println("Company added successfully");
        } catch (InterruptedException | SQLException e) {
        } catch (CustomerException e) {
            System.out.println("Customer already exists");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    /***
     * updates an existing customer in teh database
     * @param customer
     * @throws SQLException
     */
    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        try {
            //get connection to the database
            connection = ConnectionPool.getInstance().getConnection();

            //create a prepared sql statement
            PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMER);
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());
            statement.setString(5, customer.getEmail());
            statement.setString(6, customer.getPassword());
            statement.execute();
            if (isCustomerExists(customer.getEmail(), customer.getPassword()) == false) {
                throw new CustomerException("Customer does not exist");
            }
            System.out.println("Customer Updated");
        } catch (InterruptedException | SQLException e) {

        } catch (CustomerException e) {
            System.out.println("Customer does not exist");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    /***
     * Deletes customer from the database
     * @param customerID
     * @throws SQLException
     */
    @Override
    public void deleteCustomer(int customerID) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMER);
            statement.setInt(1, customerID);
            statement.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
    }

    /***
     * Returns all customers
     * @return ArrayList of Customers
     * @throws SQLException
     */
    @Override
    public ArrayList<Customer> getAllCustomers() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CUSTOMER);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getInt("id"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getString("EMAIL"), resultSet.getString("PASSWORD"));
                customers.add(customer);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return customers;
    }

    /***
     * returns one Customer according to it's ID
     * @param customerID
     * @return matching Customer
     * @throws SQLException
     */
    @Override
    public Customer getOneCustomer(int customerID) throws SQLException {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_CUSTOMER);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(resultSet.getInt("id"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getString("EMAIL"), resultSet.getString("PASSWORD"));
            }
            if (customer == null) {
                throw new CustomerException("Customer does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CustomerException e) {
            System.out.println("Customer does not exist");
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return customer;
    }
}
