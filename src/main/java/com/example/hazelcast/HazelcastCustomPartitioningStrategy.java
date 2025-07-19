package com.example.hazelcast;

import com.hazelcast.partition.PartitioningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class HazelcastCustomPartitioningStrategy implements PartitioningStrategy, Serializable {
    
    private static final Logger logger = LoggerFactory.getLogger(HazelcastCustomPartitioningStrategy.class);
    private static final long serialVersionUID = 1L;

    @Override
    public Object getPartitionKey(Object o) {
        return 1;
//        logger.info("Custom partitioning strategy called with object: {}", o);
//
//        // Simple strategy: use the object's hash code modulo 271 (number of partitions)
//        if (o != null) {
//            int partitionKey = Math.abs(o.hashCode() % 271);
//            logger.info("Calculated partition key: {} for object: {}", partitionKey, o);
//            return partitionKey;
//        }
//
//        logger.info("Object is null, returning default partition key: 0");
//        return 0;
    }
}
