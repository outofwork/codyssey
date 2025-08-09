# 🔐 Authentication APIs

Complete documentation for user authentication and JWT token management endpoints.

## 📋 Overview

| Aspect | Details |
|--------|---------|
| **Base Path** | `/api/v1/auth` |
| **Total Endpoints** | 4 |
| **Authentication** | JWT Bearer Token |
| **Content-Type** | `application/json` |

---

## 📚 API Endpoints

### 1. 🔑 User Login
Authenticates user credentials and returns JWT token.

**`POST /api/v1/auth/login`**

#### Request Body
```json
{
  "usernameOrEmail": "john_doe",
  "password": "SecurePass123!"
}
```

#### Field Validations
| Field | Required | Rules |
|-------|----------|-------|
| `usernameOrEmail` | ✅ | Username or valid email format |
| `password` | ✅ | User's password |

#### Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJBQ0MtMTAwMDAxIiwidXNlcm5hbWUiOiJqb2huX2RvZSIsImVtYWlsIjoiam9obi5kb2VAZXhhbXBsZS5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiZW5hYmxlZCI6dHJ1ZSwiaXNzIjoiY29keXNzZXktYXBpIiwiaWF0IjoxNjQxMDI0MDAwLCJleHAiOjE2NDExMTA0MDB9.signature",
  "type": "Bearer",
  "user": {
    "id": "ACC-100001",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "enabled": true,
    "roles": ["ROLE_USER"],
    "createdAt": "2025-01-06T12:00:00Z",
    "updatedAt": "2025-01-06T12:00:00Z"
  },
  "expiresIn": 86400
}
```

#### Error Responses
- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Invalid credentials or account disabled

---

### 2. 🚪 User Logout
Logout user (client-side token invalidation).

**`POST /api/v1/auth/logout`**

#### Headers
```
Authorization: Bearer <jwt_token>
```

#### Response (200 OK)
```json
{
  "message": "Logout successful",
  "instruction": "Please discard the JWT token on the client side"
}
```

#### Error Responses
- **401 Unauthorized**: Invalid or expired token

---

### 3. 👤 Get Current User
Validates JWT token and returns current user information.

**`GET /api/v1/auth/me`**

#### Headers
```
Authorization: Bearer <jwt_token>
```

#### Response (200 OK)
```json
{
  "username": "john_doe",
  "userId": "ACC-100001",
  "authorities": [
    {
      "authority": "ROLE_USER"
    }
  ],
  "authenticated": true
}
```

#### Error Responses
- **401 Unauthorized**: Invalid or expired token

---

### 4. ❤️ Health Check
Checks if authentication service is running.

**`GET /api/v1/auth/health`**

#### Response (200 OK)
```json
{
  "status": "UP",
  "service": "Authentication Service",
  "timestamp": "2025-01-06T12:00:00.123Z"
}
```

---

## 🔒 JWT Token Details

### Token Structure
```json
{
  "userId": "ACC-100001",
  "username": "john_doe",
  "email": "john.doe@example.com",
  "roles": ["ROLE_USER"],
  "enabled": true,
  "iss": "codyssey-api",
  "iat": 1641024000,
  "exp": 1641110400
}
```

### Token Configuration
- **Algorithm**: HS256
- **Expiration**: 24 hours (86400 seconds)
- **Issuer**: codyssey-api
- **Format**: Bearer Token

### Using JWT Token
Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 🔐 Protected Endpoints

After successful login, include the JWT token to access these endpoints:

### User Management
- `GET /api/v1/users/*` (except registration and checks)
- `PUT /api/v1/users/*`
- `DELETE /api/v1/users/*`

### Articles
- `GET /api/v1/articles/*`
- `POST /api/v1/articles/*`
- `PUT /api/v1/articles/*`
- `DELETE /api/v1/articles/*`

### Questions
- `GET /api/v1/questions/*`
- `POST /api/v1/questions/*`
- `PUT /api/v1/questions/*`
- `DELETE /api/v1/questions/*`

### Labels & Sources
- `GET /api/v1/labels/*`
- `POST /api/v1/labels/*`
- `PUT /api/v1/labels/*`
- `DELETE /api/v1/labels/*`
- Similar for `/api/v1/sources/*`

---

## 🛡️ Security Features

✅ **Stateless Authentication**: No server-side session storage  
✅ **Token Expiration**: Automatic token expiry after 24 hours  
✅ **Role-Based Access**: Support for multiple user roles  
✅ **Secure Password**: BCrypt password hashing  
✅ **JWT Validation**: Comprehensive token validation  
✅ **Error Handling**: Secure error messages  

---

## 🔧 Integration Examples

### Login and Use Token
```bash
# 1. Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "john_doe",
    "password": "SecurePass123!"
  }'

# 2. Use returned token
export TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# 3. Access protected endpoint
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer $TOKEN"
```

### JavaScript Example
```javascript
// Login
const loginResponse = await fetch('/api/v1/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    usernameOrEmail: 'john_doe',
    password: 'SecurePass123!'
  })
});

const authData = await loginResponse.json();
const token = authData.token;

// Use token for subsequent requests
const usersResponse = await fetch('/api/v1/users', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```
