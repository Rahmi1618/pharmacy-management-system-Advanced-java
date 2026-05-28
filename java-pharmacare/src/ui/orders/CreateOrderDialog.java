package ui.orders;

import dao.*;
import models.*;
import utils.SessionManager;
import utils.UIHelper;
import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateOrderDialog extends JDialog {
    private OrderDAO orderDAO;
    private MedicineDAO medicineDAO;
    private PharmacyDAO pharmacyDAO;
    private PharmacyMedicineDAO pharmacyMedicineDAO;
    
    private JComboBox<String> medicineCombo;
    private JComboBox<String> pharmacyCombo;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextArea deliveryAddressArea;
    private JTextField deliveryPhoneField;
    private JComboBox<String> paymentMethodCombo;
    private JTextArea notesArea;
    
    private List<Medicine> medicines;
    private List<Pharmacy> pharmacies;
    
    public CreateOrderDialog(Frame parent) {
        super(parent, "Create New Order", true);
        this.orderDAO = new OrderDAO();
        this.medicineDAO = new MedicineDAO();
        this.pharmacyDAO = new PharmacyDAO();
        this.pharmacyMedicineDAO = new PharmacyMedicineDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setSize(550, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Create New Order");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        medicineCombo = addComboField(mainPanel, "Select Medicine");
        medicineCombo.addActionListener(e -> updatePharmacies());
        
        pharmacyCombo = addComboField(mainPanel, "Select Pharmacy");
        pharmacyCombo.addActionListener(e -> updatePrice());
        
        quantityField = addFormField(mainPanel, "Quantity");
        quantityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateTotalPrice();
            }
        });
        
        priceField = addFormField(mainPanel, "Total Price (ETB)");
        priceField.setEditable(false);
        
        JLabel addressLabel = new JLabel("Delivery Address");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        deliveryAddressArea = new JTextArea(3, 20);
        deliveryAddressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deliveryAddressArea.setLineWrap(true);
        deliveryAddressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(deliveryAddressArea);
        addressScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        mainPanel.add(addressLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(addressScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        deliveryPhoneField = addFormField(mainPanel, "Delivery Phone");
        
        String[] paymentMethods = {"telebirr", "cash", "bank_transfer"};
        paymentMethodCombo = addComboField(mainPanel, "Payment Method");
        for (String method : paymentMethods) {
            paymentMethodCombo.addItem(method);
        }
        
        JLabel notesLabel = new JLabel("Notes (Optional)");
        notesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        notesArea = new JTextArea(2, 20);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        mainPanel.add(notesLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(notesScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        
        JButton createButton = UIHelper.createPrimaryButton("Create Order");
        createButton.addActionListener(e -> handleCreateOrder());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        add(new JScrollPane(mainPanel));
    }
    
    private JTextField addFormField(JPanel panel, String label) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField field = UIHelper.createTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        panel.add(fieldLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return field;
    }
    
    private JComboBox<String> addComboField(JPanel panel, String label) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(250, 35));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        panel.add(fieldLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(combo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return combo;
    }
    
    private void loadData() {
        try {
            medicines = medicineDAO.findAll();
            for (Medicine medicine : medicines) {
                medicineCombo.addItem(medicine.getName() + " - " + medicine.getBrand());
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updatePharmacies() {
        int selectedIndex = medicineCombo.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < medicines.size()) {
            Medicine medicine = medicines.get(selectedIndex);
            
            try {
                List<PharmacyMedicine> stocks = pharmacyMedicineDAO.findByMedicine(medicine.getId());
                pharmacyCombo.removeAllItems();
                
                for (PharmacyMedicine pm : stocks) {
                    if (!"Out of Stock".equals(pm.getAvailability())) {
                        pharmacyCombo.addItem(pm.getPharmacyName() + " - " + pm.getPrice() + " ETB");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void updatePrice() {
        updateTotalPrice();
    }
    
    private void updateTotalPrice() {
        try {
            int medicineIndex = medicineCombo.getSelectedIndex();
            int pharmacyIndex = pharmacyCombo.getSelectedIndex();
            String quantityStr = quantityField.getText().trim();
            
            if (medicineIndex >= 0 && pharmacyIndex >= 0 && ValidationUtil.isPositiveInteger(quantityStr)) {
                Medicine medicine = medicines.get(medicineIndex);
                List<PharmacyMedicine> stocks = pharmacyMedicineDAO.findByMedicine(medicine.getId());
                
                if (pharmacyIndex < stocks.size()) {
                    PharmacyMedicine pm = stocks.get(pharmacyIndex);
                    int quantity = Integer.parseInt(quantityStr);
                    BigDecimal total = pm.getPrice().multiply(BigDecimal.valueOf(quantity));
                    priceField.setText(String.format("%.2f", total));
                }
            }
        } catch (Exception e) {
            priceField.setText("0.00");
        }
    }
    
    private void handleCreateOrder() {
        int medicineIndex = medicineCombo.getSelectedIndex();
        int pharmacyIndex = pharmacyCombo.getSelectedIndex();
        String quantityStr = quantityField.getText().trim();
        String deliveryAddress = deliveryAddressArea.getText().trim();
        String deliveryPhone = deliveryPhoneField.getText().trim();
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        String notes = notesArea.getText().trim();
        
        if (medicineIndex < 0) {
            UIHelper.showError(this, "Please select a medicine");
            return;
        }
        
        if (pharmacyIndex < 0) {
            UIHelper.showError(this, "Please select a pharmacy");
            return;
        }
        
        if (!ValidationUtil.isPositiveInteger(quantityStr)) {
            UIHelper.showError(this, "Please enter a valid quantity");
            return;
        }
        
        try {
            Medicine medicine = medicines.get(medicineIndex);
            List<PharmacyMedicine> stocks = pharmacyMedicineDAO.findByMedicine(medicine.getId());
            PharmacyMedicine pm = stocks.get(pharmacyIndex);
            
            int quantity = Integer.parseInt(quantityStr);
            BigDecimal totalPrice = pm.getPrice().multiply(BigDecimal.valueOf(quantity));
            
            Order order = new Order(
                SessionManager.getInstance().getCurrentUser().getId(),
                pm.getPharmacyId(),
                medicine.getId(),
                quantity,
                totalPrice
            );
            
            order.setDeliveryAddress(deliveryAddress);
            order.setDeliveryPhone(deliveryPhone);
            order.setPaymentMethod(paymentMethod);
            order.setNotes(notes);
            
            Order created = orderDAO.create(order);
            if (created != null) {
                pharmacyMedicineDAO.updateStock(pm.getPharmacyId(), medicine.getId(), quantity);
                UIHelper.showSuccess(this, "Order created successfully!");
                dispose();
            } else {
                UIHelper.showError(this, "Failed to create order");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Order creation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
