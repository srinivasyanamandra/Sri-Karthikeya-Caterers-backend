package sri.karthikeya.caterers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sri.karthikeya.caterers.dto.request.GalleryCreateRequest;
import sri.karthikeya.caterers.dto.request.GalleryUpdateRequest;
import sri.karthikeya.caterers.dto.response.GalleryResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.entity.Gallery;
import sri.karthikeya.caterers.exception.custom.ResourceNotFoundException;
import sri.karthikeya.caterers.mapper.GalleryMapper;
import sri.karthikeya.caterers.repository.GalleryRepository;
import sri.karthikeya.caterers.service.GalleryService;
import sri.karthikeya.caterers.engine.s3.S3PathConstants;
import sri.karthikeya.caterers.engine.s3.S3Service;
import sri.karthikeya.caterers.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {
    private final GalleryRepository galleryRepository;
    private final GalleryMapper galleryMapper;
    private final S3Service s3Service;

    @Override
    public GalleryResponse create(GalleryCreateRequest request) {
        log.info("Creating gallery with name: {}", request.getName());
        
        String imageKey = s3Service.uploadFile(request.getImage(), S3PathConstants.getPathByType(request.getType()));
        
        Gallery gallery = new Gallery();
        gallery.setId(UUID.randomUUID().toString());
        gallery.setImageId(imageKey);
        gallery.setType(request.getType());
        gallery.setName(request.getName());
        gallery.setDescription(request.getDescription());
        gallery.setCreatedAt(LocalDateTime.now());
        gallery.setUpdatedAt(LocalDateTime.now());
        
        Gallery saved = galleryRepository.save(gallery);
        log.info("Gallery created with id: {}", saved.getId());
        return galleryMapper.toResponse(saved);
    }

    @Override
    public GalleryResponse getById(String id) {
        log.info("Fetching gallery with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found with id: " + id));
        return galleryMapper.toResponse(gallery);
    }

    @Override
    public PageResponse<GalleryResponse> getAll(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all galleries - page: {}, size: {}", page, size);
        ValidationUtil.validatePagination(page, size);
        
        List<Gallery> galleries = galleryRepository.findAll(page, size, sortBy, sortDir);
        long total = galleryRepository.count();
        
        List<GalleryResponse> responses = galleries.stream()
                .map(galleryMapper::toResponse)
                .toList();
        
        return PageResponse.<GalleryResponse>builder()
                .content(responses)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .last(page >= (int) Math.ceil((double) total / size) - 1)
                .build();
    }

    @Override
    public GalleryResponse update(String id, GalleryUpdateRequest request) {
        log.info("Updating gallery with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found with id: " + id));
        
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            s3Service.deleteFile(gallery.getImageId());
            String newImageKey = s3Service.uploadFile(request.getImage(), S3PathConstants.getPathByType(request.getType()));
            gallery.setImageId(newImageKey);
        }
        
        gallery.setType(request.getType());
        gallery.setName(request.getName());
        gallery.setDescription(request.getDescription());
        gallery.setUpdatedAt(LocalDateTime.now());
        
        Gallery updated = galleryRepository.save(gallery);
        log.info("Gallery updated with id: {}", updated.getId());
        return galleryMapper.toResponse(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting gallery with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery not found with id: " + id));
        
        s3Service.deleteFile(gallery.getImageId());
        galleryRepository.deleteById(id);
        log.info("Gallery deleted with id: {}", id);
    }
}
