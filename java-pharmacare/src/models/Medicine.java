package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Medicine {
    private int id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private String image;
    private boolean requiresPrescription;
    private Date expiryDate;
    private Timestamp createdAt;
    
    public Medicine() {}
    
    public Medicine(String name, String brand, String category, String description, boolean requiresPrescription) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.requiresPrescription = requiresPrescription;
    }
    
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
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }
    
    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", requiresPrescription=" + requiresPrescription +
                '}';
    }
}
