package DBDAO;

import Beans.Category;
import Beans.Coupon;
import Beans.Customer;
import DAO.CouponsDAO;
import Exceptions.CouponException;
import SQL.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CouponsDBDao implements CouponsDAO {
    private static final String ADD_COUPON = "INSERT INTO `CouponSystem`.`COUPONS`(`COMPANY_ID`,`CATEGORY_ID`,`TITLE`,`DESCRIPTION`,`START_DATE`,`END_DATE`,`AMOUNT`,`PRICE`,`IMAGE`) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_COUPON = "UPDATE `CouponSystem`.`COUPONS` set CATEGORY_ID=?,TITLE=?,DESCRIPTION=?,START_DATE=?,END_DATE=?,AMOUNT=?,PRICE=?,IMAGE=? WHERE COMPANY_ID=? AND TITLE=?";
    private static final String DELETE_COUPON = "DELETE FROM `CouponSystem`.`COUPONS` where id=?";
    private static final String IS_COUPON_EXIST = "SELECT count(ID) FROM `CouponSystem`.`COUPONS` WHERE COMPANY_ID=? AND TITLE=?";
    private static final String GET_ALL_COUPONS = "SELECT * FROM `CouponSystem`.`COUPONS`";
    private static final String GET_ONE_COUPON = "SELECT * FROM `CouponSystem`.`COUPONS` WHERE id=?";
    private static final String GET_ONE_COUPON_BY_TITLE = "SELECT * FROM `CouponSystem`.`COUPONS` WHERE COMPANY_ID=? AND TITLE=?";
    private static final String ADD_COUPON_PURCHASE = "INSERT INTO `CouponSystem`.`customer_vs_coupons`(`customer_id`,`coupon_id`) VALUES(?,?)";
    private static final String REMOVE_ONE_COUPON = "UPDATE `CouponSystem`.`COUPONS` set AMOUNT=AMOUNT-1 WHERE id=?";
    private static final String DELETE_COUPON_PURCHASE = "DELETE FROM CouponSystem`.`customer_vs_coupons` WHERE customer_id=? AND coupon_id=?";
    private static final String GET_COUPON_ID_FROM_PURCHASE = "SELECT coupon_id FROM couponsystem.customer_vs_coupons where customer_id=? LIMIT 0, 1000";
    private static final String PURCHASE_EXISTS = "SELECT count(customer_id) FROM `CouponSystem`.`customer_vs_coupons` WHERE customer_id=? AND coupon_id=?";
    private static final String GET_ALL_COUPONS_BY_COMPANY = "SELECT * FROM `CouponSystem`.`COUPONS` WHERE COMPANY_ID=?";
    Connection connection;

    public CouponsDBDao() throws SQLException {
        try {
            ConnectionPool.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * checks if a coupon exists in the Data base
     * @param coupon
     * @return true/false
     * @throws SQLException
     */
    public boolean isCouponExist(Coupon coupon) throws SQLException {
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_COUPON_EXIST);
            statement.setInt(1, coupon.getCompanyID());
            statement.setString(2, coupon.getTitle());
            resultSet = statement.executeQuery();
            resultSet.next();
            ConnectionPool.getInstance().returnConnection(connection);
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            System.out.println("failed to check customer existence");
            ConnectionPool.getInstance().returnConnection(connection);
            return false;
        }
    }

    /***
     * returns all the coupons made by a specific company
     * @param companyID
     * @return an ArrayList by a single Company
     * @throws SQLException
     */
    public ArrayList<Coupon> getAllCouponsByCompany(int companyID) throws SQLException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS_BY_COMPANY);
            statement.setInt(1, companyID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                coupons.add(getOneCoupon(resultSet.getInt(1)));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return coupons;
    }

    /***
     * Checks if a purchase Exists by checking the couponID and CustomerID
     * @param customerID
     * @param couponID
     * @return true or false
     * @throws SQLException
     */
    @Override
    public boolean isPurchaseExists(int customerID, int couponID) throws SQLException {
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(PURCHASE_EXISTS);
            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            resultSet = statement.executeQuery();
            resultSet.next();
            ConnectionPool.getInstance().returnConnection(connection);
            return (resultSet.getInt(1) > 0);
        } catch (InterruptedException | SQLException e) {
            System.out.println("failed to check customer existence");
            ConnectionPool.getInstance().returnConnection(connection);
            return false;
        }
    }

    /***
     * returns all the Coupons that were purchased by a specific Customer
     * @param customerID
     * @return ArrayList of coupons
     * @throws SQLException
     */
    public ArrayList<Coupon> getCouponsByPurchase(int customerID) throws SQLException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_COUPON_ID_FROM_PURCHASE);
            statement.setInt(1, customerID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                coupons.add(getOneCoupon(resultSet.getInt(1)));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return coupons;
    }

    /***
     * adds a coupon to the database
     * @param coupon
     * @throws SQLException
     * @throws InterruptedException
     */
    @Override
    public void addCoupon(Coupon coupon) throws SQLException, InterruptedException {
        try {
            connection = ConnectionPool.getInstance().getConnection();

            //create a prepared sql statement
            PreparedStatement statement = connection.prepareStatement(ADD_COUPON);
            statement.setInt(1, coupon.getCompanyID());
            statement.setInt(2, coupon.getCategory().ordinal() + 1);
            statement.setString(3, coupon.getTitle());
            statement.setString(4, coupon.getDescription());
            statement.setDate(5, coupon.getStartDate());
            statement.setDate(6, coupon.getEndDate());
            statement.setInt(7, coupon.getAmount());
            statement.setDouble(8, coupon.getPrice());
            statement.setString(9, coupon.getImage());
            statement.execute();
            if (isCouponExist(coupon)) {
                throw new CouponException("Coupon already exists");
            }
            System.out.println("Coupon added successfully");
        } catch (InterruptedException | SQLException e) {

        } catch (CouponException e) {
            System.out.println("Coupon already exists");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }

    }

    /***
     * Updates an existing coupon
     * @param coupon
     * @throws SQLException
     */
    @Override
    public void updateCoupon(Coupon coupon) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();

            //create a prepared sql statement
            PreparedStatement statement = connection.prepareStatement(UPDATE_COUPON);
            statement.setInt(1, coupon.getCategory().ordinal() + 1);
            statement.setString(2, coupon.getTitle());
            statement.setString(3, coupon.getDescription());
            statement.setDate(4, coupon.getStartDate());
            statement.setDate(5, coupon.getEndDate());
            statement.setInt(6, coupon.getAmount());
            statement.setDouble(7, coupon.getPrice());
            statement.setString(8, coupon.getImage());
            statement.setInt(9, coupon.getCompanyID());
            statement.setString(10, coupon.getTitle());
            statement.execute();
            if (!isCouponExist(coupon)) {
                throw new CouponException("Coupon does not exist");
            }
            System.out.println("Coupon added successfully");
        } catch (InterruptedException | SQLException e) {
        } catch (CouponException e) {
            System.out.println("Coupon does not exist");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    /***
     * deletes a coupon from database
     * @param customerID
     * @throws SQLException
     */
    @Override
    public void deleteCoupon(int customerID) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON);
            statement.setInt(1, customerID);
            statement.execute();
        } catch (InterruptedException e) {
            System.out.println("Failed to delete coupon");
        } catch (SQLException throwables) {
            System.out.println("Failed to delete coupon");
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }
    }

    /***
     * Returns every coupon in the database
     * @return ArrayList of Coupons
     * @throws SQLException
     */
    @Override
    public ArrayList<Coupon> getAllCoupons() throws SQLException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Coupon coupon = new Coupon(resultSet.getInt("id"),
                        resultSet.getInt("COMPANY_ID"),
                        Category.values()[resultSet.getInt("CATEGORY_ID") - 1],
                        resultSet.getString("TITLE"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getDate("START_DATE"),
                        resultSet.getDate("END_DATE"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("IMAGE"),
                        resultSet.getInt("AMOUNT")
                );
                coupons.add(coupon);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return coupons;
    }

    /***
     * returns one coupon according to it's ID
     * @param couponID
     * @return one Coupon with matching ID
     * @throws SQLException
     */
    @Override
    public Coupon getOneCoupon(int couponID) throws SQLException {
        Coupon coupon = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COUPON);
            statement.setInt(1, couponID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                coupon = new Coupon(resultSet.getInt("id"),
                        resultSet.getInt("COMPANY_ID"),
                        Category.values()[resultSet.getInt("CATEGORY_ID") - 1],
                        resultSet.getString("TITLE"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getDate("START_DATE"),
                        resultSet.getDate("END_DATE"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("IMAGE"),
                        resultSet.getInt("AMOUNT")
                );
            }
            if (!isCouponExist(coupon)) {
                throw new CouponException("Coupon does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CouponException e) {
            System.out.println("Coupon does not exist");
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return coupon;
    }

    /***
     * Returns a coupon according to it's CompanyID and Title
     * @param coupon
     * @return a matching Coupon
     * @throws SQLException
     */
    public Coupon getOneCouponByTitle(Coupon coupon) throws SQLException {
        Coupon coupon1 = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COUPON_BY_TITLE);
            statement.setInt(1, coupon.getCompanyID());
            statement.setString(2, coupon.getTitle());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                coupon1 = new Coupon(resultSet.getInt("id"),
                        resultSet.getInt("COMPANY_ID"),
                        Category.values()[resultSet.getInt("CATEGORY_ID") - 1],
                        resultSet.getString("TITLE"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getDate("START_DATE"),
                        resultSet.getDate("END_DATE"),
                        resultSet.getDouble("PRICE"),
                        resultSet.getString("IMAGE"),
                        resultSet.getInt("AMOUNT")
                );
            }
            if (!isCouponExist(coupon)) {
                throw new CouponException("Coupon does not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (CouponException e) {
            System.out.println("Coupon does not exist");
        }
        ConnectionPool.getInstance().returnConnection(connection);
        return coupon1;
    }

    /***
     * Adds a coupon purchase to the Customer_vs_Coupon table
     * @param customerID
     * @param couponID
     * @throws SQLException
     */
    @Override
    public void addCouponPurchase(int customerID, int couponID) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();

            //create a prepared sql statement
            PreparedStatement statement1 = connection.prepareStatement(ADD_COUPON_PURCHASE);
            statement1.setInt(1, customerID);
            statement1.setInt(2, couponID);
            statement1.execute();
            PreparedStatement statement2 = connection.prepareStatement(REMOVE_ONE_COUPON);
            statement2.setInt(1, couponID);
            statement2.execute();
            System.out.println("Coupon purchase added successfully");
        } catch (InterruptedException | SQLException e) {
        } finally {
            ConnectionPool.getInstance().returnConnection(connection);
        }

    }

    /***
     * Deletes purchase from Customer_vs_Coupon
     * @param customerID
     * @param couponID
     * @throws SQLException
     */
    @Override
    public void deleteCouponPurchase(int customerID, int couponID) throws SQLException {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON_PURCHASE);
            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            statement.execute();
            System.out.println("Purchase deleted successfully");
        } catch (InterruptedException e) {
            System.out.println("Failed to delete purchase");
        } catch (SQLException throwables) {
            System.out.println("Failed to delete purchase");
        }
        ConnectionPool.getInstance().returnConnection(connection);
    }
}
