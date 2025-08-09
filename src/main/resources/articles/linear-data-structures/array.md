# Array

An array is a collection of elements stored at contiguous memory locations. It is one of the most fundamental and widely used data structures in computer science.

## Basic Description

Arrays provide a way to store multiple elements of the same data type under a single name. Elements can be accessed directly using their index position, making arrays very efficient for random access operations.

## Properties

- **Contiguous memory**: Elements are stored in consecutive memory locations
- **Fixed size**: Size is determined at creation time (for static arrays)
- **Homogeneous**: All elements are of the same data type
- **Indexed access**: Elements accessed using zero-based indexing
- **Constant time access**: O(1) time complexity for accessing elements

## Types of Arrays

### Static Array
Fixed size determined at compile time or initialization.

### Dynamic Array
Size can be changed during runtime (e.g., vectors, lists).

### Multidimensional Array
Arrays with more than one dimension (2D, 3D, etc.).

### Sparse Array
Array where most elements have the same value (typically zero).

## Applications

Arrays are used in:
- Storing and manipulating collections of data
- Implementing other data structures (stacks, queues, heaps)
- Mathematical computations and matrix operations
- Database tables and records
- Image and signal processing

## Common Operations

- **Access**: Retrieve element at specific index - O(1)
- **Update**: Modify element at specific index - O(1)
- **Search**: Find element in array - O(n) for unsorted, O(log n) for sorted
- **Insertion**: Add element - O(n) for static arrays
- **Deletion**: Remove element - O(n) for maintaining order