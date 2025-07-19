package com.example.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hazelcast configuration examples
 */
public class HazelcastConfig {
    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);

    /**
     * Creates a Hazelcast instance with custom configuration
     */
    public static HazelcastInstance createConfiguredInstance() {
        Config config = new Config();
        
        // Set cluster name
        config.setClusterName("hazelcast-study-cluster");
        
        // Configure network
        config.getNetworkConfig()
              .setPort(5701)
              .setPortAutoIncrement(true)
              .setPortCount(100);
        
        // Configure join mechanism (multicast)
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
        
        // Configure maps
        configureMaps(config);
        
        // Configure queues
        configureQueues(config);
        
        // Configure topics
        configureTopics(config);
        
        // Configure executor service
        configureExecutorService(config);
        
        // Configure CP subsystem
        configureCPSubsystem(config);
        
        logger.info("Creating Hazelcast instance with custom configuration");
        return Hazelcast.newHazelcastInstance(config);
    }

    private static void configureMaps(Config config) {
        // Configure a specific map
        MapConfig userMapConfig = new MapConfig("users");
        userMapConfig.setBackupCount(1);
        userMapConfig.setAsyncBackupCount(0);
        userMapConfig.setTimeToLiveSeconds(3600); // 1 hour TTL
        
        // Configure eviction
        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.PER_NODE);
        evictionConfig.setSize(1000);
        userMapConfig.setEvictionConfig(evictionConfig);
        
        // Configure indexes
        userMapConfig.addIndexConfig(new IndexConfig(IndexType.HASH, "age"));
        userMapConfig.addIndexConfig(new IndexConfig(IndexType.HASH, "email"));
        
        config.addMapConfig(userMapConfig);
        
        // Configure another map with different settings
        MapConfig cacheMapConfig = new MapConfig("cache");
        cacheMapConfig.setBackupCount(0);
        cacheMapConfig.setTimeToLiveSeconds(300); // 5 minutes TTL
        config.addMapConfig(cacheMapConfig);
    }

    private static void configureQueues(Config config) {
        QueueConfig queueConfig = new QueueConfig("demo-queue");
        queueConfig.setMaxSize(1000);
        queueConfig.setBackupCount(1);
        queueConfig.setAsyncBackupCount(0);
        config.addQueueConfig(queueConfig);
    }

    private static void configureTopics(Config config) {
        TopicConfig topicConfig = new TopicConfig("demo-topic");
        topicConfig.setGlobalOrderingEnabled(true);
        config.addTopicConfig(topicConfig);
    }

    private static void configureExecutorService(Config config) {
        ExecutorConfig executorConfig = new ExecutorConfig("demo-executor", 2);
        executorConfig.setQueueCapacity(100);
        config.addExecutorConfig(executorConfig);
    }

    private static void configureCPSubsystem(Config config) {
        // CP Subsystem configuration - simplified for compatibility
        // In Hazelcast 5.2.2, some CP subsystem features may not be available
        logger.info("CP Subsystem configuration skipped for compatibility");
    }

    /**
     * Creates a Hazelcast client configuration
     */
    public static com.hazelcast.client.config.ClientConfig createClientConfig() {
        com.hazelcast.client.config.ClientConfig clientConfig = new com.hazelcast.client.config.ClientConfig();
        
        clientConfig.setClusterName("hazelcast-study-cluster");
        clientConfig.setInstanceName("hazelcast-study-client");
        
        // Configure client network
        clientConfig.getNetworkConfig()
                   .addAddress("127.0.0.1:5701")
                   .setConnectionTimeout(5000);
        
        // Configure client properties
        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "10000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "60000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "120");
        
        return clientConfig;
    }

    /**
     * Creates a Hazelcast instance with minimal configuration for development
     */
    public static HazelcastInstance createDevInstance() {
        Config config = new Config();
        config.setClusterName("dev");
        
        // Enable multicast for easy development
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
        
        // Disable backups for development
        config.getMapConfigs().values().forEach(mapConfig -> {
            mapConfig.setBackupCount(0);
            mapConfig.setAsyncBackupCount(0);
        });
        
        logger.info("Creating Hazelcast instance with development configuration");
        return Hazelcast.newHazelcastInstance(config);
    }
} 