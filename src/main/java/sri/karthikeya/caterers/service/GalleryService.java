package sri.karthikeya.caterers.service;

import sri.karthikeya.caterers.dto.request.GalleryCreateRequest;
import sri.karthikeya.caterers.dto.request.GalleryUpdateRequest;
import sri.karthikeya.caterers.dto.response.GalleryResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;

public interface GalleryService {
    GalleryResponse create(GalleryCreateRequest request);
    GalleryResponse getById(String id);
    PageResponse<GalleryResponse> getAll(int page, int size, String sortBy, String sortDir);
    GalleryResponse update(String id, GalleryUpdateRequest request);
    void delete(String id);
}
