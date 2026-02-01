package sri.karthikeya.caterers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sri.karthikeya.caterers.dto.request.ReviewRequest;
import sri.karthikeya.caterers.dto.response.ApiResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.ReviewResponse;
import sri.karthikeya.caterers.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Create a new review", description = "Creates a new review with the provided details")
    public ResponseEntity<ApiResponse<ReviewResponse>> create(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReviewResponse>builder()
                        .success(true)
                        .message("Review created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieves a review by its unique identifier")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(
            @Parameter(description = "Review ID") @PathVariable String id) {
        ReviewResponse response = reviewService.getById(id);
        return ResponseEntity.ok(ApiResponse.<ReviewResponse>builder()
                .success(true)
                .message("Review retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Retrieves all reviews with pagination and sorting")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        PageResponse<ReviewResponse> response = reviewService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<PageResponse<ReviewResponse>>builder()
                .success(true)
                .message("Reviews retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update review", description = "Updates an existing review")
    public ResponseEntity<ApiResponse<ReviewResponse>> update(
            @Parameter(description = "Review ID") @PathVariable String id,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<ReviewResponse>builder()
                .success(true)
                .message("Review updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Deletes a review by its ID")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Review ID") @PathVariable String id) {
        reviewService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Review deleted successfully")
                .build());
    }
}
