package DAO;

import Beans.Coupon;
import Beans.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CouponsDAO {
    void addCoupon(Coupon coupon) throws SQLException, InterruptedException;
    void updateCoupon(Coupon coupon) throws SQLException;
    void deleteCoupon(int customerID) throws SQLException;
    ArrayList<Coupon> getAllCoupons() throws SQLException;
    public boolean isPurchaseExists(int customerID, int couponID) throws SQLException;
    Coupon getOneCoupon(int customerID) throws SQLException;
    void addCouponPurchase(int customerID,int couponID) throws SQLException;
    void deleteCouponPurchase(int customerID,int couponID) throws SQLException;
}
