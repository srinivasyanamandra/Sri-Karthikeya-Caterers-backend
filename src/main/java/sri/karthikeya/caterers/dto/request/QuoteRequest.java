package sri.karthikeya.caterers.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sri.karthikeya.caterers.enums.ReviewEventType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    @NotNull(message = "Event type is required")
    private ReviewEventType eventType;

    @NotNull(message = "Expected guests count is required")
    @Min(value = 1, message = "Expected guests must be at least 1")
    @Max(value = 100000, message = "Expected guests must not exceed 100000")
    private Integer expectedGuests;

    @Size(max = 2000, message = "Additional details must not exceed 2000 characters")
    private String additionalDetails;
}
