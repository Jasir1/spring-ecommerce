package com.xrontech.web.domain.order;

import com.xrontech.web.domain.cart.Cart;
import com.xrontech.web.domain.cart.CartDTO;
import com.xrontech.web.domain.cart.CartRepository;
import com.xrontech.web.domain.category.Category;
import com.xrontech.web.domain.category.CategoryRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    public ApplicationResponseDTO addOrder(OrderDTO orderDTO) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        Product product = productRepository.findByIdAndStatus(orderDTO.getProductId(), true)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_FOUND", "Product not found"));
        Integer stockAvailability = product.getQty();

        if (stockAvailability < orderDTO.getQty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PRODUCT_OUT_OF_STOCK", "Product out of stock");
        }

        Order order = new Order();
        order.setProductId(orderDTO.getProductId());
        order.setQty(orderDTO.getQty());
        order.setTotal(orderDTO.getQty() * product.getPrice());
        order.setUserId(user.getId());
        orderRepository.save(order);

        product.setQty(stockAvailability - order.getQty());
        productRepository.save(product);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "ORDER_ADDED_SUCCESSFULLY", "Order added successfully!");

    }

    public ApplicationResponseDTO cancelOrder(Long orderId) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found"));

        if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.CANCELLED).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_ALREADY_CANCELLED", "Order already cancelled!");
        }
        if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.PENDING).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_IS_PROCESSING_CANNOT_CANCEL_ORDER", "Order is processing. Cannot cancel the order!");
        }

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId()).get();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "ORDER_CANCELED_SUCCESSFULLY", "Order cancelled successfully!");
    }

    public List<Order> getAllOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        if (orderList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
        }
        return orderList;
    }

    public List<Order> ongoingOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        List<Order> orderList = orderRepository.findByStatusAndUserId(OrderStatus.ONGOING, user.getId());
        if (orderList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
        }
        return orderList;
    }

    public List<Order> pendingOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        List<Order> orderList = orderRepository.findByStatusAndUserId(OrderStatus.PENDING, user.getId());
        if (orderList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
        }
        return orderList;
    }

    public List<Order> canceledOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        List<Order> orderList = orderRepository.findByStatusAndUserId(OrderStatus.CANCELLED, user.getId());
        if (orderList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
        }
        return orderList;
    }

    public Order getOrderById(Long id) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
        return orderRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORDER_ID_NOT_FOUND", "Order id not found"));
    }


//    public ApplicationResponseDTO updateCart(Long id,CartDTO cartDTO) {
//        cartRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
//
//        Cart cart = cartRepository.findById(id).get();
//        cart.setQty(cartDTO.getQty());
//        cartRepository.save(cart);
//
//        return new ApplicationResponseDTO(HttpStatus.OK, "CART_UPDATED_SUCCESSFULLY", "Cart updated successfully!");
//    }

    public ApplicationResponseDTO deleteProductFromCart(Long id) {

        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
        cartRepository.delete(cart);
        return new ApplicationResponseDTO(HttpStatus.OK, "PRODUCT_DELETED_FROM_CART_SUCCESSFULLY", "Product deleted from cart successfully!");
    }

}
