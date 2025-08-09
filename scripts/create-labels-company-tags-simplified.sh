#!/bin/bash

# Script to create COMPANY_TAGS labels - Simplified version with main categories
# Category ID: CAT-100004

BASE_URL="http://localhost:8080/api/v1/labels"
CONTENT_TYPE="Content-Type: application/json"
CATEGORY_ID="CAT-100004"

echo "Creating COMPANY_TAGS Labels - Main Categories..."
echo "================================================="

# Level 1: Main Company Categories
echo "Creating FAANG..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"FAANG\",
    \"description\": \"Meta, Apple, Amazon, Netflix, Alphabet (Google)\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Big Tech..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Big Tech\",
    \"description\": \"Major technology companies beyond FAANG\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating FinTech & Banking..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"FinTech & Banking\",
    \"description\": \"Financial technology and banking companies\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Indian Product Companies..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Indian Product Companies\",
    \"description\": \"Indian product-based technology companies\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating HFTs (High-Frequency Trading Firms)..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"HFTs (High-Frequency Trading Firms)\",
    \"description\": \"High-frequency and algorithmic trading firms\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Rideshare & Delivery..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Rideshare & Delivery\",
    \"description\": \"Transportation and delivery service companies\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating Tech Startups / Scaleups..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"Tech Startups / Scaleups\",
    \"description\": \"Growing technology startups and scale-ups\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

echo "Creating IT Service Providers..."
curl -X POST "$BASE_URL" \
  -H "$CONTENT_TYPE" \
  -d "{
    \"name\": \"IT Service Providers\",
    \"description\": \"IT consulting and service companies\",
    \"categoryId\": \"$CATEGORY_ID\"
  }"
echo -e "\n"

# Individual major companies (flat structure for now)
major_companies=(
  "Meta:Social media and metaverse technology company"
  "Apple:Consumer electronics and technology company"
  "Amazon:E-commerce and cloud computing company"
  "Netflix:Streaming entertainment company"
  "Google:Search engine and technology conglomerate"
  "Microsoft:Software and cloud computing company"
  "Adobe:Creative software and digital marketing company"
  "Salesforce:Customer relationship management platform"
  "Oracle:Database and enterprise software company"
  "Cisco:Networking hardware and software company"
  "IBM:Technology and consulting company"
  "LinkedIn:Professional networking platform"
  "Uber:Ride-hailing and delivery platform"
  "Lyft:Ride-sharing service"
  "Goldman Sachs:Investment banking and financial services"
  "JPMorgan Chase:Multinational investment bank"
  "Flipkart:E-commerce marketplace"
  "Swiggy:Food delivery platform"
  "Zomato:Food delivery and restaurant discovery"
  "Paytm:Digital payments and financial services"
  "Jane Street:Quantitative trading firm"
  "Citadel Securities:Market maker and trading firm"
  "Infosys:IT services and consulting"
  "TCS:IT services and business solutions"
  "Wipro:IT services and consulting"
)

echo "Creating individual major companies..."
for item in "${major_companies[@]}"; do
  name="${item%%:*}"
  desc="${item##*:}"
  echo "Creating $name..."
  curl -X POST "$BASE_URL" \
    -H "$CONTENT_TYPE" \
    -d "{
      \"name\": \"$name\",
      \"description\": \"$desc\",
      \"categoryId\": \"$CATEGORY_ID\"
    }"
  echo -e "\n"
done

echo "================================================="
echo "COMPANY_TAGS labels (main categories and major companies) creation completed!"
echo "Note: Individual companies can be organized under parent categories later"