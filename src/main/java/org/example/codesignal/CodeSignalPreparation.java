package org.example.codesignal;

import java.util.*;

/**
 * CodeSignal Test Preparation - eBay Focused
 * 
 * Common Patterns and Strategies:
 * 
 * Q1-Q2: Usually straightforward array/string problems
 * Q3: Implementation-heavy, follow instructions literally
 * Q4: HashMap optimization - reduce O(n²) to O(n)
 * 
 * Time Management:
 * - Q1: 5-10 minutes
 * - Q2: 10-15 minutes
 * - Q3: 20-30 minutes
 * - Q4: 20-30 minutes
 * 
 * Total: ~70 minutes (aim for 40-50 for better score)
 */
public class CodeSignalPreparation {
    
    // ============================================
    // Pattern 1: Two Pointers
    // ============================================
    
    /**
     * Two Sum (Sorted Array)
     * Find two numbers that add up to target
     */
    public static int[] twoSumSorted(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new int[]{left, right};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{};
    }
    
    /**
     * Remove Duplicates from Sorted Array
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int writeIndex = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                nums[writeIndex++] = nums[i];
            }
        }
        return writeIndex;
    }
    
    // ============================================
    // Pattern 2: Sliding Window
    // ============================================
    
    /**
     * Maximum Sum Subarray of Size K
     */
    public static int maxSumSubarray(int[] nums, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }
        int maxSum = windowSum;
        
        for (int i = k; i < nums.length; i++) {
            windowSum = windowSum - nums[i - k] + nums[i];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }
    
    /**
     * Longest Substring Without Repeating Characters
     */
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLen = 0;
        int start = 0;
        
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            if (map.containsKey(c) && map.get(c) >= start) {
                start = map.get(c) + 1;
            }
            map.put(c, end);
            maxLen = Math.max(maxLen, end - start + 1);
        }
        return maxLen;
    }
    
    // ============================================
    // Pattern 3: HashMap Optimization (Q4 Pattern)
    // ============================================
    
    /**
     * Two Sum (Unsorted) - Classic Q4 pattern
     * O(n) instead of O(n²)
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
     * Count Pairs with Given Difference
     * arr[i] - arr[j] = k
     */
    public static int countPairsWithDiff(int[] arr, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : arr) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        int count = 0;
        for (int num : arr) {
            int target = num - k;
            if (freq.containsKey(target)) {
                count += freq.get(target);
            }
        }
        return count;
    }
    
    /**
     * Group Anagrams
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(map.values());
    }
    
    // ============================================
    // Pattern 4: Prefix Sum
    // ============================================
    
    /**
     * Subarray Sum Equals K
     */
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixSum = new HashMap<>();
        prefixSum.put(0, 1);
        int sum = 0;
        int count = 0;
        
        for (int num : nums) {
            sum += num;
            if (prefixSum.containsKey(sum - k)) {
                count += prefixSum.get(sum - k);
            }
            prefixSum.put(sum, prefixSum.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
    
    // ============================================
    // Pattern 5: Stack
    // ============================================
    
    /**
     * Valid Parentheses
     */
    public static boolean isValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = Map.of(')', '(', '}', '{', ']', '[');
        
        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != map.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }
    
    /**
     * Next Greater Element
     */
    public static int[] nextGreaterElement(int[] nums) {
        int[] result = new int[nums.length];
        Arrays.fill(result, -1);
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                result[stack.pop()] = nums[i];
            }
            stack.push(i);
        }
        return result;
    }
    
    // ============================================
    // Pattern 6: Binary Search
    // ============================================
    
    /**
     * Binary Search
     */
    public static int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    /**
     * Find First and Last Position
     */
    public static int[] searchRange(int[] nums, int target) {
        int first = findFirst(nums, target);
        if (first == -1) return new int[]{-1, -1};
        int last = findLast(nums, target);
        return new int[]{first, last};
    }
    
    private static int findFirst(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                result = mid;
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
    
    private static int findLast(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                result = mid;
                left = mid + 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
    
    // ============================================
    // Pattern 7: Greedy
    // ============================================
    
    /**
     * Maximum Profit (Buy and Sell Stock)
     */
    public static int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }
    
    /**
     * Jump Game
     */
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }
    
    // ============================================
    // Pattern 8: String Manipulation
    // ============================================
    
    /**
     * Reverse String
     */
    public static String reverseString(String s) {
        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }
    
    /**
     * Valid Palindrome
     */
    public static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(s.charAt(left)) != 
                Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
    // ============================================
    // eBay-Specific Patterns
    // ============================================
    
    /**
     * Shopping Cart Total with Discounts
     * Common eBay Q3 pattern
     */
    public static double calculateTotal(double[] prices, double discountPercent) {
        double total = 0;
        for (double price : prices) {
            total += price * (1 - discountPercent / 100.0);
        }
        return total;
    }
    
    /**
     * Inventory Management
     * Track arrivals and departures
     */
    public static int maxInventory(int[] arrivals, int[] departures) {
        Arrays.sort(arrivals);
        Arrays.sort(departures);
        
        int maxInventory = 0;
        int current = 0;
        int i = 0, j = 0;
        
        while (i < arrivals.length && j < departures.length) {
            if (arrivals[i] <= departures[j]) {
                current++;
                maxInventory = Math.max(maxInventory, current);
                i++;
            } else {
                current--;
                j++;
            }
        }
        return maxInventory;
    }
    
    /**
     * Product Search Autocomplete
     * Trie-based search
     */
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
    }
    
    static class Trie {
        TrieNode root = new TrieNode();
        
        void insert(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
            }
            current.isEnd = true;
        }
        
        List<String> search(String prefix) {
            List<String> results = new ArrayList<>();
            TrieNode current = root;
            
            for (char c : prefix.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return results;
                }
                current = current.children.get(c);
            }
            
            dfs(current, prefix, results);
            return results;
        }
        
        private void dfs(TrieNode node, String current, List<String> results) {
            if (node.isEnd) {
                results.add(current);
            }
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                dfs(entry.getValue(), current + entry.getKey(), results);
            }
        }
    }
    
    // ============================================
    // Common Q4 Optimization Pattern
    // ============================================
    
    /**
     * Count pairs where arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])
     * Rearrange: arr[i] - rev(arr[i]) = arr[j] - rev(arr[j])
     * Use HashMap to count frequencies of differences
     */
    public static long countPairsOptimized(int[] arr) {
        Map<Integer, Long> diffFreq = new HashMap<>();
        
        for (int num : arr) {
            int diff = num - reverse(num);
            diffFreq.put(diff, diffFreq.getOrDefault(diff, 0L) + 1);
        }
        
        long count = 0;
        for (long freq : diffFreq.values()) {
            if (freq > 1) {
                count += freq * (freq - 1) / 2; // C(n,2)
            }
        }
        return count;
    }
    
    private static int reverse(int num) {
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }
        return reversed;
    }
    
    // ============================================
    // Main - Testing
    // ============================================
    
    public static void main(String[] args) {
        System.out.println("=== CodeSignal Test Preparation ===\n");
        
        // Test Two Sum
        System.out.println("1. Two Sum:");
        int[] nums1 = {2, 7, 11, 15};
        System.out.println("Input: [2,7,11,15], target=9");
        System.out.println("Output: " + Arrays.toString(twoSum(nums1, 9)));
        System.out.println();
        
        // Test Sliding Window
        System.out.println("2. Max Sum Subarray (k=3):");
        int[] nums2 = {1, 4, 2, 10, 23, 3, 1, 0, 20};
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Output: " + maxSumSubarray(nums2, 3));
        System.out.println();
        
        // Test Subarray Sum
        System.out.println("3. Subarray Sum Equals K:");
        int[] nums3 = {1, 1, 1};
        System.out.println("Input: [1,1,1], k=2");
        System.out.println("Output: " + subarraySum(nums3, 2));
        System.out.println();
        
        // Test Q4 Pattern
        System.out.println("4. Count Pairs (Q4 Pattern):");
        int[] nums4 = {42, 11, 1, 97};
        System.out.println("Input: [42,11,1,97]");
        System.out.println("Output: " + countPairsOptimized(nums4));
        System.out.println();
        
        System.out.println("=== Key Tips ===");
        System.out.println("Q1-Q2: Straightforward, solve quickly");
        System.out.println("Q3: Follow instructions literally, don't overthink");
        System.out.println("Q4: Look for HashMap optimization (O(n²) -> O(n))");
        System.out.println("Speed matters: Finish in 40-50 min for better score");
    }
}
