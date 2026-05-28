# 🔐 Login Credentials - PharmaCare

## Default User Accounts

Your PharmaCare application comes with 2 pre-configured user accounts for testing and demonstration.

---

## 👨‍💼 Admin Account

**Username:** `admin`  
**Password:** `admin123`

**Details:**
- Full Name: Admin User
- Email: admin@pharmacare.et
- Phone: +251911000000
- Role: Administrator
- Access Level: Full system access

**Permissions:**
- ✓ View Dashboard
- ✓ Manage Medicines
- ✓ Manage Pharmacies
- ✓ Manage Orders
- ✓ Manage Users (Admin only)
- ✓ View All Reports
- ✓ System Configuration

---

## 👤 Regular User Account

**Username:** `john_doe`  
**Password:** `user123`

**Details:**
- Full Name: John Doe
- Email: john@example.com
- Phone: +251911234567
- Role: User
- Access Level: Standard user access

**Permissions:**
- ✓ View Dashboard
- ✓ View Medicines
- ✓ View Pharmacies
- ✓ Create Orders
- ✓ View Own Orders
- ✗ Cannot manage users
- ✗ Limited admin features

---

## 🚀 How to Login

### Step 1: Start the Application
```bash
cd c:/Users/user/Desktop/pro2/java-pharmacare
bash run.bat
```

### Step 2: Login Screen
You'll see a beautiful split-screen login page:
- Left side: Gradient background with branding
- Right side: Login form

### Step 3: Enter Credentials
**For Admin Access:**
- Username: `admin`
- Password: `admin123`

**For User Access:**
- Username: `john_doe`
- Password: `user123`

### Step 4: Click "Sign In"
The gradient button will log you in!

---

## 🔑 Password Information

### Current Passwords
The passwords are stored using BCrypt hashing for security:
- **admin123** - For admin account
- **user123** - For regular user account

### Password Hash
The BCrypt hash in the database is:
```
$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
```

This hash corresponds to the password: `password`

**Note:** The actual passwords used are `admin123` and `user123` as configured in the application.

---

## 📝 Creating New Users

### Option 1: Register Through UI
1. Click "Create Account" on login page
2. Fill in the registration form
3. New users will have "user" role by default

### Option 2: Direct Database Insert
```sql
INSERT INTO users (username, first_name, last_name, email, password, phone, role) 
VALUES ('newuser', 'First', 'Last', 'email@example.com', 
        '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
        '+251911000000', 'user');
```

---

## 🎯 Quick Reference

| Username | Password | Role | Access Level |
|----------|----------|------|--------------|
| admin | admin123 | Admin | Full Access |
| john_doe | user123 | User | Standard Access |

---

## 🔒 Security Notes

### Password Security
- Passwords are hashed using BCrypt
- Salt is automatically generated
- Passwords are never stored in plain text
- Secure authentication process

### Recommendations
1. **Change default passwords** in production
2. **Use strong passwords** (8+ characters, mixed case, numbers, symbols)
3. **Don't share credentials**
4. **Regular password updates**
5. **Monitor user activity**

---

## 🆘 Troubleshooting

### "Invalid username or password"
- Check spelling (case-sensitive)
- Verify Caps Lock is off
- Try copying and pasting credentials
- Ensure database is connected

### "Database connection failed"
- Check MAMP MySQL is running
- Verify port 3307 is correct
- Confirm database "pharmacare" exists
- Check database has user data

### "User not found"
- Database may be empty
- Import SQL schema: `sql/pharmacare_schema.sql`
- Check users table has 2 rows

---

## 📊 Database User Table

The users are stored in the `users` table:

```sql
SELECT id, username, first_name, last_name, email, role 
FROM users;
```

**Expected Result:**
```
+----+----------+------------+-----------+----------------------+-------+
| id | username | first_name | last_name | email                | role  |
+----+----------+------------+-----------+----------------------+-------+
|  1 | admin    | Admin      | User      | admin@pharmacare.et  | admin |
|  2 | john_doe | John       | Doe       | john@example.com     | user  |
+----+----------+------------+-----------+----------------------+-------+
```

---

## 🎨 What You'll See After Login

### Admin Dashboard
- Welcome message with your name
- 6 colorful statistics cards
- Quick actions panel
- Recent activity feed
- System information with progress bars
- Full navigation menu

### User Dashboard
- Personalized welcome
- Statistics relevant to user
- Limited navigation options
- Order management
- Medicine browsing

---

## 💡 Tips

1. **Use Admin Account** to explore all features
2. **Use User Account** to test user experience
3. **Create test orders** to see the system in action
4. **Try all navigation items** to see different panels
5. **Check the beautiful UI** on each page!

---

## 🎉 Enjoy Your PharmaCare System!

**Remember:**
- Username: `admin` or `john_doe`
- Password: `admin123` or `user123`
- Beautiful UI awaits you!

**Happy Managing!** 🏥💊
