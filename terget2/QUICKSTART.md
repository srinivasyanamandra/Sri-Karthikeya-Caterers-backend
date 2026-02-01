# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Start MongoDB
```bash
# Windows
net start MongoDB

# Linux/Mac
sudo systemctl start mongod
```

### Step 2: Initialize Database
```bash
mongosh < mongodb-init.js
```

### Step 3: Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### Step 4: Test the API
Open browser: http://localhost:8080/swagger-ui.html

## 📝 Quick API Tests

### Test 1: Get All Menus (Public)
```bash
curl http://localhost:8080/api/menu
```

### Test 2: Create Quote (Public)
```bash
curl -X POST http://localhost:8080/api/quotes \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "phoneNumber": "+919876543210",
    "email": "test@example.com",
    "eventDate": "2024-12-31",
    "eventType": "WEDDING",
    "expectedGuests": 200,
    "additionalDetails": "Test quote"
  }'
```

### Test 3: Get All Reviews (Public)
```bash
curl http://localhost:8080/api/reviews?page=0&size=5
```

## 🎯 Key Features

✅ **Clean Architecture** - Separation of concerns  
✅ **MongoDB with MongoTemplate** - No Spring Data Repositories  
✅ **MapStruct** - Automatic DTO mapping  
✅ **Swagger UI** - Interactive API documentation  
✅ **Jakarta Validation** - Input validation  
✅ **Global Exception Handling** - Consistent error responses  
✅ **Pagination** - All list endpoints support pagination  
✅ **Auditing** - Automatic createdAt/updatedAt timestamps  
✅ **Security** - Public GET, Protected POST/PUT/DELETE  

## 📚 Documentation

- **API Docs**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **README**: [README.md](README.md)
- **API Examples**: [API_EXAMPLES.md](API_EXAMPLES.md)
- **Deployment**: [DEPLOYMENT.md](DEPLOYMENT.md)

## 🔧 Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/sri-karthikeya-caterers

server:
  port: 8080
```

## 📦 Project Structure

```
src/main/java/sri/karthikeya/caterers/
├── config/          # Configurations (MongoDB, Security, CORS, OpenAPI)
├── controller/      # REST Controllers
├── dto/            # Request/Response DTOs
├── entity/         # MongoDB Entities
├── enums/          # Enumerations
├── exception/      # Custom Exceptions & Global Handler
├── mapper/         # MapStruct Mappers
├── repository/     # MongoDB Repositories (MongoTemplate)
├── service/        # Business Logic (Interface + Impl)
├── security/       # Spring Security Config
└── util/           # Utility Classes
```

## 🔐 Security Rules

| Endpoint | Method | Access |
|----------|--------|--------|
| `/api/menu` | GET | Public |
| `/api/menu` | POST/PUT/DELETE | Protected |
| `/api/gallery` | GET | Public |
| `/api/gallery` | POST/PUT/DELETE | Protected |
| `/api/reviews` | GET | Public |
| `/api/reviews` | POST/PUT/DELETE | Protected |
| `/api/quotes` | GET | Public |
| `/api/quotes` | POST | Public |
| `/api/quotes` | PUT/DELETE | Protected |

## 🐛 Troubleshooting

### Port 8080 already in use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### MongoDB connection failed
```bash
# Check MongoDB status
mongosh --eval "db.adminCommand('ping')"

# Restart MongoDB
sudo systemctl restart mongod
```

### Build fails
```bash
# Clean and rebuild
mvn clean install -U
```

## 📊 Sample Data

After running `mongodb-init.js`, you'll have:
- 1 sample menu item
- 1 sample gallery item
- 1 sample review
- 1 sample quote

## 🎓 Next Steps

1. ✅ Explore Swagger UI
2. ✅ Test all CRUD operations
3. ✅ Review code structure
4. ✅ Customize for your needs
5. ✅ Add authentication (JWT)
6. ✅ Deploy to production

## 💡 Tips

- Use Swagger UI for interactive testing
- Check logs for debugging: `tail -f logs/application.log`
- MongoDB GUI: MongoDB Compass
- API Testing: Postman or Insomnia
- Code Quality: SonarQube

## 🆘 Need Help?

- Check [README.md](README.md) for detailed documentation
- Review [API_EXAMPLES.md](API_EXAMPLES.md) for request examples
- See [DEPLOYMENT.md](DEPLOYMENT.md) for production setup

---

**Happy Coding! 🎉**
