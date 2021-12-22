package Testers;

import SQL.ConnectionPool;
import Threads.CouponExpirationDailyJob;

import java.sql.SQLException;

public class Program {
    public static void main(String[] args) {

        testAll();
    }
    public static void testAll(){
        try {
            DataBaseMaker.dbCreate();
            AdminTester.adminTestAll();
            CompanyTester.companyTestAll();
            CustomerTester.customerTestAll();
            CouponExpirationDailyJob deleteThread;
            deleteThread = new CouponExpirationDailyJob();
            new Thread(deleteThread);
            deleteThread.run();
            ConnectionPool.getInstance().closeAllConnection();
        }catch (SQLException e) {
            System.out.println("Error in daily job");
        } catch (InterruptedException e) {
            System.out.println("Error closing all connections");
        }
    }
}
