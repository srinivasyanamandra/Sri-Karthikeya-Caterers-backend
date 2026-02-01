package sri.karthikeya.caterers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sri.karthikeya.caterers.enums.ReviewEventType;
import sri.karthikeya.caterers.enums.TopPicks;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reviews")
public class Review extends BaseEntity {
    @Id
    private String id;
    private String imageId;
    private String timeline;
    private Integer guestsCount;
    private Integer stars;
    private String comments;
    private List<TopPicks> topPicks;
    private ReviewEventType type;
}
