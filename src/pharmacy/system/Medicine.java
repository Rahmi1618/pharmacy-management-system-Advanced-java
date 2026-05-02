package pharmacy.system;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Medicine model class representing a medicine in the pharmacy
 * @author Member 2
 * @version 1.0
 */
public class Medicine {
    
    // Private fields (encapsulation)
    private int medicineId;
    private String name;
    private String category;
    private String manufacturer;
    private BigDecimal price;
    private int stockQuantity;
    private Date expiryDate;
    private boolean prescriptionRequired;
    private Date createdAt;
    private Date updatedAt;
    
    /**
     * Default constructor
     */
    public Medicine() {}
    
    /**
     * Parameterized constructor for creating a new medicine
     * @param name Medicine name
     * @param category Medicine category
     * @param manufacturer Manufacturer name
     * @param price Unit price
     * @param stockQuantity Available stock
     * @param expiryDate Expiration date
     * @param prescriptionRequired Whether prescription is needed
     */
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
    
    // ========== GETTERS AND SETTERS ==========
    
    public int getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public boolean isPrescriptionRequired() {
        return prescriptionRequired;
    }
    
    public void setPrescriptionRequired(boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Checks if medicine is expired
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        Date today = new Date();
        return expiryDate != null && expiryDate.before(today);
    }
    
    /**
     * Checks if stock is low (less than 20 units)
     * @return true if low stock, false otherwise
     */
    public boolean isLowStock() {
        return stockQuantity < 20;
    }
    
    /**
     * Reduces stock quantity by specified amount
     * @param quantity Quantity to reduce
     * @return true if successful, false if insufficient stock
     */
    public boolean reduceStock(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Increases stock quantity by specified amount
     * @param quantity Quantity to add
     */
    public void increaseStock(int quantity) {
        stockQuantity += quantity;
    }
    
    @Override
    public String toString() {
        return medicineId + " - " + name + " (Stock: " + stockQuantity + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medicine medicine = (Medicine) obj;
        return medicineId == medicine.medicineId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(medicineId);
    }
}
