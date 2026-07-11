# Java RMI Hotel Reservation System

A distributed client-server application built using Java Remote Method Invocation (RMI). This system allows concurrent clients to view, book, and cancel hotel rooms while maintaining a thread-safe server state.

## Features
* **Remote Method Invocation:** Client-server communication is handled entirely via Java RMI.
* **Thread-Safe Concurrency:** The server utilizes `ConcurrentHashMap` and `CopyOnWriteArrayList` to safely manage bookings from multiple concurrent clients.
* **RMI Callbacks:** Clients attempting to book unavailable rooms can subscribe to a waitlist. The server uses remote callbacks to actively notify waiting clients the moment a room is canceled and becomes available.
* **Partial Bookings:** If a user requests more rooms than available, the system calculates the partial cost and prompts the user to accept the available remainder.

## Build and Run Instructions

Ensure you have a standard Java Development Kit (JDK) installed. 

### 1. Compilation
Navigate to the `src` directory inside your terminal and compile all Java files at once, preserving the package structure:
```bash
cd src
javac hotel/inteface/*.java hotel/model/*.java hotel/server/*.java hotel/client/*.java
