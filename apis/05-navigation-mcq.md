# Navigation and MCQ API Documentation

## Overview

This document describes the hierarchical navigation and Multiple Choice Questions (MCQ) API endpoints that allow users to browse through label categories, labels, and access associated MCQ questions.

## Features

- **Hierarchical Navigation**: Browse through label categories and their hierarchical label structure
- **MCQ Management**: Create, read, update, and delete multiple choice questions
- **Label-based MCQ Retrieval**: Get MCQ questions associated with specific labels or label hierarchies
- **Random MCQ Selection**: Retrieve random questions for practice sessions
- **Navigation Links**: Automatic generation of navigation URLs for easy browsing

## Navigation Endpoints

### Get All Label Categories

```http
GET /api/v1/navigation/categories
```

Returns all active label categories for navigation.

**Response:**
```json
[
  {
    "id": "CAT-100001",
    "name": "Data Structure",
    "code": "DATA_STRUCTURE",
    "description": "Data structure related labels",
    "urlSlug": "data-structure",
    "labelCount": 15
  }
]
```

### Get Labels by Category

```http
GET /api/v1/navigation/categories/{categorySlug}/labels
```

Returns all labels within a specific category (using category slug).

**Response:**
```json
[
  {
    "id": "LBL-100001",
    "name": "Array",
    "description": "Array data structure",
    "urlSlug": "array",
    "categoryId": "CAT-100001",
    "categoryName": "Data Structure",
    "parent": null,
    "children": [
      {
        "id": "LBL-100002",
        "name": "Dynamic Array",
        "description": "Dynamic arrays like vectors",
        "urlSlug": "dynamic-array"
      }
    ],
    "hasChildren": true,
    "isRoot": true,
    "mcqCount": 5,
    "totalMcqCountInHierarchy": 12,
    "navigationLinks": {
      "mcqsUrl": "/api/v1/mcq/labels/array/questions",
      "randomMcqsUrl": "/api/v1/mcq/labels/array/questions/random",
      "labelsUrl": "/api/v1/navigation/labels/array",
      "childrenUrl": "/api/v1/navigation/labels/array/children"
    }
  }
]
```

### Get Root Labels by Category

```http
GET /api/v1/navigation/categories/{categorySlug}/labels/root
```

Returns only root labels (labels without parent) within a specific category (using category slug).

### Get Label Navigation Details

```http
GET /api/v1/navigation/labels/{labelSlug}
```

Returns detailed navigation information for a specific label including children, MCQ counts, and navigation links (using label slug).

### Get Child Labels

```http
GET /api/v1/navigation/labels/{parentLabelSlug}/children
```

Returns child labels for a specific parent label (using parent label slug).

## MCQ Question Endpoints

### Create MCQ Question

```http
POST /api/v1/mcq
```

**Request Body:**
```json
{
  "questionText": "What is the time complexity of accessing an element in an array by index?",
  "optionA": "O(1)",
  "optionB": "O(n)",
  "optionC": "O(log n)",
  "optionD": "O(n^2)",
  "correctAnswer": "A",
  "explanation": "Array elements can be accessed directly using their index in constant time.",
  "difficultyLabelId": "LBL-100005"
}
```

**Response:**
```json
{
  "id": "MCQ-100001",
  "questionText": "What is the time complexity of accessing an element in an array by index?",
  "optionA": "O(1)",
  "optionB": "O(n)",
  "optionC": "O(log n)",
  "optionD": "O(n^2)",
  "correctAnswer": "A",
  "explanation": "Array elements can be accessed directly using their index in constant time.",
  "difficultyLabel": {
    "id": "LBL-100005",
    "name": "Easy",
    "description": "Easy difficulty level"
  },
  "status": "ACTIVE",
  "mcqLabels": [],
  "urlSlug": "time-complexity-array-access",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Get MCQ Questions by Label

```http
GET /api/v1/mcq/labels/{labelSlug}/questions
```

Returns all MCQ questions associated with a specific label (using label slug).

**Response:**
```json
[
  {
    "id": "MCQ-100001",
    "questionText": "What is the time complexity of accessing an element in an array by index?",
    "optionA": "O(1)",
    "optionB": "O(n)",
    "optionC": "O(log n)",
    "optionD": "O(n^2)",
    "difficultyLabel": {
      "id": "LBL-100005",
      "name": "Easy"
    },
    "status": "ACTIVE",
    "mcqLabels": [
      {
        "id": "MLB-100001",
        "label": {
          "id": "LBL-100001",
          "name": "Array"
        },
        "relevanceScore": 10,
        "isPrimary": true
      }
    ],
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

### Get MCQ Questions by Label Hierarchy

```http
GET /api/v1/mcq/labels/{labelSlug}/questions/hierarchy
```

Returns MCQ questions associated with a label and all its child labels (using label slug).

### Get Random MCQ Questions

```http
GET /api/v1/mcq/labels/{labelSlug}/questions/random?count=10
```

Returns random MCQ questions for practice sessions (using label slug).

**Parameters:**
- `count` (optional): Number of random questions to retrieve (default: 10)

### Get Random MCQ Questions by Hierarchy

```http
GET /api/v1/mcq/labels/{labelSlug}/questions/random/hierarchy?count=10
```

Returns random MCQ questions from a label and its children (using label slug).

### Get MCQ Question Count

```http
GET /api/v1/mcq/labels/{labelSlug}/count
```

Returns the count of MCQ questions associated with a specific label (using label slug).

**Response:**
```json
5
```

### Get MCQ Question Count by Hierarchy

```http
GET /api/v1/mcq/labels/{labelSlug}/count/hierarchy
```

Returns the count of MCQ questions in a label hierarchy (including children) (using label slug).

### Update MCQ Question

```http
PUT /api/v1/mcq/{id}
```

**Request Body:**
```json
{
  "questionText": "Updated question text",
  "explanation": "Updated explanation",
  "status": "ACTIVE"
}
```

### Delete MCQ Question

```http
DELETE /api/v1/mcq/{id}
```

### Associate Label with MCQ Question

```http
POST /api/v1/mcq/labels
```

**Request Body:**
```json
{
  "mcqQuestionId": "MCQ-100001",
  "labelId": "LBL-100001",
  "relevanceScore": 8,
  "isPrimary": true,
  "notes": "Primary topic for this question"
}
```

### Remove Label from MCQ Question

```http
DELETE /api/v1/mcq/{mcqQuestionId}/labels/{labelId}
```

## Usage Flow

### Navigation Flow

1. **Start with Categories**: Call `GET /api/v1/navigation/categories` to get all available label categories
2. **Browse Labels**: For each category, call `GET /api/v1/navigation/categories/{categoryId}/labels/root` to get root labels
3. **Explore Hierarchy**: For labels with children, call `GET /api/v1/navigation/labels/{labelId}/children` to navigate deeper
4. **Access MCQs**: Use the navigation links provided in the response to access MCQ questions

### MCQ Practice Flow

1. **Select Topic**: Choose a label using the navigation endpoints
2. **Get Questions**: Call `GET /api/v1/mcq/labels/{labelId}/questions/random?count=10` for practice questions
3. **Include Subtopics**: Use `/hierarchy` endpoints to include questions from child labels
4. **Check Progress**: Use count endpoints to see total available questions

## Example Navigation Scenario

### Scenario: User wants to practice Data Structure questions, specifically on Trees

1. **Get Categories**:
   ```http
   GET /api/v1/navigation/categories
   ```
   Response includes "Data Structure" category

2. **Get Root Labels in Data Structure**:
   ```http
   GET /api/v1/navigation/categories/data-structure/labels/root
   ```
   Response includes "Trees" label

3. **Explore Trees Hierarchy**:
   ```http
   GET /api/v1/navigation/labels/trees
   ```
   Response shows children like "Binary Trees", "BST", etc.

4. **Get Tree MCQs**:
   ```http
   GET /api/v1/mcq/labels/trees/questions/random/hierarchy?count=10
   ```
   Returns 10 random questions from Trees and all its subtopics

## Security

- **Authentication**: Required for creating, updating, and deleting MCQ questions
- **Authorization**: 
  - `ADMIN` role: Full access to all operations
  - `MODERATOR` role: Can create and update MCQ questions
  - `USER` role: Read-only access to MCQs and navigation
- **Public Access**: Navigation endpoints are publicly accessible for guest users

## Error Handling

### Common Error Responses

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Label not found with ID: LBL-999999",
  "path": "/api/v1/navigation/labels/LBL-999999"
}
```

### Status Codes

- `200` - Success
- `201` - Created (for POST requests)
- `204` - No Content (for DELETE requests)
- `400` - Bad Request (validation errors)
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `409` - Conflict (duplicate resources)

## Data Models

### MCQ Question Statuses

- `ACTIVE` - Question is active and can be used
- `DEPRECATED` - Question is deprecated but still visible
- `DRAFT` - Question is in draft status

### Navigation Links

Each label in navigation responses includes helpful links:

- `mcqsUrl` - Get all MCQs for this label (using slug)
- `randomMcqsUrl` - Get random MCQs for practice (using slug)
- `labelsUrl` - Get detailed label information (using slug)
- `parentUrl` - Navigate to parent label (if exists, using slug)
- `childrenUrl` - Get child labels (if has children, using slug)

## Testing

Use the provided script to add sample data:

```bash
./scripts/create-sample-mcqs.sh
```

This will create sample MCQ questions for testing the navigation and retrieval functionality.
