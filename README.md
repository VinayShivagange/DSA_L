# Common Interview Questions

## Low-Level Design Questions

### Design Patterns
1. **Implement Singleton Pattern (Thread-safe)**
   - See: `LLD/Design-Patterns/01-Singleton-Pattern.java`

2. **Design a Factory for Payment Processors**
   - See: `LLD/Design-Patterns/02-Factory-Pattern.java`

3. **Implement Observer Pattern for Stock Market**
   - See: `LLD/Design-Patterns/03-Observer-Pattern.java`

4. **Design Strategy Pattern for Sorting Algorithms**
   - See: `LLD/Design-Patterns/04-Strategy-Pattern.java`

5. **Implement Builder Pattern for Complex Objects**
   - See: `LLD/Design-Patterns/05-Builder-Pattern.java`

### Class Design Problems
1. **Design a Parking Lot System**
   - Requirements: Multiple floors, different spot types, vehicle types
   - See: `LLD/Class-Design/01-Parking-Lot.java`

2. **Design an Elevator System**
   - Requirements: Multiple elevators, floor requests, direction management
   - See: `LLD/Class-Design/02-Elevator-System.java`

3. **Design an LRU Cache**
   - Requirements: O(1) get/put, fixed capacity, eviction
   - See: `LLD/Class-Design/03-LRU-Cache.java`

4. **Design a Vending Machine**
   - Requirements: Product selection, payment, change, inventory
   - See: `LLD/Class-Design/04-Vending-Machine.java`

5. **Design a Task Scheduler**
   - Requirements: Priority-based, scheduled tasks, thread-safe
   - See: `LLD/Class-Design/05-Task-Scheduler.java`

6. **Design a Rate Limiter**
   - Requirements: Token bucket, sliding window, thread-safe
   - See: `LLD/Class-Design/06-Rate-Limiter.java`

7. **Design a Logger System**
   - Requirements: Multiple log levels, appenders, async logging
   - See: `LLD/Class-Design/07-Logger-System.java`

### Additional Common Questions
- Design a HashMap
- Design a Trie
- Design a File System
- Design an ATM Machine
- Design a Chess Game
- Design a Library Management System
- Design a Restaurant Management System
- Design a Movie Ticket Booking System

## High-Level Design Questions

### System Design Problems
1. **Design a URL Shortener (TinyURL)**
   - See: `HLD/01-URL-Shortener.md`

2. **Design Twitter/X**
   - See: `HLD/02-Twitter-Clone.md`

3. **Design Uber/Lyft**
   - See: `HLD/03-Uber-Lyft-Clone.md`

4. **Design a Chat Application (WhatsApp)**
   - See: `HLD/04-Chat-Application.md`

5. **Design an E-commerce Platform (Amazon)**
   - See: `HLD/05-E-commerce-Platform.md`

6. **Design a Distributed Cache**
   - See: `HLD/06-Distributed-Cache.md`

7. **Design a Notification System**
   - See: `HLD/07-Notification-System.md`

8. **Design a Search Engine**
   - See: `HLD/08-Search-Engine.md`

9. **Design a Payment Gateway**
   - See: `HLD/09-Payment-Gateway.md`

### Additional Common Questions
- Design Netflix/YouTube
- Design Instagram
- Design Dropbox/Google Drive
- Design a Distributed Lock Service
- Design a Rate Limiting Service
- Design a News Feed System
- Design a Distributed Logging System
- Design a Real-time Analytics System

## Interview Tips

### For LLD Interviews
1. **Clarify Requirements**
   - Ask about functional and non-functional requirements
   - Understand constraints and assumptions

2. **Identify Core Classes**
   - Start with main entities
   - Define relationships
   - Consider design patterns

3. **Design APIs**
   - Define public interfaces
   - Consider method signatures
   - Handle edge cases

4. **Implement Key Methods**
   - Focus on core functionality
   - Write clean, readable code
   - Handle errors appropriately

5. **Discuss Trade-offs**
   - Time vs Space complexity
   - Scalability considerations
   - Design pattern choices

### For HLD Interviews
1. **Clarify Requirements**
   - Functional requirements
   - Non-functional requirements (scale, latency, availability)
   - Constraints and assumptions

2. **Estimate Capacity**
   - Traffic estimates
   - Storage estimates
   - Bandwidth estimates

3. **Design APIs**
   - RESTful API design
   - Request/response formats
   - Error handling

4. **Database Design**
   - Schema design
   - Indexing strategy
   - Sharding approach

5. **High-Level Architecture**
   - Component diagram
   - Data flow
   - Technology choices

6. **Scale the System**
   - Horizontal scaling
   - Caching strategy
   - Load balancing
   - Database replication

7. **Discuss Trade-offs**
   - Consistency vs Availability
   - Performance vs Cost
   - Complexity vs Scalability

## Practice Strategy

1. **Start with LLD**
   - Master design patterns
   - Practice class design problems
   - Code in your preferred language

2. **Move to HLD**
   - Study system design principles
   - Practice drawing diagrams
   - Explain your design clearly

3. **Mock Interviews**
   - Practice explaining your designs
   - Get feedback
   - Time yourself

4. **Review Real Systems**
   - Study how real systems are designed
   - Read engineering blogs
   - Understand trade-offs in production
