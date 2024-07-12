package com.xrontech.web.domain.cart;

import com.xrontech.web.domain.product.Product;
import com.xrontech.web.domain.product.ProductDTO;
import com.xrontech.web.domain.product.ProductRepository;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.security.service.AuthService;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addToCart(Long productId, CartDTO cartDTO) {

        Product product = productRepository.findByIdAndStatusAndDelete(productId, true,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));
        Integer stockAvailability = product.getQty();

        if (stockAvailability < cartDTO.getQty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
        }

        Optional<Cart> optionalCart = cartRepository.findByProductIdAndUserId(productId, userService.getCurrentUser().getId());

        if (optionalCart.isPresent()) {
            // already have
            Cart cart = optionalCart.get();

            int newQty = cartDTO.getQty() + cart.getQty();
            if (stockAvailability < newQty) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
            }
            cart.setQty(newQty);
            cartRepository.save(cart);

            return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_QTY_UPDATED_TO_CART_SUCCESSFULLY", "Product qty updated to cart successfully!");
        } else {
            // not in cart
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setQty(cartDTO.getQty());
            cart.setUserId(userService.getCurrentUser().getId());
            cartRepository.save(cart);

            return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_ADDED_TO_CART_SUCCESSFULLY", "Product added to cart successfully!");
        }

    }

    public List<Cart> getAllFromCart() {

        List<Cart> cartList = cartRepository.findAllByUserId(userService.getCurrentUser().getId());
        if (cartList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_PRODUCT_FOUND_ON_CART", "No product found on cart");
        }
        return cartList;
    }

    public Cart getCartById(Long id) {

        return cartRepository.findByIdAndUserId(id, userService.getCurrentUser().getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
    }

    public List<Cart> getCartByProductId(Long productId) {

        List<Cart> cartList = cartRepository.findByUserIdAndProductId(userService.getCurrentUser().getId(),productId);
        if (cartList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_PRODUCT_FOUND_ON_CART", "No product found on cart");
        }
        return cartList;
    }

    public List<Cart> getCartByProductName(String productName) {

        List<Cart> cartList = cartRepository.findByUserIdAndProduct_NameContainsIgnoreCase(userService.getCurrentUser().getId(),productName);
        if (cartList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_PRODUCT_FOUND_ON_CART", "No product found on cart");
        }
        return cartList;
    }

    public ApplicationResponseDTO updateCart(Long cartId, CartDTO cartDTO) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CART_ID_NOT_FOUND", "Cart id not found"));

        Product product = productRepository.findByIdAndStatus(cart.getProductId(), true)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));
        Integer stockAvailability = product.getQty();

        // already have
        if (stockAvailability < cartDTO.getQty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
        }
        cart.setQty(cartDTO.getQty());
        cartRepository.save(cart);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_QTY_UPDATED_TO_CART_SUCCESSFULLY", "Product qty updated to cart successfully!");

    }

    public ApplicationResponseDTO deleteProductFromCart(Long id) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
        Cart cart = cartRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
        cartRepository.delete(cart);
        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_DELETED_FROM_CART_SUCCESSFULLY", "Product deleted from cart successfully!");
    }

}
