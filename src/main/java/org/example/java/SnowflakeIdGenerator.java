package org.example.java;

/**
 * Snowflake ID Generator
 * 
 * Twitter's Snowflake algorithm for generating unique IDs in a distributed system.
 * 
 * ID Structure (64 bits):
 * - 1 bit: Unused (sign bit, always 0 for positive numbers)
 * - 41 bits: Timestamp (milliseconds since custom epoch)
 * - 10 bits: Machine/Node ID (0-1023)
 * - 12 bits: Sequence number (0-4095)
 * 
 * Features:
 * - Thread-safe
 * - Generates up to 4096 IDs per millisecond per node
 * - Can generate IDs for ~69 years from custom epoch
 * - No external dependencies
 * 
 * Usage:
 * SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1L, 1L);
 * long id = generator.nextId();
 */
public class SnowflakeIdGenerator {
    
    // ============================================
    // Constants
    // ============================================
    
    /**
     * Custom epoch (2020-01-01 00:00:00 UTC)
     * You can adjust this to extend the lifetime
     */
    private static final long EPOCH = 1577836800000L; // 2020-01-01 00:00:00 UTC
    
    /**
     * Bits allocation
     */
    private static final long SEQUENCE_BITS = 12L;
    private static final long MACHINE_ID_BITS = 10L;
    
    /**
     * Maximum values
     */
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 4095
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1; // 1023
    
    /**
     * Bit shifts for packing
     */
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS; // 12
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS; // 22
    
    // ============================================
    // Instance Variables
    // ============================================
    
    private final long machineId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    
    // Thread safety
    private final Object lock = new Object();
    
    // ============================================
    // Constructor
    // ============================================
    
    /**
     * Create a Snowflake ID generator
     * 
     * @param machineId Machine/Node ID (0-1023)
     * @param datacenterId Datacenter ID (0-1023)
     * @throws IllegalArgumentException if IDs are out of range
     */
    public SnowflakeIdGenerator(long machineId, long datacenterId) {
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException(
                String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        if (datacenterId > MAX_MACHINE_ID || datacenterId < 0) {
            throw new IllegalArgumentException(
                String.format("Datacenter ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        
        this.machineId = machineId;
        this.datacenterId = datacenterId;
    }
    
    /**
     * Create generator with single ID (combines machine and datacenter)
     * 
     * @param nodeId Combined node ID (0-1023)
     */
    public SnowflakeIdGenerator(long nodeId) {
        this(nodeId, 0L);
    }
    
    // ============================================
    // Core Methods
    // ============================================
    
    /**
     * Generate next unique ID
     * 
     * @return Next unique ID
     * @throws RuntimeException if clock moves backwards
     */
    public long nextId() {
        synchronized (lock) {
            long timestamp = currentTimestamp();
            
            // Handle clock moving backwards
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate ID for %d milliseconds",
                        lastTimestamp - timestamp));
            }
            
            // Same millisecond - increment sequence
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                
                // Sequence overflow - wait for next millisecond
                if (sequence == 0) {
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                // New millisecond - reset sequence
                sequence = 0L;
            }
            
            lastTimestamp = timestamp;
            
            // Pack into 64-bit ID
            return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                 | (datacenterId << MACHINE_ID_SHIFT)
                 | (machineId << SEQUENCE_BITS)
                 | sequence;
        }
    }
    
    /**
     * Get current timestamp in milliseconds
     */
    private long currentTimestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * Wait until next millisecond
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }
        return timestamp;
    }
    
    // ============================================
    // Utility Methods
    // ============================================
    
    /**
     * Parse ID to extract components
     */
    public static IdComponents parseId(long id) {
        long sequence = id & MAX_SEQUENCE;
        long machineId = (id >> SEQUENCE_BITS) & MAX_MACHINE_ID;
        long datacenterId = (id >> MACHINE_ID_SHIFT) & MAX_MACHINE_ID;
        long timestamp = (id >> TIMESTAMP_SHIFT) + EPOCH;
        
        return new IdComponents(timestamp, datacenterId, machineId, sequence);
    }
    
    /**
     * Get timestamp from ID
     */
    public static long getTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }
    
    /**
     * Get machine ID from ID
     */
    public static long getMachineId(long id) {
        return (id >> SEQUENCE_BITS) & MAX_MACHINE_ID;
    }
    
    /**
     * Get datacenter ID from ID
     */
    public static long getDatacenterId(long id) {
        return (id >> MACHINE_ID_SHIFT) & MAX_MACHINE_ID;
    }
    
    /**
     * Get sequence from ID
     */
    public static long getSequence(long id) {
        return id & MAX_SEQUENCE;
    }
    
    // ============================================
    // ID Components Class
    // ============================================
    
    /**
     * Represents the components of a Snowflake ID
     */
    public static class IdComponents {
        private final long timestamp;
        private final long datacenterId;
        private final long machineId;
        private final long sequence;
        
        public IdComponents(long timestamp, long datacenterId, long machineId, long sequence) {
            this.timestamp = timestamp;
            this.datacenterId = datacenterId;
            this.machineId = machineId;
            this.sequence = sequence;
        }
        
        public long getTimestamp() { return timestamp; }
        public long getDatacenterId() { return datacenterId; }
        public long getMachineId() { return machineId; }
        public long getSequence() { return sequence; }
        
        @Override
        public String toString() {
            return String.format(
                "IdComponents{timestamp=%d, datacenterId=%d, machineId=%d, sequence=%d}",
                timestamp, datacenterId, machineId, sequence);
        }
    }
    
    // ============================================
    // Main Method - Testing
    // ============================================
    
    public static void main(String[] args) {
        System.out.println("=== Snowflake ID Generator ===\n");
        
        // Create generator
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1L, 1L);
        
        // Generate IDs
        System.out.println("Generating 10 IDs:");
        for (int i = 0; i < 10; i++) {
            long id = generator.nextId();
            System.out.println("ID " + (i + 1) + ": " + id);
        }
        System.out.println();
        
        // Parse ID
        System.out.println("=== Parsing ID ===");
        long testId = generator.nextId();
        System.out.println("Generated ID: " + testId);
        
        IdComponents components = parseId(testId);
        System.out.println("Parsed components:");
        System.out.println("  Timestamp: " + components.getTimestamp() + 
                         " (" + new java.util.Date(components.getTimestamp()) + ")");
        System.out.println("  Datacenter ID: " + components.getDatacenterId());
        System.out.println("  Machine ID: " + components.getMachineId());
        System.out.println("  Sequence: " + components.getSequence());
        System.out.println();
        
        // Test uniqueness
        System.out.println("=== Testing Uniqueness ===");
        int count = 10000;
        java.util.Set<Long> ids = new java.util.HashSet<>();
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < count; i++) {
            ids.add(generator.nextId());
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Generated " + count + " IDs");
        System.out.println("Unique IDs: " + ids.size());
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        System.out.println("All unique: " + (ids.size() == count));
        System.out.println();
        
        // Test multiple generators (different nodes)
        System.out.println("=== Testing Multiple Nodes ===");
        SnowflakeIdGenerator node1 = new SnowflakeIdGenerator(1L, 1L);
        SnowflakeIdGenerator node2 = new SnowflakeIdGenerator(2L, 1L);
        SnowflakeIdGenerator node3 = new SnowflakeIdGenerator(1L, 2L);
        
        System.out.println("Node 1 (machine=1, datacenter=1): " + node1.nextId());
        System.out.println("Node 2 (machine=2, datacenter=1): " + node2.nextId());
        System.out.println("Node 3 (machine=1, datacenter=2): " + node3.nextId());
        System.out.println();
        
        // Performance test
        System.out.println("=== Performance Test ===");
        int iterations = 1000000;
        startTime = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            generator.nextId();
        }
        
        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double idsPerSecond = (iterations * 1000.0) / duration;
        
        System.out.println("Generated " + iterations + " IDs in " + duration + " ms");
        System.out.println("Throughput: " + String.format("%.2f", idsPerSecond) + " IDs/second");
        System.out.println();
        
        // Test ID structure
        System.out.println("=== ID Structure Analysis ===");
        long id = generator.nextId();
        System.out.println("Sample ID: " + id);
        System.out.println("Binary: " + Long.toBinaryString(id));
        System.out.println("Hex: " + Long.toHexString(id));
        System.out.println();
        
        // Test concurrent generation
        System.out.println("=== Concurrent Generation Test ===");
        final SnowflakeIdGenerator concurrentGen = new SnowflakeIdGenerator(5L, 5L);
        final java.util.Set<Long> concurrentIds = java.util.Collections.synchronizedSet(
            new java.util.HashSet<>());
        
        int threadCount = 10;
        int idsPerThread = 1000;
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < idsPerThread; j++) {
                    concurrentIds.add(concurrentGen.nextId());
                }
            });
        }
        
        startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        endTime = System.currentTimeMillis();
        
        System.out.println("Threads: " + threadCount);
        System.out.println("IDs per thread: " + idsPerThread);
        System.out.println("Total IDs generated: " + concurrentIds.size());
        System.out.println("Expected: " + (threadCount * idsPerThread));
        System.out.println("All unique: " + (concurrentIds.size() == threadCount * idsPerThread));
        System.out.println("Time: " + (endTime - startTime) + " ms");
    }
}
