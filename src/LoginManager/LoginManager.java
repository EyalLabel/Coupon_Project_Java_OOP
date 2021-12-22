package LoginManager;

import Beans.ClientType;
import DBDAO.CompaniesDBDao;
import DBDAO.CustomersDBDao;
import Facade.AdminFacade;
import Facade.ClientFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;

import java.sql.SQLException;

public class LoginManager {
    CompaniesDBDao companiesDBDao = new CompaniesDBDao();
    CustomersDBDao customersDBDao = new CustomersDBDao();

    public ClientFacade login(String email, String password, ClientType clientType) {
        try {
            switch (clientType) {
                case COMPANY:
                    if (companiesDBDao.isCompanyExists(email, password)) {
                        System.out.println("Company logged in successfully");
                        return new CompanyFacade();
                    }
                case CUSTOMER:
                    if (customersDBDao.isCustomerExists(email, password)) {
                        System.out.println("Customer logged in successfully");
                        return new CustomerFacade();
                    }
                case ADMINISTRATOR:
                    if (email.equals("admin@admin.com") && password.equals("admin")) {
                        System.out.println("Admin logged in successfully");
                        return new AdminFacade();
                    }
                default:
                    System.out.println("False login information");
                    return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL ERROR while logging in");
            return null;
        }
    }

    private static LoginManager instance = null;

    private LoginManager() {
        System.out.println("LoginManager Created");
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
}
