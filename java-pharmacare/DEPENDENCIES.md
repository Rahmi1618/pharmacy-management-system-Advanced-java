# PharmaCare - Dependencies Guide

This document lists all required dependencies and where to download them.

## Required Dependencies

### 1. Java Development Kit (JDK)

**Version:** JDK 8 or higher (JDK 17 recommended)

**Download:**
- Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- OpenJDK: https://adoptium.net/

**Installation:**
1. Download installer for Windows
2. Run installer
3. Follow installation wizard
4. Verify: `java -version` and `javac -version`

---

### 2. MAMP (MySQL Server)

**Version:** Latest version

**Download:**
- Official Site: https://www.mamp.info/en/downloads/

**Configuration:**
- MySQL Port: 3307
- MySQL User: root
- MySQL Password: (empty)

**Installation:**
1. Download MAMP installer
2. Run installer
3. Install to default location
4. Start servers from MAMP control panel

---

### 3. MySQL Connector/J (JDBC Driver)

**Version:** 8.0.33 or higher

**Download Options:**

**Option 1: Direct Download**
- URL: https://dev.mysql.com/downloads/connector/j/
- Select: Platform Independent (ZIP Archive)
- File: mysql-connector-java-8.0.33.jar

**Option 2: Maven Repository**
- URL: https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.33
- Download JAR file

**Installation:**
1. Download the JAR file
2. Copy to: `java-pharmacare/lib/mysql-connector-java-8.0.33.jar`

**Maven Coordinates (for reference):**
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

---

### 4. JBCrypt (Password Hashing)

**Version:** 0.4

**Download Options:**

**Option 1: Maven Central**
- URL: https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
- Click "jar" link to download

**Option 2: Official Site**
- URL: https://www.mindrot.org/projects/jBCrypt/

**Installation:**
1. Download jbcrypt-0.4.jar
2. Copy to: `java-pharmacare/lib/jbcrypt-0.4.jar`

**Maven Coordinates (for reference):**
```xml
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
```

---

## Directory Structure

After downloading all dependencies, your project structure should look like:

```
java-pharmacare/
├── lib/
│   ├── mysql-connector-java-8.0.33.jar  ✓ Required
│   └── jbcrypt-0.4.jar                   ✓ Required
├── src/
│   └── [source files]
├── sql/
│   └── pharmacare_schema.sql
├── compile.bat
├── run.bat
└── README.md
```

---

## Verification Checklist

### ✓ Java Installation
```cmd
java -version
javac -version
```
Expected: Version 8 or higher

### ✓ MAMP Installation
1. Open MAMP
2. Click "Start Servers"
3. Verify MySQL is green/running
4. Check port: 3307

### ✓ MySQL Connector
```cmd
dir lib\mysql-connector-java-8.0.33.jar
```
Expected: File exists

### ✓ JBCrypt Library
```cmd
dir lib\jbcrypt-0.4.jar
```
Expected: File exists

### ✓ Database
1. Open phpMyAdmin: http://localhost/phpMyAdmin
2. Check database 'pharmacare' exists
3. Verify 5 tables exist

---

## Alternative Download Methods

### Using Maven (Optional)

If you have Maven installed, you can download dependencies automatically:

**pom.xml:**
```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
</dependencies>
```

Then run:
```cmd
mvn dependency:copy-dependencies -DoutputDirectory=lib
```

### Using Gradle (Optional)

**build.gradle:**
```gradle
dependencies {
    implementation 'mysql:mysql-connector-java:8.0.33'
    implementation 'org.mindrot:jbcrypt:0.4'
}
```

---

## Troubleshooting Downloads

### Issue: Cannot download from Maven Central

**Solution:**
1. Try direct download from official sites
2. Use alternative mirrors
3. Check firewall/proxy settings

### Issue: Wrong JAR version

**Solution:**
1. Verify version numbers match
2. Use compatible versions:
   - MySQL Connector: 8.0.x
   - JBCrypt: 0.4

### Issue: JAR file corrupted

**Solution:**
1. Re-download the file
2. Verify file size matches expected
3. Check MD5/SHA checksums if available

---

## License Information

### MySQL Connector/J
- License: GPL v2 with FOSS Exception
- Commercial license available

### JBCrypt
- License: ISC/BSD
- Free for commercial use

### MAMP
- Free version available
- Pro version with additional features

---

## Version Compatibility

| Component | Minimum Version | Recommended Version |
|-----------|----------------|---------------------|
| Java JDK | 8 | 17 |
| MySQL Connector | 8.0.0 | 8.0.33 |
| JBCrypt | 0.4 | 0.4 |
| MAMP | 4.0 | Latest |
| MySQL | 5.7 | 8.0 |

---

## Quick Download Links

**Java JDK:**
- https://www.oracle.com/java/technologies/downloads/
- https://adoptium.net/

**MAMP:**
- https://www.mamp.info/en/downloads/

**MySQL Connector/J:**
- https://dev.mysql.com/downloads/connector/j/
- https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.33

**JBCrypt:**
- https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
- https://www.mindrot.org/projects/jBCrypt/

---

## Support

If you encounter issues downloading or installing dependencies:

1. Check your internet connection
2. Verify URLs are accessible
3. Try alternative download methods
4. Check system requirements
5. Review error messages carefully

---

**All dependencies are free and open source!**
