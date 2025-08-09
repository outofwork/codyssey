#!/bin/bash

# Script to create DATA_STRUCTURE labels with hierarchical relationships
# Category ID: CAT-100002

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100002"

echo "Creating DATA_STRUCTURE Labels with Hierarchical Relationships..."
echo "================================================================"

# Level 1: Main Categories
echo "Creating Linear Data Structures..."
LINEAR_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Linear Data Structures",
    "description": "Data structures with sequential organization",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Linear Data Structures ID: $LINEAR_ID"
echo -e "\n"

echo "Creating Hashing..."
HASHING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Hashing",
    "description": "Hash-based data structures for fast lookup",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Hashing ID: $HASHING_ID"
echo -e "\n"

echo "Creating Trees..."
TREES_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Trees",
    "description": "Hierarchical tree-based data structures",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Trees ID: $TREES_ID"
echo -e "\n"

echo "Creating Graphs..."
GRAPHS_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Graphs",
    "description": "Graph-based data structures and representations",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Graphs ID: $GRAPHS_ID"
echo -e "\n"

echo "Creating Heaps..."
HEAPS_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Heaps",
    "description": "Heap-based priority data structures",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Heaps ID: $HEAPS_ID"
echo -e "\n"

echo "Creating Disjoint Set / Union-Find..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Disjoint Set / Union-Find",
    "description": "Union-Find data structure for disjoint sets",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Probabilistic Data Structures..."
PROB_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Probabilistic Data Structures",
    "description": "Space-efficient probabilistic data structures",
    "categoryId": "'$CATEGORY_ID'"
  }' | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Probabilistic Data Structures ID: $PROB_ID"
echo -e "\n"

# Level 2: Linear Data Structures Children
echo "Creating Array (under Linear Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Array\",
    \"description\": \"Fixed-size sequential data structure\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINEAR_ID\"
  }"
echo -e "\n"

echo "Creating Linked List (under Linear Data Structures)..."
LINKED_LIST_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Linked List\",
    \"description\": \"Dynamic sequential data structure with nodes\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINEAR_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Linked List ID: $LINKED_LIST_ID"
echo -e "\n"

echo "Creating Stack (under Linear Data Structures)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Stack\",
    \"description\": \"LIFO (Last In First Out) data structure\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINEAR_ID\"
  }"
echo -e "\n"

echo "Creating Queue (under Linear Data Structures)..."
QUEUE_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Queue\",
    \"description\": \"FIFO (First In First Out) data structure\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINEAR_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Queue ID: $QUEUE_ID"
echo -e "\n"

# Level 3: Linked List Types
echo "Creating Singly Linked List (under Linked List)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Singly Linked List\",
    \"description\": \"Linked list with forward pointers only\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINKED_LIST_ID\"
  }"
echo -e "\n"

echo "Creating Doubly Linked List (under Linked List)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Doubly Linked List\",
    \"description\": \"Linked list with forward and backward pointers\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINKED_LIST_ID\"
  }"
echo -e "\n"

echo "Creating Circular Linked List (under Linked List)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Circular Linked List\",
    \"description\": \"Linked list where last node points to first\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$LINKED_LIST_ID\"
  }"
echo -e "\n"

# Level 3: Queue Types
echo "Creating Simple Queue (under Queue)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Simple Queue\",
    \"description\": \"Basic FIFO queue implementation\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$QUEUE_ID\"
  }"
echo -e "\n"

echo "Creating Circular Queue (under Queue)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Circular Queue\",
    \"description\": \"Queue with circular buffer implementation\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$QUEUE_ID\"
  }"
echo -e "\n"

echo "Creating Priority Queue (under Queue)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Priority Queue\",
    \"description\": \"Queue where elements are served by priority\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$QUEUE_ID\"
  }"
echo -e "\n"

echo "Creating Deque (under Queue)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Deque\",
    \"description\": \"Double-ended queue allowing insertion/deletion at both ends\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$QUEUE_ID\"
  }"
echo -e "\n"

# Level 2: Hashing Children
echo "Creating Hash Table / Hash Map (under Hashing)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Hash Table / Hash Map\",
    \"description\": \"Key-value mapping using hash functions\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HASHING_ID\"
  }"
echo -e "\n"

echo "Creating Hash Set (under Hashing)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Hash Set\",
    \"description\": \"Set implementation using hash table\",
    \"categoryId\": \"$CATEGORY_ID\",
    \"parentId\": \"$HASHING_ID\"
  }"
echo -e "\n"

echo "================================================================"
echo "DATA_STRUCTURE labels (Part 1/2) creation completed!"
echo "Continue with part 2 for Trees, Graphs, Heaps, and Probabilistic structures..."