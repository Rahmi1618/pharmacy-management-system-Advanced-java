package ui.users;

import models.User;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;

public class UserDetailsDialog extends JDialog {
    private User user;
    
    public UserDetailsDialog(Frame parent, User user) {
        super(parent, "User Details - " + user.getUsername(), true);
        this.user = user;
        initComponents();
    }
    
    private void initComponents() {
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("User Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel detailsPanel = UIHelper.createCard();
        detailsPanel.setLayout(new GridLayout(0, 2, 10, 15));
        
        addDetailRow(detailsPanel, "User ID:", "#" + user.getId());
        addDetailRow(detailsPanel, "Username:", user.getUsername());
        addDetailRow(detailsPanel, "Full Name:", user.getFullName());
        addDetailRow(detailsPanel, "Email:", user.getEmail());
        addDetailRow(detailsPanel, "Phone:", user.getPhone() != null ? user.getPhone() : "N/A");
        addDetailRow(detailsPanel, "Address:", user.getAddress() != null ? user.getAddress() : "N/A");
        addDetailRow(detailsPanel, "Role:", user.getRole());
        addDetailRow(detailsPanel, "Created At:", user.getCreatedAt().toString());
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton closeButton = UIHelper.createPrimaryButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(closeButton);
        
        add(mainPanel);
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(UIHelper.TEXT_SECONDARY);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(UIHelper.TEXT_PRIMARY);
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
}
