#!/bin/bash

# Script to create DIFFICULTY_LEVEL labels
# Category ID: CAT-100001

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100001"

echo "Creating DIFFICULTY_LEVEL Labels..."
echo "=================================="

# Difficulty Level Labels (No parent-child hierarchy)
echo "Creating Easy..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Easy",
    "description": "Beginner level questions that test basic understanding",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Medium..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Medium",
    "description": "Intermediate level questions requiring algorithmic thinking",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Hard..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Hard",
    "description": "Advanced level questions with complex problem-solving",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Very Hard..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Very Hard",
    "description": "Expert level questions requiring deep algorithmic knowledge",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Expert..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Expert",
    "description": "Master level questions for advanced practitioners",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "=================================="
echo "DIFFICULTY_LEVEL labels creation completed!"