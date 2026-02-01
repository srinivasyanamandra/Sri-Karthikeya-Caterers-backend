package sri.karthikeya.caterers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sri.karthikeya.caterers.enums.ReviewEventType;
import sri.karthikeya.caterers.enums.TopPicks;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String imageId;
    private String timeline;
    private Integer guestsCount;
    private Integer stars;
    private String comments;
    private List<TopPicks> topPicks;
    private ReviewEventType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
