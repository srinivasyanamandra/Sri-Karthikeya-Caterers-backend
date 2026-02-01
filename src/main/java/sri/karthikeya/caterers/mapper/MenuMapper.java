package sri.karthikeya.caterers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sri.karthikeya.caterers.dto.request.MenuRequest;
import sri.karthikeya.caterers.dto.response.MenuResponse;
import sri.karthikeya.caterers.entity.Menu;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Menu toEntity(MenuRequest request);

    MenuResponse toResponse(Menu menu);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(MenuRequest request, @MappingTarget Menu menu);
}
