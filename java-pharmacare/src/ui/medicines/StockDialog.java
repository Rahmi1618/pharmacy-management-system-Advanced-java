package ui.medicines;

import dao.PharmacyMedicineDAO;
import models.Medicine;
import models.PharmacyMedicine;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockDialog extends JDialog {
    private Medicine medicine;
    private PharmacyMedicineDAO pharmacyMedicineDAO;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    
    public StockDialog(Frame parent, Medicine medicine) {
        super(parent, "Stock Information - " + medicine.getName(), true);
        this.medicine = medicine;
        this.pharmacyMedicineDAO = new PharmacyMedicineDAO();
        initComponents();
        loadStockData();
    }
    
    private void initComponents() {
        setSize(800, 500);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Stock Availability Across Pharmacies");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Pharmacy", "Price (ETB)", "Stock Quantity", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        stockTable = new JTable(tableModel);
        stockTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockTable.setRowHeight(30);
        stockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadStockData() {
        try {
            List<PharmacyMedicine> stocks = pharmacyMedicineDAO.findByMedicine(medicine.getId());
            tableModel.setRowCount(0);
            
            for (PharmacyMedicine pm : stocks) {
                Object[] row = {
                    pm.getPharmacyName(),
                    String.format("%.2f", pm.getPrice()),
                    pm.getStockQuantity(),
                    pm.getAvailability()
                };
                tableModel.addRow(row);
            }
            
            if (stocks.isEmpty()) {
                UIHelper.showWarning(this, "No stock information available for this medicine");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load stock data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
