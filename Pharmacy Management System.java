gbc.gridx = 3;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prescription Required:"), gbc);
        gbc.gridx = 1;
        chkPrescription = new JCheckBox();
        formPanel.add(chkPrescription, gbc);
        
        gbc.gridx = 2;
        JButton btnAdd = new JButton("Add Medicine");
        btnAdd.addActionListener(e -> addMedicine());
        formPanel.add(btnAdd, gbc);
        
        gbc.gridx = 3;
        JButton btnUpdate = new JButton("Update Medicine");
        btnUpdate.addActionListener(e -> updateMedicine());
        formPanel.add(btnUpdate, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JButton btnDelete = new JButton("Delete Medicine");
        btnDelete.addActionListener(e -> deleteMedicine());
        formPanel.add(btnDelete, gbc);
        
        gbc.gridx = 1;
        JButton btnClear = new JButton("Clear Form");
        btnClear.addActionListener(e -> clearMedicineForm());
        formPanel.add(btnClear, gbc);
        
        panel.add(formPanel, BorderLayout.SOUTH);
        
        // Table click listener
        medicineTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = medicineTable.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(medicineTableModel.getValueAt(row, 1).toString());
                    txtCategory.setText(medicineTableModel.getValueAt(row, 2).toString());
                    txtManufacturer.setText(medicineTableModel.getValueAt(row, 3).toString());
                    txtPrice.setText(medicineTableModel.getValueAt(row, 4).toString().replace("$", ""));
                    txtStock.setText(medicineTableModel.getValueAt(row, 5).toString());
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createSalePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Add to Cart"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        selectionPanel.add(new JLabel("Medicine:"), gbc);
        gbc.gridx = 1;
        cmbMedicine = new JComboBox<>();
        cmbMedicine.setPreferredSize(new Dimension(250, 25));
        selectionPanel.add(cmbMedicine, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        selectionPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(10);
        selectionPanel.add(txtQuantity, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.addActionListener(e -> addToCart());
        selectionPanel.add(btnAddToCart, gbc);
        
        gbc.gridx = 1;
        JButton btnClearCart = new JButton("Clear Cart");
        btnClearCart.addActionListener(e -> clearCart());
        selectionPanel.add(btnClearCart, gbc);
        
        leftPanel.add(selectionPanel, BorderLayout.NORTH);
        
        String[] cartColumns = {"ID", "Medicine Name", "Quantity", "Unit Price", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        leftPanel.add(cartScroll, BorderLayout.CENTER);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Checkout"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;