package org.example.String;

public class FindPalidrome {

    public static void main(String[] args) {
       // System.out.println(minRemovalsToMakePalindrome("ervervige"));
        System.out.println(minRemovalsToMakePalindrome("aabab"));
       // System.out.println(minRemovalsToMakePalindrome("x"));
      //  System.out.println(minRemovalsToMakePalindrome("abcda"));
       // System.out.println(minRemovalsToMakePalindrome("aebcbsda"));
    }

    // ============================================
    // SOLUTION 1: Using Longest Palindromic Subsequence (LPS)
    // ============================================

    /**
     * Time Complexity: O(n^2)
     * Space Complexity: O(n^2)
     */
    public static int minRemovalsToMakePalindrome(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();

        // Find longest palindromic subsequence
        int lpsLength = longestPalindromicSubsequence(s);

        // Minimum removals = total length - LPS length
        return n - lpsLength;
    }

    private static int longestPalindromicSubsequence(String s) {
        int n = s.length();

        // dp[i][j] = length of LPS from index i to j (inclusive)
        int[][] dp = new int[n][n];

        // Base case: Every single character is a palindrome of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // Fill the table for substrings of length 2 and more
        // We fill by increasing length of substring
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                if (s.charAt(i) == s.charAt(j)) {
                        // Add 2 (for the matching pair) + LPS of middle part
                        dp[i][j] = 2+ dp[i + 1][j - 1];
                } else {
                    // Characters don't match: take best of excluding either end
                    // Exclude left character (i) or right character (j)
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[0][n - 1];
    }

}
