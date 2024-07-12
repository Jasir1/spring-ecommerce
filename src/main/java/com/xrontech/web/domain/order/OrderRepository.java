package com.xrontech.web.domain.order;

import com.xrontech.web.domain.cart.Cart;
import com.xrontech.web.domain.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByProductIdAndUserId(Long productId, Long userId);

    //    Optional<Wishlist> findByNameIgnoreCase(String name);
//    Optional<Wishlist> findByIdAndStatus(Long id, boolean status);
    List<Order> findByProduct_NameContainsIgnoreCase(String product);

    //    Optional<Wishlist> findById(Long id);
    List<Order> findAllByUserId(Long userId);
    Optional<Order> findByIdAndUserIdAndStatusNot(Long orderId, Long userId, OrderStatus orderStatus);
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
    Optional<Order> findByIdAndUserIdAndStatus(Long orderId, Long userId, OrderStatus orderStatus);
    List<Order> findByStatusAndUserId(OrderStatus orderStatus, Long userId);
}
