package ui.pharmacies;

import dao.PharmacyMedicineDAO;
import models.Pharmacy;
import models.PharmacyMedicine;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryDialog extends JDialog {
    private Pharmacy pharmacy;
    private PharmacyMedicineDAO pharmacyMedicineDAO;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    
    public InventoryDialog(Frame parent, Pharmacy pharmacy) {
        super(parent, "Inventory - " + pharmacy.getName(), true);
        this.pharmacy = pharmacy;
        this.pharmacyMedicineDAO = new PharmacyMedicineDAO();
        initComponents();
        loadInventoryData();
    }
    
    private void initComponents() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Pharmacy Inventory");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Medicine", "Brand", "Price (ETB)", "Stock Quantity", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        inventoryTable = new JTable(tableModel);
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryTable.setRowHeight(30);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadInventoryData() {
        try {
            List<PharmacyMedicine> inventory = pharmacyMedicineDAO.findByPharmacy(pharmacy.getId());
            tableModel.setRowCount(0);
            
            for (PharmacyMedicine pm : inventory) {
                Object[] row = {
                    pm.getMedicineName(),
                    pm.getMedicineBrand(),
                    String.format("%.2f", pm.getPrice()),
                    pm.getStockQuantity(),
                    pm.getAvailability()
                };
                tableModel.addRow(row);
            }
            
            if (inventory.isEmpty()) {
                UIHelper.showWarning(this, "No inventory data available for this pharmacy");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
