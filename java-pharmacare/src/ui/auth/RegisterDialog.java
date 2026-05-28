package ui.auth;

import dao.UserDAO;
import models.User;
import utils.UIHelper;
import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserDAO userDAO;
    
    public RegisterDialog(Frame parent) {
        super(parent, "Create Account", true);
        this.userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 650);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        usernameField = addFormField(mainPanel, "Username");
        firstNameField = addFormField(mainPanel, "First Name");
        lastNameField = addFormField(mainPanel, "Last Name");
        emailField = addFormField(mainPanel, "Email");
        phoneField = addFormField(mainPanel, "Phone");
        passwordField = addPasswordField(mainPanel, "Password");
        confirmPasswordField = addPasswordField(mainPanel, "Confirm Password");
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        
        JButton registerButton = UIHelper.createPrimaryButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(registerButton);
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
    
    private JPasswordField addPasswordField(JPanel panel, String label) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPasswordField field = UIHelper.createPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        panel.add(fieldLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return field;
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (!ValidationUtil.isNotEmpty(username)) {
            UIHelper.showError(this, "Username is required");
            return;
        }
        
        if (!ValidationUtil.isNotEmpty(firstName)) {
            UIHelper.showError(this, "First name is required");
            return;
        }
        
        if (!ValidationUtil.isNotEmpty(lastName)) {
            UIHelper.showError(this, "Last name is required");
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            UIHelper.showError(this, "Invalid email address");
            return;
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            UIHelper.showError(this, "Password must be at least 6 characters");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            UIHelper.showError(this, "Passwords do not match");
            return;
        }
        
        try {
            if (userDAO.usernameExists(username)) {
                UIHelper.showError(this, "Username already exists");
                return;
            }
            
            if (userDAO.emailExists(email)) {
                UIHelper.showError(this, "Email already exists");
                return;
            }
            
            User user = new User(username, firstName, lastName, email, password);
            user.setPhone(phone);
            
            User createdUser = userDAO.create(user);
            if (createdUser != null) {
                UIHelper.showSuccess(this, "Account created successfully! Please login.");
                dispose();
            } else {
                UIHelper.showError(this, "Failed to create account");
            }
        } catch (Exception e) {
            UIHelper.showError(this, "Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
