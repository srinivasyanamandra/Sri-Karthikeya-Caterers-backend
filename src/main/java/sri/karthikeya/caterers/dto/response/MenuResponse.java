package sri.karthikeya.caterers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private String id;
    private String imageId;
    private String name;
    private Double price;
    private String description;
    private List<String> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
