package sri.karthikeya.caterers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sri.karthikeya.caterers.enums.ReviewEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate eventDate;
    private ReviewEventType eventType;
    private Integer expectedGuests;
    private String additionalDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
