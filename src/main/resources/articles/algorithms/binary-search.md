# Binary Search: Efficient Searching Algorithm

## Introduction

Binary Search is one of the most fundamental and efficient searching algorithms in computer science. It's a **divide-and-conquer** algorithm that can find a target value in a sorted array in O(log n) time complexity.

> **Key Insight**: Binary search only works on **sorted** data!

## How Binary Search Works

The algorithm repeatedly divides the search space in half by comparing the target value with the middle element:

1. **Compare** target with middle element
2. **Eliminate** half of the remaining elements
3. **Repeat** until target is found or search space is empty

### Visual Example
Searching for `7` in `[1, 3, 5, 7, 9, 11, 13]`:

```
Step 1: [1, 3, 5, 7, 9, 11, 13]
                  ↑
                mid=7 → Found!
```

## Basic Implementation

### Iterative Approach
```java
public int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2; // Avoid overflow
        
        if (arr[mid] == target) {
            return mid; // Found target
        } else if (arr[mid] < target) {
            left = mid + 1; // Search right half
        } else {
            right = mid - 1; // Search left half
        }
    }
    
    return -1; // Target not found
}
```

### Recursive Approach
```java
public int binarySearchRecursive(int[] arr, int target, int left, int right) {
    if (left > right) {
        return -1; // Base case: not found
    }
    
    int mid = left + (right - left) / 2;
    
    if (arr[mid] == target) {
        return mid;
    } else if (arr[mid] < target) {
        return binarySearchRecursive(arr, target, mid + 1, right);
    } else {
        return binarySearchRecursive(arr, target, left, mid - 1);
    }
}
```

## Time and Space Complexity

| Approach | Time Complexity | Space Complexity |
|----------|----------------|------------------|
| Iterative | O(log n) | O(1) |
| Recursive | O(log n) | O(log n) |

## Common Variations

### 1. Find First Occurrence
```java
public int findFirst(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            result = mid;
            right = mid - 1; // Continue searching left
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

### 2. Find Last Occurrence
```java
public int findLast(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] == target) {
            result = mid;
            left = mid + 1; // Continue searching right
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

### 3. Find Insertion Point
```java
public int findInsertionPoint(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return left; // Insertion point
}
```

## Binary Search Template

### General Template for "Find Condition"
```java
public int binarySearchTemplate(int[] arr) {
    int left = 0, right = arr.length - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        
        if (condition(mid)) {
            right = mid; // Condition satisfied, search left
        } else {
            left = mid + 1; // Condition not satisfied, search right
        }
    }
    
    return left; // or right, they're equal
}
```

## Common Interview Problems

### 1. Search in Rotated Sorted Array
```java
public int searchRotated(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) return mid;
        
        // Left half is sorted
        if (nums[left] <= nums[mid]) {
            if (target >= nums[left] && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        // Right half is sorted
        else {
            if (target > nums[mid] && target <= nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return -1;
}
```

### 2. Find Peak Element
```java
public int findPeakElement(int[] nums) {
    int left = 0, right = nums.length - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] > nums[mid + 1]) {
            right = mid; // Peak is on left side or mid itself
        } else {
            left = mid + 1; // Peak is on right side
        }
    }
    
    return left;
}
```

### 3. Search 2D Matrix
```java
public boolean searchMatrix(int[][] matrix, int target) {
    if (matrix.length == 0 || matrix[0].length == 0) return false;
    
    int rows = matrix.length, cols = matrix[0].length;
    int left = 0, right = rows * cols - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int midValue = matrix[mid / cols][mid % cols];
        
        if (midValue == target) {
            return true;
        } else if (midValue < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}
```

## Advanced Applications

### Binary Search on Answer
When the answer space is monotonic, we can binary search on the answer:

```java
// Example: Find minimum days to make m bouquets
public int minDays(int[] bloomDay, int m, int k) {
    if (m * k > bloomDay.length) return -1;
    
    int left = 1, right = Arrays.stream(bloomDay).max().getAsInt();
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        
        if (canMakeBouquets(bloomDay, m, k, mid)) {
            right = mid; // Can make bouquets, try fewer days
        } else {
            left = mid + 1; // Cannot make bouquets, need more days
        }
    }
    
    return left;
}

private boolean canMakeBouquets(int[] bloomDay, int m, int k, int days) {
    int bouquets = 0, flowers = 0;
    
    for (int bloom : bloomDay) {
        if (bloom <= days) {
            flowers++;
            if (flowers == k) {
                bouquets++;
                flowers = 0;
            }
        } else {
            flowers = 0;
        }
    }
    
    return bouquets >= m;
}
```

## Common Pitfalls

### 1. Integer Overflow
```java
// ❌ Wrong: Can cause overflow
int mid = (left + right) / 2;

// ✅ Correct: Prevents overflow
int mid = left + (right - left) / 2;
```

### 2. Infinite Loops
```java
// ❌ Wrong: Can cause infinite loop
while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition) {
        left = mid; // Infinite loop if mid == left
    } else {
        right = mid - 1;
    }
}

// ✅ Correct: Ensure progress
while (left < right) {
    int mid = left + (right - left + 1) / 2; // Ceiling division
    if (condition) {
        left = mid;
    } else {
        right = mid - 1;
    }
}
```

### 3. Boundary Conditions
```java
// Always consider edge cases:
- Empty array
- Single element
- Target at boundaries
- Target not in array
```

## When to Use Binary Search

### ✅ Use When:
- Data is **sorted**
- Need **O(log n)** search time
- Working with **large datasets**
- **Monotonic** answer space
- Need to find **boundaries** or **ranges**

### ❌ Don't Use When:
- Data is **unsorted** (unless you can sort it first)
- **Small datasets** where linear search is simpler
- Need to find **all occurrences** efficiently

## Best Practices

1. **Always check if array is sorted**
2. **Handle edge cases** (empty, single element)
3. **Use appropriate boundary conditions**
4. **Avoid integer overflow** in mid calculation
5. **Test with simple examples** first
6. **Consider using built-in functions** (`Arrays.binarySearch()` in Java)

## Related Topics
- **Two Pointers**: Similar divide-and-conquer approach
- **Divide and Conquer**: Binary search is a classic example
- **Sorting Algorithms**: Required for binary search
- **Tree Traversals**: Binary search trees use similar concepts

## Summary

Binary Search is a powerful algorithm that reduces search time from O(n) to O(log n) by systematically eliminating half of the search space. Master the basic template and its variations, as binary search problems are extremely common in coding interviews!

**Remember**: The key insight is recognizing when you can use binary search - look for sorted data or monotonic answer spaces.