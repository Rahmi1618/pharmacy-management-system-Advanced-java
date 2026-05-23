package pharmacy.system;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PharmacyManagementSystem extends JFrame {
    private JTabbedPane tabbedPane;
    private MedicineDAO medicineDAO;
    private SaleDAO saleDAO;
    
    // Medicine Panel Components
    private JTable medicineTable;
    private DefaultTableModel medicineTableModel;
    private JTextField txtName, txtCategory, txtManufacturer, txtPrice, txtStock, txtSearch;
    private JCheckBox chkPrescription;
    private JSpinner dateSpinner;
    
    // Sale Panel Components
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JComboBox<String> cmbMedicine, cmbPaymentMethod;
    private JTextField txtCustomerName, txtQuantity;
    private JLabel lblTotal;
    private List<SaleItem> cart;
    private BigDecimal currentTotal;
    
    // Sales History Panel Components
    private JTable salesTable;
    private DefaultTableModel salesTableModel;
    
    public PharmacyManagementSystem() {
        medicineDAO = new MedicineDAO();
        saleDAO = new SaleDAO();
        cart = new ArrayList<>();
        currentTotal = BigDecimal.ZERO;
        
        initUI();
        loadMedicines();
        loadSalesHistory();
        loadMedicineComboBox();
    }
    private void initUI() {
        setTitle("Pharmacy Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Medicine Management", createMedicinePanel());
        tabbedPane.addTab("Point of Sale", createSalePanel());
        tabbedPane.addTab("Sales History", createSalesPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createMedicinePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchMedicines());
        searchPanel.add(btnSearch);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadMedicines());
        searchPanel.add(btnRefresh);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Medicine Table
        String[] columns = {"ID", "Name", "Category", "Manufacturer", "Price", "Stock", "Expiry Date", "Prescription"};
        medicineTableModel = new DefaultTableModel(columns, 0);
        medicineTable = new JTable(medicineTableModel);
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Medicine Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        formPanel.add(txtName, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        txtCategory = new JTextField(15);
        formPanel.add(txtCategory, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Manufacturer:"), gbc);
        gbc.gridx = 1;
        txtManufacturer = new JTextField(15);
        formPanel.add(txtManufacturer, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3;
        txtPrice = new JTextField(15);
        formPanel.add(txtPrice, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(15);
        formPanel.add(txtStock, gbc);
        gbc.gridx = 2;
        gbc.gridx = 3;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prescription Required:"), gbc);
        gbc.gridx = 1;
        chkPrescription = new JCheckBox();
        formPanel.add(chkPrescription, gbc);
        
        gbc.gridx = 2;
        JButton btnAdd = new JButton("Add Medicine");
        btnAdd.addActionListener(e -> addMedicine());
        formPanel.add(btnAdd, gbc);
        
        gbc.gridx = 3;
        JButton btnUpdate = new JButton("Update Medicine");
        btnUpdate.addActionListener(e -> updateMedicine());
        formPanel.add(btnUpdate, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JButton btnDelete = new JButton("Delete Medicine");
        btnDelete.addActionListener(e -> deleteMedicine());
        formPanel.add(btnDelete, gbc);
        
        gbc.gridx = 1;
        JButton btnClear = new JButton("Clear Form");
        btnClear.addActionListener(e -> clearMedicineForm());
        formPanel.add(btnClear, gbc);
        
        panel.add(formPanel, BorderLayout.SOUTH);
        
        // Table click listener
        medicineTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = medicineTable.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(medicineTableModel.getValueAt(row, 1).toString());
                    txtCategory.setText(medicineTableModel.getValueAt(row, 2).toString());
                    txtManufacturer.setText(medicineTableModel.getValueAt(row, 3).toString());
                    txtPrice.setText(medicineTableModel.getValueAt(row, 4).toString().replace("$", ""));
                    txtStock.setText(medicineTableModel.getValueAt(row, 5).toString());
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createSalePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Add to Cart"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        selectionPanel.add(new JLabel("Medicine:"), gbc);
        gbc.gridx = 1;
        cmbMedicine = new JComboBox<>();
        cmbMedicine.setPreferredSize(new Dimension(250, 25));
        selectionPanel.add(cmbMedicine, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        selectionPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(10);
        selectionPanel.add(txtQuantity, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.addActionListener(e -> addToCart());
        selectionPanel.add(btnAddToCart, gbc);
        
        gbc.gridx = 1;
        JButton btnClearCart = new JButton("Clear Cart");
        btnClearCart.addActionListener(e -> clearCart());
        selectionPanel.add(btnClearCart, gbc);
        
        leftPanel.add(selectionPanel, BorderLayout.NORTH);
        
        String[] cartColumns = {"ID", "Medicine Name", "Quantity", "Unit Price", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        leftPanel.add(cartScroll, BorderLayout.CENTER);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Checkout"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        txtCustomerName = new JTextField(15);
        rightPanel.add(txtCustomerName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        rightPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "Insurance"});
        rightPanel.add(cmbPaymentMethod, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        rightPanel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(lblTotal, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton btnCheckout = new JButton("Complete Sale");
        btnCheckout.setBackground(new Color(50, 205, 50));
        btnCheckout.addActionListener(e -> completeSale());
        rightPanel.add(btnCheckout, gbc);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
 private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Sale ID", "Date", "Customer", "Total Amount", "Payment Method"};
        salesTableModel = new DefaultTableModel(columns, 0);
        salesTable = new JTable(salesTableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadSalesHistory());
        buttonPanel.add(btnRefresh);
        
        JButton btnRevenue = new JButton("Total Revenue");
        btnRevenue.addActionListener(e -> showTotalRevenue());
        buttonPanel.add(btnRevenue);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
     private void loadMedicines() {
        try {
            medicineTableModel.setRowCount(0);
            List<Medicine> medicines = medicineDAO.getAllMedicines();
            for (Medicine m : medicines) {
                medicineTableModel.addRow(new Object[]{
                    m.getMedicineId(), m.getName(), m.getCategory(),
                    m.getManufacturer(), "$" + m.getPrice(), m.getStockQuantity(),
                    m.getExpiryDate(), m.isPrescriptionRequired() ? "Yes" : "No"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading medicines: " + e.getMessage());
        }
    }
    
    private void loadMedicineComboBox() {
        try {
            cmbMedicine.removeAllItems();
            List<Medicine> medicines = medicineDAO.getAllMedicines();
            for (Medicine m : medicines) {
                cmbMedicine.addItem(m.getMedicineId() + " - " + m.getName() + " (Stock: " + m.getStockQuantity() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchMedicines() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadMedicines();
            return;
        }

        try {
            medicineTableModel.setRowCount(0);
            List<Medicine> medicines = medicineDAO.searchMedicines(keyword);
            for (Medicine m : medicines) {
                medicineTableModel.addRow(new Object[]{
                    m.getMedicineId(), m.getName(), m.getCategory(),
                    m.getManufacturer(), "$" + m.getPrice(), m.getStockQuantity(),
                    m.getExpiryDate(), m.isPrescriptionRequired() ? "Yes" : "No"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }
 private void addMedicine() {
        try {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Medicine name is required!");
                return;
            }
            Medicine medicine = new Medicine();
            medicine.setName(txtName.getText().trim());
            medicine.setCategory(txtCategory.getText().trim());
            medicine.setManufacturer(txtManufacturer.getText().trim());
            medicine.setPrice(new BigDecimal(txtPrice.getText().trim()));
            medicine.setStockQuantity(Integer.parseInt(txtStock.getText().trim()));
            medicine.setExpiryDate((Date) dateSpinner.getValue());
            medicine.setPrescriptionRequired(chkPrescription.isSelected());
            
            medicineDAO.addMedicine(medicine);
            JOptionPane.showMessageDialog(this, "Medicine added successfully!");
            loadMedicines();
            clearMedicineForm();
            loadMedicineComboBox();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding medicine: " + e.getMessage());
        }
    }
   private void updateMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to update");
            return;
        }
 try {
            int id = (int) medicineTableModel.getValueAt(row, 0);
            Medicine medicine = new Medicine();
            medicine.setMedicineId(id);
            medicine.setName(txtName.getText().trim());
            medicine.setCategory(txtCategory.getText().trim());
            medicine.setManufacturer(txtManufacturer.getText().trim());
            medicine.setPrice(new BigDecimal(txtPrice.getText().trim()));
            medicine.setStockQuantity(Integer.parseInt(txtStock.getText().trim()));
            medicine.setExpiryDate((Date) dateSpinner.getValue());
            medicine.setPrescriptionRequired(chkPrescription.isSelected());
            
            medicineDAO.updateMedicine(medicine);
            JOptionPane.showMessageDialog(this, "Medicine updated successfully!");
            loadMedicines();
            loadMedicineComboBox();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating medicine: " + e.getMessage());
        }
    }
private void deleteMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this medicine?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) medicineTableModel.getValueAt(row, 0);
                medicineDAO.deleteMedicine(id);
                JOptionPane.showMessageDialog(this, "Medicine deleted!");
                loadMedicines();
                loadMedicineComboBox();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting medicine: " + e.getMessage());
            }
        }
    }
 private void clearMedicineForm() {
        txtName.setText("");
        txtCategory.setText("");
        txtManufacturer.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        chkPrescription.setSelected(false);
        dateSpinner.setValue(new Date());
        txtSearch.setText("");
    }
