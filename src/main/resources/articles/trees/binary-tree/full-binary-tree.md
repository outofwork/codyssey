# Full Binary Tree

A full binary tree is a binary tree where every node has either 0 or 2 children. No node in a full binary tree has exactly one child.

## Basic Description

In a full binary tree, every internal node has exactly two children, and all leaf nodes are at the same level or differ by at most one level. This property ensures optimal space utilization and balanced structure.

## Properties

- Every internal node has exactly 2 children
- All leaf nodes have 0 children
- Number of leaf nodes = Number of internal nodes + 1
- Total nodes in a full binary tree with height h: 2^(h+1) - 1

## Characteristics

- **Complete filling**: No node has exactly one child
- **Optimal structure**: Maximizes the number of nodes for a given height
- **Balanced nature**: Provides good performance for tree operations

## Applications

Full binary trees are used in:
- Expression trees for arithmetic operations
- Decision trees with binary choices
- Huffman coding trees
- Heap data structures
- Binary space partitioning

## Examples

Common examples of full binary trees:
- Arithmetic expression trees
- Complete binary heaps
- Perfect binary trees (special case)

## Implementation Considerations

- Memory efficient due to complete node utilization
- Predictable structure aids in array-based implementations
- Optimal for recursive algorithms