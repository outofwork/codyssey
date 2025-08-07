# Preorder Traversal

Preorder traversal is a depth-first traversal method where nodes are visited in the order: Root → Left Subtree → Right Subtree.

## Basic Description

In preorder traversal, we visit the current node first, then recursively traverse the left subtree, followed by the right subtree. This traversal is useful when you need to process the parent node before its children.

## Algorithm Steps

1. Visit the current node (process the data)
2. Recursively traverse the left subtree
3. Recursively traverse the right subtree

## Implementation Approaches

### Recursive Implementation
The most intuitive and commonly used approach using function calls.

### Iterative Implementation
Uses an explicit stack to simulate the recursive calls.

## Applications

Preorder traversal is used for:
- Creating a copy of the tree
- Getting prefix expression from expression trees
- Tree serialization
- Directory structure listing
- Syntax tree processing in compilers

## Time and Space Complexity

- **Time Complexity**: O(n) where n is the number of nodes
- **Space Complexity**: 
  - Recursive: O(h) where h is the height of the tree
  - Iterative: O(h) for the explicit stack

## Example

For a tree with nodes: 1 (root), 2 (left child), 3 (right child)
Preorder traversal result: 1, 2, 3