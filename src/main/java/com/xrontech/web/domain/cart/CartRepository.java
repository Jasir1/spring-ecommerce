package com.xrontech.web.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByProductIdAndUserId(Long productId, Long userId);
    List<Cart> findByUserIdAndProduct_NameContainsIgnoreCase(Long userId, String product);
    List<Cart> findByUserIdAndProductId(Long userId, Long productId);
    List<Cart> findAllByUserId(Long userId);
    Optional<Cart> findByIdAndUserId(Long id,Long userId);
}
