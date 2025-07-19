package com.example.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.collection.IQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class HazelcastDemoTest {
    
    private HazelcastInstance server;
    private HazelcastInstance client;

    @BeforeEach
    void setUp() {
        // Start server
        server = Hazelcast.newHazelcastInstance();
        
        // Start client
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");
        client = HazelcastClient.newHazelcastClient(clientConfig);
    }

    @AfterEach
    void tearDown() {
        if (client != null) {
            client.shutdown();
        }
        if (server != null) {
            server.shutdown();
        }
    }

    @Test
    void testDistributedMap() {
        IMap<String, String> map = client.getMap("test-map");
        
        // Test basic operations
        map.put("key1", "value1");
        assertEquals("value1", map.get("key1"));
        assertEquals(1, map.size());
        
        // Test atomic operations
        assertNull(map.putIfAbsent("key2", "value2")); // Should return null if key didn't exist
        assertEquals("value1", map.putIfAbsent("key1", "new-value")); // Should return existing value
        assertEquals("value1", map.get("key1"));
        
        // Test replace
        assertTrue(map.replace("key1", "value1", "updated-value"));
        assertEquals("updated-value", map.get("key1"));
    }

    @Test
    void testDistributedQueue() {
        IQueue<String> queue = client.getQueue("test-queue");
        
        // Test offer and poll
        assertTrue(queue.offer("item1"));
        assertTrue(queue.offer("item2"));
        assertEquals(2, queue.size());
        
        assertEquals("item1", queue.poll());
        assertEquals("item2", queue.poll());
        assertNull(queue.poll());
        assertEquals(0, queue.size());
    }

    @Test
    void testMapWithCustomObjects() {
        IMap<String, MapExamples.User> userMap = client.getMap("test-users");
        
        MapExamples.User user = new MapExamples.User("John", "Doe", 25, "john@example.com");
        userMap.put("user1", user);
        
        MapExamples.User retrievedUser = userMap.get("user1");
        assertNotNull(retrievedUser);
        assertEquals("John", retrievedUser.getFirstName());
        assertEquals("Doe", retrievedUser.getLastName());
        assertEquals(25, retrievedUser.getAge());
        assertEquals("john@example.com", retrievedUser.getEmail());
    }

    @Test
    void testMapPredicates() {
        IMap<String, MapExamples.User> userMap = client.getMap("test-predicates");
        
        // Add test data
        userMap.put("user1", new MapExamples.User("John", "Doe", 25, "john@example.com"));
        userMap.put("user2", new MapExamples.User("Jane", "Smith", 30, "jane@example.com"));
        userMap.put("user3", new MapExamples.User("Bob", "Johnson", 35, "bob@example.com"));
        
        // Test age predicate
        var olderUsers = userMap.entrySet(
            com.hazelcast.query.Predicates.greaterThan("age", 30)
        );
        assertEquals(1, olderUsers.size());
        // Check if user3 is in the results by looking for its entry
        boolean foundUser3 = olderUsers.stream()
            .anyMatch(entry -> "user3".equals(entry.getKey()));
        assertTrue(foundUser3);
        
        // Test email predicate
        var exampleUsers = userMap.entrySet(
            com.hazelcast.query.Predicates.like("email", "%@example.com")
        );
        assertEquals(3, exampleUsers.size());
    }

    @Test
    void testMapAggregations() {
        IMap<String, MapExamples.User> userMap = client.getMap("test-aggregations");
        
        // Add test data
        userMap.put("user1", new MapExamples.User("John", "Doe", 25, "john@example.com"));
        userMap.put("user2", new MapExamples.User("Jane", "Smith", 30, "jane@example.com"));
        userMap.put("user3", new MapExamples.User("Bob", "Johnson", 35, "bob@example.com"));
        
        // Test count aggregation
        Long count = userMap.aggregate(com.hazelcast.aggregation.Aggregators.count());
        assertEquals(3L, count);
        
        // Test average age
        Double avgAge = userMap.aggregate(com.hazelcast.aggregation.Aggregators.doubleAvg("age"));
        assertEquals(30.0, avgAge, 0.01);
        
        // Test max age
        Integer maxAge = userMap.aggregate(com.hazelcast.aggregation.Aggregators.integerMax("age"));
        assertEquals(35, maxAge);
    }

    @Test
    void testDistributedLock() {
        var lock = client.getCPSubsystem().getLock("test-lock");
        
        try {
            // Test lock acquisition
            assertTrue(lock.tryLock(5, java.util.concurrent.TimeUnit.SECONDS));
            
            // Test that lock is held
            assertFalse(lock.tryLock(1, java.util.concurrent.TimeUnit.MILLISECONDS));
            
            // Release lock
            lock.unlock();
            
            // Test that lock can be acquired again
            assertTrue(lock.tryLock(5, java.util.concurrent.TimeUnit.SECONDS));
            lock.unlock();
            
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
} 