# SQL Queries for Coding Questions with Labels

This document contains comprehensive SQL queries for fetching coding questions along with their associated labels from the Codyssey database.

## Table of Contents
- [Question Queries](#question-queries)
- [Database Schema for Questions](#database-schema-for-questions)
- [Performance Notes](#performance-notes)

---

## Question Queries

### 1. Basic Query - All Questions with Their Labels
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
ORDER BY cq.title, l.name;
```

### 2. Questions with Additional Details (Category, Difficulty, Company)
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name,
    lc.name AS category_name,
    lc.code AS category_code,
    dl.name AS difficulty_level,
    s.name AS source_name,
    ql.is_primary AS is_primary_label
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
LEFT JOIN labels dl ON cq.difficulty_label_id = dl.id
LEFT JOIN sources s ON cq.source_id = s.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
ORDER BY cq.title, ql.is_primary DESC, l.name;
```

### 3. Questions Grouped by Question (Comma-separated Labels) ⭐ **Most Useful**
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

### 4. Questions with Only Primary Labels
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
  AND ql.is_primary = true
ORDER BY cq.title, l.name;
```

### 5. Questions with Company Information
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name,
    s.name AS source_name,
    lc.code AS category_code,
    STRING_AGG(DISTINCT comp.name, ', ') AS companies
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
LEFT JOIN sources s ON cq.source_id = s.id
LEFT JOIN question_companies qc ON cq.id = qc.question_id
LEFT JOIN companies comp ON qc.company_id = comp.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
GROUP BY cq.id, cq.title, l.id, l.name, s.name, lc.code
ORDER BY cq.title, l.name;
```

### 6. Questions for a Specific Question (by URL slug)
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name,
    ql.is_primary AS is_primary_label,
    lc.name AS category_name
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
WHERE cq.url_slug = 'two-sum-leetcode'  -- Replace with your question slug
  AND cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
ORDER BY ql.is_primary DESC, l.name;
```

### 7. Questions by Difficulty Level
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name,
    dl.name AS difficulty_level
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
LEFT JOIN labels dl ON cq.difficulty_label_id = dl.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
  AND dl.name = 'Easy'  -- Replace with Easy/Medium/Hard
ORDER BY cq.title, l.name;
```

### 8. Questions by Source/Platform
```sql
SELECT 
    cq.title AS question_name,
    l.name AS label_name,
    s.name AS source_name
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN sources s ON cq.source_id = s.id
WHERE cq.deleted = false 
  AND ql.deleted = false 
  AND l.deleted = false
  AND s.code = 'LEETCODE'  -- Replace with LEETCODE/HACKERRANK/GEEKSFORGEEKS/etc.
ORDER BY cq.title, l.name;
```

---

## Database Schema for Questions

### Core Tables
- **`coding_questions`** - Main coding questions table
- **`labels`** - Labels/tags that can be applied to questions
- **`label_categories`** - Categories for organizing labels (e.g., DATA_STRUCTURES, ALGORITHMS)
- **`sources`** - Sources like LeetCode, HackerRank, GeeksforGeeks
- **`companies`** - Companies that ask these questions in interviews

### Relationship Tables
- **`question_labels`** - Many-to-many relationship between questions and labels
- **`question_companies`** - Many-to-many relationship between questions and companies

### Key Relationships
```
coding_questions ──┐
                   ├── question_labels ── labels ── label_categories
                   ├── question_companies ── companies
                   ├── sources
                   └── labels (for difficulty_label_id)
```

### Coding Questions Table Structure
```sql
coding_questions:
- id (Primary Key, format: QST-100001)
- title
- short_description
- file_path
- url_slug (SEO-friendly URL)
- status (ACTIVE, DRAFT, DEPRECATED)
- source_id (Foreign Key to sources)
- difficulty_label_id (Foreign Key to labels for Easy/Medium/Hard)
- platform_question_id (Original ID from the source platform)
- original_url (Link to original question)
- created_at, updated_at
- deleted (boolean for soft delete)
- version (optimistic locking)
```

### Question Labels Table Structure
```sql
question_labels:
- id (Primary Key, format: QLB-100001)
- question_id (Foreign Key to coding_questions)
- label_id (Foreign Key to labels)
- is_primary (boolean - one primary label per question)
- notes (optional description)
- deleted, created_at, updated_at, version
```

### Question Companies Table Structure
```sql
question_companies:
- id (Primary Key, format: QCO-100001)
- question_id (Foreign Key to coding_questions)
- company_id (Foreign Key to companies)
- frequency_score (1-10 scale, how often asked)
- last_asked_date
- notes
- deleted, created_at, updated_at, version
```

### Companies Table Structure
```sql
companies:
- id (Primary Key, format: COM-100001)
- name
- description
- website_url
- industry
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
- `difficulty_label_id` is indexed for difficulty filtering

### Query Optimization Tips
1. **Always filter by `deleted = false`** on all tables to exclude soft-deleted records
2. **Use LEFT JOIN** when the relationship might not exist (e.g., difficulty labels, companies)
3. **Use DISTINCT** when joining multiple many-to-many relationships to avoid duplicates
4. **Order by primary labels first** using `is_primary DESC` for better UX
5. **Use STRING_AGG** for comma-separated lists instead of multiple rows

### Example Usage Patterns

#### Find all Easy LeetCode questions
```sql
SELECT cq.title, STRING_AGG(l.name, ', ') AS labels
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN sources s ON cq.source_id = s.id
JOIN labels dl ON cq.difficulty_label_id = dl.id
WHERE cq.deleted = false AND ql.deleted = false AND l.deleted = false
  AND s.code = 'LEETCODE' AND dl.name = 'Easy'
GROUP BY cq.id, cq.title;
```

#### Find questions by specific company
```sql
SELECT cq.title, STRING_AGG(l.name, ', ') AS labels, comp.name AS company
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN question_companies qc ON cq.id = qc.question_id
JOIN companies comp ON qc.company_id = comp.id
WHERE cq.deleted = false AND ql.deleted = false AND l.deleted = false
  AND comp.name = 'Google'
GROUP BY cq.id, cq.title, comp.name;
```

#### Find questions by algorithm type
```sql
SELECT cq.title, STRING_AGG(l.name, ', ') AS labels
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN label_categories lc ON l.category_id = lc.id
WHERE cq.deleted = false AND ql.deleted = false AND l.deleted = false
  AND lc.code = 'ALGORITHMS'
  AND l.name ILIKE '%sort%'
GROUP BY cq.id, cq.title;
```

#### Find most frequently asked questions by companies
```sql
SELECT 
    cq.title AS question_name,
    comp.name AS company_name,
    qc.frequency_score,
    STRING_AGG(l.name, ', ') AS labels
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
JOIN question_companies qc ON cq.id = qc.question_id
JOIN companies comp ON qc.company_id = comp.id
WHERE cq.deleted = false AND ql.deleted = false AND l.deleted = false
  AND qc.frequency_score >= 8
GROUP BY cq.id, cq.title, comp.name, qc.frequency_score
ORDER BY qc.frequency_score DESC, cq.title;
```

#### Find questions with specific difficulty and company combination
```sql
SELECT 
    cq.title AS question_name,
    dl.name AS difficulty,
    comp.name AS company,
    STRING_AGG(l.name, ', ') AS labels
FROM coding_questions cq
JOIN question_labels ql ON cq.id = ql.question_id
JOIN labels l ON ql.label_id = l.id
LEFT JOIN labels dl ON cq.difficulty_label_id = dl.id
LEFT JOIN question_companies qc ON cq.id = qc.question_id
LEFT JOIN companies comp ON qc.company_id = comp.id
WHERE cq.deleted = false AND ql.deleted = false AND l.deleted = false
  AND dl.name = 'Medium'
  AND comp.name = 'Microsoft'
GROUP BY cq.id, cq.title, dl.name, comp.name;
```

---

## Notes
- Replace placeholder values in WHERE clauses with actual values for your queries
- All queries include proper soft delete filtering
- Queries are optimized for PostgreSQL database
- Use `ILIKE` instead of `LIKE` for case-insensitive searches in PostgreSQL
- The **Query #3 (Grouped)** is most useful for displaying questions with all their labels in one row
- **Difficulty levels** are stored as labels: Easy, Medium, Hard
- **Company relationships** are optional - not all questions have company associations