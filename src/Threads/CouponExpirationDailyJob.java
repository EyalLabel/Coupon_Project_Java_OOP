package Threads;

import Beans.Coupon;
import DBDAO.CouponsDBDao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TimerTask;

public class CouponExpirationDailyJob extends TimerTask implements Runnable {
    private static CouponsDBDao couponsDBDao;

    static {
        try {
            couponsDBDao = new CouponsDBDao();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean quit = false;

    public CouponExpirationDailyJob() throws SQLException {
    }

    @Override
    public void run() {
        while (!quit) {
            try {
            ArrayList<Coupon> coupons = couponsDBDao.getAllCoupons();
            for (Coupon coupon : coupons) {
                if (coupon.getEndDate().before(Date.valueOf(LocalDate.now()))) {

                        couponsDBDao.deleteCoupon(coupon.getId());

                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000*60*60*24);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Thread");
            }
        }
    }
    public void stop(){
        quit=true;
    }
}
