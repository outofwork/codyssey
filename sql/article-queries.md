# SQL Queries for Articles with Labels

This document contains comprehensive SQL queries for fetching articles along with their associated labels from the Codyssey database.

## Table of Contents
- [Article Queries](#article-queries)
- [Database Schema for Articles](#database-schema-for-articles)
- [Performance Notes](#performance-notes)

---

## Article Queries

### 1. Basic Query - All Articles with Their Labels
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
ORDER BY a.title, l.name;
```

### 2. Articles with Additional Details (Category, Primary/Secondary)
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name,
    lc.name AS category_name,
    lc.code AS category_code,
    al.is_primary AS is_primary_label,
    al.relevance_score
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
WHERE a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
ORDER BY a.title, al.is_primary DESC, l.name;
```

### 3. Articles Grouped by Article (Comma-separated Labels) ⭐ **Most Useful**
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

### 4. Articles with Only Primary Labels
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
  AND al.is_primary = true
ORDER BY a.title, l.name;
```

### 5. Articles with Source Information
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name,
    s.name AS source_name,
    lc.code AS category_code
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
JOIN sources s ON a.source_id = s.id
WHERE a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
ORDER BY a.title, l.name;
```

### 6. Articles for a Specific Article (by URL slug)
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name,
    al.is_primary AS is_primary_label
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.url_slug = 'trees-internal'  -- Replace with your article slug
  AND a.deleted = false 
  AND al.deleted = false 
  AND l.deleted = false
ORDER BY al.is_primary DESC, l.name;
```

---

## Database Schema for Articles

### Core Tables
- **`articles`** - Main articles table
- **`labels`** - Labels/tags that can be applied to articles
- **`label_categories`** - Categories for organizing labels (e.g., DATA_STRUCTURES, ALGORITHMS)
- **`sources`** - Sources like Internal, LeetCode, HackerRank, GeeksforGeeks

### Relationship Tables
- **`article_labels`** - Many-to-many relationship between articles and labels

### Key Relationships
```
articles ──┐
           ├── article_labels ── labels ── label_categories
           └── sources
```

### Article Table Structure
```sql
articles:
- id (Primary Key, format: ART-100001)
- title
- short_description
- file_path
- url_slug (SEO-friendly URL)
- status (ACTIVE, DRAFT, DEPRECATED)
- source_id (Foreign Key to sources)
- created_at, updated_at
- deleted (boolean for soft delete)
- version (optimistic locking)
```

### Article Labels Table Structure
```sql
article_labels:
- id (Primary Key, format: ALB-100001)
- article_id (Foreign Key to articles)
- label_id (Foreign Key to labels)
- is_primary (boolean - one primary label per article)
- relevance_score (1-10 scale)
- notes (optional description)
- deleted, created_at, updated_at, version
```

### Labels Table Structure
```sql
labels:
- id (Primary Key, format: LBL-100001)
- name
- description
- category_id (Foreign Key to label_categories)
- parent_id (Self-reference for hierarchical labels)
- url_slug
- active (boolean)
- deleted, created_at, updated_at, version
```

---

## Performance Notes

### Indexing
- All foreign key columns are automatically indexed
- `deleted` columns are indexed for efficient soft delete filtering
- `url_slug` columns are indexed for slug-based lookups
- `is_primary` field is indexed for primary label queries

### Query Optimization Tips
1. **Always filter by `deleted = false`** on all tables to exclude soft-deleted records
2. **Use LEFT JOIN** when the relationship might not exist
3. **Use DISTINCT** when joining multiple many-to-many relationships to avoid duplicates
4. **Order by primary labels first** using `is_primary DESC` for better UX
5. **Use STRING_AGG** for comma-separated lists instead of multiple rows

### Example Usage Patterns

#### Find all articles about trees
```sql
SELECT a.title, STRING_AGG(l.name, ', ') AS labels
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.deleted = false AND al.deleted = false AND l.deleted = false
  AND l.name ILIKE '%tree%'
GROUP BY a.id, a.title;
```

#### Find articles by specific source
```sql
SELECT a.title, STRING_AGG(l.name, ', ') AS labels
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
JOIN sources s ON a.source_id = s.id
WHERE a.deleted = false AND al.deleted = false AND l.deleted = false
  AND s.code = 'INTERNAL'
GROUP BY a.id, a.title;
```

#### Find articles by category
```sql
SELECT a.title, STRING_AGG(l.name, ', ') AS labels
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
WHERE a.deleted = false AND al.deleted = false AND l.deleted = false
  AND lc.code = 'DATA_STRUCTURES'
GROUP BY a.id, a.title;
```

#### Get article with highest relevance labels
```sql
SELECT 
    a.title AS article_name,
    l.name AS label_name,
    al.relevance_score
FROM articles a
JOIN article_labels al ON a.id = al.article_id
JOIN labels l ON al.label_id = l.id
WHERE a.deleted = false AND al.deleted = false AND l.deleted = false
  AND al.relevance_score >= 8
ORDER BY a.title, al.relevance_score DESC;
```

---

## Notes
- Replace placeholder values in WHERE clauses with actual values for your queries
- All queries include proper soft delete filtering
- Queries are optimized for PostgreSQL database
- Use `ILIKE` instead of `LIKE` for case-insensitive searches in PostgreSQL
- The **Query #3 (Grouped)** is most useful for displaying articles with all their labels in one row