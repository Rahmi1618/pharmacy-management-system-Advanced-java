# PharmaCare - Complete Setup Guide

This guide will walk you through setting up the Java Pharmacy Management System from scratch.

## Table of Contents
1. [Prerequisites Installation](#prerequisites-installation)
2. [MAMP Setup](#mamp-setup)
3. [Database Setup](#database-setup)
4. [Project Setup](#project-setup)
5. [Running the Application](#running-the-application)
6. [Troubleshooting](#troubleshooting)

---

## Prerequisites Installation

### 1. Install Java Development Kit (JDK)

**Download JDK 8 or higher:**
- Visit: https://www.oracle.com/java/technologies/downloads/
- Download JDK for Windows
- Run the installer
- Follow installation wizard

**Verify Installation:**
```cmd
java -version
javac -version
```

You should see version information for both commands.

**Set JAVA_HOME (if needed):**
1. Right-click "This PC" → Properties
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under System Variables, click "New"
5. Variable name: `JAVA_HOME`
6. Variable value: `C:\Program Files\Java\jdk-17` (your JDK path)
7. Edit PATH variable and add: `%JAVA_HOME%\bin`

### 2. Install MAMP

**Download MAMP:**
- Visit: https://www.mamp.info/en/downloads/
- Download MAMP for Windows
- Run the installer
- Install to default location: `C:\MAMP`

**MAMP Configuration:**
- Default MySQL Port: 3307
- Default Apache Port: 8888
- MySQL Username: root
- MySQL Password: (empty)

---

## MAMP Setup

### Step 1: Start MAMP

1. Open MAMP application
2. Click "Start Servers" button
3. Wait for both Apache and MySQL to turn green
4. Verify MySQL is running on port 3307

### Step 2: Access phpMyAdmin

1. Click "WebStart" in MAMP
2. Or navigate to: http://localhost/phpMyAdmin
3. Login with:
   - Username: root
   - Password: (leave empty)

### Step 3: Verify MySQL Port

1. In MAMP, click "Preferences"
2. Go to "Ports" tab
3. Verify MySQL Port is 3307
4. If different, update `DatabaseConfig.java` accordingly

---

## Database Setup

### Method 1: Using phpMyAdmin (Recommended)

1. **Open phpMyAdmin**
   - Go to: http://localhost/phpMyAdmin

2. **Create Database**
   - Click "New" in left sidebar
   - Database name: `pharmacare`
   - Collation: `utf8mb4_general_ci`
   - Click "Create"

3. **Import Schema**
   - Select `pharmacare` database
   - Click "Import" tab
   - Click "Choose File"
   - Select: `java-pharmacare/sql/pharmacare_schema.sql`
   - Click "Go" at bottom
   - Wait for success message

4. **Verify Tables**
   - Click on `pharmacare` database
   - You should see 5 tables:
     - users
     - medicines
     - pharmacies
     - pharmacy_medicines
     - orders

### Method 2: Using MySQL Command Line

```cmd
cd C:\MAMP\bin\mysql\bin

mysql -u root -p -h localhost -P 3307

CREATE DATABASE pharmacare;
USE pharmacare;
SOURCE C:\Users\user\Desktop\pro2\java-pharmacare\sql\pharmacare_schema.sql;
SHOW TABLES;
EXIT;
```

---

## Project Setup

### Step 1: Download Required Libraries

**MySQL Connector/J:**
1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Select "Platform Independent"
3. Download ZIP archive
4. Extract `mysql-connector-java-8.0.33.jar`
5. Copy to: `java-pharmacare\lib\`

**JBCrypt:**
1. Visit: https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
2. Download `jbcrypt-0.4.jar`
3. Copy to: `java-pharmacare\lib\`

**Your lib folder should contain:**
```
java-pharmacare/
└── lib/
    ├── mysql-connector-java-8.0.33.jar
    └── jbcrypt-0.4.jar
```

### Step 2: Verify Database Configuration

Open `src\config\DatabaseConfig.java` and verify:

```java
private static final String DB_HOST = "localhost";
private static final String DB_PORT = "3307";  // MAMP default
private static final String DB_NAME = "pharmacare";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";  // Empty for MAMP
```

**If your MAMP uses different settings, update these values!**

### Step 3: Compile the Project

**Option 1: Using Batch Script (Easiest)**
```cmd
cd java-pharmacare
compile.bat
```

**Option 2: Manual Compilation**
```cmd
cd java-pharmacare

mkdir bin

javac -encoding UTF-8 -cp "lib\*" -d bin src\config\*.java src\database\*.java src\models\*.java src\dao\*.java src\utils\*.java src\ui\auth\*.java src\ui\dashboard\*.java src\ui\medicines\*.java src\ui\pharmacies\*.java src\ui\orders\*.java src\ui\users\*.java src\Main.java
```

**Expected Output:**
- No errors
- `.class` files created in `bin` folder

---

## Running the Application

### Method 1: Using Batch Script (Recommended)

```cmd
cd java-pharmacare
run.bat
```

### Method 2: Manual Execution

```cmd
cd java-pharmacare
java -cp "bin;lib\*" Main
```

### First Launch

1. **Login Screen Appears**
   - Modern GUI with PharmaCare logo
   - Username and password fields

2. **Default Credentials**
   - Admin Account:
     - Username: `admin`
     - Password: `password`
   
   - User Account:
     - Username: `john_doe`
     - Password: `password`

3. **Create New Account**
   - Click "Create Account" button
   - Fill in registration form
   - Click "Register"

---

## Application Features

### Dashboard
- View system statistics
- Medicine count
- Pharmacy count
- Order tracking
- Pending orders

### Medicines Management
- Add new medicines
- Edit medicine details
- Delete medicines
- Search medicines
- View stock across pharmacies
- Track expiry dates

### Pharmacies Management
- Add new pharmacies
- Edit pharmacy information
- Delete pharmacies
- View pharmacy inventory
- Track 24-hour pharmacies

### Orders Management
- Create new orders
- View order history
- Update order status
- Filter by status
- View order details
- Track deliveries

### Users Management (Admin Only)
- View all users
- View user details
- Delete users
- Monitor user activity

---

## Troubleshooting

### Issue 1: "Cannot connect to database"

**Symptoms:**
- Error message on startup
- Database connection failed

**Solutions:**
1. **Check MAMP is running**
   ```
   - Open MAMP
   - Verify MySQL is green/running
   ```

2. **Verify MySQL port**
   ```
   - MAMP → Preferences → Ports
   - Should be 3307
   - Update DatabaseConfig.java if different
   ```

3. **Check database exists**
   ```
   - Open phpMyAdmin
   - Look for 'pharmacare' database
   - If missing, import schema again
   ```

4. **Test connection manually**
   ```cmd
   cd C:\MAMP\bin\mysql\bin
   mysql -u root -p -h localhost -P 3307
   SHOW DATABASES;
   ```

### Issue 2: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Symptoms:**
- Error when starting application
- JDBC driver not found

**Solutions:**
1. **Verify JAR file exists**
   ```
   Check: java-pharmacare\lib\mysql-connector-java-8.0.33.jar
   ```

2. **Re-download if missing**
   ```
   https://dev.mysql.com/downloads/connector/j/
   ```

3. **Recompile with correct classpath**
   ```cmd
   compile.bat
   ```

### Issue 3: "BCrypt class not found"

**Symptoms:**
- Error during login/registration
- Password hashing fails

**Solutions:**
1. **Verify JAR file exists**
   ```
   Check: java-pharmacare\lib\jbcrypt-0.4.jar
   ```

2. **Download if missing**
   ```
   https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
   ```

3. **Recompile**
   ```cmd
   compile.bat
   ```

### Issue 4: Compilation Errors

**Symptoms:**
- `javac` command fails
- Cannot find symbol errors

**Solutions:**
1. **Check Java installation**
   ```cmd
   java -version
   javac -version
   ```

2. **Verify all source files exist**
   ```
   Check src folder structure matches README
   ```

3. **Clean and recompile**
   ```cmd
   rmdir /s /q bin
   mkdir bin
   compile.bat
   ```

### Issue 5: "Table doesn't exist"

**Symptoms:**
- SQL errors when using features
- Missing table errors

**Solutions:**
1. **Re-import database schema**
   ```
   - phpMyAdmin → pharmacare → Import
   - Select pharmacare_schema.sql
   - Click Go
   ```

2. **Verify all tables exist**
   ```sql
   USE pharmacare;
   SHOW TABLES;
   -- Should show: users, medicines, pharmacies, pharmacy_medicines, orders
   ```

### Issue 6: GUI Not Displaying Properly

**Symptoms:**
- Blank windows
- Missing components
- Layout issues

**Solutions:**
1. **Update Java to latest version**
2. **Check display scaling**
   ```
   Windows Settings → Display → Scale: 100%
   ```

3. **Try different Look and Feel**
   ```
   Edit UIHelper.java if needed
   ```

---

## Testing the Application

### Test 1: Login
1. Start application
2. Login with admin/password
3. Verify dashboard loads

### Test 2: Add Medicine
1. Click "Medicines" in sidebar
2. Click "Add Medicine"
3. Fill form and save
4. Verify medicine appears in table

### Test 3: Create Order
1. Click "Orders"
2. Click "Create Order"
3. Select medicine and pharmacy
4. Enter quantity
5. Submit order
6. Verify order created

### Test 4: View Statistics
1. Go to Dashboard
2. Verify counts are correct
3. Check pending orders

---

## Performance Tips

1. **Database Optimization**
   - Indexes are already created
   - Regular cleanup of old orders

2. **Application Performance**
   - Close unused dialogs
   - Refresh data only when needed

3. **MAMP Performance**
   - Allocate sufficient RAM in MAMP settings
   - Keep MySQL updated

---

## Security Best Practices

1. **Change Default Passwords**
   - Update admin password after first login
   - Use strong passwords

2. **Database Security**
   - Set MySQL root password in production
   - Limit database access

3. **Backup Data**
   - Regular database backups
   - Export important data

---

## Next Steps

1. **Customize the Application**
   - Update colors in UIHelper.java
   - Add your pharmacy logo
   - Modify categories

2. **Add More Features**
   - Reports generation
   - Email notifications
   - Advanced analytics

3. **Deploy to Production**
   - Use proper MySQL server
   - Set strong passwords
   - Enable SSL

---

## Support & Resources

**Documentation:**
- README.md - Main documentation
- SETUP_GUIDE.md - This file
- Code comments - Inline documentation

**Database:**
- phpMyAdmin: http://localhost/phpMyAdmin
- MySQL Port: 3307

**Useful Commands:**
```cmd
# Compile
compile.bat

# Run
run.bat

# Clean build
rmdir /s /q bin
mkdir bin
compile.bat

# Check Java
java -version

# Check MySQL
cd C:\MAMP\bin\mysql\bin
mysql -u root -p -h localhost -P 3307
```

---

## Congratulations!

You have successfully set up the PharmaCare Pharmacy Management System!

**Quick Start:**
1. Start MAMP
2. Run `run.bat`
3. Login with admin/password
4. Start managing your pharmacy!

**Need Help?**
- Check Troubleshooting section
- Review error messages carefully
- Verify all prerequisites are installed

---

**Happy Pharmacy Management! 💊**
