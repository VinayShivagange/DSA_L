package org.example.search;

/**
 * Koko Eating Bananas - Binary Search Problem
 * 
 * Problem: Koko loves to eat bananas. There are n piles of bananas, 
 * the i-th pile has piles[i] bananas. The guards have gone and will come back in h hours.
 * 
 * Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses 
 * some pile of bananas and eats k bananas from that pile. If the pile has less 
 * than k bananas, she eats all of them instead and will not eat any more bananas 
 * during this hour.
 * 
 * Koko wants to finish eating all the bananas before the guards come back.
 * 
 * Return the minimum integer k such that she can eat all the bananas within h hours.
 * 
 * Example 1:
 * Input: piles = [3,6,7,11], h = 8
 * Output: 4
 * Explanation: With speed 4, she can finish in 8 hours:
 * - Pile 3: 3/4 = 1 hour
 * - Pile 6: 6/4 = 2 hours
 * - Pile 7: 7/4 = 2 hours
 * - Pile 11: 11/4 = 3 hours
 * Total: 1+2+2+3 = 8 hours
 * 
 * Example 2:
 * Input: piles = [30,11,23,4,20], h = 5
 * Output: 30
 * 
 * Example 3:
 * Input: piles = [30,11,23,4,20], h = 6
 * Output: 23
 */
public class KokoEatingBananas {
    
    /**
     * Binary Search Solution
     * 
     * Approach:
     * 1. The minimum speed is 1, maximum speed is max(piles)
     * 2. Use binary search to find the minimum valid speed
     * 3. For each speed, check if Koko can finish in h hours
     * 
     * Time: O(n * log(max(piles)))
     * Space: O(1)
     */
    public static int minEatingSpeed(int[] piles, int h) {
        if (piles == null || piles.length == 0 || h < piles.length) {
            return -1; // Invalid input
        }
        
        // Find maximum pile size
        int maxPile = 0;
        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
        }
        
        // Binary search for minimum valid speed
        int left = 1;
        int right = maxPile;
        int result = maxPile;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canFinish(piles, h, mid)) {
                // Can finish with speed mid, try smaller speed
                result = mid;
                right = mid - 1;
            } else {
                // Cannot finish with speed mid, need larger speed
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * Check if Koko can finish all bananas with given speed in h hours
     */
    private static boolean canFinish(int[] piles, int h, int speed) {
        int totalHours = 0;
        
        for (int pile : piles) {
            // Time to finish a pile = ceil(pile / speed)
            // Which is equivalent to (pile + speed - 1) / speed
            totalHours += (pile + speed - 1) / speed;
            
            // Early exit if already exceeds h
            if (totalHours > h) {
                return false;
            }
        }
        
        return totalHours <= h;
    }
    
    /**
     * Alternative: Using Math.ceil (more readable but slightly slower)
     */
    private static boolean canFinishWithCeil(int[] piles, int h, int speed) {
        int totalHours = 0;
        
        for (int pile : piles) {
            totalHours += (int) Math.ceil((double) pile / speed);
            
            if (totalHours > h) {
                return false;
            }
        }
        
        return totalHours <= h;
    }
    
    /**
     * Brute Force Solution (for comparison)
     * Time: O(n * max(piles))
     * Space: O(1)
     */
    public static int minEatingSpeedBruteForce(int[] piles, int h) {
        if (piles == null || piles.length == 0 || h < piles.length) {
            return -1;
        }
        
        int maxPile = 0;
        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
        }
        
        // Try each speed from 1 to maxPile
        for (int speed = 1; speed <= maxPile; speed++) {
            if (canFinish(piles, h, speed)) {
                return speed;
            }
        }
        
        return maxPile;
    }
    
    /**
     * Optimized binary search with better bounds
     * Lower bound: ceil(sum(piles) / h) - minimum theoretical speed
     * Upper bound: max(piles) - maximum needed speed
     */
    public static int minEatingSpeedOptimized(int[] piles, int h) {
        if (piles == null || piles.length == 0 || h < piles.length) {
            return -1;
        }
        
        int maxPile = 0;
        long sum = 0;
        
        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
            sum += pile;
        }
        
        // Lower bound: at minimum, need to eat at this rate
        int left = (int) Math.ceil((double) sum / h);
        int right = maxPile;
        int result = maxPile;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canFinish(piles, h, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println("=== Koko Eating Bananas - Binary Search ===\n");
        
        // Test Case 1
        int[] piles1 = {3, 6, 7, 11};
        int h1 = 8;
        System.out.println("Test 1:");
        System.out.println("Piles: [3, 6, 7, 11], Hours: " + h1);
        System.out.println("Minimum speed: " + minEatingSpeed(piles1, h1));
        System.out.println("Expected: 4");
        System.out.println("Match: " + (minEatingSpeed(piles1, h1) == 4));
        System.out.println();
        
        // Test Case 2
        int[] piles2 = {30, 11, 23, 4, 20};
        int h2 = 5;
        System.out.println("Test 2:");
        System.out.println("Piles: [30, 11, 23, 4, 20], Hours: " + h2);
        System.out.println("Minimum speed: " + minEatingSpeed(piles2, h2));
        System.out.println("Expected: 30");
        System.out.println("Match: " + (minEatingSpeed(piles2, h2) == 30));
        System.out.println();
        
        // Test Case 3
        int[] piles3 = {30, 11, 23, 4, 20};
        int h3 = 6;
        System.out.println("Test 3:");
        System.out.println("Piles: [30, 11, 23, 4, 20], Hours: " + h3);
        System.out.println("Minimum speed: " + minEatingSpeed(piles3, h3));
        System.out.println("Expected: 23");
        System.out.println("Match: " + (minEatingSpeed(piles3, h3) == 23));
        System.out.println();
        
        // Test Case 4: Edge case - exactly one hour per pile
        int[] piles4 = {5, 5, 5, 5};
        int h4 = 4;
        System.out.println("Test 4:");
        System.out.println("Piles: [5, 5, 5, 5], Hours: " + h4);
        System.out.println("Minimum speed: " + minEatingSpeed(piles4, h4));
        System.out.println("Expected: 5");
        System.out.println();
        
        // Test Case 5: Large difference
        int[] piles5 = {1000000000};
        int h5 = 2;
        System.out.println("Test 5:");
        System.out.println("Piles: [1000000000], Hours: " + h5);
        System.out.println("Minimum speed: " + minEatingSpeed(piles5, h5));
        System.out.println("Expected: 500000000");
        System.out.println();
        
        // Performance comparison
        System.out.println("=== Performance Comparison ===\n");
        int[] largePiles = new int[1000];
        for (int i = 0; i < largePiles.length; i++) {
            largePiles[i] = (int) (Math.random() * 10000) + 1;
        }
        int hours = 1000;
        
        long start = System.nanoTime();
        int result1 = minEatingSpeed(largePiles, hours);
        long time1 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int result2 = minEatingSpeedOptimized(largePiles, hours);
        long time2 = System.nanoTime() - start;
        
        System.out.println("Large test case (1000 piles):");
        System.out.println("Binary Search:      " + result1 + " (Time: " + time1 / 1000 + " μs)");
        System.out.println("Optimized:          " + result2 + " (Time: " + time2 / 1000 + " μs)");
        System.out.println("Results match: " + (result1 == result2));
        
        // Detailed explanation
        System.out.println("\n=== How It Works ===");
        System.out.println("For piles = [3, 6, 7, 11], h = 8:");
        System.out.println("Speed 1: 3+6+7+11 = 27 hours (too slow)");
        System.out.println("Speed 2: 2+3+4+6 = 15 hours (too slow)");
        System.out.println("Speed 3: 1+2+3+4 = 10 hours (too slow)");
        System.out.println("Speed 4: 1+2+2+3 = 8 hours (valid!)");
        System.out.println("Speed 5: 1+2+2+3 = 8 hours (valid but not minimum)");
    }
}
