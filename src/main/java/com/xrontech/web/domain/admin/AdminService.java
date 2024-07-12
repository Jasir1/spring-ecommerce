package com.xrontech.web.domain.admin;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.entity.UserRole;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import com.xrontech.web.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAllByUserRole(UserRole.USER);
        if (users.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_USERS_FOUND", "No users found");
        }
        return users;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"));
    }

    public ApplicationResponseDTO changeUserStatus(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"));
        user.setStatus(!user.getStatus());
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_STATUS_CHANGED_SUCCESSFULLY", "User status changes successfully!");
    }

    public ApplicationResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"));
        user.setDelete(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_DELETED_SUCCESSFULLY", "User deleted successfully!");
    }

}
