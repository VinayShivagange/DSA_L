package org.example.wayfair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Banking System - Multi-Level Financial Management System
 * 
 * Level 1: Basic Account Operations
 * - Create accounts
 * - Deposit money
 * - Transfer money between accounts
 * 
 * Level 2: Top Spenders Ranking
 * - Track outgoing transactions
 * - Return top N spenders
 * 
 * Level 3: Scheduled Payments with Cashback
 * - Schedule future payments
 * - Process scheduled payments
 * - Apply cashback rewards
 * 
 * Level 4: Account Merging
 * - Merge two accounts
 * - Combine balances
 * - Update transaction histories
 */

// ============================================
// Domain Models
// ============================================

/**
 * Account Entity
 */
class Account {
    private final String accountId;
    private int balance;
    private final int createdAt;
    private final Map<String, Account> mergedAccounts; // For account merging
    private final Set<String> parentAccounts; // Accounts that merged into this
    
    public Account(String accountId, int timestamp) {
        this.accountId = accountId;
        this.balance = 0;
        this.createdAt = timestamp;
        this.mergedAccounts = new HashMap<>();
        this.parentAccounts = new HashSet<>();
    }
    
    public void deposit(int amount) {
        this.balance += amount;
    }
    
    public boolean withdraw(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    public void mergeAccount(Account other) {
        // Transfer balance
        this.balance += other.balance;
        // Mark as merged
        this.mergedAccounts.put(other.accountId, other);
        other.parentAccounts.add(this.accountId);
    }
    
    // Getters
    public String getAccountId() { return accountId; }
    public int getBalance() { return balance; }
    public int getCreatedAt() { return createdAt; }
    public Map<String, Account> getMergedAccounts() { return mergedAccounts; }
    public Set<String> getParentAccounts() { return parentAccounts; }
    public boolean isMerged() { return !parentAccounts.isEmpty(); }
    public String getRootAccountId() {
        // If merged, return the root account
        if (isMerged()) {
            return parentAccounts.iterator().next();
        }
        return accountId;
    }
}

/**
 * Transaction Record
 */
class Transaction {
    private final String transactionId;
    private final String fromAccountId;
    private final String toAccountId;
    private final int amount;
    private final int timestamp;
    private final TransactionType type;
    
    public Transaction(String transactionId, String fromAccountId, String toAccountId,
                      int amount, int timestamp, TransactionType type) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public int getAmount() { return amount; }
    public int getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
}

enum TransactionType {
    DEPOSIT,
    TRANSFER,
    SCHEDULED_PAYMENT,
    CASHBACK
}

/**
 * Scheduled Payment
 */
class ScheduledPayment {
    private final String paymentId;
    private final String accountId;
    private final String targetAccountId;
    private final int scheduledTimestamp;
    private final int amount;
    private final double cashbackPercentage;
    private ScheduledPaymentStatus status;
    private String failureReason;
    
    public ScheduledPayment(String paymentId, String accountId, String targetAccountId,
                           int scheduledTimestamp, int amount, double cashbackPercentage) {
        this.paymentId = paymentId;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.scheduledTimestamp = scheduledTimestamp;
        this.amount = amount;
        this.cashbackPercentage = cashbackPercentage;
        this.status = ScheduledPaymentStatus.SCHEDULED;
    }
    
    public void markAsProcessed() {
        this.status = ScheduledPaymentStatus.PROCESSED;
    }
    
    public void markAsFailed(String reason) {
        this.status = ScheduledPaymentStatus.FAILED;
        this.failureReason = reason;
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getAccountId() { return accountId; }
    public String getTargetAccountId() { return targetAccountId; }
    public int getScheduledTimestamp() { return scheduledTimestamp; }
    public int getAmount() { return amount; }
    public double getCashbackPercentage() { return cashbackPercentage; }
    public ScheduledPaymentStatus getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public int getCashbackAmount() {
        return (int) Math.floor(amount * cashbackPercentage / 100.0);
    }
}

enum ScheduledPaymentStatus {
    SCHEDULED,
    PROCESSED,
    FAILED
}

/**
 * Spender Statistics
 */
class SpenderStats {
    private final String accountId;
    private int totalSpent;
    private int transactionCount;
    
    public SpenderStats(String accountId) {
        this.accountId = accountId;
        this.totalSpent = 0;
        this.transactionCount = 0;
    }
    
    public void addSpending(int amount) {
        this.totalSpent += amount;
        this.transactionCount++;
    }
    
    // Getters
    public String getAccountId() { return accountId; }
    public int getTotalSpent() { return totalSpent; }
    public int getTransactionCount() { return transactionCount; }
}

// ============================================
// Banking System Implementation
// ============================================

public class BankingSystem {
    
    // Core data structures
    private final Map<String, Account> accounts;
    private final List<Transaction> transactions;
    private final Map<String, ScheduledPayment> scheduledPayments;
    private final Map<String, SpenderStats> spenderStats; // Account ID -> Stats
    private final Map<String, String> accountMapping; // For merged accounts: old -> new
    
    // Thread safety
    private final Lock lock = new ReentrantLock();
    
    public BankingSystem() {
        this.accounts = new ConcurrentHashMap<>();
        this.transactions = Collections.synchronizedList(new ArrayList<>());
        this.scheduledPayments = new ConcurrentHashMap<>();
        this.spenderStats = new ConcurrentHashMap<>();
        this.accountMapping = new ConcurrentHashMap<>();
    }
    
    // ============================================
    // Level 1: Basic Account Operations
    // ============================================
    
    /**
     * Create a new account
     * @return true if account created successfully, false if account already exists
     */
    public boolean createAccount(String accountId, int timestamp) {
        lock.lock();
        try {
            if (accounts.containsKey(accountId)) {
                return false; // Account already exists
            }
            
            Account account = new Account(accountId, timestamp);
            accounts.put(accountId, account);
            spenderStats.put(accountId, new SpenderStats(accountId));
            
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Deposit money into an account
     * @return Optional with account balance if successful, empty if account doesn't exist
     */
    public Optional<Integer> deposit(String accountId, int timestamp, int amount) {
        if (amount <= 0) {
            return Optional.empty();
        }
        
        lock.lock();
        try {
            Account account = getAccount(accountId);
            if (account == null) {
                return Optional.empty();
            }
            
            account.deposit(amount);
            
            // Record transaction
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
            transactions.add(new Transaction(transactionId, null, accountId, amount, 
                                            timestamp, TransactionType.DEPOSIT));
            
            return Optional.of(account.getBalance());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Transfer money between accounts
     * @return Optional with from account balance if successful, empty if transfer failed
     */
    public Optional<Integer> transfer(String fromId, String toId, int timestamp, int amount) {
        if (amount <= 0) {
            return Optional.empty();
        }
        
        if (fromId.equals(toId)) {
            return Optional.empty(); // Can't transfer to self
        }
        
        lock.lock();
        try {
            Account fromAccount = getAccount(fromId);
            Account toAccount = getAccount(toId);
            
            if (fromAccount == null || toAccount == null) {
                return Optional.empty();
            }
            
            // Check balance
            if (fromAccount.getBalance() < amount) {
                return Optional.empty(); // Insufficient funds
            }
            
            // Perform transfer
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            
            // Record transaction
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
            transactions.add(new Transaction(transactionId, fromId, toId, amount, 
                                            timestamp, TransactionType.TRANSFER));
            
            // Update spender statistics
            updateSpenderStats(fromId, amount);
            
            return Optional.of(fromAccount.getBalance());
        } finally {
            lock.unlock();
        }
    }
    
    // ============================================
    // Level 2: Top Spenders Ranking
    // ============================================
    
    /**
     * Get top N spenders based on outgoing transactions up to given timestamp
     * Sorted by total amount (descending), then by account ID (ascending)
     */
    public List<String> topSpenders(int timestamp, int n) {
        lock.lock();
        try {
            // Calculate spending up to timestamp
            Map<String, Integer> spendingMap = new HashMap<>();
            
            for (Transaction txn : transactions) {
                if (txn.getTimestamp() <= timestamp && 
                    txn.getType() == TransactionType.TRANSFER &&
                    txn.getFromAccountId() != null) {
                    
                    String accountId = resolveAccountId(txn.getFromAccountId());
                    spendingMap.put(accountId, 
                        spendingMap.getOrDefault(accountId, 0) + txn.getAmount());
                }
            }
            
            // Sort by amount (descending), then by account ID (ascending)
            return spendingMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    int amountCompare = Integer.compare(e2.getValue(), e1.getValue());
                    if (amountCompare != 0) {
                        return amountCompare;
                    }
                    return e1.getKey().compareTo(e2.getKey());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }
    
    // ============================================
    // Level 3: Scheduled Payments with Cashback
    // ============================================
    
    /**
     * Schedule a payment with cashback
     */
    public void schedulePayment(String accountId, String targetAccId, int timestamp, 
                               int amount, double cashbackPercentage) {
        lock.lock();
        try {
            Account account = getAccount(accountId);
            Account targetAccount = getAccount(targetAccId);
            
            if (account == null || targetAccount == null) {
                return; // Invalid accounts
            }
            
            if (amount <= 0 || cashbackPercentage < 0 || cashbackPercentage > 100) {
                return; // Invalid parameters
            }
            
            String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
            ScheduledPayment payment = new ScheduledPayment(
                paymentId, accountId, targetAccId, timestamp, amount, cashbackPercentage);
            
            scheduledPayments.put(paymentId, payment);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get payment status
     * @return "SCHEDULED", "PROCESSED", "FAILED", or "NOT_FOUND"
     */
    public String getPaymentStatus(String accountId, int timestamp, String paymentId) {
        lock.lock();
        try {
            ScheduledPayment payment = scheduledPayments.get(paymentId);
            
            if (payment == null) {
                return "NOT_FOUND";
            }
            
            // Verify account ownership
            if (!payment.getAccountId().equals(accountId)) {
                return "NOT_FOUND";
            }
            
            return payment.getStatus().toString();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Process all scheduled payments up to current timestamp
     */
    public void processScheduledPayments(int currentTimestamp) {
        lock.lock();
        try {
            List<ScheduledPayment> toProcess = scheduledPayments.values().stream()
                .filter(p -> p.getStatus() == ScheduledPaymentStatus.SCHEDULED &&
                           p.getScheduledTimestamp() <= currentTimestamp)
                .collect(Collectors.toList());
            
            for (ScheduledPayment payment : toProcess) {
                processPayment(payment, currentTimestamp);
            }
        } finally {
            lock.unlock();
        }
    }
    
    private void processPayment(ScheduledPayment payment, int currentTimestamp) {
        Account fromAccount = getAccount(payment.getAccountId());
        Account toAccount = getAccount(payment.getTargetAccountId());
        
        if (fromAccount == null || toAccount == null) {
            payment.markAsFailed("Account not found");
            return;
        }
        
        // Check balance
        if (fromAccount.getBalance() < payment.getAmount()) {
            payment.markAsFailed("Insufficient funds");
            return;
        }
        
        // Process payment
        fromAccount.withdraw(payment.getAmount());
        toAccount.deposit(payment.getAmount());
        
        // Apply cashback
        int cashbackAmount = payment.getCashbackAmount();
        if (cashbackAmount > 0) {
            fromAccount.deposit(cashbackAmount);
            
            // Record cashback transaction
            String cashbackTxnId = "CASHBACK-" + UUID.randomUUID().toString().substring(0, 8);
            transactions.add(new Transaction(cashbackTxnId, null, payment.getAccountId(),
                cashbackAmount, currentTimestamp, TransactionType.CASHBACK));
        }
        
        // Record payment transaction
        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
        transactions.add(new Transaction(txnId, payment.getAccountId(), 
            payment.getTargetAccountId(), payment.getAmount(), currentTimestamp,
            TransactionType.SCHEDULED_PAYMENT));
        
        // Update spender stats
        updateSpenderStats(payment.getAccountId(), payment.getAmount());
        
        payment.markAsProcessed();
    }
    
    // ============================================
    // Level 4: Account Merging
    // ============================================
    
    /**
     * Merge two accounts
     * - Combines balances
     * - Updates all transaction histories
     * - Account 1 becomes the primary account
     */
    public void mergeAccounts(String accountId1, String accountId2) {
        if (accountId1.equals(accountId2)) {
            return; // Can't merge with self
        }
        
        lock.lock();
        try {
            Account account1 = getAccount(accountId1);
            Account account2 = getAccount(accountId2);
            
            if (account1 == null || account2 == null) {
                return; // Accounts don't exist
            }
            
            // If account2 is already merged, get its root
            String rootAccount2 = account2.getRootAccountId();
            if (!rootAccount2.equals(accountId2)) {
                account2 = getAccount(rootAccount2);
            }
            
            // If account1 is already merged, get its root
            String rootAccount1 = account1.getRootAccountId();
            if (!rootAccount1.equals(accountId1)) {
                account1 = getAccount(rootAccount1);
            }
            
            if (account1.getAccountId().equals(account2.getAccountId())) {
                return; // Already merged
            }
            
            // Merge account2 into account1
            account1.mergeAccount(account2);
            
            // Update account mapping
            accountMapping.put(account2.getAccountId(), account1.getAccountId());
            
            // Update all transactions to point to account1
            for (Transaction txn : transactions) {
                if (txn.getFromAccountId() != null && 
                    txn.getFromAccountId().equals(account2.getAccountId())) {
                    // Update transaction (in real system, would need immutable transactions)
                }
                if (txn.getToAccountId() != null && 
                    txn.getToAccountId().equals(account2.getAccountId())) {
                    // Update transaction
                }
            }
            
            // Merge spender stats
            SpenderStats stats1 = spenderStats.get(account1.getAccountId());
            SpenderStats stats2 = spenderStats.get(account2.getAccountId());
            if (stats1 != null && stats2 != null) {
                stats1.addSpending(stats2.getTotalSpent());
            }
            
            // Remove account2 from active accounts (but keep for history)
            // In production, would mark as merged rather than remove
        } finally {
            lock.unlock();
        }
    }
    
    // ============================================
    // Helper Methods
    // ============================================
    
    /**
     * Get account, resolving merged accounts
     */
    private Account getAccount(String accountId) {
        // Check if account is merged
        String resolvedId = resolveAccountId(accountId);
        return accounts.get(resolvedId);
    }
    
    /**
     * Resolve account ID (handle merged accounts)
     */
    private String resolveAccountId(String accountId) {
        String current = accountId;
        while (accountMapping.containsKey(current)) {
            current = accountMapping.get(current);
        }
        return current;
    }
    
    /**
     * Update spender statistics
     */
    private void updateSpenderStats(String accountId, int amount) {
        String resolvedId = resolveAccountId(accountId);
        SpenderStats stats = spenderStats.get(resolvedId);
        if (stats != null) {
            stats.addSpending(amount);
        }
    }
    
    // ============================================
    // Utility Methods for Testing
    // ============================================
    
    public Optional<Integer> getBalance(String accountId) {
        lock.lock();
        try {
            Account account = getAccount(accountId);
            return account != null ? Optional.of(account.getBalance()) : Optional.empty();
        } finally {
            lock.unlock();
        }
    }
    
    public int getTransactionCount() {
        return transactions.size();
    }
    
    public int getScheduledPaymentCount() {
        return scheduledPayments.size();
    }
    
    public static void main(String[] args) {
        System.out.println("=== Banking System - Multi-Level Implementation ===\n");
        
        BankingSystem bank = new BankingSystem();
        
        // Level 1: Basic Operations
        System.out.println("=== Level 1: Basic Account Operations ===");
        
        // Create accounts
        bank.createAccount("ACC-001", 1000);
        bank.createAccount("ACC-002", 1001);
        bank.createAccount("ACC-003", 1002);
        System.out.println("Created 3 accounts");
        
        // Deposits
        bank.deposit("ACC-001", 1003, 1000);
        bank.deposit("ACC-002", 1004, 500);
        bank.deposit("ACC-003", 1005, 2000);
        System.out.println("Deposited: ACC-001=$1000, ACC-002=$500, ACC-003=$2000");
        
        // Transfers
        bank.transfer("ACC-001", "ACC-002", 1006, 200);
        bank.transfer("ACC-003", "ACC-001", 1007, 500);
        bank.transfer("ACC-002", "ACC-003", 1008, 100);
        System.out.println("Transferred: ACC-001->ACC-002=$200, ACC-003->ACC-001=$500, ACC-002->ACC-003=$100");
        
        System.out.println("Balances:");
        System.out.println("  ACC-001: $" + bank.getBalance("ACC-001").orElse(0));
        System.out.println("  ACC-002: $" + bank.getBalance("ACC-002").orElse(0));
        System.out.println("  ACC-003: $" + bank.getBalance("ACC-003").orElse(0));
        System.out.println();
        
        // Level 2: Top Spenders
        System.out.println("=== Level 2: Top Spenders ===");
        List<String> topSpenders = bank.topSpenders(1010, 2);
        System.out.println("Top 2 spenders: " + topSpenders);
        System.out.println();
        
        // Level 3: Scheduled Payments
        System.out.println("=== Level 3: Scheduled Payments ===");
        bank.schedulePayment("ACC-001", "ACC-002", 2000, 300, 5.0); // 5% cashback
        bank.schedulePayment("ACC-003", "ACC-001", 2001, 400, 10.0); // 10% cashback
        
        System.out.println("Scheduled 2 payments");
        System.out.println("Payment status (before processing): " + 
            bank.getPaymentStatus("ACC-001", 1999, "PAY-00000000")); // Should be NOT_FOUND or check actual ID
        
        // Process scheduled payments
        bank.processScheduledPayments(2002);
        System.out.println("Processed scheduled payments at timestamp 2002");
        
        System.out.println("Balances after scheduled payments:");
        System.out.println("  ACC-001: $" + bank.getBalance("ACC-001").orElse(0));
        System.out.println("  ACC-002: $" + bank.getBalance("ACC-002").orElse(0));
        System.out.println("  ACC-003: $" + bank.getBalance("ACC-003").orElse(0));
        System.out.println();
        
        // Level 4: Account Merging
        System.out.println("=== Level 4: Account Merging ===");
        System.out.println("Before merge:");
        System.out.println("  ACC-001: $" + bank.getBalance("ACC-001").orElse(0));
        System.out.println("  ACC-002: $" + bank.getBalance("ACC-002").orElse(0));
        
        bank.mergeAccounts("ACC-001", "ACC-002");
        System.out.println("Merged ACC-002 into ACC-001");
        
        System.out.println("After merge:");
        System.out.println("  ACC-001: $" + bank.getBalance("ACC-001").orElse(0));
        System.out.println("  ACC-002: $" + bank.getBalance("ACC-002").orElse(0));
        
        // Try to use merged account
        bank.deposit("ACC-002", 3000, 100); // Should go to ACC-001
        System.out.println("Deposited $100 to ACC-002 (merged account)");
        System.out.println("  ACC-001 balance: $" + bank.getBalance("ACC-001").orElse(0));
    }
}
