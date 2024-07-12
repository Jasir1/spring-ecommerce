package com.xrontech.web.domain.review;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@SecurityRequirement(name = "ecom")
public class ReviewResource {

    private final ReviewService reviewService;

    @PostMapping("/add/{product-id}")
    public ResponseEntity<ApplicationResponseDTO> addReview(@PathVariable("product-id") Long productId, @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.addReview(productId, reviewDTO));
    }

//    @GetMapping("/get-all")
//    public ResponseEntity<List<Review>> getAllReview() {
//        return ResponseEntity.ok(reviewService.getAllReview());
//    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/get-by-product/{product-id}")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(reviewService.getReviewByProductId(productId));
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<ApplicationResponseDTO> updateReview(@PathVariable("id") Long cartId, @Valid @RequestBody ReviewDTO reviewDTO) {
//        return ResponseEntity.ok(reviewService.updateCart(cartId, reviewDTO));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ApplicationResponseDTO> deleteReview(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(reviewService.deleteProductFromCart(id));
//    }
}
