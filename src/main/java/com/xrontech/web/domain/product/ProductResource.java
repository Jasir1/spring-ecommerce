package com.xrontech.web.domain.product;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@SecurityRequirement(name = "ecom")
public class ProductResource {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    @PostMapping("/upload-image/{id}")
    public ResponseEntity<ApplicationResponseDTO> uploadProductImage(@PathVariable("id") Long id, MultipartFile file) {
        return ResponseEntity.ok(productService.uploadProductImage(id,file));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
    }

    @PutMapping("/status-change/{id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.statusChange(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    //common
    @GetMapping("/get")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<List<Product>> getProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping("/get/category/{name}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getProductByCategory(name));
    }
    @GetMapping("/get/condition/{condition}")
    public ResponseEntity<List<Product>> getProductByCondition(@PathVariable("condition") Condition condition) {
        return ResponseEntity.ok(productService.getProductByCondition(condition));
    }

    //own
    @GetMapping("/get-own")
    public ResponseEntity<List<Product>> getOwnProducts() {
        return ResponseEntity.ok(productService.getOwnProducts());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Product> getOwnProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getOwnProductById(id));
    }
    @GetMapping("/get-own/name/{name}")
    public ResponseEntity<List<Product>> getOwnProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getOwnProductByName(name));
    }

    @GetMapping("/get-own/category/{name}")
    public ResponseEntity<List<Product>> getOwnProductByCategory(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getOwnProductByCategory(name));
    }

    @GetMapping("/get-own/condition/{condition}")
    public ResponseEntity<List<Product>> getOwnProductByCondition(@PathVariable("condition") Condition condition) {
        return ResponseEntity.ok(productService.getOwnProductByCondition(condition));
    }

    @GetMapping("/get-own/status/{status}")
    public ResponseEntity<List<Product>> getOwnProductByStatus(@PathVariable("status") boolean status) {
        return ResponseEntity.ok(productService.getOwnProductByStatus(status));
    }


}
