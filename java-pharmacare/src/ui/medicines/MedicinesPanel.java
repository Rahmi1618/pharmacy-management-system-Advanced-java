package ui.medicines;

import dao.MedicineDAO;
import models.Medicine;
import utils.SessionManager;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicinesPanel extends JPanel {
    private MedicineDAO medicineDAO;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public MedicinesPanel() {
        this.medicineDAO = new MedicineDAO();
        initComponents();
        refreshData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(248, 250, 252));
        
        JLabel titleLabel = new JLabel("Medicines Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage your medicine inventory and catalog");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(new Color(248, 250, 252));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(280, 42));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JButton searchButton = UIHelper.createPrimaryButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 42));
        searchButton.addActionListener(e -> handleSearch());
        
        JButton addButton = UIHelper.createSecondaryButton("+ Add Medicine");
        addButton.setPreferredSize(new Dimension(150, 42));
        addButton.addActionListener(e -> showAddDialog());
        
        actionPanel.add(searchField);
        actionPanel.add(searchButton);
        actionPanel.add(addButton);
        
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        String[] columns = {"ID", "Medicine Name", "Brand", "Category", "Description", "Prescription", "Expiry Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        medicineTable = new JTable(tableModel);
        UIHelper.styleTable(medicineTable);
        
        JScrollPane scrollPane = UIHelper.createStyledScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton viewStockButton = UIHelper.createOutlineButton("View Stock");
        viewStockButton.setPreferredSize(new Dimension(130, 42));
        viewStockButton.addActionListener(e -> handleViewStock());
        
        JButton editButton = UIHelper.createPrimaryButton("Edit");
        editButton.setPreferredSize(new Dimension(100, 42));
        editButton.addActionListener(e -> handleEdit());
        
        JButton deleteButton = UIHelper.createDangerButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 42));
        deleteButton.addActionListener(e -> handleDelete());
        
        buttonPanel.add(viewStockButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        try {
            List<Medicine> medicines = medicineDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Medicine medicine : medicines) {
                Object[] row = {
                    medicine.getId(),
                    medicine.getName(),
                    medicine.getBrand(),
                    medicine.getCategory(),
                    medicine.getDescription(),
                    medicine.isRequiresPrescription() ? "Yes" : "No",
                    medicine.getExpiryDate()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshData();
            return;
        }
        
        try {
            List<Medicine> medicines = medicineDAO.search(query);
            tableModel.setRowCount(0);
            
            for (Medicine medicine : medicines) {
                Object[] row = {
                    medicine.getId(),
                    medicine.getName(),
                    medicine.getBrand(),
                    medicine.getCategory(),
                    medicine.getDescription(),
                    medicine.isRequiresPrescription() ? "Yes" : "No",
                    medicine.getExpiryDate()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Search failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAddDialog() {
        MedicineDialog dialog = new MedicineDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        refreshData();
    }
    
    private void handleEdit() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a medicine to edit");
            return;
        }
        
        try {
            int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
            Medicine medicine = medicineDAO.findById(medicineId);
            
            MedicineDialog dialog = new MedicineDialog((Frame) SwingUtilities.getWindowAncestor(this), medicine);
            dialog.setVisible(true);
            refreshData();
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to edit medicine: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleDelete() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a medicine to delete");
            return;
        }
        
        if (!UIHelper.showConfirm(this, "Are you sure you want to delete this medicine?")) {
            return;
        }
        
        try {
            int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
            if (medicineDAO.delete(medicineId)) {
                UIHelper.showSuccess(this, "Medicine deleted successfully");
                refreshData();
            } else {
                UIHelper.showError(this, "Failed to delete medicine");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Delete failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleViewStock() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a medicine to view stock");
            return;
        }
        
        try {
            int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
            Medicine medicine = medicineDAO.findById(medicineId);
            
            StockDialog dialog = new StockDialog((Frame) SwingUtilities.getWindowAncestor(this), medicine);
            dialog.setVisible(true);
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to view stock: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
