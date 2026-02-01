# Deployment Guide

## Prerequisites

- Java 21 JDK
- Maven 3.8+
- MongoDB 6.0+
- 2GB RAM minimum
- 10GB disk space

## Local Development Setup

### 1. Install MongoDB

**Windows:**
```bash
# Download from https://www.mongodb.com/try/download/community
# Install and start MongoDB service
net start MongoDB
```

**Linux/Mac:**
```bash
# Install MongoDB
sudo apt-get install mongodb  # Ubuntu/Debian
brew install mongodb-community  # macOS

# Start MongoDB
sudo systemctl start mongod  # Linux
brew services start mongodb-community  # macOS
```

### 2. Verify MongoDB Connection

```bash
mongosh
> use sri-karthikeya-caterers
> db.stats()
```

### 3. Build the Application

```bash
cd sri-karthikeya-caterers-backend
mvn clean install -DskipTests
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 5. Verify Application

- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## Production Deployment

### 1. Update Configuration

Create `application-prod.yaml`:

```yaml
spring:
  application:
    name: sri-karthikeya-caterers
  data:
    mongodb:
      uri: mongodb://<username>:<password>@<host>:<port>/sri-karthikeya-caterers?authSource=admin

server:
  port: 8080
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_PASSWORD}
    key-store-type: PKCS12

logging:
  level:
    sri.karthikeya.caterers: INFO
    org.springframework: WARN
  file:
    name: /var/log/sri-karthikeya-caterers/application.log
```

### 2. Build Production JAR

```bash
mvn clean package -DskipTests -Pprod
```

### 3. Deploy Options

#### Option A: Systemd Service (Linux)

Create `/etc/systemd/system/sri-karthikeya-caterers.service`:

```ini
[Unit]
Description=Sri Karthikeya Caterers API
After=syslog.target network.target

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /opt/sri-karthikeya-caterers/app.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Start service:
```bash
sudo systemctl daemon-reload
sudo systemctl enable sri-karthikeya-caterers
sudo systemctl start sri-karthikeya-caterers
sudo systemctl status sri-karthikeya-caterers
```

#### Option B: Docker Deployment

Create `Dockerfile`:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY ../target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

Build and run:
```bash
docker build -t sri-karthikeya-caterers:latest .
docker run -d -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/sri-karthikeya-caterers \
  --name caterers-api \
  sri-karthikeya-caterers:latest
```

#### Option C: Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mongodb:
    image: mongo:6.0
    container_name: caterers-mongo
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db
    environment:
      MONGO_INITDB_DATABASE: sri-karthikeya-caterers

  api:
    build: .
    container_name: caterers-api
    ports:
      - "8080:8080"
    depends_on:
      - mongodb
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongodb:27017/sri-karthikeya-caterers

volumes:
  mongo-data:
```

Run:
```bash
docker-compose up -d
```

### 4. Nginx Reverse Proxy

Create `/etc/nginx/sites-available/caterers-api`:

```nginx
server {
    listen 80;
    server_name api.srikarthikeyacaterers.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable and restart:
```bash
sudo ln -s /etc/nginx/sites-available/caterers-api /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 5. SSL Certificate (Let's Encrypt)

```bash
sudo apt-get install certbot python3-certbot-nginx
sudo certbot --nginx -d api.srikarthikeyacaterers.com
```

## Monitoring & Maintenance

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### View Logs

```bash
# Systemd
sudo journalctl -u sri-karthikeya-caterers -f

# Docker
docker logs -f caterers-api

# File
tail -f /var/log/sri-karthikeya-caterers/application.log
```

### MongoDB Backup

```bash
# Backup
mongodump --db sri-karthikeya-caterers --out /backup/$(date +%Y%m%d)

# Restore
mongorestore --db sri-karthikeya-caterers /backup/20240101/sri-karthikeya-caterers
```

### Application Metrics

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Endpoints:
- `/actuator/health` - Health status
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

## Performance Tuning

### JVM Options

```bash
java -Xms512m -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar app.jar
```

### MongoDB Indexes

```javascript
use sri-karthikeya-caterers

// Menu indexes
db.menu.createIndex({ "imageId": 1 }, { unique: true })
db.menu.createIndex({ "createdAt": -1 })

// Gallery indexes
db.gallery.createIndex({ "imageId": 1 }, { unique: true })
db.gallery.createIndex({ "type": 1 })
db.gallery.createIndex({ "createdAt": -1 })

// Reviews indexes
db.reviews.createIndex({ "imageId": 1 }, { unique: true })
db.reviews.createIndex({ "stars": -1 })
db.reviews.createIndex({ "type": 1 })
db.reviews.createIndex({ "createdAt": -1 })

// Quotes indexes
db.quotes.createIndex({ "email": 1 })
db.quotes.createIndex({ "eventDate": 1 })
db.quotes.createIndex({ "createdAt": -1 })
```

## Security Hardening

1. **Change default MongoDB port**
2. **Enable MongoDB authentication**
3. **Use environment variables for secrets**
4. **Enable HTTPS only**
5. **Configure rate limiting**
6. **Set up firewall rules**
7. **Regular security updates**
8. **Enable audit logging**

## Troubleshooting

### Application won't start

```bash
# Check Java version
java -version

# Check port availability
netstat -an | grep 8080

# Check MongoDB connection
mongosh mongodb://localhost:27017
```

### High memory usage

```bash
# Check JVM memory
jps -lvm

# Adjust heap size
java -Xms256m -Xmx1g -jar app.jar
```

### Slow queries

```bash
# Enable MongoDB profiling
db.setProfilingLevel(2)
db.system.profile.find().sort({ts:-1}).limit(5)
```

## Support

For issues and questions:
- Email: support@srikarthikeyacaterers.com
- Documentation: http://localhost:8080/swagger-ui.html
