package sri.karthikeya.caterers.service;

import sri.karthikeya.caterers.dto.request.MenuRequest;
import sri.karthikeya.caterers.dto.response.MenuResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;

public interface MenuService {
    MenuResponse create(MenuRequest request);
    MenuResponse getById(String id);
    PageResponse<MenuResponse> getAll(int page, int size, String sortBy, String sortDir);
    MenuResponse update(String id, MenuRequest request);
    void delete(String id);
}
