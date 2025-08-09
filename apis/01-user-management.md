# 👤 User Management APIs

Complete documentation for user registration, authentication, and profile management endpoints.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Path** | `/api/v1/users` |
| **Total Endpoints** | 9 |
| **Authentication** | None (currently disabled) |
| **Content-Type** | `application/json` |

---

## 📚 API Endpoints

### 1. 🆕 Register New User
Creates a new user account with validation.

**`POST /api/v1/users/register`**

#### Request Body
```json
{
  "username": "john_doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `username` | ✅ | Custom validation (see UserService) |
| `email` | ✅ | Valid email format |
| `password` | ✅ | Min 8 chars, must contain: lowercase, uppercase, digit, special char (@$!%*?&) |
| `firstName` | ❌ | Max 50 chars, letters/spaces/apostrophes/hyphens only |
| `lastName` | ❌ | Max 50 chars, letters/spaces/apostrophes/hyphens only |

#### Response (201 Created)
```json
{
  "id": "ACC-100001",
  "username": "john_doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": true,
  "roles": ["USER"],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **409 Conflict**: Username or email already exists

---

### 2. 📋 Get All Users
Retrieves a list of all users in the system.

**`GET /api/v1/users`**

#### Response (200 OK)
```json
[
  {
    "id": "ACC-100001",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "enabled": true,
    "roles": ["USER"],
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z"
  }
]
```

---

### 3. 🔍 Get User by ID
Retrieves a specific user by their unique ID.

**`GET /api/v1/users/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | User ID (e.g., "ACC-100001") |

#### Response (200 OK)
```json
{
  "id": "ACC-100001",
  "username": "john_doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": true,
  "roles": ["USER"],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:00:00Z"
}
```

#### Error Responses
- **404 Not Found**: User not found

---

### 4. 🔍 Get User by Username
Retrieves a user by their username.

**`GET /api/v1/users/username/{username}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `username` | string | Username (e.g., "john_doe") |

#### Response (200 OK)
Same as Get User by ID response.

#### Error Responses
- **404 Not Found**: User not found

---

### 5. ✏️ Update User
Updates user information (username cannot be changed).

**`PUT /api/v1/users/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | User ID to update |

#### Request Body
```json
{
  "email": "john.updated@example.com",
  "password": "NewSecurePass456!",
  "firstName": "Jonathan",
  "lastName": "Smith",
  "enabled": true
}
```

#### Field Details
| Field | Required | Notes |
|-------|----------|-------|
| `email` | ❌ | Must be valid email if provided |
| `password` | ❌ | Only update if provided, same validation as registration |
| `firstName` | ❌ | Max 50 chars |
| `lastName` | ❌ | Max 50 chars |
| `enabled` | ❌ | Boolean to enable/disable account |

#### Response (200 OK)
```json
{
  "id": "ACC-100001",
  "username": "john_doe",
  "email": "john.updated@example.com",
  "firstName": "Jonathan",
  "lastName": "Smith",
  "enabled": true,
  "roles": ["USER"],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T12:30:00Z"
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **404 Not Found**: User not found

---

### 6. 🗑️ Delete User
Soft deletes a user (marks as inactive rather than permanent deletion).

**`DELETE /api/v1/users/{id}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | User ID to delete |

#### Response (204 No Content)
No response body.

#### Error Responses
- **404 Not Found**: User not found

---

### 7. 🔄 Toggle User Status
Enable or disable a user account.

**`PATCH /api/v1/users/{id}/enabled`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | User ID |

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `enabled` | boolean | ✅ | True to enable, false to disable |

#### Example Request
```
PATCH /api/v1/users/USR_1a2b3c4d/enabled?enabled=false
```

#### Response (200 OK)
```json
{
  "id": "ACC-100001",
  "username": "john_doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": false,
  "roles": ["USER"],
  "createdAt": "2025-01-06T12:00:00Z",
  "updatedAt": "2025-01-06T13:00:00Z"
}
```

---

### 8. ✅ Check Username Availability
Verifies if a username is available for registration.

**`GET /api/v1/users/check-username/{username}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `username` | string | Username to check |

#### Response (200 OK)
```json
{
  "available": true,
  "username": "new_username"
}
```

#### Alternative Response (Username Taken)
```json
{
  "available": false,
  "username": "existing_username"
}
```

---

### 9. ✅ Check Email Availability
Verifies if an email is available for registration.

**`GET /api/v1/users/check-email/{email}`**

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `email` | string | Email to check (URL encoded) |

#### Example Request
```
GET /api/v1/users/check-email/test%40example.com
```

#### Response (200 OK)
```json
{
  "available": true,
  "email": "test@example.com"
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
        "field": "password",
        "message": "Password must contain at least one uppercase letter"
      }
    ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### User Not Found (404)
```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "User not found with id: ACC-invalid",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Username/Email Conflict (409)
```json
{
  "error": {
    "code": "DUPLICATE_RESOURCE",
    "message": "Username 'john_doe' already exists",
    "details": []
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

---

## 📝 Usage Examples

### Complete User Registration Flow
```bash
# 1. Check username availability
curl -X GET "http://localhost:8080/api/v1/users/check-username/john_doe"

# 2. Check email availability  
curl -X GET "http://localhost:8080/api/v1/users/check-email/john%40example.com"

# 3. Register user
curl -X POST "http://localhost:8080/api/v1/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com", 
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Update User Profile
```bash
curl -X PUT "http://localhost:8080/api/v1/users/ACC-100001" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jonathan",
    "lastName": "Smith",
    "email": "jonathan.smith@example.com"
  }'
```

---

**Last Updated**: January 6, 2025