package sri.karthikeya.caterers.util;

import sri.karthikeya.caterers.exception.custom.BadRequestException;
import sri.karthikeya.caterers.exception.custom.ValidationException;

import java.util.UUID;

public class ValidationUtil {
    private static final int MAX_PAGE_SIZE = 100;

    public static void validateUUID(String uuid, String fieldName) {
        if (uuid == null || uuid.isBlank()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid UUID format for " + fieldName);
        }
    }

    public static void validatePagination(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new BadRequestException("Page size must be greater than 0");
        }
        if (size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size cannot exceed " + MAX_PAGE_SIZE);
        }
    }
}
