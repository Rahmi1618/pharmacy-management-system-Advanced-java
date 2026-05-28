package ui.users;

import dao.UserDAO;
import models.User;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsersPanel extends JPanel {
    private UserDAO userDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    public UsersPanel() {
        this.userDAO = new UserDAO();
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
        
        JLabel titleLabel = new JLabel("Users Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage system users and roles");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        String[] columns = {"ID", "Username", "First Name", "Last Name", "Email", "Phone", "Role", "Registered"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        UIHelper.styleTable(userTable);
        
        JScrollPane scrollPane = UIHelper.createStyledScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton viewButton = UIHelper.createOutlineButton("View Details");
        viewButton.setPreferredSize(new Dimension(130, 42));
        viewButton.addActionListener(e -> handleViewDetails());
        
        JButton deleteButton = UIHelper.createDangerButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 42));
        deleteButton.addActionListener(e -> handleDelete());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        try {
            List<User> users = userDAO.findAll();
            tableModel.setRowCount(0);
            
            for (User user : users) {
                Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.getCreatedAt()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to load users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleViewDetails() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a user to view");
            return;
        }
        
        try {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userDAO.findById(userId);
            
            UserDetailsDialog dialog = new UserDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), user);
            dialog.setVisible(true);
        } catch (Exception e) {
            UIHelper.showError(this, "Failed to view user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleDelete() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            UIHelper.showWarning(this, "Please select a user to delete");
            return;
        }
        
        if (!UIHelper.showConfirm(this, "Are you sure you want to delete this user?")) {
            return;
        }
        
        try {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            if (userDAO.delete(userId)) {
                UIHelper.showSuccess(this, "User deleted successfully");
                refreshData();
            } else {
                UIHelper.showError(this, "Failed to delete user");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Delete failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
