package sri.karthikeya.caterers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sri.karthikeya.caterers.dto.request.QuoteRequest;
import sri.karthikeya.caterers.dto.response.ApiResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.dto.response.QuoteResponse;
import sri.karthikeya.caterers.service.QuoteService;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
@Tag(name = "Quotes", description = "Quote management APIs")
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping
    @Operation(summary = "Create a new quote request", description = "Creates a new quote request with the provided details")
    public ResponseEntity<ApiResponse<QuoteResponse>> create(@Valid @RequestBody QuoteRequest request) {
        QuoteResponse response = quoteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<QuoteResponse>builder()
                        .success(true)
                        .message("Quote created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quote by ID", description = "Retrieves a quote by its unique identifier")
    public ResponseEntity<ApiResponse<QuoteResponse>> getById(
            @Parameter(description = "Quote ID") @PathVariable String id) {
        QuoteResponse response = quoteService.getById(id);
        return ResponseEntity.ok(ApiResponse.<QuoteResponse>builder()
                .success(true)
                .message("Quote retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all quotes", description = "Retrieves all quotes with pagination and sorting")
    public ResponseEntity<ApiResponse<PageResponse<QuoteResponse>>> getAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        PageResponse<QuoteResponse> response = quoteService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<PageResponse<QuoteResponse>>builder()
                .success(true)
                .message("Quotes retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quote", description = "Updates an existing quote")
    public ResponseEntity<ApiResponse<QuoteResponse>> update(
            @Parameter(description = "Quote ID") @PathVariable String id,
            @Valid @RequestBody QuoteRequest request) {
        QuoteResponse response = quoteService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<QuoteResponse>builder()
                .success(true)
                .message("Quote updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quote", description = "Deletes a quote by its ID")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Quote ID") @PathVariable String id) {
        quoteService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Quote deleted successfully")
                .build());
    }
}
