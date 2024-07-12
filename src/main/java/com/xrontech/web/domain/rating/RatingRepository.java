package com.xrontech.web.domain.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    Optional<Rating> findByProductIdAndUserId(Long productId, Long userId);
    List<Rating> findAllByUserId(Long userId);
    List<Rating> findAllByProductId(Long productId);
    Optional<Rating> findByIdAndUserId(Long id, Long userId);
}
