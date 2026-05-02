package pharmacy.system;

import java.math.BigDecimal;
import java.util.Date;

public class Medicine {
    private int medicineId;
    private String name;
    private String category;
    private String manufacturer;
    private BigDecimal price;
    private int stockQuantity;
    private Date expiryDate;
    private boolean prescriptionRequired;
    
    public Medicine() {}
    
    public Medicine(String name, String category, String manufacturer, 
                   BigDecimal price, int stockQuantity, Date expiryDate, 
                   boolean prescriptionRequired) {
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.prescriptionRequired = prescriptionRequired;
    }
    
    // Getters
    public int getMedicineId() { return medicineId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getManufacturer() { return manufacturer; }
    public BigDecimal getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public Date getExpiryDate() { return expiryDate; }
    public boolean isPrescriptionRequired() { return prescriptionRequired; }
    
    // Setters
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public void setPrescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; }
    
    public boolean isExpired() {
        return expiryDate != null && expiryDate.before(new Date());
    }
    
    @Override
    public String toString() {
        return medicineId + " - " + name + " (Stock: " + stockQuantity + ")";
    }
}
