package Testers;

import Beans.Category;
import Beans.Coupon;
import Facade.CustomerFacade;
import LoginManager.LoginManager;
import Beans.ClientType;
import SQL.ConnectionPool;
import Threads.CouponExpirationDailyJob;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerTester {


    public static void customerTestAll() {

            CustomerFacade customerFacade = (CustomerFacade) LoginManager.getInstance().login("zeevmindali@gmail.com", "123456789", ClientType.CUSTOMER);
            customerFacade.login("zeevmindali@gmail.com", "123456789");
            customerFacade.purchaseCoupon(new Coupon(1, Category.FOOD, "Free milkshake", "you get a free milkshake Oh jeez", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.of(2021, 7, 2)), 22.5, "milkshake.jpg", 7));
            System.out.println(customerFacade.getCustomerCoupons());
            System.out.println(customerFacade.getCustomerCoupons(25));
            System.out.println(customerFacade.getCustomerCoupons(Category.FOOD));
            System.out.println(customerFacade.getCustomerDetails());


    }
}
