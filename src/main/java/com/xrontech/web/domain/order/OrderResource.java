package com.xrontech.web.domain.order;

import com.xrontech.web.domain.cart.Cart;
import com.xrontech.web.domain.cart.CartDTO;
import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@SecurityRequirement(name = "ecom")
public class OrderResource {

    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @PostMapping("/cancel-order/{order-id}")
    public ResponseEntity<ApplicationResponseDTO> cancelOrder(@PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/ongoing-orders")
    public ResponseEntity<List<Order>> ongoingOrders() {
        return ResponseEntity.ok(orderService.ongoingOrders());
    }

    @GetMapping("/pending-orders")
    public ResponseEntity<List<Order>> pendingOrders() {
        return ResponseEntity.ok(orderService.pendingOrders());
    }

    @GetMapping("/cancelled-orders")
    public ResponseEntity<List<Order>> canceledOrders() {
        return ResponseEntity.ok(orderService.canceledOrders());
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<ApplicationResponseDTO> updateCart(@PathVariable("id") Long id, @Valid @RequestBody CartDTO cartDTO) {
//        return ResponseEntity.ok(cartService.updateCart(id, cartDTO));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ApplicationResponseDTO> deleteProductFromCart(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(orderService.deleteProductFromCart(id));
//    }
}
