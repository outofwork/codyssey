#!/bin/bash

# Script to create SOURCE labels
# Category ID: CAT-100005

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100005"

echo "Creating SOURCE Labels..."
echo "========================="

echo "Creating LeetCode..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "LeetCode",
    "description": "Questions from LeetCode platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating GeeksforGeeks..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "GeeksforGeeks",
    "description": "Questions from GeeksforGeeks platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating HackerRank..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "HackerRank",
    "description": "Questions from HackerRank platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Codeforces..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Codeforces",
    "description": "Questions from Codeforces competitive programming platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating CodeChef..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "CodeChef",
    "description": "Questions from CodeChef competitive programming platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Project Euler..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Project Euler",
    "description": "Mathematical and computational problem-solving questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating InterviewBit..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "InterviewBit",
    "description": "Questions from InterviewBit platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating AtCoder..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "AtCoder",
    "description": "Questions from AtCoder competitive programming platform",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating CSES..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "CSES",
    "description": "Questions from CSES Problem Set",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Custom / User-Defined..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Custom / User-Defined",
    "description": "Custom questions created by users",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "========================="
echo "SOURCE labels creation completed!"