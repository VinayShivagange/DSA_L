package org.example.wayfair;

import java.util.*;

/**
 * Low-Level Design: eBay-style Shopping Cart with SOLID Principles
 * 
 * This design demonstrates:
 * - Single Responsibility Principle (SRP)
 * - Open/Closed Principle (OCP) - Open for extension, closed for modification
 * - Liskov Substitution Principle (LSP)
 * - Interface Segregation Principle (ISP)
 * - Dependency Inversion Principle (DIP)
 * 
 * Design Patterns Used:
 * - Strategy Pattern: For discount strategies
 * - Factory Pattern: For creating discount strategies
 * - Builder Pattern: For constructing complex carts
 */

// ============================================
// Domain Models
// ============================================

/**
 * Represents a single item in the shopping cart
 * SRP: Single responsibility - only holds item data
 */
class CartItem {
    private final String productId;
    private final String name;
    private final double price;
    private final String category;
    private int quantity;
    private final double weight;
    
    public CartItem(String productId, String name, double price, 
                   String category, int quantity, double weight) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.weight = weight;
    }
    
    public double getTotalPrice() {
        return price * quantity;
    }
    
    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public double getWeight() { return weight; }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(productId, cartItem.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}

/**
 * Represents the result of applying a discount
 */
class DiscountResult {
    private final double discountAmount;
    private final String discountDescription;
    
    public DiscountResult(double discountAmount, String discountDescription) {
        this.discountAmount = discountAmount;
        this.discountDescription = discountDescription;
    }
    
    public double getDiscountAmount() { return discountAmount; }
    public String getDiscountDescription() { return discountDescription; }
}

// ============================================
// Strategy Pattern: Discount Strategies
// ============================================

/**
 * Discount Strategy Interface
 * OCP: Open for extension (new discount types), closed for modification
 * ISP: Segregated interface - only discount-related methods
 */
interface DiscountStrategy {
    /**
     * Calculate discount for the given cart
     * @param cart The shopping cart
     * @return Discount result
     */
    DiscountResult calculateDiscount(ShoppingCart cart);
    
    /**
     * Check if this discount is applicable to the cart
     */
    boolean isApplicable(ShoppingCart cart);
}

/**
 * Percentage Discount Strategy
 * SRP: Single responsibility - only handles percentage discounts
 */
class PercentageDiscountStrategy implements DiscountStrategy {
    private final double percentage;
    private final double minPurchaseAmount;
    
    public PercentageDiscountStrategy(double percentage, double minPurchaseAmount) {
        this.percentage = percentage;
        this.minPurchaseAmount = minPurchaseAmount;
    }
    
    @Override
    public DiscountResult calculateDiscount(ShoppingCart cart) {
        double subtotal = cart.getSubtotal();
        if (subtotal < minPurchaseAmount) {
            return new DiscountResult(0, "Minimum purchase not met");
        }
        double discount = subtotal * percentage / 100.0;
        return new DiscountResult(discount, 
            String.format("%.1f%% discount applied", percentage));
    }
    
    @Override
    public boolean isApplicable(ShoppingCart cart) {
        return cart.getSubtotal() >= minPurchaseAmount;
    }
}

/**
 * Fixed Amount Discount Strategy
 */
class FixedAmountDiscountStrategy implements DiscountStrategy {
    private final double discountAmount;
    private final double minPurchaseAmount;
    private final double maxDiscount;
    
    public FixedAmountDiscountStrategy(double discountAmount, 
                                      double minPurchaseAmount, 
                                      double maxDiscount) {
        this.discountAmount = discountAmount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscount = maxDiscount;
    }
    
    @Override
    public DiscountResult calculateDiscount(ShoppingCart cart) {
        double subtotal = cart.getSubtotal();
        if (subtotal < minPurchaseAmount) {
            return new DiscountResult(0, "Minimum purchase not met");
        }
        double discount = Math.min(discountAmount, 
            maxDiscount > 0 ? maxDiscount : subtotal);
        return new DiscountResult(discount, 
            String.format("$%.2f fixed discount applied", discount));
    }
    
    @Override
    public boolean isApplicable(ShoppingCart cart) {
        return cart.getSubtotal() >= minPurchaseAmount;
    }
}

/**
 * Buy X Get Y Discount Strategy
 */
class BuyXGetYDiscountStrategy implements DiscountStrategy {
    private final int buyQuantity;
    private final int getQuantity;
    private final double discountPercentage;
    private final Set<String> applicableCategories;
    
    public BuyXGetYDiscountStrategy(int buyQuantity, int getQuantity, 
                                   double discountPercentage,
                                   Set<String> applicableCategories) {
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.discountPercentage = discountPercentage;
        this.applicableCategories = applicableCategories;
    }
    
    @Override
    public DiscountResult calculateDiscount(ShoppingCart cart) {
        double discount = 0;
        for (CartItem item : cart.getItems()) {
            if (applicableCategories.isEmpty() || 
                applicableCategories.contains(item.getCategory())) {
                int eligibleSets = item.getQuantity() / (buyQuantity + getQuantity);
                double itemDiscount = eligibleSets * getQuantity * 
                    item.getPrice() * discountPercentage / 100.0;
                discount += itemDiscount;
            }
        }
        return new DiscountResult(discount, 
            String.format("Buy %d Get %d discount", buyQuantity, getQuantity));
    }
    
    @Override
    public boolean isApplicable(ShoppingCart cart) {
        return cart.getItems().stream()
            .anyMatch(item -> item.getQuantity() >= buyQuantity + getQuantity);
    }
}

/**
 * Category-based Discount Strategy
 */
class CategoryDiscountStrategy implements DiscountStrategy {
    private final Set<String> categories;
    private final double discountPercentage;
    
    public CategoryDiscountStrategy(Set<String> categories, double discountPercentage) {
        this.categories = categories;
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public DiscountResult calculateDiscount(ShoppingCart cart) {
        double discount = 0;
        for (CartItem item : cart.getItems()) {
            if (categories.contains(item.getCategory())) {
                discount += item.getTotalPrice() * discountPercentage / 100.0;
            }
        }
        return new DiscountResult(discount, 
            String.format("%.1f%% discount on selected categories", discountPercentage));
    }
    
    @Override
    public boolean isApplicable(ShoppingCart cart) {
        return cart.getItems().stream()
            .anyMatch(item -> categories.contains(item.getCategory()));
    }
}

// ============================================
// Factory Pattern: Discount Strategy Factory
// ============================================

/**
 * Factory for creating discount strategies
 * DIP: Depends on abstraction (DiscountStrategy), not concrete implementations
 */
class DiscountStrategyFactory {
    public static DiscountStrategy createPercentageDiscount(
            double percentage, double minPurchase) {
        return new PercentageDiscountStrategy(percentage, minPurchase);
    }
    
    public static DiscountStrategy createFixedDiscount(
            double amount, double minPurchase, double maxDiscount) {
        return new FixedAmountDiscountStrategy(amount, minPurchase, maxDiscount);
    }
    
    public static DiscountStrategy createBuyXGetYDiscount(
            int buy, int get, double discountPercent, Set<String> categories) {
        return new BuyXGetYDiscountStrategy(buy, get, discountPercent, categories);
    }
    
    public static DiscountStrategy createCategoryDiscount(
            Set<String> categories, double discountPercent) {
        return new CategoryDiscountStrategy(categories, discountPercent);
    }
}

// ============================================
// Tax Calculator
// ============================================

/**
 * Tax Calculator Interface
 * ISP: Segregated interface for tax calculation
 */
interface TaxCalculator {
    double calculateTax(ShoppingCart cart);
}

/**
 * Category-based Tax Calculator
 * SRP: Single responsibility - only calculates tax
 */
class CategoryTaxCalculator implements TaxCalculator {
    private final Map<String, Double> taxRates;
    
    public CategoryTaxCalculator(Map<String, Double> taxRates) {
        this.taxRates = taxRates;
    }
    
    @Override
    public double calculateTax(ShoppingCart cart) {
        double totalTax = 0;
        for (CartItem item : cart.getItems()) {
            double taxRate = taxRates.getOrDefault(item.getCategory(), 0.0);
            totalTax += item.getTotalPrice() * taxRate;
        }
        return totalTax;
    }
}

// ============================================
// Shipping Calculator
// ============================================

/**
 * Shipping Calculator Interface
 */
interface ShippingCalculator {
    double calculateShipping(ShoppingCart cart);
}

/**
 * Weight and Distance based Shipping Calculator
 */
class WeightBasedShippingCalculator implements ShippingCalculator {
    private final double baseRate;
    private final double ratePerKg;
    private final double freeShippingThreshold;
    
    public WeightBasedShippingCalculator(double baseRate, double ratePerKg, 
                                       double freeShippingThreshold) {
        this.baseRate = baseRate;
        this.ratePerKg = ratePerKg;
        this.freeShippingThreshold = freeShippingThreshold;
    }
    
    @Override
    public double calculateShipping(ShoppingCart cart) {
        double totalWeight = cart.getItems().stream()
            .mapToDouble(item -> item.getWeight() * item.getQuantity())
            .sum();
        
        double subtotal = cart.getSubtotal();
        if (subtotal >= freeShippingThreshold) {
            return 0; // Free shipping
        }
        
        return baseRate + (totalWeight * ratePerKg);
    }
}

// ============================================
// Shopping Cart
// ============================================

/**
 * Shopping Cart Class
 * SRP: Manages cart items and basic operations
 */
class ShoppingCart {
    private final Map<String, CartItem> items;
    private final String cartId;
    
    public ShoppingCart(String cartId) {
        this.cartId = cartId;
        this.items = new HashMap<>();
    }
    
    public void addItem(CartItem item) {
        items.merge(item.getProductId(), item, 
            (existing, newItem) -> {
                existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                return existing;
            });
    }
    
    public void removeItem(String productId) {
        items.remove(productId);
    }
    
    public void updateQuantity(String productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                items.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
        }
    }
    
    public double getSubtotal() {
        return items.values().stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }
    
    public double getTotalWeight() {
        return items.values().stream()
            .mapToDouble(item -> item.getWeight() * item.getQuantity())
            .sum();
    }
    
    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }
    
    public String getCartId() { return cartId; }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public void clear() {
        items.clear();
    }
}

// ============================================
// Cart Service (Main Business Logic)
// ============================================

/**
 * Cart Service - Orchestrates cart operations
 * SRP: Single responsibility - coordinates cart operations
 * DIP: Depends on abstractions (DiscountStrategy, TaxCalculator, ShippingCalculator)
 */
class CartService {
    private final List<DiscountStrategy> discountStrategies;
    private final TaxCalculator taxCalculator;
    private final ShippingCalculator shippingCalculator;
    
    public CartService(TaxCalculator taxCalculator, 
                      ShippingCalculator shippingCalculator) {
        this.taxCalculator = taxCalculator;
        this.shippingCalculator = shippingCalculator;
        this.discountStrategies = new ArrayList<>();
    }
    
    public void addDiscountStrategy(DiscountStrategy strategy) {
        discountStrategies.add(strategy);
    }
    
    /**
     * Calculate the best discount from all applicable strategies
     */
    public DiscountResult getBestDiscount(ShoppingCart cart) {
        return discountStrategies.stream()
            .filter(strategy -> strategy.isApplicable(cart))
            .map(strategy -> strategy.calculateDiscount(cart))
            .max(Comparator.comparingDouble(DiscountResult::getDiscountAmount))
            .orElse(new DiscountResult(0, "No discount applicable"));
    }
    
    /**
     * Calculate final total with all factors
     */
    public CartSummary calculateTotal(ShoppingCart cart) {
        double subtotal = cart.getSubtotal();
        DiscountResult discount = getBestDiscount(cart);
        double discountAmount = discount.getDiscountAmount();
        double afterDiscount = Math.max(0, subtotal - discountAmount);
        double tax = taxCalculator.calculateTax(cart);
        double shipping = shippingCalculator.calculateShipping(cart);
        double finalTotal = afterDiscount + tax + shipping;
        
        return new CartSummary(
            subtotal,
            discountAmount,
            discount.getDiscountDescription(),
            tax,
            shipping,
            finalTotal
        );
    }
}

/**
 * Cart Summary - Result of cart calculation
 */
class CartSummary {
    private final double subtotal;
    private final double discount;
    private final String discountDescription;
    private final double tax;
    private final double shipping;
    private final double finalTotal;
    
    public CartSummary(double subtotal, double discount, String discountDescription,
                     double tax, double shipping, double finalTotal) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.discountDescription = discountDescription;
        this.tax = tax;
        this.shipping = shipping;
        this.finalTotal = finalTotal;
    }
    
    public void printSummary() {
        System.out.println("=== Cart Summary ===");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Discount: $%.2f (%s)%n", discount, discountDescription);
        System.out.printf("Tax: $%.2f%n", tax);
        System.out.printf("Shipping: $%.2f%n", shipping);
        System.out.printf("Final Total: $%.2f%n", finalTotal);
        System.out.println("===================");
    }
    
    // Getters
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public String getDiscountDescription() { return discountDescription; }
    public double getTax() { return tax; }
    public double getShipping() { return shipping; }
    public double getFinalTotal() { return finalTotal; }
}

// ============================================
// Builder Pattern: Cart Builder
// ============================================

/**
 * Builder for constructing Shopping Cart with all dependencies
 * Makes it easy to create a fully configured cart service
 */
class CartServiceBuilder {
    private TaxCalculator taxCalculator;
    private ShippingCalculator shippingCalculator;
    private List<DiscountStrategy> discountStrategies = new ArrayList<>();
    
    public CartServiceBuilder withTaxCalculator(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
        return this;
    }
    
    public CartServiceBuilder withShippingCalculator(ShippingCalculator shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
        return this;
    }
    
    public CartServiceBuilder addDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategies.add(strategy);
        return this;
    }
    
    public CartService build() {
        if (taxCalculator == null || shippingCalculator == null) {
            throw new IllegalStateException("Tax and Shipping calculators are required");
        }
        
        CartService service = new CartService(taxCalculator, shippingCalculator);
        discountStrategies.forEach(service::addDiscountStrategy);
        return service;
    }
}

// ============================================
// Main Class - Demonstration
// ============================================

public class ShoppingCartLLD {
    
    public static void main(String[] args) {
        System.out.println("=== eBay-style Shopping Cart LLD ===");
        System.out.println("Demonstrating SOLID Principles\n");
        
        // Setup tax rates
        Map<String, Double> taxRates = new HashMap<>();
        taxRates.put("electronics", 0.10);
        taxRates.put("clothing", 0.08);
        taxRates.put("books", 0.05);
        
        // Build Cart Service using Builder Pattern
        CartService cartService = new CartServiceBuilder()
            .withTaxCalculator(new CategoryTaxCalculator(taxRates))
            .withShippingCalculator(new WeightBasedShippingCalculator(5.0, 2.0, 100.0))
            .addDiscountStrategy(DiscountStrategyFactory.createPercentageDiscount(10, 50))
            .addDiscountStrategy(DiscountStrategyFactory.createFixedDiscount(15, 75, 20))
            .addDiscountStrategy(DiscountStrategyFactory.createCategoryDiscount(
                Set.of("electronics"), 5))
            .build();
        
        // Create shopping cart
        ShoppingCart cart = new ShoppingCart("CART-001");
        
        // Add items
        cart.addItem(new CartItem("P1", "Laptop", 800.0, "electronics", 1, 2.5));
        cart.addItem(new CartItem("P2", "T-Shirt", 25.0, "clothing", 3, 0.2));
        cart.addItem(new CartItem("P3", "Book", 15.0, "books", 2, 0.5));
        
        // Calculate and display summary
        CartSummary summary = cartService.calculateTotal(cart);
        summary.printSummary();
        
        // Demonstrate different discount scenarios
        System.out.println("\n=== Testing Different Discount Scenarios ===\n");
        
        // Scenario 1: Low purchase amount (no discount)
        ShoppingCart smallCart = new ShoppingCart("CART-002");
        smallCart.addItem(new CartItem("P4", "Pen", 5.0, "books", 1, 0.1));
        CartSummary smallSummary = cartService.calculateTotal(smallCart);
        smallSummary.printSummary();
        
        // Scenario 2: High purchase (multiple discounts, best one selected)
        ShoppingCart largeCart = new ShoppingCart("CART-003");
        largeCart.addItem(new CartItem("P5", "TV", 1200.0, "electronics", 1, 15.0));
        CartSummary largeSummary = cartService.calculateTotal(largeCart);
        largeSummary.printSummary();
        
        System.out.println("\n=== SOLID Principles Demonstrated ===");
        System.out.println("✓ Single Responsibility: Each class has one reason to change");
        System.out.println("✓ Open/Closed: New discount strategies can be added without modifying existing code");
        System.out.println("✓ Liskov Substitution: All discount strategies are interchangeable");
        System.out.println("✓ Interface Segregation: Small, focused interfaces");
        System.out.println("✓ Dependency Inversion: High-level modules depend on abstractions");
    }
}
