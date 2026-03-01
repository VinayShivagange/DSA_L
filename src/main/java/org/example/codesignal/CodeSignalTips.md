# CodeSignal Test Preparation - eBay Focused

## Test Structure

- **Q1**: Easy array/string problem (5-10 min)
- **Q2**: Medium array/string problem (10-15 min)
- **Q3**: Implementation-heavy, follow instructions (20-30 min)
- **Q4**: HashMap optimization pattern (20-30 min)

**Total Time**: ~70 minutes (aim for 40-50 for better score bonus)

## Common Patterns

### Pattern 1: Two Pointers
```java
// Template
int left = 0, right = arr.length - 1;
while (left < right) {
    // Process
    if (condition) left++;
    else right--;
}
```

**Problems**: Two Sum (sorted), Remove Duplicates, Valid Palindrome

### Pattern 2: Sliding Window
```java
// Template
int windowSum = 0;
for (int i = 0; i < k; i++) windowSum += arr[i];
int maxSum = windowSum;

for (int i = k; i < arr.length; i++) {
    windowSum = windowSum - arr[i-k] + arr[i];
    maxSum = Math.max(maxSum, windowSum);
}
```

**Problems**: Max Sum Subarray, Longest Substring, Minimum Window

### Pattern 3: HashMap Optimization (Q4 Pattern)
```java
// Instead of O(n²) nested loops:
for (int i = 0; i < n; i++) {
    for (int j = i+1; j < n; j++) {
        // Check condition
    }
}

// Use HashMap O(n):
Map<Key, Value> map = new HashMap<>();
for (int i = 0; i < n; i++) {
    // Check complement in map
    if (map.containsKey(complement)) {
        // Found pair
    }
    map.put(key, value);
}
```

**Key Insight**: Rearrange equation to find what to store in HashMap
- `arr[i] + arr[j] = target` → Store `target - arr[i]`
- `arr[i] - arr[j] = k` → Store `arr[i] - k`
- `arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])` → Store `arr[i] - rev(arr[i])`

### Pattern 4: Prefix Sum
```java
// Template
Map<Integer, Integer> prefixSum = new HashMap<>();
prefixSum.put(0, 1);
int sum = 0;

for (int num : nums) {
    sum += num;
    if (prefixSum.containsKey(sum - target)) {
        count += prefixSum.get(sum - target);
    }
    prefixSum.put(sum, prefixSum.getOrDefault(sum, 0) + 1);
}
```

**Problems**: Subarray Sum Equals K, Contiguous Array

### Pattern 5: Stack
```java
// Template
Stack<Type> stack = new Stack<>();
for (element : elements) {
    while (!stack.isEmpty() && condition) {
        stack.pop();
    }
    stack.push(element);
}
```

**Problems**: Valid Parentheses, Next Greater Element, Daily Temperatures

## Q3 Strategy: Follow Instructions Literally

**Don't overthink Q3!** It's usually just implementation:
1. Read requirements carefully
2. Implement step by step
3. Don't look for clever algorithms
4. If stuck, move to Q4 and come back

**Common Q3 Topics**:
- Shopping cart calculations
- Inventory management
- String processing
- Array transformations

## Q4 Strategy: HashMap Optimization

**Almost every Q4 can be solved with HashMap**:
1. Identify the O(n²) pattern
2. Rearrange the equation/condition
3. Store calculated values in HashMap
4. Look up complements/differences

**Example Pattern**:
```java
// Problem: Count pairs where arr[i] + rev(arr[j]) = arr[j] + rev(arr[i])
// Rearrange: arr[i] - rev(arr[i]) = arr[j] - rev(arr[j])
// Solution: Count frequencies of differences

Map<Integer, Long> diffFreq = new HashMap<>();
for (int num : arr) {
    int diff = num - reverse(num);
    diffFreq.put(diff, diffFreq.getOrDefault(diff, 0L) + 1);
}
```

## eBay-Specific Patterns

### Shopping Cart Logic
- Discount calculations
- Tax computation
- Coupon application
- Bulk pricing

### Inventory Management
- Arrivals/departures tracking
- Max inventory needed
- Capacity planning

### Search/Autocomplete
- Trie data structure
- Prefix matching
- String search

## Time Management Tips

1. **Q1-Q2**: Solve quickly (15-20 min total)
2. **Q3**: Don't overthink, implement (20-30 min)
3. **Q4**: Focus on HashMap pattern (20-30 min)
4. **Speed Bonus**: Finishing in 40-50 min gives better score

## Common Mistakes to Avoid

1. **Overthinking Q3**: Just follow instructions
2. **Missing Q4 optimization**: Always look for HashMap solution
3. **Not handling edge cases**: Empty arrays, null values
4. **Off-by-one errors**: Check array bounds
5. **Not reading constraints**: Check if array is sorted, etc.

## Quick Reference

### HashMap Common Operations
```java
// Check if exists
if (map.containsKey(key)) { }

// Get with default
int value = map.getOrDefault(key, 0);

// Increment frequency
map.put(key, map.getOrDefault(key, 0) + 1);

// Group by key
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
```

### Array Operations
```java
// Sort
Arrays.sort(arr);

// Binary search
int index = Arrays.binarySearch(arr, target);

// Fill
Arrays.fill(arr, value);

// Copy
int[] copy = Arrays.copyOf(arr, arr.length);
```

### String Operations
```java
// Split
String[] parts = str.split(" ");

// Substring
String sub = str.substring(start, end);

// Character array
char[] chars = str.toCharArray();

// Reverse
String reversed = new StringBuilder(str).reverse().toString();
```

## Practice Problems

### Must Practice:
1. Two Sum (all variations)
2. Sliding Window problems
3. Subarray Sum problems
4. HashMap optimization problems
5. String manipulation

### eBay-Specific:
1. Shopping cart calculations
2. Inventory tracking
3. Search/autocomplete
4. Transaction processing

## Final Tips

1. **Read problem carefully**: Understand constraints
2. **Start with brute force**: Then optimize
3. **Test edge cases**: Empty, single element, duplicates
4. **Use meaningful variable names**: Helps debugging
5. **Comment complex logic**: Shows understanding
6. **Speed matters**: But correctness first!

Good luck! 🚀
