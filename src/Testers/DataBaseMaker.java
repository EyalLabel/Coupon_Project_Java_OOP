package Testers;

import SQL.DataBaseManager;

public class DataBaseMaker {
    //run this first
    public static void dbCreate() {
        DataBaseManager.createDataBase();
        DataBaseManager.createCustomersTable();
        DataBaseManager.createCategoriesTable();
        DataBaseManager.fillCategoryTable();
        DataBaseManager.createCompaniesTable();
        DataBaseManager.createCouponTable();
        DataBaseManager.createCustomerVsCouponTable();
    }
}
