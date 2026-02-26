package org.example.array;

import java.util.Arrays;

/**
 * Minimum Balls to Pick to Guarantee P Pairs
 * 
 * Problem: A box contains balls with N colors. For each color i, you have c[i] 
 * number of balls of that color. You can make a pair of balls of the same color.
 * Find the minimum number of balls you need to pick (without looking) to ensure 
 * you can make at least p pairs.
 * 
 * Approach: This is a worst-case scenario problem. To guarantee p pairs, we need
 * to find the maximum number of balls we can pick WITHOUT getting p pairs, then 
 * add 1 more ball.
 * 
 * Strategy:
 * - To minimize pairs while maximizing balls, we should pick odd numbers of balls
 *   from each color (1, 3, 5, ...) which gives fewer pairs per ball
 * - For each color i with c[i] balls:
 *   - If c[i] is even: we can pick c[i]-1 balls to get (c[i]-2)/2 pairs + 1 leftover
 *   - If c[i] is odd: we can pick c[i] balls to get (c[i]-1)/2 pairs + 1 leftover
 * - We greedily pick balls to maximize count while keeping pairs < p
 */
public class MinimumBallsForPairs {
    
    /**
     * Find minimum balls needed to guarantee at least p pairs
     * 
     * @param c Array where c[i] is the number of balls of color i
     * @param p Minimum number of pairs required
     * @return Minimum number of balls to pick, or -1 if impossible
     */
    public static int minimumBallsToGuaranteePairs(int[] c, int p) {
        // Use the optimal approach
        return minimumBallsToGuaranteePairsOptimal(c, p);
    }
    
    /**
     * Alternative simpler approach: Calculate directly
     */
    public static int minimumBallsToGuaranteePairsSimple(int[] c, int p) {
        if (c == null || c.length == 0 || p <= 0) {
            return p == 0 ? 0 : -1;
        }
        
        // Calculate total pairs possible
        int totalPairs = 0;
        for (int count : c) {
            totalPairs += count / 2;
        }
        
        if (totalPairs < p) {
            return -1;
        }
        
        // Maximum balls without p pairs:
        // We want to maximize balls while having at most (p-1) pairs
        // Best strategy: pick odd numbers (1, 3, 5, ...) from each color
        
        int maxBalls = 0;
        int currentPairs = 0;
        
        // Sort colors by their count (we'll process them strategically)
        int[] sorted = Arrays.copyOf(c, c.length);
        Arrays.sort(sorted);
        
        // Greedy: pick balls to maximize count while pairs < p
        for (int i = sorted.length - 1; i >= 0; i--) {
            int count = sorted[i];
            
            if (currentPairs >= p) {
                break;
            }
            
            // Calculate how many pairs we still need to avoid
            int remainingPairsToAvoid = p - 1 - currentPairs;
            
            if (remainingPairsToAvoid <= 0) {
                // We've reached p-1 pairs, can't take more
                break;
            }
            
            // From this color, we can take:
            // - If count is even: take count-1 balls (gives (count-2)/2 pairs)
            // - If count is odd: take count balls (gives (count-1)/2 pairs)
            // But we need to make sure we don't exceed p-1 pairs total
            
            int maxPairsFromThisColor = count / 2;
            int pairsWeCanTake = Math.min(maxPairsFromThisColor, remainingPairsToAvoid);
            
            // To get exactly 'pairsWeCanTake' pairs, we need 2*pairsWeCanTake balls
            // But we want to maximize balls, so we take 2*pairsWeCanTake + 1 if possible
            int ballsToTake;
            if (pairsWeCanTake * 2 + 1 <= count) {
                // We can take an odd number
                ballsToTake = 2 * pairsWeCanTake + 1;
            } else {
                // We can only take even number (all available)
                ballsToTake = Math.min(2 * pairsWeCanTake, count);
            }
            
            maxBalls += ballsToTake;
            currentPairs += pairsWeCanTake;
        }
        
        return maxBalls + 1;
    }
    
    /**
     * Most optimized approach using the correct mathematical formula
     * 
     * Strategy: To guarantee p pairs, find the maximum balls we can pick
     * without getting p pairs, then add 1.
     * 
     * To maximize balls while keeping pairs < p:
     * - For each color, pick odd numbers (1, 3, 5, ...) which minimize pairs
     * - Odd number k gives (k-1)/2 pairs
     * - Process colors greedily until we have p-1 pairs
     * - After reaching p-1 pairs, take ALL remaining single balls (they don't create pairs)
     */
    public static int minimumBallsToGuaranteePairsOptimal(int[] c, int p) {
        if (c == null || c.length == 0 || p <= 0) {
            return p == 0 ? 0 : -1;
        }
        
        // Calculate total pairs possible
        int totalPairs = 0;
        for (int count : c) {
            totalPairs += count / 2;
        }
        
        if (totalPairs < p) {
            return -1;
        }
        
        // Find maximum balls we can pick without getting p pairs
        // We want to maximize balls while keeping total pairs <= p-1
        
        int maxBalls = 0;
        int currentPairs = 0;
        boolean reachedMaxPairs = false;
        
        // First pass: Greedily pick balls to get exactly p-1 pairs (maximizing balls)
        for (int count : c) {
            if (reachedMaxPairs) {
                // We've reached p-1 pairs, can only take single balls (1 ball = 0 pairs)
                // from remaining colors to maximize total balls
                if (count > 0) {
                    maxBalls += 1; // Take 1 ball (0 pairs)
                }
                continue;
            }
            
            // How many more pairs can we take without reaching p?
            int remainingPairsAllowed = p - 1 - currentPairs;
            
            if (remainingPairsAllowed > 0) {
                // We can still take pairs from this color
                // Maximum pairs available from this color
                int maxPairsFromColor = count / 2;
                
                // How many pairs should we take from this color?
                int pairsToTake = Math.min(maxPairsFromColor, remainingPairsAllowed);
                
                // To get 'pairsToTake' pairs while maximizing balls:
                // - If we can, take 2*pairsToTake + 1 balls (odd number, maximizes balls)
                // - Otherwise, take 2*pairsToTake balls (even number)
                int ballsFromColor;
                if (2 * pairsToTake + 1 <= count) {
                    // We can take an odd number, which maximizes balls
                    ballsFromColor = 2 * pairsToTake + 1;
                } else {
                    // Can't take odd number, take even (or all available)
                    ballsFromColor = Math.min(2 * pairsToTake, count);
                }
                
                maxBalls += ballsFromColor;
                currentPairs += pairsToTake;
                
                // Check if we've reached p-1 pairs
                if (currentPairs >= p - 1) {
                    reachedMaxPairs = true;
                }
            } else {
                // We've reached p-1 pairs, can only take single balls
                reachedMaxPairs = true;
                if (count > 0) {
                    maxBalls += 1; // Take 1 ball (0 pairs)
                }
            }
        }
        
        return maxBalls + 1;
    }
    public static void main(String[] args) {
        // Test cases
        System.out.println("=== Minimum Balls to Guarantee P Pairs ===");
        
        // Sample Test Case from problem: N=3, P=2, C=[1, 3, 2]
        // Color 1: 1 ball (0 pairs)
        // Color 2: 3 balls (1 pair)
        // Color 3: 2 balls (1 pair)
        // Total pairs possible: 0 + 1 + 1 = 2
        // Worst case to NOT get 2 pairs: 
        //   - Take 1 from color1 (0 pairs) + 3 from color2 (1 pair) + 1 from color3 (0 pairs) = 5 balls, 1 pair
        // Maximum without 2 pairs = 5, so answer = 6
        int[] sample = {1, 3, 2};
        int pSample = 2;
        System.out.println("\nSample Test Case: N=3, P=2, C=" + Arrays.toString(sample));
        int result = minimumBallsToGuaranteePairsOptimal(sample, pSample);
        System.out.println("Result: " + result);
        System.out.println("Expected: 6");
        
        // Verify: Can we pick 5 balls with only 1 pair?
        // Yes: 1 (color1, 0 pairs) + 3 (color2, 1 pair) + 1 (color3, 0 pairs) = 5 balls, 1 pair
        // So 6th ball guarantees 2 pairs
        
        // Example 1: c = [3, 3, 3], p = 2
        int[] c1 = {3, 3, 3};
        int p1 = 2;
        System.out.println("\nExample 1: c = " + Arrays.toString(c1) + ", p = " + p1);
        System.out.println("Result: " + minimumBallsToGuaranteePairsOptimal(c1, p1));
        
        // Example 2: c = [4, 4, 4], p = 3
        int[] c2 = {4, 4, 4};
        int p2 = 3;
        System.out.println("\nExample 2: c = " + Arrays.toString(c2) + ", p = " + p2);
        System.out.println("Result: " + minimumBallsToGuaranteePairsOptimal(c2, p2));
        
        // Example 3: c = [5, 5], p = 3
        int[] c3 = {5, 5};
        int p3 = 3;
        System.out.println("\nExample 3: c = " + Arrays.toString(c3) + ", p = " + p3);
        System.out.println("Result: " + minimumBallsToGuaranteePairsOptimal(c3, p3));
        
        // Example 4: c = [2, 2, 2, 2], p = 2
        int[] c4 = {2, 2, 2, 2};
        int p4 = 2;
        System.out.println("\nExample 4: c = " + Arrays.toString(c4) + ", p = " + p4);
        System.out.println("Result: " + minimumBallsToGuaranteePairsOptimal(c4, p4));
        
        // Example 5: Edge case - impossible
        int[] c5 = {1, 1};
        int p5 = 2;
        System.out.println("\nExample 5 (Impossible): c = " + Arrays.toString(c5) + ", p = " + p5);
        System.out.println("Result: " + minimumBallsToGuaranteePairsOptimal(c5, p5));
        System.out.println("Expected: -1 (only 1 pair possible, need 2)");
    }
}
