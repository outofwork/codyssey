# Graphs

A graph is a non-linear data structure consisting of vertices (nodes) and edges that connect these vertices. Graphs are used to represent relationships between objects.

## Basic Description

Graphs are powerful data structures that can model complex relationships and networks. They consist of a finite set of vertices connected by edges, where edges represent relationships or connections between vertices.

## Components

- **Vertices (Nodes)**: The fundamental units that represent entities
- **Edges**: Connections between vertices that represent relationships
- **Weight**: Optional values assigned to edges
- **Path**: A sequence of vertices connected by edges
- **Cycle**: A path that starts and ends at the same vertex

## Types of Graphs

### By Direction
- **Directed Graph (Digraph)**: Edges have a direction
- **Undirected Graph**: Edges have no direction

### By Weight
- **Weighted Graph**: Edges have associated weights/costs
- **Unweighted Graph**: All edges are considered equal

### By Connectivity
- **Connected Graph**: Path exists between every pair of vertices
- **Disconnected Graph**: Some vertices are not reachable from others

### By Cycles
- **Cyclic Graph**: Contains at least one cycle
- **Acyclic Graph**: Contains no cycles

## Applications

Graphs are used in:
- Social networks and relationships
- Transportation and routing systems
- Computer networks and internet topology
- Dependency resolution in software
- Game theory and decision making
- Web page ranking algorithms
- Circuit design and analysis

## Common Algorithms

- **Traversal**: DFS (Depth-First Search), BFS (Breadth-First Search)
- **Shortest Path**: Dijkstra's, Bellman-Ford, Floyd-Warshall
- **Minimum Spanning Tree**: Prim's, Kruskal's
- **Topological Sort**: For directed acyclic graphs
- **Strongly Connected Components**: Kosaraju's, Tarjan's