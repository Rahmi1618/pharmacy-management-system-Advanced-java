package ui.auth;

import dao.UserDAO;
import models.User;
import ui.dashboard.MainFrame;
import utils.SessionManager;
import utils.UIHelper;
import utils.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public LoginFrame() {
        this.userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("PharmaCare - Login");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.setBackground(Color.WHITE);
        
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();
        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gp = new GradientPaint(0, 0, new Color(59, 130, 246), w, h, new Color(139, 92, 246));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(80, 60, 80, 60));
        
        JLabel iconLabel = new JLabel("⚕");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 120));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("PharmaCare");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Pharmacy Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(subtitleLabel);
        leftPanel.add(Box.createVerticalGlue());
        
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        
        featuresPanel.add(createFeatureItem("✓ Manage Medicines & Inventory"));
        featuresPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        featuresPanel.add(createFeatureItem("✓ Track Orders & Deliveries"));
        featuresPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        featuresPanel.add(createFeatureItem("✓ Pharmacy Network Management"));
        featuresPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        featuresPanel.add(createFeatureItem("✓ Real-time Analytics"));
        
        leftPanel.add(featuresPanel);
        leftPanel.add(Box.createVerticalGlue());
        
        return leftPanel;
    }
    
    private JLabel createFeatureItem(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(255, 255, 255, 230));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(60, 70, 60, 70));
        
        rightPanel.add(Box.createVerticalGlue());
        
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(15, 23, 42));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("Sign in to continue to PharmaCare");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(100, 116, 139));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(descLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        JLabel usernameLabel = new JLabel("Username or Email");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(51, 65, 85));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = createStyledTextField();
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(usernameLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(51, 65, 85));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = createStyledPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(passwordLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        
        JButton loginButton = createGradientButton("Sign In");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        
        rightPanel.add(loginButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel noAccountLabel = new JLabel("Don't have an account? ");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noAccountLabel.setForeground(new Color(100, 116, 139));
        
        JButton registerLink = new JButton("Create Account");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerLink.setForeground(new Color(59, 130, 246));
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setFocusPainted(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(e -> showRegisterDialog());
        
        registerPanel.add(noAccountLabel);
        registerPanel.add(registerLink);
        
        rightPanel.add(registerPanel);
        rightPanel.add(Box.createVerticalGlue());
        
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(148, 163, 184));
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(versionLabel);
        
        passwordField.addActionListener(e -> handleLogin());
        
        return rightPanel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(400, 45));
        field.setMaximumSize(new Dimension(400, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(400, 45));
        field.setMaximumSize(new Dimension(400, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JButton createGradientButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gp = new GradientPaint(0, 0, new Color(59, 130, 246), w, 0, new Color(37, 99, 235));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(400, 50));
        button.setMaximumSize(new Dimension(400, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 255, 255, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (!ValidationUtil.isNotEmpty(username)) {
            UIHelper.showError(this, "Please enter username or email");
            return;
        }
        
        if (!ValidationUtil.isNotEmpty(password)) {
            UIHelper.showError(this, "Please enter password");
            return;
        }
        
        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                UIHelper.showSuccess(this, "Welcome, " + user.getFullName() + "!");
                
                dispose();
                new MainFrame().setVisible(true);
            } else {
                UIHelper.showError(this, "Invalid username or password");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        UIHelper.setLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
