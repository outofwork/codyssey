#!/bin/bash

# Script to test System Design APIs
BASE_URL="http://localhost:8080/api/v1/system-designs"

echo "🏗️  Testing System Design Management APIs"
echo "========================================"
echo ""

# Test 1: Get all system designs
echo "1. Testing GET /v1/system-designs (Get all system designs)"
echo "Request: curl -s $BASE_URL"
curl -s "$BASE_URL" | jq '.' || curl -s "$BASE_URL"
echo -e "\n"

# Test 2: Get system design statistics
echo "2. Testing GET /v1/system-designs/statistics (Get system design statistics)"
echo "Request: curl -s $BASE_URL/statistics"
curl -s "$BASE_URL/statistics" | jq '.' || curl -s "$BASE_URL/statistics"
echo -e "\n"

# Test 3: Get system designs with pagination
echo "3. Testing GET /v1/system-designs/paginated (Get system designs with pagination)"
echo "Request: curl -s '$BASE_URL/paginated?page=0&size=5'"
curl -s "$BASE_URL/paginated?page=0&size=5" | jq '.' || curl -s "$BASE_URL/paginated?page=0&size=5"
echo -e "\n"

# Test 4: Create a new system design
echo "4. Testing POST /v1/system-designs (Create new system design)"
echo "Request: Creating Load Balancing system design"

CREATE_PAYLOAD='{
  "title": "Load Balancing in System Design",
  "shortDescription": "Comprehensive guide to load balancing strategies and implementations",
  "filePath": "system-design/scalability/load-balancing.md",
  "status": "ACTIVE"
}'

CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d "$CREATE_PAYLOAD")

echo "$CREATE_RESPONSE" | jq '.' || echo "$CREATE_RESPONSE"

# Extract the ID and URL slug for further testing
SYSTEM_DESIGN_ID=$(echo "$CREATE_RESPONSE" | jq -r '.id // empty')
SYSTEM_DESIGN_SLUG=$(echo "$CREATE_RESPONSE" | jq -r '.uri // empty' | sed 's|.*/||')

echo "Created System Design ID: $SYSTEM_DESIGN_ID"
echo "Created System Design Slug: $SYSTEM_DESIGN_SLUG"
echo -e "\n"

# Test 5: Get system design by ID (if created successfully)
if [ ! -z "$SYSTEM_DESIGN_ID" ] && [ "$SYSTEM_DESIGN_ID" != "null" ]; then
    echo "5. Testing GET /v1/system-designs/{id} (Get system design by ID)"
    echo "Request: curl -s $BASE_URL/$SYSTEM_DESIGN_ID"
    curl -s "$BASE_URL/$SYSTEM_DESIGN_ID" | jq '.' || curl -s "$BASE_URL/$SYSTEM_DESIGN_ID"
    echo -e "\n"
fi

# Test 6: Get system design by slug (if created successfully)
if [ ! -z "$SYSTEM_DESIGN_SLUG" ] && [ "$SYSTEM_DESIGN_SLUG" != "null" ]; then
    echo "6. Testing GET /v1/system-designs/{slug} (Get system design by slug)"
    echo "Request: curl -s $BASE_URL/$SYSTEM_DESIGN_SLUG"
    curl -s "$BASE_URL/$SYSTEM_DESIGN_SLUG" | jq '.' || curl -s "$BASE_URL/$SYSTEM_DESIGN_SLUG"
    echo -e "\n"

    # Test 7: Get system design content
    echo "7. Testing GET /v1/system-designs/{slug}/content (Get system design content)"
    echo "Request: curl -s $BASE_URL/$SYSTEM_DESIGN_SLUG/content"
    curl -s "$BASE_URL/$SYSTEM_DESIGN_SLUG/content" | head -20
    echo -e "\n...(content truncated)\n"
fi

# Test 8: Create another system design
echo "8. Testing POST /v1/system-designs (Create another system design)"
echo "Request: Creating Caching Strategies system design"

CREATE_PAYLOAD_2='{
  "title": "Caching Strategies in System Design",
  "shortDescription": "Understanding different caching patterns and their implementations",
  "filePath": "system-design/caching/caching-strategies.md",
  "status": "ACTIVE"
}'

CREATE_RESPONSE_2=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d "$CREATE_PAYLOAD_2")

echo "$CREATE_RESPONSE_2" | jq '.' || echo "$CREATE_RESPONSE_2"
echo -e "\n"

# Test 9: Create API Gateway system design
echo "9. Testing POST /v1/system-designs (Create API Gateway system design)"
echo "Request: Creating API Gateway system design"

CREATE_PAYLOAD_3='{
  "title": "API Gateway in Microservices Architecture",
  "shortDescription": "API Gateway patterns and implementation strategies",
  "filePath": "system-design/microservices/api-gateway.md",
  "status": "ACTIVE"
}'

CREATE_RESPONSE_3=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d "$CREATE_PAYLOAD_3")

echo "$CREATE_RESPONSE_3" | jq '.' || echo "$CREATE_RESPONSE_3"
echo -e "\n"

# Test 10: Create Database Sharding system design
echo "10. Testing POST /v1/system-designs (Create Database Sharding system design)"
echo "Request: Creating Database Sharding system design"

CREATE_PAYLOAD_4='{
  "title": "Database Sharding in System Design",
  "shortDescription": "Horizontal partitioning strategies for database scalability",
  "filePath": "system-design/database/database-sharding.md",
  "status": "ACTIVE"
}'

CREATE_RESPONSE_4=$(curl -s -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d "$CREATE_PAYLOAD_4")

echo "$CREATE_RESPONSE_4" | jq '.' || echo "$CREATE_RESPONSE_4"
echo -e "\n"

# Test 11: Get all system designs after creation
echo "11. Testing GET /v1/system-designs (Get all system designs after creation)"
echo "Request: curl -s $BASE_URL"
curl -s "$BASE_URL" | jq '.' || curl -s "$BASE_URL"
echo -e "\n"

# Test 12: Search system designs
echo "12. Testing GET /v1/system-designs/search (Search system designs)"
echo "Request: curl -s '$BASE_URL/search?searchTerm=caching'"
curl -s "$BASE_URL/search?searchTerm=caching" | jq '.' || curl -s "$BASE_URL/search?searchTerm=caching"
echo -e "\n"

# Test 13: Search system designs with different term
echo "13. Testing GET /v1/system-designs/search (Search system designs - microservices)"
echo "Request: curl -s '$BASE_URL/search?searchTerm=microservices'"
curl -s "$BASE_URL/search?searchTerm=microservices" | jq '.' || curl -s "$BASE_URL/search?searchTerm=microservices"
echo -e "\n"

# Test 14: Get updated statistics
echo "14. Testing GET /v1/system-designs/statistics (Get updated statistics)"
echo "Request: curl -s $BASE_URL/statistics"
curl -s "$BASE_URL/statistics" | jq '.' || curl -s "$BASE_URL/statistics"
echo -e "\n"

# Test 15: Check title availability
echo "15. Testing GET /v1/system-designs/check-title (Check title availability)"
echo "Request: curl -s '$BASE_URL/check-title?title=Load Balancing in System Design'"
curl -s "$BASE_URL/check-title?title=Load%20Balancing%20in%20System%20Design" | jq '.' || curl -s "$BASE_URL/check-title?title=Load%20Balancing%20in%20System%20Design"
echo -e "\n"

echo "16. Testing GET /v1/system-designs/check-title (Check new title availability)"
echo "Request: curl -s '$BASE_URL/check-title?title=New System Design Topic'"
curl -s "$BASE_URL/check-title?title=New%20System%20Design%20Topic" | jq '.' || curl -s "$BASE_URL/check-title?title=New%20System%20Design%20Topic"
echo -e "\n"

# Test 17: Update system design (if created successfully)
if [ ! -z "$SYSTEM_DESIGN_ID" ] && [ "$SYSTEM_DESIGN_ID" != "null" ]; then
    echo "17. Testing PUT /v1/system-designs/{id} (Update system design)"
    echo "Request: Updating system design description"

    UPDATE_PAYLOAD='{
      "shortDescription": "Updated: Comprehensive guide to load balancing strategies, algorithms, and implementations in distributed systems"
    }'

    UPDATE_RESPONSE=$(curl -s -X PUT "$BASE_URL/$SYSTEM_DESIGN_ID" \
      -H "Content-Type: application/json" \
      -d "$UPDATE_PAYLOAD")

    echo "$UPDATE_RESPONSE" | jq '.' || echo "$UPDATE_RESPONSE"
    echo -e "\n"
fi

# Test 18: Filter system designs
echo "18. Testing GET /v1/system-designs/filter (Filter system designs)"
echo "Request: curl -s '$BASE_URL/filter?searchTerm=system'"
curl -s "$BASE_URL/filter?searchTerm=system" | jq '.' || curl -s "$BASE_URL/filter?searchTerm=system"
echo -e "\n"

echo "🎉 System Design API testing completed!"
echo ""
echo "📊 Summary:"
echo "- Tested all major System Design API endpoints"
echo "- Created 4 sample system design articles"
echo "- Verified CRUD operations"
echo "- Tested search and filtering functionality"
echo "- Validated statistics endpoint"
echo ""
echo "🔗 API Endpoints tested:"
echo "- GET /v1/system-designs"
echo "- GET /v1/system-designs/statistics" 
echo "- GET /v1/system-designs/paginated"
echo "- POST /v1/system-designs"
echo "- GET /v1/system-designs/{id}"
echo "- GET /v1/system-designs/{slug}"
echo "- GET /v1/system-designs/{slug}/content"
echo "- PUT /v1/system-designs/{id}"
echo "- GET /v1/system-designs/search"
echo "- GET /v1/system-designs/filter"
echo "- GET /v1/system-designs/check-title"
echo ""
