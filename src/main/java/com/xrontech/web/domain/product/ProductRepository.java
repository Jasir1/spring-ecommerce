package com.xrontech.web.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByNameIgnoreCase(String name);
    List<Product> findByStatusAndDeleteAndCategoryIdAndQtyNot(boolean status,boolean deleted, Long categoryId,int qty);
    List<Product> findByDeleteAndUserIdAndCategoryId(boolean deleted,Long userId, Long categoryId);
    Optional<Product> findByIdAndStatus(Long id, boolean status);
    Optional<Product> findByIdAndStatusAndDelete(Long id, boolean status, boolean deleted);
    Optional<Product> findByIdAndUserIdAndStatusAndDelete(Long id,Long userId, boolean status,boolean deleted);
    List<Product> findByUserIdAndDeleteAndCondition(Long userId, boolean deleted,Condition condition);
    Optional<Product> findByIdAndUserIdAndDelete(Long id,Long userId,boolean deleted);
    List<Product> findByStatusAndDeleteAndQtyNotAndNameContainsIgnoreCase(boolean status,boolean deleted,int qty, String name);
    List<Product> findByUserIdAndDeleteAndNameContainsIgnoreCase(Long userId,boolean deleted, String name);
    List<Product> findAllByStatusAndDeleteAndQtyNot(boolean status,boolean deleted,int qty);
    List<Product> findByIdAndQty(Long productId,int qty);
    List<Product> findAllByUserIdAndDelete(Long userId,boolean deleted);
    List<Product> findByStatusAndDeleteAndConditionAndQtyNot(boolean status, boolean deleted, Condition condition,int qty);

    List<Product> findByUserIdAndDeleteAndStatus(Long id, boolean deleted, boolean status);
}
