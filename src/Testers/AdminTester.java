package Testers;

import Beans.Company;
import Beans.Customer;
import Facade.AdminFacade;
import LoginManager.LoginManager;
import SQL.ConnectionPool;
import Threads.CouponExpirationDailyJob;
import Beans.ClientType;

import java.sql.SQLException;


public class AdminTester {


    public static void adminTestAll() {
        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
        adminFacade.login("admin@admin.com", "admin");
        Company fake = new Company("fakebook", "Zuckerpunch@hotmail.com", "12345678");
        adminFacade.addCompany(fake);
        adminFacade.updateCompany(new Company("fakebook", "Zuckerpunch@hotmail.com", "87654321"));
        adminFacade.updateCompany(new Company("fakrebook", "Zuckerpunch@hotmail.com", "87654321"));
        System.out.println(adminFacade.getAllCompanies());
        System.out.println(adminFacade.getOneCompany(1));
        adminFacade.addCompany(new Company("Tnuva","parotHalav@walla.com","123456"));
        adminFacade.deleteCompany(2);
        Customer zeev = new Customer("zeev", "mindali", "zeevmindali@gmail.com", "123456789");
        adminFacade.addCustomer(zeev);
        adminFacade.updateCustomer(new Customer("zeevik", "mindali", "zeevmindali@gmail.com", "123456789"));
        adminFacade.addCustomer(new Customer("Lior","Label","LiorLabel@gmail.com","789456"));
        adminFacade.deleteCustomer(2);
        System.out.println(adminFacade.getAllCustomers());
        System.out.println(adminFacade.getOneCustomer(1));
    }
}
