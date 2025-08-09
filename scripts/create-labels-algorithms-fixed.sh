#!/bin/bash

# Script to create ALGORITHMS labels with hierarchical relationships
# Category ID: CAT-100003

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100003"

echo "Creating ALGORITHMS Labels with Hierarchical Relationships..."
echo "============================================================"

# Level 1: Main Algorithm Categories
echo "Creating Sorting..."
SORTING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Sorting\",
    \"description\": \"Algorithms for arranging data in order\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Sorting ID: $SORTING_ID"
echo -e "\n"

echo "Creating Searching..."
SEARCHING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Searching\",
    \"description\": \"Algorithms for finding elements in data structures\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Searching ID: $SEARCHING_ID"
echo -e "\n"

echo "Creating Backtracking..."
BACKTRACKING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Backtracking\",
    \"description\": \"Algorithmic approach using trial and error with rollback\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Backtracking ID: $BACKTRACKING_ID"
echo -e "\n"

echo "Creating Dynamic Programming..."
DP_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Dynamic Programming\",
    \"description\": \"Optimization technique using memoization and optimal substructure\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Dynamic Programming ID: $DP_ID"
echo -e "\n"

echo "Creating Greedy..."
GREEDY_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Greedy\",
    \"description\": \"Algorithms making locally optimal choices\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Greedy ID: $GREEDY_ID"
echo -e "\n"

echo "Creating Graph Algorithms..."
GRAPH_ALGO_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Graph Algorithms\",
    \"description\": \"Algorithms for graph traversal and analysis\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Graph Algorithms ID: $GRAPH_ALGO_ID"
echo -e "\n"

echo "Creating Bit Manipulation..."
BIT_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Bit Manipulation\",
    \"description\": \"Algorithms using bitwise operations\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Bit Manipulation ID: $BIT_ID"
echo -e "\n"

echo "Creating Math..."
MATH_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Math\",
    \"description\": \"Mathematical algorithms and number theory\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Math ID: $MATH_ID"
echo -e "\n"

echo "Creating String Algorithms..."
STRING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"String Algorithms\",
    \"description\": \"Algorithms for string processing and pattern matching\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "String Algorithms ID: $STRING_ID"
echo -e "\n"

echo "Creating Geometry Algorithms..."
GEOMETRY_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Geometry Algorithms\",
    \"description\": \"Computational geometry algorithms\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Geometry Algorithms ID: $GEOMETRY_ID"
echo -e "\n"

# Standalone algorithms (no children)
standalone_algos=(
  "Recursion:Algorithmic approach where function calls itself"
  "Two Pointers:Technique using two pointers to traverse data"
  "Sliding Window:Technique for processing subarrays or substrings"
  "Divide and Conquer:Breaking problems into smaller subproblems"
  "Binary Search on Answer:Using binary search to find optimal values"
  "Tree / Graph Traversals:Methods for visiting all nodes"
  "Randomized Algorithms:Algorithms using random choices"
  "Monotonic Stack / Queue:Data structures maintaining order property"
  "Game Theory / Minimax:Algorithms for optimal game strategies"
  "Fast Exponentiation:Efficient method for computing powers"
)

for item in "${standalone_algos[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\"
    }"
  echo -e "\n"
done

# Level 2: Sorting Algorithms
sorting_algos=(
  "Bubble Sort:Simple comparison-based sorting"
  "Insertion Sort:Building sorted array one element at a time"
  "Selection Sort:Finding minimum element and swapping"
  "Merge Sort:Divide and conquer sorting algorithm"
  "Quick Sort:Partition-based sorting algorithm"
  "Heap Sort:Sorting using heap data structure"
  "Radix Sort:Non-comparison based sorting for integers"
  "Counting Sort:Non-comparison sorting for small range integers"
)

for item in "${sorting_algos[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Sorting)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$SORTING_ID\"
    }"
  echo -e "\n"
done

# Level 2: Searching Algorithms
searching_algos=(
  "Linear Search:Sequential search through elements"
  "Binary Search:Efficient search in sorted arrays"
  "Exponential Search:Search by exponentially increasing bounds"
  "Ternary Search:Search dividing range into three parts"
)

for item in "${searching_algos[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Searching)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$SEARCHING_ID\"
    }"
  echo -e "\n"
done

# Level 2: Backtracking Problems
backtracking_problems=(
  "N-Queens:Placing N queens on chessboard"
  "Sudoku Solver:Solving Sudoku puzzles"
  "Word Search:Finding words in character grid"
)

for item in "${backtracking_problems[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Backtracking)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$BACKTRACKING_ID\"
    }"
  echo -e "\n"
done

# Level 2: Dynamic Programming Problems
dp_problems=(
  "0/1 Knapsack:Classic knapsack optimization problem"
  "Longest Common Subsequence:Finding longest common subsequence"
  "Longest Increasing Subsequence:Finding longest increasing subsequence"
  "Matrix Chain Multiplication:Optimal matrix multiplication order"
  "DP on Trees:Dynamic programming on tree structures"
)

for item in "${dp_problems[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Dynamic Programming)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$DP_ID\"
    }"
  echo -e "\n"
done

# Level 2: Greedy Problems
greedy_problems=(
  "Activity Selection:Selecting maximum non-overlapping activities"
  "Huffman Encoding:Optimal prefix-free encoding"
  "Job Scheduling:Scheduling jobs for optimal profit"
  "Coin Change (Greedy variant):Making change with minimum coins"
)

for item in "${greedy_problems[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Greedy)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$GREEDY_ID\"
    }"
  echo -e "\n"
done

# Level 2: Graph Algorithms
graph_algorithms=(
  "BFS (Breadth-First Search):Level-order graph traversal"
  "DFS (Depth-First Search):Depth-first graph traversal"
  "Dijkstra's Algorithm:Shortest path in weighted graphs"
  "Bellman-Ford:Shortest path with negative weights"
  "Floyd-Warshall:All-pairs shortest path algorithm"
  "Topological Sort:Linear ordering of directed acyclic graph"
  "Union-Find:Disjoint set data structure operations"
  "Kruskal's / Prim's (MST):Minimum spanning tree algorithms"
)

for item in "${graph_algorithms[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Graph Algorithms)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$GRAPH_ALGO_ID\"
    }"
  echo -e "\n"
done

# Level 2: Bit Manipulation Techniques
bit_techniques=(
  "XOR Tricks:Using XOR for various bit manipulations"
  "Set/Clear/Toggle Bit:Basic bit operations"
  "Bitmasking:Using bitmasks for subset enumeration"
)

for item in "${bit_techniques[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Bit Manipulation)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$BIT_ID\"
    }"
  echo -e "\n"
done

# Level 2: Math Algorithms
math_algorithms=(
  "GCD / LCM:Greatest common divisor and least common multiple"
  "Modular Arithmetic:Arithmetic operations with modulo"
  "Prime Number Algorithms (Sieve of Eratosthenes):Finding prime numbers efficiently"
  "Combinatorics:Counting and arrangement problems"
  "Number Theory:Mathematical properties of numbers"
)

for item in "${math_algorithms[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Math)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$MATH_ID\"
    }"
  echo -e "\n"
done

# Level 2: String Algorithms
string_algorithms=(
  "KMP (Knuth-Morris-Pratt):Efficient string pattern matching"
  "Rabin-Karp:Rolling hash-based pattern matching"
  "Z-Algorithm:Linear time pattern matching algorithm"
  "Manacher's Algorithm:Finding all palindromes in linear time"
)

for item in "${string_algorithms[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under String Algorithms)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$STRING_ID\"
    }"
  echo -e "\n"
done

# Level 2: Geometry Algorithms
geometry_algorithms=(
  "Convex Hull:Finding convex hull of points"
  "Line Intersection:Finding intersection of line segments"
)

for item in "${geometry_algorithms[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name (under Geometry Algorithms)..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\",
      \"parentId\": \"$GEOMETRY_ID\"
    }"
  echo -e "\n"
done

echo "============================================================"
echo "ALGORITHMS labels creation completed!"