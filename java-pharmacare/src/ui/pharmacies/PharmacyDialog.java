package ui.pharmacies;

import dao.PharmacyDAO;
import models.Pharmacy;
import utils.UIHelper;
import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class PharmacyDialog extends JDialog {
    private Pharmacy pharmacy;
    private PharmacyDAO pharmacyDAO;
    
    private JTextField nameField;
    private JTextArea addressArea;
    private JTextField phoneField;
    private JTextField emailField;
    private JCheckBox is24HoursCheckBox;
    private JTextArea descriptionArea;
    
    public PharmacyDialog(Frame parent, Pharmacy pharmacy) {
        super(parent, pharmacy == null ? "Add Pharmacy" : "Edit Pharmacy", true);
        this.pharmacy = pharmacy;
        this.pharmacyDAO = new PharmacyDAO();
        initComponents();
        
        if (pharmacy != null) {
            loadPharmacyData();
        }
    }
    
    private void initComponents() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        nameField = addFormField(mainPanel, "Pharmacy Name");
        
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        addressArea = new JTextArea(3, 20);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        mainPanel.add(addressLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(addressScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        phoneField = addFormField(mainPanel, "Phone");
        emailField = addFormField(mainPanel, "Email");
        
        is24HoursCheckBox = new JCheckBox("Open 24 Hours");
        is24HoursCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        is24HoursCheckBox.setBackground(UIHelper.BACKGROUND_COLOR);
        
        mainPanel.add(is24HoursCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        mainPanel.add(descLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(descScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        
        JButton saveButton = UIHelper.createPrimaryButton("Save");
        saveButton.addActionListener(e -> handleSave());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
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
    
    private void loadPharmacyData() {
        nameField.setText(pharmacy.getName());
        addressArea.setText(pharmacy.getAddress());
        phoneField.setText(pharmacy.getPhone());
        emailField.setText(pharmacy.getEmail());
        is24HoursCheckBox.setSelected(pharmacy.isIs24Hours());
        descriptionArea.setText(pharmacy.getDescription());
    }
    
    private void handleSave() {
        String name = nameField.getText().trim();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        boolean is24Hours = is24HoursCheckBox.isSelected();
        String description = descriptionArea.getText().trim();
        
        if (!ValidationUtil.isNotEmpty(name)) {
            UIHelper.showError(this, "Pharmacy name is required");
            return;
        }
        
        if (!ValidationUtil.isNotEmpty(address)) {
            UIHelper.showError(this, "Address is required");
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            UIHelper.showError(this, "Invalid email address");
            return;
        }
        
        try {
            if (pharmacy == null) {
                pharmacy = new Pharmacy(name, address, phone, email);
                pharmacy.setIs24Hours(is24Hours);
                pharmacy.setDescription(description);
                
                Pharmacy created = pharmacyDAO.create(pharmacy);
                if (created != null) {
                    UIHelper.showSuccess(this, "Pharmacy added successfully");
                    dispose();
                } else {
                    UIHelper.showError(this, "Failed to add pharmacy");
                }
            } else {
                pharmacy.setName(name);
                pharmacy.setAddress(address);
                pharmacy.setPhone(phone);
                pharmacy.setEmail(email);
                pharmacy.setIs24Hours(is24Hours);
                pharmacy.setDescription(description);
                
                if (pharmacyDAO.update(pharmacy)) {
                    UIHelper.showSuccess(this, "Pharmacy updated successfully");
                    dispose();
                } else {
                    UIHelper.showError(this, "Failed to update pharmacy");
                }
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Save failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
