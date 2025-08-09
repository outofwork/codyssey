#!/bin/bash

# Script to associate MCQ questions with labels
# Run this after creating MCQ questions and having labels in the system

BASE_URL="http://localhost:8080/api/v1"
CONTENT_TYPE="Content-Type: application/json"

echo "Associating MCQ questions with labels..."

# You'll need to get the actual IDs from your database
# These are example associations - update with real IDs

# Array MCQ associations
echo "Associating Array MCQs..."

# MCQ about array time complexity with Array label
curl -X POST "$BASE_URL/mcq/labels" \
  -H "$CONTENT_TYPE" \
  -d '{
    "mcqQuestionId": "MCQ-100001",
    "labelId": "LBL-100001",
    "relevanceScore": 10,
    "isPrimary": true,
    "notes": "Core array concept"
  }'

# Tree MCQ associations
echo "Associating Tree MCQs..."

# MCQ about binary tree with Trees label
curl -X POST "$BASE_URL/mcq/labels" \
  -H "$CONTENT_TYPE" \
  -d '{
    "mcqQuestionId": "MCQ-100004",
    "labelId": "LBL-100010",
    "relevanceScore": 9,
    "isPrimary": true,
    "notes": "Binary tree fundamentals"
  }'

# Algorithm MCQ associations
echo "Associating Algorithm MCQs..."

# Sorting algorithm MCQ with Algorithms label
curl -X POST "$BASE_URL/mcq/labels" \
  -H "$CONTENT_TYPE" \
  -d '{
    "mcqQuestionId": "MCQ-100007",
    "labelId": "LBL-100015",
    "relevanceScore": 8,
    "isPrimary": true,
    "notes": "Sorting algorithm comparison"
  }'

echo "MCQ-Label associations created successfully!"
echo ""
echo "You can now test the following endpoints:"
echo "1. Get MCQs by label: GET $BASE_URL/mcq/labels/{labelId}/questions"
echo "2. Get random MCQs: GET $BASE_URL/mcq/labels/{labelId}/questions/random?count=5"
echo "3. Get navigation: GET $BASE_URL/navigation/categories"
echo ""
echo "Note: Replace the IDs in this script with actual IDs from your database."
