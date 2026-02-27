# Payment Processing System - High-Level Architecture

## System Requirements

1. **Atomicity**: Buyer charged exactly once, seller credited correctly
2. **Scalability**: Handle millions of transactions per day
3. **Security**: PCI-DSS compliance and encryption
4. **Idempotency**: Retrying failed requests should not double charge

## Architecture Overview

```
┌─────────────┐
│   Client    │
│  (Buyer)    │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────────────┐
│           API Gateway / Load Balancer            │
│         (Rate Limiting, SSL Termination)        │
└─────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────┐
│         Payment Service (Stateless)             │
│  - Idempotency Check                           │
│  - Request Validation                          │
│  - Transaction Creation                        │
└─────────────────────────────────────────────────┘
       │
       ├──────────────────┬──────────────────────┐
       ▼                  ▼                      ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────────┐
│  Idempotency│  │ Transaction  │  │  Encryption      │
│  Service    │  │  Repository │  │  Service          │
│  (Redis)    │  │  (PostgreSQL)│  │  (AWS KMS)        │
└─────────────┘  └──────────────┘  └──────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────┐
│         Saga Coordinator                        │
│  - Orchestrates distributed transaction        │
│  - Handles compensation on failure              │
└─────────────────────────────────────────────────┘
       │
       ├──────────────────┬──────────────────────┐
       ▼                  ▼                      ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────────┐
│ Charge Buyer│  │ Credit Seller│  │  Notification     │
│   Service   │  │   Service    │  │  Service         │
└──────┬──────┘  └──────┬───────┘  └──────────────────┘
       │                │
       ▼                ▼
┌─────────────────────────────────────────────────┐
│         Payment Gateway (Stripe/PayPal)         │
└─────────────────────────────────────────────────┘
```

## Component Details

### 1. API Gateway / Load Balancer
- **Purpose**: Entry point, handles SSL termination, rate limiting
- **Technology**: AWS API Gateway, NGINX, or similar
- **Features**:
  - Rate limiting per client
  - SSL/TLS termination
  - Request routing
  - DDoS protection

### 2. Payment Service (Stateless)
- **Purpose**: Main orchestration layer
- **Technology**: Microservice (Spring Boot, Node.js, etc.)
- **Features**:
  - Idempotency check
  - Request validation
  - Transaction creation
  - Saga coordination
- **Scaling**: Horizontal scaling (multiple instances)

### 3. Idempotency Service
- **Purpose**: Store idempotency keys to prevent duplicate processing
- **Technology**: Redis (distributed cache)
- **Key Design**:
  - Key: `idempotency_key`
  - Value: `transaction_id`
  - TTL: 24 hours (configurable)
- **Scaling**: Redis Cluster for high availability

### 4. Transaction Repository
- **Purpose**: Store transaction records
- **Technology**: PostgreSQL (or MySQL with proper indexing)
- **Schema**:
  ```sql
  CREATE TABLE transactions (
    transaction_id VARCHAR(255) PRIMARY KEY,
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    buyer_id VARCHAR(255) NOT NULL,
    seller_id VARCHAR(255) NOT NULL,
    amount DECIMAL(18, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(50) NOT NULL,
    encrypted_card_token TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    failure_reason TEXT,
    INDEX idx_idempotency_key (idempotency_key),
    INDEX idx_buyer_id (buyer_id),
    INDEX idx_seller_id (seller_id),
    INDEX idx_status (status)
  );
  ```

### 5. Encryption Service
- **Purpose**: Encrypt sensitive data (PCI-DSS compliance)
- **Technology**: AWS KMS, HashiCorp Vault, or similar
- **Features**:
  - Encrypt card tokens before storage
  - Key rotation
  - Audit logging

### 6. Saga Coordinator
- **Purpose**: Manage distributed transaction
- **Pattern**: Saga Pattern (Choreography or Orchestration)
- **Steps**:
  1. Charge Buyer
  2. Credit Seller
  3. Send Notifications
- **Compensation**: On failure, reverse all completed steps

### 7. Payment Gateway Integration
- **Purpose**: Interface with external payment processors
- **Technology**: Stripe, PayPal, Square, etc.
- **Features**:
  - Idempotent API calls
  - Retry logic with exponential backoff
  - Circuit breaker pattern

## Data Flow

### Successful Payment Flow

```
1. Client sends payment request with idempotency key
2. API Gateway validates and routes request
3. Payment Service checks idempotency (Redis)
   - If exists: Return existing transaction
   - If not: Continue
4. Payment Service creates transaction record (PENDING)
5. Payment Service stores idempotency key (Redis)
6. Saga Coordinator executes:
   a. Charge Buyer (Payment Gateway)
   b. Credit Seller (Payment Gateway)
7. Update transaction status (COMPLETED)
8. Send notifications
9. Return success response
```

### Failed Payment Flow

```
1-5. Same as successful flow
6. Saga Coordinator executes:
   a. Charge Buyer (SUCCESS)
   b. Credit Seller (FAILED)
7. Compensation:
   - Refund Buyer
8. Update transaction status (FAILED)
9. Return failure response
```

### Idempotent Retry Flow

```
1. Client retries with same idempotency key
2. Payment Service checks idempotency (Redis)
3. Found existing transaction ID
4. Return existing transaction result
   (No duplicate processing)
```

## Scalability Design

### Horizontal Scaling
- **Payment Service**: Stateless, can run multiple instances
- **Load Balancing**: Round-robin or least connections
- **Database**: Read replicas for read-heavy operations
- **Cache**: Redis Cluster for distributed caching

### Performance Optimizations
1. **Database Indexing**: 
   - Index on `idempotency_key` (unique lookup)
   - Index on `buyer_id`, `seller_id` (query optimization)
   - Index on `status` (filtering)

2. **Caching**:
   - Cache frequently accessed transactions
   - Cache idempotency keys (Redis)

3. **Async Processing**:
   - Non-critical operations (notifications) can be async
   - Use message queue (Kafka, RabbitMQ)

4. **Connection Pooling**:
   - Database connection pooling
   - HTTP client connection pooling

### Capacity Planning
- **Transactions per day**: 1,000,000
- **Peak TPS**: ~500 transactions/second
- **Average TPS**: ~12 transactions/second
- **Database**: PostgreSQL with read replicas
- **Cache**: Redis Cluster (3 nodes minimum)

## Security Measures

### PCI-DSS Compliance
1. **Never store full card numbers**
   - Use tokenization (payment gateway tokens)
   - Encrypt any card-related data

2. **Encryption**:
   - Encrypt data at rest (database encryption)
   - Encrypt data in transit (TLS 1.2+)
   - Use strong encryption algorithms (AES-256)

3. **Access Control**:
   - Role-based access control (RBAC)
   - Least privilege principle
   - Audit logging

4. **Network Security**:
   - VPC isolation
   - Security groups
   - WAF (Web Application Firewall)

### Data Protection
- **Encryption Service**: AWS KMS for key management
- **Tokenization**: Payment gateway handles card tokenization
- **Audit Logging**: All operations logged
- **Data Retention**: Follow compliance requirements

## Idempotency Implementation

### Idempotency Key Generation
- **Client-generated**: UUID v4 recommended
- **Format**: `{client_id}-{timestamp}-{random}`
- **Uniqueness**: Enforced at database level

### Idempotency Check Flow
```
1. Client includes idempotency key in request
2. Service checks Redis cache first (fast path)
3. If not found, check database (slow path)
4. If found: Return existing transaction
5. If not found: Process new transaction
```

### Race Condition Prevention
- **Distributed Lock**: Use Redis distributed lock
- **Database Constraint**: Unique constraint on `idempotency_key`
- **Double-check Pattern**: Check again after acquiring lock

## Atomicity Guarantees

### Saga Pattern
- **Choreography**: Each service knows what to do next
- **Orchestration**: Central coordinator manages flow
- **Compensation**: Reverse operations on failure

### Transaction States
- **PENDING**: Initial state
- **PROCESSING**: Saga execution in progress
- **COMPLETED**: All steps succeeded
- **FAILED**: Step failed, compensation executed
- **REFUNDED**: Transaction refunded

### Failure Handling
1. **Charge succeeds, credit fails**:
   - Compensate: Refund buyer
   - Update status: FAILED

2. **Network timeout**:
   - Retry with exponential backoff
   - Check idempotency before retry

3. **Partial failure**:
   - Execute compensation for completed steps
   - Log failure for manual review

## Monitoring & Observability

### Metrics
- Transaction success rate
- Average processing time
- Idempotency hit rate
- Payment gateway latency
- Error rates by type

### Logging
- All transactions logged
- Idempotency key usage logged
- Payment gateway responses logged
- Compensation actions logged

### Alerts
- High failure rate
- Payment gateway downtime
- Database connection issues
- Cache unavailability

## Deployment Strategy

### High Availability
- **Multi-AZ Deployment**: Deploy across availability zones
- **Database Replication**: Master-slave or multi-master
- **Cache Replication**: Redis Cluster with replication
- **Load Balancing**: Multiple load balancers

### Disaster Recovery
- **Backup Strategy**: Daily database backups
- **Replication**: Cross-region replication
- **Failover**: Automated failover mechanisms
- **RTO/RPO**: Define recovery objectives

## Testing Strategy

### Unit Tests
- Payment service logic
- Idempotency checks
- Encryption/decryption
- Saga compensation

### Integration Tests
- Payment gateway integration
- Database operations
- Cache operations
- End-to-end flows

### Load Tests
- Simulate high traffic
- Test idempotency under load
- Test database performance
- Test cache performance

## Future Enhancements

1. **Event Sourcing**: Store all events for audit trail
2. **CQRS**: Separate read/write models
3. **Multi-currency Support**: Currency conversion
4. **Fraud Detection**: ML-based fraud detection
5. **Real-time Analytics**: Stream processing for analytics
