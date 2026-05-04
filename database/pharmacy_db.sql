-- ============================================
-- Pharmacy Management System Database
-- ============================================

CREATE DATABASE IF NOT EXISTS pharmacy_db;
USE pharmacy_db;

-- Create medicines table
CREATE TABLE IF NOT EXISTS medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    manufacturer VARCHAR(100),
    price DECIMAL(10, 2),
    stock_quantity INT DEFAULT 0,
    expiry_date DATE,
    prescription_required BOOLEAN DEFAULT FALSE
);

-- Create sales table
CREATE TABLE IF NOT EXISTS sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_name VARCHAR(100),
    total_amount DECIMAL(10, 2),
    payment_method VARCHAR(50)
);

-- Create sale_items table
CREATE TABLE IF NOT EXISTS sale_items (
    sale_item_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT,
    medicine_id INT,
    quantity INT,
    unit_price DECIMAL(10, 2),
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (sale_id) REFERENCES sales(sale_id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

-- Insert sample medicines
INSERT INTO medicines (name, category, manufacturer, price, stock_quantity, expiry_date, prescription_required) VALUES
('Paracetamol 500mg', 'Pain Relief', 'GSK', 5.50, 100, '2025-12-31', FALSE),
('Amoxicillin 250mg', 'Antibiotic', 'Pfizer', 15.75, 50, '2025-10-31', TRUE),
('Cetirizine 10mg', 'Antihistamine', 'Cipla', 8.25, 75, '2025-08-31', FALSE),
('Metformin 500mg', 'Diabetes', 'Novartis', 12.50, 60, '2025-11-30', TRUE);

SELECT 'Database Setup Complete!' as Status;
SELECT COUNT(*) as Total_Medicines FROM medicines;
