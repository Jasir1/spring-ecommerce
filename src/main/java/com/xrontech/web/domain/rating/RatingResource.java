package com.xrontech.web.domain.rating;

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
@RequestMapping("/api/v1/rating")
@SecurityRequirement(name = "ecom")
public class RatingResource {

    private final RatingService ratingService;

    @PostMapping("/add/{product-id}")
    public ResponseEntity<ApplicationResponseDTO> addRating(@PathVariable("product-id") Long productId, @Valid @RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(ratingService.addRating(productId, ratingDTO));
    }

//    @GetMapping("/get-all")
//    public ResponseEntity<List<Rating>> getAllRatings() {
//        return ResponseEntity.ok(ratingService.getAllRatings());
//    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @GetMapping("/get-by-product/{product-id}")
    public ResponseEntity<List<Rating>> getRatingByProduct(@PathVariable("product-id") Long productId) {
        return ResponseEntity.ok(ratingService.getRatingByProduct(productId));
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<ApplicationResponseDTO> updateRating(@PathVariable("id") Long id, @Valid @RequestBody RatingDTO ratingDTO) {
//        return ResponseEntity.ok(ratingService.updateRating(id, ratingDTO));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ApplicationResponseDTO> deleteProductFromCart(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(ratingService.deleteProductFromCart(id));
//    }
}
