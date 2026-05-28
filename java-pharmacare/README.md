# PharmaCare - Java Pharmacy Management System

A complete pharmacy management system built with Java Swing GUI, JDBC, MySQL, and MVC architecture. This is a modernized version of the original PHP-based system.

## Features

### Authentication & User Management
- User registration and login
- Password hashing with BCrypt
- Role-based access control (Admin, User, Pharmacist)
- Session management
- User profile management

### Dashboard
- Real-time statistics
- Medicine count
- Pharmacy count
- Order tracking
- Pending orders monitoring
- System status overview

### Medicine Management
- Add, edit, delete medicines
- Search and filter medicines
- Category management
- Prescription requirement tracking
- Expiry date management
- Stock availability across pharmacies
- Price comparison

### Pharmacy Management
- Add, edit, delete pharmacies
- Search pharmacies
- 24-hour pharmacy tracking
- Location management (GPS coordinates)
- Rating system
- Inventory management per pharmacy

### Order Management
- Create new orders
- View order history
- Update order status (pending, confirmed, delivered, cancelled)
- Filter orders by status
- Order details view
- Payment method selection (TeleBirr, Cash, Bank Transfer)
- Delivery address management
- Automatic stock updates

### Inventory Management
- Track medicine stock per pharmacy
- Low stock alerts
- Stock quantity management
- Availability status (In Stock, Limited Stock, Out of Stock)
- Price management per pharmacy

## Technology Stack

### Backend
- **Language**: Java 8+
- **Database**: MySQL (MAMP Server)
- **Database Connection**: JDBC
- **Architecture**: MVC (Model-View-Controller)
- **Design Pattern**: DAO (Data Access Object)
- **Security**: BCrypt password hashing

### Frontend
- **GUI Framework**: Java Swing
- **Components**: JFrame, JPanel, JTable, JDialog
- **Layout Managers**: BorderLayout, BoxLayout, GridLayout, CardLayout

### Database
- **Server**: MAMP MySQL
- **Port**: 3307
- **Database Name**: pharmacare
- **Connection**: JDBC MySQL Connector

## Project Structure

```
java-pharmacare/
├── src/
│   ├── config/
│   │   └── DatabaseConfig.java
│   ├── database/
│   │   └── DatabaseConnection.java
│   ├── models/
│   │   ├── User.java
│   │   ├── Medicine.java
│   │   ├── Pharmacy.java
│   │   ├── PharmacyMedicine.java
│   │   └── Order.java
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── MedicineDAO.java
│   │   ├── PharmacyDAO.java
│   │   ├── PharmacyMedicineDAO.java
│   │   └── OrderDAO.java
│   ├── utils/
│   │   ├── PasswordUtil.java
│   │   ├── ValidationUtil.java
│   │   ├── UIHelper.java
│   │   └── SessionManager.java
│   ├── ui/
│   │   ├── auth/
│   │   │   ├── LoginFrame.java
│   │   │   └── RegisterDialog.java
│   │   ├── dashboard/
│   │   │   ├── MainFrame.java
│   │   │   └── DashboardPanel.java
│   │   ├── medicines/
│   │   │   ├── MedicinesPanel.java
│   │   │   ├── MedicineDialog.java
│   │   │   └── StockDialog.java
│   │   ├── pharmacies/
│   │   │   ├── PharmaciesPanel.java
│   │   │   ├── PharmacyDialog.java
│   │   │   └── InventoryDialog.java
│   │   ├── orders/
│   │   │   ├── OrdersPanel.java
│   │   │   ├── CreateOrderDialog.java
│   │   │   └── OrderDetailsDialog.java
│   │   └── users/
│   │       ├── UsersPanel.java
│   │       └── UserDetailsDialog.java
│   └── Main.java
├── lib/
│   ├── mysql-connector-java-8.0.33.jar
│   └── jbcrypt-0.4.jar
├── sql/
│   └── pharmacare_schema.sql
└── README.md
```

## Prerequisites

1. **Java Development Kit (JDK) 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **MAMP (MySQL Server)**
   - Download from: https://www.mamp.info/
   - Start Apache and MySQL services
   - Default MySQL port: 3307

3. **MySQL JDBC Driver**
   - Download: mysql-connector-java-8.0.33.jar
   - Place in `lib/` folder

4. **BCrypt Library**
   - Download: jbcrypt-0.4.jar
   - Place in `lib/` folder

## Installation & Setup

### Step 1: Download Required Libraries

1. **MySQL Connector/J**
   ```
   Download from: https://dev.mysql.com/downloads/connector/j/
   File: mysql-connector-java-8.0.33.jar
   Place in: java-pharmacare/lib/
   ```

2. **JBCrypt**
   ```
   Download from: https://www.mindrot.org/projects/jBCrypt/
   Or Maven Central: https://mvnrepository.com/artifact/org.mindrot/jbcrypt
   File: jbcrypt-0.4.jar
   Place in: java-pharmacare/lib/
   ```

### Step 2: Setup MAMP MySQL Database

1. **Start MAMP**
   - Open MAMP application
   - Click "Start Servers"
   - Verify MySQL is running on port 3307

2. **Create Database**
   - Open phpMyAdmin: http://localhost/phpMyAdmin
   - Or use MySQL command line
   - Create database named `pharmacare`

3. **Import Database Schema**
   ```sql
   -- Option 1: Using phpMyAdmin
   - Go to phpMyAdmin
   - Select 'pharmacare' database
   - Click 'Import' tab
   - Choose file: sql/pharmacare_schema.sql
   - Click 'Go'

   -- Option 2: Using MySQL command line
   mysql -u root -p -h localhost -P 3307 pharmacare < sql/pharmacare_schema.sql
   ```

### Step 3: Configure Database Connection

The database configuration is already set in `src/config/DatabaseConfig.java`:

```java
private static final String DB_HOST = "localhost";
private static final String DB_PORT = "3307";
private static final String DB_NAME = "pharmacare";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";
```

**If your MAMP configuration is different, update these values.**

### Step 4: Compile the Project

```bash
# Navigate to project directory
cd java-pharmacare

# Compile all Java files
javac -cp "lib/*" -d bin src/**/*.java src/*.java

# Or compile with specific classpath
javac -cp "lib/mysql-connector-java-8.0.33.jar;lib/jbcrypt-0.4.jar" -d bin src/**/*.java src/*.java
```

### Step 5: Run the Application

```bash
# Run from bin directory
java -cp "bin;lib/*" Main

# Or with specific classpath
java -cp "bin;lib/mysql-connector-java-8.0.33.jar;lib/jbcrypt-0.4.jar" Main
```

## Default Login Credentials

### Admin Account
- **Username**: admin
- **Password**: password

### User Account
- **Username**: john_doe
- **Password**: password

## Usage Guide

### 1. Login
- Launch the application
- Enter username and password
- Click "Sign In"
- Or click "Create Account" to register

### 2. Dashboard
- View system statistics
- Monitor medicine count
- Track orders
- Check pending orders

### 3. Manage Medicines
- Click "Medicines" in sidebar
- Add new medicine with "Add Medicine" button
- Search medicines using search bar
- Edit or delete selected medicine
- View stock across pharmacies

### 4. Manage Pharmacies
- Click "Pharmacies" in sidebar
- Add new pharmacy
- Search pharmacies
- Edit pharmacy details
- View pharmacy inventory

### 5. Create Orders
- Click "Orders" in sidebar
- Click "Create Order"
- Select medicine and pharmacy
- Enter quantity and delivery details
- Choose payment method
- Submit order

### 6. Manage Users (Admin Only)
- Click "Users" in sidebar
- View all registered users
- View user details
- Delete users if needed

## Database Schema

### Tables

1. **users**
   - User authentication and profile information
   - Roles: admin, user, pharmacist

2. **medicines**
   - Medicine catalog
   - Categories, descriptions, prescription requirements

3. **pharmacies**
   - Pharmacy information
   - Location, contact details, ratings

4. **pharmacy_medicines**
   - Inventory management
   - Price and stock per pharmacy

5. **orders**
   - Order tracking
   - Status management
   - Delivery information

## Security Features

- **Password Hashing**: BCrypt with salt
- **SQL Injection Prevention**: PreparedStatements
- **Input Validation**: All user inputs validated
- **Session Management**: Secure session handling
- **Role-Based Access**: Admin, User, Pharmacist roles

## Features Comparison: PHP vs Java

| Feature | PHP Version | Java Version |
|---------|-------------|--------------|
| Architecture | Procedural | MVC + DAO |
| UI | HTML/CSS/JS | Java Swing GUI |
| Database | MySQL | MySQL (JDBC) |
| Security | Basic | BCrypt + PreparedStatements |
| Session | PHP Sessions | SessionManager |
| Validation | JavaScript | Java Validation |
| Code Structure | Mixed | Layered Architecture |

## Troubleshooting

### Database Connection Issues

**Problem**: Cannot connect to database
```
Solution:
1. Verify MAMP MySQL is running
2. Check port number (default: 3307)
3. Verify database name: pharmacare
4. Check username/password in DatabaseConfig.java
```

### ClassNotFoundException: com.mysql.cj.jdbc.Driver

**Problem**: MySQL JDBC driver not found
```
Solution:
1. Download mysql-connector-java-8.0.33.jar
2. Place in lib/ folder
3. Include in classpath when compiling and running
```

### BCrypt Error

**Problem**: BCrypt class not found
```
Solution:
1. Download jbcrypt-0.4.jar
2. Place in lib/ folder
3. Include in classpath
```

### Compilation Errors

**Problem**: Cannot find symbol errors
```
Solution:
1. Ensure all source files are in correct directories
2. Compile with proper classpath
3. Check Java version (requires JDK 8+)
```

## Development

### Adding New Features

1. **Create Model** in `src/models/`
2. **Create DAO** in `src/dao/`
3. **Create UI Panel** in `src/ui/`
4. **Add to MainFrame** navigation

### Code Style

- Use camelCase for variables and methods
- Use PascalCase for classes
- Add comments for complex logic
- Follow MVC architecture
- Use PreparedStatements for all SQL queries

## Future Enhancements

- [ ] Reports generation (PDF)
- [ ] Email notifications
- [ ] SMS integration
- [ ] Barcode scanning
- [ ] Advanced analytics
- [ ] Multi-language support
- [ ] Export/Import data
- [ ] Backup and restore

## License

This project is developed for educational purposes.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Verify database connection
3. Check console for error messages
4. Ensure all dependencies are installed

## Credits

**Original PHP Version**: PharmaCare Web Application
**Java Version**: Modernized with Swing GUI and JDBC
**Database**: MySQL on MAMP
**Libraries**: MySQL Connector/J, JBCrypt

---

**Built with ❤️ for Ethiopian Healthcare**

*PharmaCare - Making pharmacy management efficient and accessible*
