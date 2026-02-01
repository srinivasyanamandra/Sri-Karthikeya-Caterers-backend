package sri.karthikeya.caterers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sri.karthikeya.caterers.dto.response.GalleryResponse;
import sri.karthikeya.caterers.entity.Gallery;

@Mapper(componentModel = "spring")
public interface GalleryMapper {
    GalleryResponse toResponse(Gallery gallery);
}
