package com.xrontech.web.domain.wishlist;

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
@RequestMapping("/api/v1/wishlist")
@SecurityRequirement(name = "ecom")
public class WishlistResource {

    private final WishlistService wishlistService;

    @PostMapping("/add/{product-id}")
    public ResponseEntity<ApplicationResponseDTO> addToWishlist(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(productId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Wishlist>> getAllFromWishlist() {
        return ResponseEntity.ok(wishlistService.getAllFromWishlist());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wishlistService.getWishlistById(id));
    }

    @GetMapping("/get-by-product-id/{id}")
    public ResponseEntity<List<Wishlist>> getWishlistByProductId(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(wishlistService.getWishlistByProductId(productId));
    }
    @GetMapping("/get-by-product-name/{name}")
    public ResponseEntity<List<Wishlist>> getWishlistByProductName(@PathVariable("name") String name) {
        return ResponseEntity.ok(wishlistService.getWishlistByProductName(name));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteProductFromWishlist(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wishlistService.deleteProductFromWishlist(id));
    }
}
