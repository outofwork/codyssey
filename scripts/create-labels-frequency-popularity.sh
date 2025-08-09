#!/bin/bash

# Script to create FREQUENCY_POPULARITY labels
# Category ID: CAT-100010

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100010"

echo "Creating FREQUENCY_POPULARITY Labels..."
echo "======================================"

echo "Creating Most Frequently Asked..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Most Frequently Asked",
    "description": "Questions asked very commonly in interviews",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Frequently Asked..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Frequently Asked",
    "description": "Questions asked commonly in interviews",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Occasionally Asked..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Occasionally Asked",
    "description": "Questions asked sometimes in interviews",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Rarely Asked..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Rarely Asked",
    "description": "Questions asked infrequently in interviews",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating High Signal..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "High Signal",
    "description": "Questions that provide strong evaluation signal",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "Creating Low Signal..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Low Signal",
    "description": "Questions that provide limited evaluation signal",
    "categoryId": "'$CATEGORY_ID'"
  }'
echo -e "\n"

echo "======================================"
echo "FREQUENCY_POPULARITY labels creation completed!"