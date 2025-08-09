#!/bin/bash

# Script to create PROGRAMMING_LANGUAGE_TAGS labels
# Category ID: CAT-100009

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100009"

echo "Creating PROGRAMMING_LANGUAGE_TAGS Labels..."
echo "============================================"

echo "Creating Java..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Java",
    "description": "Java programming language",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Python..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Python",
    "description": "Python programming language",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating C++..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "C++",
    "description": "C++ programming language",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating C..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "C",
    "description": "C programming language",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating JavaScript..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "JavaScript",
    "description": "JavaScript programming language",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "============================================"
echo "PROGRAMMING_LANGUAGE_TAGS labels creation completed!"