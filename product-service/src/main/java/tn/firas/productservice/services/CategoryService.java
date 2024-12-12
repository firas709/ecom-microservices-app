package tn.firas.productservice.services;

import org.springframework.transaction.annotation.Transactional;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.CategoryRequest;
import tn.firas.productservice.dto.response.CategoryResponse;

public interface CategoryService {
    String createCategory(CategoryRequest request);

    String updateCategory(Integer id, CategoryRequest request);


    CategoryResponse getCategory(Integer id);

    PageResponse<CategoryResponse> getAllCategories(int page, int size);

    void deleteCategory(Integer id);
}
