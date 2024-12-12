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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public String createCategory(CategoryRequest request) {
        // Validate unique category name
        if (categoryRepository.existsByName(request.categoryName())) {
            throw new CategoryAlreadyExistsException("Category with name " + request.categoryName() + " already exists");
        }

        Category category = categoryMapper.toCategory(request);
        Category savedCategory = categoryRepository.save(category);
        return "Category created successfully with id: " + savedCategory.getId();
    }

    @Override
    public String updateCategory(Integer id, CategoryRequest request) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // Validate unique category name
        if (categoryRepository.existsByName(request.categoryName())) {
            throw new CategoryAlreadyExistsException("Category with name " + request.categoryName() + " already exists");
        }

        existingCategory.setName(request.categoryName());
        existingCategory.setDescription(request.categoryDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return "Category updated successfully with id: " + updatedCategory.getId();
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
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }



}
