# Arrays: The Foundation of Data Structures

## Table of Contents
1. [Introduction](#introduction)
2. [What are Arrays?](#what-are-arrays)
3. [Array Characteristics](#array-characteristics)
4. [Types of Arrays](#types-of-arrays)
5. [Common Operations](#common-operations)
6. [Time and Space Complexity](#time-and-space-complexity)
7. [Implementation Examples](#implementation-examples)
8. [Common Interview Problems](#common-interview-problems)
9. [Best Practices](#best-practices)
10. [Related Topics](#related-topics)

## Introduction

Arrays are one of the most fundamental and important data structures in computer science. They serve as the building blocks for many other data structures and are essential for solving a wide variety of programming problems, especially in coding interviews.

> **💡 Key Insight**: Understanding arrays thoroughly is crucial for mastering algorithms and data structures, as they form the foundation for many advanced concepts.

## What are Arrays?

An **array** is a collection of elements stored in contiguous memory locations. Each element can be accessed directly using its index, making arrays one of the most efficient data structures for random access operations.

### Visual Representation

```
Array: [10, 20, 30, 40, 50]
Index:  0   1   2   3   4

Memory Layout:
┌────┬────┬────┬────┬────┐
│ 10 │ 20 │ 30 │ 40 │ 50 │
└────┴────┴────┴────┴────┘
```

## Array Characteristics

### ✅ Advantages
- **Random Access**: O(1) time complexity for accessing any element
- **Memory Efficiency**: Minimal memory overhead
- **Cache Locality**: Elements stored contiguously for better performance
- **Simple Implementation**: Easy to understand and implement

### ❌ Disadvantages
- **Fixed Size**: Most arrays have a predetermined size
- **Insertion/Deletion**: Expensive operations in the middle (O(n))
- **Memory Waste**: May allocate more memory than needed

## Types of Arrays

### 1. Static Arrays
Fixed size determined at compile time or initialization.

```java
// Java example
int[] staticArray = new int[5]; // Size fixed to 5
int[] initialized = {1, 2, 3, 4, 5};
```

### 2. Dynamic Arrays
Size can be changed during runtime (ArrayList, Vector, etc.).

```java
// Java example
ArrayList<Integer> dynamicArray = new ArrayList<>();
dynamicArray.add(1);
dynamicArray.add(2);
// Size grows automatically
```

### 3. Multi-dimensional Arrays
Arrays of arrays, commonly used for matrices and grids.

```java
// 2D Array example
int[][] matrix = new int[3][4]; // 3 rows, 4 columns
matrix[0][0] = 1;
```

## Common Operations

### 1. Access/Read
```java
public int getElement(int[] arr, int index) {
    if (index >= 0 && index < arr.length) {
        return arr[index]; // O(1)
    }
    throw new IndexOutOfBoundsException();
}
```

### 2. Update/Write
```java
public void setElement(int[] arr, int index, int value) {
    if (index >= 0 && index < arr.length) {
        arr[index] = value; // O(1)
    }
}
```

### 3. Search
```java
// Linear Search
public int linearSearch(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) {
            return i; // Found at index i
        }
    }
    return -1; // Not found
}
```

### 4. Insertion
```java
// Insert at specific position (requires shifting)
public int[] insertAt(int[] arr, int index, int value) {
    int[] newArr = new int[arr.length + 1];
    
    // Copy elements before insertion point
    for (int i = 0; i < index; i++) {
        newArr[i] = arr[i];
    }
    
    // Insert new value
    newArr[index] = value;
    
    // Copy remaining elements
    for (int i = index; i < arr.length; i++) {
        newArr[i + 1] = arr[i];
    }
    
    return newArr;
}
```

### 5. Deletion
```java
// Delete element at specific index
public int[] deleteAt(int[] arr, int index) {
    int[] newArr = new int[arr.length - 1];
    
    // Copy elements before deletion point
    for (int i = 0; i < index; i++) {
        newArr[i] = arr[i];
    }
    
    // Copy elements after deletion point
    for (int i = index + 1; i < arr.length; i++) {
        newArr[i - 1] = arr[i];
    }
    
    return newArr;
}
```

## Time and Space Complexity

| Operation | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Access | O(1) | O(1) |
| Search | O(n) | O(1) |
| Insertion | O(n) | O(1) |
| Deletion | O(n) | O(1) |
| Update | O(1) | O(1) |

## Implementation Examples

### Array Rotation
```java
// Rotate array to the right by k positions
public void rotate(int[] nums, int k) {
    int n = nums.length;
    k = k % n; // Handle k > n
    
    // Reverse entire array
    reverse(nums, 0, n - 1);
    // Reverse first k elements
    reverse(nums, 0, k - 1);
    // Reverse remaining elements
    reverse(nums, k, n - 1);
}

private void reverse(int[] nums, int start, int end) {
    while (start < end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
        start++;
        end--;
    }
}
```

### Two Sum Problem
```java
// Find two numbers that add up to target
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    
    return new int[]{}; // No solution found
}
```

## Common Interview Problems

### 🔥 Frequently Asked Problems

1. **Two Sum** - Find pair that sums to target
2. **Maximum Subarray** - Kadane's algorithm
3. **Rotate Array** - Rotate elements by k positions
4. **Remove Duplicates** - Remove duplicates from sorted array
5. **Merge Sorted Arrays** - Merge two sorted arrays
6. **Product of Array Except Self** - Calculate products efficiently
7. **Find Missing Number** - Find missing number in sequence
8. **Maximum Product Subarray** - Find subarray with maximum product

### Example: Maximum Subarray (Kadane's Algorithm)
```java
public int maxSubArray(int[] nums) {
    int maxSoFar = nums[0];
    int maxEndingHere = nums[0];
    
    for (int i = 1; i < nums.length; i++) {
        // Either extend existing subarray or start new one
        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
        // Update global maximum
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }
    
    return maxSoFar;
}
```

## Best Practices

### ✅ Do's
- **Check bounds** before accessing array elements
- **Use meaningful variable names** for indices
- **Consider using collections** (ArrayList) for dynamic sizing
- **Optimize for cache locality** when possible
- **Use binary search** for sorted arrays

### ❌ Don'ts
- **Don't ignore index bounds** - leads to runtime errors
- **Don't use arrays for frequent insertions/deletions** in middle
- **Don't forget to handle empty arrays** in algorithms
- **Don't use nested loops** unnecessarily - consider alternatives

### Memory Management Tips
```java
// Good: Clear references to help garbage collection
for (int i = 0; i < arr.length; i++) {
    arr[i] = null; // For object arrays
}

// Good: Use appropriate initial capacity
ArrayList<Integer> list = new ArrayList<>(expectedSize);
```

## Performance Optimization

### 1. Cache-Friendly Access Patterns
```java
// Better: Row-major order (cache-friendly)
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        matrix[i][j] = value;
    }
}

// Worse: Column-major order (cache-unfriendly)
for (int j = 0; j < cols; j++) {
    for (int i = 0; i < rows; i++) {
        matrix[i][j] = value;
    }
}
```

### 2. Minimize Array Copying
```java
// Use System.arraycopy for better performance
System.arraycopy(sourceArray, 0, destArray, 0, length);
```

## Related Topics

### 🔗 Next Steps
- **[Linked Lists](linked-lists)** - Dynamic alternative to arrays
- **[Strings](strings)** - Character arrays with special operations
- **[Stacks and Queues](stacks-queues)** - Built using arrays
- **[Hash Tables](hash-tables)** - Arrays with hash functions
- **[Dynamic Programming](dynamic-programming)** - Often uses arrays for memoization

### 🧩 Related Algorithms
- **Two Pointers Technique**
- **Sliding Window**
- **Binary Search**
- **Sorting Algorithms**

## Summary

Arrays are fundamental data structures that provide:
- **Efficient random access** in O(1) time
- **Simple and intuitive** interface
- **Foundation for other data structures**
- **Essential for coding interviews**

Master arrays first, and you'll have a solid foundation for tackling more complex data structures and algorithms!

---

## Practice Problems

### Beginner Level
1. Find the largest element in an array
2. Calculate sum of all elements
3. Reverse an array in-place
4. Check if array is sorted

### Intermediate Level
1. Two Sum problem
2. Rotate array by k positions
3. Remove duplicates from sorted array
4. Find intersection of two arrays

### Advanced Level
1. Maximum subarray sum (Kadane's)
2. Product of array except self
3. Trapping rain water
4. Sliding window maximum

### 💡 Pro Tip
Start with understanding the basic operations thoroughly before moving to complex problems. Arrays appear in almost every coding interview!

---

*Happy Coding! 🚀*