package Facade;

import Beans.Category;
import Beans.Company;
import Beans.Coupon;
import DBDAO.CompaniesDBDao;
import DBDAO.CouponsDBDao;
import DBDAO.CustomersDBDao;
import Exceptions.CompanyException;

import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyFacade extends ClientFacade {
    public CompanyFacade(CustomersDBDao customersDBDao, CouponsDBDao couponsDBDao, CompaniesDBDao companiesDBDao) {
        super(customersDBDao, couponsDBDao, companiesDBDao);
    }

    /***
     * Constructor- creates CompanyFacade
     * @throws SQLException
     */
    public CompanyFacade() throws SQLException {
        System.out.println("Welcome Company");
    }

    private int companyID;

    /***
     * Checks if the user details are correct and set the companyID to the ID of the logged company
     * @param email
     * @param password
     * @return true/false(success)
     */
    @Override
    public boolean login(String email, String password)  {
        boolean success=false;
        try {
            if (companiesDBDao.isCompanyExists(email, password)) {
                companyID = companiesDBDao.getOneCompanyLogin(email, password).getId();
                success=companiesDBDao.isCompanyExists(email, password);
                return companiesDBDao.isCompanyExists(email, password);
            }
        }catch (SQLException e){
            System.out.println("Error logging in company");
        }
        return success;
    }

    /***
     * adds a Coupon to database using CouponDBDao
     * @param coupon
     */
    public void addCoupon(Coupon coupon) {
        boolean okay = true;
        ArrayList<Coupon> companyCoupons = null;
        try {
            companyCoupons = getCompanyCoupons();
            for (Coupon coupon1 : companyCoupons) {
                if (coupon1.getTitle().equals(coupon.getTitle())) {
                    okay = false;
                }
            }
            if (okay) {
                couponsDBDao.addCoupon(coupon);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /***
     * updates existing coupon using CouponDBDao
     * @param coupon
     */
    public void updateCoupon(Coupon coupon) {
        try {
            couponsDBDao.updateCoupon(coupon);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * deletes existing coupon using CouponDBDao
     * @param couponID
     */
    public void deleteCoupon(int couponID) {
        try {
            couponsDBDao.deleteCoupon(couponID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * gets every coupon of logged in Company
     * @return ArrayList of company's Coupons
     */
    public ArrayList<Coupon> getCompanyCoupons() {
        ArrayList<Coupon> result=null;
        try {
            result=couponsDBDao.getAllCouponsByCompany(companyID);
            return couponsDBDao.getAllCouponsByCompany(companyID);
        } catch (SQLException e) {
            e.printStackTrace();
        }return result;
    }

    /***
     * gets every coupon of logged in Company of a specific Category
     * @param category
     * @return ArrayList of matching Coupons
     */
    public ArrayList<Coupon> getCompanyCoupons(Category category) {
        ArrayList<Coupon> coupons = null;
        coupons = getCompanyCoupons();
        ArrayList<Coupon> result = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getCategory() == category) {
                result.add(coupon);
            }
        }
        return result;
    }

    /***
     * gets every coupon of logged in Company under a specific price
     * @param maxPrice
     * @return ArrayList of matching Coupons
     */
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) {
        ArrayList<Coupon> coupons = getCompanyCoupons();
        ArrayList<Coupon> result = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice) {
                result.add(coupon);
            }
        }
        return result;
    }

    /***
     * returns the logged in company
     * @return Company
     */
    public Company getCompanyDetails() {
        Company resultCompany;
        try {
             resultCompany=companiesDBDao.getOneCompany(companyID);
             resultCompany.setCoupons(getCompanyCoupons());
             return resultCompany;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
