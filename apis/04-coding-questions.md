# ❓ Coding Questions APIs

Complete documentation for coding question management endpoints. This is the core entity for managing programming questions from platforms like LeetCode, HackerRank, etc.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Path** | `/api/v1/questions` |
| **Total Endpoints** | 16 |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |
| **Features** | CRUD operations, search, filtering, statistics |

---

## 📚 API Endpoints

### 1. 🆕 Create Coding Question
Creates a new coding question with all metadata.

**`POST /api/v1/questions`**

#### Request Body
```json
{
  "title": "Two Sum",
  "shortDescription": "Find two numbers that add up to a target value",
  "description": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nYou can return the answer in any order.",
  "difficultyLabelId": "LBL-100001",
  "platformSource": "LeetCode",
  "platformQuestionId": "1",
  "inputFormat": "nums = [2,7,11,15], target = 9",
  "outputFormat": "[0,1]",
  "constraintsText": "2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9\nOnly one valid answer exists.",
  "timeComplexityHint": "O(n)",
  "spaceComplexityHint": "O(n)",
  "status": "ACTIVE",
  "createdByUserId": "ACC-100001"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `title` | ✅ | Max 200 characters, unique per platform |
| `shortDescription` | ❌ | Max 500 characters |
| `description` | ✅ | Full problem description |
| `difficultyLabelId` | ❌ | Must be valid difficulty label ID |
| `platformSource` | ❌ | Max 50 characters (LeetCode, HackerRank, etc.) |
| `platformQuestionId` | ❌ | Max 100 characters, platform-specific ID |
| `inputFormat` | ❌ | Example input format |
| `outputFormat` | ❌ | Example output format |
| `constraintsText` | ❌ | Problem constraints |
| `timeComplexityHint` | ❌ | Max 100 characters |
| `spaceComplexityHint` | ❌ | Max 100 characters |
| `status` | ❌ | Defaults to "ACTIVE" |
| `createdByUserId` | ❌ | User who created the question |

#### Response (201 Created)
```json
{
  "id": "QST-100001",
  "title": "Two Sum",
  "shortDescription": "Find two numbers that add up to a target value",
  "description": "Given an array of integers nums and an integer target...",
  "difficultyLabel": {
    "id": "LBL_easy_diff",
    "name": "Easy",
    "active": true
  },
  "platformSource": "LeetCode",
  "platformQuestionId": "1",
  "inputFormat": "nums = [2,7,11,15], target = 9",
  "outputFormat": "[0,1]",
  "constraintsText": "2 <= nums.length <= 10^4...",
  "timeComplexityHint": "O(n)",
  "spaceComplexityHint": "O(n)",
  "status": "ACTIVE",
  "createdByUser": {
    "id": "ACC-100001",
    "username": "john_doe",
    "email": "john@example.com"
  },
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z",
  "version": 1,
  "solutionsCount": 0,
  "testCasesCount": 0,
  "mediaFilesCount": 0,
  "tags": [],
  "companies": []
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **404 Not Found**: Difficulty label or user not found
- **409 Conflict**: Question title already exists for the platform

---

### 2. 📋 Get All Questions
Retrieves all active coding questions (summary format).

**`GET /api/v1/questions`**

#### Response (200 OK)
```json
[
  {
    "id": "QST-100001",
    "title": "Two Sum",
    "shortDescription": "Find two numbers that add up to a target value",
    "difficultyLabel": {
      "id": "LBL-100001",
      "name": "Easy",
      "active": true
    },
    "platformSource": "LeetCode",
    "status": "ACTIVE",
    "createdAt": "2025-01-06T12:00:00Z",
    "solutionsCount": 3,
    "primaryTags": [
      {
        "id": "LBL_array",
        "name": "Array",
        "active": true
      },
      {
        "id": "LBL_hashtable",
        "name": "Hash Table",
        "active": true
      }
    ],
    "topCompanies": [
      {
        "id": "LBL_google",
        "name": "Google",
        "active": true
      }
    ]
  }
]
```

---

### 3. 📄 Get Questions (Paginated)
Retrieves coding questions with pagination support.

**`GET /api/v1/questions/paginated`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `page` | integer | ❌ | Page number (0-based), default: 0 |
| `size` | integer | ❌ | Page size, default: 20 |
| `sort` | string | ❌ | Sort criteria (e.g., "title,asc") |

#### Example Request
```
GET /api/v1/questions/paginated?page=0&size=10&sort=createdAt,desc
```

#### Response (200 OK)
```json
{
  "content": [
    {
      "id": "QST-100001",
      "title": "Two Sum",
      "shortDescription": "Find two numbers that add up to a target value",
      "difficultyLabel": {
        "id": "LBL-100001",
        "name": "Easy",
        "active": true
      },
      "platformSource": "LeetCode",
      "status": "ACTIVE",
      "createdAt": "2025-01-06T12:00:00Z",
      "solutionsCount": 3,
      "primaryTags": [],
      "topCompanies": []
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageSize": 10,
    "pageNumber": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### 4. 🔍 Get Question by ID
Retrieves detailed information for a specific question.

**`GET /api/v1/questions/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Question ID (e.g., "QST_1a2b3c4d") |

#### Response (200 OK)
Same structure as Create Question response with full details.

---

### 5. ✏️ Update Question
Updates an existing coding question.

**`PUT /api/v1/questions/{id}`**

#### Request Body
```json
{
  "title": "Two Sum - Updated",
  "shortDescription": "Updated description",
  "description": "Updated problem description...",
  "difficultyLabelId": "LBL_medium_diff",
  "timeComplexityHint": "O(n) or O(n log n)",
  "spaceComplexityHint": "O(n)",
  "status": "ACTIVE"
}
```

#### Response (200 OK)
Same structure as Get Question by ID with updated information.

---

### 6. 🗑️ Delete Question
Soft deletes a question (marks as inactive).

**`DELETE /api/v1/questions/{id}`**

#### Response (204 No Content)
No response body.

---

### 7. 📊 Get Questions by Difficulty
Retrieves questions filtered by difficulty level.

**`GET /api/v1/questions/difficulty/{difficultyLabelId}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `difficultyLabelId` | string | Difficulty label ID |

#### Response (200 OK)
Array of question summary objects filtered by difficulty.

---

### 8. 🏢 Get Questions by Platform
Retrieves questions from a specific platform.

**`GET /api/v1/questions/platform/{platformSource}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `platformSource` | string | Platform name (e.g., "LeetCode", "HackerRank") |

#### Response (200 OK)
Array of question summary objects from the specified platform.

---

### 9. 🔍 Search Questions
Advanced search functionality for questions.

**`GET /api/v1/questions/search`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `query` | string | ✅ | Search term (title, description) |
| `difficulty` | string | ❌ | Difficulty label ID |
| `platform` | string | ❌ | Platform source |
| `status` | string | ❌ | Question status |
| `page` | integer | ❌ | Page number |
| `size` | integer | ❌ | Page size |

#### Example Request
```
GET /api/v1/questions/search?query=array&difficulty=LBL_easy_diff&platform=LeetCode
```

#### Response (200 OK)
Paginated response with filtered question summaries.

---

### 10. 🏷️ Get Questions by Label
Retrieves questions associated with a specific label/tag.

**`GET /api/v1/questions/label/{labelId}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `labelId` | string | Label/tag ID |

#### Response (200 OK)
Array of question summaries tagged with the specified label.

---

### 11. 🏢 Get Questions by Company
Retrieves questions asked by a specific company.

**`GET /api/v1/questions/company/{companyLabelId}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `companyLabelId` | string | Company label ID |

#### Response (200 OK)
Array of question summaries associated with the company.

---

### 12. 👤 Get Questions by User
Retrieves questions created by a specific user.

**`GET /api/v1/questions/user/{userId}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `userId` | string | User ID |

#### Response (200 OK)
Array of question summaries created by the user.

---

### 13. ✅ Get Active Questions
Retrieves only active questions (status = ACTIVE).

**`GET /api/v1/questions/active`**

#### Response (200 OK)
Array of active question summaries.

---

### 14. 📊 Get Questions by Status
Retrieves questions filtered by status.

**`GET /api/v1/questions/status/{status}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `status` | string | Question status (ACTIVE, INACTIVE, DRAFT, etc.) |

#### Response (200 OK)
Array of question summaries with the specified status.

---

### 15. ✅ Check Title Availability
Verifies if a question title is available for a platform.

**`GET /api/v1/questions/check-title`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `title` | string | ✅ | Question title to check |
| `platform` | string | ✅ | Platform source |
| `excludeId` | string | ❌ | Exclude this question ID (for updates) |

#### Example Request
```
GET /api/v1/questions/check-title?title=Three%20Sum&platform=LeetCode
```

#### Response (200 OK)
```json
{
  "available": true,
  "title": "Three Sum",
  "platform": "LeetCode"
}
```

---

### 16. 📈 Get Question Statistics
Retrieves detailed statistics for a specific question.

**`GET /api/v1/questions/{id}/statistics`**

#### Response (200 OK)
```json
{
  "questionId": "QST_1a2b3c4d",
  "solutionsCount": 5,
  "testCasesCount": 12,
  "mediaFilesCount": 2,
  "associatedCompaniesCount": 8,
  "associatedTagsCount": 4,
  "viewsCount": 1250,
  "lastUpdated": "2025-01-06T12:00:00Z",
  "solutionsByLanguage": {
    "Java": 2,
    "Python": 2,
    "JavaScript": 1
  },
  "popularityScore": 85.5,
  "completionRate": 78.2
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
        "field": "title",
        "message": "Title is required"
      }
    ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Question Not Found (404)
```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Question not found with id: QST_invalid",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Title Conflict (409)
```json
{
  "error": {
    "code": "DUPLICATE_RESOURCE",
    "message": "Question title 'Two Sum' already exists for platform 'LeetCode'",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Complete Question Creation Flow
```bash
# 1. Check title availability
curl -X GET "http://localhost:8080/api/v1/questions/check-title?title=Two%20Sum&platform=LeetCode"

# 2. Create question
curl -X POST "http://localhost:8080/api/v1/questions" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Two Sum",
    "shortDescription": "Find two numbers that add up to a target",
    "description": "Given an array of integers...",
    "difficultyLabelId": "LBL-100001",
    "platformSource": "LeetCode",
    "platformQuestionId": "1",
    "timeComplexityHint": "O(n)",
    "spaceComplexityHint": "O(n)",
    "createdByUserId": "ACC-100001"
  }'

# 3. Get question details
curl -X GET "http://localhost:8080/api/v1/questions/QST_1a2b3c4d"

# 4. Get statistics
curl -X GET "http://localhost:8080/api/v1/questions/QST_1a2b3c4d/statistics"
```

### Search and Filter Examples
```bash
# Search questions
curl -X GET "http://localhost:8080/api/v1/questions/search?query=array&difficulty=LBL_easy_diff"

# Get questions by platform
curl -X GET "http://localhost:8080/api/v1/questions/platform/LeetCode"

# Get paginated results
curl -X GET "http://localhost:8080/api/v1/questions/paginated?page=0&size=20&sort=createdAt,desc"

# Get questions by company
curl -X GET "http://localhost:8080/api/v1/questions/company/LBL_google"
```

---

## 📊 Question Status Values

| Status | Description |
|--------|-------------|
| `ACTIVE` | Question is active and visible |
| `INACTIVE` | Question is inactive/hidden |
| `DRAFT` | Question is in draft mode |
| `ARCHIVED` | Question is archived |
| `UNDER_REVIEW` | Question is being reviewed |

## 🏢 Common Platform Sources

| Platform | Code |
|----------|------|
| LeetCode | `LeetCode` |
| HackerRank | `HackerRank` |
| CodeSignal | `CodeSignal` |
| InterviewBit | `InterviewBit` |
| GeeksforGeeks | `GeeksforGeeks` |
| Codeforces | `Codeforces` |
| AtCoder | `AtCoder` |
| Custom | `Custom` |

---

**Last Updated**: January 6, 2025