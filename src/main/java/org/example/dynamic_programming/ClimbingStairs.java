package org.example.dynamic_programming;

import java.util.HashMap;
import java.util.Map;

/**
 * Climbing Stairs - Dynamic Programming Problem
 * 
 * Problem: You are climbing a staircase. It takes n steps to reach the top.
 * Each time you can either climb 1 or 2 steps. 
 * In how many distinct ways can you climb to the top?
 * 
 * Example:
 * n = 2
 * Ways: 1+1, 2
 * Answer: 2
 * 
 * n = 3
 * Ways: 1+1+1, 1+2, 2+1
 * Answer: 3
 * 
 * n = 4
 * Ways: 1+1+1+1, 1+1+2, 1+2+1, 2+1+1, 2+2
 * Answer: 5
 * 
 * Pattern: This is the Fibonacci sequence!
 * f(n) = f(n-1) + f(n-2)
 * Base cases: f(1) = 1, f(2) = 2
 */
public class ClimbingStairs {
    
    // ============================================
    // Approach 1: Recursive (Naive - O(2^n))
    // ============================================
    
    /**
     * Recursive solution without memoization
     * Time: O(2^n) - Exponential
     * Space: O(n) - Recursion stack
     * 
     * NOT RECOMMENDED for large n
     */
    public static int climbStairsRecursive(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // To reach step n, you can come from step n-1 or n-2
        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }
    
    // ============================================
    // Approach 2: Memoization (Top-Down DP)
    // ============================================
    
    /**
     * Recursive solution with memoization
     * Time: O(n)
     * Space: O(n) - Memoization map + recursion stack
     */
    public static int climbStairsMemo(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        return climbStairsMemoHelper(n, memo);
    }
    
    private static int climbStairsMemoHelper(int n, Map<Integer, Integer> memo) {
        // Base cases
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // Check memo
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        
        // Calculate and store
        int result = climbStairsMemoHelper(n - 1, memo) + 
                    climbStairsMemoHelper(n - 2, memo);
        memo.put(n, result);
        
        return result;
    }
    
    // ============================================
    // Approach 3: Bottom-Up DP (Iterative)
    // ============================================
    
    /**
     * Bottom-up dynamic programming
     * Time: O(n)
     * Space: O(n) - DP array
     */
    public static int climbStairsDP(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // dp[i] = number of ways to reach step i
        int[] dp = new int[n + 1];
        
        // Base cases
        dp[0] = 1; // One way to stay at ground (for consistency)
        dp[1] = 1; // One way: climb 1 step
        dp[2] = 2; // Two ways: 1+1 or 2
        
        // Fill DP array
        for (int i = 3; i <= n; i++) {
            // To reach step i, you can come from step i-1 or i-2
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    // ============================================
    // Approach 4: Space-Optimized DP
    // ============================================
    
    /**
     * Space-optimized DP (only need last 2 values)
     * Time: O(n)
     * Space: O(1) - Only using 2 variables
     * 
     * BEST APPROACH for this problem
     */
    public static int climbStairsOptimized(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // Only need to track last 2 values
        int prev2 = 1; // Ways to reach step i-2
        int prev1 = 2; // Ways to reach step i-1
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }
    
    // ============================================
    // Approach 5: Matrix Exponentiation (Advanced)
    // ============================================
    
    /**
     * Matrix exponentiation approach
     * Time: O(log n)
     * Space: O(1)
     * 
     * For very large n, this is the most efficient
     */
    public static int climbStairsMatrix(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // Using the fact that this is Fibonacci
        // F(n) = F(n-1) + F(n-2)
        // Can be computed using matrix exponentiation
        
        // For climbing stairs: f(1)=1, f(2)=2
        // So we need to compute f(n) where f(1)=1, f(2)=2
        
        // Matrix form: [f(n), f(n-1)] = [f(2), f(1)] * M^(n-2)
        // Where M = [[1, 1], [1, 0]]
        
        int[][] base = {{2, 1}}; // [f(2), f(1)]
        int[][] matrix = {{1, 1}, {1, 0}};
        
        int[][] result = matrixPower(matrix, n - 2);
        return base[0][0] * result[0][0] + base[0][1] * result[1][0];
    }
    
    private static int[][] matrixPower(int[][] matrix, int power) {
        int n = matrix.length;
        int[][] result = new int[n][n];
        
        // Initialize as identity matrix
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        int[][] base = matrix;
        while (power > 0) {
            if (power % 2 == 1) {
                result = matrixMultiply(result, base);
            }
            base = matrixMultiply(base, base);
            power /= 2;
        }
        
        return result;
    }
    
    private static int[][] matrixMultiply(int[][] a, int[][] b) {
        int n = a.length;
        int[][] result = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        
        return result;
    }
    
    // ============================================
    // Approach 6: Direct Formula (Binet's Formula)
    // ============================================
    
    /**
     * Using Binet's formula for Fibonacci
     * Time: O(1)
     * Space: O(1)
     * 
     * Note: May have precision issues for very large n
     */
    public static int climbStairsFormula(int n) {
        if (n <= 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        
        // For climbing stairs: f(1)=1, f(2)=2
        // This is Fibonacci shifted: f(n) = F(n+1) where F is standard Fibonacci
        // F(1)=1, F(2)=1, F(3)=2, F(4)=3, F(5)=5
        
        // So f(n) = F(n+1) = F(n) + F(n-1)
        // We can use Binet's formula for F(n+1)
        
        double sqrt5 = Math.sqrt(5);
        double phi = (1 + sqrt5) / 2; // Golden ratio
        double psi = (1 - sqrt5) / 2;
        
        // F(n+1) = (phi^(n+1) - psi^(n+1)) / sqrt5
        // But for climbing stairs starting at f(1)=1, f(2)=2:
        // f(n) = (phi^(n+1) - psi^(n+1)) / sqrt5
        
        double result = (Math.pow(phi, n + 1) - Math.pow(psi, n + 1)) / sqrt5;
        return (int) Math.round(result);
    }
    
    // ============================================
    // Main Method - Testing
    // ============================================
    
    public static void main(String[] args) {
        System.out.println("=== Climbing Stairs - Dynamic Programming ===\n");
        
        // Test cases
        int[] testCases = {1, 2, 3, 4, 5, 10, 20, 30};
        
        System.out.println("Testing different approaches:\n");
        System.out.printf("%-5s | %-15s | %-15s | %-15s | %-15s%n", 
            "n", "Memoization", "DP Array", "Optimized", "Formula");
        System.out.println("------------------------------------------------" +
            "----------------------------------------");
        
        for (int n : testCases) {
            int memo = climbStairsMemo(n);
            int dp = climbStairsDP(n);
            int optimized = climbStairsOptimized(n);
            int formula = climbStairsFormula(n);
            
            System.out.printf("%-5d | %-15d | %-15d | %-15d | %-15d%n",
                n, memo, dp, optimized, formula);
        }
        
        System.out.println("\n=== Detailed Examples ===\n");
        
        // Example 1: n = 2
        System.out.println("Example 1: n = 2");
        System.out.println("Ways to climb:");
        System.out.println("  1. 1 step + 1 step");
        System.out.println("  2. 2 steps");
        System.out.println("Total ways: " + climbStairsOptimized(2));
        System.out.println();
        
        // Example 2: n = 3
        System.out.println("Example 2: n = 3");
        System.out.println("Ways to climb:");
        System.out.println("  1. 1 + 1 + 1");
        System.out.println("  2. 1 + 2");
        System.out.println("  3. 2 + 1");
        System.out.println("Total ways: " + climbStairsOptimized(3));
        System.out.println();
        
        // Example 3: n = 4
        System.out.println("Example 3: n = 4");
        System.out.println("Ways to climb:");
        System.out.println("  1. 1 + 1 + 1 + 1");
        System.out.println("  2. 1 + 1 + 2");
        System.out.println("  3. 1 + 2 + 1");
        System.out.println("  4. 2 + 1 + 1");
        System.out.println("  5. 2 + 2");
        System.out.println("Total ways: " + climbStairsOptimized(4));
        System.out.println();
        
        // Performance comparison
        System.out.println("=== Performance Comparison ===\n");
        int largeN = 40;
        
        long start = System.nanoTime();
        int result1 = climbStairsMemo(largeN);
        long time1 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int result2 = climbStairsDP(largeN);
        long time2 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int result3 = climbStairsOptimized(largeN);
        long time3 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int result4 = climbStairsFormula(largeN);
        long time4 = System.nanoTime() - start;
        
        System.out.println("For n = " + largeN + ":");
        System.out.println("Memoization:  " + result1 + " (Time: " + time1 / 1000 + " μs)");
        System.out.println("DP Array:      " + result2 + " (Time: " + time2 / 1000 + " μs)");
        System.out.println("Optimized:     " + result3 + " (Time: " + time3 / 1000 + " μs)");
        System.out.println("Formula:        " + result4 + " (Time: " + time4 / 1000 + " μs)");
        
        System.out.println("\n=== Pattern Recognition ===");
        System.out.println("This is the Fibonacci sequence!");
        System.out.println("f(1) = 1");
        System.out.println("f(2) = 2");
        System.out.println("f(3) = 3");
        System.out.println("f(4) = 5");
        System.out.println("f(5) = 8");
        System.out.println("f(n) = f(n-1) + f(n-2)");
    }
}
