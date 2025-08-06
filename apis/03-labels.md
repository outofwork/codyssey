# 🏷️ Labels APIs

Complete documentation for hierarchical label management endpoints. Labels are organized within categories and support parent-child relationships.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Path** | `/api/v1/labels` |
| **Total Endpoints** | 15 |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |
| **Features** | Hierarchical structure, bulk operations, category organization |

---

## 📚 API Endpoints

### 1. 🆕 Create Label
Creates a new label within a specific category, optionally with a parent label.

**`POST /api/v1/labels`**

#### Request Body
```json
{
  "name": "Java",
  "description": "Java programming language questions",
  "categoryId": "CAT-100001",
  "parentId": null,
  "active": true
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `name` | ✅ | Max 100 characters, unique within category/parent context |
| `description` | ❌ | Max 500 characters |
| `categoryId` | ✅ | Must be valid label category ID |
| `parentId` | ❌ | Parent label ID (must be in same category) |
| `active` | ❌ | Boolean, defaults to `true` |

#### Response (201 Created)
```json
{
  "id": "LBL-100001",
  "name": "Java",
  "description": "Java programming language questions",
  "active": true,
  "category": {
    "id": "LBC_1a2b3c4d",
    "name": "Programming Languages",
    "code": "PROGRAMMING_LANGUAGE"
  },
  "parent": null,
  "children": [],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

---

### 2. 📦 Create Multiple Labels (Bulk)
Creates multiple labels in a single operation.

**`POST /api/v1/labels/bulk`**

#### Request Body
```json
{
  "labels": [
    {
      "name": "Easy",
      "description": "Easy difficulty questions",
      "categoryId": "LBC_2e3f4g5h",
      "parentId": null,
      "active": true
    },
    {
      "name": "Medium",
      "description": "Medium difficulty questions",
      "categoryId": "LBC_2e3f4g5h",
      "parentId": null,
      "active": true
    },
    {
      "name": "Hard",
      "description": "Hard difficulty questions",
      "categoryId": "LBC_2e3f4g5h",
      "parentId": null,
      "active": true
    }
  ]
}
```

#### Response (201 Created)
```json
{
  "createdLabels": [
    {
      "id": "LBL_2e3f4g5h",
      "name": "Easy",
      "description": "Easy difficulty questions",
      "active": true,
      "category": {
        "id": "LBC_2e3f4g5h",
        "name": "Difficulty Levels",
        "code": "DIFFICULTY_LEVEL"
      },
      "parent": null,
      "children": [],
      "createdAt": "2025-01-06T12:00:00Z",
      "updatedAt": "2025-01-06T12:00:00Z"
    }
  ],
  "totalCreated": 3
}
```

---

### 3. 🔍 Get Label by ID
Retrieves a specific label with its hierarchy information.

**`GET /api/v1/labels/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Label ID (e.g., "LBL-100001") |

#### Response (200 OK)
```json
{
  "id": "LBL-100001",
  "name": "Java",
  "description": "Java programming language questions",
  "active": true,
  "category": {
    "id": "LBC_1a2b3c4d",
    "name": "Programming Languages",
    "code": "PROGRAMMING_LANGUAGE"
  },
  "parent": null,
  "children": [
    {
      "id": "LBL_child1",
      "name": "Spring Boot",
      "active": true
    }
  ],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

---

### 4. ✏️ Update Label
Updates an existing label's information.

**`PUT /api/v1/labels/{id}`**

#### Request Body
```json
{
  "name": "Java Programming",
  "description": "Updated description for Java programming language questions",
  "active": true
}
```

#### Response (200 OK)
Same structure as Get Label by ID with updated information.

---

### 5. 🗑️ Delete Label
Soft deletes a label (marks as inactive).

**`DELETE /api/v1/labels/{id}`**

#### Response (204 No Content)
No response body.

#### Error Responses
- **404 Not Found**: Label not found
- **409 Conflict**: Cannot delete label with active children

---

### 6. 📋 Get All Labels
Retrieves all labels with optional filtering.

**`GET /api/v1/labels`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `activeOnly` | boolean | ❌ | Filter to active labels only |
| `page` | integer | ❌ | Page number (0-based) |
| `size` | integer | ❌ | Page size |

#### Response (200 OK)
```json
[
  {
    "id": "LBL-100001",
    "name": "Java",
    "description": "Java programming language questions",
    "active": true,
    "category": {
      "id": "CAT-100001",
      "name": "Programming Languages",
      "code": "PROGRAMMING_LANGUAGE"
    },
    "parent": null,
    "children": [],
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z"
  }
]
```

---

### 7. 🌳 Get Label Hierarchy
Retrieves complete hierarchical structure of all labels.

**`GET /api/v1/labels/hierarchy`**

#### Response (200 OK)
```json
{
  "categories": [
    {
      "category": {
        "id": "CAT-100001",
        "name": "Programming Languages",
        "code": "PROGRAMMING_LANGUAGE"
      },
      "rootLabels": [
        {
          "id": "LBL-100001",
          "name": "Java",
          "children": [
            {
              "id": "LBL_child1",
              "name": "Spring Boot",
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

---

### 8. 📂 Get Labels by Category
Retrieves all labels within a specific category.

**`GET /api/v1/labels/category/{category}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `category` | string | Category code (e.g., "PROGRAMMING_LANGUAGE") |

#### Response (200 OK)
```json
[
  {
    "id": "LBL-100001",
    "name": "Java",
    "description": "Java programming language questions",
    "active": true,
    "parent": null,
    "children": []
  }
]
```

---

### 9. ✅ Get Active Labels by Category
Retrieves only active labels within a specific category.

**`GET /api/v1/labels/category/{category}/active`**

Same response structure as Get Labels by Category, but filtered to active labels only.

---

### 10. 🌿 Get Root Labels by Category
Retrieves root-level labels (no parent) within a category.

**`GET /api/v1/labels/category/{category}/roots`**

#### Response (200 OK)
```json
[
  {
    "id": "LBL-100001",
    "name": "Java",
    "description": "Java programming language questions",
    "active": true,
    "children": [
      {
        "id": "LBL_child1",
        "name": "Spring Boot",
        "active": true
      }
    ]
  }
]
```

---

### 11. 👶 Get Child Labels
Retrieves direct children of a specific label.

**`GET /api/v1/labels/{id}/children`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `activeOnly` | boolean | ❌ | Filter to active children only |

#### Response (200 OK)
```json
[
  {
    "id": "LBL_child1",
    "name": "Spring Boot",
    "description": "Spring Boot framework questions",
    "active": true
  }
]
```

---

### 12. 🔍 Search Labels
Searches labels by name or description.

**`GET /api/v1/labels/search`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `query` | string | ✅ | Search term |
| `categoryId` | string | ❌ | Filter by category |
| `activeOnly` | boolean | ❌ | Filter to active labels only |

#### Example Request
```
GET /api/v1/labels/search?query=java&activeOnly=true
```

---

### 13. 📑 Get All Categories
Retrieves summary of all label categories.

**`GET /api/v1/labels/categories`**

#### Response (200 OK)
```json
[
  {
    "id": "LBC_1a2b3c4d",
    "name": "Programming Languages",
    "code": "PROGRAMMING_LANGUAGE",
    "active": true
  }
]
```

---

### 14. ✅ Check Name Availability
Verifies if a label name is available within a category/parent context.

**`GET /api/v1/labels/check-availability`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | string | ✅ | Label name to check |
| `categoryId` | string | ✅ | Category ID |
| `parentId` | string | ❌ | Parent label ID |
| `excludeId` | string | ❌ | Exclude this label ID (for updates) |

#### Response (200 OK)
```json
{
  "available": true,
  "name": "Python",
  "categoryId": "LBC_1a2b3c4d"
}
```

---

### 15. 🔄 Toggle Label Status
Activate or deactivate a label.

**`PATCH /api/v1/labels/{id}/active`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `active` | boolean | ✅ | True to activate, false to deactivate |

#### Response (200 OK)
Same structure as Get Label by ID with updated status.

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
        "field": "name",
        "message": "Name is required"
      }
    ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Label Not Found (404)
```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Label not found with id: LBL_invalid",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Duplicate Name (409)
```json
{
  "error": {
    "code": "DUPLICATE_RESOURCE",
    "message": "Label 'Java' already exists in category 'Programming Languages'",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Creating Hierarchical Labels
```bash
# 1. Create parent label
curl -X POST "http://localhost:8080/api/v1/labels" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Java",
    "description": "Java programming language",
    "categoryId": "CAT-100001",
    "active": true
  }'

# 2. Create child label
curl -X POST "http://localhost:8080/api/v1/labels" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring Boot",
    "description": "Spring Boot framework",
    "categoryId": "CAT-100001",
    "parentId": "LBL_1a2b3c4d",
    "active": true
  }'

# 3. Get hierarchy
curl -X GET "http://localhost:8080/api/v1/labels/hierarchy"
```

### Bulk Label Creation
```bash
curl -X POST "http://localhost:8080/api/v1/labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "labels": [
      {
        "name": "Array",
        "description": "Array data structure",
        "categoryId": "LBC_ds_category",
        "active": true
      },
      {
        "name": "Tree",
        "description": "Tree data structure", 
        "categoryId": "LBC_ds_category",
        "active": true
      }
    ]
  }'
```

---

## 📊 Hierarchy Best Practices

### Example Category Structure
```
Programming Languages (PROGRAMMING_LANGUAGE)
├── Java
│   ├── Spring Boot
│   ├── Spring MVC
│   └── Hibernate
├── Python
│   ├── Django
│   ├── Flask
│   └── NumPy
└── JavaScript
    ├── React
    ├── Node.js
    └── Vue.js

Data Structures (DATA_STRUCTURE)
├── Array
│   ├── Dynamic Array
│   └── Static Array
├── Tree
│   ├── Binary Tree
│   ├── BST
│   └── AVL Tree
└── Graph
    ├── Directed Graph
    └── Undirected Graph
```

---

**Last Updated**: January 6, 2025