package org.example.codesignal;

import java.util.*;

/**
 * eBay CodeSignal Practice Problems
 * 
 * Common problem types asked in eBay interviews
 */
public class eBayPracticeProblems {
    
    // ============================================
    // Problem 1: Shopping Cart Total
    // ============================================
    
    /**
     * Calculate total with quantity-based discounts
     * Q3 type problem - follow instructions literally
     */
    public static double calculateCartTotal(double[] prices, int[] quantities, 
                                           Map<Integer, Double> discountRules) {
        double total = 0;
        for (int i = 0; i < prices.length; i++) {
            double itemTotal = prices[i] * quantities[i];
            double discount = discountRules.getOrDefault(quantities[i], 0.0);
            total += itemTotal * (1 - discount);
        }
        return total;
    }
    
    // ============================================
    // Problem 2: Product Search Ranking
    // ============================================
    
    /**
     * Rank products by relevance score
     * Q2 type problem
     */
    public static String[] rankProducts(String[] products, int[] scores) {
        int n = products.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        Arrays.sort(indices, (a, b) -> {
            if (scores[b] != scores[a]) {
                return scores[b] - scores[a]; // Higher score first
            }
            return products[a].compareTo(products[b]); // Alphabetical
        });
        
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = products[indices[i]];
        }
        return result;
    }
    
    // ============================================
    // Problem 3: Transaction Validation
    // ============================================
    
    /**
     * Validate if transactions can be processed
     * Check if all transactions have sufficient balance
     */
    public static boolean validateTransactions(int[] balances, int[][] transactions) {
        Map<Integer, Integer> accountBalances = new HashMap<>();
        for (int i = 0; i < balances.length; i++) {
            accountBalances.put(i, balances[i]);
        }
        
        for (int[] txn : transactions) {
            int from = txn[0];
            int to = txn[1];
            int amount = txn[2];
            
            if (accountBalances.get(from) < amount) {
                return false;
            }
            accountBalances.put(from, accountBalances.get(from) - amount);
            accountBalances.put(to, accountBalances.getOrDefault(to, 0) + amount);
        }
        return true;
    }
    
    // ============================================
    // Problem 4: Find Duplicate Products (Q4 Pattern)
    // ============================================
    
    /**
     * Find products with same price and category
     * Q4 type - HashMap optimization
     */
    public static List<List<String>> findDuplicateProducts(String[] products, 
                                                          double[] prices, 
                                                          String[] categories) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (int i = 0; i < products.length; i++) {
            String key = prices[i] + "_" + categories[i];
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(products[i]);
        }
        
        List<List<String>> result = new ArrayList<>();
        for (List<String> group : map.values()) {
            if (group.size() > 1) {
                result.add(group);
            }
        }
        return result;
    }
    
    // ============================================
    // Problem 5: Inventory Reorder Point
    // ============================================
    
    /**
     * Determine when to reorder based on stock levels
     */
    public static int[] getReorderPoints(int[] currentStock, int[] dailySales, 
                                        int reorderThreshold) {
        int[] result = new int[currentStock.length];
        
        for (int i = 0; i < currentStock.length; i++) {
            int daysUntilReorder = 0;
            int stock = currentStock[i];
            
            while (stock > reorderThreshold && daysUntilReorder < 365) {
                stock -= dailySales[i];
                daysUntilReorder++;
            }
            
            result[i] = daysUntilReorder;
        }
        return result;
    }
    
    // ============================================
    // Problem 6: Shipping Cost Calculation
    // ============================================
    
    /**
     * Calculate shipping cost based on weight and distance
     * Q3 type - implementation
     */
    public static double calculateShipping(double[] weights, double[] distances,
                                          double baseRate, double ratePerKg,
                                          double ratePerKm) {
        double total = 0;
        for (int i = 0; i < weights.length; i++) {
            double cost = baseRate + (weights[i] * ratePerKg) + (distances[i] * ratePerKm);
            total += cost;
        }
        return total;
    }
    
    // ============================================
    // Problem 7: Top Sellers (Q4 Pattern)
    // ============================================
    
    /**
     * Find top N sellers by total sales
     * HashMap + Sorting
     */
    public static String[] topSellers(String[] sellers, int[] sales, int n) {
        Map<String, Integer> sellerSales = new HashMap<>();
        
        for (int i = 0; i < sellers.length; i++) {
            sellerSales.put(sellers[i], 
                sellerSales.getOrDefault(sellers[i], 0) + sales[i]);
        }
        
        return sellerSales.entrySet().stream()
            .sorted((a, b) -> {
                if (!a.getValue().equals(b.getValue())) {
                    return b.getValue() - a.getValue();
                }
                return a.getKey().compareTo(b.getKey());
            })
            .limit(n)
            .map(Map.Entry::getKey)
            .toArray(String[]::new);
    }
    
    // ============================================
    // Problem 8: Coupon Code Validation
    // ============================================
    
    /**
     * Validate and apply coupon codes
     */
    public static double applyCoupon(double total, String couponCode,
                                    Map<String, CouponRule> couponRules) {
        CouponRule rule = couponRules.get(couponCode);
        if (rule == null || total < rule.minPurchase) {
            return total;
        }
        
        double discount = 0;
        if (rule.type == DiscountType.PERCENTAGE) {
            discount = total * rule.value / 100.0;
            discount = Math.min(discount, rule.maxDiscount);
        } else {
            discount = Math.min(rule.value, total);
        }
        
        return total - discount;
    }
    
    enum DiscountType { PERCENTAGE, FIXED }
    
    static class CouponRule {
        double minPurchase;
        DiscountType type;
        double value;
        double maxDiscount;
        
        CouponRule(double minPurchase, DiscountType type, double value, double maxDiscount) {
            this.minPurchase = minPurchase;
            this.type = type;
            this.value = value;
            this.maxDiscount = maxDiscount;
        }
    }
    
    // ============================================
    // Problem 9: Product Recommendations
    // ============================================
    
    /**
     * Find products frequently bought together
     * Q4 pattern - count co-occurrences
     */
    public static Map<String, List<String>> getRecommendations(String[][] purchaseHistory) {
        Map<String, Set<String>> productUsers = new HashMap<>();
        
        // Build user-product map
        for (int i = 0; i < purchaseHistory.length; i++) {
            String[] products = purchaseHistory[i];
            for (String product : products) {
                productUsers.computeIfAbsent(product, k -> new HashSet<>())
                    .add("user" + i);
            }
        }
        
        // Find co-occurrences
        Map<String, Map<String, Integer>> coOccurrences = new HashMap<>();
        for (String[] products : purchaseHistory) {
            for (int i = 0; i < products.length; i++) {
                for (int j = i + 1; j < products.length; j++) {
                    String p1 = products[i];
                    String p2 = products[j];
                    coOccurrences.computeIfAbsent(p1, k -> new HashMap<>())
                        .put(p2, coOccurrences.get(p1).getOrDefault(p2, 0) + 1);
                    coOccurrences.computeIfAbsent(p2, k -> new HashMap<>())
                        .put(p1, coOccurrences.get(p2).getOrDefault(p1, 0) + 1);
                }
            }
        }
        
        // Build recommendations
        Map<String, List<String>> recommendations = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : coOccurrences.entrySet()) {
            List<String> top = entry.getValue().entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
            recommendations.put(entry.getKey(), top);
        }
        
        return recommendations;
    }
    
    // ============================================
    // Problem 10: Price Matching
    // ============================================
    
    /**
     * Find products with price difference within threshold
     * Q4 pattern - group by price range
     */
    public static List<List<String>> findPriceMatches(String[] products, 
                                                     double[] prices, 
                                                     double threshold) {
        Map<String, List<String>> groups = new HashMap<>();
        
        for (int i = 0; i < products.length; i++) {
            // Round price to threshold for grouping
            long priceGroup = Math.round(prices[i] / threshold);
            String key = String.valueOf(priceGroup);
            groups.computeIfAbsent(key, k -> new ArrayList<>())
                .add(products[i] + ":" + prices[i]);
        }
        
        return new ArrayList<>(groups.values());
    }
    
    // ============================================
    // Main - Testing
    // ============================================
    
    public static void main(String[] args) {
        System.out.println("=== eBay CodeSignal Practice Problems ===\n");
        
        // Test 1: Shopping Cart
        System.out.println("1. Shopping Cart Total:");
        double[] prices = {10.0, 20.0, 15.0};
        int[] quantities = {2, 1, 3};
        Map<Integer, Double> discounts = Map.of(2, 0.1, 3, 0.15);
        System.out.println("Total: $" + calculateCartTotal(prices, quantities, discounts));
        System.out.println();
        
        // Test 2: Top Sellers
        System.out.println("2. Top Sellers:");
        String[] sellers = {"A", "B", "A", "C", "B"};
        int[] sales = {100, 200, 150, 50, 300};
        System.out.println("Top 2: " + Arrays.toString(topSellers(sellers, sales, 2)));
        System.out.println();
        
        // Test 3: Price Matches
        System.out.println("3. Price Matches:");
        String[] products = {"P1", "P2", "P3", "P4"};
        double[] productPrices = {10.5, 10.3, 20.1, 20.2};
        System.out.println("Matches: " + findPriceMatches(products, productPrices, 1.0));
        System.out.println();
        
        System.out.println("Practice these patterns for eBay CodeSignal!");
    }
}
