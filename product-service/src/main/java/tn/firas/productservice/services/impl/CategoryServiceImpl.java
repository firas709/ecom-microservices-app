package tn.firas.productservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.CategoryMapper;
import tn.firas.productservice.dto.request.CategoryRequest;
import tn.firas.productservice.dto.response.CategoryResponse;
import tn.firas.productservice.entities.Category;
import tn.firas.productservice.exception.CategoryAlreadyExistsException;
import tn.firas.productservice.repositories.CategoryRepository;
import tn.firas.productservice.services.CategoryService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Map<String, Object> createCategory(CategoryRequest request) {
        // Validate unique category name
        if (categoryRepository.existsByName(request.categoryName())) {
            throw new CategoryAlreadyExistsException("Category with name " + request.categoryName() + " already exists");
        }

        Category category = categoryMapper.toCategory(request);
        Category savedCategory = categoryRepository.save(category);

        Map<String, Object> response = new HashMap<>();
        response.put("id", category.getId());
        response.put("name", category.getName());
        response.put("message", "Category created successfully");
        response.put("timestamp", LocalDateTime.now());
        return response;
    }



    @Override
    public Map<String, Object>  updateCategory(Integer id, CategoryRequest request) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        existingCategory.setIsActive(request.categoryIsActive());

        // Validate unique category name
        if (categoryRepository.existsByName(request.categoryName()) && !existingCategory.getName().equals(request.categoryName())) {
            throw new CategoryAlreadyExistsException("Category with name " + request.categoryName() + " already exists");
        }

        existingCategory.setName(request.categoryName());


        Category updatedCategory = categoryRepository.save(existingCategory);
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedCategory.getId());
        response.put("name", updatedCategory.getName());
        response.put("message", "Category updated successfully");
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    @Override
    public CategoryResponse getCategory(Integer id) {

        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }


    @Override
    public PageResponse<CategoryResponse> getAllCategories(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Category> categories = categoryRepository.findAllCategories(pageable);

        List<CategoryResponse> categoryResponses = categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
        return new PageResponse<>(
                categoryResponses,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isFirst(),
                categories.isLast()
        );
    }


    @Override
    public List<CategoryResponse> getAllCategoriesWithoutPagination() {


        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();

    }



    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }

    public Integer countProductsByCategory(Integer categoryId) {
        return categoryRepository.countProductsByCategoryId(categoryId);
    }



}
