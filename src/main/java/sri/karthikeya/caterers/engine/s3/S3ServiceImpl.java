package sri.karthikeya.caterers.engine.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import sri.karthikeya.caterers.exception.custom.BadRequestException;
import sri.karthikeya.caterers.exception.custom.InternalServerException;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};

    @Override
    public String uploadFile(MultipartFile file, String path) {
        log.info("Uploading file to S3: {}", file.getOriginalFilename());
        validateFile(file);

        String key = generateKey(path, file.getOriginalFilename());
        
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            log.info("File uploaded successfully: {}", key);
            return key;
        } catch (IOException e) {
            log.error("Failed to read file: {}", e.getMessage());
            throw new BadRequestException("Failed to read file");
        } catch (S3Exception e) {
            log.error("S3 upload failed: {}", e.getMessage());
            throw new InternalServerException("Failed to upload file to S3");
        }
    }

    @Override
    public String updateFile(String existingKey, MultipartFile newFile) {
        log.info("Updating file in S3: {}", existingKey);
        validateFile(newFile);

        if (!fileExists(existingKey)) {
            log.warn("File not found for update: {}", existingKey);
            throw new BadRequestException("File not found: " + existingKey);
        }

        deleteFile(existingKey);
        String path = existingKey.substring(0, existingKey.lastIndexOf('/') + 1);
        return uploadFile(newFile, path);
    }

    @Override
    public void deleteFile(String key) {
        log.info("Deleting file from S3: {}", key);
        
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
            log.info("File deleted successfully: {}", key);
        } catch (S3Exception e) {
            log.error("S3 delete failed: {}", e.getMessage());
            throw new InternalServerException("Failed to delete file from S3");
        }
    }

    @Override
    public String getPresignedUrl(String key, int expirationMinutes) {
        log.info("Generating presigned URL for: {}", key);
        
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expirationMinutes))
                    .getObjectRequest(request)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();
            log.info("Presigned URL generated successfully");
            return url;
        } catch (S3Exception e) {
            log.error("Failed to generate presigned URL: {}", e.getMessage());
            throw new InternalServerException("Failed to generate presigned URL");
        }
    }

    @Override
    public boolean fileExists(String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(request);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.error("Error checking file existence: {}", e.getMessage());
            throw new InternalServerException("Failed to check file existence");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !isValidExtension(filename)) {
            throw new BadRequestException("Invalid file type. Allowed: jpg, jpeg, png, gif, webp");
        }
    }

    private boolean isValidExtension(String filename) {
        String lowerFilename = filename.toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (lowerFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private String generateKey(String path, String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return path + UUID.randomUUID() + extension;
    }
}
