package com.xrontech.web.domain.admin;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "ecom")
public class AdminResource {
    private final AdminService adminService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.getUser(id));
    }

    @PutMapping("/change-user-status/{id}")
    public ResponseEntity<ApplicationResponseDTO> changeUserStatus(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.changeUserStatus(id));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    //order status update
}
