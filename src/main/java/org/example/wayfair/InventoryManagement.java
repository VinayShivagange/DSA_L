package org.example.wayfair;

import java.util.*;

/**
 * Inventory Management: Managing state across arrays
 * 
 * Common problems:
 * - Given arrivals and departures, what is the max inventory needed?
 * - Track inventory levels over time
 * - Find time periods with maximum inventory
 * - Merge intervals for inventory tracking
 */
public class InventoryManagement {
    
    /**
     * Problem: Given arrivals and departures, find max inventory needed
     * 
     * Approach: Sort both arrays and use two pointers
     * Time: O(n log n), Space: O(1)
     */
    public static int maxInventoryNeeded(int[] arrivals, int[] departures) {
        if (arrivals == null || departures == null || 
            arrivals.length != departures.length) {
            return 0;
        }
        
        // Sort both arrays
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        int maxInventory = 0;
        int currentInventory = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                // New arrival
                currentInventory++;
                maxInventory = Math.max(maxInventory, currentInventory);
                i++;
            } else {
                // Departure
                currentInventory--;
                j++;
            }
        }
        
        // Handle remaining arrivals
        while (i < arrivals.length) {
            currentInventory++;
            maxInventory = Math.max(maxInventory, currentInventory);
            i++;
        }
        
        return maxInventory;
    }
    
    /**
     * Find max inventory with time tracking
     * Returns both max inventory and the time when it occurs
     */
    public static class InventoryResult {
        int maxInventory;
        int time;
        
        InventoryResult(int maxInventory, int time) {
            this.maxInventory = maxInventory;
            this.time = time;
        }
    }
    
    public static InventoryResult maxInventoryWithTime(int[] arrivals, int[] departures) {
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        int maxInventory = 0;
        int maxTime = 0;
        int currentInventory = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                currentInventory++;
                if (currentInventory > maxInventory) {
                    maxInventory = currentInventory;
                    maxTime = arrivals[i];
                }
                i++;
            } else {
                currentInventory--;
                j++;
            }
        }
        
        while (i < arrivals.length) {
            currentInventory++;
            if (currentInventory > maxInventory) {
                maxInventory = currentInventory;
                maxTime = arrivals[i];
            }
            i++;
        }
        
        return new InventoryResult(maxInventory, maxTime);
    }
    
    /**
     * Track inventory levels at all time points
     * Returns a map of time -> inventory level
     */
    public static Map<Integer, Integer> inventoryOverTime(int[] arrivals, int[] departures) {
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        Map<Integer, Integer> inventoryMap = new TreeMap<>();
        int currentInventory = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                currentInventory++;
                inventoryMap.put(arrivals[i], currentInventory);
                i++;
            } else {
                currentInventory--;
                inventoryMap.put(departures[j], currentInventory);
                j++;
            }
        }
        
        while (i < arrivals.length) {
            currentInventory++;
            inventoryMap.put(arrivals[i], currentInventory);
            i++;
        }
        
        while (j < departures.length) {
            currentInventory--;
            inventoryMap.put(departures[j], currentInventory);
            j++;
        }
        
        return inventoryMap;
    }
    
    /**
     * Find all time periods where inventory exceeds a threshold
     */
    public static List<int[]> periodsAboveThreshold(
            int[] arrivals, int[] departures, int threshold) {
        
        Map<Integer, Integer> inventoryMap = inventoryOverTime(arrivals, departures);
        List<int[]> periods = new ArrayList<>();
        
        int periodStart = -1;
        List<Integer> times = new ArrayList<>(inventoryMap.keySet());
        
        for (int i = 0; i < times.size(); i++) {
            int time = times.get(i);
            int inventory = inventoryMap.get(time);
            
            if (inventory >= threshold) {
                if (periodStart == -1) {
                    periodStart = time;
                }
            } else {
                if (periodStart != -1) {
                    // End of period
                    int periodEnd = i > 0 ? times.get(i - 1) : time;
                    periods.add(new int[]{periodStart, periodEnd});
                    periodStart = -1;
                }
            }
        }
        
        // Handle case where period extends to end
        if (periodStart != -1) {
            int lastTime = times.get(times.size() - 1);
            periods.add(new int[]{periodStart, lastTime});
        }
        
        return periods;
    }
    
    /**
     * Multiple warehouses: Track inventory across multiple locations
     */
    public static int maxInventoryAcrossWarehouses(
            int[][] arrivals, // arrivals[i] = arrivals for warehouse i
            int[][] departures) {
        
        int maxTotal = 0;
        
        for (int w = 0; w < arrivals.length; w++) {
            int warehouseMax = maxInventoryNeeded(arrivals[w], departures[w]);
            maxTotal = Math.max(maxTotal, warehouseMax);
        }
        
        return maxTotal;
    }
    
    /**
     * Inventory with capacity constraints
     * Returns if all arrivals can be accommodated
     */
    public static boolean canAccommodateAll(
            int[] arrivals, int[] departures, int capacity) {
        
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        int currentInventory = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                currentInventory++;
                if (currentInventory > capacity) {
                    return false;
                }
                i++;
            } else {
                currentInventory--;
                j++;
            }
        }
        
        while (i < arrivals.length) {
            currentInventory++;
            if (currentInventory > capacity) {
                return false;
            }
            i++;
        }
        
        return true;
    }
    
    /**
     * Find minimum capacity needed to accommodate all arrivals
     */
    public static int minCapacityNeeded(int[] arrivals, int[] departures) {
        return maxInventoryNeeded(arrivals, departures);
    }
    
    /**
     * Inventory with item types
     * Track inventory for different product categories
     */
    public static Map<String, Integer> maxInventoryByCategory(
            int[] arrivals, int[] departures, String[] categories) {
        
        Map<String, List<Integer>> categoryArrivals = new HashMap<>();
        Map<String, List<Integer>> categoryDepartures = new HashMap<>();
        
        // Group by category
        for (int i = 0; i < arrivals.length; i++) {
            String category = categories[i];
            categoryArrivals.computeIfAbsent(category, k -> new ArrayList<>()).add(arrivals[i]);
            categoryDepartures.computeIfAbsent(category, k -> new ArrayList<>()).add(departures[i]);
        }
        
        Map<String, Integer> maxInventory = new HashMap<>();
        
        for (String category : categoryArrivals.keySet()) {
            List<Integer> arr = categoryArrivals.get(category);
            List<Integer> dep = categoryDepartures.get(category);
            
            int[] arrArray = arr.stream().mapToInt(Integer::intValue).toArray();
            int[] depArray = dep.stream().mapToInt(Integer::intValue).toArray();
            
            maxInventory.put(category, maxInventoryNeeded(arrArray, depArray));
        }
        
        return maxInventory;
    }
    
    public static void main(String[] args) {
        System.out.println("=== Inventory Management ===");
        
        // Test 1: Basic max inventory
        int[] arrivals1 = {1, 2, 3, 4, 5};
        int[] departures1 = {3, 4, 5, 6, 7};
        System.out.println("\n1. Max Inventory Needed:");
        System.out.println("Arrivals: " + Arrays.toString(arrivals1));
        System.out.println("Departures: " + Arrays.toString(departures1));
        System.out.println("Max Inventory: " + maxInventoryNeeded(arrivals1, departures1));
        System.out.println("Expected: 5 (at time 5, all items are present)");
        
        // Test 2: Max inventory with time
        InventoryResult result = maxInventoryWithTime(arrivals1, departures1);
        System.out.println("\n2. Max Inventory with Time:");
        System.out.println("Max Inventory: " + result.maxInventory + " at time " + result.time);
        
        // Test 3: Inventory over time
        System.out.println("\n3. Inventory Over Time:");
        Map<Integer, Integer> inventoryMap = inventoryOverTime(arrivals1, departures1);
        inventoryMap.forEach((time, inv) -> 
            System.out.println("Time " + time + ": " + inv + " items"));
        
        // Test 4: Periods above threshold
        System.out.println("\n4. Periods Above Threshold (>= 3):");
        List<int[]> periods = periodsAboveThreshold(arrivals1, departures1, 3);
        for (int[] period : periods) {
            System.out.println("Period: [" + period[0] + ", " + period[1] + "]");
        }
        
        // Test 5: Capacity check
        System.out.println("\n5. Capacity Check:");
        System.out.println("Can accommodate with capacity 5: " + 
            canAccommodateAll(arrivals1, departures1, 5));
        System.out.println("Can accommodate with capacity 4: " + 
            canAccommodateAll(arrivals1, departures1, 4));
        System.out.println("Min capacity needed: " + 
            minCapacityNeeded(arrivals1, departures1));
        
        // Test 6: Multiple warehouses
        int[][] multiArrivals = {{1, 2, 3}, {2, 3, 4}};
        int[][] multiDepartures = {{4, 5, 6}, {5, 6, 7}};
        System.out.println("\n6. Multiple Warehouses:");
        System.out.println("Max across warehouses: " + 
            maxInventoryAcrossWarehouses(multiArrivals, multiDepartures));
        
        // Test 7: Inventory by category
        int[] arrivals2 = {1, 2, 3};
        int[] departures2 = {5, 6, 7};
        String[] categories = {"electronics", "clothing", "electronics"};
        System.out.println("\n7. Inventory by Category:");
        Map<String, Integer> categoryMax = maxInventoryByCategory(
            arrivals2, departures2, categories);
        categoryMax.forEach((cat, max) -> 
            System.out.println(cat + ": " + max));
    }
}
