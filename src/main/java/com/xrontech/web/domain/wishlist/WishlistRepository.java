package com.xrontech.web.domain.wishlist;

import com.xrontech.web.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Optional<Wishlist> findByProductIdAndUserIdAndStatus(Long productId,Long userId,boolean status);
    Optional<Wishlist> findByIdAndUserIdAndStatus(Long id,Long userId,boolean status);
    List<Wishlist> findByUserIdAndStatusAndProductId(Long userId,boolean status,Long productId);
    List<Wishlist> findByUserIdAndStatusAndProduct_NameContainsIgnoreCase(Long userId,boolean status,String product);
    List<Wishlist> findAllByUserIdAndStatus(Long userId,boolean status);
}
