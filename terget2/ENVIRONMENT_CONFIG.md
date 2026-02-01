# Environment Configuration Guide

## Available Profiles

### 1. Development (dev) - Default
**File:** `application-dev.yaml`

**Features:**
- Local MongoDB connection
- Swagger UI enabled
- Debug logging
- Full error details in responses
- Console logging with detailed format

**Usage:**
```bash
mvn spring-boot:run
# or
java -jar app.jar
```

---

### 2. Staging (staging)
**File:** `application-staging.yaml`

**Features:**
- Remote MongoDB with authentication
- Swagger UI enabled
- Debug logging for app, INFO for Spring
- Full error details in responses
- File logging enabled

**Usage:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=staging
# or
java -jar app.jar --spring.profiles.active=staging
```

**Environment Variables:**
```bash
export MONGODB_URI="mongodb://user:pass@staging-host:27017/sri-karthikeya-caterers?authSource=admin"
export SERVER_PORT=8080
export LOG_FILE="/var/log/sri-karthikeya-caterers/application.log"
```

---

### 3. Production (prod)
**File:** `application-prod.yaml`

**Features:**
- Remote MongoDB with authentication
- Swagger UI disabled (security)
- INFO/WARN logging only
- No error details in responses
- File logging with rotation
- SSL support

**Usage:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
# or
java -jar app.jar --spring.profiles.active=prod
```

**Required Environment Variables:**
```bash
export MONGODB_URI="mongodb://user:pass@prod-host:27017/sri-karthikeya-caterers?authSource=admin"
export SERVER_PORT=8080
export SSL_ENABLED=true
export SSL_KEYSTORE="classpath:keystore.p12"
export SSL_PASSWORD="your-keystore-password"
export SWAGGER_ENABLED=false
export LOG_FILE="/var/log/sri-karthikeya-caterers/application.log"
```

---

## Configuration Comparison

| Feature | Dev | Staging | Production |
|---------|-----|---------|------------|
| MongoDB | Local | Remote | Remote |
| Port | 8080 | 8080 | 8080 (configurable) |
| Swagger UI | ✅ Enabled | ✅ Enabled | ❌ Disabled |
| Error Details | ✅ Full | ✅ Full | ❌ Hidden |
| Logging Level | DEBUG | DEBUG/INFO | INFO/WARN |
| File Logging | ❌ No | ✅ Yes | ✅ Yes |
| SSL | ❌ No | ❌ No | ✅ Optional |
| Log Retention | N/A | 15 days | 30 days |

---

## MongoDB Connection Strings

### Development
```
mongodb://localhost:27017/sri-karthikeya-caterers
```

### Staging/Production with Authentication
```
mongodb://username:password@host:27017/sri-karthikeya-caterers?authSource=admin
```

### MongoDB Atlas (Cloud)
```
mongodb+srv://username:password@cluster.mongodb.net/sri-karthikeya-caterers?retryWrites=true&w=majority
```

---

## Docker Deployment

### Development
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  sri-karthikeya-caterers:latest
```

### Production
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MONGODB_URI="mongodb://user:pass@mongo:27017/sri-karthikeya-caterers" \
  -e SSL_ENABLED=false \
  -e SWAGGER_ENABLED=false \
  -v /var/log/caterers:/var/log/sri-karthikeya-caterers \
  sri-karthikeya-caterers:latest
```

---

## Systemd Service (Production)

Create `/etc/systemd/system/sri-karthikeya-caterers.service`:

```ini
[Unit]
Description=Sri Karthikeya Caterers API
After=network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/sri-karthikeya-caterers
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="MONGODB_URI=mongodb://user:pass@localhost:27017/sri-karthikeya-caterers"
Environment="SERVER_PORT=8080"
Environment="SSL_ENABLED=false"
Environment="SWAGGER_ENABLED=false"
ExecStart=/usr/bin/java -jar /opt/sri-karthikeya-caterers/app.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

---

## Kubernetes ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: caterers-config
data:
  SPRING_PROFILES_ACTIVE: "prod"
  SERVER_PORT: "8080"
  SWAGGER_ENABLED: "false"
---
apiVersion: v1
kind: Secret
metadata:
  name: caterers-secrets
type: Opaque
stringData:
  MONGODB_URI: "mongodb://user:pass@mongo-service:27017/sri-karthikeya-caterers"
  SSL_PASSWORD: "your-ssl-password"
```

---

## Testing Different Profiles

### Test Development
```bash
curl http://localhost:8080/swagger-ui.html
# Should work
```

### Test Production
```bash
java -jar app.jar --spring.profiles.active=prod
curl http://localhost:8080/swagger-ui.html
# Should return 404 (disabled)
```

---

## Switching Profiles

### Method 1: Command Line
```bash
java -jar app.jar --spring.profiles.active=prod
```

### Method 2: Environment Variable
```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

### Method 3: application.yaml
```yaml
spring:
  profiles:
    active: prod
```

### Method 4: Maven
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Security Best Practices

1. **Never commit** `application-prod.yaml` with real credentials
2. **Use environment variables** for sensitive data
3. **Disable Swagger** in production
4. **Enable SSL** in production
5. **Hide error details** in production
6. **Use strong passwords** for MongoDB
7. **Rotate logs** regularly
8. **Monitor log files** for security issues

---

## Troubleshooting

### Profile not loading
```bash
# Check active profile
java -jar app.jar --debug | grep "active profiles"
```

### MongoDB connection failed
```bash
# Test connection
mongosh "mongodb://user:pass@host:27017/sri-karthikeya-caterers"
```

### Logs not writing
```bash
# Check directory permissions
ls -la /var/log/sri-karthikeya-caterers/
# Create if missing
sudo mkdir -p /var/log/sri-karthikeya-caterers
sudo chown appuser:appuser /var/log/sri-karthikeya-caterers
```

---

## Quick Reference

| Task | Command |
|------|---------|
| Run dev | `mvn spring-boot:run` |
| Run staging | `java -jar app.jar --spring.profiles.active=staging` |
| Run prod | `java -jar app.jar --spring.profiles.active=prod` |
| Check profile | `curl http://localhost:8080/actuator/env` |
| View logs | `tail -f /var/log/sri-karthikeya-caterers/application.log` |
