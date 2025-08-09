# System Design API Documentation

## Overview
The System Design API provides comprehensive endpoints for managing system design articles and content, similar to the Articles API but specifically focused on system design topics like scalability, microservices, databases, caching, and more.

## Base URL
```
/api/v1/system-designs
```

## Endpoints

### 1. Get All System Designs
**GET** `/api/v1/system-designs`

Returns a list of all system design articles.

**Response:**
```json
[
  {
    "id": "SYS-12345678",
    "title": "Load Balancing in System Design",
    "shortDescription": "Comprehensive guide to load balancing strategies and implementations",
    "sourceName": "Internal",
    "status": "ACTIVE",
    "uri": "/api/v1/system-designs/load-balancing-system-design",
    "contentUrl": "/api/v1/system-designs/load-balancing-system-design/content",
    "createdAt": "2024-01-15T10:30:00",
    "primaryTags": [
      {
        "id": "LBL-87654321",
        "name": "Load Balancing",
        "description": "Distributing workloads across servers",
        "urlSlug": "load-balancing",
        "categoryCode": "SYSTEM_DESIGN",
        "uri": "/api/v1/labels/load-balancing",
        "questionUri": "/api/v1/system-designs/label/load-balancing"
      }
    ]
  }
]
```

### 2. Get System Design Statistics
**GET** `/api/v1/system-designs/statistics`

Returns comprehensive statistics about system design articles.

**Response:**
```json
{
  "totalSystemDesigns": 25,
  "activeSystemDesigns": 23,
  "draftSystemDesigns": 2,
  "deprecatedSystemDesigns": 0,
  "totalSources": 3,
  "systemDesignsWithSource": 20,
  "systemDesignsWithoutSource": 5,
  "systemDesignsBySource": [
    {
      "sourceName": "Internal",
      "systemDesignCount": 15,
      "uri": "/api/v1/system-designs/source/internal"
    },
    {
      "sourceName": "External Blog",
      "systemDesignCount": 8,
      "uri": "/api/v1/system-designs/source/external-blog"
    }
  ],
  "totalTags": 45,
  "totalUniqueTagsUsed": 32,
  "mostUsedTag": "Scalability",
  "systemDesignsByTag": [
    {
      "labelName": "Scalability",
      "categoryCode": "SYSTEM_DESIGN",
      "systemDesignCount": 8,
      "uri": "/api/v1/system-designs/label/scalability"
    }
  ],
  "totalCategories": 12,
  "systemDesignsByCategory": [
    {
      "categoryId": "CAT-12345",
      "categoryName": "System Design",
      "categoryCode": "SYSTEM_DESIGN",
      "systemDesignCount": 25,
      "uniqueLabelsUsed": 32,
      "topLabels": ["Scalability", "Load Balancing", "Caching", "Database Design", "Microservices"]
    }
  ]
}
```

### 3. Create System Design
**POST** `/api/v1/system-designs`

Creates a new system design article.

**Request Body:**
```json
{
  "title": "Caching Strategies in System Design",
  "shortDescription": "Understanding different caching patterns and implementations",
  "filePath": "system-design/caching/caching-strategies.md",
  "sourceId": "SRC-12345678",
  "originalUrl": "https://example.com/caching-guide",
  "status": "ACTIVE",
  "createdByUserId": "USR-87654321"
}
```

**Response:** `201 Created`
```json
{
  "id": "SYS-98765432",
  "title": "Caching Strategies in System Design",
  "shortDescription": "Understanding different caching patterns and implementations",
  "source": {
    "name": "Internal",
    "questionUri": "/api/v1/sources/internal"
  },
  "originalUrl": "https://example.com/caching-guide",
  "status": "ACTIVE",
  "uri": "/api/v1/system-designs/caching-strategies-system-design",
  "contentUrl": "/api/v1/system-designs/caching-strategies-system-design/content",
  "createdByUser": {
    "id": "USR-87654321",
    "username": "johndoe",
    "email": "john@example.com"
  },
  "version": 1,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "tags": []
}
```

### 4. Get System Design by ID/Slug
**GET** `/api/v1/system-designs/{identifier}`

Retrieves a specific system design by ID or URL slug.

**Parameters:**
- `identifier` - System design ID (SYS-xxxxxxxx) or URL slug

**Response:**
```json
{
  "id": "SYS-12345678",
  "title": "Load Balancing in System Design",
  "shortDescription": "Comprehensive guide to load balancing strategies and implementations",
  "source": {
    "name": "Internal",
    "questionUri": "/api/v1/sources/internal"
  },
  "originalUrl": null,
  "status": "ACTIVE",
  "uri": "/api/v1/system-designs/load-balancing-system-design",
  "contentUrl": "/api/v1/system-designs/load-balancing-system-design/content",
  "createdByUser": {
    "id": "USR-87654321",
    "username": "admin",
    "email": "admin@codyssey.com"
  },
  "version": 1,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "tags": [
    {
      "id": "LBL-87654321",
      "name": "Load Balancing",
      "description": "Distributing workloads across servers",
      "urlSlug": "load-balancing",
      "categoryCode": "SYSTEM_DESIGN",
      "uri": "/api/v1/labels/load-balancing",
      "questionUri": "/api/v1/system-designs/label/load-balancing"
    }
  ]
}
```

### 5. Update System Design
**PUT** `/api/v1/system-designs/{identifier}`

Updates an existing system design article.

**Parameters:**
- `identifier` - System design ID (SYS-xxxxxxxx) or URL slug

**Request Body:**
```json
{
  "title": "Advanced Load Balancing in System Design",
  "shortDescription": "Updated: Comprehensive guide to load balancing strategies, algorithms, and implementations",
  "status": "ACTIVE"
}
```

**Response:**
```json
{
  "id": "SYS-12345678",
  "title": "Advanced Load Balancing in System Design",
  "shortDescription": "Updated: Comprehensive guide to load balancing strategies, algorithms, and implementations",
  // ... rest of the system design object
}
```

### 6. Delete System Design
**DELETE** `/api/v1/system-designs/{identifier}`

Soft deletes a system design article.

**Parameters:**
- `identifier` - System design ID (SYS-xxxxxxxx) or URL slug

**Response:** `204 No Content`

### 7. Get System Design Content
**GET** `/api/v1/system-designs/{identifier}/content`

Retrieves the markdown content of a system design article.

**Parameters:**
- `identifier` - System design ID (SYS-xxxxxxxx) or URL slug

**Response:** `200 OK`
```
Content-Type: text/plain

# Load Balancing in System Design

## Introduction

Load balancing is a critical component in system design that distributes incoming network traffic across multiple servers...

[Full markdown content]
```

### 8. Search System Designs
**GET** `/api/v1/system-designs/search`

Searches system designs by title or description.

**Query Parameters:**
- `searchTerm` (required) - Search term
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 20)
- `sort` (optional) - Sort field (default: createdAt)

**Example:**
```
GET /api/v1/system-designs/search?searchTerm=caching&page=0&size=10
```

### 9. Get System Designs with Pagination
**GET** `/api/v1/system-designs/paginated`

Retrieves system designs with pagination support.

**Query Parameters:**
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 20)
- `sort` (optional) - Sort field (default: createdAt)

### 10. Filter System Designs
**GET** `/api/v1/system-designs/filter`

Advanced filtering of system designs.

**Query Parameters:**
- `sourceIds` (optional) - Comma-separated source IDs
- `labelIds` (optional) - Comma-separated label IDs
- `searchTerm` (optional) - Search term

**Example:**
```
GET /api/v1/system-designs/filter?searchTerm=microservices&sourceIds=SRC-12345,SRC-67890
```

### 11. Get System Designs by Source
**GET** `/api/v1/system-designs/source/{identifier}`

Retrieves system designs from a specific source.

**Parameters:**
- `identifier` - Source ID (SRC-xxxxxxxx) or URL slug

### 12. Get System Designs by Label
**GET** `/api/v1/system-designs/label/{identifier}`

Retrieves system designs tagged with a specific label.

**Parameters:**
- `identifier` - Label ID (LBL-xxxxxxxx) or URL slug

### 13. Check Title Availability
**GET** `/api/v1/system-designs/check-title`

Checks if a system design title is available.

**Query Parameters:**
- `title` (required) - Title to check
- `sourceId` (optional) - Source ID for uniqueness within source

**Response:**
```json
true
```

## Label Management

### 14. Get System Design Labels
**GET** `/api/v1/system-designs/{identifier}/labels`

Retrieves all labels for a specific system design.

### 15. Get Primary System Design Labels
**GET** `/api/v1/system-designs/{identifier}/labels/primary`

Retrieves primary labels for a specific system design.

### 16. Add Label to System Design
**POST** `/api/v1/system-designs/labels`

Associates a label with a system design.

**Request Body:**
```json
{
  "systemDesignId": "SYS-12345678",
  "labelId": "LBL-87654321",
  "isPrimary": true,
  "displayOrder": 1
}
```

### 17. Add Multiple Labels
**POST** `/api/v1/system-designs/labels/bulk`

Associates multiple labels with system designs in bulk.

**Request Body:**
```json
{
  "systemDesignLabels": [
    {
      "systemDesignId": "SYS-12345678",
      "labelId": "LBL-87654321",
      "isPrimary": true,
      "displayOrder": 1
    },
    {
      "systemDesignId": "SYS-12345678",
      "labelId": "LBL-98765432",
      "isPrimary": false,
      "displayOrder": 2
    }
  ]
}
```

### 18. Remove Label from System Design
**DELETE** `/api/v1/system-designs/labels`

Removes a label association from a system design.

**Query Parameters:**
- `systemDesignId` (required) - System design ID
- `labelId` (required) - Label ID

## Status Codes

- `200 OK` - Successful retrieval
- `201 Created` - Successful creation
- `204 No Content` - Successful deletion
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

## Sample System Design Topics

The API includes sample system design articles on:

1. **Load Balancing** - Strategies for distributing traffic across servers
2. **Caching Strategies** - Different caching patterns and implementations
3. **API Gateway** - Microservices architecture patterns
4. **Database Sharding** - Horizontal partitioning for scalability

## Testing

Use the provided test script to validate the API:

```bash
./scripts/test-system-design-api.sh
```

This script tests all major endpoints and creates sample data for demonstration.

## Related APIs

- [Articles API](./01-articles.md) - For general technical articles
- [Labels API](./03-labels.md) - For managing system design tags and categories
- [Sources API](./02-sources.md) - For managing content sources
