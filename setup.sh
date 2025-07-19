#!/bin/bash

echo "Setting up Hazelcast Study Project environment..."

# Check if running as root
if [ "$EUID" -eq 0 ]; then
    echo "Please don't run this script as root. Run it as a regular user."
    exit 1
fi

# Update package list
echo "Updating package list..."
sudo apt update

# Install Java 11
echo "Installing Java 11..."
sudo apt install -y openjdk-11-jdk

# Install Maven
echo "Installing Maven..."
sudo apt install -y maven

# Verify installations
echo "Verifying installations..."
java -version
mvn -version

echo ""
echo "Setup complete! You can now run the project with:"
echo "  ./run-demo.sh"
echo ""
echo "Or manually with:"
echo "  mvn clean compile"
echo "  mvn exec:java -Dexec.mainClass=\"com.example.hazelcast.HazelcastDemo\"" 