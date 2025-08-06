#!/bin/bash

# Master script to create all labels in the correct order
# This script runs all individual label creation scripts

echo "=========================================="
echo "Creating All Labels for Codyssey Platform"
echo "=========================================="
echo "This will create all labels with proper parent-child relationships"
echo "Make sure the application is running on http://localhost:8080"
echo ""

# Check if application is running
echo "Checking if application is running..."
if ! curl -s http://localhost:8080/api/v1/labelcategories > /dev/null; then
    echo "❌ Application is not running on http://localhost:8080"
    echo "Please start the application first and then run this script"
    exit 1
fi
echo "✅ Application is running"
echo ""

# Make all scripts executable
echo "Making all scripts executable..."
chmod +x scripts/create-labels-*.sh
echo "✅ Scripts are now executable"
echo ""

# Run simple category scripts first (no dependencies)
echo "Step 1: Creating simple category labels..."
echo "----------------------------------------"

echo "1.1: Creating DIFFICULTY_LEVEL labels..."
./scripts/create-labels-difficulty-level.sh
echo ""

echo "1.2: Creating QUESTION_TYPE labels..."
./scripts/create-labels-question-type.sh
echo ""

echo "1.3: Creating SOURCE labels..."
./scripts/create-labels-source.sh
echo ""

echo "1.4: Creating ENVIRONMENT_ROUND_TYPE labels..."
./scripts/create-labels-environment-round-type.sh
echo ""

echo "1.5: Creating PROGRAMMING_LANGUAGE_TAGS labels..."
./scripts/create-labels-programming-language.sh
echo ""

echo "1.6: Creating FREQUENCY_POPULARITY labels..."
./scripts/create-labels-frequency-popularity.sh
echo ""

echo "Step 1 completed! ✅"
echo ""

# Run complex hierarchical scripts
echo "Step 2: Creating hierarchical category labels..."
echo "----------------------------------------------"

echo "2.1: Creating DATA_STRUCTURE labels (Part 1)..."
./scripts/create-labels-data-structure.sh
echo ""

echo "2.2: Creating DATA_STRUCTURE labels (Part 2)..."
./scripts/create-labels-data-structure-part2.sh
echo ""

echo "2.3: Creating ALGORITHMS labels..."
./scripts/create-labels-algorithms.sh
echo ""

echo "2.4: Creating COMPANY_TAGS labels..."
./scripts/create-labels-company-tags.sh
echo ""

echo "2.5: Creating TOPIC_TAGS labels..."
./scripts/create-labels-topic-tags.sh
echo ""

echo "Step 2 completed! ✅"
echo ""

# Final verification
echo "Step 3: Verification..."
echo "---------------------"
echo "Checking created labels count per category..."

# Get label counts for each category
categories=("CAT-100001" "CAT-100002" "CAT-100003" "CAT-100004" "CAT-100005" "CAT-100006" "CAT-100007" "CAT-100008" "CAT-100009" "CAT-100010")
category_names=("DIFFICULTY_LEVEL" "DATA_STRUCTURES" "ALGORITHMS" "COMPANY_TAGS" "SOURCE" "TOPIC_TAGS" "QUESTION_TYPE" "ENVIRONMENT_ROUND_TYPE" "PROGRAMMING_LANGUAGE_TAGS" "FREQUENCY_POPULARITY")

for i in "${!categories[@]}"; do
    category_id="${categories[$i]}"
    category_name="${category_names[$i]}"
    count=$(curl -s "http://localhost:8080/api/v1/labels?categoryId=$category_id" | grep -o '"id":' | wc -l | tr -d ' ')
    echo "$category_name: $count labels"
done

echo ""
echo "=========================================="
echo "🎉 All Labels Creation Completed!"
echo "=========================================="
echo ""
echo "You can verify the results by visiting:"
echo "- All labels: http://localhost:8080/api/v1/labels"
echo "- Labels by category: http://localhost:8080/api/v1/labels?categoryId=CAT-100001"
echo "- Label hierarchies: http://localhost:8080/api/v1/labels/{labelId}/hierarchy"
echo ""
echo "Next steps:"
echo "1. Create coding questions"
echo "2. Associate questions with labels"
echo "3. Add test cases and solutions"
echo ""