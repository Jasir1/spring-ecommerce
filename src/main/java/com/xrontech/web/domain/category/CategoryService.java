package com.xrontech.web.domain.category;

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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String CATEGORY_NOT_FOUND_CODE = "CATEGORY_NOT_FOUND";
    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found";
    public ApplicationResponseDTO createCategory(CategoryDTO categoryDTO) {

        if (categoryRepository.findByNameIgnoreCase(categoryDTO.getName()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_ALREADY_EXIST", "Category already exist");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setStatus(true);
        categoryRepository.save(category);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "CATEGORY_CREATED_SUCCESSFULLY", "Category created successfully!");
    }

    public ApplicationResponseDTO uploadCategoryImage(Long id, MultipartFile file) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndStatus(id, true);

        if (optionalCategory.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG);
        }
        Category category = optionalCategory.get();
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
                String imagePath = "/uploads/category/" + newFileName;
                Path path = Paths.get(projectRoot + imagePath);
                File saveFile = new File(String.valueOf(path));
                file.transferTo(saveFile);
                category.setImagePath(newFileName);
                categoryRepository.save(category);
            } else {
                throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original file name not found");
            }
        } catch (IOException e) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not saved");
        }
        return new ApplicationResponseDTO(HttpStatus.CREATED, "CATEGORY_IMAGE_UPLOADED_SUCCESSFULLY", "Category image uploaded successfully!");
    }


    public List<Category> getCategories() {
        return categoryRepository.findAllByStatus(true);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {

        return categoryRepository.findByIdAndStatus(id,true).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));
    }

    public List<Category> getCategoryByName(String name) {
        return categoryRepository.findByStatusAndNameContainsIgnoreCase(true,name);
    }

    public ApplicationResponseDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findByIdAndStatus(id,true).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));

        categoryRepository.findByStatusAndNameIgnoreCase(true,categoryDTO.getName()).ifPresent(existingCategory -> {
            if (!category.getName().equals(categoryDTO.getName())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_NAME_ALREADY_EXISTS", "Category name already exists");
            }
        });

        category.setName(categoryDTO.getName());

        categoryRepository.save(category);

        return new ApplicationResponseDTO(HttpStatus.OK, "CATEGORY_UPDATED_SUCCESSFULLY", "Category updated successfully!");
    }

    public ApplicationResponseDTO statusChange(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));
        category.setStatus(!category.getStatus().equals(true));
        categoryRepository.save(category);
        return new ApplicationResponseDTO(HttpStatus.OK, "CATEGORY_STATUS_UPDATED_SUCCESSFULLY", "Category status updated successfully!");
    }

}
