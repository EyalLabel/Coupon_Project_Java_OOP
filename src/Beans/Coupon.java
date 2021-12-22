package Beans;

import java.sql.Date;
import java.util.Objects;

public class Coupon {
    private int id;
    private int companyID;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private double price;
    private String image;
    private int amount;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon)) return false;
        Coupon coupon = (Coupon) o;
        return Double.compare(coupon.price, price) == 0 && Objects.equals(title, coupon.title) && Objects.equals(description, coupon.description) && Objects.equals(startDate, coupon.startDate) && Objects.equals(endDate, coupon.endDate) && Objects.equals(image, coupon.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, startDate, endDate, price, image);
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyID=" + companyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", amount=" + amount +
                '}';
    }

    /***
     * AllArgsConstructor used in READ function in couponDBDAO
     * @param id
     * @param companyID
     * @param category
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param image
     * @param amount
     */
    public Coupon(int id, int companyID, Category category, String title, String description, Date startDate, Date endDate, double price, String image, int amount) {
        this.id = id;
        this.companyID=companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }

    /***
     * No ID constructor used for creating object
     * @param companyID
     * @param category
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param image
     * @param amount
     */
    public Coupon( int companyID, Category category, String title, String description, Date startDate, Date endDate, double price, String image, int amount) {

        this.companyID=companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }
}
