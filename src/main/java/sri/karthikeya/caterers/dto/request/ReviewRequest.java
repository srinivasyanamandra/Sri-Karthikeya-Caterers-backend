package sri.karthikeya.caterers.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sri.karthikeya.caterers.enums.ReviewEventType;
import sri.karthikeya.caterers.enums.TopPicks;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    @NotBlank(message = "Image ID is required")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format for imageId")
    private String imageId;

    @NotBlank(message = "Timeline is required")
    @Size(max = 200, message = "Timeline must not exceed 200 characters")
    private String timeline;

    @NotNull(message = "Guests count is required")
    @Min(value = 1, message = "Guests count must be at least 1")
    @Max(value = 100000, message = "Guests count must not exceed 100000")
    private Integer guestsCount;

    @NotNull(message = "Stars rating is required")
    @Min(value = 1, message = "Stars must be between 1 and 5")
    @Max(value = 5, message = "Stars must be between 1 and 5")
    private Integer stars;

    @NotBlank(message = "Comments are required")
    @Size(max = 2000, message = "Comments must not exceed 2000 characters")
    private String comments;

    @NotEmpty(message = "Top picks cannot be empty")
    @Size(max = 5, message = "Top picks cannot exceed 5 items")
    private List<TopPicks> topPicks;

    @NotNull(message = "Event type is required")
    private ReviewEventType type;
}
