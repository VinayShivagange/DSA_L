package org.example.array;

import java.util.*;

/**
 * CodeSignal Pro-Tips for eBay Interviews
 * 
 * This file contains common patterns and tips for CodeSignal assessments
 */
public class CodeSignalTips {
    
    /**
     * TIP 1: Don't overthink Q3
     * Q3 is usually just a lot of code. Don't look for a "clever" algorithm;
     * just follow the instructions literally.
     */
    
    /**
     * TIP 2: Q4 is about HashMaps
     * Almost every CodeSignal Q4 can be solved by storing "complements" 
     * or "calculated differences" in a HashMap.
     */
    
    /**
     * TIP 3: Handle Large Inputs
     * If you see 10^5 constraints, O(n^2) will fail.
     * You must find an O(n) or O(n log n) solution.
     */
    
    /**
     * TIP 4: The "Speed" Bonus
     * CodeSignal tracks when you finish. If you finish in 40 minutes vs 70 minutes,
     * your score will be higher even with the same number of passed tests.
     */
    
    // ============================================
    // Common Patterns
    // ============================================
    
    /**
     * Pattern 1: Two Sum with HashMap (O(n) instead of O(n^2))
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }
    
    /**
     * Pattern 2: Frequency Counting
     */
    public static Map<Integer, Integer> countFrequencies(int[] arr) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : arr) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        return freq;
    }
    
    /**
     * Pattern 3: Grouping by Calculated Value
     * Similar to the HashMap optimization problem
     */
    public static <T> Map<Integer, List<Integer>> groupByDifference(int[] arr) {
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int diff = arr[i] - reverseDigits(arr[i]);
            groups.computeIfAbsent(diff, k -> new ArrayList<>()).add(i);
        }
        return groups;
    }
    
    private static int reverseDigits(int num) {
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }
        return reversed;
    }
    
    // ============================================
    // eBay Specific Themes
    // ============================================
    
    /**
     * Theme 1: Shopping Cart Logic
     * Calculating discounts or tax based on arrays
     */
    public static double calculateTotalWithDiscount(double[] prices, double discountPercent) {
        double total = 0;
        for (double price : prices) {
            total += price * (1 - discountPercent / 100.0);
        }
        return total;
    }
    
    /**
     * Theme 2: Inventory Management
     * Given arrivals and departures, what is the max inventory needed?
     */
    public static int maxInventoryNeeded(int[] arrivals, int[] departures) {
        // Sort both arrays
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        int maxInventory = 0;
        int currentInventory = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                currentInventory++;
                maxInventory = Math.max(maxInventory, currentInventory);
                i++;
            } else {
                currentInventory--;
                j++;
            }
        }
        
        return maxInventory;
    }
    
    /**
     * Theme 3: Search/Autocomplete
     * Working with Tries or String prefixes
     */
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
    }
    
    static class Trie {
        private TrieNode root;
        
        public Trie() {
            root = new TrieNode();
        }
        
        public void insert(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
            }
            current.isEndOfWord = true;
        }
        
        public boolean search(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return false;
                }
                current = current.children.get(c);
            }
            return current.isEndOfWord;
        }
        
        public List<String> autocomplete(String prefix) {
            List<String> results = new ArrayList<>();
            TrieNode current = root;
            
            // Navigate to prefix
            for (char c : prefix.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return results;
                }
                current = current.children.get(c);
            }
            
            // DFS to find all words with this prefix
            dfs(current, prefix, results);
            return results;
        }
        
        private void dfs(TrieNode node, String currentWord, List<String> results) {
            if (node.isEndOfWord) {
                results.add(currentWord);
            }
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                dfs(entry.getValue(), currentWord + entry.getKey(), results);
            }
        }
    }
    
    // ============================================
    // Common Optimization Patterns
    // ============================================
    
    /**
     * Pattern: Sliding Window (for subarray problems)
     */
    public static int maxSumSubarray(int[] arr, int k) {
        int maxSum = 0;
        int windowSum = 0;
        
        // Calculate sum of first window
        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }
        maxSum = windowSum;
        
        // Slide the window
        for (int i = k; i < arr.length; i++) {
            windowSum = windowSum - arr[i - k] + arr[i];
            maxSum = Math.max(maxSum, windowSum);
        }
        
        return maxSum;
    }
    
    /**
     * Pattern: Prefix Sum (for range sum queries)
     */
    public static int[] prefixSum(int[] arr) {
        int[] prefix = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }
        return prefix;
    }
    
    public static int rangeSum(int[] prefix, int left, int right) {
        return prefix[right + 1] - prefix[left];
    }
    
    public static void main(String[] args) {
        System.out.println("=== CodeSignal Pro-Tips for eBay ===");
        
        // Test Two Sum
        int[] nums = {2, 7, 11, 15};
        System.out.println("\nTwo Sum [2, 7, 11, 15], target=9: " + 
            Arrays.toString(twoSum(nums, 9)));
        
        // Test Inventory Management
        int[] arrivals = {1, 2, 3, 4, 5};
        int[] departures = {3, 4, 5, 6, 7};
        System.out.println("\nMax Inventory Needed: " + 
            maxInventoryNeeded(arrivals, departures));
        
        // Test Trie
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");
        trie.insert("apply");
        System.out.println("\nTrie autocomplete 'app': " + trie.autocomplete("app"));
        
        // Test Sliding Window
        int[] arr = {1, 4, 2, 10, 23, 3, 1, 0, 20};
        System.out.println("\nMax sum subarray of size 4: " + maxSumSubarray(arr, 4));
        
        // Test Prefix Sum
        int[] prefix = prefixSum(new int[]{1, 2, 3, 4, 5});
        System.out.println("\nRange sum [1, 3]: " + rangeSum(prefix, 1, 3));
    }
}
