package tn.firas.productservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.ProductMapper;
import tn.firas.productservice.dto.request.ProductRequest;
import tn.firas.productservice.dto.response.CategoryResponse;
import tn.firas.productservice.dto.response.ProductResponse;
import tn.firas.productservice.entities.Category;
import tn.firas.productservice.entities.Product;
import tn.firas.productservice.repositories.CategoryRepository;
import tn.firas.productservice.repositories.ProductRepository;
import tn.firas.productservice.services.ProductService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public String createProduct(ProductRequest productDto, List<MultipartFile> images) throws IOException {

        // Check if category If Not Exist
        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID:: " + productDto.categoryId()));

        // Create product
        Product product = productMapper.toProduct(productDto);

        Product savedProduct = productRepository.save(product);

        List<String> finalImagePaths = saveImages(images, savedProduct.getId());
        savedProduct.setImages(finalImagePaths);
        productRepository.save(savedProduct);
        return "product created successfully with id: " + savedProduct.getId();
    }

        @Override
    @Transactional
    public String updateProduct(Integer productId,ProductRequest req, List<MultipartFile> images) throws Exception {

        Product productExist= productRepository.findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found with ID:: "+productId));

        // Check if category If Not Exist
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID:: " + req.categoryId()));


        deleteImages(productExist.getImages());
        List<String> finalImagePaths = saveImages(images, productExist.getId());



        Product newProduct = new Product();
        newProduct.setId(productId);
        newProduct.setName(req.name());
        newProduct.setDescription(req.description());
        newProduct.setPrice(req.price());
        newProduct.setCategory(category);
        newProduct.setImages(finalImagePaths);
        newProduct.setIsActive(req.isActive());
        newProduct.setAvailableQuantity(req.availableQuantity());
        newProduct.setIsSold(req.isSold());

        productRepository.save(newProduct);

        return "product updated successfully with id: " + newProduct.getId();
    }

    @Override
    public ProductResponse getProduct(Integer idProduct) {

        return productRepository.findById(idProduct)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + idProduct));
    }


    @Override
    public PageResponse<ProductResponse> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAllProducts(pageable);

        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return new PageResponse<>(
                productResponses,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast()
        );
    }




    @Override
    public void deleteById(Integer productId) throws Exception {

        Product productExist= productRepository.findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found with ID:: "+productId));

        deleteImages(productExist.getImages());
        productRepository.deleteById(productId);


    }


    private List<String> saveImages(List<MultipartFile> images, Integer productId) throws IOException {
    List<String> finalImagePaths = new ArrayList<>();
    String projectPath = System.getProperty("user.dir");
    String productDir = projectPath + "/src/main/resources/static/images/products/" + productId;
    File dir = new File(productDir);
    if (!dir.exists()) {
        dir.mkdirs();
    }
    for (MultipartFile image : images) {
        String fileName = image.getOriginalFilename();
        String finalImagePath = productDir + "/" + fileName;


        Files.copy(image.getInputStream(), Paths.get(finalImagePath), StandardCopyOption.REPLACE_EXISTING);

        finalImagePaths.add("/images/products/" + productId + "/" + fileName);
    }
    return finalImagePaths;
}

    private void deleteImages(List<String> imagePaths) throws Exception {
    String projectPath = System.getProperty("user.dir");
    for (String imagePath : imagePaths) {
        String finalImagePath = projectPath + "/src/main/resources/static" + imagePath;
        try {
            Files.deleteIfExists(Paths.get(finalImagePath));
        } catch (IOException e) {
            throw new Exception("Failed to delete image: " + finalImagePath, e);
        }
    }
}
}
