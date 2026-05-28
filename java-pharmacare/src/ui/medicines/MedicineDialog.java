package ui.medicines;

import dao.MedicineDAO;
import models.Medicine;
import utils.UIHelper;
import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class MedicineDialog extends JDialog {
    private Medicine medicine;
    private MedicineDAO medicineDAO;
    
    private JTextField nameField;
    private JTextField brandField;
    private JComboBox<String> categoryCombo;
    private JTextArea descriptionArea;
    private JCheckBox prescriptionCheckBox;
    private JTextField expiryDateField;
    
    public MedicineDialog(Frame parent, Medicine medicine) {
        super(parent, medicine == null ? "Add Medicine" : "Edit Medicine", true);
        this.medicine = medicine;
        this.medicineDAO = new MedicineDAO();
        initComponents();
        
        if (medicine != null) {
            loadMedicineData();
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
        
        nameField = addFormField(mainPanel, "Medicine Name");
        brandField = addFormField(mainPanel, "Brand");
        
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] categories = {"Pain Relief", "Antibiotic", "Allergy", "Digestive", "Heart Health", "Diabetes", "Vitamins"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setPreferredSize(new Dimension(250, 35));
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        mainPanel.add(categoryLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(categoryCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        mainPanel.add(descLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(descScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        prescriptionCheckBox = new JCheckBox("Requires Prescription");
        prescriptionCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        prescriptionCheckBox.setBackground(UIHelper.BACKGROUND_COLOR);
        
        mainPanel.add(prescriptionCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        expiryDateField = addFormField(mainPanel, "Expiry Date (YYYY-MM-DD)");
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
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
    
    private void loadMedicineData() {
        nameField.setText(medicine.getName());
        brandField.setText(medicine.getBrand());
        categoryCombo.setSelectedItem(medicine.getCategory());
        descriptionArea.setText(medicine.getDescription());
        prescriptionCheckBox.setSelected(medicine.isRequiresPrescription());
        if (medicine.getExpiryDate() != null) {
            expiryDateField.setText(medicine.getExpiryDate().toString());
        }
    }
    
    private void handleSave() {
        String name = nameField.getText().trim();
        String brand = brandField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String description = descriptionArea.getText().trim();
        boolean requiresPrescription = prescriptionCheckBox.isSelected();
        String expiryDateStr = expiryDateField.getText().trim();
        
        if (!ValidationUtil.isNotEmpty(name)) {
            UIHelper.showError(this, "Medicine name is required");
            return;
        }
        
        try {
            Date expiryDate = null;
            if (ValidationUtil.isNotEmpty(expiryDateStr)) {
                expiryDate = Date.valueOf(expiryDateStr);
            }
            
            if (medicine == null) {
                medicine = new Medicine(name, brand, category, description, requiresPrescription);
                medicine.setExpiryDate(expiryDate);
                
                Medicine created = medicineDAO.create(medicine);
                if (created != null) {
                    UIHelper.showSuccess(this, "Medicine added successfully");
                    dispose();
                } else {
                    UIHelper.showError(this, "Failed to add medicine");
                }
            } else {
                medicine.setName(name);
                medicine.setBrand(brand);
                medicine.setCategory(category);
                medicine.setDescription(description);
                medicine.setRequiresPrescription(requiresPrescription);
                medicine.setExpiryDate(expiryDate);
                
                if (medicineDAO.update(medicine)) {
                    UIHelper.showSuccess(this, "Medicine updated successfully");
                    dispose();
                } else {
                    UIHelper.showError(this, "Failed to update medicine");
                }
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Save failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
