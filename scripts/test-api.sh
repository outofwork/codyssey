#!/bin/bash

# Test Script for Creating "Two Sum" Question with Complete Data
# This script demonstrates all the APIs we built

BASE_URL="http://localhost:8080/api/v1"

echo "🚀 Testing Codyssey Coding Questions API"
echo "========================================"

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 30

# Check if application is running
echo "🔍 Checking if application is running..."
curl -f "$BASE_URL/questions" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ Application is not running. Please start with: mvn spring-boot:run"
    exit 1
fi
echo "✅ Application is running!"

echo ""
echo "📋 Step 1: Create Label Categories"
echo "=================================="

# Create Difficulty Category
echo "Creating DIFFICULTY category..."
curl -X POST "$BASE_URL/label-categories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Difficulty Levels",
    "code": "DIFFICULTY",
    "description": "Question difficulty classifications"
  }' | jq .

# Create Topic Category
echo "Creating TOPIC category..."
curl -X POST "$BASE_URL/label-categories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Topics",
    "code": "TOPIC",
    "description": "Algorithm and data structure topics"
  }' | jq .

# Create Company Category
echo "Creating COMPANY category..."
curl -X POST "$BASE_URL/label-categories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Companies",
    "code": "COMPANY",
    "description": "Companies that ask these questions"
  }' | jq .

echo ""
echo "🏷️ Step 2: Create Labels"
echo "======================="

# Get category IDs (we'll use hardcoded ones for this demo)
DIFFICULTY_CATEGORY_ID="CAT-100001"
TOPIC_CATEGORY_ID="CAT-100002"
COMPANY_CATEGORY_ID="CAT-100003"

# Create Difficulty Labels
echo "Creating difficulty labels..."
curl -X POST "$BASE_URL/labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "labels": [
      {
        "name": "Easy",
        "description": "Beginner level questions",
        "categoryId": "'"$DIFFICULTY_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Medium", 
        "description": "Intermediate level questions",
        "categoryId": "'"$DIFFICULTY_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Hard",
        "description": "Advanced level questions",
        "categoryId": "'"$DIFFICULTY_CATEGORY_ID"'",
        "active": true
      }
    ]
  }' | jq .

# Create Topic Labels
echo "Creating topic labels..."
curl -X POST "$BASE_URL/labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "labels": [
      {
        "name": "Array",
        "description": "Array data structure problems",
        "categoryId": "'"$TOPIC_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Hash Table",
        "description": "Hash table/map problems",
        "categoryId": "'"$TOPIC_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Two Pointers",
        "description": "Two pointers technique",
        "categoryId": "'"$TOPIC_CATEGORY_ID"'",
        "active": true
      }
    ]
  }' | jq .

# Create Company Labels
echo "Creating company labels..."
curl -X POST "$BASE_URL/labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "labels": [
      {
        "name": "Meta",
        "description": "Meta (Facebook) interview questions",
        "categoryId": "'"$COMPANY_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Amazon",
        "description": "Amazon interview questions", 
        "categoryId": "'"$COMPANY_CATEGORY_ID"'",
        "active": true
      },
      {
        "name": "Google",
        "description": "Google interview questions",
        "categoryId": "'"$COMPANY_CATEGORY_ID"'",
        "active": true
      }
    ]
  }' | jq .

echo ""
echo "❓ Step 3: Create the 'Two Sum' Question"
echo "======================================="

# Create the main question
QUESTION_RESPONSE=$(curl -s -X POST "$BASE_URL/questions" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Two Sum",
    "shortDescription": "Find two numbers in an array that add up to a target sum",
    "description": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nYou can return the answer in any order.",
    "difficultyLabelId": "LBL-100001",
    "platformSource": "LeetCode",
    "platformQuestionId": "1",
    "inputFormat": "nums: List[int], target: int",
    "outputFormat": "List[int] - indices of two numbers",
    "constraintsText": "• 2 ≤ nums.length ≤ 10^4\n• -10^9 ≤ nums[i] ≤ 10^9\n• -10^9 ≤ target ≤ 10^9\n• Only one valid answer exists",
    "timeComplexityHint": "O(n)",
    "spaceComplexityHint": "O(n)",
    "status": "ACTIVE"
  }')

echo "$QUESTION_RESPONSE" | jq .
QUESTION_ID=$(echo "$QUESTION_RESPONSE" | jq -r '.id')
echo "Created question with ID: $QUESTION_ID"

echo ""
echo "💻 Step 4: Add Solutions (Multiple Languages)"
echo "============================================"

# Add solutions in bulk
curl -X POST "$BASE_URL/solutions/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "solutions": [
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 1,
        "language": "Java",
        "solutionCode": "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        throw new IllegalArgumentException(\"No two sum solution\");\n    }\n}",
        "explanation": "Use a hash map to store numbers and their indices. For each number, check if its complement exists in the map.",
        "timeComplexity": "O(n)",
        "spaceComplexity": "O(n)",
        "isOptimal": true
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 2,
        "language": "Python",
        "solutionCode": "class Solution:\n    def twoSum(self, nums: List[int], target: int) -> List[int]:\n        seen = {}\n        for i, num in enumerate(nums):\n            complement = target - num\n            if complement in seen:\n                return [seen[complement], i]\n            seen[num] = i\n        return []",
        "explanation": "Python implementation using dictionary for O(1) lookups",
        "timeComplexity": "O(n)",
        "spaceComplexity": "O(n)",
        "isOptimal": true
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 3,
        "language": "C++",
        "solutionCode": "class Solution {\npublic:\n    vector<int> twoSum(vector<int>& nums, int target) {\n        unordered_map<int, int> map;\n        for (int i = 0; i < nums.size(); i++) {\n            int complement = target - nums[i];\n            if (map.find(complement) != map.end()) {\n                return {map[complement], i};\n            }\n            map[nums[i]] = i;\n        }\n        return {};\n    }\n};",
        "explanation": "C++ implementation using unordered_map for optimal performance",
        "timeComplexity": "O(n)",
        "spaceComplexity": "O(n)",
        "isOptimal": true
      }
    ]
  }' | jq .

echo ""
echo "🏷️ Step 5: Add Tags/Labels to Question"
echo "======================================"

# Add topic labels
curl -X POST "$BASE_URL/question-labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "questionLabels": [
      {
        "questionId": "'"$QUESTION_ID"'",
        "labelId": "LBL-100004"
      },
      {
        "questionId": "'"$QUESTION_ID"'", 
        "labelId": "LBL-100005"
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "labelId": "LBL-100006"
      }
    ]
  }' | jq .

echo ""
echo "🏢 Step 6: Add Company Associations"
echo "=================================="

# Add company associations
curl -X POST "$BASE_URL/question-companies/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "questionCompanies": [
      {
        "questionId": "'"$QUESTION_ID"'",
        "companyLabelId": "LBL-100007",
        "frequency": 5,
        "lastAskedYear": 2024
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "companyLabelId": "LBL-100008", 
        "frequency": 3,
        "lastAskedYear": 2023
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "companyLabelId": "LBL-100009",
        "frequency": 4,
        "lastAskedYear": 2024
      }
    ]
  }' | jq .

echo ""
echo "🧪 Step 7: Add Test Cases"
echo "========================"

# Add test cases
curl -X POST "$BASE_URL/test-cases/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "testCases": [
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 1,
        "inputData": "nums = [2,7,11,15], target = 9",
        "expectedOutput": "[0,1]",
        "isSample": true,
        "explanation": "Because nums[0] + nums[1] == 9, we return [0, 1]"
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 2,
        "inputData": "nums = [3,2,4], target = 6", 
        "expectedOutput": "[1,2]",
        "isSample": true,
        "explanation": "Because nums[1] + nums[2] == 6, we return [1, 2]"
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 3,
        "inputData": "nums = [3,3], target = 6",
        "expectedOutput": "[0,1]",
        "isSample": false,
        "explanation": "Hidden test case for edge case with duplicate numbers"
      },
      {
        "questionId": "'"$QUESTION_ID"'",
        "sequence": 4,
        "inputData": "nums = [-1,-2,-3,-4,-5], target = -8",
        "expectedOutput": "[2,4]", 
        "isSample": false,
        "explanation": "Hidden test case with negative numbers"
      }
    ]
  }' | jq .

echo ""
echo "📊 Step 8: View Complete Question"
echo "==============================="

# Get the complete question with all details
echo "Getting complete question details..."
curl -s "$BASE_URL/questions/$QUESTION_ID" | jq .

echo ""
echo "📈 Step 9: Get Question Statistics"
echo "================================="

# Get question statistics
curl -s "$BASE_URL/questions/$QUESTION_ID/statistics" | jq .

echo ""
echo "🔍 Step 10: Test Search and Filtering"
echo "===================================="

# Search questions
echo "Searching for 'two sum'..."
curl -s "$BASE_URL/questions/search?q=two+sum" | jq .

# Get questions by difficulty
echo "Getting Easy questions..."
curl -s "$BASE_URL/questions/difficulty/LBL-100001" | jq .

# Get questions by company
echo "Getting Meta questions..."
curl -s "$BASE_URL/questions/company/LBL-100007" | jq .

echo ""
echo "✅ API Testing Complete!"
echo "======================="
echo "🎉 Successfully created 'Two Sum' question with:"
echo "   • Multiple language solutions (Java, Python, C++)"
echo "   • Topic tags (Array, Hash Table, Two Pointers)"
echo "   • Company associations (Meta, Amazon, Google)"
echo "   • Test cases (2 sample + 2 hidden)"
echo "   • Complete metadata and constraints"
echo ""
echo "🔗 You can now access:"
echo "   • Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   • Question: $BASE_URL/questions/$QUESTION_ID"
echo "   • Solutions: $BASE_URL/solutions/question/$QUESTION_ID"
echo "   • Test Cases: $BASE_URL/test-cases/question/$QUESTION_ID"