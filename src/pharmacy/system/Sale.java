package pharmacy.system;
Ekram

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
}

}
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
Rehemet
public class Sale {
    private int saleId;
    private Date saleDate;
    private String customerName;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private List<SaleItem> items;
    
    public Sale() {}
    
    public Sale(String customerName, BigDecimal totalAmount, String paymentMethod) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.saleDate = new Date();
    }
    
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }
 Ekram
}

}
Rehemet
