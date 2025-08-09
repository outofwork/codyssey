#!/bin/bash

# Script to test the navigation and MCQ functionality
# This script demonstrates the complete navigation flow

BASE_URL="http://localhost:8080/api/v1"

echo "Testing Navigation and MCQ API Endpoints"
echo "========================================"

echo ""
echo "1. Testing Navigation - Get All Categories"
echo "curl -X GET $BASE_URL/navigation/categories"
curl -X GET "$BASE_URL/navigation/categories" | jq '.'

echo ""
echo "2. Testing Navigation - Get Root Labels for First Category"
echo "Note: Replace 'data-structure' with actual category slug from step 1"
echo "curl -X GET $BASE_URL/navigation/categories/data-structure/labels/root"
# curl -X GET "$BASE_URL/navigation/categories/data-structure/labels/root" | jq '.'

echo ""
echo "3. Testing Navigation - Get Specific Label Details"
echo "Note: Replace 'array' with actual label slug"
echo "curl -X GET $BASE_URL/navigation/labels/array"
# curl -X GET "$BASE_URL/navigation/labels/array" | jq '.'

echo ""
echo "4. Testing MCQ - Get All MCQ Questions"
echo "curl -X GET $BASE_URL/mcq"
curl -X GET "$BASE_URL/mcq" | jq '.'

echo ""
echo "5. Testing MCQ - Get MCQs by Label"
echo "Note: Replace 'array' with actual label slug"
echo "curl -X GET $BASE_URL/mcq/labels/array/questions"
# curl -X GET "$BASE_URL/mcq/labels/array/questions" | jq '.'

echo ""
echo "6. Testing MCQ - Get Random MCQs by Label"
echo "Note: Replace 'array' with actual label slug"
echo "curl -X GET $BASE_URL/mcq/labels/array/questions/random?count=5"
# curl -X GET "$BASE_URL/mcq/labels/array/questions/random?count=5" | jq '.'

echo ""
echo "7. Testing MCQ - Get MCQ Count by Label"
echo "Note: Replace 'array' with actual label slug"
echo "curl -X GET $BASE_URL/mcq/labels/array/count"
# curl -X GET "$BASE_URL/mcq/labels/array/count"

echo ""
echo "8. Testing Navigation - Get Child Labels"
echo "Note: Replace 'trees' with actual parent label slug"
echo "curl -X GET $BASE_URL/navigation/labels/trees/children"
# curl -X GET "$BASE_URL/navigation/labels/trees/children" | jq '.'

echo ""
echo "Test completed!"
echo ""
echo "Manual Testing Steps:"
echo "1. First, ensure your application is running"
echo "2. Create label categories using: ./scripts/create-label-categories.sh"
echo "3. Create labels using: ./scripts/create-all-labels.sh"
echo "4. Create sample MCQs using: ./scripts/create-sample-mcqs.sh"
echo "5. Associate MCQs with labels using: ./scripts/associate-mcq-labels.sh"
echo "6. Update the label/category slugs in this script and run specific tests"
echo ""
echo "Example Navigation Flow:"
echo "1. GET /navigation/categories → Get all categories"
echo "2. GET /navigation/categories/{categorySlug}/labels/root → Get root labels"
echo "3. GET /navigation/labels/{labelSlug} → Get label details with navigation links"
echo "4. GET /mcq/labels/{labelSlug}/questions/random?count=10 → Practice with MCQs"
