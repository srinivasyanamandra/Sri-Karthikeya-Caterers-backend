# API Testing Examples

## Menu APIs

### Create Menu
```bash
POST http://localhost:8080/api/menu
Content-Type: application/json

{
  "imageId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Wedding Special Package",
  "price": 599.99,
  "description": "Complete wedding catering package with traditional dishes",
  "items": [
    "Biryani",
    "Paneer Butter Masala",
    "Dal Makhani",
    "Naan",
    "Raita",
    "Gulab Jamun"
  ]
}
```

### Get All Menus
```bash
GET http://localhost:8080/api/menu?page=0&size=10&sortBy=createdAt&sortDir=DESC
```

### Get Menu by ID
```bash
GET http://localhost:8080/api/menu/{id}
```

### Update Menu
```bash
PUT http://localhost:8080/api/menu/{id}
Content-Type: application/json

{
  "imageId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Updated Wedding Package",
  "price": 649.99,
  "description": "Updated description",
  "items": ["Item1", "Item2"]
}
```

### Delete Menu
```bash
DELETE http://localhost:8080/api/menu/{id}
```

## Gallery APIs

### Create Gallery
```bash
POST http://localhost:8080/api/gallery
Content-Type: application/json

{
  "imageId": "660e8400-e29b-41d4-a716-446655440001",
  "type": "SERVICES",
  "name": "Professional Catering Service",
  "description": "Our team providing excellent catering services"
}
```

### Get All Galleries
```bash
GET http://localhost:8080/api/gallery?page=0&size=10
```

## Review APIs

### Create Review
```bash
POST http://localhost:8080/api/reviews
Content-Type: application/json

{
  "imageId": "770e8400-e29b-41d4-a716-446655440002",
  "timeline": "December 2023",
  "guestsCount": 500,
  "stars": 5,
  "comments": "Excellent service and delicious food. Highly recommended!",
  "topPicks": ["FOOD", "SERVICE", "PRESENTATION"],
  "type": "WEDDING"
}
```

### Get All Reviews
```bash
GET http://localhost:8080/api/reviews?page=0&size=10
```

## Quote APIs

### Create Quote (Public)
```bash
POST http://localhost:8080/api/quotes
Content-Type: application/json

{
  "fullName": "John Doe",
  "phoneNumber": "+919876543210",
  "email": "john.doe@example.com",
  "eventDate": "2024-12-25",
  "eventType": "WEDDING",
  "expectedGuests": 300,
  "additionalDetails": "Need vegetarian options only"
}
```

### Get All Quotes
```bash
GET http://localhost:8080/api/quotes?page=0&size=10
```

## Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": "uuid",
    "...": "..."
  }
}
```

### Paginated Response
```json
{
  "success": true,
  "message": "Data retrieved successfully",
  "data": {
    "content": [...],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false
  }
}
```

### Error Response
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed: Name is required",
  "path": "/api/menu"
}
```

## Testing Notes

1. All GET endpoints are public
2. POST /api/quotes is public (for customer quote requests)
3. All other POST/PUT/DELETE endpoints require authentication
4. UUIDs must be in valid format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
5. Pagination: page starts from 0, max size is 100
6. Sort direction: ASC or DESC
