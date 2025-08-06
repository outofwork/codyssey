# Linked Lists: Dynamic Data Organization

## Introduction

Linked Lists are fundamental linear data structures that store elements in a sequence, but unlike arrays, elements are not stored in contiguous memory locations. Instead, each element (called a **node**) contains data and a reference (or link) to the next node in the sequence.

## What are Linked Lists?

A **linked list** is a linear data structure where elements are stored in nodes, and each node contains:
1. **Data**: The actual value stored in the node
2. **Next**: A reference/pointer to the next node in the sequence

### Visual Representation

```
Head -> [Data|Next] -> [Data|Next] -> [Data|Next] -> NULL
        Node 1        Node 2        Node 3
```

## Types of Linked Lists

### 1. Singly Linked List
Each node points to the next node, with the last node pointing to NULL.

```java
class ListNode {
    int val;
    ListNode next;
    
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```

### 2. Doubly Linked List
Each node has pointers to both next and previous nodes.

```java
class DoublyListNode {
    int val;
    DoublyListNode prev;
    DoublyListNode next;
    
    DoublyListNode(int val) { this.val = val; }
}
```

### 3. Circular Linked List
The last node points back to the first node, forming a circle.

## Basic Operations

### 1. Insertion
```java
// Insert at beginning
public ListNode insertAtBeginning(ListNode head, int val) {
    ListNode newNode = new ListNode(val);
    newNode.next = head;
    return newNode; // New head
}

// Insert at end
public ListNode insertAtEnd(ListNode head, int val) {
    ListNode newNode = new ListNode(val);
    if (head == null) return newNode;
    
    ListNode current = head;
    while (current.next != null) {
        current = current.next;
    }
    current.next = newNode;
    return head;
}
```

### 2. Deletion
```java
// Delete by value
public ListNode deleteNode(ListNode head, int val) {
    if (head == null) return null;
    
    // If head needs to be deleted
    if (head.val == val) {
        return head.next;
    }
    
    ListNode current = head;
    while (current.next != null && current.next.val != val) {
        current = current.next;
    }
    
    if (current.next != null) {
        current.next = current.next.next;
    }
    
    return head;
}
```

### 3. Search
```java
public boolean search(ListNode head, int val) {
    ListNode current = head;
    while (current != null) {
        if (current.val == val) {
            return true;
        }
        current = current.next;
    }
    return false;
}
```

## Time and Space Complexity

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Access | O(n) | O(1) |
| Search | O(n) | O(1) |
| Insertion | O(1) at head, O(n) at position | O(1) |
| Deletion | O(1) if node given, O(n) to find | O(1) |

## Common Interview Problems

### 1. Reverse Linked List
```java
public ListNode reverseList(ListNode head) {
    ListNode prev = null;
    ListNode current = head;
    
    while (current != null) {
        ListNode next = current.next;
        current.next = prev;
        prev = current;
        current = next;
    }
    
    return prev; // New head
}
```

### 2. Detect Cycle (Floyd's Algorithm)
```java
public boolean hasCycle(ListNode head) {
    if (head == null || head.next == null) return false;
    
    ListNode slow = head;
    ListNode fast = head.next;
    
    while (slow != fast) {
        if (fast == null || fast.next == null) {
            return false;
        }
        slow = slow.next;
        fast = fast.next.next;
    }
    
    return true;
}
```

### 3. Find Middle Element
```java
public ListNode findMiddle(ListNode head) {
    if (head == null) return null;
    
    ListNode slow = head;
    ListNode fast = head;
    
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    
    return slow;
}
```

## Advantages vs Disadvantages

### ✅ Advantages
- **Dynamic size**: Can grow/shrink during runtime
- **Efficient insertion/deletion**: O(1) at known positions
- **Memory efficient**: Only allocates memory as needed
- **No memory waste**: Unlike arrays with fixed size

### ❌ Disadvantages
- **No random access**: Must traverse from head to reach elements
- **Extra memory**: Each node requires additional pointer storage
- **Poor cache locality**: Nodes may be scattered in memory
- **Not cache-friendly**: Unlike arrays with contiguous memory

## When to Use Linked Lists

### ✅ Use When:
- Frequent insertions/deletions at the beginning
- Unknown or highly variable data size
- Implementing other data structures (stacks, queues)
- Memory is a concern and you want to avoid waste

### ❌ Avoid When:
- Need random access to elements
- Cache performance is critical
- Memory overhead is a concern
- Frequent access by index

## Best Practices

### Memory Management
```java
// Always check for null pointers
if (head == null) return null;

// Be careful with edge cases
if (head.next == null) {
    // Handle single node case
}
```

### Common Patterns
```java
// Two-pointer technique
ListNode slow = head, fast = head;

// Dummy node to simplify edge cases
ListNode dummy = new ListNode(0);
dummy.next = head;
```

## Related Topics
- **Arrays**: Alternative linear data structure
- **Stacks and Queues**: Often implemented using linked lists
- **Trees**: Extension of linked list concept
- **Hash Tables**: May use linked lists for collision resolution

## Summary

Linked Lists are essential data structures that offer dynamic memory allocation and efficient insertion/deletion operations. While they lack random access capabilities of arrays, they excel in scenarios requiring frequent modifications and unknown data sizes.

Master the basic operations and common patterns, as linked list problems are extremely common in coding interviews!