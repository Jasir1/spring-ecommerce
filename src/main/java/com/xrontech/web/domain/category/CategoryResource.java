package com.xrontech.web.domain.category;

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
@RequestMapping("/api/v1/category")
@SecurityRequirement(name = "ecom")
public class CategoryResource {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApplicationResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }
    @PostMapping("/upload-image/{id}")
    public ResponseEntity<ApplicationResponseDTO> uploadCategoryImage(@PathVariable("id") Long id, MultipartFile file) {
        return ResponseEntity.ok(categoryService.uploadCategoryImage(id,file));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<List<Category>> getCategoryByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id,categoryDTO));
    }

    @PutMapping("/status-change/{id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.statusChange(id));
    }
}
