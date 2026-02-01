package sri.karthikeya.caterers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import sri.karthikeya.caterers.enums.GalleryType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryCreateRequest {
    @NotNull(message = "Image file is required")
    private MultipartFile image;

    @NotNull(message = "Type is required")
    private GalleryType type;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}
