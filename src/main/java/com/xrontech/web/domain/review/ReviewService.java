package com.xrontech.web.domain.review;

import com.xrontech.web.domain.product.Product;
import com.xrontech.web.domain.product.ProductRepository;
import com.xrontech.web.domain.rating.Rating;
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

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addReview(Long productId, ReviewDTO reviewDTO) {

        productRepository.findByIdAndStatusAndDelete(productId, true,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));

        if (reviewRepository.findByProductIdAndUserId(productId, userService.getCurrentUser().getId()).isPresent()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_ALREADY_RATED", "Product already rated!");
        }

        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userService.getCurrentUser().getId());
        review.setDescription(reviewDTO.getDescription());
        reviewRepository.save(review);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_REVIEWED_SUCCESSFULLY", "Product reviewed successfully!");
    }

//    public List<Review> getAllReview() {
//
//        List<Review> reviewList = reviewRepository.findAll();
//        if (reviewList.isEmpty()) {
//            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_REVIEW_FOUND", "No review found");
//        }
//        return reviewList;
//    }

    public Review getReviewById(Long id) {

        return reviewRepository.findByIdAndUserId(id, userService.getCurrentUser().getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_REVIEW_FOUND", "No review found"));
    }

    public List<Review> getReviewByProductId(Long productId) {

        List<Review> reviewList = reviewRepository.findAllByProductId(productId);

        if (reviewList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_REVIEW_FOUND_FOR_THE_PRODUCT", "No review found for the product");
        }
        return reviewList;
    }

//    public ApplicationResponseDTO updateCart(Long cartId, ReviewDTO reviewDTO) {
//        User user = userRepository.findByEmail(AuthService.getCurrentUser())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
//
//        Review review = reviewRepository.findByIdAndUserId(cartId, user.getId())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CART_ID_NOT_FOUND", "Cart id not found"));
//
//        Product product = productRepository.findByIdAndStatus(review.getProductId(), true)
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));
//        Integer stockAvailability = product.getQty();
//
//        // already have
//        if (stockAvailability < reviewDTO.getQty()) {
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
//        }
//        review.setQty(reviewDTO.getQty());
//        reviewRepository.save(review);
//
//        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_QTY_UPDATED_TO_CART_SUCCESSFULLY", "Product qty updated to cart successfully!");
//
//    }
//
//    public ApplicationResponseDTO deleteProductFromCart(Long id) {
//        User user = userRepository.findByEmail(AuthService.getCurrentUser())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
//        Review review = reviewRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
//        reviewRepository.delete(review);
//        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_DELETED_FROM_CART_SUCCESSFULLY", "Product deleted from cart successfully!");
//    }

}
