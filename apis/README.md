# 📚 Codyssey API Documentation

Welcome to the Codyssey API documentation! This folder contains comprehensive documentation for all API endpoints organized by category.

## 📖 Base Information

- **Base URL**: `http://localhost:8080/api`
- **API Version**: v1
- **Content-Type**: `application/json`
- **Authentication**: Currently disabled (all endpoints are public)

## 📁 Documentation Structure

### 🔗 Quick Links
- [User Management APIs](./01-user-management.md) - User registration, authentication, and profile management
- [Label Categories APIs](./02-label-categories.md) - Label category management
- [Labels APIs](./03-labels.md) - Hierarchical label management
- [Coding Questions APIs](./04-coding-questions.md) - Core question management
- [Question Relations APIs](./05-question-relations.md) - Links, labels, and company associations
- [Media & Test Cases APIs](./06-media-testcases.md) - File management and test case operations

### 🌐 Interactive Documentation
- **Swagger UI**: [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/api/v3/api-docs](http://localhost:8080/api/v3/api-docs)
- **Health Check**: [http://localhost:8080/api/actuator/health](http://localhost:8080/api/actuator/health)

## 🚀 Getting Started

1. **Start the Application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Test the Health Endpoint**:
   ```bash
   curl http://localhost:8080/api/actuator/health
   ```

3. **Access Swagger UI** for interactive testing:
   ```
   http://localhost:8080/api/swagger-ui/index.html
   ```

## 📊 API Categories Overview

| Category | Endpoints | Description |
|----------|-----------|-------------|
| **User Management** | 9 endpoints | User registration, profile management, validation |
| **Label Categories** | 7 endpoints | Category creation and management |
| **Labels** | 15 endpoints | Hierarchical label system with parent-child relationships |
| **Coding Questions** | 16 endpoints | Core question CRUD operations and search |
| **Question Relations** | 35+ endpoints | Links, company associations, label mappings |
| **Media & Test Cases** | 30+ endpoints | File management, test case operations |

## 🔧 Common Response Formats

### Success Response
```json
{
  "data": { /* response data */ },
  "status": "success",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

### Error Response
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [ /* validation errors */ ]
  },
  "status": "error",
  "timestamp": "2025-01-06T12:00:00Z"
}
```

## 📝 Notes

- All timestamps are in ISO 8601 format
- Pagination uses standard Spring Boot parameters (`page`, `size`, `sort`)
- All endpoints include comprehensive validation
- Error messages provide detailed information for debugging
- The API follows RESTful conventions

---

**Last Updated**: January 6, 2025  
**API Version**: 1.0.0