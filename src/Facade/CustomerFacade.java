package Facade;

import Beans.Category;
import Beans.Coupon;
import Beans.Customer;
import DBDAO.CompaniesDBDao;
import DBDAO.CouponsDBDao;
import DBDAO.CustomersDBDao;
import Exceptions.CustomerException;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {
    public CustomerFacade(CustomersDBDao customersDBDao, CouponsDBDao couponsDBDao, CompaniesDBDao companiesDBDao) throws SQLException {
        super(customersDBDao, couponsDBDao, companiesDBDao);
    }

    /***
     * Constructor makes Customer Facade
     * @throws SQLException
     */
    public CustomerFacade() throws SQLException {
        System.out.println("Welcome customer");
    }

    private int customerID;

    /***
     * Checks if the user details are correct and set the customerID to the ID of the logged Customer
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean login(String email, String password) {
        boolean success = false;
        try {
            if (customersDBDao.isCustomerExists(email, password)) {
                customerID = customersDBDao.getCustomerByLogin(email, password).getId();
                System.out.println("Login Successful");
            }
            success = customersDBDao.isCustomerExists(email, password);
            return (customersDBDao.isCustomerExists(email, password));
        } catch (SQLException e) {
            System.out.println("Login Error");
        }
        return success;
    }

    /***
     * adds a coupon purchase to the coupons_vs_customer table using CouponsDBDao
     * @param coupon
     */
    public void purchaseCoupon(Coupon coupon) {
        try {
            Coupon tempCoupon = couponsDBDao.getOneCouponByTitle(coupon);
            if (couponsDBDao.isPurchaseExists(customerID, tempCoupon.getId()) == false && (!coupon.getEndDate().before(Date.valueOf(LocalDate.now())))) {
                if (tempCoupon.getAmount() > 0) {
                    couponsDBDao.addCouponPurchase(customerID, tempCoupon.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * gets every coupon that was purchased by customer using CouponsDBDao
     * @return ArrayList of matching Coupons
     */
    public ArrayList<Coupon> getCustomerCoupons() {
        ArrayList<Coupon> result = null;
        try {
            result = couponsDBDao.getCouponsByPurchase(customerID);
            return couponsDBDao.getCouponsByPurchase(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * gets every coupon that was purchased by customer using CouponsDBDao of a specific Category
     * @param category
     * @return ArrayList of matching Coupons
     */
    public ArrayList<Coupon> getCustomerCoupons(Category category) {
        ArrayList<Coupon> coupons = getCustomerCoupons();
        ArrayList<Coupon> result = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getCategory() == category) {
                result.add(coupon);
            }
        }
        return result;
    }

    /***
     * gets every coupon that was purchased by customer using CouponsDBDao under a specific price
     * @param maxPrice
     * @return ArrayList of matching Coupons
     */
    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) {
        ArrayList<Coupon> coupons = getCustomerCoupons();
        ArrayList<Coupon> result = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice) {
                result.add(coupon);
            }
        }
        return result;
    }

    /***
     * returns the logged in customer
     * @return Customer
     */
    public Customer getCustomerDetails() {

        try {
            Customer loggedCustomer = customersDBDao.getOneCustomer(customerID);
            loggedCustomer.setCoupons(couponsDBDao.getCouponsByPurchase(customerID));
            return loggedCustomer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
