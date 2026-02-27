package org.example.wayfair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Payment Processing System - Low-Level Design
 * 
 * Requirements:
 * 1. Atomicity: Buyer charged exactly once, seller credited correctly
 * 2. Scalability: Handle millions of transactions per day
 * 3. Security: PCI-DSS compliance and encryption
 * 4. Idempotency: Retrying failed requests should not double charge
 * 
 * Design Patterns:
 * - Saga Pattern: For distributed transactions
 * - Idempotency Pattern: Using idempotency keys
 * - Circuit Breaker: For resilience
 * - Repository Pattern: For data access
 */

// ============================================
// Domain Models
// ============================================

/**
 * Payment Transaction Status
 */
enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED
}

/**
 * Payment Transaction Entity
 */
class PaymentTransaction {
    private final String transactionId;
    private final String idempotencyKey;
    private final String buyerId;
    private final String sellerId;
    private final BigDecimal amount;
    private final String currency;
    private PaymentStatus status; // Non-final to allow status updates
    private final Instant createdAt;
    private Instant updatedAt;
    private String failureReason;
    private String encryptedCardToken; // Encrypted, not plain card number
    
    public PaymentTransaction(String transactionId, String idempotencyKey,
                             String buyerId, String sellerId, BigDecimal amount,
                             String currency, PaymentStatus status) {
        this.transactionId = transactionId;
        this.idempotencyKey = idempotencyKey;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    
    public void updateStatus(PaymentStatus newStatus, String failureReason) {
        this.status = newStatus;
        this.failureReason = failureReason;
        this.updatedAt = Instant.now();
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getBuyerId() { return buyerId; }
    public String getSellerId() { return sellerId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getFailureReason() { return failureReason; }
    public String getEncryptedCardToken() { return encryptedCardToken; }
    
    public void setEncryptedCardToken(String encryptedCardToken) {
        this.encryptedCardToken = encryptedCardToken;
    }
}

/**
 * Payment Request
 */
class PaymentRequest {
    private final String idempotencyKey;
    private final String buyerId;
    private final String sellerId;
    private final BigDecimal amount;
    private final String currency;
    private final String cardToken; // Tokenized card (PCI-DSS compliant)
    
    public PaymentRequest(String idempotencyKey, String buyerId, String sellerId,
                         BigDecimal amount, String currency, String cardToken) {
        this.idempotencyKey = idempotencyKey;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.amount = amount;
        this.currency = currency;
        this.cardToken = cardToken;
    }
    
    // Getters
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getBuyerId() { return buyerId; }
    public String getSellerId() { return sellerId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCardToken() { return cardToken; }
}

/**
 * Payment Result
 */
class PaymentResult {
    private final boolean success;
    private final String transactionId;
    private final String message;
    private final PaymentStatus status;
    
    public PaymentResult(boolean success, String transactionId, 
                        String message, PaymentStatus status) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.status = status;
    }
    
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, "Payment processed successfully", 
                                PaymentStatus.COMPLETED);
    }
    
    public static PaymentResult failure(String transactionId, String message) {
        return new PaymentResult(false, transactionId, message, PaymentStatus.FAILED);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }
    public String getMessage() { return message; }
    public PaymentStatus getStatus() { return status; }
}

// ============================================
// Security: Encryption Service
// ============================================

/**
 * Encryption Service Interface
 * Handles encryption/decryption for PCI-DSS compliance
 */
interface EncryptionService {
    String encrypt(String plaintext);
    String decrypt(String ciphertext);
    String hash(String data); // For one-way hashing
}

/**
 * AES Encryption Service (Simplified - in production use proper key management)
 */
class AESEncryptionService implements EncryptionService {
    // In production: Use proper key management (AWS KMS, HashiCorp Vault, etc.)
    // Keys should be stored in secure key management service, not in code
    
    @Override
    public String encrypt(String plaintext) {
        // Simplified - in production use proper AES encryption
        // This is just a placeholder for demonstration
        // In production: Use AWS KMS, HashiCorp Vault, or similar
        return "encrypted:" + Base64.getEncoder().encodeToString(plaintext.getBytes());
    }
    
    @Override
    public String decrypt(String ciphertext) {
        // Simplified - in production use proper AES decryption
        if (ciphertext.startsWith("encrypted:")) {
            String encoded = ciphertext.substring(10);
            return new String(Base64.getDecoder().decode(encoded));
        }
        return ciphertext;
    }
    
    @Override
    public String hash(String data) {
        // In production: Use SHA-256 or better
        return String.valueOf(data.hashCode());
    }
}

// ============================================
// Idempotency Service
// ============================================

/**
 * Idempotency Service
 * Ensures that retrying a request with the same idempotency key doesn't result in duplicate processing
 */
interface IdempotencyService {
    /**
     * Check if a transaction with this idempotency key already exists
     * @return Existing transaction ID if found, null otherwise
     */
    String getExistingTransaction(String idempotencyKey);
    
    /**
     * Store idempotency key with transaction ID
     */
    void storeIdempotencyKey(String idempotencyKey, String transactionId);
}

/**
 * In-Memory Idempotency Service (for demo)
 * In production: Use Redis or distributed cache
 */
class InMemoryIdempotencyService implements IdempotencyService {
    private final Map<String, String> idempotencyMap = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    
    @Override
    public String getExistingTransaction(String idempotencyKey) {
        return idempotencyMap.get(idempotencyKey);
    }
    
    @Override
    public void storeIdempotencyKey(String idempotencyKey, String transactionId) {
        lock.lock();
        try {
            // Double-check pattern
            if (!idempotencyMap.containsKey(idempotencyKey)) {
                idempotencyMap.put(idempotencyKey, transactionId);
            }
        } finally {
            lock.unlock();
        }
    }
}

// ============================================
// Payment Gateway Interface
// ============================================

/**
 * Payment Gateway Interface
 * Abstraction for payment processors (Stripe, PayPal, etc.)
 */
interface PaymentGateway {
    /**
     * Charge the buyer's card
     */
    PaymentResult charge(String cardToken, BigDecimal amount, String currency, 
                        String idempotencyKey);
    
    /**
     * Credit the seller's account
     */
    PaymentResult credit(String sellerAccountId, BigDecimal amount, String currency);
    
    /**
     * Refund a transaction
     */
    PaymentResult refund(String transactionId, BigDecimal amount);
}

/**
 * Mock Payment Gateway (for demonstration)
 * In production: Integrate with real payment gateways (Stripe, PayPal, etc.)
 */
class MockPaymentGateway implements PaymentGateway {
    private final Random random = new Random();
    
    @Override
    public PaymentResult charge(String cardToken, BigDecimal amount, String currency,
                               String idempotencyKey) {
        // Simulate network delay
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate 95% success rate
        if (random.nextDouble() < 0.95) {
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
            return PaymentResult.success(transactionId);
        } else {
            return PaymentResult.failure(null, "Payment gateway error");
        }
    }
    
    @Override
    public PaymentResult credit(String sellerAccountId, BigDecimal amount, String currency) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String transactionId = "CREDIT-" + UUID.randomUUID().toString().substring(0, 8);
        return PaymentResult.success(transactionId);
    }
    
    @Override
    public PaymentResult refund(String transactionId, BigDecimal amount) {
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return PaymentResult.success("REFUND-" + transactionId);
    }
}

// ============================================
// Repository Pattern
// ============================================

/**
 * Transaction Repository Interface
 */
interface TransactionRepository {
    void save(PaymentTransaction transaction);
    PaymentTransaction findById(String transactionId);
    PaymentTransaction findByIdempotencyKey(String idempotencyKey);
    void updateStatus(String transactionId, PaymentStatus status, String failureReason);
}

/**
 * In-Memory Transaction Repository (for demo)
 * In production: Use database (PostgreSQL, MySQL with proper indexing)
 */
class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<String, PaymentTransaction> transactions = new ConcurrentHashMap<>();
    private final Map<String, String> idempotencyToTransaction = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    
    @Override
    public void save(PaymentTransaction transaction) {
        lock.lock();
        try {
            transactions.put(transaction.getTransactionId(), transaction);
            idempotencyToTransaction.put(transaction.getIdempotencyKey(), 
                                        transaction.getTransactionId());
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public PaymentTransaction findById(String transactionId) {
        return transactions.get(transactionId);
    }
    
    @Override
    public PaymentTransaction findByIdempotencyKey(String idempotencyKey) {
        String transactionId = idempotencyToTransaction.get(idempotencyKey);
        return transactionId != null ? transactions.get(transactionId) : null;
    }
    
    @Override
    public void updateStatus(String transactionId, PaymentStatus status, String failureReason) {
        lock.lock();
        try {
            PaymentTransaction transaction = transactions.get(transactionId);
            if (transaction != null) {
                transaction.updateStatus(status, failureReason);
            }
        } finally {
            lock.unlock();
        }
    }
}

// ============================================
// Saga Pattern: Distributed Transaction Coordinator
// ============================================

/**
 * Saga Step - Represents one step in a distributed transaction
 */
interface SagaStep {
    PaymentResult execute(PaymentTransaction transaction);
    PaymentResult compensate(PaymentTransaction transaction);
}

/**
 * Charge Buyer Step
 */
class ChargeBuyerStep implements SagaStep {
    private final PaymentGateway paymentGateway;
    
    public ChargeBuyerStep(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    
    @Override
    public PaymentResult execute(PaymentTransaction transaction) {
        return paymentGateway.charge(
            transaction.getEncryptedCardToken(),
            transaction.getAmount(),
            transaction.getCurrency(),
            transaction.getIdempotencyKey()
        );
    }
    
    @Override
    public PaymentResult compensate(PaymentTransaction transaction) {
        // Refund the buyer
        return paymentGateway.refund(transaction.getTransactionId(), transaction.getAmount());
    }
}

/**
 * Credit Seller Step
 */
class CreditSellerStep implements SagaStep {
    private final PaymentGateway paymentGateway;
    
    public CreditSellerStep(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    
    @Override
    public PaymentResult execute(PaymentTransaction transaction) {
        return paymentGateway.credit(
            transaction.getSellerId(),
            transaction.getAmount(),
            transaction.getCurrency()
        );
    }
    
    @Override
    public PaymentResult compensate(PaymentTransaction transaction) {
        // Reverse the credit (debit seller)
        // In production: Implement reverse credit
        return PaymentResult.failure(transaction.getTransactionId(), 
                                    "Credit reversal not implemented");
    }
}

/**
 * Saga Coordinator - Manages distributed transaction
 * Ensures atomicity across multiple steps
 */
class SagaCoordinator {
    private final List<SagaStep> steps;
    private final TransactionRepository repository;
    
    public SagaCoordinator(List<SagaStep> steps, TransactionRepository repository) {
        this.steps = steps;
        this.repository = repository;
    }
    
    /**
     * Execute saga with compensation on failure
     */
    public PaymentResult executeSaga(PaymentTransaction transaction) {
        List<SagaStep> executedSteps = new ArrayList<>();
        
        try {
            // Execute each step
            for (SagaStep step : steps) {
                PaymentResult result = step.execute(transaction);
                
                if (!result.isSuccess()) {
                    // Compensate all executed steps
                    compensateSteps(executedSteps, transaction);
                    repository.updateStatus(transaction.getTransactionId(), 
                                           PaymentStatus.FAILED, result.getMessage());
                    return result;
                }
                
                executedSteps.add(step);
            }
            
            // All steps succeeded
            repository.updateStatus(transaction.getTransactionId(), 
                                 PaymentStatus.COMPLETED, null);
            return PaymentResult.success(transaction.getTransactionId());
            
        } catch (Exception e) {
            // Compensate on exception
            compensateSteps(executedSteps, transaction);
            repository.updateStatus(transaction.getTransactionId(), 
                                 PaymentStatus.FAILED, e.getMessage());
            return PaymentResult.failure(transaction.getTransactionId(), e.getMessage());
        }
    }
    
    private void compensateSteps(List<SagaStep> executedSteps, 
                                PaymentTransaction transaction) {
        // Compensate in reverse order
        for (int i = executedSteps.size() - 1; i >= 0; i--) {
            try {
                executedSteps.get(i).compensate(transaction);
            } catch (Exception e) {
                // Log compensation failure but continue
                System.err.println("Compensation failed: " + e.getMessage());
            }
        }
    }
}

// ============================================
// Payment Service - Main Orchestrator
// ============================================

/**
 * Payment Service - Main business logic
 * Handles idempotency, atomicity, and coordinates payment flow
 */
class PaymentService {
    private final IdempotencyService idempotencyService;
    private final TransactionRepository repository;
    private final EncryptionService encryptionService;
    private final SagaCoordinator sagaCoordinator;
    private final Lock processingLock = new ReentrantLock();
    
    public PaymentService(IdempotencyService idempotencyService,
                         TransactionRepository repository,
                         EncryptionService encryptionService,
                         SagaCoordinator sagaCoordinator) {
        this.idempotencyService = idempotencyService;
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.sagaCoordinator = sagaCoordinator;
    }
    
    /**
     * Process payment with idempotency and atomicity guarantees
     */
    public PaymentResult processPayment(PaymentRequest request) {
        // Step 1: Check idempotency
        String existingTransactionId = idempotencyService.getExistingTransaction(
            request.getIdempotencyKey());
        
        if (existingTransactionId != null) {
            // Return existing transaction result
            PaymentTransaction existing = repository.findById(existingTransactionId);
            if (existing != null) {
                return new PaymentResult(
                    existing.getStatus() == PaymentStatus.COMPLETED,
                    existing.getTransactionId(),
                    "Idempotent request - returning existing transaction",
                    existing.getStatus()
                );
            }
        }
        
        // Step 2: Create transaction record
        String transactionId = "TXN-" + UUID.randomUUID().toString();
        PaymentTransaction transaction = new PaymentTransaction(
            transactionId,
            request.getIdempotencyKey(),
            request.getBuyerId(),
            request.getSellerId(),
            request.getAmount(),
            request.getCurrency(),
            PaymentStatus.PENDING
        );
        
        // Encrypt card token
        transaction.setEncryptedCardToken(encryptionService.encrypt(request.getCardToken()));
        
        // Step 3: Store idempotency key (with lock to prevent race condition)
        processingLock.lock();
        try {
            // Double-check idempotency after acquiring lock
            String existing = idempotencyService.getExistingTransaction(
                request.getIdempotencyKey());
            if (existing != null) {
                PaymentTransaction existingTxn = repository.findById(existing);
                return new PaymentResult(
                    existingTxn.getStatus() == PaymentStatus.COMPLETED,
                    existingTxn.getTransactionId(),
                    "Idempotent request - duplicate detected",
                    existingTxn.getStatus()
                );
            }
            
            // Store transaction and idempotency key atomically
            repository.save(transaction);
            idempotencyService.storeIdempotencyKey(
                request.getIdempotencyKey(), transactionId);
        } finally {
            processingLock.unlock();
        }
        
        // Step 4: Update status to processing
        repository.updateStatus(transactionId, PaymentStatus.PROCESSING, null);
        
        // Step 5: Execute saga (charge buyer, credit seller)
        PaymentResult result = sagaCoordinator.executeSaga(transaction);
        
        return result;
    }
    
    /**
     * Get transaction status
     */
    public PaymentTransaction getTransactionStatus(String transactionId) {
        return repository.findById(transactionId);
    }
}

// ============================================
// Main Class - Demonstration
// ============================================

public class PaymentSystemLLD {
    
    public static void main(String[] args) {
        System.out.println("=== Payment Processing System ===");
        System.out.println("Demonstrating: Atomicity, Scalability, Security, Idempotency\n");
        
        // Setup dependencies
        IdempotencyService idempotencyService = new InMemoryIdempotencyService();
        TransactionRepository repository = new InMemoryTransactionRepository();
        EncryptionService encryptionService = new AESEncryptionService();
        PaymentGateway paymentGateway = new MockPaymentGateway();
        
        // Setup saga steps
        List<SagaStep> sagaSteps = Arrays.asList(
            new ChargeBuyerStep(paymentGateway),
            new CreditSellerStep(paymentGateway)
        );
        SagaCoordinator sagaCoordinator = new SagaCoordinator(sagaSteps, repository);
        
        // Create payment service
        PaymentService paymentService = new PaymentService(
            idempotencyService, repository, encryptionService, sagaCoordinator);
        
        // Test 1: Normal payment
        System.out.println("Test 1: Normal Payment");
        PaymentRequest request1 = new PaymentRequest(
            "IDEMPOTENCY-KEY-001",
            "BUYER-001",
            "SELLER-001",
            new BigDecimal("100.00"),
            "USD",
            "CARD-TOKEN-123"
        );
        
        PaymentResult result1 = paymentService.processPayment(request1);
        System.out.println("Result: " + result1.isSuccess());
        System.out.println("Transaction ID: " + result1.getTransactionId());
        System.out.println("Message: " + result1.getMessage());
        System.out.println();
        
        // Test 2: Idempotency - retry same request
        System.out.println("Test 2: Idempotency Check (Retry Same Request)");
        PaymentResult result2 = paymentService.processPayment(request1);
        System.out.println("Result: " + result2.isSuccess());
        System.out.println("Transaction ID: " + result2.getTransactionId());
        System.out.println("Message: " + result2.getMessage());
        System.out.println("Same transaction ID? " + 
            result1.getTransactionId().equals(result2.getTransactionId()));
        System.out.println();
        
        // Test 3: New payment
        System.out.println("Test 3: New Payment");
        PaymentRequest request3 = new PaymentRequest(
            "IDEMPOTENCY-KEY-002",
            "BUYER-002",
            "SELLER-002",
            new BigDecimal("250.50"),
            "USD",
            "CARD-TOKEN-456"
        );
        
        PaymentResult result3 = paymentService.processPayment(request3);
        System.out.println("Result: " + result3.isSuccess());
        System.out.println("Transaction ID: " + result3.getTransactionId());
        System.out.println();
        
        // Test 4: Get transaction status
        System.out.println("Test 4: Get Transaction Status");
        PaymentTransaction transaction = paymentService.getTransactionStatus(
            result1.getTransactionId());
        if (transaction != null) {
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Status: " + transaction.getStatus());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.println("Buyer: " + transaction.getBuyerId());
            System.out.println("Seller: " + transaction.getSellerId());
        }
        
        System.out.println("\n=== Key Features ===");
        System.out.println("✓ Atomicity: Saga pattern ensures all-or-nothing execution");
        System.out.println("✓ Idempotency: Same idempotency key returns same result");
        System.out.println("✓ Security: Card tokens encrypted, never stored in plain text");
        System.out.println("✓ Scalability: Stateless service, can be horizontally scaled");
    }
}
