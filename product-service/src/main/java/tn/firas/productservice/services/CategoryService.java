package tn.firas.productservice.services;

import org.springframework.transaction.annotation.Transactional;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.CategoryRequest;
import tn.firas.productservice.dto.response.CategoryResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, Object> createCategory(CategoryRequest request);

    Map<String, Object>  updateCategory(Integer id, CategoryRequest request);


    CategoryResponse getCategory(Integer id);

    PageResponse<CategoryResponse> getAllCategories(int page, int size);

    List<CategoryResponse> getAllCategoriesWithoutPagination();

    void deleteCategory(Integer id);

    Integer countProductsByCategory(Integer categoryId);
}
