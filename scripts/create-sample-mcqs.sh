#!/bin/bash

# Script to create sample MCQ questions for testing the navigation feature
# Run this after the application is running and you have created label categories and labels

BASE_URL="http://localhost:8080/api/v1"

# Set content type
CONTENT_TYPE="Content-Type: application/json"

echo "Creating sample MCQ questions for testing..."

# Get authentication token (assuming you have a user account)
# You may need to adjust this based on your authentication setup
# AUTH_TOKEN="Bearer your-jwt-token-here"

# Data Structure MCQs
echo "Creating Data Structure MCQs..."

# Array MCQs
curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What is the time complexity of accessing an element in an array by index?",
    "optionA": "O(1)",
    "optionB": "O(n)",
    "optionC": "O(log n)",
    "optionD": "O(n^2)",
    "correctAnswer": "A",
    "explanation": "Array elements can be accessed directly using their index in constant time."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "Which of the following is NOT a characteristic of arrays?",
    "optionA": "Fixed size",
    "optionB": "Elements stored in contiguous memory",
    "optionC": "Dynamic size",
    "optionD": "Index-based access",
    "correctAnswer": "C",
    "explanation": "Traditional arrays have a fixed size determined at declaration time."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What is the space complexity of an array of size n?",
    "optionA": "O(1)",
    "optionB": "O(n)",
    "optionC": "O(log n)",
    "optionD": "O(n^2)",
    "correctAnswer": "B",
    "explanation": "An array of size n requires O(n) space to store n elements."
  }'

# Tree MCQs
echo "Creating Tree MCQs..."

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "In a binary tree, what is the maximum number of nodes at level h?",
    "optionA": "2^h",
    "optionB": "2^(h-1)",
    "optionC": "h^2",
    "optionD": "2h",
    "correctAnswer": "A",
    "explanation": "At level h (0-indexed), a binary tree can have at most 2^h nodes."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "Which traversal technique is used to copy a tree?",
    "optionA": "Inorder",
    "optionB": "Preorder",
    "optionC": "Postorder",
    "optionD": "Level order",
    "correctAnswer": "B",
    "explanation": "Preorder traversal (root, left, right) is used to copy a tree structure."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What is the time complexity of searching in a balanced BST?",
    "optionA": "O(1)",
    "optionB": "O(n)",
    "optionC": "O(log n)",
    "optionD": "O(n^2)",
    "correctAnswer": "C",
    "explanation": "In a balanced BST, search operation takes O(log n) time."
  }'

# Algorithm MCQs
echo "Creating Algorithm MCQs..."

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "Which sorting algorithm has the best average-case time complexity?",
    "optionA": "Bubble Sort",
    "optionB": "Selection Sort",
    "optionC": "Merge Sort",
    "optionD": "Insertion Sort",
    "correctAnswer": "C",
    "explanation": "Merge Sort has O(n log n) average-case time complexity, which is optimal for comparison-based sorting."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What is the principle behind Dynamic Programming?",
    "optionA": "Divide and Conquer",
    "optionB": "Optimal Substructure and Overlapping Subproblems",
    "optionC": "Greedy Choice",
    "optionD": "Backtracking",
    "correctAnswer": "B",
    "explanation": "Dynamic Programming is based on optimal substructure and overlapping subproblems."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "Which algorithm is used to find the shortest path in a weighted graph?",
    "optionA": "BFS",
    "optionB": "DFS",
    "optionC": "Dijkstra",
    "optionD": "Bubble Sort",
    "correctAnswer": "C",
    "explanation": "Dijkstra algorithm is used to find shortest paths in weighted graphs with non-negative weights."
  }'

# Difficulty Level MCQs
echo "Creating various difficulty level MCQs..."

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What does API stand for?",
    "optionA": "Application Programming Interface",
    "optionB": "Advanced Programming Interface",
    "optionC": "Automated Programming Interface",
    "optionD": "Application Process Interface",
    "correctAnswer": "A",
    "explanation": "API stands for Application Programming Interface."
  }'

curl -X POST "$BASE_URL/mcq" \
  -H "$CONTENT_TYPE" \
  -d '{
    "questionText": "What is the time complexity of the optimal solution for the Longest Common Subsequence problem?",
    "optionA": "O(n)",
    "optionB": "O(n^2)",
    "optionC": "O(n log n)",
    "optionD": "O(2^n)",
    "correctAnswer": "B",
    "explanation": "The LCS problem can be solved using dynamic programming in O(n*m) time where n and m are the lengths of the sequences."
  }'

echo "Sample MCQ questions created successfully!"
echo "You can now test the navigation endpoints and MCQ retrieval."
echo ""
echo "Example API calls to test:"
echo "1. Get all categories: GET $BASE_URL/navigation/categories"
echo "2. Get labels by category: GET $BASE_URL/navigation/categories/{categoryId}/labels"
echo "3. Get MCQs by label: GET $BASE_URL/mcq/labels/{labelId}/questions"
echo "4. Get random MCQs: GET $BASE_URL/mcq/labels/{labelId}/questions/random?count=5"
