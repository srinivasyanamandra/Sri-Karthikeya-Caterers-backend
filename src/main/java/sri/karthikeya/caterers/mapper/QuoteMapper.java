package sri.karthikeya.caterers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sri.karthikeya.caterers.dto.request.QuoteRequest;
import sri.karthikeya.caterers.dto.response.QuoteResponse;
import sri.karthikeya.caterers.entity.Quote;

@Mapper(componentModel = "spring")
public interface QuoteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Quote toEntity(QuoteRequest request);

    QuoteResponse toResponse(Quote quote);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(QuoteRequest request, @MappingTarget Quote quote);
}
