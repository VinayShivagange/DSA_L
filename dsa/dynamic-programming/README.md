# Dynamic Programming Learning Guide

## Table of Contents
1. [DP Fundamentals](#dp-fundamentals)
2. [DP Patterns](#dp-patterns)
3. [1D Dynamic Programming](#1d-dynamic-programming)
4. [2D Dynamic Programming](#2d-dynamic-programming)
5. [Advanced DP Topics](#advanced-dp-topics)
6. [Common DP Problems](#common-dp-problems)
7. [Practice Problems](#practice-problems)
8. [Code Examples](#code-examples)

---

## DP Fundamentals

### What is Dynamic Programming?
Dynamic Programming is an optimization technique that solves problems by breaking them into overlapping subproblems and storing results to avoid recomputation.

### When to Use DP?
1. **Optimal Substructure**: Optimal solution contains optimal solutions to subproblems
2. **Overlapping Subproblems**: Same subproblems computed multiple times
3. **Memoization**: Store results of expensive function calls

### DP Approaches

**1. Top-Down (Memoization)**
- Recursive with caching
- Natural problem decomposition
- May have stack overflow for deep recursion

**2. Bottom-Up (Tabulation)**
- Iterative, build from base cases
- Better space optimization
- More control over computation order

---

## DP Patterns

### Pattern 1: Fibonacci Sequence
**Problem**: Calculate nth Fibonacci number

**Recursive (Naive):**
```python
def fib(n):
    if n <= 1:
        return n
    return fib(n-1) + fib(n-2)
# Time: O(2^n), Space: O(n)
```

**Memoization:**
```python
def fib_memo(n, memo={}):
    if n in memo:
        return memo[n]
    if n <= 1:
        return n
    memo[n] = fib_memo(n-1, memo) + fib_memo(n-2, memo)
    return memo[n]
# Time: O(n), Space: O(n)
```

```java
private Map<Integer, Integer> memo = new HashMap<>();

public int fibMemo(int n) {
    if (memo.containsKey(n)) {
        return memo.get(n);
    }
    if (n <= 1) {
        return n;
    }
    int result = fibMemo(n - 1) + fibMemo(n - 2);
    memo.put(n, result);
    return result;
}
// Time: O(n), Space: O(n)
```

**Tabulation:**
```python
def fib_tab(n):
    if n <= 1:
        return n
    dp = [0] * (n + 1)
    dp[1] = 1
    for i in range(2, n + 1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]
# Time: O(n), Space: O(n)
```

```java
public int fibTab(int n) {
    if (n <= 1) {
        return n;
    }
    int[] dp = new int[n + 1];
    dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
// Time: O(n), Space: O(n)
```

**Space Optimized:**
```python
def fib_optimized(n):
    if n <= 1:
        return n
    prev2, prev1 = 0, 1
    for i in range(2, n + 1):
        curr = prev1 + prev2
        prev2, prev1 = prev1, curr
    return prev1
# Time: O(n), Space: O(1)
```

```java
public int fibOptimized(int n) {
    if (n <= 1) {
        return n;
    }
    int prev2 = 0, prev1 = 1;
    for (int i = 2; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
// Time: O(n), Space: O(1)
```

---

## 1D Dynamic Programming

### Pattern 2: Climbing Stairs
**Problem**: Ways to reach nth step (1 or 2 steps at a time)

```python
def climbStairs(n):
    if n <= 2:
        return n
    dp = [0] * (n + 1)
    dp[1] = 1
    dp[2] = 2
    for i in range(3, n + 1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]

# Space optimized
def climbStairs_opt(n):
    if n <= 2:
        return n
    prev2, prev1 = 1, 2
    for i in range(3, n + 1):
        curr = prev1 + prev2
        prev2, prev1 = prev1, curr
    return prev1
```

```java
public int climbStairs(int n) {
    if (n <= 2) {
        return n;
    }
    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;
    for (int i = 3; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}

// Space optimized
public int climbStairsOpt(int n) {
    if (n <= 2) {
        return n;
    }
    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### Pattern 3: House Robber
**Problem**: Maximum money without robbing adjacent houses

```python
def rob(nums):
    if not nums:
        return 0
    if len(nums) == 1:
        return nums[0]
    
    dp = [0] * len(nums)
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])
    
    for i in range(2, len(nums)):
        dp[i] = max(dp[i-1], dp[i-2] + nums[i])
    
    return dp[-1]

# Space optimized
def rob_opt(nums):
    if not nums:
        return 0
    prev2, prev1 = 0, 0
    for num in nums:
        curr = max(prev1, prev2 + num)
        prev2, prev1 = prev1, curr
    return prev1
```

```java
public int rob(int[] nums) {
    if (nums.length == 0) {
        return 0;
    }
    if (nums.length == 1) {
        return nums[0];
    }
    
    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    dp[1] = Math.max(nums[0], nums[1]);
    
    for (int i = 2; i < nums.length; i++) {
        dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
    }
    
    return dp[nums.length - 1];
}

// Space optimized
public int robOpt(int[] nums) {
    int prev2 = 0, prev1 = 0;
    for (int num : nums) {
        int curr = Math.max(prev1, prev2 + num);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### Pattern 4: Coin Change
**Problem**: Minimum coins to make amount (unbounded knapsack)

```python
def coinChange(coins, amount):
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] != float('inf') else -1
```

```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            if (dp[i - coin] != Integer.MAX_VALUE) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
}
```

### Pattern 5: Longest Increasing Subsequence
**Problem**: Length of longest increasing subsequence

**O(n²) Solution:**
```python
def lengthOfLIS(nums):
    n = len(nums)
    dp = [1] * n
    
    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
    
    return max(dp)
```

**O(n log n) Solution (Binary Search):**
```python
import bisect

def lengthOfLIS_opt(nums):
    tails = []
    for num in nums:
        pos = bisect.bisect_left(tails, num)
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num
    return len(tails)
```

### Pattern 6: Word Break
**Problem**: Check if string can be segmented using dictionary

```python
def wordBreak(s, wordDict):
    wordSet = set(wordDict)
    dp = [False] * (len(s) + 1)
    dp[0] = True
    
    for i in range(1, len(s) + 1):
        for j in range(i):
            if dp[j] and s[j:i] in wordSet:
                dp[i] = True
                break
    
    return dp[len(s)]
```

### Pattern 7: Decode Ways
**Problem**: Ways to decode digit string to letters (1-26)

```python
def numDecodings(s):
    if not s or s[0] == '0':
        return 0
    
    n = len(s)
    dp = [0] * (n + 1)
    dp[0] = 1
    dp[1] = 1
    
    for i in range(2, n + 1):
        # Single digit
        if s[i-1] != '0':
            dp[i] += dp[i-1]
        # Two digits
        two_digit = int(s[i-2:i])
        if 10 <= two_digit <= 26:
            dp[i] += dp[i-2]
    
    return dp[n]
```

---

## 2D Dynamic Programming

### Pattern 8: Unique Paths
**Problem**: Number of unique paths from top-left to bottom-right

```python
def uniquePaths(m, n):
    dp = [[1] * n for _ in range(m)]
    
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    return dp[m-1][n-1]

# Space optimized
def uniquePaths_opt(m, n):
    prev = [1] * n
    for i in range(1, m):
        curr = [1] * n
        for j in range(1, n):
            curr[j] = prev[j] + curr[j-1]
        prev = curr
    return prev[n-1]
```

```java
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    for (int i = 0; i < m; i++) {
        dp[i][0] = 1;
    }
    for (int j = 0; j < n; j++) {
        dp[0][j] = 1;
    }
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
        }
    }
    
    return dp[m - 1][n - 1];
}

// Space optimized
public int uniquePathsOpt(int m, int n) {
    int[] prev = new int[n];
    Arrays.fill(prev, 1);
    
    for (int i = 1; i < m; i++) {
        int[] curr = new int[n];
        curr[0] = 1;
        for (int j = 1; j < n; j++) {
            curr[j] = prev[j] + curr[j - 1];
        }
        prev = curr;
    }
    return prev[n - 1];
}
```

### Pattern 9: Minimum Path Sum
**Problem**: Minimum sum path from top-left to bottom-right

```python
def minPathSum(grid):
    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]
    dp[0][0] = grid[0][0]
    
    # First row
    for j in range(1, n):
        dp[0][j] = dp[0][j-1] + grid[0][j]
    
    # First column
    for i in range(1, m):
        dp[i][0] = dp[i-1][0] + grid[i][0]
    
    # Fill rest
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
    
    return dp[m-1][n-1]
```

```java
public int minPathSum(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    int[][] dp = new int[m][n];
    dp[0][0] = grid[0][0];
    
    // First row
    for (int j = 1; j < n; j++) {
        dp[0][j] = dp[0][j - 1] + grid[0][j];
    }
    
    // First column
    for (int i = 1; i < m; i++) {
        dp[i][0] = dp[i - 1][0] + grid[i][0];
    }
    
    // Fill rest
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
        }
    }
    
    return dp[m - 1][n - 1];
}
```

### Pattern 10: Edit Distance
**Problem**: Minimum operations to convert word1 to word2

```python
def minDistance(word1, word2):
    m, n = len(word1), len(word2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Base cases
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = 1 + min(
                    dp[i-1][j],      # Delete
                    dp[i][j-1],      # Insert
                    dp[i-1][j-1]     # Replace
                )
    
    return dp[m][n]
```

```java
public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    // Base cases
    for (int i = 0; i <= m; i++) {
        dp[i][0] = i;
    }
    for (int j = 0; j <= n; j++) {
        dp[0][j] = j;
    }
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(
                    Math.min(dp[i - 1][j],      // Delete
                             dp[i][j - 1]),    // Insert
                    dp[i - 1][j - 1]           // Replace
                );
            }
        }
    }
    
    return dp[m][n];
}
```

### Pattern 11: Longest Common Subsequence
**Problem**: Length of longest common subsequence

```python
def longestCommonSubsequence(text1, text2):
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]

# Space optimized
def longestCommonSubsequence_opt(text1, text2):
    m, n = len(text1), len(text2)
    prev = [0] * (n + 1)
    
    for i in range(1, m + 1):
        curr = [0] * (n + 1)
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                curr[j] = prev[j-1] + 1
            else:
                curr[j] = max(prev[j], curr[j-1])
        prev = curr
    
    return prev[n]
```

```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length();
    int n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }
    
    return dp[m][n];
}

// Space optimized
public int longestCommonSubsequenceOpt(String text1, String text2) {
    int m = text1.length();
    int n = text2.length();
    int[] prev = new int[n + 1];
    
    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                curr[j] = prev[j - 1] + 1;
            } else {
                curr[j] = Math.max(prev[j], curr[j - 1]);
            }
        }
        prev = curr;
    }
    
    return prev[n];
}
```

### Pattern 12: Regular Expression Matching
**Problem**: Match pattern with '.' and '*'

```python
def isMatch(s, p):
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True
    
    # Handle patterns like a*b*c*
    for j in range(2, n + 1):
        if p[j-1] == '*':
            dp[0][j] = dp[0][j-2]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j-1] == s[i-1] or p[j-1] == '.':
                dp[i][j] = dp[i-1][j-1]
            elif p[j-1] == '*':
                dp[i][j] = dp[i][j-2]  # Zero occurrences
                if p[j-2] == s[i-1] or p[j-2] == '.':
                    dp[i][j] = dp[i][j] or dp[i-1][j]
    
    return dp[m][n]
```

---

## Advanced DP Topics

### Pattern 13: Knapsack Problems

**0/1 Knapsack:**
```python
def knapsack01(weights, values, capacity):
    n = len(weights)
    dp = [[0] * (capacity + 1) for _ in range(n + 1)]
    
    for i in range(1, n + 1):
        for w in range(1, capacity + 1):
            if weights[i-1] <= w:
                dp[i][w] = max(
                    dp[i-1][w],
                    dp[i-1][w - weights[i-1]] + values[i-1]
                )
            else:
                dp[i][w] = dp[i-1][w]
    
    return dp[n][capacity]

# Space optimized
def knapsack01_opt(weights, values, capacity):
    dp = [0] * (capacity + 1)
    for i in range(len(weights)):
        for w in range(capacity, weights[i] - 1, -1):
            dp[w] = max(dp[w], dp[w - weights[i]] + values[i])
    return dp[capacity]
```

**Unbounded Knapsack:**
```python
def unboundedKnapsack(weights, values, capacity):
    dp = [0] * (capacity + 1)
    for w in range(1, capacity + 1):
        for i in range(len(weights)):
            if weights[i] <= w:
                dp[w] = max(dp[w], dp[w - weights[i]] + values[i])
    return dp[capacity]
```

### Pattern 14: Partition Problems

**Partition Equal Subset Sum:**
```python
def canPartition(nums):
    total = sum(nums)
    if total % 2 != 0:
        return False
    target = total // 2
    
    dp = [False] * (target + 1)
    dp[0] = True
    
    for num in nums:
        for j in range(target, num - 1, -1):
            dp[j] = dp[j] or dp[j - num]
    
    return dp[target]
```

### Pattern 15: State Machine DP

**Best Time to Buy/Sell Stock with Cooldown:**
```python
def maxProfit(prices):
    if not prices:
        return 0
    
    # States: hold, sold, rest
    hold = -prices[0]
    sold = 0
    rest = 0
    
    for i in range(1, len(prices)):
        prev_hold, prev_sold, prev_rest = hold, sold, rest
        hold = max(prev_hold, prev_rest - prices[i])
        sold = prev_hold + prices[i]
        rest = max(prev_rest, prev_sold)
    
    return max(sold, rest)
```

### Pattern 16: Interval DP

**Burst Balloons:**
```python
def maxCoins(nums):
    nums = [1] + nums + [1]
    n = len(nums)
    dp = [[0] * n for _ in range(n)]
    
    for length in range(2, n):
        for left in range(n - length):
            right = left + length
            for i in range(left + 1, right):
                dp[left][right] = max(
                    dp[left][right],
                    nums[left] * nums[i] * nums[right] + 
                    dp[left][i] + dp[i][right]
                )
    
    return dp[0][n-1]
```

---

## Common DP Problems

### Easy
1. Climbing Stairs
2. House Robber
3. Fibonacci Number
4. Min Cost Climbing Stairs
5. Tribonacci Number

### Medium
6. Coin Change
7. Longest Increasing Subsequence
8. Word Break
9. Decode Ways
10. Unique Paths
11. Minimum Path Sum
12. Edit Distance
13. Longest Common Subsequence
14. Maximum Product Subarray
15. Jump Game

### Hard
16. Regular Expression Matching
17. Wildcard Matching
18. Burst Balloons
19. Russian Doll Envelopes
20. Best Time to Buy/Sell Stock with Cooldown

---

## Practice Problems

### Week 1: 1D DP Basics
- Climbing Stairs
- House Robber
- Coin Change
- Word Break
- Decode Ways

### Week 2: 1D DP Advanced
- Longest Increasing Subsequence
- Maximum Product Subarray
- Jump Game
- Partition Equal Subset Sum
- Target Sum

### Week 3: 2D DP Basics
- Unique Paths
- Minimum Path Sum
- Edit Distance
- Longest Common Subsequence
- Interleaving String

### Week 4: 2D DP Advanced
- Regular Expression Matching
- Wildcard Matching
- Distinct Subsequences
- Edit Distance (variations)
- Longest Palindromic Subsequence

### Week 5: Advanced Patterns
- Knapsack problems
- State machine DP
- Interval DP
- Tree DP
- Bitmask DP

---

## Code Examples

### Complete DP Template

```python
class DPSolver:
    def __init__(self):
        self.memo = {}
    
    def solve_top_down(self, problem, *args):
        """Top-down with memoization"""
        key = (problem, args)
        if key in self.memo:
            return self.memo[key]
        
        # Base cases
        if base_case(args):
            return base_value
        
        # Recursive case
        result = recursive_solution(args)
        self.memo[key] = result
        return result
    
    def solve_bottom_up(self, n):
        """Bottom-up tabulation"""
        dp = [0] * (n + 1)
        dp[0] = base_value
        
        for i in range(1, n + 1):
            dp[i] = compute(dp, i)
        
        return dp[n]
    
    def solve_space_optimized(self, n):
        """Space-optimized version"""
        prev2, prev1 = base1, base2
        
        for i in range(2, n + 1):
            curr = compute(prev2, prev1)
            prev2, prev1 = prev1, curr
        
        return prev1
```

### DP Problem Solver Framework

```python
def solve_dp_problem(problem_type, inputs):
    """
    Framework for solving DP problems:
    1. Identify subproblems
    2. Define state
    3. Find recurrence relation
    4. Determine base cases
    5. Choose approach (top-down/bottom-up)
    6. Optimize space if possible
    """
    
    # Step 1: Define state
    # dp[i] or dp[i][j] represents what?
    
    # Step 2: Base cases
    # What are the smallest subproblems?
    
    # Step 3: Recurrence relation
    # How to compute dp[i] from previous states?
    
    # Step 4: Implementation
    if problem_type == "1D":
        return solve_1d_dp(inputs)
    elif problem_type == "2D":
        return solve_2d_dp(inputs)
    # ...
```

---

## Learning Strategy

### Step 1: Understand the Pattern
- Recognize DP problems
- Identify overlapping subproblems
- Find optimal substructure

### Step 2: Define State
- What does dp[i] represent?
- What information do we need to track?

### Step 3: Find Recurrence
- How to compute current state from previous?
- What are the transitions?

### Step 4: Base Cases
- Smallest subproblems
- Edge cases

### Step 5: Implementation
- Start with top-down (easier to think)
- Convert to bottom-up (more efficient)
- Optimize space if possible

### Step 6: Practice
- Solve similar problems
- Recognize patterns
- Build intuition

---

## Tips for Interviews

1. **Recognize DP**: Look for optimization, overlapping subproblems
2. **Start Simple**: Brute force first, then optimize
3. **Draw it Out**: Visualize the state space
4. **Think Recursively**: Natural way to express recurrence
5. **Optimize Later**: Get working solution first
6. **Space Optimization**: Often asked as follow-up
7. **Edge Cases**: Empty inputs, single element, etc.

---

## Common Mistakes

1. **Wrong State Definition**: dp[i] doesn't represent what you think
2. **Missing Base Cases**: Forgot to handle smallest subproblems
3. **Off-by-One Errors**: Indexing mistakes
4. **Not Optimizing**: Using O(n²) space when O(n) possible
5. **Wrong Recurrence**: Incorrect transition relation

---

## Resources

- LeetCode DP Tag (200+ problems)
- Dynamic Programming Patterns (NeetCode)
- DP Playlist (YouTube)
- Practice: Focus on pattern recognition

---

## Key Takeaways

1. **DP = Recursion + Memoization**
2. **Pattern Recognition** is crucial
3. **State Definition** is the hardest part
4. **Practice** builds intuition
5. **Space Optimization** is often possible
6. **Start Top-Down**, optimize to Bottom-Up
