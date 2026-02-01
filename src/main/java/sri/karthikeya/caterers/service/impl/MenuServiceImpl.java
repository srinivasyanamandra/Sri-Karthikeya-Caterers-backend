package sri.karthikeya.caterers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sri.karthikeya.caterers.dto.request.MenuRequest;
import sri.karthikeya.caterers.dto.response.MenuResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.entity.Menu;
import sri.karthikeya.caterers.exception.custom.DuplicateResourceException;
import sri.karthikeya.caterers.exception.custom.ResourceNotFoundException;
import sri.karthikeya.caterers.mapper.MenuMapper;
import sri.karthikeya.caterers.repository.MenuRepository;
import sri.karthikeya.caterers.service.MenuService;
import sri.karthikeya.caterers.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    @Override
    public MenuResponse create(MenuRequest request) {
        log.info("Creating menu with name: {}", request.getName());
        ValidationUtil.validateUUID(request.getImageId(), "imageId");
        
        if (menuRepository.existsByImageId(request.getImageId())) {
            throw new DuplicateResourceException("Menu with imageId " + request.getImageId() + " already exists");
        }

        Menu menu = menuMapper.toEntity(request);
        menu.setId(UUID.randomUUID().toString());
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());
        
        Menu saved = menuRepository.save(menu);
        log.info("Menu created with id: {}", saved.getId());
        return menuMapper.toResponse(saved);
    }

    @Override
    public MenuResponse getById(String id) {
        log.info("Fetching menu with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        return menuMapper.toResponse(menu);
    }

    @Override
    public PageResponse<MenuResponse> getAll(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all menus - page: {}, size: {}", page, size);
        ValidationUtil.validatePagination(page, size);
        
        List<Menu> menus = menuRepository.findAll(page, size, sortBy, sortDir);
        long total = menuRepository.count();
        
        List<MenuResponse> responses = menus.stream()
                .map(menuMapper::toResponse)
                .toList();
        
        return PageResponse.<MenuResponse>builder()
                .content(responses)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .last(page >= (int) Math.ceil((double) total / size) - 1)
                .build();
    }

    @Override
    public MenuResponse update(String id, MenuRequest request) {
        log.info("Updating menu with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        ValidationUtil.validateUUID(request.getImageId(), "imageId");
        
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        
        if (menuRepository.existsByImageIdAndIdNot(request.getImageId(), id)) {
            throw new DuplicateResourceException("Menu with imageId " + request.getImageId() + " already exists");
        }
        
        menuMapper.updateEntity(request, menu);
        menu.setUpdatedAt(LocalDateTime.now());
        
        Menu updated = menuRepository.save(menu);
        log.info("Menu updated with id: {}", updated.getId());
        return menuMapper.toResponse(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting menu with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        if (!menuRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        
        menuRepository.deleteById(id);
        log.info("Menu deleted with id: {}", id);
    }
}
