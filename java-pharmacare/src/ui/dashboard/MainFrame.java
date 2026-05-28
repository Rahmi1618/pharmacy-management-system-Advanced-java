package ui.dashboard;

import ui.auth.LoginFrame;
import ui.medicines.MedicinesPanel;
import ui.orders.OrdersPanel;
import ui.pharmacies.PharmaciesPanel;
import ui.users.UsersPanel;
import utils.SessionManager;
import utils.UIHelper;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DashboardPanel dashboardPanel;
    private MedicinesPanel medicinesPanel;
    private PharmaciesPanel pharmaciesPanel;
    private OrdersPanel ordersPanel;
    private UsersPanel usersPanel;
    
    public MainFrame() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("PharmaCare - Pharmacy Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        
        dashboardPanel = new DashboardPanel();
        medicinesPanel = new MedicinesPanel();
        pharmaciesPanel = new PharmaciesPanel();
        ordersPanel = new OrdersPanel();
        usersPanel = new UsersPanel();
        
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(medicinesPanel, "medicines");
        contentPanel.add(pharmaciesPanel, "pharmacies");
        contentPanel.add(ordersPanel, "orders");
        contentPanel.add(usersPanel, "users");
        
        add(contentPanel, BorderLayout.CENTER);
        
        showPanel("dashboard");
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(new Color(30, 41, 59));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        logoPanel.setMaximumSize(new Dimension(280, 120));
        
        JLabel logoLabel = new JLabel("PharmaCare");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel taglineLabel = new JLabel("Management System");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taglineLabel.setForeground(new Color(148, 163, 184));
        taglineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(taglineLabel);
        
        sidebar.add(logoPanel);
        
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(15, 23, 42));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        userPanel.setMaximumSize(new Dimension(280, 80));
        
        String userName = SessionManager.getInstance().getCurrentUser().getFullName();
        String userRole = SessionManager.getInstance().getCurrentUser().getRole();
        
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(userRole.substring(0, 1).toUpperCase() + userRole.substring(1));
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(148, 163, 184));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userPanel.add(userNameLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        userPanel.add(roleLabel);
        
        sidebar.add(userPanel);
        sidebar.add(createSeparator());
        
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        navLabel.setForeground(new Color(100, 116, 139));
        navLabel.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navLabel);
        
        sidebar.add(createMenuButton("Dashboard", "dashboard", true));
        sidebar.add(createMenuButton("Medicines", "medicines", false));
        sidebar.add(createMenuButton("Pharmacies", "pharmacies", false));
        sidebar.add(createMenuButton("Orders", "orders", false));
        
        if (SessionManager.getInstance().isAdmin()) {
            sidebar.add(createMenuButton("Users", "users", false));
        }
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createSeparator());
        
        JButton logoutButton = createMenuButton("Logout", null, false);
        logoutButton.setForeground(new Color(248, 113, 113));
        logoutButton.addActionListener(e -> handleLogout());
        sidebar.add(logoutButton);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String panelName, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(isActive ? Color.WHITE : new Color(148, 163, 184));
        button.setBackground(isActive ? new Color(30, 41, 59) : new Color(15, 23, 42));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(280, 48));
        button.setPreferredSize(new Dimension(280, 48));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        
        if (panelName != null) {
            button.addActionListener(e -> {
                showPanel(panelName);
                updateActiveButton(button);
            });
        }
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(30, 41, 59));
                    button.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(15, 23, 42));
                    button.setForeground(new Color(148, 163, 184));
                }
            }
        });
        
        return button;
    }
    
    private void updateActiveButton(JButton activeButton) {
        Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(new Color(15, 23, 42));
                btn.setForeground(new Color(148, 163, 184));
            }
        }
        activeButton.setBackground(new Color(30, 41, 59));
        activeButton.setForeground(Color.WHITE);
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(250, 1));
        separator.setForeground(new Color(51, 65, 85));
        separator.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return separator;
    }
    
    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        
        if ("dashboard".equals(panelName)) {
            dashboardPanel.refreshData();
        } else if ("medicines".equals(panelName)) {
            medicinesPanel.refreshData();
        } else if ("pharmacies".equals(panelName)) {
            pharmaciesPanel.refreshData();
        } else if ("orders".equals(panelName)) {
            ordersPanel.refreshData();
        } else if ("users".equals(panelName)) {
            usersPanel.refreshData();
        }
    }
    
    private void handleLogout() {
        if (UIHelper.showConfirm(this, "Are you sure you want to logout?")) {
            SessionManager.getInstance().logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
