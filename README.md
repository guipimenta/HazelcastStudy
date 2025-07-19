# Hazelcast Study Project

A comprehensive Java project demonstrating Hazelcast's distributed computing features including distributed maps, queues, topics, locks, and executor services.

## Features

This project showcases the following Hazelcast capabilities:

- **Distributed Maps**: Key-value storage with atomic operations, predicates, and aggregations
- **Distributed Queues**: FIFO data structures for distributed processing
- **Distributed Topics**: Publish-subscribe messaging
- **Distributed Locks**: CP (Consensus Protocol) subsystem locks
- **Distributed Executor Service**: Distributed task execution
- **Advanced Map Features**: Entry processors, TTL, eviction policies, and indexes
- **Configuration Management**: Programmatic and declarative configuration

## Project Structure

```
hazelcast-study/
├── src/
│   ├── main/
│   │   ├── java/com/example/hazelcast/
│   │   │   ├── HazelcastDemo.java          # Main demo application
│   │   │   ├── MapExamples.java            # Advanced map features
│   │   │   └── HazelcastConfig.java        # Configuration examples
│   │   └── resources/
│   │       └── logback.xml                 # Logging configuration
│   └── test/
│       └── java/com/example/hazelcast/
│           └── HazelcastDemoTest.java      # Unit tests
├── pom.xml                                 # Maven configuration
└── README.md                               # This file
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Build the Project

```bash
mvn clean compile
```

### 2. Run the Demo

```bash
mvn exec:java -Dexec.mainClass="com.example.hazelcast.HazelcastDemo"
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/hazelcast-study-1.0.0.jar
```

### 3. Run Tests

```bash
mvn test
```

## Demo Features Explained

### 1. Distributed Map
- Basic put/get operations
- Atomic operations (putIfAbsent, replace)
- Size tracking

### 2. Distributed Queue
- FIFO queue operations
- Offer and poll operations
- Size management

### 3. Distributed Topic (Pub/Sub)
- Message publishing
- Message listeners
- Asynchronous messaging

### 4. Distributed Lock
- CP subsystem locks
- Timeout-based acquisition
- Proper lock release

### 5. Distributed Executor Service
- Task submission
- Distributed execution
- Result retrieval

### 6. Advanced Map Features
- **Entry Processors**: Atomic operations on map entries
- **Predicates**: Complex filtering queries
- **Aggregations**: Count, average, max, sum operations
- **TTL**: Time-to-live for entries
- **Indexes**: Performance optimization for queries

## Configuration

The project includes several configuration examples:

### Development Configuration
```java
HazelcastInstance instance = HazelcastConfig.createDevInstance();
```

### Production Configuration
```java
HazelcastInstance instance = HazelcastConfig.createConfiguredInstance();
```

### Client Configuration
```java
ClientConfig clientConfig = HazelcastConfig.createClientConfig();
HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
```

## Key Hazelcast Concepts Demonstrated

### 1. Clustering
- Automatic member discovery via multicast
- Cluster formation and management
- Client-server architecture

### 2. Data Distribution
- Partitioned data storage
- Backup replication
- Load balancing

### 3. Consistency
- CP subsystem for strong consistency
- Eventual consistency for performance
- Atomic operations

### 4. Scalability
- Horizontal scaling
- Partition-based distribution
- Automatic rebalancing

## Advanced Features

### Entry Processors
```java
EntryProcessor<String, User, Integer> processor = entry -> {
    User user = entry.getValue();
    user.setAge(user.getAge() + 1);
    entry.setValue(user);
    return user.getAge();
};
Map<String, Object> results = map.executeOnEntries(processor);
```

### Predicates
```java
Predicate<String, User> predicate = Predicates.and(
    Predicates.greaterThan("age", 25),
    Predicates.like("email", "%@example.com")
);
Map<String, User> results = map.entrySet(predicate);
```

### Aggregations
```java
Double avgAge = map.aggregate(Aggregators.doubleAvg("age"));
Long count = map.aggregate(Aggregators.count());
Integer maxAge = map.aggregate(Aggregators.integerMax("age"));
```

## Monitoring and Logging

The project includes comprehensive logging configuration:

- Console output for development
- File logging with rotation
- Hazelcast-specific log levels
- Application-specific logging

Logs are written to:
- Console (for immediate feedback)
- `logs/hazelcast-study.log` (for persistence)

## Performance Considerations

### 1. Serialization
- Use `Serializable` for simple objects
- Consider `DataSerializable` for performance-critical applications
- Use `IdentifiedDataSerializable` for versioning

### 2. Indexing
- Add indexes for frequently queried fields
- Balance between query performance and storage overhead
- Use appropriate index types (HASH, SORTED, BITMAP)

### 3. Eviction
- Configure appropriate eviction policies
- Set reasonable TTL values
- Monitor memory usage

## Troubleshooting

### Common Issues

1. **Port Conflicts**: Change the default port (5701) in configuration
2. **Multicast Issues**: Use TCP/IP join mechanism for restricted networks
3. **Memory Issues**: Adjust heap size and eviction policies
4. **Network Issues**: Check firewall settings and network connectivity

### Debug Mode

Enable debug logging by modifying `logback.xml`:

```xml
<logger name="com.example.hazelcast" level="DEBUG"/>
```

## Next Steps

To extend this project, consider:

1. **Persistence**: Add Hazelcast persistence for data durability
2. **Security**: Implement authentication and authorization
3. **Monitoring**: Add metrics and health checks
4. **Integration**: Connect with external systems (databases, message queues)
5. **Cloud Deployment**: Deploy to cloud platforms (AWS, Azure, GCP)

## Resources

- [Hazelcast Documentation](https://docs.hazelcast.com/)
- [Hazelcast Java API](https://docs.hazelcast.com/hazelcast/latest/apis/java-api.html)
- [Hazelcast GitHub](https://github.com/hazelcast/hazelcast)
- [Hazelcast Community](https://hazelcast.com/community/)

## License

This project is for educational purposes. Hazelcast is licensed under the Apache License 2.0. 