package ui.orders;

import dao.OrderDAO;
import models.Order;
import utils.SessionManager;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdersPanel extends JPanel {
    private OrderDAO orderDAO;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    
    public OrdersPanel() {
        this.orderDAO = new OrderDAO();
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
        
        JLabel titleLabel = new JLabel("Orders Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Track and manage customer orders");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(new Color(248, 250, 252));
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(new Color(51, 65, 85));
        
        String[] statuses = {"All", "pending", "confirmed", "delivered", "cancelled"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilter.setPreferredSize(new Dimension(150, 42));
        statusFilter.setBackground(Color.WHITE);
        statusFilter.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        statusFilter.addActionListener(e -> handleFilter());
        
        JButton createOrderButton = UIHelper.createSecondaryButton("+ Create Order");
        createOrderButton.setPreferredSize(new Dimension(150, 42));
        createOrderButton.addActionListener(e -> showCreateOrderDialog());
        
        actionPanel.add(filterLabel);
        actionPanel.add(statusFilter);
        actionPanel.add(createOrderButton);
        
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        String[] columns = {"Order ID", "Customer", "Medicine", "Pharmacy", "Qty", "Total Price", "Status", "Payment", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        orderTable = new JTable(tableModel);
        UIHelper.styleTable(orderTable);
        
        JScrollPane scrollPane = UIHelper.createStyledScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton viewButton = UIHelper.createOutlineButton("View Details");
        viewButton.setPreferredSize(new Dimension(130, 42));
        viewButton.addActionListener(e -> handleViewDetails());
        
        JButton updateStatusButton = UIHelper.createPrimaryButton("Update Status");
        updateStatusButton.setPreferredSize(new Dimension(140, 42));
        updateStatusButton.addActionListener(e -> handleUpdateStatus());
        
        JButton deleteButton = UIHelper.createDangerButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 42));
        deleteButton.addActionListener(e -> handleDelete());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        try {
            List<Order> orders;
            if (SessionManager.getInstance().isAdmin()) {
                orders = orderDAO.findAll();
            } else {
                orders = orderDAO.findByUser(SessionManager.getInstance().getCurrentUser().getId());
            }
            
            tableModel.setRowCount(0);
            
            for (Order order : orders) {
                Object[] row = {
                    order.getId(),
                    order.getUserName(),
                    order.getMedicineName() + " (" + order.getMedicineBrand() + ")",
                    order.getPharmacyName(),
                    order.getQuantity(),
                    String.format("%.2f ETB", order.getTotalPrice()),
                    order.getStatus(),
                    order.getPaymentMethod(),
                    order.getCreatedAt()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load orders: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleFilter() {
        String status = (String) statusFilter.getSelectedItem();
        
        try {
            List<Order> orders;
            if ("All".equals(status)) {
                if (SessionManager.getInstance().isAdmin()) {
                    orders = orderDAO.findAll();
                } else {
                    orders = orderDAO.findByUser(SessionManager.getInstance().getCurrentUser().getId());
                }
            } else {
                orders = orderDAO.findByStatus(status);
                if (!SessionManager.getInstance().isAdmin()) {
                    int userId = SessionManager.getInstance().getCurrentUser().getId();
                    orders.removeIf(order -> order.getUserId() != userId);
                }
            }
            
            tableModel.setRowCount(0);
            
            for (Order order : orders) {
                Object[] row = {
                    order.getId(),
                    order.getUserName(),
                    order.getMedicineName() + " (" + order.getMedicineBrand() + ")",
                    order.getPharmacyName(),
                    order.getQuantity(),
                    String.format("%.2f ETB", order.getTotalPrice()),
                    order.getStatus(),
                    order.getPaymentMethod(),
                    order.getCreatedAt()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Filter failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showCreateOrderDialog() {
        CreateOrderDialog dialog = new CreateOrderDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        refreshData();
    }
    
    private void handleViewDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select an order to view");
            return;
        }
        
        try {
            int orderId = (int) tableModel.getValueAt(selectedRow, 0);
            Order order = orderDAO.findById(orderId);
            
            OrderDetailsDialog dialog = new OrderDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), order);
            dialog.setVisible(true);
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to view order: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleUpdateStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select an order to update");
            return;
        }
        
        String[] statuses = {"pending", "confirmed", "delivered", "cancelled"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Order Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuses,
            statuses[0]
        );
        
        if (newStatus != null) {
            try {
                int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                if (orderDAO.updateStatus(orderId, newStatus)) {
                    UIHelper.showSuccess(this, "Order status updated successfully");
                    refreshData();
                } else {
                    UIHelper.showError(this, "Failed to update order status");
                }
            } catch (Exception e) {
                UIHelper.showError(this, "Update failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void handleDelete() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select an order to delete");
            return;
        }
        
        if (!UIHelper.showConfirm(this, "Are you sure you want to delete this order?")) {
            return;
        }
        
        try {
            int orderId = (int) tableModel.getValueAt(selectedRow, 0);
            if (orderDAO.delete(orderId)) {
                UIHelper.showSuccess(this, "Order deleted successfully");
                refreshData();
            } else {
                UIHelper.showError(this, "Failed to delete order");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Delete failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
