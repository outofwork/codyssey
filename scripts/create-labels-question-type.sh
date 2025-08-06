#!/bin/bash

# Script to create QUESTION_TYPE labels
# Category ID: CAT-100007

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100007"

echo "Creating QUESTION_TYPE Labels..."
echo "==============================="

echo "Creating Coding..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Coding",
    "description": "Programming and algorithm implementation questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating System Design..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "System Design",
    "description": "Architecture and system design questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Behavioral..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Behavioral",
    "description": "Soft skills and experience-based questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating SQL..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "SQL",
    "description": "Database query and design questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Shell Scripting..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Shell Scripting",
    "description": "Command line and shell scripting questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Debugging..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Debugging",
    "description": "Code debugging and error identification questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Code Comprehension..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Code Comprehension",
    "description": "Understanding and analyzing existing code",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Multiple Choice..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Multiple Choice",
    "description": "Multiple choice theoretical questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Theoretical / Conceptual..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Theoretical / Conceptual",
    "description": "Conceptual understanding and theoretical questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "==============================="
echo "QUESTION_TYPE labels creation completed!"