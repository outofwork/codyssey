# 🔗 Question Relations APIs

Complete documentation for managing question relationships including links, labels, and company associations.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Paths** | `/api/v1/question-links`, `/api/v1/question-labels`, `/api/v1/question-companies` |
| **Total Endpoints** | 35+ |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |
| **Features** | Link management, tagging, company tracking, bulk operations |

---

## 🔗 Question Links APIs (`/v1/question-links`)

Question links establish relationships between questions (follow-ups, similar questions, prerequisites).

### 1. 🆕 Create Question Link
Creates a relationship between two questions.

**`POST /api/v1/question-links`**

#### Request Body
```json
{
  "sourceQuestionId": "QST-100001",
  "targetQuestionId": "QST-100002",
  "sequence": 1,
  "relationshipType": "FOLLOW_UP"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `sourceQuestionId` | ✅ | Must be valid question ID |
| `targetQuestionId` | ✅ | Must be valid question ID, different from source |
| `sequence` | ✅ | Integer, order within relationship type |
| `relationshipType` | ✅ | FOLLOW_UP, SIMILAR, PREREQUISITE |

#### Response (201 Created)
```json
{
  "id": "QLK-100001",
  "sourceQuestionId": "QST-100001",
  "sourceQuestionTitle": "Two Sum",
  "targetQuestionId": "QST-100002",
  "targetQuestionTitle": "Three Sum",
  "sequence": 1,
  "relationshipType": "FOLLOW_UP"
}
```

### 2. 📦 Create Multiple Links (Bulk)
Creates multiple question links in one operation.

**`POST /api/v1/question-links/bulk`**

#### Request Body
```json
{
  "questionLinks": [
    {
      "sourceQuestionId": "QST-100001",
      "targetQuestionId": "QST-100002",
      "sequence": 1,
      "relationshipType": "FOLLOW_UP"
    },
    {
      "sourceQuestionId": "QST-100001",
      "targetQuestionId": "QST_3f4g5h6i",
      "sequence": 2,
      "relationshipType": "FOLLOW_UP"
    }
  ]
}
```

#### Response (201 Created)
```json
{
  "createdLinks": [
    {
      "id": "QLK-100001",
      "sourceQuestionId": "QST-100001",
      "sourceQuestionTitle": "Two Sum",
      "targetQuestionId": "QST-100002",
      "targetQuestionTitle": "Three Sum",
      "sequence": 1,
      "relationshipType": "FOLLOW_UP"
    }
  ],
  "totalCreated": 2
}
```

### 3. 🔍 Get Outgoing Links
Retrieves all links from a source question.

**`GET /api/v1/question-links/outgoing/{sourceQuestionId}`**

#### Response (200 OK)
```json
[
  {
    "id": "QLK-100001",
    "sourceQuestionId": "QST-100001",
    "sourceQuestionTitle": "Two Sum",
    "targetQuestionId": "QST-100002",
    "targetQuestionTitle": "Three Sum",
    "sequence": 1,
    "relationshipType": "FOLLOW_UP"
  }
]
```

### 4. 🔍 Get Incoming Links
Retrieves all links to a target question.

**`GET /api/v1/question-links/incoming/{targetQuestionId}`**

### 5. 🔍 Get Follow-up Questions
Retrieves questions that follow the specified question.

**`GET /api/v1/question-links/{sourceQuestionId}/follow-ups`**

### 6. 🔍 Get Similar Questions
Retrieves questions similar to the specified question.

**`GET /api/v1/question-links/{sourceQuestionId}/similar`**

### 7. 🔍 Get Prerequisite Questions
Retrieves questions that are prerequisites for the specified question.

**`GET /api/v1/question-links/{sourceQuestionId}/prerequisites`**

### 8. 🔄 Reorder Links
Updates the sequence order of links from a source question.

**`PUT /api/v1/question-links/reorder/{sourceQuestionId}`**

#### Request Body
```json
{
  "sequenceUpdates": [
    {
      "linkId": "QLK_1a2b3c4d",
      "sequence": 2
    },
    {
      "linkId": "QLK_2e3f4g5h",
      "sequence": 1
    }
  ]
}
```

---

## 🏷️ Question Labels APIs (`/v1/question-labels`)

Manage tags/labels associated with questions.

### 1. 🆕 Create Question-Label Association
Associates a label/tag with a question.

**`POST /api/v1/question-labels`**

#### Request Body
```json
{
  "questionId": "QST-100001",
  "labelId": "LBL_array"
}
```

#### Response (201 Created)
```json
{
  "questionId": "QST-100001",
  "questionTitle": "Two Sum",
  "labelId": "LBL_array",
  "labelName": "Array",
  "createdAt": "2025-01-06T12:00:00Z"
}
```

### 2. 📦 Create Multiple Associations (Bulk)
Associates multiple labels with questions in one operation.

**`POST /api/v1/question-labels/bulk`**

#### Request Body
```json
{
  "associations": [
    {
      "questionId": "QST-100001",
      "labelId": "LBL_array"
    },
    {
      "questionId": "QST-100001",
      "labelId": "LBL_hashtable"
    }
  ]
}
```

### 3. 🔍 Get Labels for Question
Retrieves all labels associated with a specific question.

**`GET /api/v1/question-labels/question/{questionId}/labels`**

#### Response (200 OK)
```json
[
  {
    "id": "LBL_array",
    "name": "Array",
    "description": "Array data structure problems",
    "category": {
      "id": "LBC_datastructure",
      "name": "Data Structures",
      "code": "DATA_STRUCTURE"
    },
    "active": true
  }
]
```

### 4. 🔍 Get Questions for Label
Retrieves all questions associated with a specific label.

**`GET /api/v1/question-labels/label/{labelId}/questions`**

### 5. ✏️ Update Question Labels
Replaces all labels for a question with a new set.

**`PUT /api/v1/question-labels/question/{questionId}/labels`**

#### Request Body
```json
{
  "labelIds": ["LBL_array", "LBL_hashtable", "LBL_easy"]
}
```

### 6. 🗑️ Remove Label from Question
Removes a specific label from a question.

**`DELETE /api/v1/question-labels/question/{questionId}/label/{labelId}`**

### 7. 📊 Get Most Used Labels
Retrieves labels sorted by usage frequency.

**`GET /api/v1/question-labels/most-used`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `limit` | integer | ❌ | Max number of labels to return (default: 20) |

#### Response (200 OK)
```json
[
  {
    "label": {
      "id": "LBL_array",
      "name": "Array",
      "active": true
    },
    "usageCount": 150,
    "percentage": 25.5
  }
]
```

---

## 🏢 Question Companies APIs (`/v1/question-companies`)

Manage company associations with questions (interview tracking).

### 1. 🆕 Create Question-Company Association
Associates a company with a question they ask in interviews.

**`POST /api/v1/question-companies`**

#### Request Body
```json
{
  "questionId": "QST-100001",
  "companyLabelId": "LBL_google",
  "frequency": 5,
  "lastAskedYear": 2024
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `questionId` | ✅ | Must be valid question ID |
| `companyLabelId` | ✅ | Must be valid company label ID |
| `frequency` | ❌ | Integer, defaults to 1 |
| `lastAskedYear` | ❌ | Year when last asked |

#### Response (201 Created)
```json
{
  "questionId": "QST-100001",
  "questionTitle": "Two Sum",
  "companyLabelId": "LBL_google",
  "companyName": "Google",
  "frequency": 5,
  "lastAskedYear": 2024,
  "createdAt": "2025-01-06T12:00:00Z"
}
```

### 2. 📦 Create Multiple Associations (Bulk)
Associates multiple companies with questions.

**`POST /api/v1/question-companies/bulk`**

#### Request Body
```json
{
  "associations": [
    {
      "questionId": "QST-100001",
      "companyLabelId": "LBL_google",
      "frequency": 5,
      "lastAskedYear": 2024
    },
    {
      "questionId": "QST-100001",
      "companyLabelId": "LBL_microsoft",
      "frequency": 3,
      "lastAskedYear": 2023
    }
  ]
}
```

### 3. 🔍 Get Companies for Question
Retrieves all companies associated with a specific question.

**`GET /api/v1/question-companies/question/{questionId}/companies`**

#### Response (200 OK)
```json
[
  {
    "company": {
      "id": "LBL_google",
      "name": "Google",
      "active": true
    },
    "frequency": 5,
    "lastAskedYear": 2024,
    "popularityScore": 8.5
  }
]
```

### 4. 🔍 Get Questions for Company
Retrieves all questions asked by a specific company.

**`GET /api/v1/question-companies/company/{companyLabelId}/questions`**

### 5. 🔍 Get Questions by Company and Year
Retrieves questions asked by a company in a specific year.

**`GET /api/v1/question-companies/company/{companyLabelId}/year/{year}/questions`**

### 6. ✏️ Update Question Frequency
Updates how frequently a company asks a specific question.

**`PUT /api/v1/question-companies/question/{questionId}/company/{companyLabelId}/frequency`**

#### Request Body
```json
{
  "frequency": 7
}
```

### 7. ✏️ Update Last Asked Year
Updates when a company last asked a specific question.

**`PUT /api/v1/question-companies/question/{questionId}/company/{companyLabelId}/year`**

#### Request Body
```json
{
  "lastAskedYear": 2024
}
```

### 8. 📊 Get Top Companies
Retrieves companies sorted by number of questions they ask.

**`GET /api/v1/question-companies/top-companies`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `limit` | integer | ❌ | Max number of companies (default: 20) |
| `year` | integer | ❌ | Filter by specific year |

#### Response (200 OK)
```json
[
  {
    "company": {
      "id": "LBL_google",
      "name": "Google",
      "active": true
    },
    "totalQuestions": 245,
    "avgFrequency": 4.2,
    "lastActivity": "2024-12-15",
    "popularityRank": 1
  }
]
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
        "field": "sourceQuestionId",
        "message": "Source question ID is required"
      }
    ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Circular Dependency (400)
```json
{
  "error": {
    "code": "CIRCULAR_DEPENDENCY",
    "message": "Creating this link would result in a circular dependency",
    "details": ["QST_1a2b3c4d -> QST_2e3f4g5h -> QST_1a2b3c4d"]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Association Already Exists (409)
```json
{
  "error": {
    "code": "DUPLICATE_RESOURCE",
    "message": "Association between question and label already exists",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Creating Question Relationships
```bash
# 1. Create question link
curl -X POST "http://localhost:8080/api/v1/question-links" \
  -H "Content-Type: application/json" \
  -d '{
    "sourceQuestionId": "QST-100001",
    "targetQuestionId": "QST-100002",
    "sequence": 1,
    "relationshipType": "FOLLOW_UP"
  }'

# 2. Add labels to question
curl -X POST "http://localhost:8080/api/v1/question-labels/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "associations": [
      {
        "questionId": "QST-100001",
        "labelId": "LBL_array"
      },
      {
        "questionId": "QST-100001",
        "labelId": "LBL_hashtable"
      }
    ]
  }'

# 3. Associate with companies
curl -X POST "http://localhost:8080/api/v1/question-companies" \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": "QST-100001",
    "companyLabelId": "LBL_google",
    "frequency": 5,
    "lastAskedYear": 2024
  }'
```

### Querying Relationships
```bash
# Get follow-up questions
curl -X GET "http://localhost:8080/api/v1/question-links/QST-100001/follow-ups"

# Get question labels
curl -X GET "http://localhost:8080/api/v1/question-labels/question/QST-100001/labels"

# Get company questions for 2024
curl -X GET "http://localhost:8080/api/v1/question-companies/company/LBL_google/year/2024/questions"

# Get most used labels
curl -X GET "http://localhost:8080/api/v1/question-labels/most-used?limit=10"

# Get top companies
curl -X GET "http://localhost:8080/api/v1/question-companies/top-companies?limit=15"
```

---

## 📊 Relationship Types

### Question Link Types
| Type | Description | Example |
|------|-------------|---------|
| `FOLLOW_UP` | Next question in learning path | Two Sum → Three Sum |
| `SIMILAR` | Similar problem patterns | Two Sum → Two Sum II |
| `PREREQUISITE` | Required background knowledge | Array Basics → Two Sum |

### Common Company Frequency Meanings
| Frequency | Meaning |
|-----------|---------|
| 1-2 | Rarely asked |
| 3-4 | Occasionally asked |
| 5-6 | Frequently asked |
| 7-8 | Very frequently asked |
| 9-10 | Almost always asked |

---

## 🎯 Best Practices

### Question Linking Strategy
1. **Follow-ups**: Natural progression of difficulty or concepts
2. **Similar**: Same patterns, different contexts
3. **Prerequisites**: Foundational knowledge needed
4. **Sequence**: Order questions by learning progression

### Tagging Guidelines
1. **Data Structure tags**: Array, Tree, Graph, etc.
2. **Algorithm tags**: Sorting, DP, BFS, etc.
3. **Technique tags**: Two Pointers, Sliding Window, etc.
4. **Difficulty tags**: Easy, Medium, Hard

### Company Tracking
1. **Frequency**: Based on interview reports
2. **Year**: Keep recent (last 2-3 years)
3. **Verification**: Cross-reference multiple sources

---

**Last Updated**: January 6, 2025