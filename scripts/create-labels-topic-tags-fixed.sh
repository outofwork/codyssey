#!/bin/bash

# Script to create TOPIC_TAGS labels with hierarchical relationships
# Category ID: CAT-100006

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100006"

echo "Creating TOPIC_TAGS Labels with Hierarchical Relationships..."
echo "============================================================"

# Level 1: Main Topic Categories
echo "Creating Recursion..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Recursion\",
    \"description\": \"Recursive problem-solving techniques\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Dynamic Programming Paradigms..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Dynamic Programming Paradigms\",
    \"description\": \"Different approaches to dynamic programming\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Bitwise Techniques..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Bitwise Techniques\",
    \"description\": \"Bit manipulation and bitwise operation techniques\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Pointer Techniques..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Pointer Techniques\",
    \"description\": \"Pointer-based problem-solving approaches\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Range Techniques..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Range Techniques\",
    \"description\": \"Techniques for handling ranges and subarrays\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Binary Search Techniques..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Binary Search Techniques\",
    \"description\": \"Advanced binary search applications\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Search Paradigms..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Search Paradigms\",
    \"description\": \"Different search and exploration strategies\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Optimization Strategies..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Optimization Strategies\",
    \"description\": \"Techniques for optimizing solutions\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

# Standalone topic tags
echo "Creating Simulation..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Simulation\",
    \"description\": \"Simulating processes and systems\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Math Tricks..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Math Tricks\",
    \"description\": \"Mathematical shortcuts and techniques\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Game Theory..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Game Theory\",
    \"description\": \"Strategic decision-making in competitive scenarios\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "============================================================"
echo "TOPIC_TAGS labels (main categories) creation completed!"
echo "Note: Child labels can be added later using individual API calls"