#!/bin/bash

# Script to create SYSTEM_DESIGN labels with hierarchical relationships
# This script should be run after the System Design category is created

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"

# First, get the category ID for SYSTEM_DESIGN
echo "Getting SYSTEM_DESIGN category ID..."
CATEGORY_ID=$(curl -s "http://localhost:8080/api/v1/labelcategories" | grep -A 10 -B 10 "SYSTEM_DESIGN" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

if [ -z "$CATEGORY_ID" ]; then
    echo "Error: SYSTEM_DESIGN category not found. Please create the category first."
    exit 1
fi

echo "Using SYSTEM_DESIGN Category ID: $CATEGORY_ID"
echo "============================================================"

# Level 1: Main System Design Categories

echo "Creating Scalability..."
SCALABILITY_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Scalability\",
    \"description\": \"Concepts and patterns for scaling systems\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Scalability ID: $SCALABILITY_ID"

echo "Creating Load Balancing..."
LOAD_BALANCING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Load Balancing\",
    \"description\": \"Distributing incoming requests across multiple servers\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Load Balancing ID: $LOAD_BALANCING_ID"

echo "Creating Caching..."
CACHING_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Caching\",
    \"description\": \"Temporary storage for faster data access\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Caching ID: $CACHING_ID"

echo "Creating Database Design..."
DATABASE_DESIGN_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Database Design\",
    \"description\": \"Database architecture and design patterns\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Database Design ID: $DATABASE_DESIGN_ID"

echo "Creating Microservices Architecture..."
MICROSERVICES_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Microservices Architecture\",
    \"description\": \"Distributed system architecture using small, independent services\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Microservices Architecture ID: $MICROSERVICES_ID"

echo "Creating Message Queues & Event Streaming..."
MESSAGE_QUEUES_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Message Queues & Event Streaming\",
    \"description\": \"Asynchronous communication patterns\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Message Queues & Event Streaming ID: $MESSAGE_QUEUES_ID"

echo "Creating Storage Systems..."
STORAGE_SYSTEMS_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Storage Systems\",
    \"description\": \"Different types of data storage solutions\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Storage Systems ID: $STORAGE_SYSTEMS_ID"

echo "Creating Security..."
SECURITY_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Security\",
    \"description\": \"System security patterns and practices\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Security ID: $SECURITY_ID"

echo "Creating Performance & Monitoring..."
PERFORMANCE_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Performance & Monitoring\",
    \"description\": \"System performance optimization and monitoring\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Performance & Monitoring ID: $PERFORMANCE_ID"

echo "Creating Availability & Reliability..."
AVAILABILITY_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Availability & Reliability\",
    \"description\": \"System uptime and fault tolerance patterns\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Availability & Reliability ID: $AVAILABILITY_ID"

echo "Creating API Design..."
API_DESIGN_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"API Design\",
    \"description\": \"Application Programming Interface design patterns\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "API Design ID: $API_DESIGN_ID"

echo "Creating Real-time Systems..."
REALTIME_ID=$(curl -s -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Real-time Systems\",
    \"description\": \"Systems requiring immediate response and live updates\",
    \"categoryId\": \"$CATEGORY_ID\"
  }" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
echo "Real-time Systems ID: $REALTIME_ID"

echo -e "\n============================================================"
echo "Creating Level 2 Labels (Sub-categories)..."
echo "============================================================"

# Scalability sub-labels
if [ ! -z "$SCALABILITY_ID" ]; then
    echo "Creating Scalability sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Horizontal Scaling\",
        \"description\": \"Adding more servers to handle increased load\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SCALABILITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Vertical Scaling\",
        \"description\": \"Adding more power to existing servers\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SCALABILITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Auto Scaling\",
        \"description\": \"Automatically adjusting resources based on demand\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SCALABILITY_ID\"
      }" > /dev/null
    
    echo "✓ Scalability sub-labels created"
fi

# Load Balancing sub-labels
if [ ! -z "$LOAD_BALANCING_ID" ]; then
    echo "Creating Load Balancing sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Layer 4 Load Balancer\",
        \"description\": \"Load balancing at the transport layer\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$LOAD_BALANCING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Layer 7 Load Balancer\",
        \"description\": \"Load balancing at the application layer\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$LOAD_BALANCING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Round Robin\",
        \"description\": \"Sequential distribution of requests\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$LOAD_BALANCING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Least Connections\",
        \"description\": \"Route to server with fewest active connections\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$LOAD_BALANCING_ID\"
      }" > /dev/null
    
    echo "✓ Load Balancing sub-labels created"
fi

# Caching sub-labels
if [ ! -z "$CACHING_ID" ]; then
    echo "Creating Caching sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"In-Memory Cache\",
        \"description\": \"Cache stored in system memory for fast access\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$CACHING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Distributed Cache\",
        \"description\": \"Cache spread across multiple servers\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$CACHING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"CDN (Content Delivery Network)\",
        \"description\": \"Geographically distributed cache for content delivery\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$CACHING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Cache Aside\",
        \"description\": \"Application manages cache directly\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$CACHING_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Write Through\",
        \"description\": \"Write to cache and database simultaneously\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$CACHING_ID\"
      }" > /dev/null
    
    echo "✓ Caching sub-labels created"
fi

# Database Design sub-labels
if [ ! -z "$DATABASE_DESIGN_ID" ]; then
    echo "Creating Database Design sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"SQL Database\",
        \"description\": \"Relational database management systems\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$DATABASE_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"NoSQL Database\",
        \"description\": \"Non-relational database systems\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$DATABASE_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Database Sharding\",
        \"description\": \"Horizontal partitioning of database\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$DATABASE_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Database Replication\",
        \"description\": \"Copying data across multiple database instances\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$DATABASE_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"CAP Theorem\",
        \"description\": \"Consistency, Availability, Partition tolerance trade-offs\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$DATABASE_DESIGN_ID\"
      }" > /dev/null
    
    echo "✓ Database Design sub-labels created"
fi

# Security sub-labels
if [ ! -z "$SECURITY_ID" ]; then
    echo "Creating Security sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Authentication\",
        \"description\": \"Verifying user identity\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SECURITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Authorization\",
        \"description\": \"Controlling access to resources\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SECURITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"OAuth 2.0\",
        \"description\": \"Authorization framework for third-party access\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SECURITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"JWT (JSON Web Token)\",
        \"description\": \"Compact tokens for secure information transmission\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SECURITY_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Rate Limiting\",
        \"description\": \"Controlling request frequency to prevent abuse\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$SECURITY_ID\"
      }" > /dev/null
    
    echo "✓ Security sub-labels created"
fi

# API Design sub-labels
if [ ! -z "$API_DESIGN_ID" ]; then
    echo "Creating API Design sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"REST API\",
        \"description\": \"Representational State Transfer architectural style\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$API_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"GraphQL\",
        \"description\": \"Query language and runtime for APIs\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$API_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"gRPC\",
        \"description\": \"High-performance RPC framework\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$API_DESIGN_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"API Gateway\",
        \"description\": \"Entry point for API management and routing\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$API_DESIGN_ID\"
      }" > /dev/null
    
    echo "✓ API Design sub-labels created"
fi

# Real-time Systems sub-labels
if [ ! -z "$REALTIME_ID" ]; then
    echo "Creating Real-time Systems sub-labels..."
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"WebSockets\",
        \"description\": \"Full-duplex communication over TCP\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$REALTIME_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Long Polling\",
        \"description\": \"Client holds request open for real-time updates\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$REALTIME_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Server-Sent Events\",
        \"description\": \"Server pushes data to client over HTTP\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$REALTIME_ID\"
      }" > /dev/null
    
    curl -s -X POST "$BASE_URL" \
      -H "$CONTENT_TYPE" \
      -d "{
        \"name\": \"Push Notifications\",
        \"description\": \"Real-time messaging to client devices\",
        \"categoryId\": \"$CATEGORY_ID\",
        \"parentId\": \"$REALTIME_ID\"
      }" > /dev/null
    
    echo "✓ Real-time Systems sub-labels created"
fi

echo -e "\n============================================================"
echo "SYSTEM_DESIGN Labels creation completed!"
echo "============================================================"
echo "Category ID used: $CATEGORY_ID"
echo "You can verify by running: curl http://localhost:8080/api/v1/labels?categoryId=$CATEGORY_ID"
