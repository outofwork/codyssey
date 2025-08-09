#!/bin/bash

# Script to test Article APIs
BASE_URL="http://localhost:8080/api/v1/articles"

echo "🧪 Testing Article Management APIs"
echo "=================================="
echo ""

# Test 1: Get all articles
echo "1. Testing GET /v1/articles (Get all articles)"
echo "Request: curl -s $BASE_URL"
curl -s "$BASE_URL" | jq '.' || curl -s "$BASE_URL"
echo -e "\n"

# Test 2: Get categories
echo "2. Testing GET /v1/articles/categories (Get available categories)"
echo "Request: curl -s $BASE_URL/categories"
curl -s "$BASE_URL/categories" | jq '.' || curl -s "$BASE_URL/categories"
echo -e "\n"

# Test 3: Get tags
echo "3. Testing GET /v1/articles/tags/all (Get available tags)"
echo "Request: curl -s $BASE_URL/tags/all"
curl -s "$BASE_URL/tags/all" | jq '.' || curl -s "$BASE_URL/tags/all"
echo -e "\n"

# Test 4: Get article by ID
echo "4. Testing GET /v1/articles/{id} (Get article by ID)"
echo "Request: curl -s $BASE_URL/arrays"
curl -s "$BASE_URL/arrays" | jq '.' || curl -s "$BASE_URL/arrays"
echo -e "\n"

# Test 5: Get article by slug
echo "5. Testing GET /v1/articles/slug/{slug} (Get article by slug)"
echo "Request: curl -s $BASE_URL/slug/arrays-data-structure-guide"
curl -s "$BASE_URL/slug/arrays-data-structure-guide" | jq '.' || curl -s "$BASE_URL/slug/arrays-data-structure-guide"
echo -e "\n"

# Test 6: Get articles by category
echo "6. Testing GET /v1/articles/category/{category} (Get articles by category)"
echo "Request: curl -s $BASE_URL/category/data-structures"
curl -s "$BASE_URL/category/data-structures" | jq '.' || curl -s "$BASE_URL/category/data-structures"
echo -e "\n"

# Test 7: Search articles
echo "7. Testing GET /v1/articles/search (Search articles)"
echo "Request: curl -s '$BASE_URL/search?q=array'"
curl -s "$BASE_URL/search?q=array" | jq '.' || curl -s "$BASE_URL/search?q=array"
echo -e "\n"

# Test 8: Get articles by difficulty
echo "8. Testing GET /v1/articles/difficulty/{difficulty} (Get articles by difficulty)"
echo "Request: curl -s $BASE_URL/difficulty/Beginner"
curl -s "$BASE_URL/difficulty/Beginner" | jq '.' || curl -s "$BASE_URL/difficulty/Beginner"
echo -e "\n"

# Test 9: Get related articles
echo "9. Testing GET /v1/articles/{id}/related (Get related articles)"
echo "Request: curl -s $BASE_URL/arrays/related"
curl -s "$BASE_URL/arrays/related" | jq '.' || curl -s "$BASE_URL/arrays/related"
echo -e "\n"

# Test 10: Get articles by tags
echo "10. Testing GET /v1/articles/tags (Get articles by tags)"
echo "Request: curl -s '$BASE_URL/tags?tags=arrays,fundamentals'"
curl -s "$BASE_URL/tags?tags=arrays,fundamentals" | jq '.' || curl -s "$BASE_URL/tags?tags=arrays,fundamentals"
echo -e "\n"

echo "🎉 Article API testing completed!"
echo ""
echo "📋 Available Endpoints Summary:"
echo "  GET    /v1/articles                     - Get all articles"
echo "  GET    /v1/articles/{id}               - Get article by ID"
echo "  GET    /v1/articles/slug/{slug}        - Get article by slug"
echo "  GET    /v1/articles/category/{cat}     - Get articles by category"
echo "  GET    /v1/articles/search?q={query}   - Search articles"
echo "  GET    /v1/articles/tags?tags={tags}   - Get articles by tags"
echo "  GET    /v1/articles/difficulty/{diff}  - Get articles by difficulty"
echo "  GET    /v1/articles/{id}/related       - Get related articles"
echo "  GET    /v1/articles/categories         - Get available categories"
echo "  GET    /v1/articles/tags/all           - Get available tags"
echo "  POST   /v1/articles/refresh-index      - Refresh article index"