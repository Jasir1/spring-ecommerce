package com.xrontech.web.domain.user;

import com.xrontech.web.domain.security.domain.UserData;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ApplicationResponseDTO updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        userRepository.findByMobile(userUpdateDTO.getMobile()).ifPresent(existingUser -> {
            if (!user.getMobile().equals(userUpdateDTO.getMobile())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_ALREADY_EXISTS", "Mobile already exists");
            }
        });

        user.setName(userUpdateDTO.getName());
        user.setPrimaryAddress(userUpdateDTO.getPrimaryAddress());
        if (userUpdateDTO.getSecondaryAddress()==null){
            user.setSecondaryAddress(null);
        }
        if (userUpdateDTO.getSecondaryAddress()!=null && !userUpdateDTO.getSecondaryAddress().isEmpty()){
            user.setSecondaryAddress(userUpdateDTO.getSecondaryAddress());
        }
        user.setMobile(userUpdateDTO.getMobile());

        userRepository.save(user);

        return new ApplicationResponseDTO(HttpStatus.OK, "USER_UPDATED_SUCCESSFULLY!", "User updated successfully!");
    }

    public ApplicationResponseDTO updateProfilePic(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "FILE_NOT_SELECTED", "File not selected");
        } else {
            try {
                String projectRoot = System.getProperty("user.dir");
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    if (!(fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png"))) {
                        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                    }

                    String newFileName = UUID.randomUUID() + fileExtension;
                    String imagePath = "/uploads/user/" + newFileName;
                    Path path = Paths.get(projectRoot + imagePath);
                    File saveFile = new File(String.valueOf(path));
                    file.transferTo(saveFile);
                    User user = getCurrentUser();
                    user.setImageURL(newFileName);
                    userRepository.save(user);
                    return new ApplicationResponseDTO(HttpStatus.CREATED, "IMAGE_UPLOADED_SUCCESSFULLY", "Image uploaded successfully!");
                } else {
                    throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original file name not found");
                }
            } catch (IOException e) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not saved");
            }
        }
    }

    public User getCurrentUser() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            if (securityContext != null && securityContext.getAuthentication() != null) {
                Object principal = securityContext.getAuthentication().getPrincipal();
                if (principal instanceof UserData userData) {
                    String email = userData.getUsername();
                    Optional<User> optionalUser = userRepository.findByEmail(email);
                    if (optionalUser.isPresent()) {
                        return optionalUser.get();
                    } else {
                        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "User not found");
                    }
                } else {
                    throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_PRINCIPAL", "Invalid Principal");
                }
            } else {
                throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "SECURITY_CONTEXT_IS_NULL", "Security context is null");
            }
        } catch (Exception e) {
            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_USER", e.getMessage());
        }
    }

    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        } else {
            throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "User not found");
        }
    }
}