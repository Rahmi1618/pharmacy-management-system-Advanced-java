CREATE DATABASE IF NOT EXISTS pharmacare;
USE pharmacare;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role ENUM('admin', 'user', 'pharmacist') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

CREATE TABLE IF NOT EXISTS pharmacies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    rating DECIMAL(3, 2) DEFAULT 0.00,
    is_24_hours BOOLEAN DEFAULT FALSE,
    description TEXT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

CREATE TABLE IF NOT EXISTS medicines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(100),
    category VARCHAR(50),
    description TEXT,
    image VARCHAR(255),
    requires_prescription BOOLEAN DEFAULT FALSE,
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_category (category)
);

CREATE TABLE IF NOT EXISTS pharmacy_medicines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_id INT NOT NULL,
    medicine_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    availability ENUM('In Stock', 'Limited Stock', 'Out of Stock') DEFAULT 'In Stock',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
    UNIQUE KEY unique_pharmacy_medicine (pharmacy_id, medicine_id),
    INDEX idx_pharmacy (pharmacy_id),
    INDEX idx_medicine (medicine_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    pharmacy_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'confirmed', 'delivered', 'cancelled') DEFAULT 'pending',
    delivery_address TEXT,
    delivery_phone VARCHAR(20),
    payment_method ENUM('cash', 'telebirr', 'bank_transfer') DEFAULT 'telebirr',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_status (status)
);

INSERT INTO users (username, first_name, last_name, email, password, phone, role) VALUES
('admin', 'Admin', 'User', 'admin@pharmacare.et', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+251911000000', 'admin'),
('john_doe', 'John', 'Doe', 'john@example.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '+251911234567', 'user');

INSERT INTO pharmacies (name, address, phone, email, latitude, longitude, rating, is_24_hours, description) VALUES
('Kombolcha Central Pharmacy', 'Main Street, Near Hospital, Kombolcha', '+251911234567', 'central@pharmacy.et', 11.0815, 39.7267, 4.8, TRUE, 'The largest pharmacy in Kombolcha with 24/7 service'),
('Dessie Road Pharmacy', 'Dessie Road, Near Bus Station, Kombolcha', '+251911234568', 'dessie@pharmacy.et', 11.0845, 39.7298, 4.6, FALSE, 'Convenient location near transportation hub'),
('Wollo University Pharmacy', 'University Campus, Kombolcha', '+251911234569', 'uni@pharmacy.et', 11.0789, 39.7234, 4.7, FALSE, 'Serving university community and surrounding area'),
('Bati Road Pharmacy', 'Bati Road, Kombolcha', '+251911234570', 'bati@pharmacy.et', 11.0798, 39.7301, 4.4, FALSE, 'Specialized in chronic disease medications'),
('Express Pharmacy', 'Commercial District, Kombolcha', '+251911234571', 'express@pharmacy.et', 11.0832, 39.7289, 4.5, TRUE, 'Fast service with home delivery'),
('Family Care Pharmacy', 'Residential Area, Kombolcha', '+251911234572', 'family@pharmacy.et', 11.0801, 39.7245, 4.3, FALSE, 'Family-focused healthcare services');

INSERT INTO medicines (name, brand, category, description, requires_prescription, expiry_date) VALUES
('Paracetamol 500mg', 'Panadol', 'Pain Relief', 'Effective pain relief and fever reducer', FALSE, '2026-12-31'),
('Ibuprofen 400mg', 'Advil', 'Pain Relief', 'Anti-inflammatory pain reliever', FALSE, '2026-11-30'),
('Amoxicillin 250mg', 'Amoxil', 'Antibiotic', 'Broad-spectrum antibiotic for infections', TRUE, '2026-10-31'),
('Omeprazole 20mg', 'Losec', 'Digestive', 'Acid reflux and stomach ulcer treatment', FALSE, '2027-01-31'),
('Cetirizine 10mg', 'Zyrtec', 'Allergy', 'Antihistamine for allergies', FALSE, '2026-09-30'),
('Aspirin 100mg', 'Aspirin', 'Heart Health', 'Blood thinner and pain relief', FALSE, '2027-03-31'),
('Metformin 500mg', 'Glucophage', 'Diabetes', 'Type 2 diabetes medication', TRUE, '2026-08-31'),
('Vitamin D3 1000 IU', 'Calciferol', 'Vitamins', 'Bone health and immunity support', FALSE, '2027-06-30');

INSERT INTO pharmacy_medicines (pharmacy_id, medicine_id, price, stock_quantity, availability) VALUES
(1, 1, 25.50, 100, 'In Stock'),
(1, 2, 32.00, 80, 'In Stock'),
(1, 3, 45.00, 50, 'In Stock'),
(1, 4, 38.50, 60, 'In Stock'),
(1, 5, 28.00, 40, 'In Stock'),
(1, 6, 15.00, 120, 'In Stock'),
(1, 7, 55.00, 30, 'In Stock'),
(1, 8, 42.00, 70, 'In Stock'),
(2, 1, 23.00, 90, 'In Stock'),
(2, 2, 30.00, 60, 'In Stock'),
(2, 3, 42.50, 0, 'Out of Stock'),
(2, 4, 35.00, 45, 'In Stock'),
(2, 5, 26.50, 35, 'In Stock'),
(2, 6, 14.50, 100, 'In Stock'),
(2, 7, 52.00, 25, 'In Stock'),
(2, 8, 40.00, 55, 'In Stock'),
(3, 1, 27.00, 75, 'In Stock'),
(3, 2, 35.50, 50, 'In Stock'),
(3, 3, 48.00, 40, 'In Stock'),
(3, 4, 40.00, 30, 'In Stock'),
(3, 5, 30.00, 25, 'In Stock'),
(3, 6, 16.00, 80, 'In Stock'),
(3, 7, 58.00, 20, 'In Stock'),
(3, 8, 45.00, 60, 'In Stock'),
(4, 1, 24.00, 85, 'In Stock'),
(4, 2, 31.50, 70, 'In Stock'),
(4, 3, 44.00, 35, 'In Stock'),
(4, 4, 37.00, 50, 'In Stock'),
(4, 5, 27.50, 30, 'In Stock'),
(4, 6, 15.50, 90, 'In Stock'),
(4, 7, 54.00, 15, 'Limited Stock'),
(4, 8, 41.50, 65, 'In Stock'),
(5, 1, 26.00, 95, 'In Stock'),
(5, 2, 33.00, 65, 'In Stock'),
(5, 3, 46.50, 45, 'In Stock'),
(5, 4, 39.00, 55, 'In Stock'),
(5, 5, 29.00, 40, 'In Stock'),
(5, 6, 15.75, 110, 'In Stock'),
(5, 7, 56.50, 28, 'In Stock'),
(5, 8, 43.50, 75, 'In Stock'),
(6, 1, 25.00, 80, 'In Stock'),
(6, 2, 30.50, 55, 'In Stock'),
(6, 3, 43.50, 30, 'In Stock'),
(6, 4, 36.50, 40, 'In Stock'),
(6, 5, 28.50, 35, 'In Stock'),
(6, 6, 14.75, 95, 'In Stock'),
(6, 7, 53.50, 22, 'In Stock'),
(6, 8, 41.00, 50, 'In Stock');
