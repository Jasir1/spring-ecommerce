package com.xrontech.web.domain.product;

import com.xrontech.web.domain.category.Category;
import com.xrontech.web.domain.category.CategoryRepository;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private static final String PRODUCT_NOT_FOUND_CODE = "PRODUCT_NOT_FOUND";
    private static final String PRODUCT_NOT_FOUND_MSG = "Product not found";

    public ApplicationResponseDTO addProduct(ProductDTO productDTO) {

        if (categoryRepository.findByIdAndStatus(productDTO.getCategoryId(), true).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_NOT_FOUND", "Category not found");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setQty(productDTO.getQty());
        product.setPrice(productDTO.getPrice());
        product.setStatus(true);
        product.setCondition(productDTO.getCondition());
        product.setCategoryId(productDTO.getCategoryId());
        product.setImagePath("");
        product.setUserId(userService.getCurrentUser().getId());
        productRepository.save(product);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_ADDED_SUCCESSFULLY", "Product added successfully!");
    }


    public ApplicationResponseDTO uploadProductImage(Long id, MultipartFile file) {
        Optional<Product> optionalProduct = productRepository.findByIdAndUserIdAndStatusAndDelete(id, userService.getCurrentUser().getId(), true, false);
        if (optionalProduct.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG);
        }

        Product product = optionalProduct.get();
        if (file.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SELECTED", "File not selected");
        }

        try {
            String projectRoot = System.getProperty("user.dir");
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                if (!(fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png"))) {
                    throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                }

                String newFileName = UUID.randomUUID() + fileExtension;
                String imagePath = "/uploads/product/" + newFileName;
                Path path = Paths.get(projectRoot + imagePath);
                File saveFile = new File(String.valueOf(path));
                file.transferTo(saveFile);
                product.setImagePath(newFileName);
                productRepository.save(product);
            } else {
                throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original file name not found");
            }
        } catch (IOException e) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not saved");
        }
        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_IMAGE_UPLOADED_SUCCESSFULLY", "Product image uploaded successfully!");
    }

    public ApplicationResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {

        Product product = productRepository.findByIdAndUserIdAndStatusAndDelete(id, userService.getCurrentUser().getId(), true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG));

        product.setName(productUpdateDTO.getName());
        product.setDescription(productUpdateDTO.getDescription());
        product.setCondition(productUpdateDTO.getCondition());
        product.setQty(productUpdateDTO.getQty());
        product.setPrice(productUpdateDTO.getPrice());
        productRepository.save(product);

        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_UPDATED_SUCCESSFULLY", "Product updated successfully!");
    }

    public ApplicationResponseDTO statusChange(Long id) {

        Product product = productRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(),false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG));
        product.setStatus(!product.getStatus().equals(true));
        productRepository.save(product);
        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_STATUS_CHANGED_SUCCESSFULLY", "Product status changed successfully!");
    }

    public ApplicationResponseDTO deleteProduct(Long id) {

        Product product = productRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG));
        product.setDelete(true);
        productRepository.save(product);
        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_DELETED_SUCCESSFULLY", "Product deleted successfully!");
    }

    //common
    public List<Product> getProducts() {
        return productRepository.findAllByStatusAndDeleteAndQtyNot(true, false,0);
    }

    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findByIdAndStatusAndDelete(productId, true, false);
        if (product.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG);
        }
        if (product.get().getQty() == 0){
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
        }

        return product.get();
    }

    public List<Product> getProductByName(String name) {
        return productRepository.findByStatusAndDeleteAndQtyNotAndNameContainsIgnoreCase(true, false,0, name);
    }

    public List<Product> getProductByCategory(String name) {
        Optional<Category> category = categoryRepository.findByStatusAndNameIgnoreCase(true, name);
        if (category.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_CATEGORY_FOUND", "No category found");
        }

        List<Product> productList = productRepository.findByStatusAndDeleteAndCategoryIdAndQtyNot(true, false, category.get().getId(),0);
        if (productList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG);
        }
        return productList;
    }
    public List<Product> getProductByCondition(Condition condition) {
        return productRepository.findByStatusAndDeleteAndConditionAndQtyNot(true, false, condition,0);
    }


    //own
    public List<Product> getOwnProducts() {
        return productRepository.findAllByUserIdAndDelete(userService.getCurrentUser().getId(), false);
    }
    public Product getOwnProductById(Long id) {
        return productRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG));
    }


    public List<Product> getOwnProductByName(String name) {
        return productRepository.findByUserIdAndDeleteAndNameContainsIgnoreCase(userService.getCurrentUser().getId(), false, name);
    }

    public List<Product> getOwnProductByCategory(String name) {
        Optional<Category> category = categoryRepository.findByStatusAndNameIgnoreCase(true, name);
        if (category.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_CATEGORY_FOUND", "No category found");
        }

        List<Product> productList = productRepository.findByDeleteAndUserIdAndCategoryId(false,userService.getCurrentUser().getId(), category.get().getId());
        if (productList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND_CODE, PRODUCT_NOT_FOUND_MSG);
        }
        return productList;
    }

    public List<Product> getOwnProductByCondition(Condition condition) {
        return productRepository.findByUserIdAndDeleteAndCondition(userService.getCurrentUser().getId(), false, condition);
    }
    public List<Product> getOwnProductByStatus(boolean status) {
        return productRepository.findByUserIdAndDeleteAndStatus(userService.getCurrentUser().getId(), false, status);
    }

}
