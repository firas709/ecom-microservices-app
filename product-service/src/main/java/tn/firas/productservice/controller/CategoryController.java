package tn.firas.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.CategoryRequest;
import tn.firas.productservice.dto.response.CategoryResponse;
import tn.firas.productservice.services.CategoryService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequest request) {


        return categoryService.updateCategory(id, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategory(@PathVariable Integer id) {
        return categoryService.getCategory(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<CategoryResponse> getAllCategories(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return categoryService.getAllCategories(page,size);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getAllCategoriesWithoutPagination() {
        return categoryService.getAllCategoriesWithoutPagination();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }


    @GetMapping("/{categoryId}/product-count")
    public ResponseEntity<Integer> countProductsByCategory(@PathVariable Integer categoryId) {
        Integer productCount = categoryService.countProductsByCategory(categoryId);
        return ResponseEntity.ok(productCount);
    }

}
