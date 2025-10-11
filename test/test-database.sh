#!/bin/bash

echo "================================================"
echo "FruitStore Database Connection Test"
echo "================================================"

echo ""
echo "Compiling Java files..."

# Create build directory
mkdir -p ../build/classes

# Compile all Java files (from test/ directory, need to go up one level)
javac -cp "../ref_lib/*" -d ../build/classes ../src/java/config/*.java ../src/java/utils/*.java ../src/java/daos/*.java ../src/java/models/*.java DatabaseTest.java

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Compilation failed!"
    echo "Make sure you have:"
    echo "1. Java JDK installed"
    echo "2. MySQL Connector JAR in ../ref_lib folder"
    echo "3. All source files exist in ../src/java"
    exit 1
fi

echo "Compilation successful!"
echo ""

echo "Copying resource files..."
cp ../src/java/resources/database.properties ../build/classes/ 2>/dev/null
cp ../src/java/resources/Database.sql ../build/classes/ 2>/dev/null

echo "Running database test..."
echo ""

# Run the test
java -cp "../ref_lib/*:../build/classes" DatabaseTest

echo ""
echo "Test completed."
