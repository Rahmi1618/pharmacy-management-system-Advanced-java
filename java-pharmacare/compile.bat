@echo off
echo ========================================
echo PharmaCare - Compilation Script
echo ========================================
echo.

if not exist "bin" mkdir bin

echo Compiling Java source files...
echo.

javac -encoding UTF-8 -cp "lib\*" -d bin src\config\*.java src\database\*.java src\models\*.java src\dao\*.java src\utils\*.java src\ui\auth\*.java src\ui\dashboard\*.java src\ui\medicines\*.java src\ui\pharmacies\*.java src\ui\orders\*.java src\ui\users\*.java src\Main.java

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo.
    echo You can now run the application using run.bat
    echo.
) else (
    echo.
    echo ========================================
    echo Compilation failed!
    echo ========================================
    echo.
    echo Please check the error messages above.
    echo Make sure all required JAR files are in the lib folder:
    echo - mysql-connector-java-8.0.33.jar
    echo - jbcrypt-0.4.jar
    echo.
)

pause
