package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PharmacyMedicine {
    private int id;
    private int pharmacyId;
    private int medicineId;
    private BigDecimal price;
    private int stockQuantity;
    private String availability;
    private Timestamp createdAt;
    
    private String medicineName;
    private String medicineBrand;
    private String pharmacyName;
    
    public PharmacyMedicine() {}
    
    public PharmacyMedicine(int pharmacyId, int medicineId, BigDecimal price, int stockQuantity) {
        this.pharmacyId = pharmacyId;
        this.medicineId = medicineId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        updateAvailability();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPharmacyId() {
        return pharmacyId;
    }
    
    public void setPharmacyId(int pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
    
    public int getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        updateAvailability();
    }
    
    public String getAvailability() {
        return availability;
    }
    
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getMedicineName() {
        return medicineName;
    }
    
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    
    public String getMedicineBrand() {
        return medicineBrand;
    }
    
    public void setMedicineBrand(String medicineBrand) {
        this.medicineBrand = medicineBrand;
    }
    
    public String getPharmacyName() {
        return pharmacyName;
    }
    
    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }
    
    private void updateAvailability() {
        if (stockQuantity <= 0) {
            this.availability = "Out of Stock";
        } else if (stockQuantity <= 5) {
            this.availability = "Limited Stock";
        } else {
            this.availability = "In Stock";
        }
    }
    
    @Override
    public String toString() {
        return "PharmacyMedicine{" +
                "id=" + id +
                ", pharmacyId=" + pharmacyId +
                ", medicineId=" + medicineId +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", availability='" + availability + '\'' +
                '}';
    }
}
