package Testers;

import Beans.Category;
import Beans.Coupon;
import Facade.CompanyFacade;
import LoginManager.LoginManager;
import SQL.ConnectionPool;
import Threads.CouponExpirationDailyJob;
import Beans.ClientType;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class CompanyTester {


    public static void companyTestAll() {

            CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("Zuckerpunch@hotmail.com", "87654321", ClientType.COMPANY);
            companyFacade.login("Zuckerpunch@hotmail.com", "87654321");
            Coupon coupon = new Coupon(1, Category.FOOD, "Free milkshake", "you get a free milkshake", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.of(2021, 7, 2)), 22.5, "milkshake.jpg", 7);
            companyFacade.addCoupon(coupon);
            companyFacade.updateCoupon(new Coupon(1, Category.FOOD, "Free milkshake", "you get a free milkshake Oh jeez", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.of(2021, 7, 2)), 22.5, "milkshake.jpg", 7));
            System.out.println(companyFacade.getCompanyCoupons());
            System.out.println(companyFacade.getCompanyCoupons(Category.FOOD));
            System.out.println(companyFacade.getCompanyCoupons(25));
            companyFacade.addCoupon(new Coupon(1, Category.ELECTRICITY, "20% discount on ads", "Advertise for cheap", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.of(2021, 7, 14)), 22.5, "adSale.jpg", 7));
            companyFacade.deleteCoupon(2);
            System.out.println(companyFacade.getCompanyDetails());

    }
}
