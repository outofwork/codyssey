# Heaps

A heap is a specialized tree-based data structure that satisfies the heap property. It is commonly used to implement priority queues and in heap sort algorithms.

## Basic Description

Heaps are complete binary trees where each node satisfies a specific ordering property relative to its children. This structure allows for efficient insertion, deletion, and access to the extreme (minimum or maximum) element.

## Heap Property

### Min Heap
- The value of each node is less than or equal to the values of its children
- The minimum element is always at the root

### Max Heap
- The value of each node is greater than or equal to the values of its children
- The maximum element is always at the root

## Characteristics

- **Complete binary tree**: All levels are filled except possibly the last level
- **Array representation**: Can be efficiently stored in arrays
- **Efficient operations**: O(log n) insertion and deletion
- **Constant access**: O(1) access to min/max element

## Applications

Heaps are used in:
- Priority queues implementation
- Heap sort algorithm
- Graph algorithms (Dijkstra's, Prim's)
- Event simulation systems
- Task scheduling in operating systems
- Finding kth largest/smallest elements

## Types of Heaps

### Binary Heap
Standard heap with at most two children per node.

### Binomial Heap
Collection of binomial trees with specific properties.

### Fibonacci Heap
Advanced heap with better amortized performance.

### Pairing Heap
Self-adjusting heap with good practical performance.