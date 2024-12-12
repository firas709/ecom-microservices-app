package tn.firas.productservice.services;

import org.springframework.web.multipart.MultipartFile;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.ProductRequest;
import tn.firas.productservice.dto.response.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {


    String createProduct(ProductRequest productDto, List<MultipartFile> images) throws IOException;
    String updateProduct(Integer productId, ProductRequest req, List<MultipartFile> images) throws Exception;
    ProductResponse getProduct(Integer idProduct);
    PageResponse<ProductResponse> getAllProducts(int page, int size);
    void deleteById(Integer productId) throws Exception;
}
