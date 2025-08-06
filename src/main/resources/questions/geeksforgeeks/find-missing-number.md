# Find the Missing Number

## Problem Statement

Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number in the range that is missing from the array.

## Examples

### Example 1:
- **Input:** `nums = [3,0,1]`
- **Output:** `2`
- **Explanation:** n = 3 since there are 3 numbers, so all numbers are in the range [0,3]. 2 is the missing number in the range since it does not appear in nums.

### Example 2:
- **Input:** `nums = [0,1]`
- **Output:** `2`
- **Explanation:** n = 2 since there are 2 numbers, so all numbers are in the range [0,2]. 2 is the missing number in the range since it does not appear in nums.

### Example 3:
- **Input:** `nums = [9,6,4,2,3,5,7,0,1]`
- **Output:** `8`
- **Explanation:** n = 9 since there are 9 numbers, so all numbers are in the range [0,9]. 8 is the missing number in the range since it does not appear in nums.

## Constraints
- `n == nums.length`
- `1 <= n <= 10^4`
- `0 <= nums[i] <= n`
- All the numbers of `nums` are unique.

## Follow-up
Could you implement a solution using only O(1) extra space complexity and O(n) time complexity?

## Solution Approaches

### Approach 1: Mathematical Sum (Optimal)
- **Time Complexity:** O(n)
- **Space Complexity:** O(1)

Calculate the expected sum of numbers from 0 to n, then subtract the actual sum of the array.

**Formula:** Expected Sum = n × (n + 1) / 2

### Approach 2: XOR Operation
- **Time Complexity:** O(n)
- **Space Complexity:** O(1)

Use the property that XOR of a number with itself is 0, and XOR is commutative.

### Approach 3: Hash Set
- **Time Complexity:** O(n)
- **Space Complexity:** O(n)

Store all numbers in a hash set and check which number from 0 to n is missing.

### Approach 4: Binary Search (After Sorting)
- **Time Complexity:** O(n log n)
- **Space Complexity:** O(1)

Sort the array and use binary search to find the missing number.

## Implementation Notes

### Mathematical Approach:
```
missing = n * (n + 1) / 2 - sum(nums)
```

### XOR Approach:
```
missing = n
for i in range(n):
    missing ^= i ^ nums[i]
```

## Tags
- Array
- Math
- Bit Manipulation
- Hash Table
- Binary Search

## Companies
- Amazon
- Microsoft
- Google
- Facebook

## Difficulty
Easy

## Practice Level
Basic