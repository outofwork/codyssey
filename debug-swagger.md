# Swagger UI Debugging Guide

## Current Status ✅
- Swagger UI is accessible and loads properly
- All API endpoints work correctly
- All documentation annotations are in place

## Issue 🔍
OpenAPI JSON generation fails with 500 error at `/api/v3/api-docs`

## Troubleshooting Steps

### 1. Check Application Logs
```bash
# Check for errors in the application logs
tail -f target/spring-boot.log
# or check console output when starting with mvn spring-boot:run
```

### 2. Test with Minimal Controller
Create a simple test controller to isolate the issue:

```java
@RestController
@RequestMapping("/v1/health")
public class HealthController {
    
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
```

### 3. Check for Circular Dependencies
The 500 error might be caused by:
- Circular references in DTOs
- Complex validation annotations
- Nested object structures

### 4. Alternative Swagger Configuration
Try disabling certain features:

```properties
# Add to application.properties
springdoc.default-produces-media-type=application/json
springdoc.default-consumes-media-type=application/json
springdoc.swagger-ui.disable-swagger-default-url=true
```

### 5. Browser Testing
1. Open: http://localhost:8080/api/swagger-ui/index.html
2. Check browser console for errors
3. Try using the "Explore" field with individual endpoint URLs

### 6. Manual API Testing
All endpoints are working, so you can test manually:
- Users: http://localhost:8080/api/v1/users
- Articles: http://localhost:8080/api/v1/articles
- Labels: http://localhost:8080/api/v1/labels
- Sources: http://localhost:8080/api/v1/sources

## Working Features ✅
- ✅ Swagger UI Interface loads correctly
- ✅ All REST API endpoints functional
- ✅ JavaDoc + Swagger annotations complete
- ✅ Security configuration allows Swagger access
- ✅ Dependencies properly configured

## Browser Access
Open in your browser: http://localhost:8080/api/swagger-ui/index.html
