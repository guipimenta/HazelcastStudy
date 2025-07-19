# Quick Start Guide

## Prerequisites Check

First, check if you have the required tools:

```bash
java -version
mvn -version
```

If either command fails, you need to install the missing components.

## Installation

### Option 1: Automatic Setup (Recommended)

Run the setup script:

```bash
./setup.sh
```

This will install Java 11 and Maven automatically.

### Option 2: Manual Installation

#### Install Java 11
```bash
sudo apt update
sudo apt install openjdk-11-jdk
```

#### Install Maven
```bash
sudo apt install maven
```

## Running the Project

### 1. Build the Project
```bash
mvn clean compile
```

### 2. Run the Demo
```bash
mvn exec:java -Dexec.mainClass="com.example.hazelcast.HazelcastDemo"
```

### 3. Or Use the Convenience Script
```bash
./run-demo.sh
```

## What You'll See

The demo will show:

1. **Distributed Map Operations**: Basic key-value operations
2. **Distributed Queue**: FIFO queue operations
3. **Distributed Topic**: Publish-subscribe messaging
4. **Distributed Lock**: CP subsystem locks
5. **Distributed Executor**: Task execution across the cluster
6. **Advanced Map Features**: Entry processors, predicates, aggregations

## Running Tests

```bash
mvn test
```

## Project Structure

```
hazelcast-study/
├── src/main/java/com/example/hazelcast/
│   ├── HazelcastDemo.java      # Main demo application
│   ├── MapExamples.java        # Advanced map features
│   └── HazelcastConfig.java    # Configuration examples
├── src/main/resources/
│   └── logback.xml             # Logging configuration
├── src/test/java/com/example/hazelcast/
│   └── HazelcastDemoTest.java  # Unit tests
├── pom.xml                     # Maven configuration
├── README.md                   # Detailed documentation
├── QUICKSTART.md               # This file
├── setup.sh                    # Environment setup script
└── run-demo.sh                 # Demo runner script
```

## Troubleshooting

### Port Already in Use
If you get port conflicts, Hazelcast will automatically try the next available port.

### Memory Issues
If you encounter memory issues, increase the JVM heap size:

```bash
mvn exec:java -Dexec.mainClass="com.example.hazelcast.HazelcastDemo" -Dexec.args="-Xmx2g"
```

### Network Issues
The demo uses multicast for cluster discovery. If you're in a restricted network environment, you may need to configure TCP/IP join mechanism.

## Next Steps

After running the demo successfully:

1. Read the full [README.md](README.md) for detailed explanations
2. Explore the source code to understand the implementation
3. Modify the examples to experiment with different features
4. Try running multiple instances to see clustering in action

## Support

If you encounter issues:

1. Check the logs in the console output
2. Verify Java and Maven versions are compatible
3. Ensure no firewall is blocking the required ports
4. Check the [Hazelcast Documentation](https://docs.hazelcast.com/) for more information 