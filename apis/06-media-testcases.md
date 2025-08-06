# 📁 Media & Test Cases APIs

Complete documentation for managing question media files, test cases, and solutions.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Paths** | `/api/v1/media`, `/api/v1/test-cases`, `/api/v1/solutions` |
| **Total Endpoints** | 30+ |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |
| **Features** | File upload/download, test case management, solution tracking |

---

## 📁 Question Media APIs (`/v1/media`)

Manage media files (images, diagrams, videos) associated with coding questions.

### 1. 🆕 Create Media File Reference
Creates a reference to a media file for a question.

**`POST /api/v1/media`**

#### Request Body
```json
{
  "questionId": "QST-100001",
  "sequence": 1,
  "mediaType": "IMAGE",
  "filePath": "/uploads/questions/QST_1a2b3c4d/diagram_1.png",
  "fileName": "two_sum_diagram.png",
  "description": "Visual representation of the two sum problem"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `questionId` | ✅ | Must be valid question ID |
| `sequence` | ✅ | Integer, order within question |
| `mediaType` | ✅ | IMAGE, DIAGRAM, VIDEO, AUDIO, DOCUMENT |
| `filePath` | ✅ | Max 500 characters, file storage path |
| `fileName` | ✅ | Max 200 characters, original filename |
| `description` | ❌ | Max 500 characters |

#### Response (201 Created)
```json
{
  "id": "MED-100001",
  "questionId": "QST-100001",
  "sequence": 1,
  "mediaType": "IMAGE",
  "filePath": "/uploads/questions/QST_1a2b3c4d/diagram_1.png",
  "fileName": "two_sum_diagram.png",
  "description": "Visual representation of the two sum problem",
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z",
  "version": 1
}
```

### 2. 📦 Create Multiple Media Files (Bulk)
Creates multiple media file references in one operation.

**`POST /api/v1/media/bulk`**

#### Request Body
```json
{
  "mediaFiles": [
    {
      "questionId": "QST-100001",
      "sequence": 1,
      "mediaType": "IMAGE",
      "filePath": "/uploads/questions/QST_1a2b3c4d/diagram_1.png",
      "fileName": "two_sum_diagram.png",
      "description": "Visual representation"
    },
    {
      "questionId": "QST-100001",
      "sequence": 2,
      "mediaType": "VIDEO",
      "filePath": "/uploads/questions/QST_1a2b3c4d/explanation.mp4",
      "fileName": "explanation_video.mp4",
      "description": "Video explanation"
    }
  ]
}
```

### 3. 📤 Upload Media File
Uploads a media file and creates the database reference.

**`POST /api/v1/media/upload`**

#### Request (Multipart Form Data)
```
Content-Type: multipart/form-data

Fields:
- file: (binary file data)
- questionId: QST_1a2b3c4d
- sequence: 1
- mediaType: IMAGE
- description: Visual diagram for the problem
```

#### Response (201 Created)
```json
{
  "id": "MED-100001",
  "questionId": "QST-100001",
  "sequence": 1,
  "mediaType": "IMAGE",
  "filePath": "/uploads/questions/QST_1a2b3c4d/20250106_120000_diagram.png",
  "fileName": "diagram.png",
  "description": "Visual diagram for the problem",
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z",
  "version": 1
}
```

### 4. 📥 Download Media File
Downloads a media file by its ID.

**`GET /api/v1/media/{id}/download`**

#### Response (200 OK)
```
Content-Type: image/png (or appropriate MIME type)
Content-Disposition: attachment; filename="two_sum_diagram.png"

[Binary file data]
```

### 5. 🔍 Get Media Files for Question
Retrieves all media files for a specific question.

**`GET /api/v1/media/question/{questionId}`**

#### Response (200 OK)
```json
[
  {
    "id": "MED-100001",
    "questionId": "QST-100001",
    "sequence": 1,
    "mediaType": "IMAGE",
    "filePath": "/uploads/questions/QST_1a2b3c4d/diagram_1.png",
    "fileName": "two_sum_diagram.png",
    "description": "Visual representation",
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z",
    "version": 1
  }
]
```

### 6. 🔍 Get Media Files by Type
Retrieves media files filtered by type.

**`GET /api/v1/media/type/{mediaType}`**

### 7. 🔄 Reorder Media Files
Updates the sequence order of media files for a question.

**`PUT /api/v1/media/reorder/{questionId}`**

#### Request Body
```json
{
  "sequenceUpdates": [
    {
      "mediaId": "MED_1a2b3c4d",
      "sequence": 2
    },
    {
      "mediaId": "MED_2e3f4g5h",
      "sequence": 1
    }
  ]
}
```

---

## 🧪 Test Cases APIs (`/v1/test-cases`)

Manage test cases for coding questions including sample and hidden test cases.

### 1. 🆕 Create Test Case
Creates a new test case for a question.

**`POST /api/v1/test-cases`**

#### Request Body
```json
{
  "questionId": "QST-100001",
  "sequence": 1,
  "inputData": "[2,7,11,15]\n9",
  "expectedOutput": "[0,1]",
  "isSample": true,
  "explanation": "nums[0] + nums[1] = 2 + 7 = 9, so return [0, 1]"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `questionId` | ✅ | Must be valid question ID |
| `sequence` | ✅ | Integer, order within question |
| `inputData` | ✅ | Test case input data |
| `expectedOutput` | ✅ | Expected output for the input |
| `isSample` | ❌ | Boolean, defaults to false |
| `explanation` | ❌ | Optional explanation for the test case |

#### Response (201 Created)
```json
{
  "id": "TST-100001",
  "questionId": "QST-100001",
  "sequence": 1,
  "inputData": "[2,7,11,15]\n9",
  "expectedOutput": "[0,1]",
  "isSample": true,
  "explanation": "nums[0] + nums[1] = 2 + 7 = 9, so return [0, 1]",
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z",
  "version": 1
}
```

### 2. 📦 Create Multiple Test Cases (Bulk)
Creates multiple test cases in one operation.

**`POST /api/v1/test-cases/bulk`**

#### Request Body
```json
{
  "testCases": [
    {
      "questionId": "QST-100001",
      "sequence": 1,
      "inputData": "[2,7,11,15]\n9",
      "expectedOutput": "[0,1]",
      "isSample": true,
      "explanation": "Basic example"
    },
    {
      "questionId": "QST-100001",
      "sequence": 2,
      "inputData": "[3,2,4]\n6",
      "expectedOutput": "[1,2]",
      "isSample": true,
      "explanation": "Second example"
    },
    {
      "questionId": "QST-100001",
      "sequence": 3,
      "inputData": "[1000,999,1]\n2000",
      "expectedOutput": "[0,1]",
      "isSample": false,
      "explanation": "Hidden edge case"
    }
  ]
}
```

### 3. 🔍 Get Test Cases for Question
Retrieves all test cases for a specific question.

**`GET /api/v1/test-cases/question/{questionId}`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `includeHidden` | boolean | ❌ | Include hidden test cases (default: true) |

### 4. 👀 Get Sample Test Cases
Retrieves only sample (visible) test cases for a question.

**`GET /api/v1/test-cases/question/{questionId}/sample`**

#### Response (200 OK)
```json
[
  {
    "id": "TST-100001",
    "questionId": "QST-100001",
    "sequence": 1,
    "inputData": "[2,7,11,15]\n9",
    "expectedOutput": "[0,1]",
    "isSample": true,
    "explanation": "nums[0] + nums[1] = 2 + 7 = 9, so return [0, 1]",
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z",
    "version": 1
  }
]
```

### 5. 🔒 Get Hidden Test Cases
Retrieves only hidden test cases for a question.

**`GET /api/v1/test-cases/question/{questionId}/hidden`**

### 6. ✏️ Update Test Case
Updates an existing test case.

**`PUT /api/v1/test-cases/{id}`**

#### Request Body
```json
{
  "inputData": "[2,7,11,15]\n9",
  "expectedOutput": "[0,1]",
  "isSample": true,
  "explanation": "Updated explanation: nums[0] + nums[1] = 2 + 7 = 9"
}
```

### 7. 🔄 Toggle Sample Status
Marks a test case as sample or hidden.

**`PUT /api/v1/test-cases/{id}/sample`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `isSample` | boolean | ✅ | True for sample, false for hidden |

### 8. 🔍 Search Test Cases by Input
Searches test cases by input data patterns.

**`GET /api/v1/test-cases/search/input`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `pattern` | string | ✅ | Search pattern in input data |
| `questionId` | string | ❌ | Filter by specific question |

### 9. 📊 Clone Test Case
Creates a copy of an existing test case.

**`POST /api/v1/test-cases/clone`**

#### Request Body
```json
{
  "sourceTestCaseId": "TST_1a2b3c4d",
  "targetQuestionId": "QST_2e3f4g5h",
  "sequence": 1
}
```

---

## 💡 Solutions APIs (`/v1/solutions`)

Manage coding solutions for questions in different programming languages.

### 1. 🆕 Create Solution
Creates a new solution for a question.

**`POST /api/v1/solutions`**

#### Request Body
```json
{
  "questionId": "QST-100001",
  "sequence": 1,
  "language": "Java",
  "solutionCode": "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[]{map.get(complement), i};\n            }\n            map.put(nums[i], i);\n        }\n        throw new IllegalArgumentException(\"No two sum solution\");\n    }\n}",
  "explanation": "Use a hash map to store numbers and their indices. For each number, check if its complement exists in the map.",
  "timeComplexity": "O(n)",
  "spaceComplexity": "O(n)",
  "isOptimal": true,
  "createdByUserId": "ACC-100001"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `questionId` | ✅ | Must be valid question ID |
| `sequence` | ✅ | Integer, order within question/language |
| `language` | ✅ | Max 50 characters (Java, Python, C++, etc.) |
| `solutionCode` | ✅ | Complete solution code |
| `explanation` | ❌ | Solution explanation |
| `timeComplexity` | ❌ | Max 50 characters (O(n), O(log n), etc.) |
| `spaceComplexity` | ❌ | Max 50 characters |
| `isOptimal` | ❌ | Boolean, defaults to false |
| `createdByUserId` | ❌ | User who created the solution |

#### Response (201 Created)
```json
{
  "id": "SOL-100001",
  "questionId": "QST-100001",
  "sequence": 1,
  "language": "Java",
  "solutionCode": "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // ... code ...\n    }\n}",
  "explanation": "Use a hash map to store numbers and their indices...",
  "timeComplexity": "O(n)",
  "spaceComplexity": "O(n)",
  "isOptimal": true,
  "createdByUser": {
    "id": "USR_1a2b3c4d",
    "username": "john_doe",
    "email": "john@example.com"
  },
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z",
  "version": 1
}
```

### 2. 📦 Create Multiple Solutions (Bulk)
Creates multiple solutions in one operation.

**`POST /api/v1/solutions/bulk`**

### 3. 🔍 Get Solutions for Question
Retrieves all solutions for a specific question.

**`GET /api/v1/solutions/question/{questionId}`**

#### Response (200 OK)
```json
[
  {
    "id": "SOL-100001",
    "questionId": "QST-100001",
    "sequence": 1,
    "language": "Java",
    "solutionCode": "class Solution { ... }",
    "explanation": "Hash map approach",
    "timeComplexity": "O(n)",
    "spaceComplexity": "O(n)",
    "isOptimal": true,
    "createdByUser": {
      "id": "USR_1a2b3c4d",
      "username": "john_doe"
    },
    "createdAt": "2025-01-06T12:00:00Z"
  }
]
```

### 4. 🔍 Get Solutions by Language
Retrieves solutions filtered by programming language.

**`GET /api/v1/solutions/language/{language}`**

### 5. 🏆 Get Optimal Solution
Retrieves the optimal solution for a question.

**`GET /api/v1/solutions/question/{questionId}/optimal`**

### 6. 🔍 Get Solutions by Question and Language
Retrieves solutions for a specific question in a specific language.

**`GET /api/v1/solutions/question/{questionId}/language/{language}`**

### 7. 🏆 Mark Solution as Optimal
Updates a solution's optimal status.

**`PUT /api/v1/solutions/{id}/optimal`**

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `isOptimal` | boolean | ✅ | True to mark as optimal |

---

## 🔧 Common Error Responses

### File Upload Error (400)
```json
{
  "error": {
    "code": "FILE_UPLOAD_ERROR",
    "message": "Invalid file type. Only PNG, JPG, GIF, MP4 are allowed",
    "details": ["allowedTypes: [png, jpg, gif, mp4]"]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### File Size Error (413)
```json
{
  "error": {
    "code": "FILE_TOO_LARGE",
    "message": "File size exceeds maximum limit of 10MB",
    "details": ["maxSize: 10485760 bytes"]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Sequence Conflict (409)
```json
{
  "error": {
    "code": "SEQUENCE_CONFLICT",
    "message": "Sequence number 1 already exists for this question",
    "details": ["questionId: QST_1a2b3c4d", "sequence: 1"]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Complete Media Management Flow
```bash
# 1. Upload media file
curl -X POST "http://localhost:8080/api/v1/media/upload" \
  -F "file=@diagram.png" \
  -F "questionId=QST_1a2b3c4d" \
  -F "sequence=1" \
  -F "mediaType=IMAGE" \
  -F "description=Problem visualization"

# 2. Get media files for question
curl -X GET "http://localhost:8080/api/v1/media/question/QST-100001"

# 3. Download media file
curl -X GET "http://localhost:8080/api/v1/media/MED-100001/download" -o downloaded_diagram.png
```

### Test Case Management
```bash
# 1. Create sample test case
curl -X POST "http://localhost:8080/api/v1/test-cases" \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": "QST-100001",
    "sequence": 1,
    "inputData": "[2,7,11,15]\\n9",
    "expectedOutput": "[0,1]",
    "isSample": true,
    "explanation": "Basic example case"
  }'

# 2. Bulk create test cases
curl -X POST "http://localhost:8080/api/v1/test-cases/bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "testCases": [
      {
        "questionId": "QST-100001",
        "sequence": 2,
        "inputData": "[3,2,4]\\n6",
        "expectedOutput": "[1,2]",
        "isSample": true
      },
      {
        "questionId": "QST-100001",
        "sequence": 3,
        "inputData": "[1000,999,1]\\n2000",
        "expectedOutput": "[0,1]",
        "isSample": false
      }
    ]
  }'

# 3. Get only sample test cases
curl -X GET "http://localhost:8080/api/v1/test-cases/question/QST_1a2b3c4d/sample"
```

### Solution Management
```bash
# 1. Create Java solution
curl -X POST "http://localhost:8080/api/v1/solutions" \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": "QST-100001",
    "sequence": 1,
    "language": "Java",
    "solutionCode": "class Solution { ... }",
    "explanation": "Hash map approach",
    "timeComplexity": "O(n)",
    "spaceComplexity": "O(n)",
    "isOptimal": true
  }'

# 2. Get optimal solution
curl -X GET "http://localhost:8080/api/v1/solutions/question/QST_1a2b3c4d/optimal"

# 3. Get solutions by language
curl -X GET "http://localhost:8080/api/v1/solutions/question/QST_1a2b3c4d/language/Java"
```

---

## 📊 File Management Guidelines

### Supported Media Types
| Type | Extensions | Max Size | Use Case |
|------|------------|----------|----------|
| `IMAGE` | png, jpg, jpeg, gif | 5MB | Diagrams, screenshots |
| `DIAGRAM` | png, jpg, svg | 2MB | Problem illustrations |
| `VIDEO` | mp4, avi, mov | 50MB | Solution explanations |
| `AUDIO` | mp3, wav | 10MB | Voice explanations |
| `DOCUMENT` | pdf, txt | 10MB | Additional resources |

### Programming Languages Supported
- **Java** - Most common interview language
- **Python** - Popular for rapid prototyping
- **C++** - Performance-critical solutions
- **JavaScript** - Web development roles
- **C#** - Microsoft stack positions
- **Go** - Systems programming
- **Rust** - Memory-safe systems code
- **TypeScript** - Type-safe JavaScript
- **Kotlin** - Android development
- **Swift** - iOS development

### Test Case Best Practices
1. **Sample Cases**: 2-3 visible examples showing basic functionality
2. **Edge Cases**: Empty inputs, single elements, maximum constraints
3. **Hidden Cases**: Complex scenarios for thorough validation
4. **Explanation**: Clear reasoning for expected outputs
5. **Sequence**: Order from simple to complex

---

**Last Updated**: January 6, 2025