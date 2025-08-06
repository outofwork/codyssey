#!/bin/bash

# Script to create ENVIRONMENT_ROUND_TYPE labels
# Category ID: CAT-100008

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100008"

echo "Creating ENVIRONMENT_ROUND_TYPE Labels..."
echo "========================================"

echo "Creating Online Assessment (OA)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Online Assessment (OA)",
    "description": "Online coding assessments and tests",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Phone Screen..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Phone Screen",
    "description": "Initial phone screening interviews",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Technical Interview..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Technical Interview",
    "description": "Technical round interviews with coding questions",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Onsite Interview..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Onsite Interview",
    "description": "In-person or virtual onsite interview rounds",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Coding Round..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Coding Round",
    "description": "Dedicated coding assessment rounds",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating System Design Round..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "System Design Round",
    "description": "System architecture and design interview rounds",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Behavioral Round..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Behavioral Round",
    "description": "Behavioral and cultural fit interview rounds",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Take Home Assignment..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Take Home Assignment",
    "description": "Extended coding assignments completed at home",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Contest..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Contest",
    "description": "Competitive programming contests and challenges",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "========================================"
echo "ENVIRONMENT_ROUND_TYPE labels creation completed!"