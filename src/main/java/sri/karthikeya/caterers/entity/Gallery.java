package sri.karthikeya.caterers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sri.karthikeya.caterers.enums.GalleryType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "gallery")
public class Gallery extends BaseEntity {
    @Id
    private String id;
    private String imageId;
    private GalleryType type;
    private String name;
    private String description;
}
