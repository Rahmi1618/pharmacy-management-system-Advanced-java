# PharmaCare - Quick Start Guide

Get up and running in 5 minutes!

## Prerequisites Checklist

Before starting, ensure you have:

- [ ] Java JDK 8+ installed
- [ ] MAMP installed and running
- [ ] MySQL Connector JAR in lib folder
- [ ] JBCrypt JAR in lib folder

---

## Step 1: Download Dependencies (2 minutes)

### MySQL Connector
1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Download: mysql-connector-java-8.0.33.jar
3. Place in: `java-pharmacare/lib/`

### JBCrypt
1. Go to: https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
2. Download: jbcrypt-0.4.jar
3. Place in: `java-pharmacare/lib/`

---

## Step 2: Setup Database (2 minutes)

### Start MAMP
```
1. Open MAMP application
2. Click "Start Servers"
3. Wait for MySQL to turn green
```

### Import Database
```
1. Open: http://localhost/phpMyAdmin
2. Click "New" → Create database "pharmacare"
3. Select "pharmacare" database
4. Click "Import" tab
5. Choose file: sql/pharmacare_schema.sql
6. Click "Go"
```

---

## Step 3: Compile & Run (1 minute)

### Windows
```cmd
cd java-pharmacare
compile.bat
run.bat
```

### Manual
```cmd
cd java-pharmacare

# Compile
javac -cp "lib\*" -d bin src\**\*.java src\*.java

# Run
java -cp "bin;lib\*" Main
```

---

## Step 4: Login

### Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `password`

**User Account:**
- Username: `john_doe`
- Password: `password`

---

## Quick Feature Tour

### 1. Dashboard
- View system statistics
- Monitor orders and inventory

### 2. Medicines
- Click "Medicines" in sidebar
- Click "Add Medicine" to add new
- Use search bar to find medicines

### 3. Pharmacies
- Click "Pharmacies" in sidebar
- Add, edit, or view pharmacies
- Check inventory per pharmacy

### 4. Orders
- Click "Orders" in sidebar
- Click "Create Order" to place new order
- Filter by status to track orders

### 5. Users (Admin Only)
- Click "Users" in sidebar
- View all registered users
- Manage user accounts

---

## Troubleshooting

### Can't connect to database?
```
✓ Check MAMP is running
✓ Verify MySQL port is 3307
✓ Confirm database "pharmacare" exists
```

### Compilation errors?
```
✓ Verify both JAR files are in lib folder
✓ Check Java version: java -version
✓ Try: rmdir /s /q bin, then compile again
```

### Application won't start?
```
✓ Compile first: compile.bat
✓ Check MAMP MySQL is running
✓ Verify database was imported
```

---

## Next Steps

1. **Change default passwords**
   - Login as admin
   - Update password in profile

2. **Add your data**
   - Add your medicines
   - Add your pharmacies
   - Set up inventory

3. **Create test order**
   - Go to Orders
   - Create a test order
   - Verify stock updates

4. **Explore features**
   - Try search functionality
   - View reports
   - Test all CRUD operations

---

## Support

**Documentation:**
- README.md - Full documentation
- SETUP_GUIDE.md - Detailed setup
- DEPENDENCIES.md - Library info
- PROJECT_SUMMARY.md - Technical details

**Quick Commands:**
```cmd
compile.bat  # Compile the project
run.bat      # Run the application
```

---

## That's It!

You're now ready to use PharmaCare! 🎉

**Need help?** Check the full documentation in README.md

**Found a bug?** Review error messages and check troubleshooting section

**Want to customize?** See PROJECT_SUMMARY.md for architecture details

---

**Happy Pharmacy Management! 💊**
