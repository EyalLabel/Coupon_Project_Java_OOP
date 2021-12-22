package Facade;

import DAO.CompaniesDao;
import DBDAO.CompaniesDBDao;
import DBDAO.CouponsDBDao;
import DBDAO.CustomersDBDao;

import java.sql.SQLException;

public abstract class ClientFacade {
    protected CustomersDBDao customersDBDao=new CustomersDBDao();
    protected CouponsDBDao couponsDBDao;

    {
        try {
            couponsDBDao = new CouponsDBDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected CompaniesDBDao companiesDBDao=new CompaniesDBDao();

    public ClientFacade(CustomersDBDao customersDBDao, CouponsDBDao couponsDBDao, CompaniesDBDao companiesDBDao) {
        this.customersDBDao = customersDBDao;
        this.couponsDBDao = couponsDBDao;
        this.companiesDBDao = companiesDBDao;
    }

    public ClientFacade() throws SQLException {
    }

    public boolean login(String email, String password) throws SQLException {
        String rightMail = null, rightPassword = null;
        return (email.equals(rightMail) && password.equals(rightPassword));
    }
}
