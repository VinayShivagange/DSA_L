package org.example.dynamic_programming;

import java.util.Arrays;

/**
 * Coin Change - Dynamic Programming Problem
 * 
 * Problem: You are given an integer array coins representing coins of different 
 * denominations and an integer amount representing a total amount of money.
 * 
 * Return the fewest number of coins that you need to make up that amount. 
 * If that amount of money cannot be made up by any combination of the coins, return -1.
 * 
 * You may assume that you have an infinite number of each kind of coin.
 * 
 * Example 1:
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 * 
 * Example 2:
 * Input: coins = [2], amount = 3
 * Output: -1
 * Explanation: Cannot make 3 with only 2s
 * 
 * Example 3:
 * Input: coins = [1], amount = 0
 * Output: 0
 * 
 * Variations:
 * 1. Minimum number of coins (this problem)
 * 2. Number of ways to make change
 * 3. Print the actual coins used
 */
public class CoinChange {
    
    // ============================================
    // Approach 1: Recursive with Memoization
    // ============================================
    
    /**
     * Top-down DP with memoization
     * Time: O(amount * coins.length)
     * Space: O(amount) for memoization + recursion stack
     */
    public static int coinChangeMemo(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, -2); // -2 means not computed
        
        return coinChangeHelper(coins, amount, memo);
    }
    
    private static int coinChangeHelper(int[] coins, int amount, int[] memo) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // Check memo
        if (memo[amount] != -2) {
            return memo[amount];
        }
        
        int minCoins = Integer.MAX_VALUE;
        
        // Try each coin
        for (int coin : coins) {
            int result = coinChangeHelper(coins, amount - coin, memo);
            if (result != -1) {
                minCoins = Math.min(minCoins, result + 1);
            }
        }
        
        // Store result
        memo[amount] = (minCoins == Integer.MAX_VALUE) ? -1 : minCoins;
        return memo[amount];
    }
    
    // ============================================
    // Approach 2: Bottom-Up DP
    // ============================================
    
    /**
     * Bottom-up dynamic programming
     * Time: O(amount * coins.length)
     * Space: O(amount)
     * 
     * BEST APPROACH for this problem
     */
    public static int coinChange(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // dp[i] = minimum coins needed to make amount i
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // Initialize with a large value
        dp[0] = 0; // Base case: 0 coins needed for amount 0
        
        // Fill DP array
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    // Try using this coin
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        // If dp[amount] is still the initial value, it's impossible
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // ============================================
    // Variation 1: Number of Ways to Make Change
    // ============================================
    
    /**
     * Count the number of ways to make change (order matters)
     * Example: coins = [1,2], amount = 3
     * Ways: 1+1+1, 1+2, 2+1
     * Answer: 3
     */
    public static int coinChangeWays(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }
        
        // dp[i] = number of ways to make amount i
        int[] dp = new int[amount + 1];
        dp[0] = 1; // Base case: 1 way to make 0 (use no coins)
        
        // Fill DP array
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] += dp[i - coin];
                }
            }
        }
        
        return dp[amount];
    }
    
    /**
     * Count ways where order doesn't matter (combinations)
     * Example: coins = [1,2], amount = 3
     * Ways: 1+1+1, 1+2
     * Answer: 2 (2+1 is same as 1+2)
     */
    public static int coinChangeCombinations(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }
        
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        // Process each coin type completely before moving to next
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    // ============================================
    // Variation 2: Print Actual Coins Used
    // ============================================
    
    /**
     * Find minimum coins and return the actual coins used
     */
    public static class CoinChangeResult {
        int minCoins;
        int[] coinsUsed;
        
        CoinChangeResult(int minCoins, int[] coinsUsed) {
            this.minCoins = minCoins;
            this.coinsUsed = coinsUsed;
        }
    }
    
    public static CoinChangeResult coinChangeWithCoins(int[] coins, int amount) {
        if (amount < 0) {
            return new CoinChangeResult(-1, new int[0]);
        }
        if (amount == 0) {
            return new CoinChangeResult(0, new int[0]);
        }
        
        int[] dp = new int[amount + 1];
        int[] parent = new int[amount + 1]; // Track which coin was used
        Arrays.fill(dp, amount + 1);
        Arrays.fill(parent, -1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    parent[i] = coin; // Remember which coin was used
                }
            }
        }
        
        if (dp[amount] > amount) {
            return new CoinChangeResult(-1, new int[0]);
        }
        
        // Reconstruct coins used
        int[] coinsUsed = new int[dp[amount]];
        int current = amount;
        int index = 0;
        
        while (current > 0) {
            int coin = parent[current];
            coinsUsed[index++] = coin;
            current -= coin;
        }
        
        return new CoinChangeResult(dp[amount], coinsUsed);
    }
    
    // ============================================
    // Variation 3: Limited Coins (0/1 Knapsack style)
    // ============================================
    
    /**
     * Each coin can be used at most once
     * This becomes a 0/1 knapsack problem
     */
    public static int coinChangeLimited(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // dp[i][j] = min coins using first i coins to make amount j
        int[][] dp = new int[coins.length + 1][amount + 1];
        
        // Initialize: impossible amounts
        for (int i = 0; i <= coins.length; i++) {
            Arrays.fill(dp[i], amount + 1);
            dp[i][0] = 0;
        }
        
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                // Don't use coin i-1
                dp[i][j] = dp[i - 1][j];
                
                // Use coin i-1 (if possible)
                if (coins[i - 1] <= j) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - coins[i - 1]] + 1);
                }
            }
        }
        
        return dp[coins.length][amount] > amount ? -1 : dp[coins.length][amount];
    }
    
    public static void main(String[] args) {
        System.out.println("=== Coin Change - Dynamic Programming ===\n");
        
        // Test Case 1
        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        System.out.println("Test 1:");
        System.out.println("Coins: " + Arrays.toString(coins1) + ", Amount: " + amount1);
        System.out.println("Minimum coins: " + coinChange(coins1, amount1));
        System.out.println("Expected: 3 (5+5+1)");
        System.out.println("Match: " + (coinChange(coins1, amount1) == 3));
        
        CoinChangeResult result1 = coinChangeWithCoins(coins1, amount1);
        System.out.println("Coins used: " + Arrays.toString(result1.coinsUsed));
        System.out.println();
        
        // Test Case 2
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("Test 2:");
        System.out.println("Coins: " + Arrays.toString(coins2) + ", Amount: " + amount2);
        System.out.println("Minimum coins: " + coinChange(coins2, amount2));
        System.out.println("Expected: -1 (impossible)");
        System.out.println("Match: " + (coinChange(coins2, amount2) == -1));
        System.out.println();
        
        // Test Case 3
        int[] coins3 = {1};
        int amount3 = 0;
        System.out.println("Test 3:");
        System.out.println("Coins: " + Arrays.toString(coins3) + ", Amount: " + amount3);
        System.out.println("Minimum coins: " + coinChange(coins3, amount3));
        System.out.println("Expected: 0");
        System.out.println("Match: " + (coinChange(coins3, amount3) == 0));
        System.out.println();
        
        // Test Case 4
        int[] coins4 = {1, 3, 4};
        int amount4 = 6;
        System.out.println("Test 4:");
        System.out.println("Coins: " + Arrays.toString(coins4) + ", Amount: " + amount4);
        System.out.println("Minimum coins: " + coinChange(coins4, amount4));
        System.out.println("Expected: 2 (3+3)");
        System.out.println("Match: " + (coinChange(coins4, amount4) == 2));
        System.out.println();
        
        // Variation: Number of ways
        System.out.println("=== Variation: Number of Ways ===");
        int[] coins5 = {1, 2};
        int amount5 = 3;
        System.out.println("Coins: " + Arrays.toString(coins5) + ", Amount: " + amount5);
        System.out.println("Ways (order matters): " + coinChangeWays(coins5, amount5));
        System.out.println("  (1+1+1, 1+2, 2+1)");
        System.out.println("Ways (combinations): " + coinChangeCombinations(coins5, amount5));
        System.out.println("  (1+1+1, 1+2)");
        System.out.println();
        
        // Performance comparison
        System.out.println("=== Performance Comparison ===");
        int[] coins6 = {1, 2, 5, 10, 20, 50};
        int amount6 = 100;
        
        long start = System.nanoTime();
        int resultMemo = coinChangeMemo(coins6, amount6);
        long timeMemo = System.nanoTime() - start;
        
        start = System.nanoTime();
        int resultDP = coinChange(coins6, amount6);
        long timeDP = System.nanoTime() - start;
        
        System.out.println("Coins: " + Arrays.toString(coins6) + ", Amount: " + amount6);
        System.out.println("Memoization: " + resultMemo + " (Time: " + timeMemo / 1000 + " μs)");
        System.out.println("Bottom-up DP: " + resultDP + " (Time: " + timeDP / 1000 + " μs)");
        System.out.println("Results match: " + (resultMemo == resultDP));
        System.out.println();
        
        // Limited coins variation
        System.out.println("=== Variation: Limited Coins ===");
        int[] coins7 = {1, 2, 5};
        int amount7 = 11;
        System.out.println("Coins (each used once): " + Arrays.toString(coins7) + 
                          ", Amount: " + amount7);
        System.out.println("Minimum coins: " + coinChangeLimited(coins7, amount7));
        System.out.println("Note: With limited coins, might not be able to make amount");
    }
}
