package org.example.array;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap Optimization Problem
 * 
 * Task: Find the number of pairs (i, j) such that 
 * arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])
 * 
 * Math Trick: Rearrange the equation
 * arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])
 * arr[i] - rev(arr[i]) = arr[j] - rev(arr[j])
 * 
 * So we need to count frequencies of the difference arr[i] - rev(arr[i])
 * 
 * Time Complexity: O(n * log(max(arr[i]))) where log is for reversing digits
 * Space Complexity: O(n) for the HashMap
 */
public class HashMapOptimization {
    
    /**
     * Reverse the digits of a number
     * Example: rev(123) = 321, rev(100) = 1
     */
    private static int reverse(int num) {
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }
        return reversed;
    }
    
    /**
     * Find number of pairs (i, j) such that 
     * arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])
     * 
     * Using the optimization: arr[i] - rev(arr[i]) = arr[j] - rev(arr[j])
     * 
     * @param arr Input array
     * @return Number of valid pairs
     */
    public static long countPairs(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        // Map to store frequency of (arr[i] - rev(arr[i]))
        Map<Integer, Long> diffFreq = new HashMap<>();
        
        // Calculate difference for each element and count frequencies
        for (int num : arr) {
            int diff = num - reverse(num);
            diffFreq.put(diff, diffFreq.getOrDefault(diff, 0L) + 1);
        }
        
        // Count pairs: if n elements have the same difference,
        // number of pairs = n * (n - 1) / 2
        long totalPairs = 0;
        for (long freq : diffFreq.values()) {
            if (freq > 1) {
                // C(n, 2) = n * (n - 1) / 2
                totalPairs += freq * (freq - 1) / 2;
            }
        }
        
        return totalPairs;
    }
    
    /**
     * Alternative approach: Count pairs including same indices
     * (i, i) pairs are also counted if arr[i] - rev(arr[i]) = 0
     */
    public static long countPairsIncludingSame(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        Map<Integer, Long> diffFreq = new HashMap<>();
        
        for (int num : arr) {
            int diff = num - reverse(num);
            diffFreq.put(diff, diffFreq.getOrDefault(diff, 0L) + 1);
        }
        
        long totalPairs = 0;
        for (long freq : diffFreq.values()) {
            // Include pairs (i, i) - so all pairs including same index
            totalPairs += freq * freq;
        }
        
        return totalPairs;
    }
    
    /**
     * Brute force approach for verification (O(n^2))
     */
    public static long countPairsBruteForce(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        long count = 0;
        int n = arr.length;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] + reverse(arr[j]) == arr[j] + reverse(arr[i])) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    public static void main(String[] args) {
        System.out.println("=== HashMap Optimization: Count Pairs ===");
        
        // Test Case 1
        int[] arr1 = {1, 2, 3, 4};
        System.out.println("\nTest Case 1: arr = [1, 2, 3, 4]");
        System.out.println("Optimized result: " + countPairs(arr1));
        System.out.println("Brute force result: " + countPairsBruteForce(arr1));
        
        // Test Case 2: Numbers with same difference
        int[] arr2 = {42, 11, 1, 97};
        // 42 - 24 = 18
        // 11 - 11 = 0
        // 1 - 1 = 0
        // 97 - 79 = 18
        // Pairs: (42, 97) and (11, 1)
        System.out.println("\nTest Case 2: arr = [42, 11, 1, 97]");
        System.out.println("42 - rev(42) = 42 - 24 = 18");
        System.out.println("11 - rev(11) = 11 - 11 = 0");
        System.out.println("1 - rev(1) = 1 - 1 = 0");
        System.out.println("97 - rev(97) = 97 - 79 = 18");
        System.out.println("Optimized result: " + countPairs(arr2));
        System.out.println("Brute force result: " + countPairsBruteForce(arr2));
        
        // Test Case 3: Palindrome numbers (difference = 0)
        int[] arr3 = {11, 22, 33, 44};
        // All have difference 0, so all pairs are valid
        // C(4, 2) = 6 pairs
        System.out.println("\nTest Case 3: arr = [11, 22, 33, 44] (all palindromes)");
        System.out.println("All have difference 0, so C(4, 2) = 6 pairs");
        System.out.println("Optimized result: " + countPairs(arr3));
        System.out.println("Brute force result: " + countPairsBruteForce(arr3));
        
        // Test Case 4: Large array
        int[] arr4 = {10, 20, 30, 40, 50};
        System.out.println("\nTest Case 4: arr = [10, 20, 30, 40, 50]");
        System.out.println("Optimized result: " + countPairs(arr4));
        System.out.println("Brute force result: " + countPairsBruteForce(arr4));
        
        // Test Case 5: Single element
        int[] arr5 = {123};
        System.out.println("\nTest Case 5: arr = [123]");
        System.out.println("Optimized result: " + countPairs(arr5));
        System.out.println("Brute force result: " + countPairsBruteForce(arr5));
        
        // Verify the math trick
        System.out.println("\n=== Verifying Math Trick ===");
        int a = 42, b = 97;
        int revA = reverse(a), revB = reverse(b);
        System.out.println("a = " + a + ", rev(a) = " + revA);
        System.out.println("b = " + b + ", rev(b) = " + revB);
        System.out.println("a + rev(b) = " + (a + revB));
        System.out.println("b + rev(a) = " + (b + revA));
        System.out.println("Are they equal? " + (a + revB == b + revA));
        System.out.println("a - rev(a) = " + (a - revA));
        System.out.println("b - rev(b) = " + (b - revB));
        System.out.println("Are differences equal? " + (a - revA == b - revB));
    }
}
