package com.example.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.aggregation.Aggregators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates advanced Hazelcast Map features
 */
public class MapExamples {
    private static final Logger logger = LoggerFactory.getLogger(MapExamples.class);

    public static void demonstrateAdvancedMapFeatures(HazelcastInstance client) {
        logger.info("=== Advanced Map Features Demo ===");

        // Create a map with custom objects
        IMap<String, User> userMap = client.getMap("users");

        // Add some users
        userMap.put("user1", new User("John", "Doe", 25, "john@example.com"));
        userMap.put("user2", new User("Jane", "Smith", 30, "jane@example.com"));
        userMap.put("user3", new User("Bob", "Johnson", 35, "bob@example.com"));
        userMap.put("user4", new User("Alice", "Brown", 28, "alice@example.com"));

        logger.info("Added {} users to the map", userMap.size());

        // Demo 1: Entry Processor (atomic operations on entries)
        demonstrateEntryProcessor(userMap);

        // Demo 2: Predicates (filtering)
        demonstratePredicates(userMap);

        // Demo 3: Aggregations
        demonstrateAggregations(userMap);

        // Demo 4: TTL (Time To Live)
        demonstrateTTL(userMap);

        // Demo 5: Map with indexes
        demonstrateIndexedMap(client);
    }

    private static void demonstrateEntryProcessor(IMap<String, User> userMap) {
        logger.info("--- Entry Processor Demo ---");

        // Create an entry processor that updates age
        EntryProcessor<String, User, Object> ageProcessor = entry -> {
            User user = entry.getValue();
            if (user != null) {
                user.setAge(user.getAge() + 1);
                entry.setValue(user);
                return user.getAge();
            }
            return null;
        };

        // Apply to all entries
        Map<String, Object> results = userMap.executeOnEntries(ageProcessor);
        logger.info("Updated ages for {} users", results.size());
        results.forEach((key, value) -> logger.info("User {} new age: {}", key, value));
    }

    private static void demonstratePredicates(IMap<String, User> userMap) {
        logger.info("--- Predicates Demo ---");

        // Find users older than 30
        Predicate<String, User> agePredicate = Predicates.greaterThan("age", 30);
        var olderUsers = userMap.entrySet(agePredicate);
        logger.info("Found {} users older than 30", olderUsers.size());

        // Find users with specific email domain
        Predicate<String, User> emailPredicate = Predicates.like("email", "%@example.com");
        var exampleUsers = userMap.entrySet(emailPredicate);
        logger.info("Found {} users with @example.com email", exampleUsers.size());

        // Complex predicate: age between 25 and 35 AND email contains 'john'
        Predicate<String, User> complexPredicate = Predicates.and(
            Predicates.between("age", 25, 35),
            Predicates.like("email", "%john%")
        );
        var complexResults = userMap.entrySet(complexPredicate);
        logger.info("Found {} users matching complex criteria", complexResults.size());
    }

    private static void demonstrateAggregations(IMap<String, User> userMap) {
        logger.info("--- Aggregations Demo ---");

        // Calculate average age
        Double avgAge = userMap.aggregate(Aggregators.doubleAvg("age"));
        logger.info("Average age: {}", avgAge);

        // Calculate maximum age
        Integer maxAge = userMap.aggregate(Aggregators.integerMax("age"));
        logger.info("Maximum age: {}", maxAge);

        // Count users
        Long userCount = userMap.aggregate(Aggregators.count());
        logger.info("Total users: {}", userCount);

        // Sum of all ages
        Long totalAge = userMap.aggregate(Aggregators.longSum("age"));
        logger.info("Sum of all ages: {}", totalAge);
    }

    private static void demonstrateTTL(IMap<String, User> userMap) {
        logger.info("--- TTL Demo ---");

        // Add a user with 5 seconds TTL
        userMap.put("temp-user", new User("Temp", "User", 25, "temp@example.com"), 5, TimeUnit.SECONDS);
        logger.info("Added temporary user with 5-second TTL");

        // Check if user exists
        boolean exists = userMap.containsKey("temp-user");
        logger.info("Temporary user exists: {}", exists);

        // Wait for TTL to expire
        try {
            Thread.sleep(6000);
            exists = userMap.containsKey("temp-user");
            logger.info("After 6 seconds, temporary user exists: {}", exists);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for TTL", e);
        }
    }

    private static void demonstrateIndexedMap(HazelcastInstance client) {
        logger.info("--- Indexed Map Demo ---");

        // Create a new map for indexing demo
        IMap<String, User> indexedMap = client.getMap("indexed-users");

        // Add some data
        indexedMap.put("idx1", new User("Indexed", "User1", 25, "idx1@example.com"));
        indexedMap.put("idx2", new User("Indexed", "User2", 30, "idx2@example.com"));
        indexedMap.put("idx3", new User("Indexed", "User3", 35, "idx3@example.com"));

        // Note: In a real application, you would configure indexes in the Hazelcast configuration
        // For this demo, we'll just show how queries work
        Predicate<String, User> ageQuery = Predicates.greaterThan("age", 30);
        var results = indexedMap.entrySet(ageQuery);
        logger.info("Found {} users older than 30 in indexed map", results.size());
    }

    // Inner class for demo data
    public static class User {
        private String firstName;
        private String lastName;
        private int age;
        private String email;

        public User(String firstName, String lastName, int age, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.email = email;
        }

        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return String.format("User{firstName='%s', lastName='%s', age=%d, email='%s'}", 
                               firstName, lastName, age, email);
        }
    }
} 