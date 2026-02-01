package sri.karthikeya.caterers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sri.karthikeya.caterers.dto.request.ReviewRequest;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.ReviewResponse;
import sri.karthikeya.caterers.entity.Review;
import sri.karthikeya.caterers.exception.custom.DuplicateResourceException;
import sri.karthikeya.caterers.exception.custom.ResourceNotFoundException;
import sri.karthikeya.caterers.mapper.ReviewMapper;
import sri.karthikeya.caterers.repository.ReviewRepository;
import sri.karthikeya.caterers.service.ReviewService;
import sri.karthikeya.caterers.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse create(ReviewRequest request) {
        log.info("Creating review for event type: {}", request.getType());
        ValidationUtil.validateUUID(request.getImageId(), "imageId");
        
        if (reviewRepository.existsByImageId(request.getImageId())) {
            throw new DuplicateResourceException("Review with imageId " + request.getImageId() + " already exists");
        }

        Review review = reviewMapper.toEntity(request);
        review.setId(UUID.randomUUID().toString());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        
        Review saved = reviewRepository.save(review);
        log.info("Review created with id: {}", saved.getId());
        return reviewMapper.toResponse(saved);
    }

    @Override
    public ReviewResponse getById(String id) {
        log.info("Fetching review with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return reviewMapper.toResponse(review);
    }

    @Override
    public PageResponse<ReviewResponse> getAll(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all reviews - page: {}, size: {}", page, size);
        ValidationUtil.validatePagination(page, size);
        
        List<Review> reviews = reviewRepository.findAll(page, size, sortBy, sortDir);
        long total = reviewRepository.count();
        
        List<ReviewResponse> responses = reviews.stream()
                .map(reviewMapper::toResponse)
                .toList();
        
        return PageResponse.<ReviewResponse>builder()
                .content(responses)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / size))
                .last(page >= (int) Math.ceil((double) total / size) - 1)
                .build();
    }

    @Override
    public ReviewResponse update(String id, ReviewRequest request) {
        log.info("Updating review with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        ValidationUtil.validateUUID(request.getImageId(), "imageId");
        
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        if (reviewRepository.existsByImageIdAndIdNot(request.getImageId(), id)) {
            throw new DuplicateResourceException("Review with imageId " + request.getImageId() + " already exists");
        }
        
        reviewMapper.updateEntity(request, review);
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updated = reviewRepository.save(review);
        log.info("Review updated with id: {}", updated.getId());
        return reviewMapper.toResponse(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting review with id: {}", id);
        ValidationUtil.validateUUID(id, "id");
        
        if (!reviewRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Review not found with id: " + id);
        }
        
        reviewRepository.deleteById(id);
        log.info("Review deleted with id: {}", id);
    }
}
