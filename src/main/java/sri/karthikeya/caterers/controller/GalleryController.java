package sri.karthikeya.caterers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sri.karthikeya.caterers.dto.request.GalleryCreateRequest;
import sri.karthikeya.caterers.dto.request.GalleryUpdateRequest;
import sri.karthikeya.caterers.dto.response.ApiResponse;
import sri.karthikeya.caterers.dto.response.GalleryResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.service.GalleryService;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Tag(name = "Gallery", description = "Gallery management APIs")
public class GalleryController {
    private final GalleryService galleryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new gallery item", description = "Creates a new gallery item with image upload")
    public ResponseEntity<ApiResponse<GalleryResponse>> create(@Valid @ModelAttribute GalleryCreateRequest request) {
        GalleryResponse response = galleryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<GalleryResponse>builder()
                        .success(true)
                        .message("Gallery created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gallery by ID", description = "Retrieves a gallery item by its unique identifier")
    public ResponseEntity<ApiResponse<GalleryResponse>> getById(
            @Parameter(description = "Gallery ID") @PathVariable String id) {
        GalleryResponse response = galleryService.getById(id);
        return ResponseEntity.ok(ApiResponse.<GalleryResponse>builder()
                .success(true)
                .message("Gallery retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all galleries", description = "Retrieves all gallery items with pagination and sorting")
    public ResponseEntity<ApiResponse<PageResponse<GalleryResponse>>> getAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        PageResponse<GalleryResponse> response = galleryService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<PageResponse<GalleryResponse>>builder()
                .success(true)
                .message("Galleries retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update gallery", description = "Updates an existing gallery item with optional image update")
    public ResponseEntity<ApiResponse<GalleryResponse>> update(
            @Parameter(description = "Gallery ID") @PathVariable String id,
            @Valid @ModelAttribute GalleryUpdateRequest request) {
        GalleryResponse response = galleryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<GalleryResponse>builder()
                .success(true)
                .message("Gallery updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gallery", description = "Deletes a gallery item and its image from S3")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Gallery ID") @PathVariable String id) {
        galleryService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Gallery deleted successfully")
                .build());
    }
}
