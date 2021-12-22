package Beans;

import java.util.ArrayList;
import java.util.Objects;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private ArrayList<Coupon> coupons;
    private String email;
    private String password;



    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", coupons=" + coupons +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    /***
     * Constructor used for READ functions in CustomerDBDAO
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public Customer(int id,String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /***
     * Constructor used for creating Object no need for ID
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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


}
