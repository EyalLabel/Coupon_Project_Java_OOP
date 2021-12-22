package Beans;

import java.util.ArrayList;
import java.util.Objects;

public class Company {
    private int id;
    private String name;
    private String email;
    private ArrayList<Coupon> coupons;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /***
     * Constructor used for READ actions in CompanyDBDAO
     * @param id
     * @param name
     * @param email
     * @param password
     */

    public Company(int id,String name, String email, String password) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /***
     * Constructor used for UPDATE function and general creation- no ID needed- AUTO INCREMENT
     * @param name
     * @param email
     * @param password
     */
    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", coupons=" + coupons +
                ", password='" + password + '\'' +
                '}';
    }

    public Company() {
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
}
