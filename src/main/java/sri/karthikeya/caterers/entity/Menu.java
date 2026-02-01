package sri.karthikeya.caterers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "menu")
public class Menu extends BaseEntity {
    @Id
    private String id;
    private String imageId;
    private String name;
    private Double price;
    private String description;
    private List<String> items;
}
