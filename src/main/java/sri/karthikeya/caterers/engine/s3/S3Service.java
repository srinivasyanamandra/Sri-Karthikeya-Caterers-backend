package sri.karthikeya.caterers.engine.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file, String path);
    String updateFile(String existingKey, MultipartFile newFile);
    void deleteFile(String key);
    String getPresignedUrl(String key, int expirationMinutes);
    boolean fileExists(String key);
}
