package sri.karthikeya.caterers.service;

import sri.karthikeya.caterers.dto.request.ReviewRequest;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse create(ReviewRequest request);
    ReviewResponse getById(String id);
    PageResponse<ReviewResponse> getAll(int page, int size, String sortBy, String sortDir);
    ReviewResponse update(String id, ReviewRequest request);
    void delete(String id);
}
