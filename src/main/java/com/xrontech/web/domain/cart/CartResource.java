package com.xrontech.web.domain.cart;

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
@RequestMapping("/api/v1/cart")
@SecurityRequirement(name = "ecom")
public class CartResource {

    private final CartService cartService;

    @PostMapping("/add/{product-id}")
    public ResponseEntity<ApplicationResponseDTO> addToCart(@PathVariable("product-id") Long productId, @Valid @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartService.addToCart(productId,cartDTO));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Cart>> getAllFromCart() {
        return ResponseEntity.ok(cartService.getAllFromCart());
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/get-by-product-id/{id}")
    public ResponseEntity<List<Cart>> getCartByProductId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.getCartByProductId(id));
    }

    @GetMapping("/get-by-product-name/{name}")
    public ResponseEntity<List<Cart>> getCartByProductName(@PathVariable("name") String name) {
        return ResponseEntity.ok(cartService.getCartByProductName(name));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateCart(@PathVariable("id") Long cartId, @Valid @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartService.updateCart(cartId,cartDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteProductFromCart(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.deleteProductFromCart(id));
    }
}
