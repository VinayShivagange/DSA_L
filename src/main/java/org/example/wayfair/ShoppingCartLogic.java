package org.example.wayfair;

import java.util.*;

/**
 * Shopping Cart Logic: Calculating discounts or tax based on arrays
 * 
 * Common problems in e-commerce interviews:
 * - Apply discounts based on quantity thresholds
 * - Calculate tax based on item categories
 * - Apply coupon codes with conditions
 * - Calculate shipping costs
 * - Handle bulk pricing
 */
public class ShoppingCartLogic {
    
    /**
     * Calculate total with quantity-based discounts
     * Discount rules:
     * - Buy 2, get 10% off
     * - Buy 5, get 20% off
     * - Buy 10, get 30% off
     */
    public static double calculateTotalWithQuantityDiscount(double[] prices, int[] quantities) {
        double total = 0;
        
        for (int i = 0; i < prices.length; i++) {
            double itemTotal = prices[i] * quantities[i];
            double discount = 0;
            
            if (quantities[i] >= 10) {
                discount = 0.30; // 30% off
            } else if (quantities[i] >= 5) {
                discount = 0.20; // 20% off
            } else if (quantities[i] >= 2) {
                discount = 0.10; // 10% off
            }
            
            total += itemTotal * (1 - discount);
        }
        
        return total;
    }
    
    /**
     * Calculate total with category-based tax
     * Different categories have different tax rates
     */
    public static double calculateTotalWithCategoryTax(
            double[] prices, 
            String[] categories, 
            Map<String, Double> taxRates) {
        
        double total = 0;
        
        for (int i = 0; i < prices.length; i++) {
            double taxRate = taxRates.getOrDefault(categories[i], 0.0);
            total += prices[i] * (1 + taxRate);
        }
        
        return total;
    }
    
    /**
     * Apply coupon code with conditions
     * Conditions: minimum purchase amount, applicable categories, etc.
     */
    public static double applyCoupon(
            double[] prices,
            String[] categories,
            String couponCode,
            Map<String, CouponRule> couponRules) {
        
        double subtotal = Arrays.stream(prices).sum();
        CouponRule rule = couponRules.get(couponCode);
        
        if (rule == null) {
            return subtotal; // Invalid coupon
        }
        
        // Check minimum purchase
        if (subtotal < rule.minPurchase) {
            return subtotal;
        }
        
        // Calculate discount based on applicable items
        double discountAmount = 0;
        
        if (rule.discountType == DiscountType.PERCENTAGE) {
            if (rule.applicableCategories.isEmpty()) {
                // Apply to all items
                discountAmount = subtotal * rule.discountValue / 100.0;
            } else {
                // Apply only to specific categories
                double applicableTotal = 0;
                for (int i = 0; i < prices.length; i++) {
                    if (rule.applicableCategories.contains(categories[i])) {
                        applicableTotal += prices[i];
                    }
                }
                discountAmount = applicableTotal * rule.discountValue / 100.0;
            }
        } else {
            // Fixed amount discount
            discountAmount = Math.min(rule.discountValue, subtotal);
        }
        
        // Apply maximum discount cap if exists
        if (rule.maxDiscount > 0) {
            discountAmount = Math.min(discountAmount, rule.maxDiscount);
        }
        
        return subtotal - discountAmount;
    }
    
    /**
     * Calculate shipping cost based on weight and distance
     */
    public static double calculateShipping(
            double[] weights,
            double baseRate,
            double ratePerKg,
            double[] distances) {
        
        double shippingTotal = 0;
        
        for (int i = 0; i < weights.length; i++) {
            double cost = baseRate + (weights[i] * ratePerKg);
            // Distance-based surcharge
            if (distances[i] > 100) {
                cost += (distances[i] - 100) * 0.5; // $0.5 per km over 100km
            }
            shippingTotal += cost;
        }
        
        return shippingTotal;
    }
    
    /**
     * Bulk pricing: Different prices based on quantity tiers
     */
    public static double calculateBulkPricing(
            double basePrice,
            int quantity,
            List<BulkTier> tiers) {
        
        // Sort tiers by quantity (descending)
        tiers.sort((a, b) -> Integer.compare(b.minQuantity, a.minQuantity));
        
        double total = 0;
        int remaining = quantity;
        
        for (BulkTier tier : tiers) {
            if (remaining >= tier.minQuantity) {
                int tierQuantity = remaining - tier.minQuantity + 1;
                total += tierQuantity * tier.price;
                remaining = tier.minQuantity - 1;
            }
        }
        
        // Remaining items at base price
        if (remaining > 0) {
            total += remaining * basePrice;
        }
        
        return total;
    }
    
    /**
     * Calculate final total with all factors
     */
    public static double calculateFinalTotal(
            double[] prices,
            int[] quantities,
            String[] categories,
            double[] weights,
            Map<String, Double> taxRates,
            String couponCode,
            Map<String, CouponRule> couponRules) {
        
        // Step 1: Calculate item totals
        double[] itemTotals = new double[prices.length];
        for (int i = 0; i < prices.length; i++) {
            itemTotals[i] = prices[i] * quantities[i];
        }
        
        // Step 2: Apply coupon
        double afterCoupon = applyCoupon(itemTotals, categories, couponCode, couponRules);
        
        // Step 3: Add tax to the coupon-adjusted total
        // For simplicity, apply average tax rate to coupon-adjusted total
        // In practice, you'd want to apply tax per item before coupon
        double totalTax = 0;
        for (int i = 0; i < prices.length; i++) {
            double taxRate = taxRates.getOrDefault(categories[i], 0.0);
            totalTax += itemTotals[i] * taxRate;
        }
        
        return afterCoupon + totalTax;
    }
    
    // Helper classes
    enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }
    
    static class CouponRule {
        double minPurchase;
        DiscountType discountType;
        double discountValue;
        double maxDiscount;
        Set<String> applicableCategories;
        
        CouponRule(double minPurchase, DiscountType type, double value, 
                  double maxDiscount, Set<String> categories) {
            this.minPurchase = minPurchase;
            this.discountType = type;
            this.discountValue = value;
            this.maxDiscount = maxDiscount;
            this.applicableCategories = categories;
        }
    }
    
    static class BulkTier {
        int minQuantity;
        double price;
        
        BulkTier(int minQuantity, double price) {
            this.minQuantity = minQuantity;
            this.price = price;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Shopping Cart Logic ===");
        
        // Test 1: Quantity-based discounts
        double[] prices1 = {10.0, 20.0, 15.0};
        int[] quantities1 = {2, 5, 1};
        System.out.println("\n1. Quantity Discounts:");
        System.out.println("Prices: " + Arrays.toString(prices1));
        System.out.println("Quantities: " + Arrays.toString(quantities1));
        System.out.println("Total: $" + calculateTotalWithQuantityDiscount(prices1, quantities1));
        
        // Test 2: Category-based tax
        double[] prices2 = {100.0, 50.0, 200.0};
        String[] categories2 = {"electronics", "clothing", "electronics"};
        Map<String, Double> taxRates = new HashMap<>();
        taxRates.put("electronics", 0.10); // 10% tax
        taxRates.put("clothing", 0.08);    // 8% tax
        System.out.println("\n2. Category-based Tax:");
        System.out.println("Total with tax: $" + 
            calculateTotalWithCategoryTax(prices2, categories2, taxRates));
        
        // Test 3: Coupon application
        double[] prices3 = {100.0, 50.0, 75.0};
        String[] categories3 = {"electronics", "clothing", "electronics"};
        Map<String, CouponRule> couponRules = new HashMap<>();
        couponRules.put("SAVE20", new CouponRule(
            100.0, DiscountType.PERCENTAGE, 20.0, 50.0,
            new HashSet<>(Arrays.asList("electronics"))
        ));
        System.out.println("\n3. Coupon Application:");
        System.out.println("Subtotal: $" + Arrays.stream(prices3).sum());
        System.out.println("After coupon 'SAVE20': $" + 
            applyCoupon(prices3, categories3, "SAVE20", couponRules));
        
        // Test 4: Bulk pricing
        List<BulkTier> tiers = Arrays.asList(
            new BulkTier(10, 8.0),  // 10+ items: $8 each
            new BulkTier(5, 9.0),   // 5-9 items: $9 each
            new BulkTier(2, 9.5)    // 2-4 items: $9.5 each
        );
        System.out.println("\n4. Bulk Pricing:");
        System.out.println("Base price: $10, Quantity: 12");
        System.out.println("Total: $" + calculateBulkPricing(10.0, 12, tiers));
    }
}
