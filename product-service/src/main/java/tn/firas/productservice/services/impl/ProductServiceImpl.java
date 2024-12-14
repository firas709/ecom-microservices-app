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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public Map<String, Object> createProduct(ProductRequest productDto, List<MultipartFile> images) throws IOException {

        // Check if category If Not Exist
        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID:: " + productDto.categoryId()));

        // Create product
        Product product = productMapper.toProduct(productDto);

        Product savedProduct = productRepository.save(product);

        List<String> finalImagePaths = saveImages(images, savedProduct.getId());
        savedProduct.setImages(finalImagePaths);
        productRepository.save(savedProduct);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedProduct.getId());
        response.put("name", savedProduct.getName());
        response.put("message", "product created successfully with id: " + savedProduct.getId());
        response.put("timestamp", LocalDateTime.now());
        return response;

    }

        @Override
    @Transactional
    public Map<String, Object> updateProduct(Integer productId,ProductRequest req, List<MultipartFile> images) throws Exception {

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

        Product savedProduct = productRepository.save(newProduct);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedProduct.getId());
            response.put("name", savedProduct.getName());
            response.put("message", "product updated successfully with id: " + newProduct.getId());
            response.put("timestamp", LocalDateTime.now());
            return response;


    }

    @Override
    public ProductResponse getProduct(Integer idProduct) throws IOException {

        Product found =  productRepository.findById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + idProduct));


        List<byte[]> images = new ArrayList<>();
        for (String imagePath : found.getImages()) {
            String projectPath = System.getProperty("user.dir");
            byte[] loadedImage = null;
            String finalImagePath = projectPath + "/src/main/resources/static" + imagePath;
            loadedImage = loadImageB64(finalImagePath);
            images.add(loadedImage);
        }


        return new ProductResponse(
                found.getId(),
                found.getName(),
                found.getDescription(),
                found.getAvailableQuantity(),
                images,found.getPrice(),
                found.getPriceSold(),
                new CategoryResponse(found.getCategory().getId(),found.getCategory().getName(),found.getCategory().getIsActive(),found.getCategory().getCreatedDate()),
                found.getIsActive(),found.getIsSold()
        );
    }







    @Override
    public void deleteById(Integer productId) throws Exception {

        Product productExist= productRepository.findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found with ID:: "+productId));

        deleteImages(productExist.getImages());
        productRepository.deleteById(productId);


    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent().stream().map(product -> {
            List<byte[]> images = new ArrayList<>();
            for (String imagePath : product.getImages()) {
                try {
                    String projectPath = System.getProperty("user.dir");
                    String finalImagePath = projectPath + "/src/main/resources/static" + imagePath;
                    images.add(loadImageB64(finalImagePath));
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getAvailableQuantity(),
                    images,
                    product.getPrice(),
                    product.getPriceSold(),
                    new CategoryResponse(
                            product.getCategory().getId(),
                            product.getCategory().getName(),
                            product.getCategory().getIsActive(),
                            product.getCategory().getCreatedDate()
                    ),
                    product.getIsActive(),
                    product.getIsSold()
            );
        }).toList();

        return PageResponse.<ProductResponse>builder()
                .content(productResponses)
                .number(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .build();
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

    private byte[] loadImageB64(String imagePath) throws IOException {

        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }
}
