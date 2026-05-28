package ui.orders;

import models.Order;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;

public class OrderDetailsDialog extends JDialog {
    private Order order;
    
    public OrderDetailsDialog(Frame parent, Order order) {
        super(parent, "Order Details - #" + order.getId(), true);
        this.order = order;
        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel detailsPanel = UIHelper.createCard();
        detailsPanel.setLayout(new GridLayout(0, 2, 10, 15));
        
        addDetailRow(detailsPanel, "Order ID:", "#" + order.getId());
        addDetailRow(detailsPanel, "Customer:", order.getUserName());
        addDetailRow(detailsPanel, "Medicine:", order.getMedicineName() + " (" + order.getMedicineBrand() + ")");
        addDetailRow(detailsPanel, "Pharmacy:", order.getPharmacyName());
        addDetailRow(detailsPanel, "Quantity:", String.valueOf(order.getQuantity()));
        addDetailRow(detailsPanel, "Total Price:", String.format("%.2f ETB", order.getTotalPrice()));
        addDetailRow(detailsPanel, "Status:", order.getStatus());
        addDetailRow(detailsPanel, "Payment Method:", order.getPaymentMethod());
        addDetailRow(detailsPanel, "Delivery Address:", order.getDeliveryAddress());
        addDetailRow(detailsPanel, "Delivery Phone:", order.getDeliveryPhone());
        addDetailRow(detailsPanel, "Order Date:", order.getCreatedAt().toString());
        addDetailRow(detailsPanel, "Notes:", order.getNotes() != null ? order.getNotes() : "N/A");
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton closeButton = UIHelper.createPrimaryButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(closeButton);
        
        add(new JScrollPane(mainPanel));
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
