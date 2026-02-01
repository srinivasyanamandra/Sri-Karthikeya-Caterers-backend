# Sri Karthikeya Caterers - Backend API

Production-grade Spring Boot backend following Clean Architecture principles for a public-facing catering business website.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.2**
- **MongoDB** (with MongoTemplate + Criteria API)
- **Spring Security** (Stateless, JWT-ready)
- **MapStruct** (Entity-DTO mapping)
- **Swagger/OpenAPI 3** (API documentation)
- **Jakarta Validation** (Input validation)
- **Lombok** (Boilerplate reduction)

## Architecture

### Clean Architecture Layers

```
├── Controller Layer    → HTTP handling, validation, Swagger docs
├── Service Layer       → Business logic, workflows
├── Repository Layer    → MongoDB operations (MongoTemplate only)
├── Entity Layer        → Domain models
├── DTO Layer          → Request/Response objects
├── Mapper Layer       → MapStruct mappers
├── Exception Layer    → Custom exceptions + global handler
├── Security Layer     → Spring Security configuration
└── Config Layer       → Application configurations
```

## Project Structure

```
sri.karthikeya.caterers
├── config/
│   ├── CorsConfig.java
│   ├── MongoConfig.java
│   └── OpenApiConfig.java
├── controller/
│   ├── MenuController.java
│   ├── GalleryController.java
│   ├── ReviewController.java
│   └── QuoteController.java
├── dto/
│   ├── request/
│   │   ├── MenuRequest.java
│   │   ├── GalleryRequest.java
│   │   ├── ReviewRequest.java
│   │   └── QuoteRequest.java
│   └── response/
│       ├── ApiResponse.java
│       ├── PageResponse.java
│       ├── ErrorResponse.java
│       ├── MenuResponse.java
│       ├── GalleryResponse.java
│       ├── ReviewResponse.java
│       └── QuoteResponse.java
├── entity/
│   ├── BaseEntity.java
│   ├── Menu.java
│   ├── Gallery.java
│   ├── Review.java
│   └── Quote.java
├── enums/
│   ├── GalleryType.java
│   ├── ReviewEventType.java
│   └── TopPicks.java
├── exception/
│   ├── custom/
│   │   ├── ValidationException.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── BadRequestException.java
│   │   └── InternalServerException.java
│   └── handler/
│       └── GlobalExceptionHandler.java
├── mapper/
│   ├── MenuMapper.java
│   ├── GalleryMapper.java
│   ├── ReviewMapper.java
│   └── QuoteMapper.java
├── repository/
│   ├── MenuRepository.java
│   ├── GalleryRepository.java
│   ├── ReviewRepository.java
│   └── QuoteRepository.java
├── service/
│   ├── MenuService.java
│   ├── GalleryService.java
│   ├── ReviewService.java
│   ├── QuoteService.java
│   └── impl/
│       ├── MenuServiceImpl.java
│       ├── GalleryServiceImpl.java
│       ├── ReviewServiceImpl.java
│       └── QuoteServiceImpl.java
├── security/
│   └── SecurityConfig.java
└── util/
    └── ValidationUtil.java
```

## MongoDB Collections

### menu
```json
{
  "_id": "UUID",
  "imageId": "UUID",
  "name": "string",
  "price": "double",
  "description": "text",
  "items": ["string"],
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### gallery
```json
{
  "_id": "UUID",
  "imageId": "UUID",
  "type": "MENU | SERVICES | TEAM | REVIEWS",
  "name": "string",
  "description": "text",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### reviews
```json
{
  "_id": "UUID",
  "imageId": "UUID",
  "timeline": "string",
  "guestsCount": "number",
  "stars": "1-5",
  "comments": "string",
  "topPicks": ["FOOD", "SERVICE", "PRESENTATION", "VALUE", "HYGIENE"],
  "type": "WEDDING | PRIVATE_PARTY | CORPORATE_LUNCH | POLITICAL_EVENT | RELIGIOUS_EVENT",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### quotes
```json
{
  "_id": "UUID",
  "fullName": "string",
  "phoneNumber": "string",
  "email": "string",
  "eventDate": "date",
  "eventType": "enum",
  "expectedGuests": "number",
  "additionalDetails": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

## API Endpoints

### Menu APIs
- `POST /api/menu` - Create menu (Protected)
- `GET /api/menu/{id}` - Get menu by ID (Public)
- `GET /api/menu` - Get all menus with pagination (Public)
- `PUT /api/menu/{id}` - Update menu (Protected)
- `DELETE /api/menu/{id}` - Delete menu (Protected)

### Gallery APIs
- `POST /api/gallery` - Create gallery item (Protected)
- `GET /api/gallery/{id}` - Get gallery by ID (Public)
- `GET /api/gallery` - Get all galleries with pagination (Public)
- `PUT /api/gallery/{id}` - Update gallery (Protected)
- `DELETE /api/gallery/{id}` - Delete gallery (Protected)

### Review APIs
- `POST /api/reviews` - Create review (Protected)
- `GET /api/reviews/{id}` - Get review by ID (Public)
- `GET /api/reviews` - Get all reviews with pagination (Public)
- `PUT /api/reviews/{id}` - Update review (Protected)
- `DELETE /api/reviews/{id}` - Delete review (Protected)

### Quote APIs
- `POST /api/quotes` - Create quote request (Public)
- `GET /api/quotes/{id}` - Get quote by ID (Public)
- `GET /api/quotes` - Get all quotes with pagination (Public)
- `PUT /api/quotes/{id}` - Update quote (Protected)
- `DELETE /api/quotes/{id}` - Delete quote (Protected)

## Security Configuration

- **Public Endpoints**: All GET requests, POST /api/quotes
- **Protected Endpoints**: All POST/PUT/DELETE (except quotes POST)
- **CSRF**: Disabled (stateless API)
- **Session Management**: Stateless
- **CORS**: Enabled for all origins (configure for production)

## Validation Rules

### Common
- UUID format validation for all IDs
- Null and empty checks
- String length limits
- XSS prevention through sanitization

### Menu
- Name: 2-100 characters
- Price: 0.01 - 999999.99
- Description: max 1000 characters
- Items: 1-50 items, each max 200 characters

### Gallery
- Name: 2-100 characters
- Description: max 1000 characters
- Type: Valid enum value

### Review
- Stars: 1-5
- Guests: 1-100000
- Comments: max 2000 characters
- Top picks: 1-5 items

### Quote
- Phone: 10-15 digits
- Email: Valid email format
- Event date: Future date
- Guests: 1-100000

## Pagination

All GET all endpoints support:
- `page` (default: 0)
- `size` (default: 10, max: 100)
- `sortBy` (default: createdAt)
- `sortDir` (default: DESC)

Response format:
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

## Error Handling

All errors return consistent format:
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/menu"
}
```

HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request / Validation Error
- `404` - Resource Not Found
- `409` - Duplicate Resource
- `500` - Internal Server Error

## Setup & Run

### Prerequisites
- Java 21
- Maven 3.8+
- MongoDB 6.0+

### Installation

1. Clone the repository
2. Start MongoDB:
   ```bash
   mongod --dbpath /path/to/data
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Development Guidelines

### Code Standards
- Classes: PascalCase
- Methods/Variables: camelCase
- Constants: UPPER_SNAKE_CASE
- Max function length: 20 lines
- Max nesting depth: 3

### Best Practices
- Always use MapStruct for mappings
- Never expose entities in controllers
- Use MongoTemplate + Criteria API only
- Validate all inputs
- Log all operations
- Handle all edge cases
- Write null-safe code

## Production Checklist

- [ ] Configure production MongoDB URI
- [ ] Set up authentication (JWT/OAuth2)
- [ ] Configure CORS for specific origins
- [ ] Enable HTTPS
- [ ] Set up rate limiting
- [ ] Configure logging levels
- [ ] Set up monitoring (Actuator)
- [ ] Configure backup strategy
- [ ] Set up CI/CD pipeline
- [ ] Security audit
- [ ] Load testing
- [ ] Documentation review

## License

Proprietary - Sri Karthikeya Caterers
