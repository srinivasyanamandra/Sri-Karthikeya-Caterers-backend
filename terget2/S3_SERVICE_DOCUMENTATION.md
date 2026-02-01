# S3 Service Engine Documentation

## Overview
Production-grade S3 service engine for file management following Clean Architecture and SOLID principles.

## Features
- ✅ Upload files to S3
- ✅ Update existing files
- ✅ Delete files
- ✅ Generate presigned URLs
- ✅ File validation (size, type)
- ✅ Path-based organization
- ✅ Comprehensive error handling
- ✅ Logging

## Configuration

### Environment Variables (Recommended for Production)
```bash
export AWS_ACCESS_KEY=your-access-key
export AWS_SECRET_KEY=your-secret-key
export AWS_BUCKET_NAME=your-bucket-name
export AWS_REGION=ap-south-2
```

### Application YAML
```yaml
aws:
  s3:
    access-key: ${AWS_ACCESS_KEY}
    secret-key: ${AWS_SECRET_KEY}
    bucket-name: ${AWS_BUCKET_NAME}
    region: ${AWS_REGION}
```

## Path Constants

Located in `S3PathConstants.java`:
```java
MENU_PATH = "menu/"
GALLERY_PATH = "gallery/"
REVIEWS_PATH = "reviews/"
QUOTES_PATH = "quotes/"
TEMP_PATH = "temp/"
```

## Service Interface

```java
public interface S3Service {
    String uploadFile(MultipartFile file, String path);
    String updateFile(String existingKey, MultipartFile newFile);
    void deleteFile(String key);
    String getPresignedUrl(String key, int expirationMinutes);
    boolean fileExists(String key);
}
```

## Usage Examples

### 1. Upload File
```java
@Autowired
private S3Service s3Service;

public void uploadMenuImage(MultipartFile file) {
    String key = s3Service.uploadFile(file, S3PathConstants.MENU_PATH);
    // Returns: "menu/uuid.jpg"
}
```

### 2. Update File
```java
public void updateImage(String existingKey, MultipartFile newFile) {
    String newKey = s3Service.updateFile(existingKey, newFile);
    // Deletes old file and uploads new one
}
```

### 3. Delete File
```java
public void deleteImage(String key) {
    s3Service.deleteFile(key);
}
```

### 4. Get Presigned URL
```java
public String getImageUrl(String key) {
    return s3Service.getPresignedUrl(key, 60); // 60 minutes expiration
}
```

### 5. Check File Existence
```java
public boolean checkFile(String key) {
    return s3Service.fileExists(key);
}
```

## API Endpoints

### Upload Menu Image
```http
POST /api/s3/upload/menu
Content-Type: multipart/form-data

file: [binary]
```

### Upload Gallery Image
```http
POST /api/s3/upload/gallery
Content-Type: multipart/form-data

file: [binary]
```

### Upload Review Image
```http
POST /api/s3/upload/reviews
Content-Type: multipart/form-data

file: [binary]
```

### Update File
```http
PUT /api/s3/update?key=menu/uuid.jpg
Content-Type: multipart/form-data

file: [binary]
```

### Delete File
```http
DELETE /api/s3?key=menu/uuid.jpg
```

### Get Presigned URL
```http
GET /api/s3/presigned-url?key=menu/uuid.jpg&expirationMinutes=60
```

## Validation Rules

### File Size
- Maximum: 10MB
- Configurable in `application.yaml`

### Allowed File Types
- `.jpg`
- `.jpeg`
- `.png`
- `.gif`
- `.webp`

### File Naming
- Auto-generated UUID
- Original extension preserved
- Format: `{path}{uuid}{extension}`

## Error Handling

### BadRequestException
- Empty file
- File size exceeds limit
- Invalid file type
- File not found for update

### InternalServerException
- S3 upload failure
- S3 delete failure
- Presigned URL generation failure

## Integration Examples

### In Menu Service
```java
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final S3Service s3Service;
    private final MenuRepository menuRepository;
    
    public MenuResponse create(MenuRequest request, MultipartFile image) {
        // Upload image
        String imageKey = s3Service.uploadFile(image, S3PathConstants.MENU_PATH);
        
        // Save menu with image key
        Menu menu = new Menu();
        menu.setImageId(imageKey);
        // ... set other fields
        
        return menuMapper.toResponse(menuRepository.save(menu));
    }
    
    public void delete(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        
        // Delete image from S3
        s3Service.deleteFile(menu.getImageId());
        
        // Delete menu
        menuRepository.deleteById(id);
    }
}
```

### In Gallery Service
```java
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {
    private final S3Service s3Service;
    
    public String getImageUrl(String imageKey) {
        return s3Service.getPresignedUrl(imageKey, 120); // 2 hours
    }
}
```

## Security Best Practices

### 1. Environment Variables
```bash
# Never commit credentials
export AWS_ACCESS_KEY=xxx
export AWS_SECRET_KEY=xxx
```

### 2. IAM Permissions
Minimum required S3 permissions:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObject",
        "s3:DeleteObject",
        "s3:HeadObject"
      ],
      "Resource": "arn:aws:s3:::skc-gallery-and-type-s3/*"
    }
  ]
}
```

### 3. Bucket Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::skc-gallery-and-type-s3/*"
    }
  ]
}
```

## Testing

### Unit Test Example
```java
@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {
    @Mock
    private S3Client s3Client;
    
    @Mock
    private S3Presigner s3Presigner;
    
    @InjectMocks
    private S3ServiceImpl s3Service;
    
    @Test
    void uploadFile_Success() {
        // Test implementation
    }
}
```

### Integration Test
```bash
curl -X POST http://localhost:8080/api/s3/upload/menu \
  -F "file=@test-image.jpg"
```

## Monitoring

### Logs
```
INFO  - Uploading file to S3: image.jpg
INFO  - File uploaded successfully: menu/uuid.jpg
INFO  - Generating presigned URL for: menu/uuid.jpg
INFO  - Presigned URL generated successfully
```

### Metrics to Monitor
- Upload success rate
- Upload duration
- File size distribution
- Presigned URL generation rate
- Error rate by type

## Performance Optimization

### 1. Async Upload (Optional)
```java
@Async
public CompletableFuture<String> uploadFileAsync(MultipartFile file, String path) {
    return CompletableFuture.completedFuture(uploadFile(file, path));
}
```

### 2. Batch Operations
```java
public List<String> uploadMultipleFiles(List<MultipartFile> files, String path) {
    return files.stream()
            .map(file -> uploadFile(file, path))
            .toList();
}
```

## Troubleshooting

### Issue: Upload fails
**Solution:** Check AWS credentials and bucket permissions

### Issue: Presigned URL doesn't work
**Solution:** Verify bucket CORS configuration

### Issue: File size error
**Solution:** Adjust `spring.servlet.multipart.max-file-size`

## Production Checklist

- [ ] Move credentials to environment variables
- [ ] Configure IAM role (EC2/ECS)
- [ ] Set up S3 bucket lifecycle policies
- [ ] Enable S3 versioning
- [ ] Configure CloudFront CDN
- [ ] Set up monitoring and alerts
- [ ] Implement retry logic
- [ ] Add rate limiting
- [ ] Configure CORS properly
- [ ] Enable S3 access logging

## Architecture Compliance

✅ **Clean Architecture** - Service layer separated from infrastructure  
✅ **SOLID Principles** - Single responsibility, dependency injection  
✅ **Naming Conventions** - camelCase methods, PascalCase classes  
✅ **Error Handling** - Custom exceptions, comprehensive logging  
✅ **Validation** - File size, type, existence checks  
✅ **Constants** - Path constants in separate class  
✅ **Configuration** - Externalized via environment variables  
✅ **Documentation** - Complete API and usage docs  

## Support

For issues or questions:
- Check logs for detailed error messages
- Verify AWS credentials and permissions
- Review S3 bucket configuration
- Test with Swagger UI: http://localhost:8080/swagger-ui.html
