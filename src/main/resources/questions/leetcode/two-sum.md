# Two Sum

## Problem Statement

Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

## Examples

### Example 1:
- **Input:** `nums = [2,7,11,15]`, `target = 9`
- **Output:** `[0,1]`
- **Explanation:** Because `nums[0] + nums[1] == 9`, we return `[0, 1]`.

### Example 2:
- **Input:** `nums = [3,2,4]`, `target = 6`
- **Output:** `[1,2]`

### Example 3:
- **Input:** `nums = [3,3]`, `target = 6`
- **Output:** `[0,1]`

## Constraints

- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- Only one valid answer exists.

## Follow-up
Can you come up with an algorithm that is less than O(n²) time complexity?

## Solution Approach

### Approach 1: Hash Map (Optimal)
- **Time Complexity:** O(n)
- **Space Complexity:** O(n)

Use a hash map to store the complement of each number as we iterate through the array.

### Approach 2: Two Pointers (After Sorting)
- **Time Complexity:** O(n log n)
- **Space Complexity:** O(1) or O(n) depending on sorting algorithm

Sort the array first, then use two pointers from both ends.

### Approach 3: Brute Force
- **Time Complexity:** O(n²)
- **Space Complexity:** O(1)

Check every pair of numbers to see if they sum to the target.

## Tags
- Array
- Hash Table
- Two Pointers

## Companies
- Amazon
- Google
- Microsoft
- Facebook
- Apple