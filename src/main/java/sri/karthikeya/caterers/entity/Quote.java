package sri.karthikeya.caterers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sri.karthikeya.caterers.enums.ReviewEventType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "quotes")
public class Quote extends BaseEntity {
    @Id
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate eventDate;
    private ReviewEventType eventType;
    private Integer expectedGuests;
    private String additionalDetails;
}
