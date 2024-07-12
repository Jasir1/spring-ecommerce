package com.xrontech.web.domain.review;

import com.xrontech.web.domain.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
    List<Review> findByUserIdAndProductId(Long userId, Long productId);
    List<Review> findAllByUserId(Long userId);
    List<Review> findAllByProductId(Long productId);
    Optional<Review> findByIdAndUserId(Long id, Long userId);
}
