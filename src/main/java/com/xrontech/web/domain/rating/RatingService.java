package com.xrontech.web.domain.rating;

import com.xrontech.web.domain.product.Product;
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

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addRating(Long productId, RatingDTO ratingDTO) {

        productRepository.findByIdAndStatusAndDelete(productId, true,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found!"));

        if (ratingRepository.findByProductIdAndUserId(productId, userService.getCurrentUser().getId()).isPresent()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_ALREADY_RATED", "Product already rated!");
        }

        Rating rating = new Rating();
        rating.setProductId(productId);
        rating.setUserId(userService.getCurrentUser().getId());
        rating.setStar(ratingDTO.getStar());
        ratingRepository.save(rating);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_RATED_SUCCESSFULLY", "Product rated successfully!");

    }

//    public List<Rating> getAllRatings() {
//        List<Rating> ratingList = ratingRepository.findAll();
//        if (ratingList.isEmpty()) {
//            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_RATING_FOUND", "No rating found!");
//        }
//        return ratingList;
//    }

    public List<Rating> getRatingByProduct(Long productId) {

        productRepository.findByIdAndStatusAndDelete(productId, true,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found!"));

        List<Rating> ratingList = ratingRepository.findAllByProductId(productId);
        if (ratingList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_RATING_FOUND_FOR_THE_PRODUCT", "No rating found for the product!");
        }
        return ratingList;
    }

    public Rating getRatingById(Long id) {

        return ratingRepository.findByIdAndUserId(id, userService.getCurrentUser().getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "RATING_ID_NOT_FOUND", "Rating id not found"));
    }


//    public ApplicationResponseDTO updateRating(Long id, RatingDTO ratingDTO) {
//        User user = userRepository.findByEmail(AuthService.getCurrentUser())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
//
//        Rating rating = ratingRepository.findByIdAndUserId(id, user.getId())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RATING_ID_NOT_FOUND", "Rating id not found"));
//
//        productRepository.findByIdAndStatus(rating.getProductId(), true)
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));
//
//        rating.setStar(ratingDTO.getStar());
//        ratingRepository.save(rating);
//
//        return new ApplicationResponseDTO(HttpStatus.CREATED, "PRODUCT_RATING_UPDATED_SUCCESSFULLY", "Product rating updated successfully!");
//
//    }

//    public ApplicationResponseDTO deleteProductFromCart(Long id) {
//        User user = userRepository.findByEmail(AuthService.getCurrentUser())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
//        Rating rating = ratingRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
//        ratingRepository.delete(rating);
//        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_DELETED_FROM_CART_SUCCESSFULLY", "Product deleted from cart successfully!");
//    }

}
