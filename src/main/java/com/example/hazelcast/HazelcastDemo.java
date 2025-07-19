package com.example.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.collection.IQueue;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Main demo class showcasing Hazelcast features
 */
public class HazelcastDemo {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastDemo.class);

    public static void main(String[] args) {
        logger.info("Starting Hazelcast Demo...");

        // Start a Hazelcast server instance
        HazelcastInstance server = Hazelcast.newHazelcastInstance();
        logger.info("Hazelcast server started");

        // Create a client to connect to the server
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        logger.info("Hazelcast client connected");

        try {
            // Demo 1: Distributed Map
            demoDistributedMap(client);

            // Demo 2: Distributed Queue
            demoDistributedQueue(client);

            // Demo 3: Distributed Topic (Pub/Sub)
            demoDistributedTopic(client);

            // Demo 4: Distributed Lock
            demoDistributedLock(client);

            // Demo 5: Distributed Executor Service
            demoDistributedExecutor(client);

        } catch (Exception e) {
            logger.error("Error during demo execution", e);
        } finally {
            // Cleanup
            client.shutdown();
            server.shutdown();
            logger.info("Hazelcast instances shut down");
        }
    }

    private static void demoDistributedMap(HazelcastInstance client) {
        logger.info("=== Demo 1: Distributed Map ===");
        
        IMap<String, String> map = client.getMap("demo-map");
        
        // Put some data
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        
        logger.info("Put 3 entries into distributed map");
        
        // Get data
        String value1 = map.get("key1");
        logger.info("Retrieved value for key1: {}", value1);
        
        // Check size
        logger.info("Map size: {}", map.size());
        
        // Use atomic operations
        map.putIfAbsent("key4", "value4");
        map.replace("key1", "value1", "updated-value1");
        
        logger.info("After atomic operations - Map size: {}", map.size());
        logger.info("Updated value for key1: {}", map.get("key1"));
    }

    private static void demoDistributedQueue(HazelcastInstance client) {
        logger.info("=== Demo 2: Distributed Queue ===");
        
        IQueue<String> queue = client.getQueue("demo-queue");
        
        // Add items to queue
        queue.offer("item1");
        queue.offer("item2");
        queue.offer("item3");
        
        logger.info("Added 3 items to distributed queue");
        logger.info("Queue size: {}", queue.size());
        
        // Poll items from queue
        String item1 = queue.poll();
        String item2 = queue.poll();
        
        logger.info("Polled items: {}, {}", item1, item2);
        logger.info("Remaining queue size: {}", queue.size());
    }

    private static void demoDistributedTopic(HazelcastInstance client) {
        logger.info("=== Demo 3: Distributed Topic (Pub/Sub) ===");
        
        ITopic<String> topic = client.getTopic("demo-topic");
        
        // Add a message listener
        var listenerId = topic.addMessageListener(new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> message) {
                logger.info("Received message: {}", message.getMessageObject());
            }
        });
        
        // Publish messages
        topic.publish("Hello from publisher!");
        topic.publish("Another message");
        topic.publish("Final message");
        
        logger.info("Published 3 messages to topic");
        
        // Remove listener
        topic.removeMessageListener(listenerId);
    }

    private static void demoDistributedLock(HazelcastInstance client) {
        logger.info("=== Demo 4: Distributed Lock ===");
        
        var lock = client.getCPSubsystem().getLock("demo-lock");
        
        try {
            // Try to acquire lock
            boolean acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (acquired) {
                logger.info("Successfully acquired distributed lock");
                
                // Simulate some work
                Thread.sleep(1000);
                
                // Release lock
                lock.unlock();
                logger.info("Released distributed lock");
            } else {
                logger.warn("Failed to acquire distributed lock within timeout");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while working with lock", e);
        }
    }

    private static void demoDistributedExecutor(HazelcastInstance client) {
        logger.info("=== Demo 5: Distributed Executor Service ===");
        
        var executor = client.getExecutorService("demo-executor");
        
        // Submit a simple task using a serializable callable
        var future = executor.submit(new SimpleTask());
        
        try {
            String result = future.get(10, TimeUnit.SECONDS);
            logger.info("Distributed task result: {}", result);
        } catch (Exception e) {
            logger.error("Error executing distributed task", e);
        }
    }
    
    // Serializable task for distributed execution
    public static class SimpleTask implements java.util.concurrent.Callable<String>, java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        @Override
        public String call() throws Exception {
            return "Task completed successfully on member: " + 
                   java.util.UUID.randomUUID().toString().substring(0, 8);
        }
    }
} 