package com.example.hazelcast;

import com.hazelcast.partition.PartitionAware;

public class HazelcastPartitionAwareKey implements PartitionAware {

    private String val;
    private int partitionKey;

    public HazelcastPartitionAwareKey(String val, int partitionKey) {
        this.val = val;
        this.partitionKey = partitionKey;
    }

    public String getVal() {
        return val;
    }

    @Override
    public Object getPartitionKey() {
        return partitionKey;
    }
}
