package ui.dashboard;

import dao.MedicineDAO;
import dao.OrderDAO;
import dao.PharmacyDAO;
import dao.UserDAO;
import utils.SessionManager;
import utils.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends JPanel {
    private MedicineDAO medicineDAO;
    private PharmacyDAO pharmacyDAO;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    
    private JLabel medicineCountLabel;
    private JLabel pharmacyCountLabel;
    private JLabel orderCountLabel;
    private JLabel userCountLabel;
    private JLabel pendingOrdersLabel;
    private JLabel completedOrdersLabel;
    
    public DashboardPanel() {
        this.medicineDAO = new MedicineDAO();
        this.pharmacyDAO = new PharmacyDAO();
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
        
        initComponents();
        refreshData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(248, 250, 252));
        mainContainer.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 25));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.CENTER);
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(248, 250, 252));
        
        String userName = SessionManager.getInstance().getCurrentUser().getFirstName();
        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(15, 23, 42));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        JLabel dateLabel = new JLabel(dateFormat.format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(100, 116, 139));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanel.add(welcomeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(dateLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBackground(new Color(248, 250, 252));
        
        medicineCountLabel = new JLabel("0");
        pharmacyCountLabel = new JLabel("0");
        orderCountLabel = new JLabel("0");
        userCountLabel = new JLabel("0");
        pendingOrdersLabel = new JLabel("0");
        completedOrdersLabel = new JLabel("0");
        
        statsPanel.add(createModernStatCard("Total Medicines", medicineCountLabel, 
            new Color(59, 130, 246), new Color(219, 234, 254), "MEDICINES"));
        statsPanel.add(createModernStatCard("Total Pharmacies", pharmacyCountLabel, 
            new Color(16, 185, 129), new Color(209, 250, 229), "PHARMACIES"));
        statsPanel.add(createModernStatCard("Total Orders", orderCountLabel, 
            new Color(139, 92, 246), new Color(237, 233, 254), "ORDERS"));
        statsPanel.add(createModernStatCard("Registered Users", userCountLabel, 
            new Color(236, 72, 153), new Color(252, 231, 243), "USERS"));
        statsPanel.add(createModernStatCard("Pending Orders", pendingOrdersLabel, 
            new Color(245, 158, 11), new Color(254, 243, 199), "PENDING"));
        statsPanel.add(createModernStatCard("Completed Orders", completedOrdersLabel, 
            new Color(34, 197, 94), new Color(220, 252, 231), "COMPLETED"));
        
        return statsPanel;
    }
    
    private JPanel createModernStatCard(String title, JLabel valueLabel, Color accentColor, Color bgColor, String badge) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(249, 250, 251));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(24, 24, 24, 24)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    new EmptyBorder(25, 25, 25, 25)
                ));
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel badgeLabel = new JLabel(badge);
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badgeLabel.setForeground(accentColor);
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(bgColor);
        badgeLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        topPanel.add(badgeLabel, BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(new Color(15, 23, 42));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleLabel.setForeground(new Color(100, 116, 139));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        centerPanel.add(valueLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        centerPanel.add(titleLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        bottomPanel.setBackground(new Color(248, 250, 252));
        
        JPanel quickActionsCard = createQuickActionsCard();
        JPanel recentActivityCard = createRecentActivityCard();
        JPanel systemInfoCard = createSystemInfoCard();
        
        bottomPanel.add(quickActionsCard);
        bottomPanel.add(recentActivityCard);
        bottomPanel.add(systemInfoCard);
        
        return bottomPanel;
    }
    
    private JPanel createRecentActivityCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BoxLayout(activityPanel, BoxLayout.Y_AXIS));
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        activityPanel.add(createActivityItem("New order placed", "2 minutes ago", new Color(59, 130, 246)));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        activityPanel.add(createActivityItem("Medicine stock updated", "15 minutes ago", new Color(16, 185, 129)));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        activityPanel.add(createActivityItem("New user registered", "1 hour ago", new Color(139, 92, 246)));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        activityPanel.add(createActivityItem("Order delivered", "2 hours ago", new Color(34, 197, 94)));
        activityPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        activityPanel.add(createActivityItem("Pharmacy added", "3 hours ago", new Color(236, 72, 153)));
        activityPanel.add(Box.createVerticalGlue());
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(activityPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createActivityItem(String title, String time, Color color) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(0, 0, 10, 10);
            }
        };
        dotPanel.setPreferredSize(new Dimension(10, 10));
        dotPanel.setBackground(Color.WHITE);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(new Color(100, 116, 139));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(timeLabel);
        
        item.add(dotPanel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(248, 250, 252));
                textPanel.setBackground(new Color(248, 250, 252));
                dotPanel.setBackground(new Color(248, 250, 252));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
                textPanel.setBackground(Color.WHITE);
                dotPanel.setBackground(Color.WHITE);
            }
        });
        
        return item;
    }
    
    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        actionsPanel.add(createActionItem("View All Medicines", "Browse medicine inventory", new Color(59, 130, 246)));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        actionsPanel.add(createActionItem("Manage Pharmacies", "Add or edit pharmacy locations", new Color(16, 185, 129)));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        actionsPanel.add(createActionItem("Process Orders", "View and manage customer orders", new Color(139, 92, 246)));
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        actionsPanel.add(createActionItem("User Management", "Manage system users and roles", new Color(236, 72, 153)));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(actionsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createActionItem(String title, String description, Color color) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(4, 40));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 116, 139));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(descLabel);
        
        item.add(colorBar, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(248, 250, 252));
                textPanel.setBackground(new Color(248, 250, 252));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
                textPanel.setBackground(Color.WHITE);
            }
        });
        
        return item;
    }
    
    private JPanel createSystemInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 23, 42));
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        infoPanel.add(createInfoRow("System Status", "Online", new Color(34, 197, 94)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(createInfoRow("Database", "Connected", new Color(34, 197, 94)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(createInfoRow("Version", "1.0.0", new Color(59, 130, 246)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(createInfoRow("Last Backup", "Today", new Color(139, 92, 246)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        infoPanel.add(createProgressBar("Storage Used", 65, new Color(59, 130, 246)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(createProgressBar("Memory Usage", 42, new Color(16, 185, 129)));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(createProgressBar("CPU Load", 28, new Color(245, 158, 11)));
        
        infoPanel.add(Box.createVerticalGlue());
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 250, 252));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
            new EmptyBorder(20, 0, 0, 0)
        ));
        
        JLabel footerLabel = new JLabel("PharmaCare Management System");
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        footerLabel.setForeground(new Color(100, 116, 139));
        
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createProgressBar(String label, int percentage, Color color) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(100, 116, 139));
        
        JLabel percentLabel = new JLabel(percentage + "%");
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        percentLabel.setForeground(color);
        
        labelPanel.add(nameLabel, BorderLayout.WEST);
        labelPanel.add(percentLabel, BorderLayout.EAST);
        
        JPanel progressBarBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(226, 232, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                int fillWidth = (int) (getWidth() * percentage / 100.0);
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, fillWidth, getHeight(), 10, 10);
            }
        };
        progressBarBg.setPreferredSize(new Dimension(200, 8));
        progressBarBg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBarBg.setBackground(Color.WHITE);
        
        container.add(labelPanel);
        container.add(Box.createRigidArea(new Dimension(0, 8)));
        container.add(progressBarBg);
        
        return container;
    }
    
    private JPanel createInfoRow(String label, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(new Color(100, 116, 139));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(valueColor);
        
        row.add(labelLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        
        return row;
    }
    
    public void refreshData() {
        try {
            int medicineCount = medicineDAO.getTotalCount();
            int pharmacyCount = pharmacyDAO.getTotalCount();
            int orderCount = orderDAO.getTotalCount();
            int userCount = userDAO.findAll().size();
            int pendingOrders = orderDAO.getCountByStatus("pending");
            int completedOrders = orderDAO.getCountByStatus("delivered");
            
            medicineCountLabel.setText(String.valueOf(medicineCount));
            pharmacyCountLabel.setText(String.valueOf(pharmacyCount));
            orderCountLabel.setText(String.valueOf(orderCount));
            userCountLabel.setText(String.valueOf(userCount));
            pendingOrdersLabel.setText(String.valueOf(pendingOrders));
            completedOrdersLabel.setText(String.valueOf(completedOrders));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
