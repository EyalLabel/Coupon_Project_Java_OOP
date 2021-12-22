package SQL;

import Beans.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseManager {
    //connection
    public static final String URL = "jdbc:mysql://localhost:3306?createDatabaseIfNotExist=FALSE";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "12345678";
    //create and drop DB
    private static final String CREATE_DB = "CREATE SCHEMA if not exists CouponSystem ";
    private static final String DROP_DB = "DROP CouponSystem";
    //create and drop tables
    private static final String CREATE_COUPON_TABLE = "CREATE TABLE if not exists `CouponSystem`.`coupons`" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`COMPANY_ID` INT," +
            "`CATEGORY_ID` INT," +
            "`TITLE` VARCHAR(100) NOT NULL," +
            "`DESCRIPTION` VARCHAR(250) NOT NULL," +
            "`START_DATE` DATE NOT NULL," +
            "`END_DATE` DATE NOT NULL," +
            "`AMOUNT` INT NOT NULL," +
            "`PRICE` DECIMAL NOT NULL," +
            "`IMAGE` VARCHAR(150) NOT NULL," +
            "PRIMARY KEY(`id`)," +
            "FOREIGN KEY (`COMPANY_ID`) REFERENCES `COMPANIES`(`ID`) ON DELETE CASCADE," +
            "FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORIES`(`ID`) ON DELETE CASCADE)";
    private static final String DROP_COUPON_TABLE = "DROP TABLE `couponsystem`.`coupons`";
    private static final String CREATE_COMPANIES_TABLE = "CREATE TABLE if not exists `CouponSystem`.`COMPANIES`" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`NAME` VARCHAR(150) NOT NULL UNIQUE," +
            "`EMAIL` VARCHAR(200) NOT NULL UNIQUE," +
            "`PASSWORD` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY(`id`))";
    private static final String DROP_COMPANIES_TABLE = "DROP TABLE `CouponSystem`.`COMPANIES`";
    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE if not exists `CouponSystem`.`CUSTOMERS`" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`FIRST_NAME` VARCHAR(150) NOT NULL," +
            "`LAST_NAME` VARCHAR(150) NOT NULL," +
            "`EMAIL` VARCHAR(200) NOT NULL UNIQUE," +
            "`PASSWORD` VARCHAR(50) NOT NULL," +
            "PRIMARY KEY(`id`))";
    private static final String DROP_CUSTOMERS_TABLE = "DROP TABLE `CouponSystem`.`CUSTOMERS`";
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE if not exists `CouponSystem`.`CATEGORIES`" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`NAME` VARCHAR(150) NOT NULL," +
            "PRIMARY KEY(`id`))";
    private static final String FILL_CATEGORIES_TABLE = "INSERT INTO `CouponSystem`.`CATEGORIES`(`NAME`) VALUES(?)";
    private static final String DROP_CATEGORIES_TABLE = "DROP TABLE `CouponSystem`.`CATEGORIES`";
    private static final String CREATE_CUSTOMER_VS_COUPON_TABLE = "CREATE TABLE IF NOT EXISTS `CouponSystem`.`customer_vs_coupons`" +
            "(`customer_id` INT NOT NULL," +
            "`coupon_id` INT NOT NULL," +
            " PRIMARY KEY (`customer_id`,`coupon_id`)," +
            "INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE," +
            "FOREIGN KEY (`customer_id`) REFERENCES `CUSTOMERS`(`ID`) ON DELETE CASCADE," +
            " FOREIGN KEY (`coupon_id`) REFERENCES `coupons`(`ID`) ON DELETE CASCADE)";
    private static final String DROP_CUSTOMERS_VS_COUPONS_TABLE = "DROP TABLE `couponsystem`.`customer_vs_coupons`";

    public static void createDataBase() {
        try {
            DButils.runQuery(CREATE_DB);
        } catch (SQLException throwables) {
            System.out.println("Failed to create database");
            ;
        }
    }

    public static void fillCategoryTable() {
        for (Category category : Category.values()) {
            try {
                Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(FILL_CATEGORIES_TABLE);
                statement.setString(1, category.toString());
                statement.execute();
            } catch (SQLException | InterruptedException throwables) {
                System.out.println("Failed to create table");
            }
        }
    }

    public static void createCouponTable() {
        try {
            DButils.runQuery(CREATE_COUPON_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to create table");
        }
    }

    public static void createCustomerVsCouponTable() {
        try {
            DButils.runQuery(CREATE_CUSTOMER_VS_COUPON_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to create table");
        }
    }

    public static void createCompaniesTable() {
        try {
            DButils.runQuery(CREATE_COMPANIES_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to create table");
        }
    }

    public static void dropCouponTable() {
        try {
            DButils.runQuery(DROP_COUPON_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop table");
        }
    }

    public static void dropCustomersVsCouponTable() {
        try {
            DButils.runQuery(DROP_CUSTOMERS_VS_COUPONS_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop table");
        }
    }

    public static void createCustomersTable() {
        try {
            DButils.runQuery(CREATE_CUSTOMERS_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to create table");
        }
    }

    public static void dropCustomersTable() {
        try {
            DButils.runQuery(DROP_CUSTOMERS_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop table");
        }
    }

    public static void createCategoriesTable() {
        try {
            DButils.runQuery(CREATE_CATEGORIES_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to create table");
        }
    }

    public static void dropCategoriesTable() {
        try {
            DButils.runQuery(DROP_CATEGORIES_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop table");
        }
    }

    public static void dropCompaniesTable() {
        try {
            DButils.runQuery(DROP_COMPANIES_TABLE);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop table");
        }
    }

    public static void dropDataBase() {
        try {
            DButils.runQuery(DROP_DB);
        } catch (SQLException throwables) {
            System.out.println("Failed to drop database");
        }

    }
}
