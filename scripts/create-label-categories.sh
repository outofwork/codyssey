#!/bin/bash

# Script to create all label categories
# Run this script to populate the database with predefined label categories

BASE_URL="http://localhost:8080/api/v1/labelcategories"
CONTENT_TYPE="Content-Type: application/json"

echo "Creating Label Categories..."
echo "================================"

# 1. Difficulty Level
echo "Creating Difficulty Level category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Difficulty Level",
    "code": "DIFFICULTY_LEVEL", 
    "description": "Categorizes questions by complexity level, such as Easy, Medium, Hard, Very Hard, and Expert."
  }'
echo -e "\n"

# 2. Data Structures
echo "Creating Data Structures category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Data Structures",
    "code": "DATA_STRUCTURES", 
    "description": "Tags based on the primary data structure involved, like Arrays, Linked Lists, Trees, Graphs, Heaps, etc."
  }'
echo -e "\n"

# 3. Algorithms
echo "Creating Algorithms category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Algorithms",
    "code": "ALGORITHMS", 
    "description": "Tags based on algorithmic approach, such as Sorting, Searching, Dynamic Programming, Backtracking, etc."
  }'
echo -e "\n"

# 4. Company Tags
echo "Creating Company Tags category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Company Tags",
    "code": "COMPANY_TAGS", 
    "description": "Indicates companies that have asked or are known for asking this question in interviews, like Google, Microsoft, Amazon, etc."
  }'
echo -e "\n"

# 5. Source
echo "Creating Source category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Source",
    "code": "SOURCE", 
    "description": "Shows where the question originated or was inspired from, e.g., LeetCode, GeeksforGeeks, Project Euler, HackerRank."
  }'
echo -e "\n"

# 6. Topic Tags
echo "Creating Topic Tags category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Topic Tags",
    "code": "TOPIC_TAGS", 
    "description": "Tags representing key topics or techniques, such as Greedy, Bit Manipulation, Recursion, Sliding Window, etc."
  }'
echo -e "\n"

# 7. Question Type
echo "Creating Question Type category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Question Type",
    "code": "QUESTION_TYPE", 
    "description": "Classifies the type of question: Coding, System Design, Behavioral, or SQL."
  }'
echo -e "\n"

# 8. Environment / Round Type
echo "Creating Environment / Round Type category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Environment / Round Type",
    "code": "ENVIRONMENT_ROUND_TYPE", 
    "description": "Represents the interview stage or platform where the question is asked: Online Assessment, Phone Screen, Onsite Interview."
  }'
echo -e "\n"

# 9. Programming Language Tags
echo "Creating Programming Language Tags category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Programming Language Tags",
    "code": "PROGRAMMING_LANGUAGE_TAGS", 
    "description": "Identifies the language(s) used or required to solve the problem, such as Java, Python, C++, JavaScript, etc."
  }'
echo -e "\n"

# 10. Frequency / Popularity
echo "Creating Frequency / Popularity category..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d '{
    "name": "Frequency / Popularity",
    "code": "FREQUENCY_POPULARITY", 
    "description": "Indicates how frequently a question is asked or its signal strength, like Most Frequently Asked, High Signal, Rarely Asked."
  }'
echo -e "\n"

echo "================================"
echo "Label Categories creation completed!"
echo "You can verify by running: curl http://localhost:8080/api/v1/labelcategories"