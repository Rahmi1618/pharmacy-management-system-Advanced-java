package ui.pharmacies;

import dao.PharmacyDAO;
import models.Pharmacy;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PharmaciesPanel extends JPanel {
    private PharmacyDAO pharmacyDAO;
    private JTable pharmacyTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public PharmaciesPanel() {
        this.pharmacyDAO = new PharmacyDAO();
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
        
        JLabel titleLabel = new JLabel("Pharmacies Network");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage pharmacy locations and network");
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
        
        JButton addButton = UIHelper.createSecondaryButton("+ Add Pharmacy");
        addButton.setPreferredSize(new Dimension(160, 42));
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
        
        String[] columns = {"ID", "Pharmacy Name", "Address", "Phone", "Email", "Rating", "24/7 Service"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pharmacyTable = new JTable(tableModel);
        UIHelper.styleTable(pharmacyTable);
        
        JScrollPane scrollPane = UIHelper.createStyledScrollPane(pharmacyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton viewInventoryButton = UIHelper.createOutlineButton("View Inventory");
        viewInventoryButton.setPreferredSize(new Dimension(150, 42));
        viewInventoryButton.addActionListener(e -> handleViewInventory());
        
        JButton editButton = UIHelper.createPrimaryButton("Edit");
        editButton.setPreferredSize(new Dimension(100, 42));
        editButton.addActionListener(e -> handleEdit());
        
        JButton deleteButton = UIHelper.createDangerButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 42));
        deleteButton.addActionListener(e -> handleDelete());
        
        buttonPanel.add(viewInventoryButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        try {
            List<Pharmacy> pharmacies = pharmacyDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Pharmacy pharmacy : pharmacies) {
                Object[] row = {
                    pharmacy.getId(),
                    pharmacy.getName(),
                    pharmacy.getAddress(),
                    pharmacy.getPhone(),
                    pharmacy.getEmail(),
                    pharmacy.getRating(),
                    pharmacy.isIs24Hours() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load pharmacies: " + e.getMessage());
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
            List<Pharmacy> pharmacies = pharmacyDAO.search(query);
            tableModel.setRowCount(0);
            
            for (Pharmacy pharmacy : pharmacies) {
                Object[] row = {
                    pharmacy.getId(),
                    pharmacy.getName(),
                    pharmacy.getAddress(),
                    pharmacy.getPhone(),
                    pharmacy.getEmail(),
                    pharmacy.getRating(),
                    pharmacy.isIs24Hours() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Search failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAddDialog() {
        PharmacyDialog dialog = new PharmacyDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        refreshData();
    }
    
    private void handleEdit() {
        int selectedRow = pharmacyTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a pharmacy to edit");
            return;
        }
        
        try {
            int pharmacyId = (int) tableModel.getValueAt(selectedRow, 0);
            Pharmacy pharmacy = pharmacyDAO.findById(pharmacyId);
            
            PharmacyDialog dialog = new PharmacyDialog((Frame) SwingUtilities.getWindowAncestor(this), pharmacy);
            dialog.setVisible(true);
            refreshData();
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to edit pharmacy: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleDelete() {
        int selectedRow = pharmacyTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a pharmacy to delete");
            return;
        }
        
        if (!UIHelper.showConfirm(this, "Are you sure you want to delete this pharmacy?")) {
            return;
        }
        
        try {
            int pharmacyId = (int) tableModel.getValueAt(selectedRow, 0);
            if (pharmacyDAO.delete(pharmacyId)) {
                UIHelper.showSuccess(this, "Pharmacy deleted successfully");
                refreshData();
            } else {
                UIHelper.showError(this, "Failed to delete pharmacy");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Delete failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleViewInventory() {
        int selectedRow = pharmacyTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a pharmacy to view inventory");
            return;
        }
        
        try {
            int pharmacyId = (int) tableModel.getValueAt(selectedRow, 0);
            Pharmacy pharmacy = pharmacyDAO.findById(pharmacyId);
            
            InventoryDialog dialog = new InventoryDialog((Frame) SwingUtilities.getWindowAncestor(this), pharmacy);
            dialog.setVisible(true);
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to view inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
