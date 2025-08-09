#!/bin/bash

# Article Management API Examples
BASE_URL="http://localhost:8080/api/v1/articles"

echo "📚 Article Management API Examples"
echo "=================================="
echo ""

# 1. Get all articles (summary)
echo "1. Get All Articles:"
echo "   curl -X GET $BASE_URL"
echo ""

# 2. Get specific article by ID
echo "2. Get Article by ID:"
echo "   curl -X GET $BASE_URL/arrays"
echo ""

# 3. Get article by slug (SEO-friendly)
echo "3. Get Article by Slug:"
echo "   curl -X GET $BASE_URL/slug/arrays-data-structure-guide"
echo ""

# 4. Get articles by category
echo "4. Get Articles by Category:"
echo "   curl -X GET $BASE_URL/category/data-structures"
echo "   curl -X GET $BASE_URL/category/algorithms"
echo ""

# 5. Search articles
echo "5. Search Articles:"
echo "   curl -X GET '$BASE_URL/search?q=array'"
echo "   curl -X GET '$BASE_URL/search?q=search'"
echo ""

# 6. Get articles by tags
echo "6. Get Articles by Tags:"
echo "   curl -X GET '$BASE_URL/tags?tags=arrays,fundamentals'"
echo "   curl -X GET '$BASE_URL/tags?tags=searching,algorithms'"
echo ""

# 7. Get articles by difficulty
echo "7. Get Articles by Difficulty:"
echo "   curl -X GET $BASE_URL/difficulty/Beginner"
echo "   curl -X GET $BASE_URL/difficulty/Intermediate"
echo ""

# 8. Get related articles
echo "8. Get Related Articles:"
echo "   curl -X GET $BASE_URL/arrays/related"
echo ""

# 9. Get available categories
echo "9. Get Available Categories:"
echo "   curl -X GET $BASE_URL/categories"
echo ""

# 10. Get available tags
echo "10. Get Available Tags:"
echo "    curl -X GET $BASE_URL/tags/all"
echo ""

# 11. Refresh index
echo "11. Refresh Article Index:"
echo "    curl -X POST $BASE_URL/refresh-index"
echo ""

echo "🎯 Frontend Integration Examples:"
echo "================================="
echo ""
echo "// JavaScript/React examples"
echo ""
echo "// Get all articles for homepage"
echo "const articles = await fetch('/api/v1/articles').then(r => r.json());"
echo ""
echo "// Get specific article for article page"
echo "const article = await fetch('/api/v1/articles/arrays').then(r => r.json());"
echo ""
echo "// Search functionality"
echo "const searchResults = await fetch('/api/v1/articles/search?q=\${query}').then(r => r.json());"
echo ""
echo "// Category page"
echo "const dsArticles = await fetch('/api/v1/articles/category/data-structures').then(r => r.json());"
echo ""
echo "// Filter by difficulty"
echo "const beginnerArticles = await fetch('/api/v1/articles/difficulty/Beginner').then(r => r.json());"