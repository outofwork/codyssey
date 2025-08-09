# SQL Documentation

This folder contains SQL queries and documentation for the Codyssey application.

## Files

### `article-queries.md`
Comprehensive collection of SQL queries for fetching articles with their associated labels.

**Contents:**
- **Article Queries** (Queries 1-6)
  1. Basic articles with labels
  2. Articles with additional details
  3. Articles grouped with comma-separated labels ⭐ **Most Useful**
  4. Articles with only primary labels
  5. Articles with source information
  6. Specific article by URL slug

- **Database Schema for Articles**
- **Performance Notes and Optimization Tips**

### `question-queries.md`
Comprehensive collection of SQL queries for fetching coding questions with their associated labels.

**Contents:**
- **Question Queries** (Queries 1-8)
  1. Basic questions with labels
  2. Questions with additional details
  3. Questions grouped with comma-separated labels ⭐ **Most Useful**
  4. Questions with only primary labels
  5. Questions with company information
  6. Specific question by URL slug
  7. Questions by difficulty level
  8. Questions by source/platform

- **Database Schema for Questions**
- **Performance Notes and Optimization Tips**

## Quick Access

For the most commonly used queries:

### Most Useful Article Query (Grouped)
```sql
SELECT 
    a.title AS article_name,
    STRING_AGG(l.name, ', ' ORDER BY l.name) AS label_names
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
GROUP BY a.id, a.title
ORDER BY a.title;
```

### Most Useful Question Query (Grouped)
```sql
SELECT 
    cq.title AS question_name,
    STRING_AGG(l.name, ', ' ORDER BY l.name) AS label_names
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
GROUP BY cq.id, cq.title
ORDER BY cq.title;
```

## Database Connection
These queries are designed for PostgreSQL and include Codyssey-specific:
- Custom ID generation (ART-*, QST-*, LBL-*)
- Soft delete patterns (`deleted = false`)
- URL slug fields for SEO-friendly URLs
- Primary label designation (`is_primary`)

## Usage Notes
- Always include `deleted = false` filters
- Use `LEFT JOIN` for optional relationships
- Use `STRING_AGG` for comma-separated lists
- Order by `is_primary DESC` for better UX