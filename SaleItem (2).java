package pharmacy.system;

import java.math.BigDecimal;

/**
 * SaleItem model class representing individual items in a sale
 * @author Member 3
 * @version 1.0
 */
public class SaleItem {
    
    private int saleItemId;
    private int saleId;
    private int medicineId;
    private String medicineName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    
    /**
     * Default constructor
     */
    public SaleItem() {}
    
    /**
     * Parameterized constructor
     */
    public SaleItem(int medicineId, String medicineName, int quantity, BigDecimal unitPrice) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Getters and Setters
    public int getSaleItemId() {
        return saleItemId;
    }
    
    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }
    
    public int getSaleId() {
        return saleId;
    }
    
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    
    public int getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    
    public String getMedicineName() {
        return medicineName;
    }
    
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        if (unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    /**
     * Calculates the subtotal
     */
    public BigDecimal calculateSubtotal() {
        if (unitPrice != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return quantity + " x " + medicineName + " @ $" + unitPrice + " = $" + subtotal;
    }
}
