#!/bin/bash

# Script to create DATA_STRUCTURE labels - Part 2 (Trees, Graphs, Heaps, Probabilistic)
# Category ID: CAT-100002
# Run this after create-labels-data-structure.sh

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100002"

echo "Creating DATA_STRUCTURE Labels - Part 2..."
echo "=========================================="

# Get Trees, Graphs, Heaps, and Probabilistic parent IDs
echo "Fetching parent IDs..."
ALL_LABELS=$(curl -s "$BASE_URL?categoryId=$CATEGORY_ID")

TREES_ID=$(echo "$ALL_LABELS" | grep -o '"id":"[^"]*","name":"Trees"' | head -1 | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
GRAPHS_ID=$(echo "$ALL_LABELS" | grep -o '"id":"[^"]*","name":"Graphs"' | head -1 | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
HEAPS_ID=$(echo "$ALL_LABELS" | grep -o '"id":"[^"]*","name":"Heaps"' | head -1 | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
PROB_ID=$(echo "$ALL_LABELS" | grep -o '"id":"[^"]*","name":"Probabilistic Data Structures"' | head -1 | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

echo "Trees ID: $TREES_ID"
echo "Graphs ID: $GRAPHS_ID" 
echo "Heaps ID: $HEAPS_ID"
echo "Probabilistic ID: $PROB_ID"
echo -e "\n"

# Trees Level 2
echo "Creating Binary Tree (under Trees)..."
BINARY_TREE_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Binary Tree\",
    \"description\": \"Tree with at most two children per node\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Binary Tree ID: $BINARY_TREE_ID"
echo -e "\n"

echo "Creating Binary Search Tree (BST) (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Binary Search Tree (BST)\",
    \"description\": \"Binary tree with ordering property\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Trie (Prefix Tree) (under Trees)..."
TRIE_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Trie (Prefix Tree)\",
    \"description\": \"Tree for storing strings with common prefixes\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Trie ID: $TRIE_ID"
echo -e "\n"

# Trees Level 2 - More types (simple names to avoid URL issues)
echo "Creating AVL Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"AVL Tree\",
    \"description\": \"Self-balancing binary search tree\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Red-Black Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Red-Black Tree\",
    \"description\": \"Self-balancing binary search tree with color properties\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Segment Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Segment Tree\",
    \"description\": \"Tree for range queries and updates\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Fenwick Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Fenwick Tree\",
    \"description\": \"Binary Indexed Tree for prefix sum queries\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Interval Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Interval Tree\",
    \"description\": \"Tree for interval overlap queries\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating B-Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"B-Tree\",
    \"description\": \"Self-balancing tree for databases\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating B+ Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"B+ Tree\",
    \"description\": \"Variant of B-tree used in databases\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating Suffix Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Suffix Tree\",
    \"description\": \"Tree for string pattern matching\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating K-D Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"K-D Tree\",
    \"description\": \"Tree for multidimensional data\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

echo "Creating N-ary Tree (under Trees)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"N-ary Tree\",
    \"description\": \"Tree with variable number of children\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TREES_ID\"
  }"
echo -e "\n"

# Binary Tree Level 3
echo "Creating Full Binary Tree (under Binary Tree)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Full Binary Tree\",
    \"description\": \"Binary tree where every node has 0 or 2 children\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$BINARY_TREE_ID\"
  }"
echo -e "\n"

echo "Creating Complete Binary Tree (under Binary Tree)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Complete Binary Tree\",
    \"description\": \"Binary tree filled from left to right\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$BINARY_TREE_ID\"
  }"
echo -e "\n"

# Trie Level 3
echo "Creating Compressed Trie (under Trie)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Compressed Trie\",
    \"description\": \"Space-optimized trie\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TRIE_ID\"
  }"
echo -e "\n"

echo "Creating Suffix Trie (under Trie)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Suffix Trie\",
    \"description\": \"Trie storing all suffixes of a string\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TRIE_ID\"
  }"
echo -e "\n"

echo "Creating Radix Tree (under Trie)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Radix Tree\",
    \"description\": \"Compressed trie with path compression\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$TRIE_ID\"
  }"
echo -e "\n"

# Graphs Level 2
echo "Creating Directed Graph (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Directed Graph\",
    \"description\": \"Graph with directed edges\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Undirected Graph (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Undirected Graph\",
    \"description\": \"Graph with bidirectional edges\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Weighted Graph (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Weighted Graph\",
    \"description\": \"Graph with edge weights\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Unweighted Graph (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Unweighted Graph\",
    \"description\": \"Graph without edge weights\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Adjacency List (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Adjacency List\",
    \"description\": \"Graph representation using lists\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Adjacency Matrix (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Adjacency Matrix\",
    \"description\": \"Graph representation using matrix\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

echo "Creating Edge List (under Graphs)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Edge List\",
    \"description\": \"Graph representation as list of edges\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$GRAPHS_ID\"
  }"
echo -e "\n"

# Heaps Level 2
echo "Creating Min Heap (under Heaps)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Min Heap\",
    \"description\": \"Heap where parent is smaller than children\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HEAPS_ID\"
  }"
echo -e "\n"

echo "Creating Max Heap (under Heaps)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Max Heap\",
    \"description\": \"Heap where parent is larger than children\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HEAPS_ID\"
  }"
echo -e "\n"

echo "Creating Binomial Heap (under Heaps)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Binomial Heap\",
    \"description\": \"Collection of binomial trees\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HEAPS_ID\"
  }"
echo -e "\n"

echo "Creating Fibonacci Heap (under Heaps)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Fibonacci Heap\",
    \"description\": \"Advanced heap with amortized operations\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HEAPS_ID\"
  }"
echo -e "\n"

echo "Creating Pairing Heap (under Heaps)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Pairing Heap\",
    \"description\": \"Simplified Fibonacci heap\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HEAPS_ID\"
  }"
echo -e "\n"

# Probabilistic Data Structures Level 2
echo "Creating Bloom Filter (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Bloom Filter\",
    \"description\": \"Space-efficient probabilistic set membership\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "Creating Counting Bloom Filter (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Counting Bloom Filter\",
    \"description\": \"Bloom filter supporting deletions\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "Creating Cuckoo Filter (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Cuckoo Filter\",
    \"description\": \"Alternative to Bloom filter\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "Creating Count-Min Sketch (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Count-Min Sketch\",
    \"description\": \"Frequency estimation data structure\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "Creating HyperLogLog (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"HyperLogLog\",
    \"description\": \"Cardinality estimation algorithm\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "Creating Skip List (under Probabilistic Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Skip List\",
    \"description\": \"Probabilistic alternative to balanced trees\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$PROB_ID\"
  }"
echo -e "\n"

echo "=========================================="
echo "DATA_STRUCTURE labels (Part 2/2) creation completed!"