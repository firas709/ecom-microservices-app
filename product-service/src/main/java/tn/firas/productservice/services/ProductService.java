package tn.firas.productservice.services;

import org.springframework.web.multipart.MultipartFile;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.ProductRequest;
import tn.firas.productservice.dto.response.ProductResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {


    Map<String, Object> createProduct(ProductRequest productDto, List<MultipartFile> images) throws IOException;
    Map<String, Object> updateProduct(Integer productId, ProductRequest req, List<MultipartFile> images) throws Exception;
    ProductResponse getProduct(Integer idProduct) throws IOException ;
    void deleteById(Integer productId) throws Exception;
    PageResponse<ProductResponse> getAllProducts(int page, int size);
}
