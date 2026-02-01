package sri.karthikeya.caterers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sri.karthikeya.caterers.dto.request.MenuRequest;
import sri.karthikeya.caterers.dto.response.ApiResponse;
import sri.karthikeya.caterers.dto.response.MenuResponse;
import sri.karthikeya.caterers.dto.response.PageResponse;
import sri.karthikeya.caterers.service.MenuService;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Menu management APIs")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    @Operation(summary = "Create a new menu item", description = "Creates a new menu item with the provided details")
    public ResponseEntity<ApiResponse<MenuResponse>> create(@Valid @RequestBody MenuRequest request) {
        MenuResponse response = menuService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<MenuResponse>builder()
                        .success(true)
                        .message("Menu created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu by ID", description = "Retrieves a menu item by its unique identifier")
    public ResponseEntity<ApiResponse<MenuResponse>> getById(
            @Parameter(description = "Menu ID") @PathVariable String id) {
        MenuResponse response = menuService.getById(id);
        return ResponseEntity.ok(ApiResponse.<MenuResponse>builder()
                .success(true)
                .message("Menu retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all menus", description = "Retrieves all menu items with pagination and sorting")
    public ResponseEntity<ApiResponse<PageResponse<MenuResponse>>> getAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        PageResponse<MenuResponse> response = menuService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<PageResponse<MenuResponse>>builder()
                .success(true)
                .message("Menus retrieved successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu", description = "Updates an existing menu item")
    public ResponseEntity<ApiResponse<MenuResponse>> update(
            @Parameter(description = "Menu ID") @PathVariable String id,
            @Valid @RequestBody MenuRequest request) {
        MenuResponse response = menuService.update(id, request);
        return ResponseEntity.ok(ApiResponse.<MenuResponse>builder()
                .success(true)
                .message("Menu updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu", description = "Deletes a menu item by its ID")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Menu ID") @PathVariable String id) {
        menuService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Menu deleted successfully")
                .build());
    }
}
