package DBDAO;

import Beans.Company;
import Beans.Customer;
import DAO.CompaniesDao;
import Exceptions.CompanyException;
import SQL.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/***
 * The CompaniesDBDAO:
 * Responsible for all Queries regarding the Companies table in the Data Base
 */
public class CompaniesDBDao implements CompaniesDao {
    private static final String ADD_COMPANY = "INSERT INTO `CouponSystem`.`COMPANIES`(`NAME`,`EMAIL`,`PASSWORD`) VALUES(?,?,?)";
    private static final String IS_EXISTS = "SELECT count(id) FROM couponsystem.companies where email=? and password=? LIMIT 0, 1000";
    private static final String UPDATE_COMPANY = "UPDATE `CouponSystem`.`COMPANIES` SET EMAIL=?,PASSWORD=? WHERE NAME=?";
    private static final String DELETE_COMPANY = "DELETE FROM `CouponSystem`.`COMPANIES` where id=?";
    private static final String GET_ALL_COMPANIES = "SELECT * FROM `CouponSystem`.`COMPANIES`";
    private static final String GET_ONE_COMPANY = "SELECT * FROM `CouponSystem`.`COMPANIES` WHERE id=?";
    private static final String GET_ONE_COMPANY_BY_LOGIN = "SELECT * FROM `CouponSystem`.`COMPANIES` WHERE email=? and password=?";
    Connection connection;


    /***
     * constructor
     */
    public CompaniesDBDao() {
        try {
            ConnectionPool.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //

    /***
     * function that returns a company according to the Email and Password
     * @param email
     * @param password
     * @return Company
     * @throws SQLException
     */
    public Company getOneCompanyLogin(String email, String password) throws SQLException {
        // Create the Company that will be returned later
        Company company = null;
        try {
            //get connection to database
            connection = ConnectionPool.getInstance().getConnection();
            //pull statement and enter relevant values
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COMPANY_BY_LOGIN);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                //insert the vaules from the database into the new Object
                company = new Company(resultSet.getInt("id"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("PASSWORD"));
            }
            if (!isCompanyExists(email, password)) {
                throw new CompanyException("Company does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CompanyException e) {
            System.out.println("Company does not exist");
        }
        //close the connection
        ConnectionPool.getInstance().returnConnection(connection);
//return the new company
        return company;
    }

    /***
     * returns true/false if the company exists in the Database
     * @param email
     * @param password
     * @return If company exists
     * @throws SQLException
     */
    @Override
    public boolean isCompanyExists(String email, String password) throws SQLException {
        // create result set
        ResultSet resultSet = null;
        try {
            // get connection
            connection = ConnectionPool.getInstance().getConnection();
            //runs statement that returns the amount of matching ids therefore we can know if a matching company exists
            PreparedStatement statement = connection.prepareStatement(IS_EXISTS);
            statement.setString(1, email);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            resultSet.next();
            //returns the amount of matching IDs (either 1 or 0)
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            System.out.println("failed to check customer existence");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
            return (resultSet.getInt(1) > 0);
        }

    }


    /***
     * Adds a company to the DB
     * @param company
     * @throws SQLException
     */
    @Override
    public void addCompany(Company company) throws SQLException {
        try {
            //get connection to the database
            connection = ConnectionPool.getInstance().getConnection();
            //create a prepared sql statement to insert details from Object
            PreparedStatement statement = connection.prepareStatement(ADD_COMPANY);
            statement.setString(1, company.getName());
            statement.setString(2, company.getEmail());
            statement.setString(3, company.getPassword());
            statement.execute();
            if (isCompanyExists(company.getEmail(), company.getPassword())) {
                throw new CompanyException("Company already exists");
            }
            System.out.println("Company added successfully");
        } catch (InterruptedException | SQLException e) {
        } catch (CompanyException e) {
            System.out.println("Company already exists");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }


    /***
     * updates an existing company using it's name
     * @param company
     * @throws SQLException
     */
    @Override
    public void updateCompany(Company company) throws SQLException {
        try {
            //get connection to the database
            connection = ConnectionPool.getInstance().getConnection();
            //create a prepared sql statement to insert details from Object
            PreparedStatement statement = connection.prepareStatement(UPDATE_COMPANY);
            statement.setString(1, company.getEmail());
            statement.setString(2, company.getPassword());
            statement.setString(3, company.getName());
            statement.execute();
            if (!isCompanyExists(company.getEmail(), company.getPassword())) {
                throw new CompanyException("Company does not exist");
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println("Illegal Action");
        } catch (CompanyException e) {
            System.out.println("Company does not exist");
            ;
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }

    }

    /***
     * Deletes a company from the Database
     * @param companyID
     * @throws SQLException
     */
    @Override
    public void deleteCompany(int companyID) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COMPANY);
            statement.setInt(1, companyID);
            statement.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
    }

    /***
     * Gets all companies
     * @return an ArrayList of all existing companies in the Database
     * @throws SQLException
     */
    @Override
    public ArrayList<Company> getAllCompanies() throws SQLException {
        ArrayList<Company> companies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COMPANIES);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Company company = new Company(resultSet.getInt("id"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("PASSWORD"));
                companies.add(company);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return companies;
    }

    /***
     * returns one Company according to it's ID
     * @param companyID
     * @return a Company
     * @throws SQLException
     */
    @Override
    public Company getOneCompany(int companyID) throws SQLException {
        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COMPANY);
            statement.setInt(1, companyID); //where id = ? => select * from repair where id = 1
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                company = new Company(resultSet.getInt("id"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getString("PASSWORD"));
            }
            if (company == null) {
                throw new CompanyException("Company does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CompanyException e) {
            System.out.println("Company does not exist");
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return company;
    }
}
