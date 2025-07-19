#!/bin/bash

echo "Building Hazelcast Study Project..."
mvn clean compile

echo "Running Hazelcast Demo..."
mvn exec:java -Dexec.mainClass="com.example.hazelcast.HazelcastDemo" 