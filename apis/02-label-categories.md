# 🏷️ Label Categories APIs

Complete documentation for label category management endpoints. Label categories serve as top-level organizational structures for hierarchical labels.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Path** | `/api/v1/labelcategories` |
| **Total Endpoints** | 7 |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |

---

## 📚 API Endpoints

### 1. 🆕 Create Label Category
Creates a new label category for organizing labels.

**`POST /api/v1/labelcategories`**

#### Request Body
```json
{
  "name": "Programming Languages",
  "code": "PROGRAMMING_LANGUAGE",
  "description": "Categories for different programming languages used in coding questions",
  "active": true
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `name` | ✅ | Max 100 characters |
| `code` | ✅ | Max 50 chars, uppercase letters/numbers/underscores only |
| `description` | ❌ | Max 500 characters |
| `active` | ❌ | Boolean, defaults to `true` |

#### Response (201 Created)
```json
{
  "id": "CAT-100001",
  "name": "Programming Languages",
  "code": "PROGRAMMING_LANGUAGE",
  "description": "Categories for different programming languages used in coding questions",
  "active": true,
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **409 Conflict**: Code already exists

---

### 2. 📋 Get All Label Categories
Retrieves all label categories in the system.

**`GET /api/v1/labelcategories`**

#### Response (200 OK)
```json
[
  {
    "id": "CAT-100001",
    "name": "Programming Languages",
    "code": "PROGRAMMING_LANGUAGE",
    "description": "Categories for different programming languages",
    "active": true,
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z"
  },
  {
    "id": "CAT-100002",
    "name": "Difficulty Levels",
    "code": "DIFFICULTY_LEVEL",
    "description": "Question difficulty categories",
    "active": true,
    "createdAt": "2025-01-06T12:01:00Z",
    "updatedAt": "2025-01-06T12:01:00Z"
  }
]
```

---

### 3. 🔍 Get Label Category by ID
Retrieves a specific label category by its unique ID.

**`GET /api/v1/labelcategories/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Label Category ID (e.g., "CAT-100001") |

#### Response (200 OK)
```json
{
  "id": "CAT-100001",
  "name": "Programming Languages",
  "code": "PROGRAMMING_LANGUAGE",
  "description": "Categories for different programming languages",
  "active": true,
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

#### Error Responses
- **404 Not Found**: Label category not found

---

### 4. ✏️ Update Label Category
Updates an existing label category.

**`PUT /api/v1/labelcategories/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Label Category ID to update |

#### Request Body
```json
{
  "name": "Programming Languages & Frameworks",
  "code": "PROGRAMMING_LANGUAGE", 
  "description": "Updated description for programming languages and frameworks",
  "active": true
}
```

#### Response (200 OK)
```json
{
  "id": "CAT-100001",
  "name": "Programming Languages & Frameworks",
  "code": "PROGRAMMING_LANGUAGE",
  "description": "Updated description for programming languages and frameworks",
  "active": true,
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:30:00Z"
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **404 Not Found**: Label category not found
- **409 Conflict**: Code already exists (if changing code)

---

### 5. 🗑️ Delete Label Category
Soft deletes a label category (marks as inactive).

**`DELETE /api/v1/labelcategories/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Label Category ID to delete |

#### Response (204 No Content)
No response body.

#### Error Responses
- **404 Not Found**: Label category not found
- **409 Conflict**: Cannot delete category with active labels

---

### 6. 🔍 Search Label Categories
Searches label categories by name or description.

**`GET /api/v1/labelcategories/search`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `query` | string | ✅ | Search term for name or description |
| `activeOnly` | boolean | ❌ | Filter to active categories only |

#### Example Request
```
GET /api/v1/labelcategories/search?query=programming&activeOnly=true
```

#### Response (200 OK)
```json
[
  {
    "id": "CAT-100001",
    "name": "Programming Languages",
    "code": "PROGRAMMING_LANGUAGE",
    "description": "Categories for different programming languages",
    "active": true,
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z"
  }
]
```

---

### 7. ✅ Check Code Availability
Verifies if a category code is available for use.

**`GET /api/v1/labelcategories/check-code`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `code` | string | ✅ | Code to check availability |
| `excludeId` | string | ❌ | Exclude this category ID from check (for updates) |

#### Example Request
```
GET /api/v1/labelcategories/check-code?code=NEW_CATEGORY
```

#### Response (200 OK)
```json
{
  "available": true,
  "code": "NEW_CATEGORY"
}
```

#### Alternative Response (Code Taken)
```json
{
  "available": false,
  "code": "EXISTING_CODE"
}
```

---

## 🔧 Common Error Responses

### Validation Error (400)
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "code",
        "message": "Code must contain only uppercase letters, numbers, and underscores"
      }
    ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Code Conflict (409)
```json
{
  "error": {
    "code": "DUPLICATE_RESOURCE",
    "message": "Label category code 'PROGRAMMING_LANGUAGE' already exists",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Cannot Delete (409)
```json
{
  "error": {
    "code": "CONSTRAINT_VIOLATION",
    "message": "Cannot delete label category with active labels. Please delete or move labels first.",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Complete Category Management Flow
```bash
# 1. Check code availability
curl -X GET "http://localhost:8080/api/v1/labelcategories/check-code?code=DATA_STRUCTURE"

# 2. Create new category
curl -X POST "http://localhost:8080/api/v1/labelcategories" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Data Structures",
    "code": "DATA_STRUCTURE",
    "description": "Categories for different data structure types",
    "active": true
  }'

# 3. Search categories
curl -X GET "http://localhost:8080/api/v1/labelcategories/search?query=data&activeOnly=true"

# 4. Update category
curl -X PUT "http://localhost:8080/api/v1/labelcategories/CAT-100001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Data Structures & Algorithms",
    "code": "DATA_STRUCTURE",
    "description": "Updated description",
    "active": true
  }'
```

---

## 📄 Common Category Codes

Here are some suggested category codes for typical coding question platforms:

| Category | Code | Description |
|----------|------|-------------|
| **Algorithms** | `ALGORITHMS` | Algorithm-based question types |
| **Data Structures** | `DATA_STRUCTURE` | Array, tree, graph, etc. |
| **Difficulty** | `DIFFICULTY_LEVEL` | Easy, Medium, Hard |
| **Programming Languages** | `PROGRAMMING_LANGUAGE` | Java, Python, C++, etc. |
| **Companies** | `COMPANY_TAGS` | Google, Microsoft, Amazon, etc. |
| **Topics** | `TOPIC_TAGS` | Dynamic Programming, Sorting, etc. |
| **Question Types** | `QUESTION_TYPE` | Coding, System Design, etc. |
| **Frequency** | `FREQUENCY_POPULARITY` | High, Medium, Low frequency |

---

**Last Updated**: January 6, 2025